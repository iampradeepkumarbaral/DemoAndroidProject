package com.demo.application.util;

import com.demo.application.home.data.model.KYCDocumentDatamodel;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public enum ConversionUtil {
    INSTANCE;

    public int convertStringToInteger(String value) {
        int val = 0;
        try {
            if (value != null && !value.isEmpty()) {
                val = Integer.parseInt(value);
                return val;
            } else {
                return val;
            }
        } catch (Exception e) {
            return val;
        }
    }

    public double convertStringToDouble(String value) {
        double val = 0;
        try {
            if (value != null && !value.isEmpty()) {
                val = Double.parseDouble(value);
                return val;
            } else {
                return val;
            }
        } catch (Exception e) {
            return val;
        }
    }


    public MultipartBody.Part makeMultipartRequest(KYCDocumentDatamodel kycDocumentDatamodel) {
        if (kycDocumentDatamodel.getImageBytes() != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), kycDocumentDatamodel.getImageBytes());
            return MultipartBody.Part.createFormData(kycDocumentDatamodel.getId_name(), "image.jpg", requestFile);
        } else {
            return null;
        }
    }


}
