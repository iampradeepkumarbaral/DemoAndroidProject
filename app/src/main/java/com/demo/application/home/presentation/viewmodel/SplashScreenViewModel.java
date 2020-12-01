package com.demo.application.home.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.common.MessgeNotifyStatus;
import com.demo.application.core.common.ResponseApi;
import com.demo.application.core.common.Status;
import com.demo.application.home.domain.usecase.HomeDbUseCase;
import com.demo.application.home.domain.usecase.HomeRemoteUseCase;
import com.demo.application.home.domain.usecase.HomeUseCase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.demo.application.core.common.Status.VERSIONAPI;

public class SplashScreenViewModel extends ViewModel {

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    HomeUseCase homeUseCase;
    HomeDbUseCase homeDbUseCase;
    HomeRemoteUseCase homeRemoteUseCase;

    private MutableLiveData<MessgeNotifyStatus> notifyLiveData = new MutableLiveData<>();
    public MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();

    public SplashScreenViewModel(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        this.homeUseCase = homeUseCase;
        this.homeDbUseCase = homeDbUseCase;
        this.homeRemoteUseCase = homeRemoteUseCase;
    }

    public MutableLiveData<MessgeNotifyStatus> getNotifyLiveData() {
        return notifyLiveData;
    }

    public void getVersionApi(){
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet ->{
            if(hasInternet){
                getVersionApiCall();
            }else {
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, DemoApp.getAppContext().getString(R.string.internet_check), Status.SEND_OTP));
            }

        });
        getCompositeDisposable().add(disposable);
    }


    private void  getVersionApiCall(){
        getCompositeDisposable()
                .add(homeRemoteUseCase.getVersionApiCheck()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                serviceLiveData.setValue(ResponseApi.loading(VERSIONAPI));
                            }
                        })
                        .subscribe(
                                new Consumer<ResponseApi>() {
                                    @Override
                                    public void accept(ResponseApi responseApi) throws Exception {
                                        serviceLiveData.setValue(responseApi);
                                    }
                                },

                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        defaultError();
                                    }
                                }
                        ));
    }

    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }

    private void defaultError() {
        serviceLiveData.setValue(new ResponseApi(Status.FAIL, DemoApp.getAppContext().getResources().getString(R.string.default_error), null));
    }

    public LiveData<ResponseApi> serviceLiveData() {

        return serviceLiveData;

    }

}
