package com.demo.application.home.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.demo.application.R;
import com.demo.application.core.application.DemoApp;
import com.demo.application.core.common.ResponseApi;
import com.demo.application.core.common.Status;
import com.demo.application.home.data.model.AssignmentReqModel;
import com.demo.application.home.data.model.AuroScholarDataModel;
import com.demo.application.home.domain.usecase.HomeDbUseCase;
import com.demo.application.home.domain.usecase.HomeRemoteUseCase;
import com.demo.application.home.domain.usecase.HomeUseCase;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.demo.application.core.common.Status.DASHBOARD_API;
import static com.demo.application.core.common.Status.SEND_INVITE_API;


public class FriendsLeaderShipViewModel extends ViewModel {
    CompositeDisposable compositeDisposable;
    public MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();


    public MutableLiveData<String> mobileNumber = new MutableLiveData<>();
    public HomeUseCase homeUseCase;
    public HomeDbUseCase homeDbUseCase;
    public HomeRemoteUseCase homeRemoteUseCase;

    public FriendsLeaderShipViewModel(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        this.homeUseCase = homeUseCase;
        this.homeDbUseCase = homeDbUseCase;
        this.homeRemoteUseCase = homeRemoteUseCase;
    }

    public void getDashBoardData(AuroScholarDataModel model) {

        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                dashBoardApi(model);
            } else {
                // please check your internet
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, DemoApp.getAppContext().getString(R.string.internet_check), Status.NO_INTERNET));
            }
        });

        getCompositeDisposable().add(disposable);

    }

    private void dashBoardApi(AuroScholarDataModel model) {
        getCompositeDisposable()
                .add(homeRemoteUseCase.getDashboardData(model)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable __) throws Exception {
                                /*Do code here*/
                                serviceLiveData.setValue(ResponseApi.loading(DASHBOARD_API));
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
                                        defaultError();                                    }
                                }));

    }





    public void getFriendsListData() {
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
             //   inviteFriendListApi();
            } else {
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, DemoApp.getAppContext().getString(R.string.internet_check), Status.INVITE_FRIENDS_LIST));
            }
        });
        getCompositeDisposable().add(disposable);
    }

    public void findFriendData(double lat, double longt, double radius) {
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                findFriendApi(lat,longt,radius);
            } else {
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, DemoApp.getAppContext().getString(R.string.internet_check), Status.FIND_FRIEND_DATA));
            }
        });
        getCompositeDisposable().add(disposable);
    }

    public void sendFriendRequestData(int requested_by_id, int requested_user_id, String requested_by_phone, String requested_user_phone) {
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                sendFriendRequestApi(requested_by_id,requested_user_id,requested_by_phone,requested_user_phone);
            } else {
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, DemoApp.getAppContext().getString(R.string.internet_check), Status.SEND_FRIENDS_REQUEST));
            }
        });
        getCompositeDisposable().add(disposable);
    }

    public void friendRequestListData(int requested_user_id) {
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                friendRequestListApi(requested_user_id);
            } else {
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, DemoApp.getAppContext().getString(R.string.internet_check), Status.FIND_FRIEND_DATA));
            }
        });
        getCompositeDisposable().add(disposable);
    }

    public void friendAcceptData(int friend_request_id, String request_status) {
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if (hasInternet) {
                friendAcceptApi(friend_request_id,request_status);
            } else {
                serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET, DemoApp.getAppContext().getString(R.string.internet_check), Status.ACCEPT_INVITE_REQUEST));
            }
        });
        getCompositeDisposable().add(disposable);
    }



    private void findFriendApi(double lat, double longt, double radius) {
        getCompositeDisposable().add(homeRemoteUseCase.findFriendApi(lat,longt,radius).subscribeOn(Schedulers.io())
                                             .observeOn(AndroidSchedulers.mainThread())
                                             .doOnSubscribe(new Consumer<Disposable>() {
                                                 @Override
                                                 public void accept(Disposable disposable) throws Exception {
                                                     serviceLiveData.setValue(ResponseApi.loading(Status.FIND_FRIEND_DATA));
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

    private void sendFriendRequestApi(int requested_by_id, int requested_user_id, String requested_by_phone, String requested_user_phone) {
        getCompositeDisposable().add(homeRemoteUseCase.sendFriendRequestApi(  requested_by_id,  requested_user_id,requested_by_phone,requested_user_phone).subscribeOn(Schedulers.io())
                                             .observeOn(AndroidSchedulers.mainThread())
                                             .doOnSubscribe(new Consumer<Disposable>() {
                                                 @Override
                                                 public void accept(Disposable disposable) throws Exception {
                                                     serviceLiveData.setValue(ResponseApi.loading(Status.SEND_FRIENDS_REQUEST));
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

    private void friendRequestListApi(int requested_user_id) {
        getCompositeDisposable().add(homeRemoteUseCase.friendRequestListApi(requested_user_id).subscribeOn(Schedulers.io())
                                             .observeOn(AndroidSchedulers.mainThread())
                                             .doOnSubscribe(new Consumer<Disposable>() {
                                                 @Override
                                                 public void accept(Disposable disposable) throws Exception {
                                                     serviceLiveData.setValue(ResponseApi.loading(Status.FRIENDS_REQUEST_LIST));
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

    private void friendAcceptApi(int friend_request_id, String request_status){
        getCompositeDisposable().add(homeRemoteUseCase.friendAcceptApi(friend_request_id,request_status).subscribeOn(Schedulers.io())
                                             .observeOn(AndroidSchedulers.mainThread())
                                             .doOnSubscribe(new Consumer<Disposable>() {
                                                 @Override
                                                 public void accept(Disposable disposable) throws Exception {
                                                     serviceLiveData.setValue(ResponseApi.loading(Status.ACCEPT_INVITE_REQUEST));
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




    public void getAzureRequestData(AssignmentReqModel model){
        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet ->{
            if(hasInternet){
                azureRequestApi(model);
            }else{
                // serviceLiveData.setValue(new ResponseApi(Status.NO_INTERNET,DemoApp.getAppContext().getString(R.string.internet_check),Status.NO_INTERNET));
            }
        });
        getCompositeDisposable().add(disposable);
    }
    private  void azureRequestApi(AssignmentReqModel model){
        getCompositeDisposable().add(homeRemoteUseCase.getAzureData(model).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

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

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
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

    private void defaultError() {
        serviceLiveData.setValue(new ResponseApi(Status.FAIL, DemoApp.getAppContext().getResources().getString(R.string.default_error), null));
    }

}
