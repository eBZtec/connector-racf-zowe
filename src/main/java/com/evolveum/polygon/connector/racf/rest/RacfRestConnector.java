/*
 * Copyright (c) 2010-2014 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evolveum.polygon.connector.racf.rest;

import com.evolveum.polygon.connector.racf.rest.api.*;
import com.evolveum.polygon.connector.racf.rest.util.AbstractRacfRestConnector;
import com.evolveum.polygon.connector.racf.rest.util.FilterHandler;
import com.evolveum.polygon.connector.racf.rest.util.RacfRestConstants;
import com.evolveum.polygon.connector.racf.rest.util.StringAccessor;
import org.apache.cxf.interceptor.Fault;
import org.identityconnectors.common.CollectionUtil;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.*;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.Connector;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.SyncTokenResultsHandler;
import org.identityconnectors.framework.spi.operations.*;
import org.w3c.dom.Attr;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.*;

@ConnectorClass(displayNameKey = "racfrest.connector.display", configurationClass = RacfRestConfiguration.class)
public class RacfRestConnector extends AbstractRacfRestConnector implements Connector, TestOp, SchemaOp,
        SearchOp<Filter>, CreateOp, DeleteOp, UpdateDeltaOp, SyncOp {

    private static final Log LOG = Log.getLog(RacfRestConnector.class);

    private RacfRestConfiguration configuration;

    private RacfRestConnection connection;
    private UserService userService;
    private GroupService groupService;

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void init(Configuration configuration) {
        this.configuration = (RacfRestConfiguration)configuration;
        this.connection = new RacfRestConnection(this.configuration);

        connection.setUpConnections();

        userService = connection.getUserService();
        groupService = connection.getGroupService();

        LOG.info("Initialization of configuration has finished.");
    }

    @Override
    public void dispose() {
        configuration = null;
        userService = null;
        groupService = null;

        if (connection != null) {
            connection.dispose();
            connection = null;
        }
    }

    @Override
    protected void handleGenericException(Exception ex, String message) {

        if (ex instanceof ConnectException || ex instanceof Fault || ex instanceof ProcessingException) {
            throw new ConnectionFailedException(message + ", reason: " + ex.getMessage(), ex);
        }

        if ( ex instanceof ConnectorException || ex instanceof UnsupportedOperationException || ex instanceof IllegalArgumentException) {
            throw (RuntimeException) ex;
        }

        if (ex instanceof NotAuthorizedException) {
            throw new InvalidCredentialException("Not authorized");
        }

        if (ex instanceof NotFoundException) {
            throw new UnknownUidException(message);
        }

        if (ex instanceof IOException) {
            if ((ex instanceof SocketTimeoutException || ex instanceof NoRouteToHostException)) {
                throw new OperationTimeoutException(message + ", timeout occured, reason: " + ex.getMessage(), ex);
            }

            throw new ConnectorIOException(message + " IO exception occcured, reason: " + ex.getMessage(), ex);
        }

        if (ex instanceof ClientErrorException) {
            Response response = ((ClientErrorException) ex).getResponse();

            if (response != null) {
                int status = response.getStatus();

                if (status == 409) {
                    throw new AlreadyExistsException(message + " Conflict during operation execution occured, reason:" + ex.getMessage(), ex);

                } else if (status == 422) {
                    throw new InvalidAttributeValueException(message + ", reason: " + ex.getMessage(), ex);

                } else {
                    throw new ConnectorException(message + ", reason: " + ex.getMessage(), ex);

                }
            }

            throw new ConnectorException(message + ", reason: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected RUser translateUser(Uid uid, Set<Attribute> attributes) {

        LOG.ok("Translating new User object with the UID {0} to the target resource output type.", uid);

        RUser user = new RUser();

        translateObject(uid, attributes, user);

        user.setRacuName(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_NAME, String.class, attributes));
        user.setRacuErUid(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_ERUID, String.class, attributes));
        user.setRacuIsSpecial(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_SPECIAL, Boolean.class, attributes));
        user.setRacuIsAudit(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_IS_AUDIT, Boolean.class, attributes));
        user.setRacuIsOper(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_IS_OPER, Boolean.class, attributes));
        user.setRacuIsProtect(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_IS_PROTECT, Boolean.class, attributes));
        user.setRacuIsRestrict(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_IS_RESTRICT, Boolean.class, attributes));
        user.setRacuIsUAudit(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_IS_UAUDIT, Boolean.class, attributes));
        user.setRacuisGrpAcc(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_IS_GRP_ACC, Boolean.class, attributes));
        user.setRacuDefaultGroup(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_DEFAULT_GROUP, String.class, attributes));
        user.setRacuOwner(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_OWNER, String.class, attributes));
        user.setRacuConnectGroups(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_CONNECT_GROUPS, List.class, attributes));
        user.setRacuInstData(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_INST_DATA, String.class, attributes));
        user.setRacuLastLogonDate(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_LAST_LOGON_DATE, String.class, attributes));
        user.setRacuCreateDate(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_CREATE_DATE, String.class, attributes));
        user.setRacuLastChangePassword(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_LAST_CHANGE_PASSWORD, String.class, attributes));
        user.setRacuPasswordChangeInterval(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_PASSWORD_CHANGE_INTERVAL, String.class, attributes));
        user.setRacuAccountXml(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_ACCOUNT_XML, List.class, attributes));
        user.setRacuFutureRevokeDate(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_LOGON_FUTURE_REVOKE_DATE, Date.class, attributes));
        user.setRacuFutureResumeDate(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_LOGON_FUTURE_RESUME_DATE, Date.class, attributes));
        user.setRacuAllowedLogonDays(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_LOGON_ALLOWED_LOGON_DAYS, List.class, attributes));
        user.setRacuAllowedLogonTime(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_LOGON_ALLOWED_LOGON_TIME, String.class, attributes));
        user.setRacuTsoSegment(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_TSO_SEGMENT, Boolean.class, attributes));
        user.setRacuTsoAccNumber(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_TSO_ACC_NUMBER, String.class, attributes));
        user.setRacuTsoReqRegSize(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_TSO_REQUESTED_REGION_SIZE, String.class, attributes));
        user.setRacuTsoMaxRegionSize(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_TSO_MAX_REGION_SIZE, String.class, attributes));
        user.setRacuTsoLogonProcedure(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_TSO_LOGON_PROCEDURE, String.class, attributes));
        user.setRacuTsoInitialCommand(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_TSO_INITIAL_COMMAND, String.class, attributes));
        user.setRacuTsoUnitNameClass(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_TSO_UNIT_NAME_ALLOC, String.class, attributes));
        user.setRacuTsoJesExecClass(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_TSO_JES_EXEC_CLASS, String.class, attributes));
        user.setRacuTsoJesSysoutMsgClass(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_TSO_JES_SYSOUT_MSG_CLASS, String.class, attributes));
        user.setRacuTsoJesSysoutClass(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_TSO_JES_SYSOUT_CLASS, String.class, attributes));
        user.setRacuTsoJesHoldClass(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_TSO_JES_HOLD_CLASS, String.class, attributes));
        user.setRacuJesTsoDestination(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_TSO_JES_DESTINATION, String.class, attributes));
        user.setRacuTsoUserData(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_TSO_USER_DATA, String.class, attributes));
        user.setRacuClAuth(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_CLASS_AUTH, List.class, attributes));
        user.setRacuIsADSP(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_IS_ADSP, Boolean.class, attributes));
        user.setRacuIsOmvsSegment(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_OMVS_IS_SEGMENT, Boolean.class, attributes));
        user.setRacuOmvsCpu(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_OMVS_CPU, String.class, attributes));
        user.setRacuOmvsFiles(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_OMVS_FILES, String.class, attributes));
        user.setRacuOmvsHome(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_OMVS_HOME, String.class, attributes));
        user.setRacuOmvsIsShared(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_OMVS_IS_SHARED, Boolean.class, attributes));
        user.setRacuOmvsMMap(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_OMVS_MMAP, String.class, attributes));
        user.setRacuOmvsProc(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_OMVS_PROC, String.class, attributes));
        user.setRacuOmvsShell(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_OMVS_SHELL, String.class, attributes));
        user.setRacuOmvsStorage(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_OMVS_STORAGE, String.class, attributes));
        user.setRacuOmvsThread(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_OMVS_THREAD, String.class, attributes));
        user.setRacuOmvsUid(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_OMVS_UID, String.class, attributes));
        user.setRacuModel(getAttributeValue(RacfRestConstants.USER_ATTR_RACF_MODEL, String.class, attributes));

        GuardedString pwd = getAttributeValue(OperationalAttributes.PASSWORD_NAME, GuardedString.class, attributes);
        if (pwd != null) {
            StringAccessor accessor = new StringAccessor();
            pwd.access(accessor);
            user.setPassword(accessor.getValue());
        }

        user.setEnabled(getAttributeValue(OperationalAttributes.ENABLE_NAME, Boolean.class, attributes));

        return user;
    }

    @Override
    protected RGroup translateGroup(Uid uid, Set<Attribute> attributes) {

        LOG.ok("Translating new group object with the uid {0} to the target resource output type.", uid);

        RGroup rGroup = new RGroup();

        translateObject(uid, attributes, rGroup);

        rGroup.setDescription(getAttributeValue(RacfRestConstants.GROUP_ATTR_DESCRIPTION, String.class, attributes));
        rGroup.setMembers(getAttributeValue(RacfRestConstants.GROUP_ATTR_MEMBERS, List.class, attributes));

        return rGroup;
    }

    private void translateObject(Uid uid, Set<Attribute> attributes, RObject object) {

        if (uid != null) {
            object.setId(uid.getUidValue());
        }

        String nameUpdate = getAttributeValue(Name.NAME, String.class, attributes);

        Name name;

        if (nameUpdate != null && !nameUpdate.isEmpty()) {
            name = new Name(nameUpdate);
        } else {
            name = AttributeUtil.getNameFromAttributes(attributes);
        }

        if (name == null || StringUtil.isEmpty(name.getNameValue())) {
            throw new InvalidAttributeValueException("Name not present or it's empty.");
        }

        object.setName(name.getNameValue());
    }

    private <T> T getAttributeValue(String name, Class<T> type, Set<Attribute> attributes) {
        LOG.ok("Processing attribute {0} of the type {1}", name, type.toString());

        Attribute attr = AttributeUtil.find(name, attributes);

        if (attr == null) {
            return null;
        }

        if (String.class.equals(type)) {
            return (T) AttributeUtil.getStringValue(attr);
        } else if (Long.class.equals(type)) {
            return (T) AttributeUtil.getLongValue(attr);
        } else if (Integer.class.equals(type)) {
            return (T) AttributeUtil.getIntegerValue(attr);
        } else if (GuardedString.class.equals(type)) {
            return (T) AttributeUtil.getGuardedStringValue(attr);
        } else if (Boolean.class.equals(type)) {
            return (T) AttributeUtil.getBooleanValue(attr);
        } else if (List.class.equals(type)) {
            return (T) attr.getValue();
        } else if(Date.class.equals(type)) {
            return (T) AttributeUtil.getDateValue(attr);
        } else {
            throw new InvalidAttributeValueException("Unknown value type " + type);
        }
    }

    @Override
    protected ConnectorObject translate(RObject object) {

        ConnectorObjectBuilder builder = new ConnectorObjectBuilder();

        String objectId = object.getId();

        addAttribute(builder, Uid.NAME, objectId);
        addAttribute(builder, Name.NAME, object.getName());

        addAttribute(builder, RacfRestConstants.OBJECT_ATTR_CHANGED, object.getChanged());

        if (object instanceof RUser) {
            builder.setObjectClass(ObjectClass.ACCOUNT);

            RUser user = (RUser) object;
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_NAME, user.getRacuName());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_ERUID, user.getRacuErUid());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_SPECIAL, user.getRacuIsSpecial());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_IS_AUDIT, user.getRacuIsAudit());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_IS_OPER, user.getRacuIsOper());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_IS_PROTECT, user.getRacuIsProtect());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_IS_RESTRICT, user.getRacuIsRestrict());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_IS_UAUDIT, user.getRacuIsUAudit());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_IS_GRP_ACC, user.getRacuisGrpAcc());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_DEFAULT_GROUP, user.getRacuDefaultGroup());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_OWNER, user.getRacuOwner());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_CONNECT_GROUPS, user.getRacuConnectGroups());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_INST_DATA, user.getRacuInstData());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_CREATE_DATE, user.getRacuLastLogonDate());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_LAST_CHANGE_PASSWORD, user.getRacuLastChangePassword());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_LAST_LOGON_DATE, user.getRacuLastLogonDate());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_PASSWORD_CHANGE_INTERVAL, user.getRacuPasswordChangeInterval());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_ACCOUNT_XML, user.getRacuAccountXml());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_LOGON_FUTURE_REVOKE_DATE, user.getRacuFutureRevokeDate());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_LOGON_FUTURE_RESUME_DATE, user.getRacuFutureResumeDate());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_LOGON_ALLOWED_LOGON_DAYS, user.getRacuAllowedLogonDays());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_LOGON_ALLOWED_LOGON_TIME, user.getRacuAllowedLogonTime());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_TSO_SEGMENT, user.getRacuTsoSegment());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_TSO_ACC_NUMBER, user.getRacuTsoAccNumber());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_TSO_REQUESTED_REGION_SIZE, user.getRacuTsoReqRegSize());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_TSO_MAX_REGION_SIZE, user.getRacuTsoMaxRegionSize());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_TSO_LOGON_PROCEDURE, user.getRacuTsoLogonProcedure());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_TSO_INITIAL_COMMAND, user.getRacuTsoInitialCommand());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_TSO_UNIT_NAME_ALLOC, user.getRacuTsoUnitNameClass());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_TSO_JES_EXEC_CLASS, user.getRacuTsoJesExecClass());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_TSO_JES_SYSOUT_MSG_CLASS, user.getRacuTsoJesSysoutMsgClass());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_TSO_JES_SYSOUT_CLASS, user.getRacuTsoJesSysoutClass());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_TSO_JES_HOLD_CLASS, user.getRacuTsoJesHoldClass());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_TSO_JES_DESTINATION, user.getRacuJesTsoDestination());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_TSO_USER_DATA, user.getRacuTsoUserData());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_CLASS_AUTH, user.getRacuClAuth());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_IS_ADSP, user.getRacuIsADSP());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_OMVS_IS_SEGMENT, user.getRacuIsOmvsSegment());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_OMVS_CPU, user.getRacuOmvsCpu());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_OMVS_FILES, user.getRacuOmvsFiles());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_OMVS_HOME, user.getRacuOmvsHome());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_OMVS_IS_SHARED, user.getRacuOmvsIsShared());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_OMVS_MMAP, user.getRacuOmvsMMap());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_OMVS_PROC, user.getRacuOmvsProc());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_OMVS_SHELL, user.getRacuOmvsShell());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_OMVS_STORAGE, user.getRacuOmvsStorage());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_OMVS_THREAD, user.getRacuOmvsThread());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_OMVS_UID, user.getRacuOmvsUid());
            addAttribute(builder, RacfRestConstants.USER_ATTR_RACF_MODEL, user.getRacuModel());
            addAttribute(builder, OperationalAttributes.ENABLE_NAME, user.getEnabled());

        } else if (object instanceof RGroup) {
            builder.setObjectClass(ObjectClass.GROUP);

            RGroup group = (RGroup) object;
            addAttribute(builder, RacfRestConstants.GROUP_ATTR_MEMBERS, group.getMembers());
            addAttribute(builder, RacfRestConstants.GROUP_ATTR_DESCRIPTION, group.getDescription());
        }

        LOG.ok("Object with the Id {0} succesfully build.", objectId);
        return builder.build();
    }

    private void addAttribute(ConnectorObjectBuilder builder, String name, Object value) {
        if (value == null) {
            return;
        }

        AttributeBuilder ab = new AttributeBuilder();
        ab.setName(name);

        if (value instanceof Collection) {
            ab.addValue((Collection) value);
        } else {
            ab.addValue(value);
        }

        LOG.ok("Attribute: " + name + " with value(s): " + value.toString() + " added to construction.");
        builder.addAttribute(ab.build());
    }

    @Override
    public void test() {
        try {
            connection.getTestService().test();
            LOG.info("Test service execution finished");
        } catch(Exception ex) {
            handleGenericException(ex, "Test connection failed, reason: " + ex.getMessage());
        }
    }

    @Override
    public Schema schema() {

        SchemaBuilder sb = new SchemaBuilder(RacfRestConnector.class);

        ObjectClassInfoBuilder ocBuilder = new ObjectClassInfoBuilder();
        ocBuilder.setType(ObjectClass.ACCOUNT_NAME);

        ocBuilder.addAttributeInfo(buildAttributeInfo(Uid.NAME, String.class, RacfRestConstants.OBJECT_ATTR_UID,
                AttributeInfo.Flags.NOT_UPDATEABLE));
        ocBuilder.addAttributeInfo(buildAttributeInfo(Name.NAME, String.class, RacfRestConstants.OBJECT_ATTR_NAME));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_ERUID, String.class, null,
                AttributeInfo.Flags.REQUIRED));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.OBJECT_ATTR_CHANGED, Long.class, null,
                AttributeInfo.Flags.NOT_UPDATEABLE, AttributeInfo.Flags.NOT_CREATABLE));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_NAME, String.class,
                null, AttributeInfo.Flags.REQUIRED));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_SPECIAL, Boolean.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_IS_AUDIT, Boolean.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_IS_GRP_ACC, Boolean.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_IS_OPER, Boolean.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_IS_RESTRICT, Boolean.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_IS_PROTECT, Boolean.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_IS_UAUDIT, Boolean.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_DEFAULT_GROUP, String.class,
                null, AttributeInfo.Flags.REQUIRED));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_OWNER, String.class,
                null, AttributeInfo.Flags.REQUIRED));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_CONNECT_GROUPS, String.class,
                null, AttributeInfo.Flags.MULTIVALUED));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_INST_DATA, String.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_CREATE_DATE, String.class,
                null,
                AttributeInfo.Flags.NOT_UPDATEABLE, AttributeInfo.Flags.NOT_CREATABLE));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_LAST_LOGON_DATE, String.class,
                null,
                AttributeInfo.Flags.NOT_UPDATEABLE, AttributeInfo.Flags.NOT_CREATABLE));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_LAST_CHANGE_PASSWORD, String.class,
                null,
                AttributeInfo.Flags.NOT_UPDATEABLE, AttributeInfo.Flags.NOT_CREATABLE));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_PASSWORD_CHANGE_INTERVAL,
                String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_ACCOUNT_XML, String.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_LOGON_FUTURE_REVOKE_DATE,
                String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_LOGON_FUTURE_RESUME_DATE,
                String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_LOGON_ALLOWED_LOGON_DAYS,
                String.class, null, AttributeInfo.Flags.MULTIVALUED));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_LOGON_ALLOWED_LOGON_TIME,
                String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_CLASS_AUTH, String.class,
                null, AttributeInfo.Flags.MULTIVALUED));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_IS_ADSP, String.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_MODEL, String.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_TSO_LOGON_PROCEDURE, String.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_TSO_SEGMENT, Boolean.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_TSO_ACC_NUMBER, String.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_TSO_REQUESTED_REGION_SIZE,
                String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_TSO_MAX_REGION_SIZE,
                String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_TSO_INITIAL_COMMAND,
                String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_TSO_UNIT_NAME_ALLOC,
                String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_TSO_JES_EXEC_CLASS,
                String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_TSO_JES_SYSOUT_MSG_CLASS,
                String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_TSO_JES_SYSOUT_CLASS,
                String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_TSO_JES_DESTINATION,
                String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_TSO_JES_HOLD_CLASS,
                String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_TSO_USER_DATA,
                String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_OMVS_IS_SEGMENT, Boolean.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_OMVS_CPU, String.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_OMVS_FILES, String.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_OMVS_HOME, String.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_OMVS_IS_SHARED, Boolean.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_OMVS_MMAP, String.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_OMVS_PROC, String.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_OMVS_SHELL, String.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_OMVS_STORAGE, String.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_OMVS_THREAD, String.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.USER_ATTR_RACF_OMVS_UID, String.class,
                null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(OperationalAttributes.PASSWORD_NAME, GuardedString.class,
                RacfRestConstants.USER_ATTR_PASSWORD, AttributeInfo.Flags.NOT_READABLE,
                AttributeInfo.Flags.NOT_RETURNED_BY_DEFAULT));
        ocBuilder.addAttributeInfo(buildAttributeInfo(OperationalAttributes.ENABLE_NAME, Boolean.class,
                RacfRestConstants.USER_ATTR_ENABLED));
        sb.defineObjectClass(ocBuilder.build());

        // GROUPS
        ocBuilder = new ObjectClassInfoBuilder();
        ocBuilder.setType(ObjectClass.GROUP_NAME);
        ocBuilder.setContainer(true);
        ocBuilder.addAttributeInfo(buildAttributeInfo(Uid.NAME, String.class, RacfRestConstants.OBJECT_ATTR_UID,
                AttributeInfo.Flags.NOT_UPDATEABLE, AttributeInfo.Flags.NOT_CREATABLE));
        ocBuilder.addAttributeInfo(buildAttributeInfo(Name.NAME, String.class, RacfRestConstants.OBJECT_ATTR_NAME));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.OBJECT_ATTR_CHANGED, Long.class, null,
                AttributeInfo.Flags.NOT_UPDATEABLE, AttributeInfo.Flags.NOT_CREATABLE));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.GROUP_ATTR_DESCRIPTION, String.class, null));
        ocBuilder.addAttributeInfo(buildAttributeInfo(RacfRestConstants.GROUP_ATTR_MEMBERS, String.class, null,
                AttributeInfo.Flags.MULTIVALUED));
        sb.defineObjectClass(ocBuilder.build());

        LOG.info("Connector finished building of the schema");
        return sb.build();
    }

    private AttributeInfo buildAttributeInfo(String name, Class type, String nativeName, AttributeInfo.Flags... flags) {

        AttributeInfoBuilder aib = new AttributeInfoBuilder(name);
        aib.setType(type);

        if (nativeName == null) {
            nativeName = name;
        }

        aib.setNativeName(nativeName);

        if (flags.length != 0) {
            Set<AttributeInfo.Flags> set = new HashSet<>();
            set.addAll(Arrays.asList(flags));
            aib.setFlags(set);
        }

        return aib.build();
    }

    @Override
    public FilterTranslator<Filter> createFilterTranslator(ObjectClass objectClass, OperationOptions operationOptions) {
        return new FilterTranslator<Filter>() {
            @Override
            public List<Filter> translate(Filter filter) {
                return CollectionUtil.newList(filter);
            }
        };
    }

    @Override
    public void executeQuery(ObjectClass objectClass, Filter filter, ResultsHandler resultsHandler, OperationOptions operationOptions) {

        String query = "";

        if (filter == null) {

        } else {

            query = filter.accept(new FilterHandler(), "");
            LOG.info("Query will be executed with the following filter: {0}", query);
            LOG.info("The object class from which the filter will be executed: {0}", objectClass.getDisplayNameKey());
        }

        try {
            RObjects<? extends RObject> objects;
            if (ObjectClass.ACCOUNT.equals(objectClass)) {
                objects = userService.list(query);
            } else if (ObjectClass.GROUP.equals(objectClass)){
                objects = groupService.list(query);
            } else {
                throw new UnsupportedOperationException("Unknown object class " + objectClass);
            }

            if (objects == null) {
                LOG.info("No objects returned by query.");

                return;
            }

            LOG.ok("Objects found: {0}", objects.getObjects().size());

            for (RObject object: objects.getObjects()) {
                if (object == null) {
                    continue;
                }

                ConnectorObject co = translate(object);

                if (!resultsHandler.handle(co)) {
                    break;
                }
            }
        } catch (Exception ex) {
            handleGenericException(ex, "Couldn't search " + objectClass + " with filter " + query + ", reason: " + ex.getMessage());
        }
    }

    @Override
    public Uid create(ObjectClass objectClass, Set<Attribute> set, OperationOptions operationOptions) {

        try {
            String uid;
            if (ObjectClass.ACCOUNT.equals(objectClass)) {
                RUser user = translateUser(null, set);
                uid = userService.add(user);
            } else if (ObjectClass.GROUP.equals(objectClass)) {
                RGroup group = translateGroup(null, set);
                uid = groupService.add(group);
            } else {
                throw new UnsupportedOperationException("Unkown object class " + objectClass);
            }

            LOG.ok("Created new object with the UID: {0}", uid);

            return new Uid(uid);
        } catch (Exception ex) {
            handleGenericException(ex, "Couldn't create object " + objectClass + " with attributes " + set + ", reason: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public void delete(ObjectClass objectClass, Uid uid, OperationOptions operationOptions) {
        try {
            if (ObjectClass.ACCOUNT.equals(objectClass)) {
                userService.delete(uid.getUidValue());
            } else if (ObjectClass.GROUP.equals(objectClass)) {
                groupService.delete(uid.getUidValue());
            } else {
                throw new UnsupportedOperationException("Unknown object class " + objectClass);
            }

            LOG.ok("The object with the uid {0} was deleted by the connector instance.", uid);
        } catch (Exception ex) {
            handleGenericException(ex, "Couldn't delete " + objectClass + " with the uid " + uid + ", reason: " + ex.getMessage());
        }
    }

    @Override
    public Set<AttributeDelta> updateDelta(ObjectClass objectClass, Uid uid, Set<AttributeDelta> set, OperationOptions operationOptions) {

        validate(uid, objectClass);

        Set<Attribute> attrsToReplace = new HashSet<>();
        Set<Attribute> attrsToRemove = new HashSet<>();
        Set<Attribute> attrsToAdd = new HashSet<>();

        Uid newUid = null;

        set.forEach(delta -> {

            List<Object> valuesToReplace = delta.getValuesToReplace();

            if (valuesToReplace != null) {

                attrsToReplace.add(AttributeBuilder.build(delta.getName(), valuesToReplace));
            } else {
                List<Object> valuesToRemove = delta.getValuesToRemove();
                List<Object> valuesToAdd = delta.getValuesToAdd();

                if (valuesToRemove != null) {

                    attrsToRemove.add(AttributeBuilder.build(delta.getName(), valuesToRemove));
                } else if (valuesToAdd != null) {

                    attrsToAdd.add(AttributeBuilder.build(delta.getName(), valuesToAdd));
                }
            }
        });

        try {

            ConnectorObject old = getOldObject(objectClass, uid);

            LOG.ok("Fetched the object with the uid {0} from the resource for delta update", uid.getUidValue());
            Set<Attribute> processedAttrs = new HashSet<>();

            processedAttrs.addAll(old.getAttributes());

            if (!attrsToReplace.isEmpty()) {
                LOG.ok("Processing though REPLACE set of attributes in the update attribute delta op");

                attrsToReplace.forEach(newAttr -> {

                    Attribute oldAttr = AttributeUtil.find(newAttr.getName(), processedAttrs);
                    if (oldAttr != null) {
                        processedAttrs.remove(oldAttr);
                    }
                    processedAttrs.add(newAttr);
                });
            }

            if (!attrsToAdd.isEmpty()) {
                LOG.ok("Processing through ADD set of attributes in the update attribute delta op");

                attrsToAdd.forEach(newAttr -> {

                    Attribute oldAttr = AttributeUtil.find(newAttr.getName(), processedAttrs);

                    if (oldAttr != null) {
                        List values = new ArrayList();

                        if (oldAttr.getValue() != null) {

                            values.addAll(oldAttr.getValue());
                        }

                        values.addAll(newAttr.getValue());
                        processedAttrs.remove(oldAttr);
                        processedAttrs.add(AttributeBuilder.build(oldAttr.getName(), values));
                    } else {

                        processedAttrs.add(newAttr);
                    }
                });
            }

            if (!attrsToRemove.isEmpty()) {
                LOG.ok("Processing through DELETE set of attributes in the update attribute delta op");

                attrsToRemove.forEach(newAttr -> {

                    Attribute oldAttr = AttributeUtil.find(newAttr.getName(), processedAttrs);

                    if (oldAttr != null) {

                        List values = new ArrayList();

                        if (oldAttr.getValue() != null) {

                            values.addAll(oldAttr.getValue());
                        }

                        values.removeAll(newAttr.getValue());
                        processedAttrs.remove(oldAttr);
                        processedAttrs.add(AttributeBuilder.build(oldAttr.getName(), values));
                    }
                });
            }

            newUid = updateOldObject(objectClass, uid, processedAttrs);
        } catch (Exception ex) {
            handleGenericException(ex, "Couldn't modify attribute values from object " + objectClass +
                    " with uid " + uid + ", reason: " + ex.getMessage());
        }

        Set<AttributeDelta> returnDelta = new HashSet<>();

        if (newUid != null && newUid != uid) {
            AttributeDelta newUidAttributeDelta = AttributeDeltaBuilder.build(Uid.NAME, newUid.getValue());
            returnDelta.add(newUidAttributeDelta);
        }
        return returnDelta;
    }

    private ConnectorObject getOldObject(ObjectClass oc, Uid uid) {
        RObject object;

        if (ObjectClass.ACCOUNT.equals(oc)) {
            object = userService.get(uid.getUidValue());
        } else if (ObjectClass.GROUP.equals(oc)) {
            object = groupService.get(uid.getUidValue());
        } else {
            throw new UnsupportedOperationException("Unknown object class " + oc);
        }

        if (object == null) {
            throw new UnknownUidException("Couldn't find object " + oc + " with uid " + uid);
        }

        return translate(object);
    }

    private Uid updateOldObject(ObjectClass oc, Uid uid, Set<Attribute> attributes) {
        String uidString = null;

        if (ObjectClass.ACCOUNT.equals(oc)) {
            RUser user = translateUser(uid, attributes);
            uidString = userService.update(user);
        } else if (ObjectClass.GROUP.equals(oc)) {
            RGroup group = translateGroup(uid, attributes);
            uidString = groupService.update(group);
        }

        if (uidString != null && !uidString.isEmpty()) {
            return new Uid(uidString);
        } else {
            throw new ConnectorException("Unexpected exception occurred. No uid returned by resource after update" +
                    " operation execution for the object with the uid: " + uid + ".");
        }
    }

    @Override
    public void sync(ObjectClass objectClass, SyncToken syncToken, SyncResultsHandler syncResultsHandler, OperationOptions operationOptions) {
        Long tokenLatest = (Long) getLatestSyncToken(objectClass).getValue();
        Long token = (Long) syncToken.getValue();
        boolean handlerExited = false;

        RDeltas deltas = null;
        try {
            if (ObjectClass.ACCOUNT.equals(objectClass)) {
                deltas = userService.sync(token);
            } else if (ObjectClass.GROUP.equals(objectClass)) {
                deltas = groupService.sync(token);
            } else {
                throw new UnsupportedOperationException("Unknown object class " + objectClass);
            }
        } catch (Exception e) {
            handleGenericException(e, "Could not execute the sync operation for the objectc class " + objectClass +
                    " with the token " + syncToken + ", reason " + e.getMessage());
        }

        if (deltas != null) {
            for (RDelta delta : deltas.getDeltas()) {
                SyncDeltaBuilder sdb = new SyncDeltaBuilder();
                sdb.setObjectClass(objectClass);
                sdb.setToken(syncToken);

                SyncDeltaType deltaType = RDeltaType.CHANGED.equals(delta.getType()) ?
                        SyncDeltaType.CREATE_OR_UPDATE : SyncDeltaType.DELETE;
                sdb.setDeltaType(deltaType);

                RObject object = delta.getObject();
                sdb.setUid(new Uid(object.getId()));

                if (!SyncDeltaType.DELETE.equals(deltaType)) {
                    sdb.setObject(translate(object));
                }

                if (!syncResultsHandler.handle(sdb.build())) {
                    handlerExited = true;
                    break;
                }
            }
        }

        if (syncResultsHandler instanceof SyncTokenResultsHandler) {
            SyncTokenResultsHandler h = (SyncTokenResultsHandler) syncResultsHandler;

            if (!handlerExited) {
                h.handleResult(new SyncToken(tokenLatest));
            } else {
                h.handleResult(new SyncToken(token));
            }
        }

    }

    @Override
    public SyncToken getLatestSyncToken(ObjectClass objectClass) {

        if (!ObjectClass.ACCOUNT.equals(objectClass) && !(ObjectClass.GROUP.equals(objectClass))) {
            throw new UnsupportedOperationException("Unknown object class " + objectClass);

        }
        if (ObjectClass.ACCOUNT.equals(objectClass)) {

            return new SyncToken(userService.latestToken());
        } else {

            return new SyncToken(groupService.latestToken());
        }
    }
}
