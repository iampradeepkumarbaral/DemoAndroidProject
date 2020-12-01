package com.demo.application.home.presentation.view.activity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.application.base_component.BaseActivity;
import com.demo.application.core.application.di.component.ViewModelFactory;
import com.demo.application.core.common.AppConstant;
import com.demo.application.core.common.CommonCallBackListner;
import com.demo.application.core.common.CommonDataModel;
import com.demo.application.core.common.Status;
import com.demo.application.core.database.DemoAppPref;
import com.demo.application.core.database.PrefModel;
import com.demo.application.databinding.ActivityLoginMainScreenBinding;
import com.demo.application.home.data.model.CheckUserApiReqModel;
import com.demo.application.home.data.model.FbGoogleUserModel;

import com.demo.application.home.data.model.SendOtpReqModel;
import com.demo.application.home.data.model.response.CheckUserValidResModel;
import com.demo.application.home.data.model.response.SendOtpResModel;
import com.demo.application.home.presentation.viewmodel.LoginScreenViewModel;
import com.demo.application.util.AppLogger;


import com.demo.application.util.ConversionUtil;
import com.demo.application.util.ViewUtil;
import com.demo.application.util.authenticate.FbConnectHelper;
import com.demo.application.util.authenticate.GoogleSignInHelper;


import com.demo.application.util.firebase.FirebaseEventUtil;
import com.demo.application.core.network.URLConstant;
import com.demo.application.util.TextUtil;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;


public class LoginMainScreenActivity extends BaseActivity implements View.OnClickListener, FbConnectHelper.OnFbSignInListener, GoogleSignInHelper.OnGoogleSignInListener, CommonCallBackListner {


    private static String TAG = LoginMainScreenActivity.class.getSimpleName();
    @Inject
    @Named("LoginMainScreenActivity")
    ViewModelFactory viewModelFactory;

    ActivityLoginMainScreenBinding binding;
    private LoginScreenViewModel viewModel;
    private Context mContext;
    GoogleSignInHelper gSignInHelper;
    private FbConnectHelper fbConnectHelper;
    FbGoogleUserModel gmailFbResModel;

    FirebaseEventUtil firebaseEventUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setLanguageonUi(this);
        init();


    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(this, getLayout());
        ((DemoApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginScreenViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        mContext = LoginMainScreenActivity.this;
        firebaseEventUtil = new FirebaseEventUtil();

//        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
//            viewModel.serviceLiveData().removeObservers(this);
//
//        } else {
//            observeServiceResponse();
//        }
        ViewUtil.customTextView(binding.RPTextView7, this);
        setListener();
        googleFbSignin();
    }

    @Override
    protected void setListener() {
        binding.googleLayout.setOnClickListener(this);
        binding.facebookLayout.setOnClickListener(this);
        binding.RPButtonSendOtp.setOnClickListener(this);
        binding.textSigninWithGoogle.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login_main_screen;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.google_layout || view.getId() == R.id.text_signin_with_google) {
            gSignInHelper.signIn(this);
            binding.progressbar.pgbar.setVisibility(View.VISIBLE);

        }
        if (view.getId() == R.id.facebook_layout) {
            fbConnectHelper.connect();
            binding.progressbar.pgbar.setVisibility(View.VISIBLE);
        }

        if (view.getId() == R.id.RPButtonSendOtp) {
            if (binding.textPhonenumber.getText().length() == 10) {
               // sendOtpApiReqPass();
                startActivityDashboardScreen(1111);
            } else if (TextUtil.isEmpty(binding.textPhonenumber.getText().toString())) {
                ViewUtil.showSnackBar(binding.getRoot(), this.getResources().getString(R.string.enter_phone_number));
                // showSnackbarError("", Color.RED);
            } else {

                ViewUtil.showSnackBar(binding.getRoot(), this.getResources().getString(R.string.invalid_phone_number));

            }
        }
    }


    private void googleFbSignin() {

        gSignInHelper = GoogleSignInHelper.getFireBaseInstance();
        gSignInHelper.initialize(this, this);
        fbConnectHelper = new FbConnectHelper(this, this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gSignInHelper.onActivityResult(requestCode, resultCode, data);
        fbConnectHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void OnFbSuccess(GraphResponse graphResponse) {
        // Toast.makeText(this, graphResponse.getRawResponse(), Toast.LENGTH_SHORT).show();
        //binding.progressbar.pgbar.setVisibility(View.GONE);

        gmailFbResModel = getUserModelFromGraphResponse(graphResponse);
        if (gmailFbResModel != null) {
            //funnel
            Map<String, String> bundle = new HashMap<String, String>();
            bundle.put(getResources().getString(R.string.login_page_userfbemail), gmailFbResModel.userEmail);

            bundle.put(getResources().getString(R.string.login_page_userfbname), gmailFbResModel.userName);
            firebaseEventUtil.logEvent(mContext, getResources().getString(R.string.login_page_verify_by_fb), bundle);

            if (!TextUtil.isEmpty(gmailFbResModel.userEmail)) {
                callCheckUser();
            } else {
                showSnackbarError("Facebook SignIn Error");
            }
        }

    }

    @Override
    public void OnFbError(String errorMessage) {
        resetToDefaultView(errorMessage);
        binding.progressbar.pgbar.setVisibility(View.GONE);
        showSnackbarError("Facebook SignIn Error");
    }

    private void resetToDefaultView(String message) {
        // Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        binding.progressbar.pgbar.setVisibility(View.GONE);
    }

    @Override
    public void OnGSignSuccess(GoogleSignInAccount googleSignInAccount) {

        gmailFbResModel = new FbGoogleUserModel();
        gmailFbResModel.userEmail = googleSignInAccount.getEmail();
        gmailFbResModel.userName = googleSignInAccount.getDisplayName();
        gmailFbResModel.profilePic = "";
        if (googleSignInAccount.getPhotoUrl() != null) {
            gmailFbResModel.profilePic = googleSignInAccount.getPhotoUrl().toString();
        }

        if (!TextUtil.isEmpty(gmailFbResModel.userEmail)) {
            callCheckUser();
        } else {
            showSnackbarError("Google SignIn Error");
        }
        //funnel

        Map<String, String> bundle = new HashMap<String, String>();
        bundle.put(getResources().getString(R.string.login_page_userfbemail), gmailFbResModel.userEmail);
        bundle.put(getResources().getString(R.string.login_page_userfbname), gmailFbResModel.userName);
        firebaseEventUtil.logEvent(mContext, getResources().getString(R.string.login_page_verify_by_fb), bundle);


        AppLogger.e("Google Signin",
                "Name: " + googleSignInAccount.getDisplayName() +
                        ", email: " + googleSignInAccount.getEmail()
                        + ", Image: " + gmailFbResModel.profilePic);

    }

    @Override
    public void OnGSignError(GoogleSignInResult errorMessage) {
        AppLogger.e("Google Signin", "Error");
        binding.progressbar.pgbar.setVisibility(View.GONE);
        showSnackbarError("Google SignIn Error");
    }

    public void startActivityDashboardScreen(int otp) {

        //funnel add
        Map<String, String> bundle = new HashMap<>();
        bundle.put(getResources().getString(R.string.event_mobile_no), binding.textPhonenumber.getText().toString());
        firebaseEventUtil.logEvent(mContext, getResources().getString(R.string.event_verify_by_phone), bundle);

        Intent i = new Intent(LoginMainScreenActivity.this, OtpScreenActivity.class);
        i.putExtra(getResources().getString(R.string.intent_phone_number), binding.textPhonenumber.getText().toString());
        i.putExtra(getResources().getString(R.string.userOtp), otp);
        startActivity(i);

    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    private FbGoogleUserModel getUserModelFromGraphResponse(GraphResponse graphResponse) {
        FbGoogleUserModel userModel = new FbGoogleUserModel();
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        try {
            JSONObject jsonObject = graphResponse.getJSONObject();
            userModel.userName = jsonObject.getString("name");
            userModel.userEmail = jsonObject.getString("email");
            String id = jsonObject.getString("id");
            String profileImg = "http://graph.facebook.com/" + id + "/picture?type=large";
            userModel.profilePic = profileImg;
            prefModel.setFbuserModel(userModel);
            prefModel.setLogin(true);
            DemoAppPref.INSTANCE.setPref(prefModel);

            Log.i(TAG, profileImg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userModel;
    }

    private void startVerifyActivity(FbGoogleUserModel userModel) {
        Intent intent = new Intent(this, VerifyWithPhoneNoActivity.class);
        intent.putExtra(FbGoogleUserModel.class.getSimpleName(), userModel);
        startActivity(intent);
        this.finishAffinity();
    }

    private void sendOtpApiReqPass() {
        String phonenumber = "91" + binding.textPhonenumber.getText().toString();
        SendOtpReqModel mreqmodel = new SendOtpReqModel();
        mreqmodel.setMobileNo(phonenumber);
        viewModel.sendOtpApi(mreqmodel);
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
    }


    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:
                    /*Loading code here*/
                    break;

                case SUCCESS:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    if (responseApi.apiTypeStatus == Status.SEND_OTP) {
                        SendOtpResModel sendOtp = (SendOtpResModel) responseApi.data;
                        startActivityDashboardScreen(sendOtp.getOtp());
                    } else if (responseApi.apiTypeStatus == Status.CHECKVALIDUSER) {
                        binding.progressbar.pgbar.setVisibility(View.GONE);
                        CheckUserValidResModel userValidResModel = (CheckUserValidResModel) responseApi.data;
                        checkUserResponse(userValidResModel);
                    }
                    break;

                case NO_INTERNET:
                    // closeDialog();
                    //   showSnackbarError((String) responseApi.data);
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    break;

                case AUTH_FAIL:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    break;
                case FAIL_400:
// When Authrization is fail
                    // closeDialog();
                    // showSnackbarError((String) responseApi.data);
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
                    break;

                default:
                    //closeDialog();
                    //showSnackbarError((String) responseApi.data);
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
                    break;
            }

        });
    }


    private void checkUserResponse(CheckUserValidResModel userValidResModel) {
        binding.progressbar.pgbar.setVisibility(View.GONE);
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        prefModel.setFbuserModel(gmailFbResModel);

        if (!userValidResModel.getError()) {
            DemoAppPref.INSTANCE.setPref(prefModel);
            startActivityDashboardScreen(userValidResModel);
        } else {
            prefModel.setEmailId(gmailFbResModel.userEmail);
            DemoAppPref.INSTANCE.setPref(prefModel);
            startVerifyActivity(gmailFbResModel);
        }

    }


    private void startActivityDashboardScreen(CheckUserValidResModel userValidResModel) {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        prefModel.setUserMobile(userValidResModel.getMobileNo());
        prefModel.setStudentClass(ConversionUtil.INSTANCE.convertStringToInteger(userValidResModel.getStudentClass()));
        prefModel.setEmailId(userValidResModel.getEmailId());
        prefModel.setLogin(true);
        DemoAppPref.INSTANCE.setPref(prefModel);
        Intent i = new Intent(LoginMainScreenActivity.this, DashBoardActivity.class);
        startActivity(i);

    }

    private void callCheckUser() {
        CheckUserApiReqModel checkUserApiReqModel = new CheckUserApiReqModel();
        checkUserApiReqModel.setEmailId(gmailFbResModel.userEmail);
        checkUserApiReqModel.setMobileNo("");
        viewModel.checkUserValid(checkUserApiReqModel);
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getSource()) {
            case AppConstant.PRIVACY_POLICY_TEXT:
                openWebActivty(URLConstant.PRIVACY_POLICY);

                break;

            case AppConstant.TERMS_CONDITION_TEXT:
                openWebActivty(URLConstant.TERM_CONDITION);
                break;

        }
    }

    private void openWebActivty(String link) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(AppConstant.WEB_LINK, link);
        startActivity(intent);
    }
}