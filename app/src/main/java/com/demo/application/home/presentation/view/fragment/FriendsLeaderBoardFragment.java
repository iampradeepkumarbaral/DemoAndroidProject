package com.demo.application.home.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.application.base_component.BaseFragment;
import com.demo.application.core.application.di.component.ViewModelFactory;
import com.demo.application.core.common.AppConstant;
import com.demo.application.core.common.CommonCallBackListner;
import com.demo.application.core.common.CommonDataModel;
import com.demo.application.core.database.DemoAppPref;
import com.demo.application.core.database.PrefModel;
import com.demo.application.databinding.FriendsLeoboardLayoutBinding;
import com.demo.application.home.data.model.DashboardResModel;
import com.demo.application.home.data.model.FriendRequestList;
import com.demo.application.home.presentation.view.activity.DashBoardActivity;
import com.demo.application.home.presentation.view.adapter.LeaderBoardViewPagerAdapter;
import com.demo.application.home.presentation.viewmodel.FriendsLeaderShipViewModel;
import com.demo.application.util.TextUtil;
import com.demo.application.util.ViewUtil;
import com.demo.application.util.firebase.FirebaseEventUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import static com.demo.application.core.common.Status.FRIENDS_REQUEST_LIST;

public class FriendsLeaderBoardFragment extends BaseFragment implements View.OnClickListener, CommonCallBackListner {

    @Inject
    @Named("FriendsLeaderBoardFragment")
    ViewModelFactory viewModelFactory;

    private static final String TAG = FriendsLeaderBoardFragment.class.getSimpleName();
    FriendsLeoboardLayoutBinding binding;
    FriendsLeaderShipViewModel viewModel;

    FirebaseEventUtil firebaseEventUtil;
    LeaderBoardViewPagerAdapter viewPagerAdapter;

    FriendRequestList friendRequestList;
    DashboardResModel dashboardResModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((DemoApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FriendsLeaderShipViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        return binding.getRoot();
    }

    @Override
    protected void init() {
        firebaseEventUtil = new FirebaseEventUtil();
        setListener();
        binding.headerTopParent.cambridgeHeading.setVisibility(View.GONE);
        setDataUi();
//        if (DemoApp.getAuroScholarModel() != null) {
//            DemoApp.getAuroScholarModel().setSdkFragmentType(AppConstant.FragmentType.QUIZ_DASHBOARD);
//        }

        loadData();
        initialiseTabs();

    }

    public void loadData() {
        PrefModel prefModel = DemoAppPref.INSTANCE.getModelInstance();
        DashboardResModel dashboardResModel = prefModel.getDashboardResModel();
        if (dashboardResModel != null) {
            viewModel.friendRequestListData(Integer.valueOf(dashboardResModel.getAuroid()));
        }
    }

    private void setDataUi() {

    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        binding.headerParent.cambridgeHeading.setVisibility(View.GONE);
        binding.toolbarLayout.backArrow.setOnClickListener(this);
        binding.inviteButton.setOnClickListener(this);
        binding.toolbarLayout.langEng.setOnClickListener(this);
        binding.tvShowFriendRequests.setOnClickListener(this);

        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);

        } else {
            observeServiceResponse();
        }

    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:
                    //For ProgressBar
                    if (responseApi.apiTypeStatus == FRIENDS_REQUEST_LIST) {
                        handleProgress(0, "");
                    }

                    break;

                case SUCCESS:
                    if (responseApi.apiTypeStatus == FRIENDS_REQUEST_LIST) {
                        friendRequestList = (FriendRequestList) responseApi.data;
                        if (!friendRequestList.isError()) {
                            handleProgress(1, "");
                        }
                    }
                    break;

                case NO_INTERNET:
                case FAIL_400:
                    if (responseApi.apiTypeStatus == FRIENDS_REQUEST_LIST) {
                        handleProgress(3, (String) responseApi.data);
                    }
                    showSnackbarError((String) responseApi.data);
                    break;

                default:
                    if (responseApi.apiTypeStatus == FRIENDS_REQUEST_LIST) {
                        handleProgress(3, (String) responseApi.data);
                    }
                    showSnackbarError((String) responseApi.data);
                    break;
            }

        });
    }

    private void handleProgress(int i, String msg) {
        switch (i) {
            case 0:
                binding.tvRequestCount.setVisibility(View.GONE);
                break;

            case 1:
                if (friendRequestList != null && friendRequestList.getFriends() != null && friendRequestList.getFriends().size() > 0) {
                    binding.tvRequestCount.setVisibility(View.VISIBLE);
                    binding.tvRequestCount.setText(friendRequestList.getFriends().size() + "");
                } else {
                    binding.tvRequestCount.setVisibility(View.GONE);
                }

                break;

            case 2:
                showSnackbarError(msg);
                break;

            case 3:

                break;

        }

    }

    public void initialiseTabs() {
        for (int i = 0; i < getTabList().size(); i++) {
            binding.dineHomeTabs.addTab(binding.dineHomeTabs.newTab().setCustomView(getViewForEachTab(i)));
        }
        setViewPagerAdapter();
        binding.dineHomeTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setSelectedTabText(tab.getPosition());
                binding.dineHomeTabs.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(),R.color.off_white));
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do code here

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do code here
                if (binding.dineHomeTabs.getSelectedTabPosition() == 0) {
                    setSelectedTabText(0);
                }

            }
        });
        setSelectedTabText(0);
        binding.dineHomeTabs.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(),R.color.off_white));
    }

    public void setViewPagerAdapter() {
        viewPagerAdapter = new LeaderBoardViewPagerAdapter(getActivity(), getChildFragmentManager(), binding.dineHomeTabs.getTabCount());
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.dineHomeTabs));
    }

    private View getViewForEachTab(int tabNo) {
        View view = getLayoutInflater().inflate(R.layout.tab_text_layout, null);
        TextView tabTitle = view.findViewById(R.id.text_one);
        //tabTitle.setTextSize(8);
        tabTitle.setText(getTabList().get(tabNo));
        return view;
    }

    private void setSelectedTabText(int pos) {
        for (int i = 0; i < binding.dineHomeTabs.getTabCount(); i++) {
            View view = binding.dineHomeTabs.getTabAt(i).getCustomView();
            TextView text = view.findViewById(R.id.text_one);
            if (i == pos) {
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Poppins-SemiBold.ttf");
                text.setTextColor(ContextCompat.getColor(getActivity(),R.color.dark_magenta));
                text.setTypeface(face);
            } else {
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Poppins-SemiBold.ttf");
                text.setTextColor(ContextCompat.getColor(getActivity(),R.color.non_select));
                text.setTypeface(face);
            }

        }
    }

    public List<String> getTabList() {
        List<String> tabList = new ArrayList<>();
        tabList.add("Friend List");
        tabList.add("Add Friend");
        return tabList;
    }

    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    @Override
    protected int getLayout() {
        return R.layout.friends_leoboard_layout;
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

    @Override
    public void onResume() {
        super.onResume();
        setKeyListner();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvShowFriendRequests) {
            if (friendRequestList != null && friendRequestList.getFriends() != null && friendRequestList.getFriends().size() > 0) {
                FriendRequestListDialogFragment bottomSheetFragment = new FriendRequestListDialogFragment(this, friendRequestList);
                bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());

            } else {
                showSnackbarError("No friend request");
            }

        } else if (v.getId() == R.id.back_arrow) {
            getActivity().getSupportFragmentManager().popBackStack();
            //openFragment(new QuizHomeFragment());
        } else if (v.getId() == R.id.invite_button) {

            firebaseEventUtil.logEvent(getContext(), getResources().getString(R.string.log_friend_leader_board_student), new HashMap<String, String>());
            openShareDefaultDialog();
           /* mInviteBoxDialog = new InviteFriendDialog(getContext());
            openFragmentDialog(mInviteBoxDialog);*/
        } else if (v.getId() == R.id.lang_eng) {
            reloadFragment();
        } else if (v.getId() == R.id.invite_now) {
            firebaseEventUtil.logEvent(getContext(), getResources().getString(R.string.log_friend_leader_board_student), new HashMap<String, String>());
            openShareDefaultDialog();
        }
    }

//    private void openFragment(Fragment fragment) {
//        getActivity().getSupportFragmentManager().popBackStack();
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .setReorderingAllowed(true)
//                .replace(DemoApp.getFragmentContainerUiId(), fragment, InviteFriendDialog.class.getSimpleName())
//                .addToBackStack(null)
//                .commitAllowingStateLoss();
//
//    }

    private void reloadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

    private void openShareDefaultDialog() {
        String completeLink = getActivity().getResources().getString(R.string.invite_friend_refrral);
//        if (DemoApp.getAuroScholarModel() != null && !TextUtil.isEmpty(DemoApp.getAuroScholarModel().getReferralLink())) {
//            completeLink = completeLink + " " + DemoApp.getAuroScholarModel().getReferralLink();
//        } else {
//            completeLink = completeLink + " https://rb.gy/np9uh5";
//        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, completeLink);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        getActivity().startActivity(shareIntent);
    }

    private void setLanguageText(String text) {
        binding.toolbarLayout.langEng.setText(text);
    }

    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {
        switch (commonDataModel.getClickType()) {
            case LISTNER_SUCCESS:
                handleNavigationProgress(1,"");
                dashboardResModel = (DashboardResModel) commonDataModel.getObject();
                ((DashBoardActivity) getActivity()).dashboardModel(dashboardResModel);
                setDataUi();
                break;

            case LISTNER_FAIL:
                handleNavigationProgress(2,(String)commonDataModel.getObject());

                break;
        }



    }

    private void setKeyListner() {
        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }
    private void handleNavigationProgress(int status, String msg) {
        if (status == 0) {
            binding.transparet.setVisibility(View.VISIBLE);
            binding.friendleaderboard.setVisibility(View.VISIBLE);

            binding.errorConstraint.setVisibility(View.GONE);
        } else if (status == 1) {
            binding.transparet.setVisibility(View.GONE);
            binding.friendleaderboard.setVisibility(View.VISIBLE);

            binding.errorConstraint.setVisibility(View.GONE);
        } else if (status == 2) {
            binding.transparet.setVisibility(View.GONE);
            binding.friendleaderboard.setVisibility(View.GONE);

            binding.errorConstraint.setVisibility(View.VISIBLE);


            binding.errorLayout.textError.setText(msg);
        }
    }


}

