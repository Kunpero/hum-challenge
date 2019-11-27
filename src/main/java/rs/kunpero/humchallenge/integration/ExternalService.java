package rs.kunpero.humchallenge.integration;

import rs.kunpero.humchallenge.integration.dto.QueryQuestionRequest;
import rs.kunpero.humchallenge.integration.dto.QueryQuestionResponse;
import rs.kunpero.humchallenge.integration.dto.SubmitQuestionnaireRequest;
import rs.kunpero.humchallenge.integration.dto.SubmitQuestionnaireResponse;

public interface ExternalService {

    /**
     * Query next question
     *
     * @param request If uuid in is not present in QueryQuestionRequest, then first question would be returned.
     * @return {@code QueryQuestionResponse} - next available question
     */
    QueryQuestionResponse queryQuestion(QueryQuestionRequest request);


    /**
     * Submit client's answers
     *
     * @param request - Request contains list of filled answers
     * @return {@code SubmitQuestionnaireResponse} - Questionnaire result description
     */
    SubmitQuestionnaireResponse submitQuestionary(SubmitQuestionnaireRequest request);
}
