package com.demo.application.home.presentation.view.activity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.application.base_component.BaseActivity;
import com.demo.application.core.application.di.component.ViewModelFactory;
import com.demo.application.core.database.DemoAppPref;
import com.demo.application.core.database.PrefModel;
import com.demo.application.databinding.ActivitySplashScreenBinding;

import com.demo.application.home.data.model.response.CheckVerResModel;
import com.demo.application.home.presentation.viewmodel.SplashScreenViewModel;
import com.demo.application.util.AppLogger;
import com.demo.application.util.ViewUtil;
import com.demo.application.util.firebase.FirebaseDynamicLink;
import com.demo.application.util.firebase.FirebaseEventUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

public class SplashScreenActivity extends BaseActivity implements View.OnClickListener {
    @Inject
    @Named("SplashScreenActivity")
    ViewModelFactory viewModelFactory;
    ActivitySplashScreenBinding binding;

    private SplashScreenViewModel viewModel;
    private Context mContext;
    FirebaseEventUtil firebaseEventUtil;
    CheckVerResModel checkVerResModel;
    String TAG="SplashScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash_screen);
        init();
        setListener();
    }

    @Override
    protected void init() {

        binding = DataBindingUtil.setContentView(this, getLayout());
        ((DemoApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashScreenViewModel.class);
        binding.setLifecycleOwner(this);
        mContext = SplashScreenActivity.this;
        //setUpBottomSheet();
        AppLogger.e(TAG, "Version Api start calling in init ");
        viewModel.getVersionApi();


//        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
//            viewModel.serviceLiveData().removeObservers(this);
//        } else {
//            observeServiceResponse();
//        }
        whichScreenOpen();//noapicall

        FirebaseDynamicLink mfirebase = new FirebaseDynamicLink(this);
        //mfirebase.dynamiclinking();
        mfirebase.getFirebaseData();
        ViewUtil.setLanguageonUi(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void setListener() {
        binding.userSelectionSheet.teacherBtn.setOnClickListener(this);
        binding.userSelectionSheet.studentBtn.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_splash_screen;
    }

    public void setSleepModeForSplashScreen(Class<?> cls) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
                if (prefModel.getUserType() > 0) {
                    if (prefModel.getUserType() == 1) {
                        //funnel
                        firebaseEventUtil = new FirebaseEventUtil();
                        Map<String, String> bundle = new HashMap<String, String>();
                        bundle.put(getResources().getString(R.string.event_splash_page_choose), getResources().getString(R.string.event_teacher));
                        firebaseEventUtil.logEvent(mContext, getResources().getString(R.string.splash_screen_page), bundle);
                    } else {
                        //funnel
                        firebaseEventUtil = new FirebaseEventUtil();
                        Map<String, String> bundle = new HashMap<String, String>();
                        bundle.put(getResources().getString(R.string.event_splash_page_choose), getResources().getString(R.string.event_student));
                        firebaseEventUtil.logEvent(mContext, getResources().getString(R.string.splash_screen_page), bundle);
                    }

                    startActivity(new Intent(SplashScreenActivity.this, cls));
                    finish();

                } else {

                   // openSelectionLayout();

                }
            }
        }, 3000);
    }


    private void whichScreenOpen() {
       // setValuesInPref(2); // For student sdk
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
//        if (prefModel.isLogin() && prefModel.getUserMobile() != null && prefModel.getUserMobile().length() != 0) {
//            setSleepModeForSplashScreen(DashBoardActivity.class);
//        } else if (prefModel.isLogin() && prefModel.getUserMobile() == null) {
//            setSleepModeForSplashScreen(VerifyWithPhoneNoActivity.class);
//        } else {
//            setSleepModeForSplashScreen(LoginMainScreenActivity.class);
//        }
        setSleepModeForSplashScreen(IntroSliderActivity.class);
    }

    private void openSelectionLayout() {
        //Animation on button
        binding.userSelectionSheet.sheetLayout.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.bottom_to_up);
        binding.userSelectionSheet.sheetLayout.startAnimation(anim);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.teacher_btn) {

            startActivity(1);
        }
        if (view.getId() == R.id.student_btn) {

            startActivity(2);
        }
    }

    public void startActivity(int id) {

        if (id == 1) {
            //funnel
            firebaseEventUtil = new FirebaseEventUtil();
            Map<String, String> bundle = new HashMap<String, String>();
            bundle.put(getResources().getString(R.string.event_splash_page_choose), getResources().getString(R.string.event_teacher));
            firebaseEventUtil.logEvent(mContext, getResources().getString(R.string.splash_screen_page), bundle);
        } else {
            //funnel
            firebaseEventUtil = new FirebaseEventUtil();
            Map<String, String> bundle = new HashMap<String, String>();
            bundle.put(getResources().getString(R.string.event_splash_page_choose), getResources().getString(R.string.event_student));
            firebaseEventUtil.logEvent(mContext, getResources().getString(R.string.splash_screen_page), bundle);
        }

        Intent i = new Intent(SplashScreenActivity.this, LoginMainScreenActivity.class);
        setValuesInPref(id);
        startActivity(i);
        finish();
    }

    private void setValuesInPref(int type) {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        prefModel.setUserType(type);
        DemoAppPref.INSTANCE.setPref(prefModel);
    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:
                    //For ProgressBar
                    //  openProgressDialog();
                    //Toast.makeText(activity, "Loading...", Toast.LENGTH_SHORT).show();


                    break;

                case SUCCESS:
                    AppLogger.e(TAG, "observeServiceResponse method calling 1");
                    checkVerResModel = (CheckVerResModel) responseApi.data;
                    if (!checkVerResModel.getError()) {
                        AppLogger.e(TAG, "observeServiceResponse method calling 2");
                        setVersionCheckPref();
                    }
                    whichScreenOpen();
                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
                default:
                    AppLogger.e(TAG, "observeServiceResponse method calling 4");
                    whichScreenOpen();
                    break;

            }
        });
    }






    @Override
    protected void onDestroy() {
        AppLogger.e(TAG, "onDestroy method calling ");
        super.onDestroy();
    }

    private void setVersionCheckPref() {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        prefModel.setCheckVerResModel(checkVerResModel);
        DemoAppPref.INSTANCE.setPref(prefModel);
    }
}
