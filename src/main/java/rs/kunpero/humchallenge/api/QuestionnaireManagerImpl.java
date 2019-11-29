package rs.kunpero.humchallenge.api;

import rs.kunpero.humchallenge.api.dto.QueryQuestionApiRequest;
import rs.kunpero.humchallenge.api.dto.QueryQuestionApiResponse;
import rs.kunpero.humchallenge.api.dto.SubmitApiRequest;
import rs.kunpero.humchallenge.api.dto.SubmitApiResponse;
import rs.kunpero.humchallenge.api.dto.UpdateQuestionApiRequest;
import rs.kunpero.humchallenge.api.dto.UpdateQuestionApiResponse;
import rs.kunpero.humchallenge.integration.dto.UpdateQuestionRequestDto;
import rs.kunpero.humchallenge.service.QuestionnaireService;
import rs.kunpero.humchallenge.service.dto.QueryQuestionRequestDto;
import rs.kunpero.humchallenge.service.dto.SubmitResponseDto;
import rs.kunpero.humchallenge.util.exception.ExternalServiceException;
import rs.kunpero.humchallenge.util.exception.SubmitException;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static rs.kunpero.humchallenge.util.converter.QuestionnaireConverter.convert;

public class QuestionnaireManagerImpl implements QuestionnaireManager {

    private Map<String, Lock> lockMap = new ConcurrentHashMap<>();

    private final QuestionnaireService questionnaireService;

    public QuestionnaireManagerImpl(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @Override
    public UpdateQuestionApiResponse updateQuestion(UpdateQuestionApiRequest request) {
        Callable<UpdateQuestionApiResponse> action = () -> {
            UpdateQuestionRequestDto requestDto = convert(request);
            return convert(questionnaireService.updateQuestion(requestDto));
        };
        return synchronize(action, request.getUser());
    }

    @Override
    public QueryQuestionApiResponse queryQuestion(QueryQuestionApiRequest request) throws IllegalArgumentException, ExternalServiceException {
        Callable<QueryQuestionApiResponse> action = () -> {
            QueryQuestionRequestDto requestDto = convert(request);
            return convert(questionnaireService.queryQuestion(requestDto));
        };
        return synchronize(action, request.getUser());
    }

    @Override
    public SubmitApiResponse submit(SubmitApiRequest request) {
        Callable<SubmitApiResponse> action = () -> {
            try {
                SubmitResponseDto responseDto = questionnaireService.submit(convert(request));
                return convert(responseDto);
            } catch (SubmitException | ExternalServiceException ex) {
                return new SubmitApiResponse(false, ex.getMessage());
            }
        };
        return synchronize(action, request.getUser());
    }

    private <R> R synchronize(Callable<R> callable, String user) {
        lockMap.putIfAbsent(user, new ReentrantLock());
        Lock lock = lockMap.get(user);
        lock.tryLock();
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            lockMap.remove(user);
            lock.unlock();
        }
    }
}
