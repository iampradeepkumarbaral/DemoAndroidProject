package com.demo.application.home.presentation.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.application.base_component.BaseActivity;
import com.demo.application.core.application.di.component.ViewModelFactory;
import com.demo.application.core.common.AppConstant;
import com.demo.application.core.common.ValidationModel;
import com.demo.application.core.database.DemoAppPref;
import com.demo.application.core.database.PrefModel;
import com.demo.application.databinding.ActivityLoginScreenBinding;
import com.demo.application.home.presentation.viewmodel.LoginScreenViewModel;
import com.demo.application.util.ViewUtil;

import com.truecaller.android.sdk.TrueProfile;
import com.truecaller.android.sdk.TruecallerSDK;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class LoginScreenActivity extends BaseActivity {

    private static String TAG = LoginScreenActivity.class.getSimpleName();
    @Inject
    @Named("LoginScreenActivity")
    ViewModelFactory viewModelFactory;
    ActivityLoginScreenBinding binding;
    private LoginScreenViewModel viewModel;
    private Context mContext;
    List<String> userTypelist;
    List<String> classlist;
    String userType;
    int classtype = 0;
    int userTypeint = 0;

    PrefModel prefModel;
    HashMap<String, Integer> classesMap = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_login_screen);
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void init() {

        binding = DataBindingUtil.setContentView(this, getLayout());
        ((DemoApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginScreenViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        mContext = LoginScreenActivity.this;
        prefModel = new PrefModel();
        spinnermethod();
        setDesgin();
        setListener();
        truecallerSdk();
        //

    }

    @Override
    protected void setListener() {
        binding.register.setOnClickListener((View v) -> {
            trueallerDddone();
        });

    }

    private void trueallerDddone() {
        ValidationModel validation = viewModel.homeUseCase.isUserTypeSelected(userTypeint,classtype);

        if (validation.isStatus()) {

            if (TruecallerSDK.getInstance().isUsable()) {
               // TruecallerSDK.getInstance().getUserProfile(LoginScreenActivity.this);
                onDirectcall();

            } else {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setMessage("Truecaller App not installed.");

                dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                    Log.d(TAG, "onClick: Closing dialog");

                    dialog.dismiss();
                });

                dialogBuilder.setIcon(R.drawable.com_truecaller_icon);
                dialogBuilder.setTitle(" ");

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        } else {
            showSnackbarError(validation.getMessage());
        }

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login_screen;
    }

    public void spinnermethodcall(List<String> languageLines, AppCompatSpinner spinner) {
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, languageLines);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    public void setDesgin() {

        String privacy = "By joining us,you agree to the " + "<font color='#00A1DB'><u>Terms and Conditions</u></font>" + " and " + "<font color='#00A1DB'> <u>Privcy Policy</u></font> ";
        binding.privacy.setText(Html.fromHtml(privacy));

    }


    public void truecallerSdk() {
        launchHome(null);
    }

    private void launchHome(TrueProfile trueProfile) {

        startActivity(new Intent(LoginScreenActivity.this,DashBoardActivity.class));
        finishAffinity();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TruecallerSDK.getInstance().onActivityResultObtained(this, resultCode, data);
    }

    public void spinnermethod() {
        // Spinner Drop down userType
        userTypelist = Arrays.asList(getResources().getStringArray(R.array.select_type_scolar));
        spinnermethodcall(userTypelist, binding.SpinnerGender);
        binding.SpinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                userTypeint = position;
                if (userTypeint == 1) {

                    setValuesInPref(AppConstant.userTypeLogin.STUDENT);
                    binding.layoutClass.setVisibility(View.VISIBLE);
                    binding.register.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_medium_turque));
                } else if(position == 2) {
                    setValuesInPref(AppConstant.userTypeLogin.TEACHER);
                    binding.register.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.blue_color));
                    binding.layoutClass.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Spinner Drop down Class
        classlist = Arrays.asList(getResources().getStringArray(R.array.classes));
        for (int i = 0; i < classlist.size(); i++) {
            if (i > 0) {
                classesMap.put(classlist.get(i), 5 + i);
            }
        }
        spinnermethodcall(classlist, binding.SpinnerClass);
        binding.SpinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                classtype = position;
                if (position > 0) {
                    setClassInPref(classesMap.get(classlist.get(position)));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    private void setValuesInPref(int type) {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        prefModel.setUserType(type);
        DemoAppPref.INSTANCE.setPref(prefModel);
    }

    private void setClassInPref(int studentClass) {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        prefModel.setStudentClass(studentClass);
        DemoAppPref.INSTANCE.setPref(prefModel);
    }

    public void onDirectcall(){
        startActivity(new Intent(LoginScreenActivity.this,DashBoardActivity.class));
        finishAffinity();
    }
}
