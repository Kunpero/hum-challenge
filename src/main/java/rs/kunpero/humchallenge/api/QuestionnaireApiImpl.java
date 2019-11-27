package rs.kunpero.humchallenge.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rs.kunpero.humchallenge.api.dto.InitQuestionnaireApiRequest;
import rs.kunpero.humchallenge.api.dto.InitQuestionnaireApiResponse;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiRequest;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiResponse;
import rs.kunpero.humchallenge.api.dto.SubmitRequest;
import rs.kunpero.humchallenge.api.dto.SubmitResponse;
import rs.kunpero.humchallenge.service.QuestionnaireService;
import rs.kunpero.humchallenge.service.dto.InitQuestionnaireRequestDto;
import rs.kunpero.humchallenge.service.dto.QueryQuestionRequestDto;
import rs.kunpero.humchallenge.service.dto.SubmitResponseDto;
import rs.kunpero.humchallenge.util.exception.ExternalServiceException;
import rs.kunpero.humchallenge.util.exception.SubmitException;

import static rs.kunpero.humchallenge.util.converter.QuestionnaireConverter.convert;

public class QuestionnaireApiImpl implements QuestionnaireApi {
    private static final Logger log = LoggerFactory.getLogger(QuestionnaireApiImpl.class);

    private final QuestionnaireService questionnaireService;

    public QuestionnaireApiImpl(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @Override
    public InitQuestionnaireApiResponse init(InitQuestionnaireApiRequest request) {
        // TODO: 2019-11-27 assert user not empty
        InitQuestionnaireRequestDto requestDto = convert(request);
        return convert(questionnaireService.init(requestDto));
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
