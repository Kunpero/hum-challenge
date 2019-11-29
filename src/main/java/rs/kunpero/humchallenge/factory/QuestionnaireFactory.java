package rs.kunpero.humchallenge.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rs.kunpero.humchallenge.api.QuestionnaireManager;
import rs.kunpero.humchallenge.api.QuestionnaireManagerImpl;
import rs.kunpero.humchallenge.integration.ExternalService;
import rs.kunpero.humchallenge.service.QuestionnaireService;

public class QuestionnaireFactory {
    private static final Logger log = LoggerFactory.getLogger(QuestionnaireFactory.class);

    public static QuestionnaireManager getManager(ExternalService externalService) {
        QuestionnaireService service = new QuestionnaireService(externalService);
        return new QuestionnaireManagerImpl(service);
    }
}
