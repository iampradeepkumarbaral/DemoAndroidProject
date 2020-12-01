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
import com.demo.application.core.common.AppConstant;
import com.demo.application.core.common.CommonCallBackListner;
import com.demo.application.core.common.Status;
import com.demo.application.databinding.DialogLessScoreCongratulationsBinding;
import com.demo.application.home.data.model.AssignmentReqModel;
import com.demo.application.home.data.model.DashboardResModel;
import com.demo.application.home.data.model.QuizResModel;
import com.demo.application.home.data.model.SubjectResModel;
import com.demo.application.home.presentation.viewmodel.CongratulationsDialogViewModel;
import com.demo.application.util.AppUtil;
import com.demo.application.util.ConversionUtil;
import com.demo.application.util.TextUtil;
import com.demo.application.util.firebase.FirebaseEventUtil;
import com.google.android.material.shape.CornerFamily;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

public class ConsgratuationLessScoreDialog extends BaseDialog implements View.OnClickListener{


    @Inject
    @Named("CongratulationsDialog")
    ViewModelFactory viewModelFactory;
    public static String bundledashboardResModel = "dashboardResModel";
    public static String bundleassignmentReqModel = "assignmentReqModel";



    DialogLessScoreCongratulationsBinding binding;
    CongratulationsDialogViewModel viewModel;
    Context mcontext;
    static CommonCallBackListner commonCallBackListner;
    DashboardResModel dashboardResModel;
    AssignmentReqModel assignmentReqModel;
    FirebaseEventUtil firebaseEventUtil;
    int marks;
    int finishedTestPos;
    SubjectResModel subjectResModel;
    QuizResModel quizResModel;



    private static final String TAG = ConsgratuationLessScoreDialog.class.getSimpleName();


    public ConsgratuationLessScoreDialog( ) {

       ;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected void init() {
        setListener();
        firebaseEventUtil = new FirebaseEventUtil();
        binding.tickerView.setPreferredScrollingDirection(TickerView.ScrollingDirection.DOWN);
        float radius = getResources().getDimension(R.dimen._10sdp);
        binding.imgtryagain.setShapeAppearanceModel(binding.imgtryagain.getShapeAppearanceModel()
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build());
        binding.tickerView.setCharacterLists(TickerUtils.provideNumberList());
/*        if (getArguments() != null) {

            dashboardResModel = getArguments().getParcelable(getActivity().getResources().getString(R.string.bundledashboardresmodel));
            assignmentReqModel = getArguments().getParcelable(getActivity().getResources().getString(R.string.bundleassignmentreqmodel));
        }*/
        subjectResModel = dashboardResModel.getSubjectResModelList().get(assignmentReqModel.getSubjectPos());
        finishedTestPos = ConversionUtil.INSTANCE.convertStringToInteger(assignmentReqModel.getExam_name());
        quizResModel = subjectResModel.getChapter().get(finishedTestPos - 1);
        marks = quizResModel.getScorepoints() * 10;
        for (int i = 0; i <= marks; i++) {
            binding.tickerView.setText(i + "%");
        }

        if (ConversionUtil.INSTANCE.convertStringToInteger(assignmentReqModel.getQuiz_attempt()) < 3) {
            binding.txtRetakeQuiz.setVisibility(View.VISIBLE);
            binding.btntutor.setVisibility(View.GONE);
        } else {
            binding.txtRetakeQuiz.setVisibility(View.GONE);
            binding.btntutor.setVisibility(View.GONE);
        }

        if (checkAllQuizAreFinishedOrNot()) {
            binding.txtStartQuiz.setVisibility(View.VISIBLE);
            binding.txtRetakeQuiz.setVisibility(View.GONE);
            binding.btntutor.setVisibility(View.GONE);
        }

        binding.btnShare.setVisibility(View.VISIBLE);

        //funnels
        Map<String,String> bundle = new HashMap<String,String>();
        bundle.put(getResources().getString(R.string.event_student_score_page),String.valueOf(marks));
        firebaseEventUtil.logEvent(getContext(),getResources().getString(R.string.event_student_score_page), bundle);

        if (!TextUtil.isEmpty(dashboardResModel.getLeadQualified()) && dashboardResModel.getLeadQualified().equalsIgnoreCase(AppConstant.DocumentType.YES)) {
            binding.btntutor.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void setToolbar() {

    }

    public static ConsgratuationLessScoreDialog newInstance(CommonCallBackListner listner, DashboardResModel dashboardResModel, AssignmentReqModel assignmentReqModel) {
        commonCallBackListner=listner;
        ConsgratuationLessScoreDialog fragment = new ConsgratuationLessScoreDialog();
        Bundle args = new Bundle();
        args.putParcelable(bundledashboardResModel, dashboardResModel);
        args.putParcelable(bundleassignmentReqModel, assignmentReqModel);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    protected void setListener() {
        binding.icClose.setOnClickListener(this);
        binding.txtStartQuiz.setOnClickListener(this);
        binding.txtRetakeQuiz.setOnClickListener(this);
        binding.btntutor.setOnClickListener(this);
        binding.btnShare.setOnClickListener(this);

    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_less_score_congratulations;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnShare) {
            shareWithFriends();
            dismiss();
        } else if (id == R.id.icClose) {
            dismiss();
        } else if (id == R.id.txtRetakeQuiz) {
            sendClickCallBack(subjectResModel.getChapter().get(finishedTestPos - 1));
            dismiss();
        } else if (id == R.id.txtStartQuiz) {
            makeQuiz();
            dismiss();
        } else if (id == R.id.btntutor) {
//            if (DemoApp.getAuroScholarModel() != null && DemoApp.getAuroScholarModel().getSdkcallback() != null) {
//                DemoApp.getAuroScholarModel().getSdkcallback().commonCallback(Status.BOOK_TUTOR_SESSION_CLICK, "");
//            }
        }

    }

    public void shareWithFriends() {
        String completeLink = getActivity().getResources().getString(R.string.invite_friend_refrral);
//        if (DemoApp.getAuroScholarModel() != null && !TextUtil.isEmpty(DemoApp.getAuroScholarModel().getReferralLink())) {
//            completeLink = completeLink + " " + DemoApp.getAuroScholarModel().getReferralLink();
//        } else {
//            completeLink = completeLink + " https://rb.gy/np9uh5";
//        }
        
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,completeLink);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        dismiss();
        getActivity().startActivity(shareIntent);
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



}
