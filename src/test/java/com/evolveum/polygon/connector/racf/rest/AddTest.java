package com.evolveum.polygon.connector.racf.rest;

import com.evolveum.polygon.connector.racf.rest.api.RUser;
import com.evolveum.polygon.connector.racf.rest.util.RacfRestConstants;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.objects.*;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import java.util.*;

public class AddTest extends BaseTest{

    private static final String NAME_USER_ADD = "addUser";
    private static final String TESTE_IDENTITY_NAME = "artur";
    private static final String TESTE_IDENTITY_PASSWORD = "123456";

    private static final List<String> AUTH_CLASSES = Arrays.asList("Class1", "Class2");

    private static final List<String> ANY_LOGON_DAYS = Arrays.asList("SUNDAY", "MONDAY");

    private Uid createdUid;

    @Test
    public void deveCriarContaComSomenteQuatroAtributos() {
        Set<Attribute> set = new HashSet<>();
        set.add(AttributeBuilder.build(Name.NAME, TESTE_IDENTITY_NAME));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_PASSWORD_CHANGE_INTERVAL, "90"));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_DEFAULT_GROUP, "SYS1SYS1"));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_ERUID, TESTE_IDENTITY_NAME));
        set.add(AttributeBuilder.build(OperationalAttributes.ENABLE_NAME, true));
        set.add(AttributeBuilder.build(OperationalAttributes.PASSWORD_NAME, new GuardedString(TESTE_IDENTITY_PASSWORD.toCharArray())));

        RacfRestConnector connector = new RacfRestConnector();
        RUser user = connector.translateUser(null, set);
    }

    @Test(groups = NAME_USER_ADD)
    public void addUser() throws Exception {
        ConnectorFacade connector = setupConnector();

        Set<Attribute> set = new HashSet<>();
        set.add(AttributeBuilder.build(Name.NAME, TESTE_IDENTITY_NAME));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_NAME, "Jean Michel"));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_ERUID, "X000408"));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_DEFAULT_GROUP, "SYSYSYS2"));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_OWNER, "SYSYSYS2"));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_SPECIAL, true));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_IS_UAUDIT, true));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_CLASS_AUTH, AUTH_CLASSES));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_LOGON_ALLOWED_LOGON_DAYS, ANY_LOGON_DAYS));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_LAST_CHANGE_PASSWORD, "2022-08-18 15:04:12.512"));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_PASSWORD_CHANGE_INTERVAL, "90"));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_OMVS_IS_SEGMENT, true));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_OMVS_SHELL, "/usr/sh"));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_OMVS_CPU, "000002147483647"));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_TSO_ACC_NUMBER, "0000022"));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_TSO_SEGMENT, true));
        set.add(AttributeBuilder.build(RacfRestConstants.USER_ATTR_RACF_IS_PROTECT, true));
        set.add(AttributeBuilder.build(OperationalAttributes.ENABLE_NAME, true));
        set.add(AttributeBuilder.build(OperationalAttributes.PASSWORD_NAME, new GuardedString(TESTE_IDENTITY_PASSWORD.toCharArray())));

        createdUid = connector.create(ObjectClass.ACCOUNT, set, null);

        AssertJUnit.assertNotNull(createdUid);
    }

    // criar o teste para usuário que já existe
}
