package rs.kunpero.humchallenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rs.kunpero.humchallenge.integration.ExternalService;
import rs.kunpero.humchallenge.integration.dto.Answer;
import rs.kunpero.humchallenge.integration.dto.QueryQuestionRequest;
import rs.kunpero.humchallenge.integration.dto.QueryQuestionResponse;
import rs.kunpero.humchallenge.integration.dto.SubmitQuestionnaireRequest;
import rs.kunpero.humchallenge.integration.dto.SubmitQuestionnaireResponse;
import rs.kunpero.humchallenge.integration.dto.UpdateQuestionRequestDto;
import rs.kunpero.humchallenge.integration.dto.UpdateQuestionResponseDto;
import rs.kunpero.humchallenge.service.dto.OptionDto;
import rs.kunpero.humchallenge.service.dto.QueryQuestionRequestDto;
import rs.kunpero.humchallenge.service.dto.QueryQuestionResponseDto;
import rs.kunpero.humchallenge.service.dto.QuestionDto;
import rs.kunpero.humchallenge.service.dto.QuestionnaireRecord;
import rs.kunpero.humchallenge.service.dto.SubmitRequestDto;
import rs.kunpero.humchallenge.service.dto.SubmitResponseDto;
import rs.kunpero.humchallenge.util.exception.ExternalServiceException;
import rs.kunpero.humchallenge.util.exception.SubmitException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class QuestionnaireService {
    private static final Logger log = LoggerFactory.getLogger(QuestionnaireService.class);

    private final Map<String, QuestionnaireRecord> records;

    private final ExternalService externalService;

    public QuestionnaireService(ExternalService externalService) {
        this.externalService = externalService;
        this.records = new ConcurrentHashMap<>();
    }

    QuestionnaireService(Map<String, QuestionnaireRecord> records, ExternalService externalService) {
        this.records = records;
        this.externalService = externalService;
    }

    public UpdateQuestionResponseDto updateQuestion(UpdateQuestionRequestDto requestDto) {
        int questionIndex = requestDto.getQuestionIndex();
        int optionIndex = requestDto.getOptionIndex();

        QuestionnaireRecord record = records.get(requestDto.getUser());

        if (record == null) {
            log.warn("No record for current user [{}]", requestDto.getUser());
            return new UpdateQuestionResponseDto()
                    .setQuestions(new ArrayList<>());
        }

        List<QuestionDto> questions = record.getQuestions();

        Optional<QuestionDto> optionalQuestion = record.getQuestion(questionIndex);
        if (optionalQuestion.isEmpty()) {
            log.warn("No question with that index [{}]", questionIndex);
            return new UpdateQuestionResponseDto()
                    .setQuestions(questions);
        }

        QuestionDto question = optionalQuestion.get();
        List<OptionDto> options = question.getOptions();
        question.setOptions(updateOptions(options, optionIndex));

        return new UpdateQuestionResponseDto()
                .setQuestions(questions);
    }

    public QueryQuestionResponseDto queryQuestion(QueryQuestionRequestDto requestDto) throws ExternalServiceException {
        String user = requestDto.getUser();

        records.putIfAbsent(user, new QuestionnaireRecord());
        QuestionnaireRecord record = records.get(user);

        List<QuestionDto> questions = record.getQuestions();

        if (!record.isHasNext()) {
            log.info("No more questions available");
            return new QueryQuestionResponseDto()
                    .setHasNext(record.isHasNext())
                    .setQuestions(questions);
        }

        int nextIndex = !questions.isEmpty() ? questions.get(questions.size() - 1).getIndex() + 1 : 0;

        QueryQuestionRequest request = new QueryQuestionRequest(nextIndex);
        QueryQuestionResponse response = wrapExternalMethod(externalService::queryQuestion, request);
        return addNewQuestion(questions, response, nextIndex);
    }

    public SubmitResponseDto submit(SubmitRequestDto requestDto) throws SubmitException, ExternalServiceException {
        String user = requestDto.getUser();
        QuestionnaireRecord record = records.get(user);

        List<Answer> answers = record.getQuestions().stream()
                .map(q -> {
                    var selectedOptions = q.getOptions().stream()
                            .filter(OptionDto::isSelected)
                            .collect(toList());

                    if (selectedOptions.size() != 1) {
                        log.warn("Incorrect selected options count");
                        throw new SubmitException(String.format("Incorrect selected options count for question [%s]", q.getIndex()));
                    }

                    var optionIndex = selectedOptions.get(0).getIndex();
                    return new Answer()
                            .setQuestionIndex(q.getIndex())
                            .setOptionIndex(optionIndex);
                })
                .collect(toList());

        SubmitQuestionnaireResponse response = wrapExternalMethod(externalService::submitQuestionnaire, new SubmitQuestionnaireRequest(answers));
        return new SubmitResponseDto()
                .setSuccessful(response.isSuccessful())
                .setResultDescription(response.getResultDescription());
    }

    private <T, R> R wrapExternalMethod(Function<T, R> method, T request) throws ExternalServiceException {
        try {
            return method.apply(request);
        } catch (Exception ex) {
            log.error("Unexpected external error [{}]", ex.getMessage());
            throw new ExternalServiceException(ex.getMessage());
        }
    }

    private QueryQuestionResponseDto addNewQuestion(List<QuestionDto> questions, QueryQuestionResponse response, int nextIndex) {
        if (response.getQuestion() == null) {
            log.warn("No question with index [{}]", nextIndex);
            return new QueryQuestionResponseDto()
                    .setHasNext(response.isHasNext())
                    .setQuestions(questions);
        }
        QuestionDto newQuestion = new QuestionDto()
                .setIndex(questions.size())
                .setDescription(response.getQuestion().getDescription())
                .setOptions(response.getQuestion().getOptions().stream()
                        .map(o -> new OptionDto()
                                .setIndex(o.getIndex())
                                .setSelected(false)
                                .setDescription(o.getDescription()))
                        .collect(toList()));
        questions.add(newQuestion);
        return new QueryQuestionResponseDto()
                .setHasNext(response.isHasNext())
                .setQuestions(questions);
    }

    private List<OptionDto> updateOptions(List<OptionDto> options, int selectedOptionIndex) {
        if (options.stream().filter(o -> o.getIndex() == selectedOptionIndex).findFirst().isEmpty()) {
            log.warn("No option with that index [{}]", selectedOptionIndex);
            return options;
        }

        var updatedOptions = options.stream()
                .map(o -> new OptionDto()
                        .setIndex(o.getIndex())
                        .setDescription(o.getDescription())
                        .setSelected(o.getIndex() == selectedOptionIndex))
                .collect(toList());

        log.info("Question with index [{}] was successfully updated", selectedOptionIndex);
        return updatedOptions;
    }
}
