package com.demo.application.core.common;

public interface SdkCallBack {

    public void callBack(String message);

    public void logOut();

    public void commonCallback(Status status, Object o);
}
