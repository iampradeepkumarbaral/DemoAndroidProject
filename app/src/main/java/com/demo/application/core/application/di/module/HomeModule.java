package com.demo.application.core.application.di.module;

import com.demo.application.core.application.di.component.ViewModelFactory;
import com.demo.application.home.data.datasource.database.HomeDbDataSourceImp;
import com.demo.application.home.data.datasource.remote.HomeRemoteApi;
import com.demo.application.home.data.datasource.remote.HomeRemoteDataSourceImp;
import com.demo.application.home.data.repository.HomeRepo;
import com.demo.application.home.domain.usecase.HomeDbUseCase;
import com.demo.application.home.domain.usecase.HomeRemoteUseCase;
import com.demo.application.home.domain.usecase.HomeUseCase;


import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class HomeModule {

    //////////////////////////// DashBoard //////////////////////////////////

    @Provides
    @Singleton
    HomeRemoteApi provideDashBoardApi(Retrofit retrofit) {
        return retrofit.create(HomeRemoteApi.class);
    }

    @Provides
    @Singleton
    HomeRepo.DashboardDbData provideDashboardDbDataSourceImp() {
        return new HomeDbDataSourceImp();
    }


    @Provides
    @Singleton
    HomeRepo.DashboardRemoteData provideDashboardRemoteDataSourceImp(HomeRemoteApi homeRemoteApi) {
        return new HomeRemoteDataSourceImp(homeRemoteApi);
    }

    @Provides
    @Singleton
    HomeUseCase provideDashboardUseCase() {
        return new HomeUseCase();
    }


    @Provides
    @Singleton
    HomeDbUseCase provideHomeDbUseCase() {
        return new HomeDbUseCase();
    }


    @Provides
    @Singleton
    HomeRemoteUseCase provideHomeRemoteUseCase(HomeRepo.DashboardRemoteData dashboardRemoteData) {
        return new HomeRemoteUseCase(dashboardRemoteData);
    }

    @Provides
    @Singleton
    @Named("CardFragment")
    ViewModelFactory provideCardFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("DineDetailFragment")
    ViewModelFactory provideDineDetailViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("DineHomeFragment")
    ViewModelFactory provideDineHomeFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    //NAccount fragment
    @Provides
    @Singleton
    @Named("RelaxHomeFragment")
    ViewModelFactory provideRelaxHomeFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("DashBoardHomeActivity")
    ViewModelFactory provideDashBoardHomeActivityViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("SplashScreenActivity")
    ViewModelFactory provideSplashScreenActivityViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("IntroSliderActivity")
    ViewModelFactory provideIntroSliderActivityViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("LoginScreenActivity")
    ViewModelFactory provideLoginScreenActivityViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("DashBoardActivity")
    ViewModelFactory provideDashBoardActivityViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("LoginMainScreenActivity")
    ViewModelFactory provideLoginMainScreenActivityViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("OtpScreenActivity")
    ViewModelFactory provideOtpScreenActivityViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("VerifyWithPhoneNoActivity")
    ViewModelFactory provideVerifyWithPhoneNoActivityViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("GradeChangeDialog")
    ViewModelFactory provideGradeChangeDialogViewModelFactory(HomeUseCase homeUseCase,HomeDbUseCase homeDbUseCase,HomeRemoteUseCase homeRemoteUseCase){
        return new ViewModelFactory(homeUseCase,homeDbUseCase,homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("QuizHomeFragment")
    ViewModelFactory provideQuizHomeViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("KYCFragment")
    ViewModelFactory provideKYCViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("ScholarShipFragment")
    ViewModelFactory provideScholarShipViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }



    @Provides
    @Singleton
    @Named("HomeActivity")
    ViewModelFactory provideHomeActivityViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("DemographicFragment")
    ViewModelFactory provideDemographicFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("QuizTestFragment")
    ViewModelFactory provideQuizTestFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("KYCViewFragment")
    ViewModelFactory provideKYCViewFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("FriendsLeaderBoardFragment")
    ViewModelFactory provideFriendsLeaderBoardFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("FriendsLeaderBoardListFragment")
    ViewModelFactory provideFriendsLeaderBoardListFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("FriendsLeaderBoardAddFragment")
    ViewModelFactory provideFriendsLeaderBoardAddFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("FriendRequestListDialogFragment")
    ViewModelFactory provideFriendRequestListDialogFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("FriendsInviteBoardFragment")
    ViewModelFactory provideFriendsInviteViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("InviteFriendDialog")
    ViewModelFactory provideInviteFriendDialogViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("CongratulationsDialog")
    ViewModelFactory provideCongratulationsDialogViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("TransactionsFragment")
    ViewModelFactory provideTransactionsFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }

    @Provides
    @Singleton
    @Named("QuizHomeNewFragment")
    ViewModelFactory provideQuizViewNewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }


    @Provides
    @Singleton
    @Named("CertificateFragment")
    ViewModelFactory provideCertificateFragmentViewModelFactory(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        return new ViewModelFactory(homeUseCase, homeDbUseCase, homeRemoteUseCase);
    }
}
