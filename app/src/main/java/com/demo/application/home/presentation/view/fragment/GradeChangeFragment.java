package com.demo.application.home.presentation.view.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.demo.application.R;
import com.demo.application.core.application.base_component.BaseFragment;
import com.demo.application.core.application.di.component.ViewModelFactory;
import com.demo.application.core.common.AppConstant;
import com.demo.application.core.common.ResponseApi;
import com.demo.application.core.database.DemoAppPref;
import com.demo.application.core.database.PrefModel;
import com.demo.application.databinding.FragmentGradeChangeDialogBinding;
import com.demo.application.home.data.model.response.ChangeGradeResModel;
import com.demo.application.home.data.model.response.CheckUserValidResModel;
import com.demo.application.home.presentation.viewmodel.GradeChangeViewModel;
import com.demo.application.core.application.DemoApp;
import com.demo.application.util.ConversionUtil;
import com.demo.application.util.ViewUtil;
import com.demo.application.util.alert_dialog.CustomDialog;
import com.demo.application.util.alert_dialog.CustomDialogModel;
import com.demo.application.home.data.model.DashboardResModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;


public class GradeChangeFragment extends BaseFragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    @Inject
    @Named("GradeChangeDialog")
    ViewModelFactory viewModelFactory;
    FragmentGradeChangeDialogBinding binding;
    GradeChangeViewModel viewModel;


    int classtype = 0;
    int studentClass = 0;

    // TODO: Rename and change types of parameters
    private String source;
    List<String> classlist;
    HashMap<String, Integer> classesMap = new HashMap<>();
    OnClickButton onClickButton;
    CustomDialog customDialog;
    DashboardResModel dashboardResModel;

    public GradeChangeFragment( ) {
    }
    public GradeChangeFragment(OnClickButton onClickButton) {
        this.onClickButton = onClickButton;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            source = getArguments().getString(AppConstant.COMING_FROM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((DemoApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GradeChangeViewModel.class);
        ViewUtil.setLanguageonUi(getActivity());
        setRetainInstance(true);
        init();
        setListener();
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_yes) {
            if (studentClass != 0) {
                changeGrade();
            } else {
                showSnackbarError("Please select the grade");
            }
        }
    }

    private void openErrorDialog() {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        String message = "You have changed your grade from " + prefModel.getStudentClass() + "th - " + studentClass + "th. Confirm if you want to change.\n\n Note: You will loose your current month's data and KYC will be verified again.";
        CustomDialogModel customDialogModel = new CustomDialogModel();
        customDialogModel.setContext(getActivity());
        customDialogModel.setTitle(DemoApp.getAppContext().getResources().getString(R.string.information));
        customDialogModel.setContent(message);
        customDialogModel.setTwoButtonRequired(true);
        customDialog = new CustomDialog(getActivity(), customDialogModel);
        customDialog.setSecondBtnTxt("Yes");
        customDialog.setFirstBtnTxt("No");
        customDialog.setFirstCallcack(new CustomDialog.FirstCallcack() {
            @Override
            public void clickNoCallback() {
                customDialog.dismiss();
            }
        });

        customDialog.setSecondCallcack(new CustomDialog.SecondCallcack() {
            @Override
            public void clickYesCallback() {
                customDialog.dismiss();
                callChangeGradeApi();


            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(customDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        customDialog.getWindow().setAttributes(lp);
        Objects.requireNonNull(customDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setCancelable(false);
        customDialog.show();
    }


    @Override
    protected void init() {
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
        if (getArguments() != null) {
            dashboardResModel = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);
        }

        selectClassBuSpinner();
    }

    @Override
    public void onResume() {
        super.onResume();
        setKeyListner();
    }

    @Override
    protected void setToolbar() {
    }

    @Override
    protected void setListener() {
        binding.btnYes.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_grade_change_dialog;
    }

    public void spinnermethodcall(List<String> languageLines, AppCompatSpinner spinner) {
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, languageLines);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    private void setClassInPref(int studentClass) {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        prefModel.setStudentClass(studentClass);
        DemoAppPref.INSTANCE.setPref(prefModel);
    }

    public void selectClassBuSpinner() {
        // Spinner Drop down Class
        classlist = Arrays.asList(getResources().getStringArray(R.array.grade));
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
                    studentClass = classesMap.get(classlist.get(position));
                    if (!source.equalsIgnoreCase(AppConstant.Source.DASHBOARD_NAVIGATION)) {
                        setClassInPref(classesMap.get(classlist.get(position)));
                    }
                } else {
                    studentClass = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public interface OnClickButton {
        void onClickListener();
    }

    private void changeGrade() {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        if (prefModel.getStudentClass() != 0) {
            if (source.equalsIgnoreCase(AppConstant.Source.DASHBOARD_NAVIGATION)) {
                openErrorDialog();
            } else {
                onClickButton.onClickListener();
            }
        } else {
            showSnackbarError("Please Select Your Class");
        }
    }


    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:
                    /*do coding here*/
                    break;

                case SUCCESS:
                    handleProgress(1);
                    handleApiRes(responseApi);
                    break;

                case NO_INTERNET:
//On fail
                    handleProgress(1);
                    showSnackbarError((String) responseApi.data);

                    break;

                case AUTH_FAIL:
                case FAIL_400:
                    handleProgress(1);
                    showSnackbarError((String) responseApi.data);
                    break;
                default:
                    handleProgress(1);
                    showSnackbarError((String) responseApi.data);
                    break;
            }

        });
    }


    private void handleProgress(int i) {
        switch (i) {
            case 0:
                binding.progressbar.pgbar.setVisibility(View.VISIBLE);
                break;

            case 1:
                binding.progressbar.pgbar.setVisibility(View.GONE);
                break;
        }
    }

    private void callChangeGradeApi() {
        handleProgress(0);
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        CheckUserValidResModel reqModel = new CheckUserValidResModel();
        reqModel.setMobileNo(prefModel.getUserMobile());
        reqModel.setStudentClass("" + studentClass);
        viewModel.changeGrade(reqModel);
    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    private void handleApiRes(ResponseApi responseApi) {
        ChangeGradeResModel resModel = (ChangeGradeResModel) responseApi.data;
        if (!resModel.getError()) {
            setClassInPref(ConversionUtil.INSTANCE.convertStringToInteger(resModel.getNewgrade()));
            onClickButton.onClickListener();
        } else {
            setClassInPref(ConversionUtil.INSTANCE.convertStringToInteger(resModel.getOldgrade()));
            showSnackbarError(resModel.getMessage());
        }


    }

    private void setKeyListner() {
        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (source.equalsIgnoreCase(AppConstant.Source.DASHBOARD_NAVIGATION)) {
                        onClickButton.onClickListener();
                    } else {
                        getActivity().onBackPressed();
                    }
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }
}