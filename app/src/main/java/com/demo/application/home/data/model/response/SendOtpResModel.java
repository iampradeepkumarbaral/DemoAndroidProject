package com.demo.application.home.data.model.response;

public class SendOtpResModel{
	private String mobileNo;
	private int otp;
	private boolean error;
	private String message;
	private String status;
	private String smsText;

	public void setMobileNo(String mobileNo){
		this.mobileNo = mobileNo;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public void setOtp(int otp){
		this.otp = otp;
	}

	public int getOtp(){
		return otp;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setSmsText(String smsText){
		this.smsText = smsText;
	}

	public String getSmsText(){
		return smsText;
	}

	@Override
 	public String toString(){
		return 
			"SendOtpResModel{" + 
			"mobile_no = '" + mobileNo + '\'' + 
			",otp = '" + otp + '\'' + 
			",error = '" + error + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			",sms_text = '" + smsText + '\'' + 
			"}";
		}
}
