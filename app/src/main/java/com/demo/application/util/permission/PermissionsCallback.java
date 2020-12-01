package com.demo.application.util.permission;

@SuppressWarnings("WeakerAccess")
public abstract class PermissionsCallback {
    public void onGranted(boolean newPermissionsGranted) {
    }

    public void onDenied() {
    }
}

