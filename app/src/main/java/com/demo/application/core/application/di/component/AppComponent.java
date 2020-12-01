package com.demo.application.core.application.di.component;

import com.demo.application.core.application.DemoApp;
import com.demo.application.home.presentation.view.activity.DashBoardActivity;
import com.demo.application.home.presentation.view.activity.DashBoardHomeActivity;
import com.demo.application.home.presentation.view.activity.HomeActivity;
import com.demo.application.home.presentation.view.activity.IntroSliderActivity;
import com.demo.application.home.presentation.view.activity.LoginMainScreenActivity;
import com.demo.application.home.presentation.view.activity.LoginScreenActivity;
import com.demo.application.home.presentation.view.activity.OtpScreenActivity;
import com.demo.application.home.presentation.view.activity.SplashScreenActivity;
import com.demo.application.home.presentation.view.activity.VerifyWithPhoneNoActivity;
import com.demo.application.home.presentation.view.fragment.CardFragment;
import com.demo.application.core.application.di.module.AppModule;
import com.demo.application.core.application.di.module.HomeModule;
import com.demo.application.core.application.di.module.UtilsModule;
import com.demo.application.home.presentation.view.fragment.CertificateFragment;
import com.demo.application.home.presentation.view.fragment.CongratulationsDialog;
import com.demo.application.home.presentation.view.fragment.ConsgratuationLessScoreDialog;
import com.demo.application.home.presentation.view.fragment.DemographicFragment;
import com.demo.application.home.presentation.view.fragment.FriendRequestListDialogFragment;
import com.demo.application.home.presentation.view.fragment.FriendsInviteBoardFragment;
import com.demo.application.home.presentation.view.fragment.FriendsLeaderBoardAddFragment;
import com.demo.application.home.presentation.view.fragment.FriendsLeaderBoardFragment;
import com.demo.application.home.presentation.view.fragment.FriendsLeaderBoardListFragment;
import com.demo.application.home.presentation.view.fragment.GradeChangeFragment;
import com.demo.application.home.presentation.view.fragment.InviteFriendDialog;
import com.demo.application.home.presentation.view.fragment.KYCFragment;
import com.demo.application.home.presentation.view.fragment.KYCViewFragment;
import com.demo.application.home.presentation.view.fragment.PrivacyPolicyFragment;
import com.demo.application.home.presentation.view.fragment.QuizHomeFragment;
import com.demo.application.home.presentation.view.fragment.QuizHomeNewFragment;
import com.demo.application.home.presentation.view.fragment.QuizTestFragment;
import com.demo.application.home.presentation.view.fragment.TransactionsFragment;


import javax.inject.Singleton;

import dagger.Component;


@Component(modules = {AppModule.class, UtilsModule.class, HomeModule.class,})
@Singleton
public interface AppComponent {

    void injectAppContext(DemoApp reciprociApp);

    void doInjection(DashBoardHomeActivity dashBoardHomeActivity);

    void doInjection(CardFragment cardFragment);

    void doInjection(CertificateFragment cardFragment);

    void doInjection(KYCViewFragment cardFragment);

    void doInjection(QuizTestFragment fragment);

    void doInjection(SplashScreenActivity splashScreenActivity);

    void doInjection(IntroSliderActivity introSliderActivity);

    void doInjection(LoginScreenActivity loginScreenActivity);

    void doInjection(DashBoardActivity dashBoardActivity);

    void doInjection(LoginMainScreenActivity loginMainScreenActivity);

    void doInjection(OtpScreenActivity loginScreenActivity);

    void doInjection(VerifyWithPhoneNoActivity verifyWithPhoneNoActivity);

    void doInjection(GradeChangeFragment dialog);

    void doInjection(PrivacyPolicyFragment privacyPolicyFragment);

    void doInjection(FriendsInviteBoardFragment friendsInviteBoardFragment);

    void doInjection(FriendsLeaderBoardFragment friendsLeaderBoardFragment);

    void doInjection(FriendsLeaderBoardAddFragment friendsLeaderBoardAddFragment);

    void doInjection(FriendsLeaderBoardListFragment friendsLeaderBoardListFragment);

    void doInjection(InviteFriendDialog inviteFriendDialog);

    void doInjection(CongratulationsDialog congratulationsDialog);

    void doInjection(TransactionsFragment transactionsFragment);



    void doInjection(ConsgratuationLessScoreDialog dialog);

    void doInjection(QuizHomeNewFragment fragment);

    void doInjection(FriendRequestListDialogFragment fragment);

    void doInjection(KYCFragment fragment);

    void doInjection(HomeActivity homeActivity);


    void doInjection(DemographicFragment fragment);

    void doInjection(QuizHomeFragment fragment);

}
