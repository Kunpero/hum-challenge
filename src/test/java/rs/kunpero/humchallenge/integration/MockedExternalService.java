package rs.kunpero.humchallenge.integration;

import rs.kunpero.humchallenge.integration.dto.Option;
import rs.kunpero.humchallenge.integration.dto.QueryQuestionRequest;
import rs.kunpero.humchallenge.integration.dto.QueryQuestionResponse;
import rs.kunpero.humchallenge.integration.dto.SubmitQuestionnaireRequest;
import rs.kunpero.humchallenge.integration.dto.SubmitQuestionnaireResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MockedExternalService implements ExternalService {
    public static final int UNEXPECTED_ERROR = -100;
    private static final Map<Integer, Map<Integer, Integer>> RESULT_MAP = Map.of(
            0, Map.of(0, 5, 1, 3, 2, 0),
            1, Map.of(0, 0, 1, 1, 2, 3, 3, 5),
            2, Map.of(0, 3, 1, 0));

    @Override
    public QueryQuestionResponse queryQuestion(QueryQuestionRequest request) {
        switch (request.getNextIndex()) {
            case 0:
                return new QueryQuestionResponse(buildQuestion(0, "Do you enjoy working in a team?",
                        buildOptionList("Teamwork is in my blood",
                                "Yes, I do",
                                "I prefer to work alone")), true);
            case 1:
                return new QueryQuestionResponse(buildQuestion(1, "How long have you been working with Java?",
                        buildOptionList(
                                "Never",
                                "Less than 1 year",
                                "Less than 2 years",
                                "More than 3 years"
                        )), true);
            case 2:
                return new QueryQuestionResponse(buildQuestion(2, "How do you feel about automated tests?",
                        buildOptionList(
                                "Mandatory",
                                "Waste of time"
                        )), false);
            default:
                return new QueryQuestionResponse(null, false);
        }
    }

    @Override
    public SubmitQuestionnaireResponse submitQuestionnaire(SubmitQuestionnaireRequest request) {

        int resultSum = request.getAnswers().stream()
                .mapToInt(a -> {
                    if (a.getQuestionIndex() == UNEXPECTED_ERROR) {
                        throw new RuntimeException("Unecpected error");
                    }
                    return RESULT_MAP.get(a.getOptionIndex()).get(a.getOptionIndex());
                })
                .sum();
        String resultDescription = null;

        if (resultSum <= 6) {
            resultDescription = "Unfortunately, we don’t match!";
        }

        if (resultSum > 6 && resultSum <= 10) {
            resultDescription = "That looks good!";
        }

        if (resultSum > 10) {
            resultDescription = "Excellent!";
        }
        return new SubmitQuestionnaireResponse(true, resultDescription);
    }

    private static QueryQuestionResponse.Question buildQuestion(int index, String description, List<Option> options) {
        return new QueryQuestionResponse.Question(index, description, options);
    }

    private static List<Option> buildOptionList(String... descriptions) {
        List<Option> options = new ArrayList<>();
        for (int i = 0; i < descriptions.length; i++) {
            options.add(new Option(i, descriptions[i]));
        }
        return options;
    }
}
