package com.demo.application.home.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.application.base_component.BaseFragment;
import com.demo.application.core.application.di.component.ViewModelFactory;
import com.demo.application.databinding.FriendsInviteLayoutBinding;
import com.demo.application.home.presentation.view.adapter.LeaderBoardAdapter;
import com.demo.application.home.presentation.viewmodel.FriendsInviteViewModel;

import javax.inject.Inject;
import javax.inject.Named;

import static com.demo.application.core.common.Status.INVITE_FRIENDS_LIST;


public class FriendsInviteBoardFragment extends BaseFragment implements View.OnClickListener {

    @Inject
    @Named("FriendsInviteBoardFragment")
    ViewModelFactory viewModelFactory;
    FriendsInviteLayoutBinding binding;
    FriendsInviteViewModel viewModel;
    LeaderBoardAdapter leaderBoardAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((DemoApp) getActivity().getApplication()).getAppComponent().doInjection(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FriendsInviteViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        return binding.getRoot();
    }


    @Override
    protected void init() {
        binding.headerParent.cambridgeHeading.setVisibility(View.GONE);
        binding.toolbarLayout.backArrow.setOnClickListener(this);
        //  mlistener = this;
        setListener();
        //setLeaderBoard();
        viewModel.getFriendsListData();
    }


    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);

        } else {
            observeServiceResponse();
        }
    }


    @Override
    protected int getLayout() {
        return R.layout.friends_invite_layout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            // dashboardResModel = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);

        }
        init();
        setToolbar();
        setListener();
    }

    private void setLeaderBoard() {
        binding.friendsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.friendsList.setHasFixedSize(true);
        //  leaderBoardAdapter = new LeaderBoardAdapter(viewModel.homeUseCase.makeListForFriendsLeaderBoard(false));
        binding.friendsList.setAdapter(leaderBoardAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        getActivity().getSupportFragmentManager().popBackStack();
    }


    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:
                    //For ProgressBar
                    handleProgress(0);

                    break;

                case SUCCESS:
                    if (responseApi.apiTypeStatus == INVITE_FRIENDS_LIST) {
                        handleProgress(1);
                    }
                    break;

                case NO_INTERNET:
                    handleProgress(2);
                    break;

                case AUTH_FAIL:
                case FAIL_400:
                    handleProgress(2);

                    break;


                default:

                    break;
            }

        });
    }

    private void handleProgress(int i) {
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }


}
