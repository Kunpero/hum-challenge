package rs.kunpero.humchallenge.integration;

import rs.kunpero.humchallenge.integration.dto.Option;
import rs.kunpero.humchallenge.integration.dto.QueryQuestionRequest;
import rs.kunpero.humchallenge.integration.dto.QueryQuestionResponse;
import rs.kunpero.humchallenge.integration.dto.SubmitQuestionnaireRequest;
import rs.kunpero.humchallenge.integration.dto.SubmitQuestionnaireResponse;

import java.util.ArrayList;
import java.util.List;

public class MockedExternalService implements ExternalService {

    @Override
    public QueryQuestionResponse queryQuestion(QueryQuestionRequest request) {
        switch (request.getNextIndex()) {
            case 0:
                return new QueryQuestionResponse(buildQuestion(0, buildOptionList(3)), true);
            case 1:
                return new QueryQuestionResponse(buildQuestion(1, buildOptionList(3)), false);
            case 2:
                return new QueryQuestionResponse(null, false);
        }
        return null;
    }

    @Override
    public SubmitQuestionnaireResponse submitQuestionnaire(SubmitQuestionnaireRequest request) {
        return null;
    }

    private static QueryQuestionResponse.Question buildQuestion(int index, List<Option> options) {
        return new QueryQuestionResponse.Question(index, "DESCRIPTION_" + index, options);
    }

    private static List<Option> buildOptionList(int count) {
        List<Option> options = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            options.add(new Option(i, "OPTION_" + i));
        }
        return options;
    }
}
