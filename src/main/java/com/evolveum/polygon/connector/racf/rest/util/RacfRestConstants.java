package com.evolveum.polygon.connector.racf.rest.util;

public interface RacfRestConstants {

    String OBJECT_ATTR_UID = "uid";
    String OBJECT_ATTR_NAME = "name";
    String OBJECT_ATTR_CHANGED = "changed";

    /* RACF ACCOUNT */
    String USER_ATTR_RACF_NAME = "racuName";
    String USER_ATTR_RACF_ERUID = "racuErUid";
    String USER_ATTR_RACF_DEFAULT_GROUP = "racuDefaultGroup";
    String USER_ATTR_RACF_OWNER = "racuOwner";
    String USER_ATTR_RACF_CONNECT_GROUPS = "racuConnectGroups";
    String USER_ATTR_RACF_INST_DATA = "racuInstData";
    String USER_ATTR_RACF_CREATE_DATE = "racuCreateDate";
    String USER_ATTR_RACF_LAST_LOGON_DATE = "racuLastLogonDate";
    String USER_ATTR_RACF_LAST_CHANGE_PASSWORD = "racuLastChangePassword";
    String USER_ATTR_RACF_PASSWORD_CHANGE_INTERVAL = "racuPasswordChangeInterval";
    String USER_ATTR_RACF_ACCOUNT_XML = "racuAccountXml";
    String USER_ATTR_RACF_SPECIAL = "racuIsSpecial";
    String USER_ATTR_RACF_IS_AUDIT = "racuIsAudit";
    String USER_ATTR_RACF_IS_OPER = "racuIsOper";
    String USER_ATTR_RACF_IS_PROTECT = "racuIsProtect";
    String USER_ATTR_RACF_IS_RESTRICT = "racuIsRestrict";
    String USER_ATTR_RACF_IS_UAUDIT = "racuIsUAudit";
    String USER_ATTR_RACF_IS_GRP_ACC = "racuisGrpAcc";
    String USER_ATTR_RACF_CLASS_AUTH = "racuClAuth";
    String USER_ATTR_RACF_MODEL = "racuModel";
    String USER_ATTR_RACF_IS_ADSP = "racuIsADSP";

    /* LOGON RESTRICTIONS */
    String USER_ATTR_RACF_LOGON_FUTURE_REVOKE_DATE = "racuFutureRevokeDate";
    String USER_ATTR_RACF_LOGON_FUTURE_RESUME_DATE = "racuFutureResumeDate";
    String USER_ATTR_RACF_LOGON_ALLOWED_LOGON_DAYS = "racuAllowedLogonDays";
    String USER_ATTR_RACF_LOGON_ALLOWED_LOGON_TIME = "racuAllowedLogonTime";

    /* TSO SEGMENT */
    String USER_ATTR_RACF_TSO_SEGMENT = "racuTsoSegment";
    String USER_ATTR_RACF_TSO_ACC_NUMBER = "racuTsoAccNumber";
    String USER_ATTR_RACF_TSO_REQUESTED_REGION_SIZE = "racuTsoReqRegSize";
    String USER_ATTR_RACF_TSO_MAX_REGION_SIZE = "racuTsoMaxRegionSize";
    String USER_ATTR_RACF_TSO_LOGON_PROCEDURE = "racuTsoLogonProcedure";
    String USER_ATTR_RACF_TSO_INITIAL_COMMAND = "racuTsoInitialCommand";
    String USER_ATTR_RACF_TSO_UNIT_NAME_ALLOC = "racuTsoUnitNameClass";
    String USER_ATTR_RACF_TSO_JES_EXEC_CLASS = "racuTsoJesExecClass";
    String USER_ATTR_RACF_TSO_JES_SYSOUT_MSG_CLASS = "racuTsoJesSysoutMsgClass";
    String USER_ATTR_RACF_TSO_JES_SYSOUT_CLASS = "racuTsoJesSysoutClass";
    String USER_ATTR_RACF_TSO_JES_HOLD_CLASS = "racuTsoJesHoldClass";
    String USER_ATTR_RACF_TSO_JES_DESTINATION = "racuJesTsoDestination";
    String USER_ATTR_RACF_TSO_USER_DATA = "racuTsoUserData";

    /*OMVS SEGMENT*/
    String USER_ATTR_RACF_OMVS_IS_SEGMENT = "racuOmvsSegment";
    String USER_ATTR_RACF_OMVS_CPU = "racuOmvsCpu";
    String USER_ATTR_RACF_OMVS_FILES = "racuOmvsFiles";
    String USER_ATTR_RACF_OMVS_HOME = "racuOmvsHome";
    String USER_ATTR_RACF_OMVS_IS_SHARED = "racuOmvsIsShared";
    String USER_ATTR_RACF_OMVS_MMAP = "racuOmvsMMap";
    String USER_ATTR_RACF_OMVS_PROC = "racuOmvsProc";
    String USER_ATTR_RACF_OMVS_SHELL = "racuOmvsShell";
    String USER_ATTR_RACF_OMVS_STORAGE = "racuOmvsStorage";
    String USER_ATTR_RACF_OMVS_THREAD = "racuOmvsThread";
    String USER_ATTR_RACF_OMVS_UID = "racuOmvsUid";

    String USER_ATTR_PASSWORD = "password";
    String USER_ATTR_ENABLED = "enabled";


    String GROUP_ATTR_DESCRIPTION = "description";
    String GROUP_ATTR_MEMBERS = "members";
}
