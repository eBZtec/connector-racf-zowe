package com.evolveum.polygon.connector.racf.rest;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.APIConfiguration;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.api.ConnectorFacadeFactory;
import org.identityconnectors.test.common.TestHelpers;

public abstract class BaseTest {

    protected ConnectorFacade setupConnector() {

        RacfRestConfiguration config = new RacfRestConfiguration();
        config.setUrl("http://localhost:11223");
        config.setUsername("user");
        config.setPassword(new GuardedString("user".toCharArray()));

        return setupConnector(config);
    }

    protected ConnectorFacade setupConnector(RacfRestConfiguration config) {

        ConnectorFacadeFactory factory = ConnectorFacadeFactory.getInstance();

        APIConfiguration impl = TestHelpers.createTestConfiguration(RacfRestConnector.class, config);

        impl.getResultsHandlerConfiguration().setEnableAttributesToGetSearchResultsHandler(false);
        impl.getResultsHandlerConfiguration().setEnableCaseInsensitiveFilter(false);
        impl.getResultsHandlerConfiguration().setEnableFilteredResultsHandler(false);
        impl.getResultsHandlerConfiguration().setEnableNormalizingResultsHandler(false);
        impl.getResultsHandlerConfiguration().setFilteredResultsHandlerInValidationMode(false);

        return factory.newInstance(impl);
    }
}
