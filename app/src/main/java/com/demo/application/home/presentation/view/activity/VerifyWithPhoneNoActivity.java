package com.demo.application.home.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.demo.application.core.application.DemoApp;
import com.demo.application.core.application.base_component.BaseActivity;
import com.demo.application.core.application.di.component.ViewModelFactory;
import com.demo.application.core.common.Status;
import com.demo.application.databinding.ActivityLoginMainScreenBinding;
import com.demo.application.home.data.model.SendOtpReqModel;
import com.demo.application.home.data.model.response.SendOtpResModel;
import com.demo.application.home.presentation.viewmodel.LoginScreenViewModel;
import com.demo.application.util.firebase.FirebaseEventUtil;
import com.demo.application.util.TextUtil;
import com.demo.application.util.ViewUtil;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;

import com.demo.application.R;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

public class VerifyWithPhoneNoActivity extends BaseActivity implements View.OnClickListener {


    private static final int RC_SIGN_IN = 1;
    private static String TAG = LoginMainScreenActivity.class.getSimpleName();
    @Inject
    @Named("VerifyWithPhoneNoActivity")
    ViewModelFactory viewModelFactory;
    ActivityLoginMainScreenBinding binding;
    private LoginScreenViewModel viewModel;
    FirebaseEventUtil firebaseEventUtil;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setLanguageonUi(this);
        init();
        setListener();

    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(this, getLayout());
        ((DemoApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginScreenViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        mContext = VerifyWithPhoneNoActivity.this;
        //funnel
        firebaseEventUtil = new FirebaseEventUtil();
        firebaseEventUtil.logEvent(mContext,getResources().getString(R.string.event_email_to_phoneno),new HashMap<>());

        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);

        } else {
            observeServiceResponse();
        }
    }

    @Override
    protected void setListener() {
        binding.linearLayoutOr.setVisibility(View.GONE);
        binding.facebookLayout.setVisibility(View.GONE);
        binding.googleLayout.setVisibility(View.GONE);
        binding.RPButtonSendOtp.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login_main_screen;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.RPButtonSendOtp) {
            if (binding.textPhonenumber.getText().length() == 10) {
                sendOtpApiReqPass();
                //startActivityToOtpScreen();
            } else if (TextUtil.isEmpty(binding.textPhonenumber.getText().toString())) {
                ViewUtil.showSnackBar(binding.getRoot(), "Enter Phone Number");
                // showSnackbarError("", Color.RED);
            } else {
                ViewUtil.showSnackBar(binding.getRoot(), "Invalid Phone Number");
            }
        }
    }

    private void showSnackbarError(String message, int color) {
        ViewUtil.showSnackBar(binding.getRoot(), message, color);
    }

    public void startActivityToOtpScreen(int otp) {

        //funnel add
        Map<String,String> bundle = new HashMap<>();
        bundle.put(getResources().getString(R.string.event_verify_phone_no),binding.textPhonenumber.getText().toString());
        firebaseEventUtil.logEvent(mContext,getResources().getString(R.string.event_verify_by_phone),bundle);

        Intent intent = new Intent(VerifyWithPhoneNoActivity.this, OtpScreenActivity.class);
        intent.putExtra(getResources().getString(R.string.intent_phone_number), binding.textPhonenumber.getText().toString());
        intent.putExtra(getResources().getString(R.string.userOtp), otp);
        startActivity(intent);

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
                    //For ProgressBar

                    //  openProgressDialog();
                    // Toast.makeText(mContext, "Loading...", Toast.LENGTH_SHORT).show();


                    break;

                case SUCCESS:
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    if (responseApi.apiTypeStatus == Status.SEND_OTP) {
                        SendOtpResModel sendOtp = (SendOtpResModel) responseApi.data;
                        startActivityToOtpScreen(sendOtp.getOtp());
                    }
                    break;

                case NO_INTERNET:
                    // closeDialog();
                    //   showSnackbarError((String) responseApi.data);
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    ViewUtil.showSnackBar(binding.getRoot(), (String) responseApi.data);
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
}