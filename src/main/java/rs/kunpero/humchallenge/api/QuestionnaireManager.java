package rs.kunpero.humchallenge.api;

import rs.kunpero.humchallenge.api.dto.QueryQuestionApiRequest;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiResponse;
import rs.kunpero.humchallenge.api.dto.SubmitApiRequest;
import rs.kunpero.humchallenge.api.dto.SubmitApiResponse;
import rs.kunpero.humchallenge.api.dto.UpdateQuestionApiRequest;
import rs.kunpero.humchallenge.api.dto.UpdateQuestionApiResponse;
import rs.kunpero.humchallenge.util.exception.ExternalServiceException;

public interface QuestionnaireManager {

    UpdateQuestionApiResponse updateQuestion(UpdateQuestionApiRequest request);

    QueryQuestionApiResponse queryQuestion(QueryQuestionApiRequest request) throws IllegalArgumentException, ExternalServiceException;

    SubmitApiResponse submit(SubmitApiRequest request);
}
