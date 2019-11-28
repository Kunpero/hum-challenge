package rs.kunpero.humchallenge.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import rs.kunpero.humchallenge.integration.MockedExternalService;
import rs.kunpero.humchallenge.integration.dto.SubmitQuestionnaireRequest;
import rs.kunpero.humchallenge.integration.dto.UpdateQuestionRequestDto;
import rs.kunpero.humchallenge.integration.dto.UpdateQuestionResponseDto;
import rs.kunpero.humchallenge.service.dto.OptionDto;
import rs.kunpero.humchallenge.service.dto.QueryQuestionRequestDto;
import rs.kunpero.humchallenge.service.dto.QueryQuestionResponseDto;
import rs.kunpero.humchallenge.service.dto.QuestionDto;
import rs.kunpero.humchallenge.service.dto.QuestionnaireRecord;
import rs.kunpero.humchallenge.service.dto.SubmitRequestDto;
import rs.kunpero.humchallenge.service.dto.SubmitResponseDto;
import rs.kunpero.humchallenge.util.exception.ExternalServiceException;
import rs.kunpero.humchallenge.util.exception.SubmitException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static rs.kunpero.humchallenge.integration.MockedExternalService.UNEXPECTED_ERROR;

@RunWith(BlockJUnit4ClassRunner.class)
public class QuestionnaireServiceTest {

    private static final String EMPTY_USER = "NONE";
    private static final String MOCKED_USER = "MOCKED_USER";

    @Test
    public void testUpdateQuestionNoRecord() {
        QuestionnaireService questionnaireService = new QuestionnaireService(new MockedExternalService());

        UpdateQuestionRequestDto requestDto = buildUpdateRequest(EMPTY_USER, 0, 0);
        UpdateQuestionResponseDto responseDto = questionnaireService.updateQuestion(requestDto);
        assert responseDto.getQuestions().isEmpty();
    }

    @Test
    public void testUpdateQuestionNoQuestionWithIndex() {
        Map<String, QuestionnaireRecord> mockRecords = new HashMap<>();
        mockRecords.put(MOCKED_USER, new QuestionnaireRecord());
        QuestionnaireService questionnaireService = new QuestionnaireService(new MockedExternalService());

        UpdateQuestionRequestDto requestDto = buildUpdateRequest(MOCKED_USER, 0, 0);
        UpdateQuestionResponseDto responseDto = questionnaireService.updateQuestion(requestDto);
        assert responseDto.getQuestions().isEmpty();
    }

    @Test
    public void testUpdateQuestionNoOptionWithIndex() {
        int questionIndex = 0;

        QuestionnaireRecord mockRecord = new QuestionnaireRecord();
        QuestionDto question = buildQuestionDto(0, buildOptionDtoList(2));
        List<QuestionDto> mockedQuestions = List.of(question);

        mockRecord.setQuestions(mockedQuestions);
        Map<String, QuestionnaireRecord> mockRecords = new HashMap<>();
        mockRecords.put(MOCKED_USER, mockRecord);
        QuestionnaireService questionnaireService = new QuestionnaireService(mockRecords, new MockedExternalService());

        UpdateQuestionRequestDto requestDto = buildUpdateRequest(MOCKED_USER, questionIndex, -1);
        UpdateQuestionResponseDto responseDto = questionnaireService.updateQuestion(requestDto);

        assert responseDto.getQuestions().size() == 1;

        assert responseDto.getQuestions().get(questionIndex).getOptions().get(0).isSelected() == false;
        assert responseDto.getQuestions().get(questionIndex).getOptions().get(1).isSelected() == false;
    }

    @Test
    public void testUpdateQuestionSuccessful() {
        int questionIndex = 0;
        int optionIndex = 0;
        QuestionnaireRecord mockRecord = new QuestionnaireRecord();
        QuestionDto question = buildQuestionDto(0, buildOptionDtoList(2));
        List<QuestionDto> mockedQuestions = List.of(question);

        mockRecord.setQuestions(mockedQuestions);
        Map<String, QuestionnaireRecord> mockRecords = new HashMap<>();
        mockRecords.put(MOCKED_USER, mockRecord);
        QuestionnaireService questionnaireService = new QuestionnaireService(mockRecords, new MockedExternalService());

        UpdateQuestionRequestDto requestDto = buildUpdateRequest(MOCKED_USER, questionIndex, optionIndex);
        UpdateQuestionResponseDto responseDto = questionnaireService.updateQuestion(requestDto);

        assert responseDto.getQuestions().size() == 1;
        assert responseDto.getQuestions().get(questionIndex).getOptions().get(optionIndex).isSelected() == true;
        assert responseDto.getQuestions().get(questionIndex).getOptions().get(optionIndex + 1).isSelected() == false;
    }

    @Test
    public void testQueryQuestionNoMoreQuestions() {
        QuestionnaireRecord mockRecord = new QuestionnaireRecord();

        Map<String, QuestionnaireRecord> mockRecords = new HashMap<>();

        List<QuestionDto> mockedQuestions = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            QuestionDto question = buildQuestionDto(i, buildOptionDtoList(2));

            mockRecord.setQuestions(mockedQuestions);
            mockRecords.put(MOCKED_USER, mockRecord);
            mockedQuestions.add(question);
        }

        QuestionnaireService questionnaireService = new QuestionnaireService(mockRecords, new MockedExternalService());

        QueryQuestionRequestDto requestDto = buildQueryQuestionRequest(MOCKED_USER);
        QueryQuestionResponseDto responseDto = questionnaireService.queryQuestion(requestDto);

        assert responseDto.getQuestions().size() == 2;
    }

    @Test
    public void testQueryQuestionFirstQuestion() {
        QuestionnaireService questionnaireService = new QuestionnaireService(new MockedExternalService());

        QueryQuestionRequestDto requestDto = buildQueryQuestionRequest(MOCKED_USER);
        QueryQuestionResponseDto responseDto = questionnaireService.queryQuestion(requestDto);

        assert responseDto.getQuestions().size() == 1;
    }

    @Test
    public void testSubmitNoRecord() {

    }

    @Test(expected = SubmitException.class)
    public void testSubmitOptionIsNotSelected() {
        QuestionnaireRecord mockRecord = new QuestionnaireRecord();

        Map<String, QuestionnaireRecord> mockRecords = new HashMap<>();
        List<QuestionDto> mockedQuestions = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            QuestionDto question = buildQuestionDto(i, buildOptionDtoList(2));

            mockRecord.setQuestions(mockedQuestions);
            mockRecords.put(MOCKED_USER, mockRecord);
            mockedQuestions.add(question);
        }
        SubmitRequestDto requestDto = new SubmitRequestDto(MOCKED_USER);
        QuestionnaireService questionnaireService = new QuestionnaireService(mockRecords, new MockedExternalService());
        questionnaireService.submit(requestDto);
    }

    @Test
    public void testSubmitSuccessful() {
        QuestionnaireRecord mockRecord = new QuestionnaireRecord();

        Map<String, QuestionnaireRecord> mockRecords = new HashMap<>();
        List<QuestionDto> mockedQuestions = new ArrayList<>();

        mockRecord.setQuestions(mockedQuestions);
        mockRecords.put(MOCKED_USER, mockRecord);

        QuestionDto question0 = buildQuestionDto(0, buildOptionDtoList(2));
        QuestionDto question1 = buildQuestionDto(1, buildOptionDtoList(2));
        QuestionDto question2 = buildQuestionDto(1, buildOptionDtoList(2));
        question0.getOptions().get(0).setSelected(true);
        question1.getOptions().get(0).setSelected(true);
        question2.getOptions().get(0).setSelected(true);
        mockedQuestions.add(question0);
        mockedQuestions.add(question2);
        mockedQuestions.add(question2);
        //       ----
        //      /(o) \
        //     (  <   )
        //      \ -- /
        //
        // alien protection
        SubmitRequestDto requestDto = new SubmitRequestDto(MOCKED_USER);
        QuestionnaireService questionnaireService = new QuestionnaireService(mockRecords, new MockedExternalService());
        SubmitResponseDto responseDto = questionnaireService.submit(requestDto);
        assert responseDto.isSuccessful();
    }

    @Test(expected = SubmitException.class)
    public void testMultipleSelectedOptions() {
        QuestionnaireRecord mockRecord = new QuestionnaireRecord();

        Map<String, QuestionnaireRecord> mockRecords = new HashMap<>();
        List<QuestionDto> mockedQuestions = new ArrayList<>();
        QuestionDto question = buildQuestionDto(0, buildOptionDtoList(2));

        mockRecord.setQuestions(mockedQuestions);
        mockRecords.put(MOCKED_USER, mockRecord);
        question.getOptions()
                .forEach(o -> o.setSelected(true));
        mockedQuestions.add(question);
        SubmitRequestDto requestDto = new SubmitRequestDto(MOCKED_USER);
        QuestionnaireService questionnaireService = new QuestionnaireService(mockRecords, new MockedExternalService());
        questionnaireService.submit(requestDto);
    }

    @Test(expected = ExternalServiceException.class)
    public void externalServiceError() {
        QuestionnaireRecord mockRecord = new QuestionnaireRecord();

        Map<String, QuestionnaireRecord> mockRecords = new HashMap<>();
        List<QuestionDto> mockedQuestions = new ArrayList<>();

        mockRecord.setQuestions(mockedQuestions);
        mockRecords.put(MOCKED_USER, mockRecord);

        QuestionDto question = buildQuestionDto(UNEXPECTED_ERROR, buildOptionDtoList(2));
        question.getOptions().get(0).setSelected(true);
        mockedQuestions.add(question);

        SubmitRequestDto requestDto = new SubmitRequestDto(MOCKED_USER);
        QuestionnaireService questionnaireService = new QuestionnaireService(mockRecords, new MockedExternalService());
        questionnaireService.submit(requestDto);
    }

    private UpdateQuestionRequestDto buildUpdateRequest(String user, int questionIndex, int optionIndex) {
        return new UpdateQuestionRequestDto()
                .setUser(user)
                .setQuestionIndex(questionIndex)
                .setOptionIndex(optionIndex);
    }

    private QueryQuestionRequestDto buildQueryQuestionRequest(String user) {
        return new QueryQuestionRequestDto()
                .setUser(user);
    }

    private static QuestionDto buildQuestionDto(int index, List<OptionDto> options) {
        return new QuestionDto()
                .setIndex(index)
                .setDescription("DESCRIPTION_" + index)
                .setOptions(options);
    }

    private static List<OptionDto> buildOptionDtoList(int count) {
        List<OptionDto> options = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            options.add(new OptionDto()
                    .setIndex(i)
                    .setDescription("OPTION_" + i)
                    .setSelected(false));
        }
        return options;
    }
}
