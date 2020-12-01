package com.demo.application.core.network;

import com.google.gson.JsonObject;
import com.demo.application.core.common.ResponseApi;
import com.demo.application.core.common.Status;

import io.reactivex.Single;
import retrofit2.Response;

public abstract class NetworkUseCase {

    public abstract Single<Boolean> isAvailInternet();

    public abstract ResponseApi response200(Response<JsonObject> response, Status status);
    public abstract ResponseApi response401(Status status);
    public abstract ResponseApi responseFail400(Response<JsonObject> response,Status status);
    public abstract ResponseApi responseFail(Status status);


}
