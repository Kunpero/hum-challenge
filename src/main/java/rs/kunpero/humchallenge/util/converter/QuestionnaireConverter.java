package rs.kunpero.humchallenge.util.converter;

import rs.kunpero.humchallenge.api.dto.ApiOption;
import rs.kunpero.humchallenge.api.dto.ApiQuestion;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiRequest;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiResponse;
import rs.kunpero.humchallenge.api.dto.SubmitRequest;
import rs.kunpero.humchallenge.api.dto.SubmitResponse;
import rs.kunpero.humchallenge.api.dto.UpdateQuestionRequest;
import rs.kunpero.humchallenge.api.dto.UpdateQuestionResponse;
import rs.kunpero.humchallenge.integration.dto.UpdateQuestionRequestDto;
import rs.kunpero.humchallenge.integration.dto.UpdateQuestionResponseDto;
import rs.kunpero.humchallenge.service.dto.QueryQuestionRequestDto;
import rs.kunpero.humchallenge.service.dto.QueryQuestionResponseDto;
import rs.kunpero.humchallenge.service.dto.QuestionDto;
import rs.kunpero.humchallenge.service.dto.SubmitRequestDto;
import rs.kunpero.humchallenge.service.dto.SubmitResponseDto;

import static java.util.stream.Collectors.toList;

public class QuestionnaireConverter {

    public static UpdateQuestionRequestDto convert(UpdateQuestionRequest request) {
        return new UpdateQuestionRequestDto()
                .setUser(request.getUser())
                .setQuestionIndex(request.getQuestionIndex())
                .setOptionIndex(request.getOptionIndex());
    }

    public static UpdateQuestionResponse convert(UpdateQuestionResponseDto response) {
        return new UpdateQuestionResponse()
                .setQuestions(response.getQuestions().stream()
                        .map(QuestionnaireConverter::convert)
                        .collect(toList()));
    }

    public static QueryQuestionRequestDto convert(QueryQuestionApiRequest request) {
        return new QueryQuestionRequestDto()
                .setUser(request.getUser());
    }

    public static QueryQuestionApiResponse convert(QueryQuestionResponseDto response) {
        return new QueryQuestionApiResponse()
                .setQuestions(response.getQuestions().stream()
                        .map(QuestionnaireConverter::convert)
                        .collect(toList()))
                .setHasNext(response.isHasNext());
    }

    public static SubmitRequestDto convert(SubmitRequest request) {
        return new SubmitRequestDto(request.getUser());
    }

    public static SubmitResponse convert(SubmitResponseDto responseDto) {
        return new SubmitResponse()
                .setSuccessful(responseDto.isSuccessful())
                .setResultDescription(responseDto.getResultDescription());

    }

    private static ApiQuestion convert(QuestionDto q) {
        return new ApiQuestion()
                .setIndex(q.getIndex())
                .setDescription(q.getDescription())
                .setOptions(q.getOptions().stream()
                        .map(o -> new ApiOption()
                                .setIndex(o.getIndex())
                                .setSelected(o.isSelected())
                                .setDescription(o.getDescription()))
                        .collect(toList()));
    }
}
