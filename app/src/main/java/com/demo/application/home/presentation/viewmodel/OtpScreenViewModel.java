package com.demo.application.home.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.common.MessgeNotifyStatus;
import com.demo.application.core.common.ResponseApi;
import com.demo.application.core.common.Status;
import com.demo.application.home.data.model.CheckUserApiReqModel;
import com.demo.application.home.data.model.SendOtpReqModel;
import com.demo.application.home.data.model.VerifyOtpReqModel;
import com.demo.application.home.domain.usecase.HomeDbUseCase;
import com.demo.application.home.domain.usecase.HomeRemoteUseCase;
import com.demo.application.home.domain.usecase.HomeUseCase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OtpScreenViewModel extends ViewModel {


    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public HomeUseCase homeUseCase;
    public HomeDbUseCase homeDbUseCase;
    public HomeRemoteUseCase homeRemoteUseCase;

    private MutableLiveData<MessgeNotifyStatus> notifyLiveData = new MutableLiveData<>();
    public MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();

    public OtpScreenViewModel(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        this.homeUseCase = homeUseCase;
        this.homeDbUseCase = homeDbUseCase;
        this.homeRemoteUseCase = homeRemoteUseCase;
    }

    public MutableLiveData<MessgeNotifyStatus> getNotifyLiveData() {
        return notifyLiveData;
    }

    public void verifyOtpApi(VerifyOtpReqModel reqModel){
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet ->{
            if(hasInternet){
                verifyOtpRxApi(reqModel);
            }else {
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, DemoApp.getAppContext().getString(R.string.internet_check), Status.SEND_OTP));
            }

        });
        getCompositeDisposable().add(disposable);
    }
    public void sendOtpApi(SendOtpReqModel reqModel){
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet ->{
            if(hasInternet){
                sendOtpRxApi(reqModel);
            }else {
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, DemoApp.getAppContext().getString(R.string.internet_check), Status.SEND_OTP));
            }

        });
        getCompositeDisposable().add(disposable);
    }


    private void verifyOtpRxApi(VerifyOtpReqModel reqModel) {
        getCompositeDisposable().add(homeRemoteUseCase.verifyOtpApi(reqModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // serviceLiveData.setValue(ResponseApi.loading(Status.ACCEPT_INVITE_CLICK));
                        serviceLiveData.setValue(ResponseApi.loading(Status.VERIFY_OTP));
                    }
                })
                .subscribe(new Consumer<ResponseApi>() {
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
                        }));
    }

    private void sendOtpRxApi(SendOtpReqModel reqModel) {
        getCompositeDisposable().add(homeRemoteUseCase.sendOtpApi(reqModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // serviceLiveData.setValue(ResponseApi.loading(Status.ACCEPT_INVITE_CLICK));
                        serviceLiveData.setValue(ResponseApi.loading(Status.SEND_OTP));
                    }
                })
                .subscribe(new Consumer<ResponseApi>() {
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
                        }));
    }



    public void checkUserValid(CheckUserApiReqModel reqModel){
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet ->{
            if(hasInternet){
                checkUserVaild(reqModel);
            }else {
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, DemoApp.getAppContext().getString(R.string.internet_check), Status.SEND_OTP));
            }

        });
        getCompositeDisposable().add(disposable);
    }



    private void checkUserVaild(CheckUserApiReqModel reqModel){
        getCompositeDisposable().add(homeRemoteUseCase.checkUserApiVerify(reqModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // serviceLiveData.setValue(ResponseApi.loading(Status.ACCEPT_INVITE_CLICK));
                        serviceLiveData.setValue(ResponseApi.loading(Status.CHECKVALIDUSER));
                    }
                })
                .subscribe(new Consumer<ResponseApi>() {
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
                        }));

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
