package rs.kunpero.humchallenge.api;

import rs.kunpero.humchallenge.api.dto.InitQuestionnaireApiRequest;
import rs.kunpero.humchallenge.api.dto.InitQuestionnaireApiResponse;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiRequest;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiResponse;
import rs.kunpero.humchallenge.api.dto.SubmitRequest;
import rs.kunpero.humchallenge.api.dto.SubmitResponse;
import rs.kunpero.humchallenge.util.exception.ExternalServiceException;
import rs.kunpero.humchallenge.util.exception.NoUserSessionException;

public interface QuestionnaireApi {

    /**
     * Init questionnaire session for user if was not submitted
     *
     * @param request contains user. Must not be empty
     * @return - Welcoming message
     */
    InitQuestionnaireApiResponse init(InitQuestionnaireApiRequest request);

    /**
     * Save/update answer in cache. If answered question was last try to query next.
     *
     * @param request
     * @return next question info if exists or
     * @throws NoUserSessionException if init method was not called before
     * @throws IllegalArgumentException if trying to update non-existent question
     * @throws ExternalServiceException if unexpected exception in external service occurred
     */
    QueryQuestionApiResponse queryQuestion(QueryQuestionApiRequest request) throws NoUserSessionException, IllegalArgumentException, ExternalServiceException;

    SubmitResponse submit(SubmitRequest request);
}
