package rs.kunpero.humchallenge.factory;

import rs.kunpero.humchallenge.api.QuestionnaireManager;
import rs.kunpero.humchallenge.api.QuestionnaireManagerImpl;
import rs.kunpero.humchallenge.integration.ExternalService;
import rs.kunpero.humchallenge.service.QuestionnaireService;

public class QuestionnaireFactory {
    public static QuestionnaireManager getManager(ExternalService externalService) {
        QuestionnaireService service = new QuestionnaireService(externalService);
        return new QuestionnaireManagerImpl(service);
    }
}
