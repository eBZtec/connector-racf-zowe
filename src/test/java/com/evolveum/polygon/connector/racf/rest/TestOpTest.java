package com.evolveum.polygon.connector.racf.rest;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.exceptions.ConnectionFailedException;
import org.identityconnectors.framework.common.exceptions.InvalidCredentialException;
import org.testng.annotations.Test;

public class TestOpTest extends BaseTest{

    @Test
    public void valid() throws Exception {
        ConnectorFacade connector = setupConnector();
        connector.test();
    }

    @Test(expectedExceptions = ConnectionFailedException.class)
    public void invalidUrl() throws Exception {
        RacfRestConfiguration config = new RacfRestConfiguration();
        config.setUrl("http://localhost:8092");
        config.setUsername("user");
        config.setPassword(new GuardedString("user".toCharArray()));

        ConnectorFacade connector = setupConnector(config);
        connector.test();
    }

}
