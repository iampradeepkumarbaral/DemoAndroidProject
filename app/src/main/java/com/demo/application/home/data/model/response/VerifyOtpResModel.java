package com.demo.application.home.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOtpResModel implements Parcelable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("otp")
    @Expose
    private String otp;

    protected VerifyOtpResModel(Parcel in) {
        status = in.readString();
        byte tmpError = in.readByte();
        error = tmpError == 0 ? null : tmpError == 1;
        message = in.readString();
        mobileNo = in.readString();
        otp = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeByte((byte) (error == null ? 0 : error ? 1 : 2));
        dest.writeString(message);
        dest.writeString(mobileNo);
        dest.writeString(otp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VerifyOtpResModel> CREATOR = new Creator<VerifyOtpResModel>() {
        @Override
        public VerifyOtpResModel createFromParcel(Parcel in) {
            return new VerifyOtpResModel(in);
        }

        @Override
        public VerifyOtpResModel[] newArray(int size) {
            return new VerifyOtpResModel[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }


}
