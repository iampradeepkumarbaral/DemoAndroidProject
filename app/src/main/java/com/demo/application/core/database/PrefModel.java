package com.demo.application.core.database;


import com.demo.application.home.data.model.AssignmentReqModel;
import com.demo.application.home.data.model.DashboardResModel;
import com.demo.application.home.data.model.FbGoogleUserModel;
import com.demo.application.home.data.model.response.CheckVerResModel;
import com.demo.application.home.data.model.response.DynamiclinkResModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrefModel implements Serializable {

    private boolean isTour;
    private boolean isLogin;
    private String userLoginId;
    private String userCountry;
    private String countryNameCode;
    private String countryPhoneCode;

    private String currentLatitude;
    private String currentLongitude;
    private boolean isEmail;
    private String signupPrefilledData;

    private String emailId;

    private String deviceToken;


    private String updateMobileNum;
    private String updateEmailId;

    // Verify Number Response
    // Verify Login Response
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private Integer expiresIn;
    private String scope;
    private String message;
    private String profileTimestamp;
    private boolean introScreen;
    private int studentClass;
    private int userType;
    private String userMobile;
    private String userName;

    private String userKYCProfilePhotoPath;
    private String userLanguage;

    private DashboardResModel dashboardResModel;

    private AssignmentReqModel assignmentReqModel;
    private boolean tooltipStatus;
    private DynamiclinkResModel dynamiclinkResModel;

    private List<AssignmentReqModel> listAzureImageList = new ArrayList<>();

    public CheckVerResModel getCheckVerResModel() {
        return checkVerResModel;
    }

    public void setCheckVerResModel(CheckVerResModel checkVerResModel) {
        this.checkVerResModel = checkVerResModel;
    }

    CheckVerResModel checkVerResModel;


    public String getUserKYCProfilePhotoPath() {
        return userKYCProfilePhotoPath;
    }

    public void setUserKYCProfilePhotoPath(String userKYCProfilePhotoPath) {
        this.userKYCProfilePhotoPath = userKYCProfilePhotoPath;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    public DashboardResModel getDashboardResModel() {
        return dashboardResModel;
    }

    public void setDashboardResModel(DashboardResModel dashboardResModel) {
        this.dashboardResModel = dashboardResModel;
    }

    public AssignmentReqModel getAssignmentReqModel() {
        return assignmentReqModel;
    }

    public void setAssignmentReqModel(AssignmentReqModel assignmentReqModel) {
        this.assignmentReqModel = assignmentReqModel;
    }

    public boolean isTooltipStatus() {
        return tooltipStatus;
    }

    public void setTooltipStatus(boolean tooltipStatus) {
        this.tooltipStatus = tooltipStatus;
    }

    public List<AssignmentReqModel> getListAzureImageList() {
        return listAzureImageList;
    }

    public void setListAzureImageList(List<AssignmentReqModel> listAzureImageList) {
        this.listAzureImageList = listAzureImageList;
    }

    public FbGoogleUserModel getFbuserModel() {
        return fbuserModel;
    }

    public void setFbuserModel(FbGoogleUserModel fbuserModel) {
        this.fbuserModel = fbuserModel;
    }

    private FbGoogleUserModel fbuserModel;

    public String getProfileTimestamp() {
        return profileTimestamp;
    }

    public void setProfileTimestamp(String profileTimestamp) {
        this.profileTimestamp = profileTimestamp;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isTour() {
        return isTour;
    }

    public void setTour(boolean tour) {
        isTour = tour;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }


    public boolean isLogin() {
        return isLogin;
    }

    public String getSignupPrefilledData() {
        return signupPrefilledData;
    }

    public void setSignupPrefilledData(String signupPrefilledData) {
        this.signupPrefilledData = signupPrefilledData;
    }

    public boolean isEmail() {
        return isEmail;
    }

    public void setEmail(boolean email) {
        isEmail = email;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getCountryNameCode() {
        return countryNameCode;
    }

    public void setCountryNameCode(String countryNameCode) {
        this.countryNameCode = countryNameCode;
    }

    public String getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public void setCountryPhoneCode(String countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }


    public String getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(String currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public String getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(String currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }


    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUpdateMobileNum() {
        return updateMobileNum;
    }

    public void setUpdateMobileNum(String updateMobileNum) {
        this.updateMobileNum = updateMobileNum;
    }

    public String getUpdateEmailId() {
        return updateEmailId;
    }

    public void setUpdateEmailId(String updateEmailId) {
        this.updateEmailId = updateEmailId;
    }

    public boolean isIntroScreen() {
        return introScreen;
    }

    public void setIntroScreen(boolean introScreen) {
        this.introScreen = introScreen;
    }


    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(int studentClass) {
        this.studentClass = studentClass;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public DynamiclinkResModel getDynamiclinkResModel() {
        return dynamiclinkResModel;
    }

    public void setDynamiclinkResModel(DynamiclinkResModel dynamiclinkResModel) {
        this.dynamiclinkResModel = dynamiclinkResModel;
    }
}
