package com.demo.application.util.permission;


import com.demo.application.core.common.Status;

public class PermissionModel {
    private Status status;
    private boolean  result;

    public PermissionModel(Status status, boolean result){
        this.status = status;
        this.result = result;
    }

    public Status getStatus() {
        return status;
    }


    public boolean isResult() {
        return result;
    }
}
