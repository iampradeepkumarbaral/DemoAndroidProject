package com.demo.application.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.application.core.application.DemoApp;
import com.demo.application.core.common.AppConstant;
import com.demo.application.core.common.CommonCallBackListner;
import com.demo.application.core.database.DemoAppPref;
import com.demo.application.core.database.PrefModel;
import com.demo.application.home.presentation.view.activity.SplashScreenActivity;
import com.demo.application.util.alert_dialog.ErrorSnackbar;
import com.demo.application.util.alert_dialog.InstallSnackbar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.demo.application.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

public class ViewUtil {

    private static View snackView;

    public static String parseJSONData(Context context, String fileName) {
        String JsonString = null;
        try {

            InputStream inputStream = context.getAssets().open(fileName);

            int sizeOfJSONFile = inputStream.available();

            byte[] bytes = new byte[sizeOfJSONFile];

            inputStream.read(bytes);

            inputStream.close();

            JsonString = new String(bytes, "UTF-8");

        } catch (IOException ex) {
            AppLogger.e("ViewUtil", ex.getMessage());
            return null;
        }
        return JsonString;
    }


    public static void showToast(Context activity, String msg) {

        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    public static void deActivateDot(ImageView imageView) {

        imageView.setBackground(DemoApp.getAppContext().getResources().getDrawable(R.drawable.slider_deactive_dot));

    }

    public static void activateDot(ImageView imageView) {

        imageView.setBackground(DemoApp.getAppContext().getResources().getDrawable(R.drawable.slider_active_dot));

    }


    public static void fullScreen(Activity context) {

        context.requestWindowFeature(Window.FEATURE_NO_TITLE);
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void keepOnScreen(Activity context) {

        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    public static String format2LenStr(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }

    public static void showSnackBar(View rootLayout, String snackTitle) {

        if (rootLayout == null) {
            return;
        }
        if (rootLayout.getParent() == null) {
            return;
        }
        snackView = ErrorSnackbar.showSnackbar(rootLayout, snackTitle);
    }

    public static void showSnackBar(View rootLayout, String snackTitle, int color) {

        if (rootLayout == null) {
            return;
        }
        if (rootLayout.getParent() == null) {
            return;
        }
        snackView = ErrorSnackbar.showSnackbar(rootLayout, snackTitle, color);
    }

    public static void showSnackBarAction(View rootLayout, String snackTitle, int color, InstallSnackbar.OnClickButton lisner) {

        if (rootLayout == null) {
            return;
        }
        if (rootLayout.getParent() == null) {
            return;
        }
        snackView = InstallSnackbar.showSnackbarClickAction(rootLayout, snackTitle, color, lisner);
    }

    public static void dismissSnackBar() {
        if (snackView != null) {
            snackView.setVisibility(View.GONE);
        }
    }

    public static String getPackageVersion() {
        String packageVersion = null;
        try {
            PackageInfo pInfo = DemoApp.getAppContext().getPackageManager().getPackageInfo(DemoApp.getAppContext().getPackageName(), 0);
            packageVersion = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return packageVersion;

    }


    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public static int dpToPx(float dp, Context context) {
        return dpToPx(dp, context.getResources());
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }


    public static void showToast(String message) {
        Toast.makeText(DemoApp.getAppContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    public static void customTextView(TextView view, CommonCallBackListner commonCallBackListner) {
        view.setHighlightColor(DemoApp.getAppContext().getResources().getColor(android.R.color.transparent));

        String terms_of_service =" "+ DemoApp.getAppContext().getResources().getString(R.string.terms_of_service);
        String privacy_policy = DemoApp.getAppContext().getResources().getString(R.string.privacy_policy);

        SpannableStringBuilder spanTxt = new SpannableStringBuilder();
        spanTxt.append(DemoApp.getAppContext().getText(R.string.by_continuing));
        spanTxt.append(terms_of_service);
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //Do code here
                if (commonCallBackListner != null) {
                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(AppConstant.TERMS_CONDITION_TEXT, null, ""));
                }

            }
        }, spanTxt.length() - (terms_of_service).length(), spanTxt.length(), 0);
        spanTxt.setSpan(new ForegroundColorSpan(DemoApp.getAppContext().getColor(R.color.blue_color)), (spanTxt.length() - (terms_of_service).length()), spanTxt.length(), 0);
        spanTxt.append(" " + DemoApp.getAppContext().getString(R.string.and) + " ");
        spanTxt.append(privacy_policy);
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //Do code here
                if (commonCallBackListner != null) {
                    commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(AppConstant.PRIVACY_POLICY_TEXT, null, ""));
                }
            }
        }, spanTxt.length() - (privacy_policy).length(), spanTxt.length(), 0);
        spanTxt.setSpan(new ForegroundColorSpan(DemoApp.getAppContext().getColor(R.color.blue_color)), (spanTxt.length() - (privacy_policy).length()), spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

    public static void hideKeyboard(Context context) {
        if (context == null) {
            return;
        }
        View view = ((AppCompatActivity) context).getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public static void showKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        InputMethodManager methodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (methodManager != null && view != null) {
            methodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    public static void setUserPrefData(boolean loginStatus) {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        prefModel.setLogin(loginStatus);
        DemoAppPref.INSTANCE.setPref(prefModel);
    }

    public static void setWelcomePrefData(boolean tourStatus) {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        prefModel.setTour(tourStatus);
        DemoAppPref.INSTANCE.setPref(prefModel);
    }

    public static void showAlertMessageInCasePermissionNotGranted(String title, String pName, final Context context) {
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.app_name);
            builder.setMessage("To take " + title + ", allow passengerhmi access to " + pName + ". Tap Settings>Permissions, and turn " + pName + " on.").setCancelable(false);
            builder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    ((AppCompatActivity) context).startActivityForResult(intent, 777);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
            Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } catch (Exception e) {
            AppLogger.d("ViewUtil", e.toString());
        }
    }

    public static String makeTextStringDiffSize(String start, String end) {

        int textSize1 = DemoApp.getAppContext().getResources().getDimensionPixelSize(R.dimen._16sdp);
        int textSize2 = DemoApp.getAppContext().getResources().getDimensionPixelSize(R.dimen._12sdp);

        SpannableString span1 = new SpannableString(start);
        span1.setSpan(new AbsoluteSizeSpan(textSize1), 0, start.length(), SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString span2 = new SpannableString(end);
        span2.setSpan(new AbsoluteSizeSpan(textSize2), 0, end.length(), SPAN_INCLUSIVE_INCLUSIVE);

// let's put both spans together with a separator and all
        CharSequence finalText = TextUtils.concat(span1, " ", span2);
        return finalText.toString();
    }

    public static void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // add LIGHT_STATUS_BAR to flag
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(activity.getColor(R.color.white)); // optional
        }
    }

    public static Resources getCustomResource(Activity activity) {
        // Locale locale = new Locale(getLanguage());
        Resources standardResources = activity.getResources();
        AssetManager assets = standardResources.getAssets();
        DisplayMetrics metrics = standardResources.getDisplayMetrics();
        Configuration config = new Configuration(standardResources.getConfiguration());
        config.locale = Locale.getDefault();
        Resources res = new Resources(assets, metrics, config);
        return res;
    }

    public static void loadGalleryImage(ImageView imageView, int resource) {

        Glide.with(imageView.getContext())
                .load(resource)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20))
                        .dontAnimate()
                        .priority(Priority.IMMEDIATE)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(imageView);

    }


    public static String getLanguage() {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        if (prefModel != null && prefModel.getUserLanguage() != null && !prefModel.getUserLanguage().isEmpty()) {
            return prefModel.getUserLanguage();
        }
        return AppConstant.LANGUAGE_EN;
    }

    public static void setLanguage(String language) {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        if (prefModel != null) {
            prefModel.setUserLanguage(language);
            DemoAppPref.INSTANCE.setPref(prefModel);
        }
    }


    public static int getDisplayMatrics(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int density = (int) activity.getResources().getDisplayMetrics().density;
        return displayMetrics.widthPixels / density;
    }

    public static void setLocaleInstant(Activity activity) {
     /*   Locale locale;
        locale = new Locale(getLanguage());
        Configuration config = new Configuration(DemoApp.getAppContext().getResources().getConfiguration());
        Locale.setDefault(locale);
        config.setLocale(locale);

        DemoApp.getAppContext().getBaseContext().getResources().updateConfiguration(config,
                DemoApp.getAppContext().getBaseContext().getResources().getDisplayMetrics());
        activity.finish();
        activity.startActivity(activity.getIntent());*/




        Locale locale = new Locale(getLanguage());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        DemoApp.getAppContext().getBaseContext().getResources().updateConfiguration(config,
                DemoApp.getAppContext().getBaseContext().getResources().getDisplayMetrics());
        //settings.edit().putString("locale", "ar").commit();
        config.setLocale(locale);
        activity.finish();
        Intent refresh = new Intent(activity, SplashScreenActivity.class);
        activity.startActivity(refresh);

    }


    public static void setLanguageonUi(Activity activity){
        Resources resources = activity.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.locale = new Locale(getLanguage());
        resources.updateConfiguration(config, dm);
    }

   /* public static void setLocale(Activity activity) {
        Locale myLocale = new Locale(getLanguage());
        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }*/
}