package com.demo.application.home.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;


import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.application.base_component.BaseActivity;
import com.demo.application.core.application.di.component.ViewModelFactory;
import com.demo.application.core.database.DemoAppPref;
import com.demo.application.core.database.PrefModel;
import com.demo.application.databinding.ActivityIntroSliderBinding;
import com.demo.application.home.presentation.view.adapter.IntroViewPager;
import com.demo.application.home.presentation.viewmodel.IntroScreenViewModel;

import javax.inject.Inject;
import javax.inject.Named;

public class IntroSliderActivity extends BaseActivity implements View.OnClickListener {


    @Inject
    @Named("IntroSliderActivity")
    ViewModelFactory viewModelFactory;
    ActivityIntroSliderBinding binding;

    private IntroScreenViewModel viewModel;
    private Context mContext;



    private IntroViewPager myViewPagerAdapter;

    private TextView[] dots;
    private int[] layouts;
    PrefModel prefModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        setListener();
    }
    public void init(){

        binding = DataBindingUtil.setContentView(this, getLayout());
        ((DemoApp) this.getApplication()).getAppComponent().doInjection(this);
        //view model and handler setup
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(IntroScreenViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        mContext = IntroSliderActivity.this;
        setAdapterForViewPager();

    }

    @Override
    protected void setListener() {
        binding.btnNext.setOnClickListener(this);
        binding.btnSkip.setOnClickListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_intro_slider;
    }

    private void launchHomeScreen() {
        prefModel.setIntroScreen(true);
        DemoAppPref.INSTANCE.setPref(prefModel);
        startActivity(new Intent(IntroSliderActivity.this, LoginMainScreenActivity.class));
        finish();
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        binding.layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            binding.layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                binding.btnNext.setText(getString(R.string.next));
                binding.btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                binding.btnNext.setText(getString(R.string.next));
                binding.btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private int getItem(int i) {
        return binding.viewPager.getCurrentItem() + i;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btn_next){
            // if last page home screen will be launched
            int current = getItem(+1);
            if (current < layouts.length) {
                // move to next screen
                binding.viewPager.setCurrentItem(current);
            } else {
                launchHomeScreen();
            }
        }else if(view.getId() == R.id.btn_skip){
            launchHomeScreen();
        }
    }

    public void setAdapterForViewPager(){
        prefModel = DemoAppPref.INSTANCE.getModelInstance();
        // Checking for first time launch - before calling setContentView()


        if (prefModel.isIntroScreen()) {
            launchHomeScreen();
            finish();
        }


        // layouts of all welcome sliders
        // add few more layouts if you want

        layouts = new int[]{ R.layout.welcome_slide1, R.layout.welcome_slide2, R.layout.welcome_slide3};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new IntroViewPager(layouts,mContext);
        binding.viewPager.setAdapter(myViewPagerAdapter);
        binding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

    }
}
