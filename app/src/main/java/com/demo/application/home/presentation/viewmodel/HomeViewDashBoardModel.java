package com.demo.application.home.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.application.core.common.MessgeNotifyStatus;
import com.demo.application.home.domain.usecase.HomeDbUseCase;
import com.demo.application.home.domain.usecase.HomeRemoteUseCase;
import com.demo.application.home.domain.usecase.HomeUseCase;

import io.reactivex.disposables.CompositeDisposable;


public class HomeViewDashBoardModel extends ViewModel {
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    HomeUseCase homeUseCase;
    HomeDbUseCase homeDbUseCase;
    HomeRemoteUseCase homeRemoteUseCase;

    private MutableLiveData<MessgeNotifyStatus> notifyLiveData = new MutableLiveData<>();

    public HomeViewDashBoardModel(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        this.homeUseCase = homeUseCase;
        this.homeDbUseCase = homeDbUseCase;
        this.homeRemoteUseCase = homeRemoteUseCase;
    }

    public MutableLiveData<MessgeNotifyStatus> getNotifyLiveData() {
        return notifyLiveData;
    }
}