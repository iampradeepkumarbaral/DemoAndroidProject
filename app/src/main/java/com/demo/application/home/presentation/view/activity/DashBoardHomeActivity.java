package com.demo.application.home.presentation.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.application.base_component.BaseActivity;
import com.demo.application.core.application.di.component.ViewModelFactory;
import com.demo.application.core.common.AppConstant;
import com.demo.application.core.common.CommonDataModel;
import com.demo.application.core.common.FragmentUtil;
import com.demo.application.core.common.MessgeNotifyStatus;
import com.demo.application.core.common.OnItemClickListener;
import com.demo.application.core.util.uiwidget.others.HideBottomNavigation;

import com.demo.application.databinding.ActivityDashboardAppBinding;
import com.demo.application.home.data.datasource.remote.HomeRemoteApi;

import com.demo.application.home.presentation.viewmodel.HomeViewDashBoardModel;
import com.demo.application.util.AppLogger;
import com.demo.application.util.LocationUtil;
import com.demo.application.util.ViewUtil;
import com.demo.application.util.permission.LocationHandler;
import com.demo.application.util.permission.PermissionListener;
import com.demo.application.util.permission.PermissionModel;
import com.demo.application.util.permission.PermissionUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;
import javax.inject.Named;

public class DashBoardHomeActivity extends BaseActivity implements OnItemClickListener, HideBottomNavigation, BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener, PermissionListener {

    @Inject
    HomeRemoteApi remoteApi;

    @Inject
    @Named("DashBoardHomeActivity")
    ViewModelFactory viewModelFactory;

    private ActivityDashboardAppBinding binding;
    private Context mContext;
    private HomeViewDashBoardModel viewModel;
    private static int LISTING_ACTIVE_FRAGMENT = 0;
    int backPress = 0;
    public static final int CARD_FRAGMENT = 1;
    public static final int HOME_FRAGMENT = 2;
    public static final int MORE_FRAGMENT = 3;
    public static final int DINE_FRAGMENT = 4;
    public static final int BACK_FRAGMENT = 5;
    public static final int REWARDS_FRAGMENT = 6;
    public static final int MY_TRANSACTIONS_FRAGMENT = 7;
    public static final int MY_TRANSACTIONS_LIST_FRAGMENT = 8;
    public static final int LOCATIONS_FRAGMENT = 9;
    public static final int OUTDETAIL_FRAGMENT = 10;
    public static final int BOOK_A_TABLE_FRAGMENT = 11;
    public static final int OUTLET_DETAIL_FRAGMENT = 12;
    private String TAG = DashBoardHomeActivity.class.getSimpleName();
    String memberType;


    CommonDataModel commonDataModel;


    /*Permission Handling*/
    PermissionResults permissionResults;
    private PermissionUtil mPermissionUtil = new PermissionUtil();
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;


    public static int getListingActiveFragment() {
        return LISTING_ACTIVE_FRAGMENT;
    }

    public static void setListingActiveFragment(int listingActiveFragment) {
        LISTING_ACTIVE_FRAGMENT = listingActiveFragment;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewUtil.setLanguageonUi(this);
        init();
        setListener();

    }


    @Override
    protected void init() {
        memberType = "Member";
        binding = DataBindingUtil.setContentView(this, getLayout());
        ((DemoApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewDashBoardModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        mContext = DashBoardHomeActivity.this;
        setLightStatusBar(this);

        if (getIntent() != null && getIntent().getSerializableExtra(AppConstant.COMING_FROM) != null) {
            commonDataModel = (CommonDataModel) getIntent().getSerializableExtra(AppConstant.COMING_FROM);
        }


        //
        setHomeFragmentTab();

        binding.naviagtionContent.fabHomeButton.setOnClickListener(this);

        if (commonDataModel != null && commonDataModel.getSource() == AppConstant.SELECT_PROFILE_ACTIVITY) {
            //openWelcomeDialog();
        }
    }


    @Override
    protected void setListener() {


        viewModel.getNotifyLiveData().observeForever(new Observer<MessgeNotifyStatus>() {
            @Override
            public void onChanged(MessgeNotifyStatus messgeNotifyStatus) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.home_container);

                switch (messgeNotifyStatus.getStatus()) {
                    case SEARCH:

                        break;
                    case BASKET:

                        break;
                    case NOTIFICATION:

                        break;
                    case HUMBURGER:
                        break;
                    default:
                        break;
                }
            }
        });


        binding.naviagtionContent.bottomNavigation.setOnNavigationItemSelectedListener(this);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_dashboard_app;
    }


    private void setLightStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // add LIGHT_STATUS_BAR to flag
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(getColor(R.color.toolbar_pink)); // optional

        }
    }

    @Override
    public void onItemClick(int position) {


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment topFragment = getSupportFragmentManager().findFragmentById(R.id.home_container);


        switch (menuItem.getItemId()) {


            case R.id.action_home: {
                selectNavigationMenu(2);

                break;
            }

        }

        return false;
    }

    private void setText(String text) {
        popBackStack();
        binding.naviagtionContent.errorMesssage.setVisibility(View.VISIBLE);
        binding.naviagtionContent.errorMesssage.setText(text);

    }


    public void openHomeFragment(Fragment fragment) {
        FragmentUtil.replaceFragment(mContext, fragment, R.id.home_container, false, AppConstant.NEITHER_LEFT_NOR_RIGHT);

    }


    private void selectNavigationMenu(int pos) {

        binding.naviagtionContent.bottomNavigation.getMenu().getItem(pos).setChecked(true);
        if (pos == 2) {
            setHomeIconResourceSelect();
        } else {
            setHomeIconResourceUnSelect();
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onBackPressed() {
        backStack();

    }


    private void hideBottomNavigationView(ConstraintLayout view) {
        view.clearAnimation();
        view.animate().translationY(view.getHeight()).setDuration(700);
        view.setVisibility(View.GONE);
    }

    public void showBottomNavigationView(ConstraintLayout view) {
        view.clearAnimation();
        view.animate().translationY(0).setDuration(500);
        view.setVisibility(View.VISIBLE);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClose() {
        hideBottomNavigationView(binding.naviagtionContent.navigationParentLayout);
        hideBottomNavigationView(binding.naviagtionContent.imageContainer);

        binding.naviagtionContent.fabHomeButton.setVisibility(View.GONE);


    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onOpen() {
        showBottomNavigationView(binding.naviagtionContent.navigationParentLayout);
        showBottomNavigationView(binding.naviagtionContent.imageContainer);
        binding.naviagtionContent.fabHomeButton.setVisibility(View.VISIBLE);

    }

    private synchronized void backStack() {

        switch (LISTING_ACTIVE_FRAGMENT) {

            case CARD_FRAGMENT:
            case MORE_FRAGMENT:
            case REWARDS_FRAGMENT:
            case LOCATIONS_FRAGMENT:
                setHomeFragmentTab();
                break;

            case HOME_FRAGMENT:
                dismissApplication();
                break;

            case OUTLET_DETAIL_FRAGMENT:

                break;


            case OUTDETAIL_FRAGMENT:
            case MY_TRANSACTIONS_LIST_FRAGMENT:
            case MY_TRANSACTIONS_FRAGMENT:
            case BOOK_A_TABLE_FRAGMENT:
            case BACK_FRAGMENT:
            case DINE_FRAGMENT:
                popBackStack();
                break;


            default:
                backPress = 0;
                setHomeFragmentTab();
                break;
        }
    }


    private void popBackStack() {
        backPress = 0;
        getSupportFragmentManager().popBackStack();
    }


    private void dismissApplication() {

        if (backPress == 0) {
            backPress++;
            //ViewUtil.showSnackBar(binding.naviagtionContent.homeContainer, getString(R.string.app_name));
        } else {
            finishAffinity();
        }

    }


    public void setHomeFragmentTab() {
        backPress = 0;
        // HomeFragment homeFragment = createObjectHomeFragment();
        // openHomeFragment(homeFragment);

        binding.naviagtionContent.bottomNavigation.getMenu().getItem(2).setChecked(true);
        setHomeIconResourceSelect();
    }

   /* private HomeFragment createObjectHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MEMBER_TYPE, memberType);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }*/

    private void setHomeIconResourceSelect() {
        setText("Camera Screen under development");
        binding.naviagtionContent.fabHomeButton.setImageResource(R.drawable.home_placeholder);
    }

    private void setHomeIconResourceUnSelect() {
        binding.naviagtionContent.fabHomeButton.setImageResource(R.drawable.home_active_placeholder);
    }


    @Override
    public void onClick(View view) {
        setFabUi();
    }

    private void setFabUi() {
        selectNavigationMenu(2);
       /* HomeFragment homeFragment = createObjectHomeFragment();
        openHomeFragment(homeFragment);*/

    }

    @Override
    public void permissionCallback(PermissionModel permissionModel) {

    }

/*
    private void openWelcomeDialog() {
        WelcomeDialog welcomeDialog = new WelcomeDialog();
        if (!welcomeDialog.isVisible()) {
            welcomeDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
            welcomeDialog.setCancelable(true);
            welcomeDialog.show(Objects.requireNonNull(this).getSupportFragmentManager(), "OtpDialog");

        }
    }*/



    /*LOCATION CODE*/

    public interface PermissionResults {
        void onPermissionReceived(boolean result, int permissionType);

    }

    public void setResultListener(PermissionResults listener) {
        this.permissionResults = listener;
    }






    public void getCurrentLocation() {
        LocationHandler locationHandlerUpdate = new LocationHandler();
        locationHandlerUpdate.setUpGoogleClient(DashBoardHomeActivity.this);
        if (LocationUtil.isGPSEnabled(this)) {
            sendPermissionCllback(true, 77);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AppLogger.d("PermissionUtils", "Request Code" + requestCode);

        /*  for locations persmission*/
        if (requestCode == AppConstant.PermissionCode.LOCATION_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                AppLogger.d(TAG, " Location PERMISSION_GRANTED");
                getCurrentLocation();

            } else {
                sendPermissionCllback(false, AppConstant.PermissionCode.LOCATION_PERMISSION_CODE);
            }

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstant.PermissionCode.LOCATION_PERMISSION_CODE) {
            AppLogger.d(TAG, "RESULT_CODE_GPS_SETTING");
        } else if (requestCode == REQUEST_CHECK_SETTINGS_GPS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    getCurrentLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    sendPermissionCllback(false, AppConstant.PermissionCode.LOCATION_PERMISSION_CODE);
                    break;
                default:
                    // do nothing here
                    break;

            }

        }


    }

    private void sendPermissionCllback(boolean result, int code) {
        if (permissionResults != null) {
            permissionResults.onPermissionReceived(result, code);
        }
    }




}
