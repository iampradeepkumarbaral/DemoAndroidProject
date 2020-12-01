package com.demo.application.home.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.application.core.common.MessgeNotifyStatus;
import com.demo.application.home.domain.usecase.HomeDbUseCase;
import com.demo.application.home.domain.usecase.HomeRemoteUseCase;
import com.demo.application.home.domain.usecase.HomeUseCase;

import io.reactivex.disposables.CompositeDisposable;


public class CardViewModel extends ViewModel {
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<MessgeNotifyStatus> notifyLiveData = new MutableLiveData<>();

    public MutableLiveData<String> mobileNumber = new MutableLiveData<>();

    public CardViewModel(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
    }

   /* public CardViewModel(SignupUseCase signupUseCase, SignUpDbUseCase signUpDbUseCase, SignUpRemoteUseCase signUpRemoteUseCase) {
    }*/

    public MutableLiveData<MessgeNotifyStatus> getNotifyLiveData() {
        return notifyLiveData;
    }


}
