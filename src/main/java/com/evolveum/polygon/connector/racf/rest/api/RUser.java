package com.evolveum.polygon.connector.racf.rest.api;

import org.identityconnectors.framework.common.exceptions.InvalidAttributeValueException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class RUser extends RObject{

    private String racuName;
    private String racuDefaultGroup;
    private String racuOwner;
    private String racuErUid;
    private List<String> racuConnectGroups;
    private String racuPasswordChangeInterval;
    private List<String> racuAccountXml;
    private String racuInstData;
    private String racuCreateDate;
    private String racuLastLogonDate;
    private String racuLastChangePassword;
    private Boolean racuIsSpecial;
    private Boolean racuIsAudit;
    private Boolean racuIsOper;
    private Boolean racuIsProtect;
    private Boolean racuIsRestrict;
    private Boolean racuIsUAudit;
    private Boolean racuisGrpAcc;
    private Date racuFutureRevokeDate;
    private Date racuFutureResumeDate;
    private List<String> racuAllowedLogonDays;
    private String racuAllowedLogonTime;
    private Boolean racuTsoSegment;
    private String racuTsoAccNumber;
    private String racuTsoReqRegSize;
    private String racuTsoMaxRegionSize;
    private String racuTsoLogonProcedure;
    private String racuTsoInitialCommand;
    private String racuTsoUnitNameClass;
    private String racuTsoJesExecClass;
    private String racuTsoJesSysoutMsgClass;
    private String racuTsoJesSysoutClass;
    private String racuTsoJesHoldClass;
    private String racuJesTsoDestination;
    private String racuTsoUserData;
    private List<String> racuClAuth;
    private Boolean racuIsADSP;
    private Boolean racuIsOmvsSegment;
    private String racuOmvsCpu;
    private String racuOmvsFiles;
    private String racuOmvsHome;
    private Boolean racuOmvsIsShared;
    private String racuOmvsMMap;
    private String racuOmvsProc;
    private String racuOmvsShell;
    private String racuOmvsStorage;
    private String racuOmvsThread;
    private String racuOmvsUid;
    private String racuModel;

    private String password;

    private Boolean enabled;

    public RUser() {
    }

    public void setRacuClAuth(List<String> racuClAuth) {
        this.racuClAuth = racuClAuth;
    }

    public List<String> getRacuClAuth() {

        if (racuClAuth == null) {
            racuClAuth = new ArrayList<>();
        }
        return racuClAuth;
    }

    public void setRacuIsADSP(Boolean racuIsADSP) { this.racuIsADSP = racuIsADSP; }

    public Boolean getRacuIsADSP() { return this.racuIsADSP; }

    public void setRacuIsOmvsSegment(Boolean racuIsOmvsSegment) { this.racuIsOmvsSegment = racuIsOmvsSegment; }

    public Boolean getRacuIsOmvsSegment() { return this.racuIsOmvsSegment; }

    public void setRacuOmvsCpu(String racuOmvsCpu) {
       this.racuOmvsCpu = racuOmvsCpu;
    }

    public String getRacuOmvsCpu() { return this.racuOmvsCpu; }

    public void setRacuOmvsFiles(String racuOmvsFiles){
        this.racuOmvsFiles = racuOmvsFiles;
    }

    public String getRacuOmvsFiles() { return this.racuOmvsFiles; }

    public void setRacuOmvsHome(String racuOmvsHome) { this.racuOmvsHome = racuOmvsHome; }

    public String getRacuOmvsHome() { return this.racuOmvsHome; }

    public void setRacuOmvsIsShared(Boolean racuOmvsIsShared) { this.racuOmvsIsShared = racuOmvsIsShared; }

    public Boolean getRacuOmvsIsShared() { return this.racuOmvsIsShared; }

    public void setRacuOmvsMMap(String racuOmvsMMap) { this.racuOmvsMMap = racuOmvsMMap; }

    public String getRacuOmvsMMap() { return this.racuOmvsMMap; }

    public void setRacuOmvsProc(String racuOmvsProc) { this.racuOmvsProc = racuOmvsProc; }

    public String getRacuOmvsProc() { return this.racuOmvsProc; }

    public void setRacuOmvsShell(String racuOmvsShell) { this.racuOmvsShell = racuOmvsShell; }

    public String getRacuOmvsShell() { return this.racuOmvsShell; }

    public void setRacuOmvsStorage(String racuOmvsStorage) { this.racuOmvsStorage = racuOmvsStorage; }

    public String getRacuOmvsStorage() { return this.racuOmvsStorage; }

    public void setRacuOmvsThread(String racuOmvsThread) { this.racuOmvsThread = racuOmvsThread; }

    public String getRacuOmvsThread() { return this.racuOmvsThread; }

    public void setRacuOmvsUid(String racuOmvsUid) { this.racuOmvsUid = racuOmvsUid; }

    public String getRacuOmvsUid() { return this.racuOmvsUid; }

    public void setRacuModel(String racuModel) { this.racuModel = racuModel; }

    public String getRacuModel() { return this.racuModel; }

    public String getRacuName() {
        return racuName;
    }

    public void setRacuName(String racuName) {
        this.racuName = racuName;
    }

    public String getRacuErUid() { return racuErUid; }

    public void setRacuErUid(String racuErUid) { this.racuErUid = racuErUid; }

    public Boolean getRacuIsSpecial() {
        return racuIsSpecial;
    }

    public void setRacuIsSpecial(Boolean racuIsSpecial) {
        this.racuIsSpecial = racuIsSpecial;
    }

    public Boolean getRacuIsAudit() {
        return racuIsAudit;
    }

    public void setRacuIsAudit(Boolean racuIsAudit) {
        this.racuIsAudit = racuIsAudit;
    }

    public Boolean getRacuIsOper() {
        return racuIsOper;
    }

    public void setRacuIsOper(Boolean racuIsOper) {
        this.racuIsOper = racuIsOper;
    }

    public Boolean getRacuIsProtect() {
        return racuIsProtect;
    }

    public void setRacuIsProtect(Boolean racuIsProtect) {
        this.racuIsProtect = racuIsProtect;
    }

    public Boolean getRacuIsRestrict() {
        return racuIsRestrict;
    }

    public void setRacuIsRestrict(Boolean racuIsRestrict) {
        this.racuIsRestrict = racuIsRestrict;
    }

    public Boolean getRacuIsUAudit() {
        return racuIsUAudit;
    }

    public void setRacuIsUAudit(Boolean racuIsUAudit) {
        this.racuIsUAudit = racuIsUAudit;
    }

    public Boolean getRacuisGrpAcc() {
        return racuisGrpAcc;
    }

    public void setRacuisGrpAcc(Boolean racuisGrpAcc) {
        this.racuisGrpAcc = racuisGrpAcc;
    }

    public String getRacuDefaultGroup() {
        return racuDefaultGroup;
    }

    public void setRacuDefaultGroup(String racuDefaultGroup) {
        this.racuDefaultGroup = racuDefaultGroup;
    }

    public String getRacuOwner() {
        return racuOwner;
    }

    public void setRacuOwner(String racuOwner) {
        this.racuOwner = racuOwner;
    }

    public List<String> getRacuConnectGroups() {

        if (racuConnectGroups == null) {
            racuConnectGroups = new ArrayList<>();
        }
        return racuConnectGroups;
    }

    public void setRacuConnectGroups(List<String> racuConnectGroups) {
        this.racuConnectGroups = racuConnectGroups;
    }

    public String getRacuInstData() {
        return racuInstData;
    }

    public void setRacuInstData(String racuInstData) {
        this.racuInstData = racuInstData;
    }

    public String getRacuCreateDate() {
        return racuCreateDate;
    }

    public void setRacuCreateDate(String racuCreateDate) {
        this.racuCreateDate = racuCreateDate;
    }

    public String getRacuLastLogonDate() {
        return racuLastLogonDate;
    }

    public void setRacuLastLogonDate(String racuLastLogonDate) {
        this.racuLastLogonDate = racuLastLogonDate;
    }

    public String getRacuLastChangePassword() {
        return racuLastChangePassword;
    }

    public void setRacuLastChangePassword(String racuLastChangePassword) {
        this.racuLastChangePassword = racuLastChangePassword;
    }

    public Boolean getRacuTsoSegment() {
        return racuTsoSegment;
    }

    public void setRacuTsoSegment(Boolean racuTsoSegment) {
        this.racuTsoSegment = racuTsoSegment;
    }

    public String getRacuTsoAccNumber() {
        return racuTsoAccNumber;
    }

    public void setRacuTsoAccNumber(String racuTsoAccNumber) {
        this.racuTsoAccNumber = racuTsoAccNumber;
    }

    public String getRacuTsoReqRegSize() {
        return racuTsoReqRegSize;
    }

    public void setRacuTsoReqRegSize(String racuTsoReqRegSize) {
        this.racuTsoReqRegSize = racuTsoReqRegSize;
    }

    public String getRacuTsoMaxRegionSize() {
        return racuTsoMaxRegionSize;
    }

    public void setRacuTsoMaxRegionSize(String racuTsoMaxRegionSize) {
        this.racuTsoMaxRegionSize = racuTsoMaxRegionSize;
    }

    public String getRacuTsoLogonProcedure() {
        return racuTsoLogonProcedure;
    }

    public void setRacuTsoLogonProcedure(String racuTsoLogonProcedure) {
        this.racuTsoLogonProcedure = racuTsoLogonProcedure;
    }

    public String getRacuTsoInitialCommand() {
        return racuTsoInitialCommand;
    }

    public void setRacuTsoInitialCommand(String racuTsoInitialCommand) {
        this.racuTsoInitialCommand = racuTsoInitialCommand;
    }

    public String getRacuTsoUnitNameClass() {
        return racuTsoUnitNameClass;
    }

    public void setRacuTsoUnitNameClass(String racuTsoUnitNameClass) {
        this.racuTsoUnitNameClass = racuTsoUnitNameClass;
    }

    public String getRacuTsoJesExecClass() {
        return racuTsoJesExecClass;
    }

    public void setRacuTsoJesExecClass(String racuTsoJesExecClass) {
        this.racuTsoJesExecClass = racuTsoJesExecClass;
    }

    public String getRacuTsoJesSysoutMsgClass() {
        return racuTsoJesSysoutMsgClass;
    }

    public void setRacuTsoJesSysoutMsgClass(String racuTsoJesSysoutMsgClass) {
        this.racuTsoJesSysoutMsgClass = racuTsoJesSysoutMsgClass;
    }

    public String getRacuTsoJesSysoutClass() {
        return racuTsoJesSysoutClass;
    }

    public void setRacuTsoJesSysoutClass(String racuTsoJesSysoutClass) {
        this.racuTsoJesSysoutClass = racuTsoJesSysoutClass;
    }

    public String getRacuTsoJesHoldClass() {
        return racuTsoJesHoldClass;
    }

    public void setRacuTsoJesHoldClass(String racuTsoJesHoldClass) {
        this.racuTsoJesHoldClass = racuTsoJesHoldClass;
    }

    public String getRacuJesTsoDestination() {
        return racuJesTsoDestination;
    }

    public void setRacuJesTsoDestination(String racuJesTsoDestination) {
        this.racuJesTsoDestination = racuJesTsoDestination;
    }

    public String getRacuTsoUserData() {
        return racuTsoUserData;
    }

    public void setRacuTsoUserData(String racuTsoUserData) {
        this.racuTsoUserData = racuTsoUserData;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
       if (this == o) return true;
       if (o == null || getClass() != o.getClass()) return false;
       if (!super.equals(o)) return false;

       RUser rUser = (RUser) o;

       if (!Objects.equals(racuName, rUser.racuName)) return false;
       if (!Objects.equals(racuErUid, rUser.racuErUid)) return false;
       if (!Objects.equals(racuIsAudit, rUser.racuIsAudit)) return false;
       if (!Objects.equals(racuisGrpAcc, rUser.racuisGrpAcc)) return false;
       if (!Objects.equals(racuIsOper, rUser.racuIsOper)) return false;
       if (!Objects.equals(racuIsProtect, rUser.racuIsProtect)) return false;
       if (!Objects.equals(racuIsRestrict, rUser.racuIsRestrict)) return false;
       if (!Objects.equals(racuIsSpecial, rUser.racuIsSpecial)) return false;
       if (!Objects.equals(racuIsUAudit, rUser.racuIsUAudit)) return false;
       if (!Objects.equals(racuDefaultGroup, rUser.racuDefaultGroup)) return false;
       if (!Objects.equals(racuOwner, rUser.racuOwner)) return false;
       if (!Objects.equals(racuConnectGroups, rUser.racuConnectGroups)) return false;
       if (!Objects.equals(racuInstData, rUser.racuInstData)) return false;
       if (!Objects.equals(racuCreateDate, rUser.racuCreateDate)) return false;
       if (!Objects.equals(racuLastLogonDate, rUser.racuLastLogonDate)) return false;
       if (!Objects.equals(racuLastChangePassword, rUser.racuLastChangePassword)) return false;
       if (!Objects.equals(racuTsoSegment, rUser.racuTsoSegment)) return false;
       if (!Objects.equals(racuTsoAccNumber, rUser.racuTsoAccNumber)) return false;
       if (!Objects.equals(racuTsoReqRegSize, rUser.racuTsoReqRegSize)) return false;
       if (!Objects.equals(racuTsoMaxRegionSize, rUser.racuTsoMaxRegionSize)) return false;
       if (!Objects.equals(racuTsoLogonProcedure, rUser.racuTsoLogonProcedure)) return false;
       if (!Objects.equals(racuTsoInitialCommand, rUser.racuTsoInitialCommand)) return false;
       if (!Objects.equals(racuTsoUnitNameClass, rUser.racuTsoUnitNameClass)) return false;
       if (!Objects.equals(racuTsoJesExecClass, rUser.racuTsoJesExecClass)) return false;
       if (!Objects.equals(racuTsoJesSysoutMsgClass, rUser.racuTsoJesSysoutMsgClass)) return false;
       if (!Objects.equals(racuTsoJesSysoutClass, rUser.racuTsoJesSysoutClass)) return false;
       if (!Objects.equals(racuTsoJesHoldClass, rUser.racuTsoJesHoldClass)) return false;
       if (!Objects.equals(racuJesTsoDestination, rUser.racuJesTsoDestination)) return false;
       if (!Objects.equals(racuTsoUserData, rUser.racuTsoUserData)) return false;
       if (!Objects.equals(racuPasswordChangeInterval, rUser.racuPasswordChangeInterval)) return false;
       if (!Objects.equals(racuFutureRevokeDate, rUser.racuFutureRevokeDate)) return false;
       if (!Objects.equals(racuFutureResumeDate, rUser.racuFutureResumeDate)) return false;
       if (!Objects.equals(racuAllowedLogonDays, rUser.racuAllowedLogonDays)) return false;
       if (!Objects.equals(racuAllowedLogonTime, rUser.racuAllowedLogonTime)) return false;
       if (!Objects.equals(racuClAuth, rUser.racuClAuth)) return false;
       if (!Objects.equals(racuIsADSP, rUser.racuIsADSP)) return false;
       if (!Objects.equals(racuIsOmvsSegment, rUser.racuIsOmvsSegment)) return false;
       if (!Objects.equals(racuOmvsCpu, rUser.racuOmvsCpu)) return false;
       if (!Objects.equals(racuOmvsFiles, rUser.racuOmvsFiles)) return false;
       if (!Objects.equals(racuOmvsHome, rUser.racuOmvsHome)) return false;
       if (!Objects.equals(racuOmvsIsShared, rUser.racuOmvsIsShared)) return false;
       if (!Objects.equals(racuOmvsMMap, rUser. racuOmvsMMap)) return false;
       if (!Objects.equals(racuOmvsProc, rUser.racuOmvsProc)) return false;
       if (!Objects.equals(racuOmvsShell, rUser.racuOmvsShell)) return false;
       if (!Objects.equals(racuOmvsStorage, rUser.racuOmvsStorage)) return false;
       if (!Objects.equals(racuOmvsThread, rUser.racuOmvsThread)) return false;
       if (!Objects.equals(racuOmvsUid, rUser.racuOmvsUid)) return false;
       if (!Objects.equals(racuModel, rUser.racuModel)) return false;
       if (!Objects.equals(password, rUser.password)) return false;

       return Objects.equals(enabled, rUser.enabled);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();

        result = 31 * result + (racuName != null ? racuName.hashCode() : 0);
        result = 31 * result + (racuErUid != null ? racuErUid.hashCode() : 0);
        result = 31 * result + (racuIsAudit != null ? racuIsAudit.hashCode() : 0);
        result = 31 * result + (racuisGrpAcc != null ? racuisGrpAcc.hashCode() : 0);
        result = 31 * result + (racuIsOper != null ? racuIsOper.hashCode() : 0);
        result = 31 * result + (racuIsProtect != null ? racuIsProtect.hashCode() : 0);
        result = 31 * result + (racuIsRestrict != null ? racuIsRestrict.hashCode() : 0);
        result = 31 * result + (racuIsSpecial != null ? racuIsSpecial.hashCode() : 0);
        result = 31 * result + (racuIsUAudit != null ? racuIsUAudit.hashCode() : 0);
        result = 31 * result + (racuDefaultGroup != null ? racuDefaultGroup.hashCode() : 0);
        result = 31 * result + (racuOwner != null ? racuOwner.hashCode() : 0);
        result = 31 * result + (racuConnectGroups != null ? racuConnectGroups.hashCode() : 0);
        result = 31 * result + (racuInstData != null ? racuInstData.hashCode() : 0);
        result = 31 * result + (racuCreateDate != null ? racuCreateDate.hashCode() : 0);
        result = 31 * result + (racuLastLogonDate != null ? racuLastLogonDate.hashCode() : 0);
        result = 31 * result + (racuLastChangePassword != null ? racuLastChangePassword.hashCode() : 0);
        result = 31 * result + (racuPasswordChangeInterval != null ? racuPasswordChangeInterval.hashCode() : 0);
        result = 31 * result + (racuAccountXml != null ? racuAccountXml.hashCode() : 0);
        result = 31 * result + (racuFutureRevokeDate != null ? racuFutureRevokeDate.hashCode() : 0);
        result = 31 * result + (racuFutureResumeDate != null ? racuFutureResumeDate.hashCode() : 0);
        result = 31 * result + (racuAllowedLogonDays != null ? racuAllowedLogonDays.hashCode() : 0);
        result = 31 * result + (racuAllowedLogonTime != null ? racuAllowedLogonTime.hashCode() : 0);
        result = 31 * result + (racuTsoSegment != null ? racuTsoSegment.hashCode() : 0);
        result = 31 * result + (racuTsoAccNumber != null ? racuTsoAccNumber.hashCode() : 0);
        result = 31 * result + (racuTsoReqRegSize != null ? racuTsoReqRegSize.hashCode() : 0);
        result = 31 * result + (racuTsoMaxRegionSize != null ? racuTsoMaxRegionSize.hashCode() : 0);
        result = 31 * result + (racuTsoLogonProcedure != null ? racuTsoLogonProcedure.hashCode() : 0);
        result = 31 * result + (racuTsoInitialCommand != null ? racuTsoInitialCommand.hashCode() : 0);
        result = 31 * result + (racuTsoUnitNameClass != null ? racuTsoUnitNameClass.hashCode() : 0);
        result = 31 * result + (racuTsoJesExecClass != null ? racuTsoJesExecClass.hashCode() : 0);
        result = 31 * result + (racuTsoJesSysoutMsgClass != null ? racuTsoJesSysoutMsgClass.hashCode() : 0);
        result = 31 * result + (racuTsoJesSysoutClass != null ? racuTsoJesSysoutClass.hashCode() : 0);
        result = 31 * result + (racuTsoJesHoldClass != null ? racuTsoJesHoldClass.hashCode() : 0);
        result = 31 * result + (racuJesTsoDestination != null ? racuJesTsoDestination.hashCode() : 0);
        result = 31 * result + (racuTsoUserData != null ? racuTsoUserData.hashCode() : 0);
        result = 31 * result + (racuClAuth != null ? racuClAuth.hashCode() : 0);
        result = 31 * result + (racuIsADSP != null ? racuIsADSP.hashCode() : 0);
        result = 31 * result + (racuIsOmvsSegment != null ? racuIsOmvsSegment.hashCode() : 0);
        result = 31 * result + (racuOmvsCpu != null ? racuOmvsCpu.hashCode() : 0);
        result = 31 * result + (racuOmvsFiles != null ? racuOmvsFiles.hashCode() : 0);
        result = 31 * result + (racuOmvsHome != null ? racuOmvsHome.hashCode() : 0);
        result = 31 * result + (racuOmvsIsShared != null ? racuOmvsIsShared.hashCode() : 0);
        result = 31 * result + (racuOmvsMMap != null ? racuOmvsMMap.hashCode() : 0);
        result = 31 * result + (racuOmvsProc != null ? racuOmvsProc.hashCode() : 0);
        result = 31 * result + (racuOmvsStorage != null ? racuOmvsStorage.hashCode() : 0);
        result = 31 * result + (racuOmvsThread != null ? racuOmvsThread.hashCode() : 0);
        result = 31 * result + (racuOmvsUid != null ? racuOmvsUid.hashCode() : 0);
        result = 31 * result + (racuModel != null ? racuModel.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (enabled != null ? enabled.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RUser{");
        sb.append("racuName='").append(racuName).append('\'');
        sb.append("racuErUid='").append(racuErUid).append('\'');
        sb.append(", racuIsAudit='").append(racuIsAudit).append('\'');
        sb.append(", racuIsGrpAcc='").append(racuisGrpAcc).append('\'');
        sb.append(", racuIsOper='").append(racuIsOper).append('\'');
        sb.append(", racuIsProtect='").append(racuIsProtect).append('\'');
        sb.append(", racuIsRestrict='").append(racuIsRestrict).append('\'');
        sb.append(", racuIsSpecial='").append(racuIsSpecial).append('\'');
        sb.append(", racuIsUAudit='").append(racuIsUAudit).append('\'');
        sb.append(", racuDefaultGroup='").append(racuDefaultGroup).append('\'');
        sb.append(", racuOwner='").append(racuOwner).append('\'');
        sb.append(", racuConnectGroups='").append(racuConnectGroups).append('\'');
        sb.append(", racuInstData='").append(racuInstData).append('\'');
        sb.append(", racuCreateDate='").append(racuCreateDate).append('\'');
        sb.append(", racuLastLogonDate='").append(racuLastLogonDate).append('\'');
        sb.append(", racuLastChangePassword='").append(racuLastChangePassword).append('\'');
        sb.append(", racuPasswordChangeInterval='").append(racuPasswordChangeInterval).append('\'');
        sb.append(", racuAccountXml='").append(racuAccountXml).append('\'');
        sb.append(", racuFutureRevokeDate='").append(racuFutureRevokeDate).append('\'');
        sb.append(", racuFutureResumeDate='").append(racuFutureResumeDate).append('\'');
        sb.append(", racuAllowedLogonDays='").append(racuAllowedLogonDays).append('\'');
        sb.append(", racuAllowedLogonTime='").append(racuAllowedLogonTime).append('\'');
        sb.append(", racuTsoSegment='").append(racuTsoSegment).append('\'');
        sb.append(", racuTsoAccNumber='").append(racuTsoAccNumber).append('\'');
        sb.append(", racuTsoReqRegSize='").append(racuTsoReqRegSize).append('\'');
        sb.append(", racuTsoMaxRegionSize='").append(racuTsoMaxRegionSize).append('\'');
        sb.append(", racuTsoLogonProcedure='").append(racuTsoLogonProcedure).append('\'');
        sb.append(", racuTsoInitialCommand='").append(racuTsoInitialCommand).append('\'');
        sb.append(", racuTsoUnitNameClass='").append(racuTsoUnitNameClass).append('\'');
        sb.append(", racuTsoJesExecClass='").append(racuTsoJesExecClass).append('\'');
        sb.append(", racuTsoJesSysoutMsgClass='").append(racuTsoJesSysoutMsgClass).append('\'');
        sb.append(", racuTsoJesSysoutClass='").append(racuTsoJesSysoutClass).append('\'');
        sb.append(", racuTsoJesHoldClass='").append(racuTsoJesHoldClass).append('\'');
        sb.append(", racuJesTsoDestination='").append(racuJesTsoDestination).append('\'');
        sb.append(", racuTsoUserData='").append(racuTsoUserData).append('\'');
        sb.append(", enabled=").append(enabled);

        return sb.toString();
    }

    public String getRacuPasswordChangeInterval() {
        return racuPasswordChangeInterval;
    }

    public void setRacuPasswordChangeInterval(String racuPasswordChangeInterval) {
        this.racuPasswordChangeInterval = racuPasswordChangeInterval;
    }

    public List<String> getRacuAccountXml() {
        if (racuAccountXml == null) {
            racuAccountXml = new ArrayList<>();
        }
        return racuAccountXml;
    }

    public void setRacuAccountXml(List<String> racuAccountXml) {
        this.racuAccountXml = racuAccountXml;
    }

    public Date getRacuFutureRevokeDate() {
        return racuFutureRevokeDate;
    }

    public void setRacuFutureRevokeDate(Date racuFutureRevokeDate) {
        this.racuFutureRevokeDate = racuFutureRevokeDate;
    }

    public Date getRacuFutureResumeDate() {
        return racuFutureResumeDate;
    }

    public void setRacuFutureResumeDate(Date racuFutureResumeDate) {
        this.racuFutureResumeDate = racuFutureResumeDate;
    }

    public List<String> getRacuAllowedLogonDays() {
        if (racuAllowedLogonDays == null) {
            racuAllowedLogonDays = new ArrayList<>();
        }
        return racuAllowedLogonDays;
    }

    public void setRacuAllowedLogonDays(List<String> racuAllowedLogonDays) {
        this.racuAllowedLogonDays = racuAllowedLogonDays;
    }

    public String getRacuAllowedLogonTime() {
        return racuAllowedLogonTime;
    }

    public void setRacuAllowedLogonTime(String racuAllowedLogonTime) {
        this.racuAllowedLogonTime = racuAllowedLogonTime;
    }
}
