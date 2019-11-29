package rs.kunpero.humchallenge.util.converter;

import rs.kunpero.humchallenge.api.dto.ApiOption;
import rs.kunpero.humchallenge.api.dto.ApiQuestion;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiRequest;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiResponse;
import rs.kunpero.humchallenge.api.dto.SubmitApiRequest;
import rs.kunpero.humchallenge.api.dto.SubmitApiResponse;
import rs.kunpero.humchallenge.api.dto.UpdateQuestionApiRequest;
import rs.kunpero.humchallenge.api.dto.UpdateQuestionApiResponse;
import rs.kunpero.humchallenge.integration.dto.UpdateQuestionRequestDto;
import rs.kunpero.humchallenge.integration.dto.UpdateQuestionResponseDto;
import rs.kunpero.humchallenge.service.dto.QueryQuestionRequestDto;
import rs.kunpero.humchallenge.service.dto.QueryQuestionResponseDto;
import rs.kunpero.humchallenge.service.dto.QuestionDto;
import rs.kunpero.humchallenge.service.dto.SubmitRequestDto;
import rs.kunpero.humchallenge.service.dto.SubmitResponseDto;

import static java.util.stream.Collectors.toList;

public class QuestionnaireConverter {

    public static UpdateQuestionRequestDto convert(UpdateQuestionApiRequest request) {
        return new UpdateQuestionRequestDto(request.getUser(), request.getQuestionIndex(), request.getOptionIndex());
    }

    public static UpdateQuestionApiResponse convert(UpdateQuestionResponseDto response) {
        return new UpdateQuestionApiResponse(response.getQuestions().stream()
                .map(QuestionnaireConverter::convert)
                .collect(toList()));
    }

    public static QueryQuestionRequestDto convert(QueryQuestionApiRequest request) {
        return new QueryQuestionRequestDto()
                .setUser(request.getUser());
    }

    public static QueryQuestionApiResponse convert(QueryQuestionResponseDto response) {
        return new QueryQuestionApiResponse(response.hasNext(), response.getQuestions().stream()
                .map(QuestionnaireConverter::convert)
                .collect(toList()));
    }

    public static SubmitRequestDto convert(SubmitApiRequest request) {
        return new SubmitRequestDto(request.getUser());
    }

    public static SubmitApiResponse convert(SubmitResponseDto responseDto) {
        return new SubmitApiResponse(responseDto.isSuccessful(), responseDto.getResultDescription());

    }

    private static ApiQuestion convert(QuestionDto q) {
        return new ApiQuestion(q.getIndex(), q.getDescription(), q.getOptions().stream()
                .map(o -> new ApiOption(o.getIndex(), o.getDescription(), o.isSelected())).collect(toList()));
    }
}
