package rs.kunpero.humchallenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rs.kunpero.humchallenge.integration.ExternalService;
import rs.kunpero.humchallenge.integration.dto.Answer;
import rs.kunpero.humchallenge.integration.dto.QueryQuestionRequest;
import rs.kunpero.humchallenge.integration.dto.QueryQuestionResponse;
import rs.kunpero.humchallenge.integration.dto.SubmitQuestionnaireRequest;
import rs.kunpero.humchallenge.integration.dto.SubmitQuestionnaireResponse;
import rs.kunpero.humchallenge.service.dto.InitQuestionnaireRequestDto;
import rs.kunpero.humchallenge.service.dto.InitQuestionnaireResponseDto;
import rs.kunpero.humchallenge.service.dto.OptionDto;
import rs.kunpero.humchallenge.service.dto.QueryQuestionRequestDto;
import rs.kunpero.humchallenge.service.dto.QueryQuestionResponseDto;
import rs.kunpero.humchallenge.service.dto.QuestionDto;
import rs.kunpero.humchallenge.service.dto.QuestionnaireRecord;
import rs.kunpero.humchallenge.service.dto.SubmitRequestDto;
import rs.kunpero.humchallenge.service.dto.SubmitResponseDto;
import rs.kunpero.humchallenge.util.exception.ExternalServiceException;
import rs.kunpero.humchallenge.util.exception.NoUserSessionException;
import rs.kunpero.humchallenge.util.exception.SubmitException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class QuestionnaireService {
    private static final Logger log = LoggerFactory.getLogger(QuestionnaireService.class);

    private static Map<String, QuestionnaireRecord> questions = new ConcurrentHashMap<>();

    private final ExternalService externalService;

    public QuestionnaireService(ExternalService externalService) {
        this.externalService = externalService;
    }

    public InitQuestionnaireResponseDto init(InitQuestionnaireRequestDto requestDto) {
        questions.put(requestDto.getUser(), new QuestionnaireRecord());
        return new InitQuestionnaireResponseDto()
                .setQuestionnaireDescription("Programming Challenge for the Role of Java Developer!");
    }

    public QueryQuestionResponseDto queryQuestion(QueryQuestionRequestDto requestDto) throws NoUserSessionException, IllegalArgumentException, ExternalServiceException {
        String user = requestDto.getUser();
        QuestionnaireRecord record = questions.get(user);

        checkSession(record);

        Integer questionIndex = requestDto.getQuestionIndex();
        List<QuestionDto> questions = record.getQuestions();

        if (questionIndex == null) {
            if (!questions.isEmpty()) {
                return new QueryQuestionResponseDto()
                        .setHasNext(record.isHasNext())
                        .setQuestion(questions.get(0));
            }
            QueryQuestionRequest request = new QueryQuestionRequest(null);
            return addNewQuestion(questions, externalService.queryQuestion(request));
        }

        if (questionIndex >= questions.size() || questionIndex < 0) {
            throw new IllegalArgumentException("No question with that index");
        }

        QuestionDto question = questions.get(questionIndex);
        if (!record.isHasNext() || questionIndex != questions.size() - 1) {
            var options = question.getOptions();
            var updatedOptions = updateOptions(options, requestDto.getSelectedOptionIndex());

            question.setOptions(updatedOptions);
            return new QueryQuestionResponseDto()
                    .setHasNext(record.isHasNext())
                    .setQuestion(question);
        }

        QueryQuestionRequest request = new QueryQuestionRequest(question.getUuid());
        QueryQuestionResponse response = wrapExternalMethod(externalService::queryQuestion, request);
        return addNewQuestion(questions, response);
    }

    public SubmitResponseDto submit(SubmitRequestDto requestDto) throws NoUserSessionException, SubmitException, ExternalServiceException {
        String user = requestDto.getUser();
        QuestionnaireRecord record = questions.get(user);

        checkSession(record);

        List<Answer> answers = record.getQuestions().stream()
                .map(q -> {
                    var option = q.getOptions().stream()
                            .filter(OptionDto::isSelected)
                            .findFirst()
                            .orElseThrow(() -> new SubmitException("Option is not selected"));

                    var optionUuid = option.getUuid();
                    return new Answer()
                            .setQuestionUuid(q.getUuid())
                            .setOptionUuid(optionUuid);
                })
                .collect(toList());

        SubmitQuestionnaireResponse response = wrapExternalMethod(externalService::submitQuestionary, new SubmitQuestionnaireRequest(answers));
        return new SubmitResponseDto()
                .setSuccessful(true)
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

    private void checkSession(QuestionnaireRecord record) throws NoUserSessionException{
        if (record == null) {
            throw new NoUserSessionException("Questionnaire session was not initialized for user");
        }
    }

    private QueryQuestionResponseDto addNewQuestion(List<QuestionDto> questions, QueryQuestionResponse response) {
        QuestionDto newQuestion = new QuestionDto()
                .setIndex(questions.size())
                .setUuid(response.getUuid())
                .setDescription(response.getUuid())
                .setOptions(response.getOptions().stream()
                        .map(o -> new OptionDto()
                                .setSelected(false)
                                .setDescription(o.getDescription())
                                .setUuid(o.getUuid()))
                        .collect(toList()));
        questions.add(newQuestion);

        return new QueryQuestionResponseDto()
                .setHasNext(response.isHasNext())
                .setQuestion(newQuestion);
    }

    private List<OptionDto> updateOptions(List<OptionDto> options, int selectedOptionIndex) {
        var updatedOptions = options.stream()
                .map(o -> o.setSelected(false))
                .collect(toList());

        updatedOptions.get(selectedOptionIndex).setSelected(true);
        return updatedOptions;
    }
}
