package rs.kunpero.humchallenge.api;

import rs.kunpero.humchallenge.api.dto.QueryQuestionApiRequest;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiResponse;
import rs.kunpero.humchallenge.api.dto.SubmitRequest;
import rs.kunpero.humchallenge.api.dto.SubmitResponse;
import rs.kunpero.humchallenge.api.dto.UpdateQuestionRequest;
import rs.kunpero.humchallenge.api.dto.UpdateQuestionResponse;
import rs.kunpero.humchallenge.integration.dto.UpdateQuestionRequestDto;
import rs.kunpero.humchallenge.service.QuestionnaireService;
import rs.kunpero.humchallenge.service.dto.QueryQuestionRequestDto;
import rs.kunpero.humchallenge.service.dto.SubmitResponseDto;
import rs.kunpero.humchallenge.util.exception.ExternalServiceException;
import rs.kunpero.humchallenge.util.exception.SubmitException;

import static rs.kunpero.humchallenge.util.converter.QuestionnaireConverter.convert;

public class QuestionnaireManagerImpl implements QuestionnaireManager {

    private final QuestionnaireService questionnaireService;

    public QuestionnaireManagerImpl(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @Override
    public UpdateQuestionResponse updateQuestion(UpdateQuestionRequest request) {
        UpdateQuestionRequestDto requestDto = convert(request);
        return convert(questionnaireService.updateQuestion(requestDto));
    }

    @Override
    public QueryQuestionApiResponse queryQuestion(QueryQuestionApiRequest request) throws IllegalArgumentException, ExternalServiceException {
        QueryQuestionRequestDto requestDto = convert(request);
        return convert(questionnaireService.queryQuestion(requestDto));
    }

    @Override
    public SubmitResponse submit(SubmitRequest request) {
        try {
            SubmitResponseDto responseDto = questionnaireService.submit(convert(request));
            return convert(responseDto);
        } catch (SubmitException | ExternalServiceException ex) {
            return new SubmitResponse()
                    .setSuccessful(false)
                    .setResultDescription(ex.getMessage());
        }
    }
}
