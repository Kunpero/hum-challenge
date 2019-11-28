package rs.kunpero.humchallenge;

import org.junit.Test;
import rs.kunpero.humchallenge.api.QuestionnaireManager;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiRequest;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiResponse;
import rs.kunpero.humchallenge.api.dto.SubmitApiRequest;
import rs.kunpero.humchallenge.api.dto.SubmitApiResponse;
import rs.kunpero.humchallenge.api.dto.UpdateQuestionApiRequest;
import rs.kunpero.humchallenge.api.dto.UpdateQuestionApiResponse;
import rs.kunpero.humchallenge.integration.MockedExternalService;

import static rs.kunpero.humchallenge.factory.QuestionnaireFactory.getManager;
import static rs.kunpero.humchallenge.integration.MockedExternalService.EXCELLENT_RESULT;

public class QuestionnaireTest {
    @Test
    public void testFullQuestionnaireFlow() {
        QuestionnaireManager manager = getManager(new MockedExternalService());

        final String user = "Pero Kulić";

        QueryQuestionApiResponse queryResponse0 = manager.queryQuestion(new QueryQuestionApiRequest(user));
        assert queryResponse0.getQuestions().size() == 1;
        UpdateQuestionApiResponse updateResponse0 = manager.updateQuestion(new UpdateQuestionApiRequest(user,
                queryResponse0.getQuestions().get(0).getIndex(), 0));
        assert updateResponse0.getQuestions().size() == 1;

        QueryQuestionApiResponse queryResponse1 = manager.queryQuestion(new QueryQuestionApiRequest(user));
        assert queryResponse1.getQuestions().size() == 2;
        UpdateQuestionApiResponse updateResponse1 = manager.updateQuestion(new UpdateQuestionApiRequest(user,
                queryResponse1.getQuestions().get(1).getIndex(), 3));
        assert updateResponse1.getQuestions().size() == 2;

        QueryQuestionApiResponse queryResponse2 = manager.queryQuestion(new QueryQuestionApiRequest(user));
        assert queryResponse2.getQuestions().size() == 3;
        UpdateQuestionApiResponse updateResponse2 = manager.updateQuestion(new UpdateQuestionApiRequest(user,
                queryResponse2.getQuestions().get(2).getIndex(), 0));
        assert updateResponse2.getQuestions().size() == 3;

        SubmitApiResponse result = manager.submit(new SubmitApiRequest(user));

        assert result.isSuccessful();
        assert result.getResultDescription().equals(EXCELLENT_RESULT);
    }

}
