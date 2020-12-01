package com.demo.application.home.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.application.base_component.BaseFragment;
import com.demo.application.core.application.di.component.ViewModelFactory;
import com.demo.application.core.common.AppConstant;
import com.demo.application.core.common.CommonCallBackListner;
import com.demo.application.core.common.CommonDataModel;
import com.demo.application.core.database.DemoAppPref;
import com.demo.application.core.database.PrefModel;
import com.demo.application.databinding.KycFragmentLayoutBinding;
import com.demo.application.home.data.model.AssignmentReqModel;
import com.demo.application.home.data.model.DashboardResModel;
import com.demo.application.home.data.model.KYCDocumentDatamodel;
import com.demo.application.home.presentation.view.activity.DashBoardActivity;
import com.demo.application.home.presentation.view.adapter.KYCViewDocAdapter;
import com.demo.application.home.presentation.viewmodel.KYCViewModel;

import com.demo.application.util.AppLogger;
import com.demo.application.util.ConversionUtil;
import com.demo.application.util.TextUtil;
import com.demo.application.util.ViewUtil;
import com.demo.application.util.firebase.FirebaseEventUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import static com.demo.application.core.common.Status.AZURE_API;


public class KYCViewFragment extends BaseFragment implements View.OnClickListener, CommonCallBackListner {

    @Inject
    @Named("KYCViewFragment")
    ViewModelFactory viewModelFactory;


    KycFragmentLayoutBinding binding;
    String TAG = "KYCViewFragment";
    KYCViewModel kycViewModel;
    KYCViewDocAdapter kycViewDocAdapter;

    private DashboardResModel dashboardResModel;
    ArrayList<KYCDocumentDatamodel> kycDocumentDatamodelArrayList;
    Resources resources;
    FirebaseEventUtil firebaseEventUtil;
    private String comingFrom;


    /*Face Image Params*/
    List<AssignmentReqModel> faceModelList;
    int faceCounter = 0;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((DemoApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        kycViewModel = ViewModelProviders.of(this, viewModelFactory).get(KYCViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setKycViewModel(kycViewModel);
        setRetainInstance(true);
        ViewUtil.setLanguageonUi(getActivity());
        return binding.getRoot();
    }

    public void setAdapter() {
        this.kycDocumentDatamodelArrayList = kycViewModel.homeUseCase.makeAdapterDocumentList(dashboardResModel,getActivity());
        binding.documentUploadRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.documentUploadRecyclerview.setHasFixedSize(true);
        kycViewDocAdapter = new KYCViewDocAdapter(getActivity(), kycViewModel.homeUseCase.makeAdapterDocumentList(dashboardResModel,getActivity()));
        binding.documentUploadRecyclerview.setAdapter(kycViewDocAdapter);
    }

    @Override
    protected void init() {
        if (getArguments() != null) {
            dashboardResModel = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);
            comingFrom = getArguments().getString(AppConstant.COMING_FROM);
        }
        firebaseEventUtil = new FirebaseEventUtil();
        firebaseEventUtil.logEvent(getContext(),getResources().getString(R.string.event_kyc_document_uplaod_page),new HashMap<>());
        binding.btUploadAll.setVisibility(View.GONE);
        binding.btModifyAll.setVisibility(View.VISIBLE);

        if (comingFrom != null && comingFrom.equalsIgnoreCase(AppConstant.SENDING_DATA.DYNAMIC_LINK) ) {
            handleNavigationProgress(0,"");
            DashBoardActivity.setListingActiveFragment(DashBoardActivity.KYC_DIRECT_FRAGMENT);
            AppLogger.i(TAG,"Log DynamicLink");
            ((DashBoardActivity) getActivity()).setListner(this);

            ((DashBoardActivity) getActivity()).callDashboardApi();
        }else{
            DashBoardActivity.setListingActiveFragment(DashBoardActivity.QUIZ_KYC_VIEW_FRAGMENT);
            setDataOnUi();
        }


    }

    private void setDataOnUi() {
        if (dashboardResModel != null) {
            if (!TextUtil.isEmpty(dashboardResModel.getWalletbalance())) {
                //binding.walletBalText.setText(getString(R.string.rs) + " " + dashboardResModel.getWalletbalance());
                binding.walletBalText.setText(getString(R.string.rs) + " " + kycViewModel.homeUseCase.getWalletBalance(dashboardResModel));
            }
            setDataStepsOfVerifications();
        }
        binding.cambridgeHeading.cambridgeHeading.setTextColor(DemoApp.getAppContext().getResources().getColor(R.color.white));


    }

    private void setLanguageText(String text) {
        binding.toolbarLayout.langEng.setText(text);
    }

    @Override
    protected void setToolbar() {
        /*Do code here*/
    }

    @Override
    protected void setListener() {
        /*Do code here*/
        binding.toolbarLayout.backArrow.setVisibility(View.VISIBLE);
        binding.toolbarLayout.backArrow.setOnClickListener(this);
        binding.btModifyAll.setOnClickListener(this);
        binding.walletInfo.setOnClickListener(this);
        binding.toolbarLayout.langEng.setOnClickListener(this);
        if (kycViewModel != null && kycViewModel.serviceLiveData().hasObservers()) {
            kycViewModel.serviceLiveData().removeObservers(this);
        } else {
            observeServiceResponse();
        }
    }


    @Override
    protected int getLayout() {
        return R.layout.kyc_fragment_layout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setToolbar();
        setListener();
        setAdapter();

        checkForFaceImage();
    }


    private void checkForFaceImage() {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        if (prefModel != null && !TextUtil.checkListIsEmpty(prefModel.getListAzureImageList()) && prefModel.getListAzureImageList().size() > 0) {
            faceModelList = prefModel.getListAzureImageList();
            if (faceModelList.get(0) != null) {
                kycViewModel.sendAzureImageData(faceModelList.get(0));
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        setKeyListner();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_modify_all) {
            openKYCFragment();
        } else if (v.getId() == R.id.lang_eng) {
            reloadFragment();
        } else if (v.getId() == R.id.back_arrow) {
            getActivity().onBackPressed();
        } else if (v.getId() == R.id.bt_transfer_money) {
            //callNumber();

        } else if (v.getId() == R.id.wallet_info) {
            firebaseEventUtil.logEvent(getContext(),getResources().getString(R.string.event_payment_info_page),new HashMap<>());
            openTransactionFragment();
        }
    }




    private void openTransactionFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        TransactionsFragment fragment = new TransactionsFragment();
        fragment.setArguments(bundle);
        //openFragment(fragment);
    }


    public void callNumber() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:9667480783"));
        startActivity(callIntent);
    }


    private void reloadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

    public void openKYCFragment() {
        dashboardResModel.setModify(true);
        Bundle bundle = new Bundle();
        KYCFragment kycFragment = new KYCFragment();
        bundle.putParcelable(AppConstant.DASHBOARD_RES_MODEL, dashboardResModel);
        kycFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().popBackStack();
        //openFragment(kycFragment);
    }

//    private void openFragment(Fragment fragment) {
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .setReorderingAllowed(true)
//                .replace(DemoApp.getFragmentContainerUiId(), fragment, KYCViewFragment.class
//                        .getSimpleName())
//                .addToBackStack(null)
//                .commitAllowingStateLoss();
//
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // CustomSnackBar.INSTANCE.dismissCartSnackbar();
    }

    private void setDataStepsOfVerifications() {
       /* dashboardResModel.setIs_kyc_uploaded("Yes");
        dashboardResModel.setIs_kyc_verified("Rejected");
        dashboardResModel.setIs_payment_lastmonth("Yes");*/
          //  dashboardResModel.setIs_kyc_verified(AppConstant.DocumentType.YES);
      //  dashboardResModel.setApproved_scholarship_money("50");

        if (dashboardResModel.getIs_kyc_uploaded().equalsIgnoreCase(AppConstant.DocumentType.YES)) {
            binding.stepOne.tickSign.setVisibility(View.VISIBLE);
            binding.stepOne.textUploadDocumentMsg.setText(R.string.document_uploaded);
            binding.stepOne.textUploadDocumentMsg.setTextColor(DemoApp.getAppContext().getResources().getColor(R.color.ufo_green));
            if (dashboardResModel.getIs_kyc_verified().equalsIgnoreCase(AppConstant.DocumentType.IN_PROCESS)) {
                binding.stepTwo.textVerifyMsg.setText(getString(R.string.verification_is_in_process));
                binding.stepTwo.textVerifyMsg.setVisibility(View.VISIBLE);
            } else if (dashboardResModel.getIs_kyc_verified().equalsIgnoreCase(AppConstant.DocumentType.YES)) {
                binding.stepTwo.textVerifyMsg.setText(R.string.document_verified);
                binding.stepTwo.textVerifyMsg.setVisibility(View.VISIBLE);
                binding.stepTwo.tickSign.setVisibility(View.VISIBLE);
                binding.stepTwo.textVerifyMsg.setTextColor(DemoApp.getAppContext().getResources().getColor(R.color.ufo_green));
                int approvedMoney = ConversionUtil.INSTANCE.convertStringToInteger(dashboardResModel.getApproved_scholarship_money());
                if (approvedMoney < 1) {
                   /* binding.stepThree.tickSign.setVisibility(View.VISIBLE);
                    binding.stepThree.textQuizVerifyMsg.setText(DemoApp.getAppContext().getResources().getString(R.string.scholarship_approved));

                    binding.stepFour.textTransferMsg.setText(R.string.successfully_transfered);
                    binding.stepFour.textTransferMsg.setTextColor(DemoApp.getAppContext().getResources().getColor(R.color.ufo_green));
                    binding.stepFour.tickSign.setVisibility(View.GONE);
                    binding.stepFour.textTransferMsg.setVisibility(View.VISIBLE);
                    binding.stepFour.btTransferMoney.setVisibility(View.GONE);*/
                } else {
                    binding.stepThree.tickSign.setVisibility(View.VISIBLE);
                    binding.stepThree.textQuizVerifyMsg.setText(DemoApp.getAppContext().getResources().getString(R.string.scholarship_approved));
                    binding.stepThree.textQuizVerifyMsg.setTextColor(DemoApp.getAppContext().getResources().getColor(R.color.ufo_green));

                    binding.stepFour.textTransferMsg.setTextColor(DemoApp.getAppContext().getResources().getColor(R.color.ufo_green));
                    binding.stepFour.textTransferMsg.setText(R.string.call_our_customercare);
                    binding.stepFour.textTransferMsg.setVisibility(View.GONE);
                    binding.stepFour.tickSign.setVisibility(View.VISIBLE);
                    binding.stepFour.btTransferMoney.setVisibility(View.VISIBLE);
                    binding.stepFour.btTransferMoney.setOnClickListener(this);
                }

            } else if (dashboardResModel.getIs_kyc_verified().equalsIgnoreCase(AppConstant.DocumentType.REJECTED)) {
                binding.stepTwo.textVerifyMsg.setText(R.string.declined);
                binding.stepTwo.textVerifyMsg.setTextColor(DemoApp.getAppContext().getResources().getColor(R.color.color_red));
                binding.stepTwo.textVerifyMsg.setVisibility(View.VISIBLE);
                binding.stepTwo.tickSign.setVisibility(View.VISIBLE);
                binding.stepTwo.tickSign.setBackground(DemoApp.getAppContext().getResources().getDrawable(R.drawable.ic_cancel_icon));
                binding.stepFour.textTransferMsg.setTextColor(ContextCompat.getColor(getActivity(),R.color.auro_dark_blue));
                binding.stepFour.textTransferMsg.setText(R.string.you_will_see_transfer);
                binding.stepFour.btTransferMoney.setVisibility(View.GONE);
                binding.stepFour.tickSign.setVisibility(View.GONE);
            }
        }
    }

    private void observeServiceResponse() {
        kycViewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {
                case LOADING:
                    /*Do handling in background*/
                    break;

                case SUCCESS:
                    if (responseApi.apiTypeStatus == AZURE_API) {
                        sendFaceImageOnServer();
                    }

                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
                default:
                    updateFaceListInPref();
                    break;

            }

        });
    }

    private void sendFaceImageOnServer() {
        if (!TextUtil.checkListIsEmpty(faceModelList)) {
            try{
                faceModelList.get(faceCounter).setUploaded(true);
                faceCounter++;
                if (faceModelList.size() > faceCounter) {
                    kycViewModel.sendAzureImageData(faceModelList.get(faceCounter));
                } else {
                    updateFaceListInPref();
                }
            }catch (IndexOutOfBoundsException e){
                Toast.makeText(getActivity(),  "IndexOutOfBoundsException", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void updateFaceListInPref() {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        if (prefModel != null) {
            List<AssignmentReqModel> newList = new ArrayList<>();
            for (AssignmentReqModel model : faceModelList) {
                if (model != null && !model.isUploaded()) {
                    newList.add(model);
                }
            }
            prefModel.setListAzureImageList(newList);
            DemoAppPref.INSTANCE.setPref(prefModel);
        }
    }

    private void setKeyListner() {
        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                   // getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().onBackPressed();
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

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case LISTNER_SUCCESS:
                handleNavigationProgress(1,"");
                dashboardResModel = (DashboardResModel) commonDataModel.getObject();
                ((DashBoardActivity) getActivity()).dashboardModel(dashboardResModel);
                setDataOnUi();
                break;

            case LISTNER_FAIL:
                handleNavigationProgress(2,(String)commonDataModel.getObject());

                break;
        }
    }
    private void handleNavigationProgress(int status, String msg) {
        if (status == 0) {
           binding.transparet.setVisibility(View.VISIBLE);
           binding.kycbackground.setVisibility(View.VISIBLE);

           binding.errorConstraint.setVisibility(View.GONE);
        } else if (status == 1) {
            binding.transparet.setVisibility(View.GONE);
            binding.kycbackground.setVisibility(View.VISIBLE);

            binding.errorConstraint.setVisibility(View.GONE);
        } else if (status == 2) {
            binding.transparet.setVisibility(View.GONE);
            binding.kycbackground.setVisibility(View.GONE);

            binding.errorConstraint.setVisibility(View.VISIBLE);


            binding.errorLayout.textError.setText(msg);
            binding.errorLayout.btRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DashBoardActivity) getActivity()).callDashboardApi();
                }
            });
        }
    }

}
