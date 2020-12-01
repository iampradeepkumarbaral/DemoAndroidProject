package com.demo.application.home.presentation.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.application.base_component.BaseFragment;
import com.demo.application.core.application.di.component.ViewModelFactory;

import com.demo.application.databinding.FragmentQuizHomeNewBinding;
import com.demo.application.home.data.model.ChapterResModel;
import com.demo.application.home.data.model.QuizTestDataModel;

import com.demo.application.home.presentation.view.adapter.QuizItemNewAdapter;
import com.demo.application.home.presentation.viewmodel.QuizViewNewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizHomeNewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizHomeNewFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    @Inject
    @Named("QuizHomeNewFragment")
    ViewModelFactory viewModelFactory;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentQuizHomeNewBinding binding;
    boolean isStateRestore;
    QuizViewNewModel quizViewNewModel;
    QuizItemNewAdapter quizItemAdapter;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;




    public QuizHomeNewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuizHomeNewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuizHomeNewFragment newInstance(String param1, String param2) {
        QuizHomeNewFragment fragment = new QuizHomeNewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((DemoApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        quizViewNewModel = ViewModelProviders.of(this, viewModelFactory).get(QuizViewNewModel.class);
        binding.setLifecycleOwner(this);
        binding.setQuizViewNewModel(quizViewNewModel);
        setRetainInstance(true);
        return binding.getRoot();
        //  return inflater.inflate(R.layout.fragment_quiz_home_new, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void init() {
        setQuizListAdapter();
    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_quiz_home_new;
    }

    @Override
    public void onClick(View view) {

    }


    private void setQuizListAdapter() {
        binding.quizTypeList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.quizTypeList.setHasFixedSize(true);
       // quizItemAdapter = new QuizItemNewAdapter(this.getContext(), makeDummyList());
        binding.quizTypeList.setAdapter(quizItemAdapter);

    }

    private List<QuizTestDataModel> makeDummyList() {
        List<QuizTestDataModel> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                list.add(makeQuizTestModel("Maths", 50));
            }
            if (i == 1) {
                list.add(makeQuizTestModel("Science", 70));
            }
            if (i == 2) {
                list.add(makeQuizTestModel("English", 10));
            }
            if (i == 3) {
                list.add(makeQuizTestModel("Physics", 30));
            }
            if (i == 4) {
                list.add(makeQuizTestModel("Social Science", 60));
            }
        }
        return list;
    }

    private QuizTestDataModel makeQuizTestModel(String subjectName, int percentage) {
        QuizTestDataModel quizTestDataModel = new QuizTestDataModel();
        quizTestDataModel.setSubject(subjectName);
        quizTestDataModel.setScorePercentage(percentage);
        quizTestDataModel.setChapter(makeChapterList());
        return quizTestDataModel;
    }

    private List<ChapterResModel> makeChapterList() {
        List<ChapterResModel> chapterResModelList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                chapterResModelList.add(chapterModel(1, 1, 50, "Polynomials", 6));
            }
            if (i == 1) {
                chapterResModelList.add(chapterModel(2, 2, 50, "Natural Numbers", 10));
            }
            if (i == 2) {
                chapterResModelList.add(chapterModel(2, 3, 50, "Prime Numbers", 6));
            }
            if (i == 3) {
                chapterResModelList.add(chapterModel(3, 4, 50, "Odd Numbers", 10));
            }
        }
        return chapterResModelList;
    }

    private ChapterResModel chapterModel(int attmept, int quizNumer, int amount, String name, int points) {
        ChapterResModel chapterResModel = new ChapterResModel();
        chapterResModel.setAttempt(attmept);
        chapterResModel.setNumber(quizNumer);
        chapterResModel.setScholarshipamount(amount);
        chapterResModel.setName(name);
        chapterResModel.setTotalpoints(points);
        return chapterResModel;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

}
