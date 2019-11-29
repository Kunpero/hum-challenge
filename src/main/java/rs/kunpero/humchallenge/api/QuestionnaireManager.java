package rs.kunpero.humchallenge.api;

import rs.kunpero.humchallenge.api.dto.QueryQuestionApiRequest;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiResponse;
import rs.kunpero.humchallenge.api.dto.SubmitApiRequest;
import rs.kunpero.humchallenge.api.dto.SubmitApiResponse;
import rs.kunpero.humchallenge.api.dto.UpdateQuestionApiRequest;
import rs.kunpero.humchallenge.api.dto.UpdateQuestionApiResponse;
import rs.kunpero.humchallenge.util.exception.ExternalServiceException;

public interface QuestionnaireManager {

    /**
     * Update question's option if exist
     *
     * @param request  {@link UpdateQuestionApiRequest}
     * @return Current questions for user
     * @throws ExternalServiceException Unexpected external service error
     */
    UpdateQuestionApiResponse updateQuestion(UpdateQuestionApiRequest request);

    /**
     * Query for next user's question
     *
     * @param request  {@link QueryQuestionApiRequest}
     * @return Current questions for user
     * @throws ExternalServiceException Unexpected external service error
     */
    QueryQuestionApiResponse queryQuestion(QueryQuestionApiRequest request) throws ExternalServiceException;

    /**
     * Submit user's answers
     *
     * @param request  {@link SubmitApiRequest}
     * @return Result of questionnaire
     * @throws ExternalServiceException Unexpected external service error
     */
    SubmitApiResponse submit(SubmitApiRequest request);
}
