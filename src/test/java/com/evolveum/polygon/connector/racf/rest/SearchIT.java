package com.evolveum.polygon.connector.racf.rest;

import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

public class SearchIT extends BaseIT{

    @Test
    public void searchAllAccounts() throws Exception {
        ConnectorFacade facade = setupConnector();

        ListResultHandler handler = new ListResultHandler();
        facade.search(ObjectClass.ACCOUNT, null, handler, null);

        AssertJUnit.assertTrue(!handler.getObjects().isEmpty());
    }
}
