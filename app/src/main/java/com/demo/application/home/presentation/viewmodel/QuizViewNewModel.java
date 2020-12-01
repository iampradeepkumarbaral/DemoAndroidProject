package com.demo.application.home.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.demo.application.core.common.MessgeNotifyStatus;
import com.demo.application.core.common.ResponseApi;
import com.demo.application.home.domain.usecase.HomeDbUseCase;
import com.demo.application.home.domain.usecase.HomeRemoteUseCase;
import com.demo.application.home.domain.usecase.HomeUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class QuizViewNewModel extends ViewModel {


    CompositeDisposable compositeDisposable;
    public MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();
    private MutableLiveData<MessgeNotifyStatus> notifyLiveData = new MutableLiveData<>();

    public MutableLiveData<String> mobileNumber = new MutableLiveData<>();
    public HomeUseCase homeUseCase;
    private HomeDbUseCase homeDbUseCase;
    private HomeRemoteUseCase homeRemoteUseCase;

    public MutableLiveData<String> walletBalance = new MutableLiveData<>();

    public QuizViewNewModel(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        this.homeUseCase = homeUseCase;
        this.homeDbUseCase = homeDbUseCase;
        this.homeRemoteUseCase = homeRemoteUseCase;
    }

}
