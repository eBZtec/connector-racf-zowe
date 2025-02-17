package com.evolveum.polygon.connector.racf.rest;

import com.evolveum.polygon.connector.racf.rest.util.RacfRestConstants;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.objects.*;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

public class UpdateTest extends BaseTest{

    @Test(priority = 10)
    public void changeAttribute() throws Exception {
        ConnectorFacade connector = setupConnector();

        Set<AttributeDelta> deltaAttributes = new HashSet<AttributeDelta>();

        AttributeDeltaBuilder builder = new AttributeDeltaBuilder();
        builder.setName(RacfRestConstants.USER_ATTR_RACF_NAME);
        builder.addValueToReplace("Jean Santos");

        deltaAttributes.add(builder.build());

        builder.setName(RacfRestConstants.USER_ATTR_RACF_IS_PROTECT);
        builder.addValueToReplace(true);

        deltaAttributes.add(builder.build());

        connector.updateDelta(ObjectClass.ACCOUNT, new Uid("E999996"), deltaAttributes, null);

        ConnectorObject obj = connector.getObject(ObjectClass.ACCOUNT, new Uid("E999996"), null);
        Attribute racfName = AttributeUtil.find(RacfRestConstants.USER_ATTR_RACF_NAME, obj.getAttributes());

        AssertJUnit.assertTrue(racfName.getValue().contains("USER INTEGRACAO ZOWE"));
    }
}
