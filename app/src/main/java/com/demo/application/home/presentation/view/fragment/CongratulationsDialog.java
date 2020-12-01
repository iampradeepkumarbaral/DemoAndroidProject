package com.demo.application.home.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.application.base_component.BaseDialog;
import com.demo.application.core.application.di.component.ViewModelFactory;
import com.demo.application.core.common.CommonCallBackListner;
import com.demo.application.core.common.CommonDataModel;
import com.demo.application.core.common.Status;
import com.demo.application.databinding.DialogCongratulations2Binding;
import com.demo.application.home.data.model.AssignmentReqModel;
import com.demo.application.home.data.model.DashboardResModel;
import com.demo.application.home.data.model.QuizResModel;
import com.demo.application.home.data.model.SubjectResModel;
import com.demo.application.home.presentation.viewmodel.CongratulationsDialogViewModel;
import com.demo.application.util.AppUtil;
import com.demo.application.util.ConversionUtil;
import com.demo.application.util.TextUtil;
import com.bumptech.glide.Glide;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;


import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

public class CongratulationsDialog extends BaseDialog implements View.OnClickListener,CommonCallBackListner {


    @Inject
    @Named("CongratulationsDialog")
    ViewModelFactory viewModelFactory;
    DialogCongratulations2Binding binding;
    CongratulationsDialogViewModel viewModel;
    public static String bundledashboardResModel = "dashboardResModel";
    public static String bundleassignmentReqModel = "assignmentReqModel";

    private static final String commonCallBackListnerbundle = "commonCallBackListner";
    private static final String dashboardResModelbundle = "dashboardResModel";
    private static final String assignmentReqModelbundle = "assignmentReqModel";
    Context mcontext;
    DashboardResModel dashboardResModel;
    AssignmentReqModel assignmentReqModel;
    static CommonCallBackListner commonCallBackListner;
    int marks;
    SubjectResModel subjectResModel;
    QuizResModel quizResModel;
    int finishedTestPos;

    private static final String TAG = CongratulationsDialog.class.getSimpleName();

/*
    public CongratulationsDialog(Context mcontext, DashboardResModel dashboardResModel, AssignmentReqModel assignmentReqModel, CommonCallBackListner commonCallBackListner) {
        this.mcontext = mcontext;
        this.dashboardResModel = dashboardResModel;
        this.assignmentReqModel = assignmentReqModel;
        this.commonCallBackListner = commonCallBackListner;
    }
*/

    public CongratulationsDialog(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            dashboardResModel = getArguments().getParcelable(getActivity().getResources().getString(R.string.bundledashboardresmodel));
            assignmentReqModel = getArguments().getParcelable(getActivity().getResources().getString(R.string.bundleassignmentreqmodel));
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((DemoApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CongratulationsDialogViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        init();
        setListener();
        return binding.getRoot();
    }

    @Override
    protected void init() {
        binding.btnShare.setOnClickListener(this);
        binding.icClose.setOnClickListener(this);
        binding.txtStartQuiz.setOnClickListener(this);
        binding.txtRetakeQuiz.setOnClickListener(this);


        Glide.with(this).load(R.raw.confetti_4).into(binding.backgroundSprincle11);

        // create random object
        Random randomno = new Random();
  /*      if (getArguments() != null) {
            dashboardResModel = getArguments().getParcelable(dashboardResModelbundle);
            assignmentReqModel = getArguments().getParcelable(assignmentReqModelbundle);
        }*/
        binding.tickerView.setPreferredScrollingDirection(TickerView.ScrollingDirection.DOWN);
        binding.tickerView.setCharacterLists(TickerUtils.provideNumberList());
        subjectResModel = dashboardResModel.getSubjectResModelList().get(assignmentReqModel.getSubjectPos());
        finishedTestPos = ConversionUtil.INSTANCE.convertStringToInteger(assignmentReqModel.getExam_name());
        quizResModel = subjectResModel.getChapter().get(finishedTestPos - 1);
        marks = quizResModel.getScorepoints() * 10;

        for (int i = 1; i <= marks; i++) {
            binding.tickerView.setText(i + "%");
        }

        if (!TextUtil.isEmpty(dashboardResModel.getStudent_name())) {
            binding.RPTextView4.setVisibility(View.VISIBLE);
            binding.RPTextView4.setText(dashboardResModel.getStudent_name());
        } else {
            binding.RPTextView4.setVisibility(View.GONE);
        }

        if (checkAllQuizAreFinishedOrNot()) {
            binding.txtStartQuiz.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        commonCallBackListner = this;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_congratulations_2;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnShare) {
            //shareWithFriends();
            dismiss();
        } else if (id == R.id.icClose) {
            dismiss();
        } else if (id == R.id.txtRetakeQuiz) {
        } else if (id == R.id.txtStartQuiz) {
            makeQuiz();
            dismiss();
        }
    }
    public static CongratulationsDialog newInstance(CommonCallBackListner listner, DashboardResModel dashboardResModel, AssignmentReqModel assignmentReqModel) {


        CongratulationsDialog fragment = new CongratulationsDialog();
        commonCallBackListner=listner;
        Bundle args = new Bundle();
        args.putParcelable(bundledashboardResModel, dashboardResModel);
        args.putParcelable(bundleassignmentReqModel, assignmentReqModel);
        fragment.setArguments(args);
        return fragment;
    }



//    public void shareWithFriends() {
//        String completeLink = getActivity().getResources().getString(R.string.invite_friend_refrral);
//        if (DemoApp.getAuroScholarModel() != null && !TextUtil.isEmpty(DemoApp.getAuroScholarModel().getReferralLink())) {
//            completeLink = completeLink + " " + DemoApp.getAuroScholarModel().getReferralLink();
//        } else {
//            completeLink = completeLink + " https://rb.gy/np9uh5";
//        }
//
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, completeLink);
//        sendIntent.setType("text/plain");
//        Intent shareIntent = Intent.createChooser(sendIntent, null);
//        dismiss();
//        getActivity().startActivity(shareIntent);
//    }

    private String generateChars(Random random, String list, int numDigits) {
        final char[] result = new char[numDigits];
        for (int i = 0; i < numDigits; i++) {
            result[i] = list.charAt(random.nextInt(list.length()));
        }
        return new String(result);
    }


    private void makeQuiz() {
        int lastPos = finishedTestPos - 1;
        for (int i = 0; i < subjectResModel.getChapter().size(); i++) {
            if (i != lastPos) {
                if (subjectResModel.getChapter().get(i).getAttempt() < 3) {
                    sendClickCallBack(subjectResModel.getChapter().get(i));
                    break;
                }
            }
        }
    }


    private boolean checkAllQuizAreFinishedOrNot() {
        int totalAttempt = 0;
        for (QuizResModel quizResModel : subjectResModel.getChapter()) {
            totalAttempt = quizResModel.getAttempt() + totalAttempt;
        }
        if (totalAttempt == 12) {
            return true;
        }
        return false;
    }


    private void sendClickCallBack(QuizResModel quizResModel) {
        if (commonCallBackListner != null) {
            commonCallBackListner.commonEventListner(AppUtil.getCommonClickModel(0, Status.NEXT_QUIZ_CLICK, quizResModel));
        }
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {

    }
}
