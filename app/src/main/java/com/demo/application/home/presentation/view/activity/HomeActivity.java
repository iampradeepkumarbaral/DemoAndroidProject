package com.demo.application.home.presentation.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.demo.application.core.application.base_component.BaseActivity;
import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.application.di.component.ViewModelFactory;
import com.demo.application.core.common.AppConstant;
import com.demo.application.core.common.CommonCallBackListner;
import com.demo.application.core.common.FragmentUtil;
import com.demo.application.core.common.OnItemClickListener;
import com.demo.application.core.common.Status;
import com.demo.application.core.database.DemoAppPref;
import com.demo.application.core.database.PrefModel;
import com.demo.application.databinding.ActivityDashboardBinding;
import com.demo.application.home.data.datasource.remote.HomeRemoteApi;
import com.demo.application.home.data.model.AuroScholarDataModel;
import com.demo.application.home.data.model.response.DynamiclinkResModel;
import com.demo.application.home.presentation.viewmodel.HomeViewModel;

import com.demo.application.util.AppLogger;
import com.demo.application.util.ViewUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.inject.Named;

public class HomeActivity extends BaseActivity implements OnItemClickListener, View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {


    private String TAG = HomeActivity.class.getSimpleName().toString();
    ;
    @Inject
    HomeRemoteApi remoteApi;

    @Inject
    @Named("HomeActivity")
    ViewModelFactory viewModelFactory;

    private ActivityDashboardBinding binding;
    private Context mContext;
    private HomeViewModel viewModel;
    private static int LISTING_ACTIVE_FRAGMENT = 0;
    int backPress = 0;
    public static final int QUIZ_DASHBOARD_FRAGMENT = 1;
    public static final int QUIZ_DASHBOARD_WEB_FRAGMENT = 2;
    public static final int KYC_FRAGMENT = 3;
    public static final int DEMOGRAPHIC_FRAGMENT = 04;
    public static final int TEACHER_KYC_FRAGMENT = 05;
    public static final int TEACHER_DASHBOARD_FRAGMENT = 06;
    public static final int TEACHER_PROFILE_FRAGMENT = 07;
    public static final int TEACHER_INFO_FRAGMENT = 8;

    String memberType;
    CommonCallBackListner commonCallBackListner;
    public static int screenHeight = 0;
    public static int screenWidth = 0;

    AuroScholarDataModel auroScholarDataModel;


    public static int getListingActiveFragment() {
        return LISTING_ACTIVE_FRAGMENT;
    }

    public static void setListingActiveFragment(int listingActiveFragment) {
        LISTING_ACTIVE_FRAGMENT = listingActiveFragment;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        ViewUtil.setLanguageonUi(this);
        init();
        setListener();
        DynamiclinkResModel dynamiclinkResModel = new DynamiclinkResModel();

        dynamiclinkResModel.setSource("");
        dynamiclinkResModel.setNavigationTo(AppConstant.NavigateToScreen.STUDENT_DASHBOARD);
        dynamiclinkResModel.setReffer_type(AppConstant.NavigateToScreen.TEACHER);
        viewModel.getDynamicData(dynamiclinkResModel);
    }

    @Override
    protected void init() {
        memberType = "Member";
        binding = DataBindingUtil.setContentView(this, getLayout());
        ((DemoApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        mContext = HomeActivity.this;
        setLightStatusBar(this);
        if (getIntent() != null && getIntent().getParcelableExtra(AppConstant.AURO_DATA_MODEL) != null) {
            auroScholarDataModel = (AuroScholarDataModel) getIntent().getParcelableExtra(AppConstant.AURO_DATA_MODEL);
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        // printHashKey(this);
        setHomeFragmentTab();


    }

    @Override
    protected void setListener() {
        /*set listner here*/
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    private void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // add LIGHT_STATUS_BAR to flag
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue_color)); // optional
        }
    }

    @Override
    public void onItemClick(int position) {


    }


    private void setText(String text) {
        popBackStack();
        binding.naviagtionContent.errorMesssage.setVisibility(View.VISIBLE);
        binding.naviagtionContent.errorMesssage.setText(text);

    }


    public void openFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(mContext, fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onBackPressed() {
        backStack();

    }


    private synchronized void backStack() {

        switch (LISTING_ACTIVE_FRAGMENT) {
            case TEACHER_DASHBOARD_FRAGMENT:
            case TEACHER_KYC_FRAGMENT:
            case TEACHER_INFO_FRAGMENT:
            case TEACHER_PROFILE_FRAGMENT:
                dismissApplication();
                break;
            case DEMOGRAPHIC_FRAGMENT:
            case KYC_FRAGMENT:
                popBackStack();
                break;
            default:
                popBackStack();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        AppLogger.e("chhonker", "Activity requestCode=" + requestCode);
    }

    private void popBackStack() {
        backPress = 0;
        getSupportFragmentManager().popBackStack();
    }


    private void dismissApplication() {
        if (backPress == 0) {
            backPress++;
            ViewUtil.showSnackBar(binding.naviagtionContent.homeContainer, "Press again to close the app");
        } else {
            finish();
            //  finishAffinity();
        }
    }


    public void setHomeFragmentTab() {
        binding.naviagtionContent.bottomNavigation.setOnNavigationItemSelectedListener(this);

        selectNavigationMenu(1);
    }


    @Override
    public void onClick(View view) {

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {



        return false;
    }

    public void selectNavigationMenu(int pos) {
        binding.naviagtionContent.bottomNavigation.getMenu().getItem(pos).setChecked(true);

    }

    public void printHashKey(Activity context) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {
            switch (responseApi.status) {

                case LOADING:

                    break;

                case SUCCESS:

                    break;

                case FAIL:
                case NO_INTERNET:
                    AppLogger.e("Error", (String) responseApi.data);
                    break;


                default:
                    AppLogger.e("Error", (String) responseApi.data);
                    break;
            }

        });
    }


}
