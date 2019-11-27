package rs.kunpero.humchallenge.util.converter;

import rs.kunpero.humchallenge.api.dto.ApiOption;
import rs.kunpero.humchallenge.api.dto.InitQuestionnaireApiRequest;
import rs.kunpero.humchallenge.api.dto.InitQuestionnaireApiResponse;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiRequest;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiResponse;
import rs.kunpero.humchallenge.api.dto.SubmitRequest;
import rs.kunpero.humchallenge.api.dto.SubmitResponse;
import rs.kunpero.humchallenge.service.dto.InitQuestionnaireRequestDto;
import rs.kunpero.humchallenge.service.dto.InitQuestionnaireResponseDto;
import rs.kunpero.humchallenge.service.dto.QueryQuestionRequestDto;
import rs.kunpero.humchallenge.service.dto.QueryQuestionResponseDto;
import rs.kunpero.humchallenge.service.dto.SubmitRequestDto;
import rs.kunpero.humchallenge.service.dto.SubmitResponseDto;

import static java.util.stream.Collectors.toList;

public class QuestionnaireConverter {

    public static InitQuestionnaireRequestDto convert(InitQuestionnaireApiRequest request) {
        return new InitQuestionnaireRequestDto(request.getUser());
    }

    public static InitQuestionnaireApiResponse convert(InitQuestionnaireResponseDto response) {
        return new InitQuestionnaireApiResponse(response.getQuestionnaireDescription());
    }

    public static QueryQuestionRequestDto convert(QueryQuestionApiRequest request) {
        return new QueryQuestionRequestDto()
                .setUser(request.getUser())
                .setQuestionIndex(request.getQuestionIndex())
                .setSelectedOptionIndex(request.getOptionIndex());
    }

    public static QueryQuestionApiResponse convert(QueryQuestionResponseDto response) {
        return new QueryQuestionApiResponse()
                .setIndex(response.getQuestion().getIndex())
                .setDescription(response.getQuestion().getDescription())
                .setOptions(response.getQuestion().getOptions().stream()
                        .map(o -> new ApiOption()
                                .setDescription(o.getDescription())
                                .setIndex(o.getIndex())
                                .setSelected(o.isSelected()))
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
}
