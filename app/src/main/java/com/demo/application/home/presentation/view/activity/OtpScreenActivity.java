package com.demo.application.home.presentation.view.activity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.demo.application.databinding.ActivityOtpScreenBinding;
import com.demo.application.home.data.model.CheckUserApiReqModel;
import com.demo.application.home.data.model.SendOtpReqModel;
import com.demo.application.home.data.model.VerifyOtpReqModel;
import com.demo.application.home.data.model.response.CheckUserValidResModel;
import com.demo.application.home.data.model.response.SendOtpResModel;
import com.demo.application.home.data.model.response.VerifyOtpResModel;
import com.demo.application.home.presentation.viewmodel.OtpScreenViewModel;
import com.demo.application.util.ConversionUtil;
import com.demo.application.util.ViewUtil;
import com.demo.application.util.firebase.FirebaseEventUtil;
import com.demo.application.util.timer.Hourglass;
import com.demo.application.core.network.URLConstant;
import com.demo.application.util.TextUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import in.aabhasjindal.otptextview.OTPListener;

public class OtpScreenActivity extends BaseActivity implements OTPListener, View.OnClickListener, CommonCallBackListner {


    @Inject
    @Named("OtpScreenActivity")
    ViewModelFactory viewModelFactory;

    OtpScreenViewModel viewModel;
    Activity activity;
    private static String TAG = OtpScreenActivity.class.getSimpleName();
    ActivityOtpScreenBinding binding;
    String otptext = "";
    String phoneNumber = "";
    Hourglass timer;
    FirebaseEventUtil firebaseEventUtil;

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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(OtpScreenViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        activity = OtpScreenActivity.this;
        firebaseEventUtil = new FirebaseEventUtil();

        if (getIntent().hasExtra(getResources().getString(R.string.intent_phone_number))) {
          /*  trueProfile = getIntent().getParcelableExtra("profile");
            userType = getIntent().getStringExtra("userType");*/
            //otptext = String.valueOf(getIntent().getIntExtra(getResources().getString(R.string.userOtp), 0));
            phoneNumber = getIntent().getStringExtra(getResources().getString(R.string.intent_phone_number));
            binding.textOtpShow.setText(this.getString(R.string.opt_sent_number_txt) +" " +phoneNumber);

        }

//
//        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
//            viewModel.serviceLiveData().removeObservers(this);
//
//        } else {
//            observeServiceResponse();
//        }
        ViewUtil.customTextView(binding.RPTextView7, this);
        initRecordingTimer();
    }

    @Override
    protected void setListener() {
        binding.otpView.setOtpListener(this);
        binding.RPButtonConfirm.setOnClickListener(this);
        binding.resendText.setOnClickListener(this);
        binding.backButton.backButton.setOnClickListener(this);
        binding.codeEditMobileno.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_otp_screen;
    }

    @Override
    public void onInteractionListener() {
    }

    @Override
    public void onOTPComplete(String otp) {
        otptext = otp;
        //Toast.makeText(this, "he OTP is" + otp, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.RPButtonConfirm:
                if (!otptext.isEmpty() && otptext.length() == 6) {
                    binding.otpView.showSuccess();
                    ViewUtil.hideKeyboard(this);
                   // verifyOtpRxApi();
                    startDashboardActivity();

                } else {
                    otptext = "";
                    binding.otpView.showError();
                    showSnackbarError(this.getString(R.string.enter_otp_txt));

                }
                break;

            case R.id.resendText:
                sendOtpApiReqPass();
                break;

            case R.id.code_editMobileno:
                onBackPressed();
                break;
        }

    }


    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    private void verifyOtpRxApi() {
        VerifyOtpReqModel mverifyOtpRequestModel = new VerifyOtpReqModel();
        mverifyOtpRequestModel.setMobileNumber(phoneNumber);
        mverifyOtpRequestModel.setOtpVerify(otptext);
        viewModel.verifyOtpApi(mverifyOtpRequestModel);
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
    }

    private void startDashboardActivity() {

        //funnel add
        Map<String, String> bundle = new HashMap<>();
        bundle.put(getResources().getString(R.string.event_otp_verify_mobileno), phoneNumber);
        firebaseEventUtil.logEvent(this, getString(R.string.event_otp_page), bundle);
        Intent i = new Intent(OtpScreenActivity.this, DashBoardActivity.class);
        startActivity(i);

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
                    binding.progressbar.pgbar.setVisibility(View.GONE);
                    if (responseApi.apiTypeStatus == Status.VERIFY_OTP) {
                        VerifyOtpResModel verifyOtp = (VerifyOtpResModel) responseApi.data;
                        if (!verifyOtp.getError()) {
                            callCheckUser(verifyOtp);
                        } else {
                            ViewUtil.showSnackBar(binding.getRoot(), verifyOtp.getMessage());
                        }
                    }

                    if (responseApi.apiTypeStatus == Status.CHECKVALIDUSER) {
                        CheckUserValidResModel userValidResModel = (CheckUserValidResModel) responseApi.data;
                        checkUserResponse(userValidResModel);
                    }

                    if (responseApi.apiTypeStatus == Status.SEND_OTP) {
                        binding.progressbar.pgbar.setVisibility(View.GONE);
                        SendOtpResModel sendOtp = (SendOtpResModel) responseApi.data;
                        if (!sendOtp.isError()) {
                            initRecordingTimer();
                            otptext = String.valueOf(sendOtp.getOtp());
                        } else {
                            ViewUtil.showSnackBar(binding.getRoot(), sendOtp.getMessage());
                        }

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

    private void checkUserResponse(CheckUserValidResModel userValidResModel) {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        prefModel.setUserMobile(phoneNumber);
        prefModel.setLogin(true);
        if (!userValidResModel.getError()) {
            prefModel.setStudentClass(ConversionUtil.INSTANCE.convertStringToInteger(userValidResModel.getStudentClass()));
            if (!TextUtil.isEmpty(userValidResModel.getEmailId())) {
                prefModel.setEmailId(userValidResModel.getEmailId());
            }
            prefModel.setLogin(true);
        }
        DemoAppPref.INSTANCE.setPref(prefModel);

        startDashboardActivity();
    }

    private void sendOtpApiReqPass() {

        SendOtpReqModel mreqmodel = new SendOtpReqModel();
        mreqmodel.setMobileNo(phoneNumber);
        viewModel.sendOtpApi(mreqmodel);
        binding.progressbar.pgbar.setVisibility(View.VISIBLE);
    }


    public void initRecordingTimer() {
        binding.codeValidText.setVisibility(View.VISIBLE);
        binding.timerLayout.setVisibility(View.VISIBLE);
        binding.resendText.setVisibility(View.GONE);
        timer = new Hourglass() {
            @Override
            public void onTimerTick(final long timeRemaining) {

                int seconds = (int) (timeRemaining / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                binding.timerText.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            @Override
            public void onTimerFinish() {
                binding.codeValidText.setVisibility(View.GONE);
                binding.timerLayout.setVisibility(View.GONE);
                binding.resendText.setVisibility(View.VISIBLE);
            }
        };
        timer.setTime(120 * 1000 + 1000);
        timer.startTimer();

    }

    private void callCheckUser(VerifyOtpResModel verifyOtp) {
        CheckUserApiReqModel checkUserApiReqModel = new CheckUserApiReqModel();
        checkUserApiReqModel.setMobileNo(phoneNumber);
        checkUserApiReqModel.setEmailId("");
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