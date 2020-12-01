package com.demo.application.home.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.common.ResponseApi;
import com.demo.application.core.common.Status;
import com.demo.application.home.data.model.response.DynamiclinkResModel;
import com.demo.application.home.domain.usecase.HomeDbUseCase;
import com.demo.application.home.domain.usecase.HomeRemoteUseCase;
import com.demo.application.home.domain.usecase.HomeUseCase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class HomeViewModel extends ViewModel {

    HomeUseCase homeUseCase;
    HomeDbUseCase homeDbUseCase;
    HomeRemoteUseCase homeRemoteUseCase;
    CompositeDisposable compositeDisposable;
    public MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();

    public HomeViewModel(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        this.homeUseCase = homeUseCase;
        this.homeDbUseCase = homeDbUseCase;
        this.homeRemoteUseCase = homeRemoteUseCase;
    }

    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }

    public LiveData<ResponseApi> serviceLiveData() {

        return serviceLiveData;

    }
    public void getDynamicData(DynamiclinkResModel reqmodel) {
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                getDynamicDataApi(reqmodel);
            } else {
                // please check your internet
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, DemoApp.getAppContext().getResources().getString(R.string.internet_check), Status.NO_INTERNET));
            }

        });
        getCompositeDisposable().add(disposable);

    }

    private void defaultError() {
        serviceLiveData.setValue(new ResponseApi(Status.FAIL, DemoApp.getAppContext().getResources().getString(R.string.default_error), null));
    }

    private void getDynamicDataApi(DynamiclinkResModel model) {
        getCompositeDisposable()
                .add(homeRemoteUseCase.getDynamicDataApi(model)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                serviceLiveData.setValue(ResponseApi.loading(null));
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






}