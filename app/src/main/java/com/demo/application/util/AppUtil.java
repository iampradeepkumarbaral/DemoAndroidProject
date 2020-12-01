package com.demo.application.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.TypedValue;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;

import com.demo.application.core.application.DemoApp;
import com.demo.application.core.common.CommonDataModel;
import com.demo.application.core.common.Status;
import com.demo.application.core.database.DemoAppPref;
import com.demo.application.core.database.PrefModel;
import com.demo.application.home.data.model.DashboardResModel;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

public class AppUtil {
    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getVersionNumber(Context pContext) {
        String lStrVersion = "";
        try {
            PackageInfo pInfo = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), 0);
            lStrVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return lStrVersion;
    }


    public static String getLocale() {


        return Locale.getDefault().getLanguage().toUpperCase();
    }

    public static CommonDataModel getCommonClickModel(int source, Status clickType, Object object) {
        CommonDataModel commonDataModel = new CommonDataModel();
        commonDataModel.setSource(source);
        commonDataModel.setClickType(clickType);
        commonDataModel.setObject(object);
        return commonDataModel;
    }

    public static float dpToPx(@NonNull Context context, @Dimension(unit = Dimension.DP) int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }


    public static String errorMessageHandler(String defaultMsg, String responseData) {

        JSONObject errorJson = null;
        try {
            errorJson = new JSONObject(responseData);
            JSONObject description = (JSONObject) errorJson.getJSONArray("errorDetails").opt(0);
            // return description.optString(AppConstant.DESCRIPTION);
            return "";
        } catch (Exception e) {
            return defaultMsg;
        }
    }


    public static void openDialer(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    public static void openUrlInBrowser(String url, Context context) {
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static byte[] encodeToBase64(Bitmap image, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOS);
        byte[] byteArray = byteArrayOS.toByteArray();
        return byteArray;
    }

    public static long bytesIntoHumanReadable(long bytes) {
        long kilobyte = 1024;
        long megabyte = kilobyte * 1024;
        long gigabyte = megabyte * 1024;
        long terabyte = gigabyte * 1024;

        return (bytes / megabyte);
    }


    public static void setDashboardResModelToPref(DashboardResModel dashboardResModel) {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        prefModel.setDashboardResModel(dashboardResModel);
        DemoAppPref.INSTANCE.setPref(prefModel);
    }


    public static String getAppVersionName() {
        try {
            PackageInfo pInfo = DemoApp.getAppContext().getPackageManager().getPackageInfo(DemoApp.getAppContext().getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean checkForUpdate(String newVersion) {
        String existingVersion = getAppVersionName();
        if (existingVersion.isEmpty() || newVersion.isEmpty()) {
            return false;
        }

        existingVersion = existingVersion.replaceAll("\\.", "");
        newVersion = newVersion.replaceAll("\\.", "");

        int existingVersionLength = existingVersion.length();
        int newVersionLength = newVersion.length();

        StringBuilder versionBuilder = new StringBuilder();
        if (newVersionLength > existingVersionLength) {
            versionBuilder.append(existingVersion);
            for (int i = existingVersionLength; i < newVersionLength; i++) {
                versionBuilder.append("0");
            }
            existingVersion = versionBuilder.toString();
        } else if (existingVersionLength > newVersionLength) {
            versionBuilder.append(newVersion);
            for (int i = newVersionLength; i < existingVersionLength; i++) {
                versionBuilder.append("0");
            }
            newVersion = versionBuilder.toString();
        }

        return Integer.parseInt(newVersion) > Integer.parseInt(existingVersion);
    }

}
