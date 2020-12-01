package com.demo.application.core.application.base_component;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;



public abstract class BaseDialog  extends DialogFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected abstract void init();
    protected abstract void setToolbar();

    protected abstract void setListener();

    protected abstract int getLayout();
}
