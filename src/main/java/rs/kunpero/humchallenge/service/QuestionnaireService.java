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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class QuestionnaireService {
    private static final Logger log = LoggerFactory.getLogger(QuestionnaireService.class);

    private static Map<String, QuestionnaireRecord> records = new ConcurrentHashMap<>();

    private final ExternalService externalService;

    public QuestionnaireService(ExternalService externalService) {
        this.externalService = externalService;
    }

    public UpdateQuestionResponseDto updateQuestion(UpdateQuestionRequestDto requestDto) {
        int questionIndex = requestDto.getQuestionIndex();
        int optionIndex = requestDto.getOptionIndex();

        QuestionnaireRecord record = records.get(requestDto.getUser());

        if (record == null) {
            log.warn("No record for current user [{}]", requestDto.getUser());
            return null;
        }

        List<QuestionDto> questions = record.getQuestions();
        if (questionIndex >= questions.size() || questionIndex < 0) {
            log.warn("No question with that index [{}]", questionIndex);
            return new UpdateQuestionResponseDto()
                    .setQuestions(questions);
        }

        List<OptionDto> options = questions.get(questionIndex).getOptions();
        updateOptions(options, optionIndex);

        log.info("Question with index [{}] was successfully updated", questionIndex);
        return new UpdateQuestionResponseDto()
                .setQuestions(questions);
    }

    public QueryQuestionResponseDto queryQuestion(QueryQuestionRequestDto requestDto) throws ExternalServiceException {
        String user = requestDto.getUser();
        QuestionnaireRecord record = records.get(user);

        records.putIfAbsent(user, new QuestionnaireRecord());

        List<QuestionDto> questions = record.getQuestions();
        int nextIndex = !questions.isEmpty() ? questions.get(questions.size() - 1).getIndex() + 1 : 0;

        QueryQuestionRequest request = new QueryQuestionRequest(nextIndex);
        QueryQuestionResponse response = wrapExternalMethod(externalService::queryQuestion, request);
        return addNewQuestion(questions, response);
    }

    public SubmitResponseDto submit(SubmitRequestDto requestDto) throws SubmitException, ExternalServiceException {
        String user = requestDto.getUser();
        QuestionnaireRecord record = records.get(user);

        List<Answer> answers = record.getQuestions().stream()
                .map(q -> {
                    var option = q.getOptions().stream()
                            .filter(OptionDto::isSelected)
                            .findFirst()
                            .orElseThrow(() -> new SubmitException("Option is not selected"));

                    var optionIndex = option.getIndex();
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

    private QueryQuestionResponseDto addNewQuestion(List<QuestionDto> questions, QueryQuestionResponse response) {
        QuestionDto newQuestion = new QuestionDto()
                .setIndex(questions.size())
                .setDescription(response.getDescription())
                .setOptions(response.getOptions().stream()
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
        if (selectedOptionIndex > options.size() - 1) {
            log.warn("No option with that index [{}]", selectedOptionIndex);
            return options;
        }

        var updatedOptions = options.stream()
                .map(o -> o.setSelected(false))
                .collect(toList());

        updatedOptions.get(selectedOptionIndex).setSelected(true);
        return updatedOptions;
    }
}
