package com.demo.application.core.network;

import com.demo.application.BuildConfig;


public interface URLConstant {


    // String BASE_URL = BuildConfig.BASE_URL;

    String BASE_URL = BuildConfig.BASE_URL;
    //String VERSION_URL = "http://demo.auroscholar.com/api/";

    //http://demo.auroscholar.com/api/usercheck.php


    String OTP_SEND_API = BASE_URL + "otp_send.php";

    String OTP_VERIFY = BASE_URL + "otp_verify.php";

    String VERSION_API = BASE_URL + "check_api_version.php";

    String USER_CHECK = BASE_URL + "usercheck.php";

    String GRADE_UPGRADE = BASE_URL + "grade_update.php";


    String DASHBOARD_API = BASE_URL + "dashboard_partner.php";
    String DASHBOARD_SDK_API = BASE_URL + "dashboard_sdk_partner.php";

    String DEMOGRAPHIC_API = BASE_URL + "demographics.php";

    // String AZURE_API = BASE_URL + "faceimg.php";

    String AZURE_API = BASE_URL + "faceimg_multiple.php";

    String UPLOAD_IMAGE_URL = BASE_URL + "kyc.php";
    // String GET_ASSIGNMENT_ID = BASE_URL + "statrquizeaction.php ";
    String GET_ASSIGNMENT_ID = BASE_URL + "statrquizeaction_multiple.php";
    String TEST_URL = "https://assessment.eklavvya.com/exam/StartExam?";
    String PRIVACY_POLICY = "https://auroscholar.com/privacy_policy.php";

    String TEACHER_PROFILE_UPDATE_API = BASE_URL + "teacher_profile_data_api.php ";

    String GET_PROFILE_UPDATE_API = BASE_URL + "students_score_data_api.php ";

    String GET_PROFILE_TEACHER_API = BASE_URL + "teacher_profile_display_api.php ";

    String GET_TEACHER_PROGRESS_API = BASE_URL + "teacher_progress.php ";

    String TEACHER_KYC_API = BASE_URL + "teacher_kyc_api.php ";

    String INVITE_FRIEND_LIST_API = BASE_URL + "student_referral_data.php";

    String SEND_NOTIFICATION_API = BASE_URL + "push_notification.php";

    String GET_ZOHO_APPOINTMENT = BASE_URL + "get_zoho_slot.php";

    String BOOK_ZOHO_APPOINTMENT = BASE_URL + "book_zoho_slot.php";

    String ACCEPT_STUDENT_INVITE = BASE_URL + "student_challenge_accepted.php";

    String PAYTM_API = BASE_URL + "paytm_wallet_transfer.php";

    String PAYTM_UPI_TRANSFER = BASE_URL + "paytm_upi_transfer.php";

    String PAYTM_ACCOUNT_TRANSFRER = BASE_URL + "paytm_account_transfer.php";

    String FIND_FRIEND_API = BASE_URL + "find_friend.php";

    String SEND_FRIEND_REQUEST_API = BASE_URL + "friend_request.php";

    String FRIEND_REQUEST_LIST_API = BASE_URL + "friend_request_list.php";

    String FRIEND_ACCEPT_API = BASE_URL + "friend_accepted.php";

    String TERM_CONDITION = "https://auroscholar.com/terms_of_use.php";

    String REFFERAL_API  = BASE_URL + "reffer.php";

    String SEND_REFERRAL_API  = BASE_URL + "student_referral.php";

    String CERTIFICATE_API  = BASE_URL + "certificate_data_api.php";
}
