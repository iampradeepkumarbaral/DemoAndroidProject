package com.demo.application.util.firebase;

import android.app.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.demo.application.core.common.AppConstant;
import com.demo.application.core.database.DemoAppPref;
import com.demo.application.core.database.PrefModel;
import com.demo.application.home.data.model.response.DynamiclinkResModel;
import com.demo.application.util.AppLogger;
import com.demo.application.util.TextUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;

public class FirebaseDynamicLink {


    private FirebaseDynamicLinks mObjFirebaseDyanamic;
    private static String TAG = FirebaseDynamicLink.class.getSimpleName();

    // private static boolean enableFirebaseLogger = true;
    Bundle bundle;
    Activity mContext;

    public FirebaseDynamicLink(Activity mContext) {
        this.mContext = mContext;
        mObjFirebaseDyanamic = FirebaseDynamicLinks.getInstance();
        bundle = new Bundle();
    }

    public void dynamiclinking() {

        String json = createjSONObject().toString();
        String link = "https://auroscholarapp.page.link/?jsondata=" + json;
        /*Task<ShortDynamicLink> shortLinkTask =  */

        /*  Task<ShortDynamicLink> shortLinkTask */
        DynamicLink dynamicLink = mObjFirebaseDyanamic.createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://auroscholarapp.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder()
                        .setFallbackUrl(Uri.parse("https://auroscholar.com/"))
                        .build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.aurosampleapplication")
                        .setFallbackUrl(Uri.parse("https://auroscholar.com/"))
                        .build())

                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();
        Log.e("main", " Loog reffer no work  " + dynamicLinkUri);

    }

    public JSONObject createjSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("StudentPhoneNumber", "7978027446");
            obj.put("StudentName", "Pradeep");
            obj.put("StudentClass", "6 Class");
            obj.put("StudentComeFrom", "AuroScholar App");


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj;
    }

    public void getFirebaseData() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(mContext.getIntent())
                .addOnSuccessListener(mContext, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        AppLogger.i(TAG, "Dynamiclink" + pendingDynamicLinkData);
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            AppLogger.e(TAG, "Link " + deepLink);
                            String json = deepLink.getQueryParameter("jsondata");
                            try {
                                String val = URLDecoder.decode(json, "UTF-8");
                                if (!TextUtil.isEmpty(val)) {
                                    DynamiclinkResModel model = new Gson().fromJson(val, DynamiclinkResModel.class);
                                    AppLogger.i(TAG, "Dynamiclink" + model);
                                    PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
                                    prefModel.setDynamiclinkResModel(model);
                                    if (model != null && !TextUtil.isEmpty(model.getNavigationTo()) &&
                                            model.getNavigationTo().equalsIgnoreCase(AppConstant.NavigateToScreen.STUDENT_DASHBOARD) ||
                                            model.getNavigationTo().equalsIgnoreCase(AppConstant.NavigateToScreen.STUDENT_KYC) ||
                                            model.getNavigationTo().equalsIgnoreCase(AppConstant.NavigateToScreen.PAYMENT_TRANSFER) ||
                                            model.getNavigationTo().equalsIgnoreCase(AppConstant.NavigateToScreen.STUDENT_CERTIFICATE) ||
                                            model.getNavigationTo().equalsIgnoreCase(AppConstant.NavigateToScreen.FRIENDS_LEADERBOARD)) {
                                        if(!prefModel.isLogin() && prefModel.getUserType() ==0) {
                                            prefModel.setUserType(2);
                                        }
                                    }
                                    if (model != null && !TextUtil.isEmpty(model.getNavigationTo()) && model.getNavigationTo().equalsIgnoreCase(AppConstant.NavigateToScreen.TEACHER_DASHBOARD)) {
                                        if(!prefModel.isLogin() && prefModel.getUserType() ==0) {
                                            prefModel.setUserType(1);
                                        }
                                    }

                                    DemoAppPref.INSTANCE.setPref(prefModel);
                                    AppLogger.e(TAG, "Decode " + val);
                                }
                            } catch (Exception e) {
                                Log.i(TAG, "exception " + e.getMessage());
                            }

                        }

                    }
                });
    }

}
