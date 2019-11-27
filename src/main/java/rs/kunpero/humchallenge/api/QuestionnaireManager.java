package rs.kunpero.humchallenge.api;

import rs.kunpero.humchallenge.api.dto.QueryQuestionApiRequest;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiResponse;
import rs.kunpero.humchallenge.api.dto.SubmitRequest;
import rs.kunpero.humchallenge.api.dto.SubmitResponse;
import rs.kunpero.humchallenge.api.dto.UpdateQuestionRequest;
import rs.kunpero.humchallenge.api.dto.UpdateQuestionResponse;
import rs.kunpero.humchallenge.util.exception.ExternalServiceException;

public interface QuestionnaireManager {

    UpdateQuestionResponse updateQuestion(UpdateQuestionRequest request);

    QueryQuestionApiResponse queryQuestion(QueryQuestionApiRequest request) throws IllegalArgumentException, ExternalServiceException;

    SubmitResponse submit(SubmitRequest request);
}
