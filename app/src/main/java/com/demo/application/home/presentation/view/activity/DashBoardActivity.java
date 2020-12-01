package com.demo.application.home.presentation.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentSender;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;


import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.application.base_component.BaseActivity;
import com.demo.application.core.application.di.component.ViewModelFactory;
import com.demo.application.core.common.AppConstant;
import com.demo.application.core.common.CommonCallBackListner;
import com.demo.application.core.common.FragmentUtil;

import com.demo.application.core.common.ResponseApi;
import com.demo.application.core.common.SdkCallBack;
import com.demo.application.core.common.Status;
import com.demo.application.core.database.DemoAppPref;
import com.demo.application.core.database.PrefModel;
import com.demo.application.databinding.ActivityAuroScholarDashBoardBinding;
import com.demo.application.home.data.model.AuroScholarDataModel;
import com.demo.application.home.data.model.AuroScholarInputModel;
import com.demo.application.home.data.model.DashboardResModel;
import com.demo.application.home.data.model.FbGoogleUserModel;

import com.demo.application.home.data.model.response.CheckVerResModel;
import com.demo.application.home.data.model.response.DynamiclinkResModel;
import com.demo.application.home.presentation.view.fragment.CertificateFragment;
import com.demo.application.home.presentation.view.fragment.FriendsLeaderBoardFragment;
import com.demo.application.home.presentation.view.fragment.GradeChangeFragment;
import com.demo.application.home.presentation.view.fragment.KYCFragment;
import com.demo.application.home.presentation.view.fragment.KYCViewFragment;
import com.demo.application.home.presentation.viewmodel.AuroScholarDashBoardViewModel;

import com.demo.application.util.AppLogger;
import com.demo.application.util.AppUtil;
import com.demo.application.util.ConversionUtil;
import com.demo.application.util.TextUtil;
import com.demo.application.util.ViewUtil;
import com.demo.application.util.authenticate.GoogleSignInHelper;
import com.demo.application.util.firebase.FirebaseEventUtil;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.truecaller.android.sdk.TrueProfile;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import static com.demo.application.core.common.Status.DASHBOARD_API;
import static com.demo.application.core.common.Status.LISTNER_FAIL;
import static com.demo.application.core.common.Status.LISTNER_SUCCESS;

public class DashBoardActivity extends BaseActivity implements GradeChangeFragment.OnClickButton, BottomNavigationView.OnNavigationItemSelectedListener {

    @Inject
    @Named("DashBoardActivity")
    ViewModelFactory viewModelFactory;
    ActivityAuroScholarDashBoardBinding binding;
    TrueProfile trueProfile;
    String userType;
    private AuroScholarDashBoardViewModel viewModel;
    private Context mContext;
    PrefModel prefModel;
    int backPress = 0;
    GoogleSignInHelper mgoogleSignInHelper;
    FirebaseEventUtil firebaseEventUtil;
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    String TAG = "AppUpdate";
    CheckVerResModel checkVerResModel;

    private static int LISTING_ACTIVE_FRAGMENT = 0;
    public static final int QUIZ_DASHBOARD_FRAGMENT = 2;
    public static final int QUIZ_KYC_FRAGMENT = 2;
    public static final int QUIZ_KYC_VIEW_FRAGMENT = 3;
    public static final int WALLET_INFO_FRAGMENT = 4;
    public static final int QUIZ_TEST_FRAGMENT = 5;
    public static final int PRIVACY_POLICY_FRAGMENT = 6;
    public static final int CERTIFICATE_FRAGMENT = 7;
    public static final int KYC_DIRECT_FRAGMENT = 8;
    public static final int CERTIFICATE_DIRECT_FRAGMENT = 9;
    public static final int PAYMENT_DIRECT_FRAGMENT = 10;
    public static final int TRANSACTION_FRAGMENT = 11;

    DashboardResModel dashboardResModel;
    AuroScholarInputModel inputModel;
    CommonCallBackListner commonCallBackListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_auro_scholar_dash_board);
        ViewUtil.setLanguageonUi(this);

        init();
        setListener();
        appUpdateManager = AppUpdateManagerFactory.create(DemoApp.getAppContext());
       // checkVersion();

    }

    @Override
    protected void init() {

        binding = DataBindingUtil.setContentView(this, getLayout());
        ((DemoApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AuroScholarDashBoardViewModel.class);
        binding.setLifecycleOwner(this);
        mContext = DashBoardActivity.this;
        firebaseEventUtil = new FirebaseEventUtil();
        if (getIntent().hasExtra(FbGoogleUserModel.class.getSimpleName())) {
          /*  trueProfile = getIntent().getParcelableExtra("profile");
            userType = getIntent().getStringExtra("userType");*/
        }
        prefModel = DemoAppPref.INSTANCE.getModelInstance();
        setListener();
       // checkRefferedData();
//
//        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
//            viewModel.serviceLiveData().removeObservers(this);
//        } else {
//            observeServiceResponse();
//        }
    }

    public void openGradeChangeFragment(String source) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.COMING_FROM, source);
        GradeChangeFragment gradeChangeFragment = new GradeChangeFragment(this);
        gradeChangeFragment.setArguments(bundle);
        openFragment(gradeChangeFragment);
    }

    @Override
    protected void setListener() {
        binding.naviagtionContent.bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_auro_scholar_dash_board;
    }



    public void callDashboardApi() {
        viewModel.getDashboardData(inputModel);
    }

    private void openFragment(Fragment fragment) {

        FragmentUtil.addFragment(this, fragment, R.id.auro_container, 0);

    }

    private void openLeaderBoardFragment(Fragment fragment) {
        ((AppCompatActivity) (this)).getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.auro_container, fragment, DashBoardActivity.class
                        .getSimpleName())
                .addToBackStack(null)
                .commitAllowingStateLoss();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_VERSION_UPDATE:
                if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                    AppLogger.e(TAG, "REQ_CODE_VERSION_UPDATE method calling 1 ");                    // If the update is cancelled or fails,
                    // you can request to start the update again.
                    unregisterInstallStateUpdListener();
                }

                break;
        }

        //must param to get the acitivity
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }



    @Override
    public void onBackPressed() {

    }

    private void dismissApplication() {
        if (backPress == 0) {
            backPress++;
            ViewUtil.showSnackBar(binding.getRoot(), getString(R.string.press_again_for_exit));
        } else {
            finishAffinity();
        }
    }

    @Override
    public void onClickListener() {

    }

    private void checkVersion() {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        checkVerResModel = prefModel.getCheckVerResModel();
        if (checkVerResModel != null) {
            AppLogger.e(TAG, "checkVersionApiResponse method calling 1");
            boolean isStatus = AppUtil.checkForUpdate(prefModel.getCheckVerResModel().getNewVersion());
            AppLogger.e(TAG, "checkVersionApiResponse method calling 2--" + isStatus);
            if (isStatus) {
                checkForAppUpdate();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNewAppVersionState();
    }

    private void checkForAppUpdate() {
        AppLogger.e(TAG, "checkForAppUpdate method calling 1 ");                    // If the update is cancelled or fails,
        // Creates instance of the manager.

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Create a listener to track request state updates.
        installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState installState) {
                AppLogger.e(TAG, "checkForAppUpdate method calling 2");
                // If the update is cancelled or fails,
                // Show module progress, log state, or install the update.
                if (installState.installStatus() == InstallStatus.DOWNLOADED)
                    AppLogger.e(TAG, "checkForAppUpdate method calling 3");
                // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                popupSnackbarForCompleteUpdateAndUnregister();
            }
        };
        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            AppLogger.e(TAG, "checkForAppUpdate method calling 4");
            int priority = ConversionUtil.INSTANCE.convertStringToInteger(checkVerResModel.getPriorty());
            AppLogger.e(TAG, "checkForAppUpdate method calling 5 & priority-" + priority);
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                AppLogger.e(TAG, "checkForAppUpdate method calling 6");
                if (priority == 0 || priority == 1) {
                    AppLogger.e(TAG, "checkForAppUpdate method calling 7");
                    // Request the update.
                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                        AppLogger.e(TAG, "checkForAppUpdate method calling 8");
                        // Before starting an update, register a listener for updates.
                        appUpdateManager.registerListener(installStateUpdatedListener);
                        // Start an update.
                        startAppUpdateFlexible(appUpdateInfo);
                    }
                } else {
                    AppLogger.e(TAG, "checkForAppUpdate method calling 9");
                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        // Start an update.
                        AppLogger.e(TAG, "checkForAppUpdate method calling 10");
                        startAppUpdateImmediate(appUpdateInfo);
                    }
                }
            }
        });
    }

    private void popupSnackbarForCompleteUpdateAndUnregister() {
        AppLogger.e(TAG, "popupSnackbarForCompleteUpdateAndUnregister method calling 1 ");                    // If the update is cancelled or fails,
        Snackbar snackbar =
                Snackbar.make(binding.getRoot(), "App is downloaded.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();

        unregisterInstallStateUpdListener();
    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * However, you should execute this check at all app entry points.
     */
    private void checkNewAppVersionState() {
        AppLogger.e(TAG, "checkNewAppVersionState method calling 1 ");
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            //FLEXIBLE:
                            // If the update is downloaded but not installed,
                            // notify the user to complete the update.
                            AppLogger.e(TAG, "checkNewAppVersionState method calling 2 ");
                            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                                popupSnackbarForCompleteUpdateAndUnregister();
                                AppLogger.e(TAG, "checkNewAppVersionState method calling 3 ");
                            }
                            AppLogger.e(TAG, "checkNewAppVersionState method calling 4");
                            //IMMEDIATE:
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                AppLogger.e(TAG, "checkNewAppVersionState method calling 5 ");
                                // If an in-app update is already running, resume the update.
                                startAppUpdateImmediate(appUpdateInfo);
                            }
                        });

    }

    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        AppLogger.e(TAG, "startAppUpdateImmediate method calling 1 ");
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            AppLogger.e(TAG, "startAppUpdateImmediate method calling  Exception 2 ");
        }
    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        AppLogger.e(TAG, "startAppUpdateFlexible method calling 1 ");
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            AppLogger.e(TAG, "startAppUpdateFlexible method calling  Exception 2 ");
            unregisterInstallStateUpdListener();
        }
    }

    /**
     * Needed only for FLEXIBLE update
     */
    private void unregisterInstallStateUpdListener() {
        AppLogger.e(TAG, "unregisterInstallStateUpdListener method calling   1 ");
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        AppLogger.e(TAG, "unregisterInstallStateUpdListener method calling   2");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppLogger.e(TAG, "onDestroy method calling ");
        unregisterInstallStateUpdListener();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.action_home:
                selectNavigationMenu(1);

                break;
            case R.id.action_profile:
                selectNavigationMenu(2);


                break;
            case R.id.action_leaderboard:
                selectNavigationMenu(0);
                openLeaderBoardFragment(new FriendsLeaderBoardFragment());
                break;

        }

        return false;
    }

    public void setHomeFragmentTab() {

        selectNavigationMenu(1);
    }


    public void selectNavigationMenu(int pos) {
        binding.naviagtionContent.bottomNavigation.getMenu().getItem(pos).setChecked(true);

    }



    private void popBackStack() {
        backPress = 0;
        getSupportFragmentManager().popBackStack();
    }

    public static void setListingActiveFragment(int listingActiveFragment) {
        LISTING_ACTIVE_FRAGMENT = listingActiveFragment;
    }


    public void alertDialogForQuitQuiz() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.quiz_exit_txt);

        // Set the alert dialog yes button click listener
        builder.setPositiveButton(Html.fromHtml("<font color='#00A1DB'>" + this.getString(R.string.yes) + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                popBackStack();
                dialog.dismiss();
            }
        });
        // Set the alert dialog no button click listener
        builder.setNegativeButton(Html.fromHtml("<font color='#00A1DB'>" + this.getString(R.string.no) + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when No button clicked
                dialog.dismiss();
                     /*   Toast.makeText(getApplicationContext(),
                                "No Button Clicked",Toast.LENGTH_SHORT).show();*/
            }
        });
        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }

    private void checkRefferedData() {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        DynamiclinkResModel dynamiclinkResModel = prefModel.getDynamiclinkResModel();
        if (dynamiclinkResModel != null && !TextUtil.isEmpty(dynamiclinkResModel.getRefferMobileno())) {
            dynamiclinkResModel.setRefferalMobileno(prefModel.getUserMobile());
          /*  if (dynamiclinkResModel.getSource().equalsIgnoreCase(AppConstant.Source.TEACHER_APP_AURO)) {
                dynamiclinkResModel.setSource("teacher");
            } else {
                dynamiclinkResModel.setSource("student");
            }*/
            dynamiclinkResModel.setSource("");
            AppLogger.i(TAG,"dynamiclink"+dynamiclinkResModel);
            viewModel.sendRefferalData(dynamiclinkResModel);
            AppLogger.e(TAG, dynamiclinkResModel.getRefferMobileno());
        } else {
            AppLogger.e(TAG, "No Link Available");
        }
    }

    private void observeServiceResponse() {
        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {
                case SUCCESS:
                    if (responseApi.apiTypeStatus == DASHBOARD_API) {
                        onApiSuccess(responseApi);
                        DashboardResModel dashboardResModel = (DashboardResModel) responseApi.data;
                        if (commonCallBackListner != null) {
                            commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, LISTNER_SUCCESS, dashboardResModel));
                        }
                    }
                    break;

                case NO_INTERNET:
                    if (commonCallBackListner != null) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, LISTNER_FAIL, (String) responseApi.data));
                    }
                    break;

                case AUTH_FAIL:
                case FAIL_400:
                    if (commonCallBackListner != null) {
                        commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, LISTNER_FAIL, (String) responseApi.data));
                    }
                    break;
            }

        });
    }

    private void onApiSuccess(ResponseApi responseApi) {
        dashboardResModel = (DashboardResModel) responseApi.data;
        AppUtil.setDashboardResModelToPref(dashboardResModel);


        /*Update Dynamic  to empty*/
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        DynamiclinkResModel model = prefModel.getDynamiclinkResModel();
        prefModel.setDynamiclinkResModel(new DynamiclinkResModel());
        DemoAppPref.INSTANCE.setPref(prefModel);
    }

    public void dashboardModel(DashboardResModel model) {
        dashboardResModel = model;
        AppUtil.setDashboardResModelToPref(model);
    }


    public void openKYCFragment(DashboardResModel dashboardResModel) {
        Bundle bundle = new Bundle();
        KYCFragment kycFragment = new KYCFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        bundle.putString(AppConstant.COMING_FROM, AppConstant.SENDING_DATA.DYNAMIC_LINK);
        kycFragment.setArguments(bundle);
        openFragment(kycFragment);
    }

    public void openKYCViewFragment(DashboardResModel dashboardResModel) {
        Bundle bundle = new Bundle();
        KYCViewFragment kycViewFragment = new KYCViewFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        bundle.putString(AppConstant.COMING_FROM, AppConstant.SENDING_DATA.DYNAMIC_LINK);

        kycViewFragment.setArguments(bundle);
        openFragment(kycViewFragment);
    }

    public void openCertificate() {
        Bundle bundle = new Bundle();
        CertificateFragment certificateFragment = new CertificateFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        bundle.putString(AppConstant.COMING_FROM, AppConstant.SENDING_DATA.DYNAMIC_LINK);
        certificateFragment.setArguments(bundle);

        openFragment(certificateFragment);
    }


    public void setListner(CommonCallBackListner listner) {
        this.commonCallBackListner = listner;
    }



}
