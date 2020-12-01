package com.demo.application.home.data.repository;

import com.demo.application.home.data.model.AssignmentReqModel;
import com.demo.application.home.data.model.AuroScholarDataModel;
import com.demo.application.home.data.model.CheckUserApiReqModel;
import com.demo.application.home.data.model.DemographicResModel;
import com.demo.application.home.data.model.KYCDocumentDatamodel;
import com.demo.application.home.data.model.KYCInputModel;
import com.demo.application.home.data.model.SendOtpReqModel;
import com.demo.application.home.data.model.VerifyOtpReqModel;
import com.demo.application.home.data.model.response.CertificateResModel;
import com.demo.application.home.data.model.response.CheckUserValidResModel;

import com.demo.application.home.data.model.response.DynamiclinkResModel;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;

public interface HomeRepo {

    interface DashboardRemoteData {
        Single<Response<JsonObject>> getStoreOnlineData(String modifiedTime);
        Single<Response<JsonObject>> sendOtpHomeRepo(SendOtpReqModel reqModel);
        Single<Response<JsonObject>> verifyOtpHomeRepo(VerifyOtpReqModel reqModel);
        Single<Response<JsonObject>> versionApiCheck();
        Single<Response<JsonObject>> checkUserValidApi(CheckUserApiReqModel reqModel);
        Single<Response<JsonObject>> changeGradeApi(CheckUserValidResModel reqModel);
        Single<Response<JsonObject>> getDashboardData(AuroScholarDataModel model);
        Single<Response<JsonObject>> uploadProfileImage(List<KYCDocumentDatamodel> list, KYCInputModel kycInputModel);
        Single<Response<JsonObject>> postDemographicData(DemographicResModel demographicResModel);
        Single<Response<JsonObject>> getAssignmentId(AssignmentReqModel assignmentReqModel);
        Single<Response<JsonObject>> getAzureData(AssignmentReqModel azureReqModel);
        Single<Response<JsonObject>> upgradeClass(AuroScholarDataModel model);
        Single<Response<JsonObject>> findFriendApi(double lat, double longt, double radius);
        Single<Response<JsonObject>> sendFriendRequestApi(int requested_by_id, int requested_user_id, String requested_by_phone, String requested_user_phone);
        Single<Response<JsonObject>> friendRequestListApi(int requested_by_id);
        Single<Response<JsonObject>> friendAcceptApi(int friend_request_id, String request_status);
        Single<Response<JsonObject>> getDynamicDataApi(DynamiclinkResModel model);

        Single<Response<JsonObject>> sendRefferalDataApi(DynamiclinkResModel model);

        Single<Response<JsonObject>> getCertificateApi(CertificateResModel model);
    }


    interface DashboardDbData {
        Single<Integer> getStoreDataCount();

    }

}
