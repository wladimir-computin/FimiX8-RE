package com.fimi.network;

import android.content.Context;
import com.fimi.kernel.R;

public class ErrorMessage {
    public static final String AIRCRAFT_ID_CANNOT_BE_EMPTY = "92001";
    public static final String CLIENT_DATA_ILLEGAL_DATA = "90002";
    public static final String EMAIL_INPUT_TOO_LONG = "91012";
    public static final String EMAIL_NOT_EMPTY = "91009";
    public static final String EMAIL_NOT_RIGHT_FORMAT = "91003";
    public static final String MD5_SIGNATURE_VERIFICATION_CANNOT_PASS = "90003";
    public static final String MOBILE_PHONES_CANNOT_BE_EMPTY = "91001";
    public static final String MOBILE_PHONE_FORMAT_IS_INCORRECT = "91019";
    public static final String NOT_ENOUGH_PASSWORD_LENGTH = "91020";
    public static final String NO_WAY_TO_KNOW_HOW_TO_LOG_IN = "91018";
    public static final String PASSWORD_INCONSISTENT = "91011";
    public static final String PASSWORD_NOT_EMPTY = "91010";
    public static final String PHONE_NUMBER_NOT_RIGHT_FORMAT = "91002";
    public static final String SYSTEM_ERROR_DATABASE_EXCEPTION = "90001";
    public static final String SYSTEM_NORMAL_RESPONSE = "90000";
    public static final String THE_USER_PASSWORD_INCORRECT = "91008";
    public static final String THE_VERIFICATION_CODE_EXPIRED = "91006";
    public static final String THE_VERIFICATION_CODE_MADE = "91007";
    public static final String TWO_CIPHER_INCONSISTENCIES = "91021";
    public static final String TWO_USER_RIGHT_APPLY = "91022";
    public static final String USER_ALREADY_EXIST = "91013";
    public static final String USER_EXIST = "91004";
    public static final String VERIFICATION_CODE_CANNOT_EMPTY = "91014";
    public static final String VERIFICATION_CODE_ERROR = "91005";
    public static final String VERIFICATION_CODE_GET_FREQUENTLY = "91015";
    public static final String VERIFICATION_CODE_LOGIN_OUTTIME = "91016";

    public static String getSystemErrorMessage(Context context, String code) {
        if ("90000".equals(code)) {
            return context.getResources().getString(R.string.error_system_system_normal_response);
        }
        if (SYSTEM_ERROR_DATABASE_EXCEPTION.equals(code)) {
            return context.getResources().getString(R.string.error_system_client_data_illegal_data);
        }
        if (CLIENT_DATA_ILLEGAL_DATA.equals(code)) {
            return context.getResources().getString(R.string.error_system_client_data_illegal_data);
        }
        if (MD5_SIGNATURE_VERIFICATION_CANNOT_PASS.equals(code)) {
            return context.getResources().getString(R.string.error_system_md5_signature_verification_cannot_pass);
        }
        return null;
    }

    public static String getUserModeErrorMessage(Context context, String code) {
        String systrmError = getSystemErrorMessage(context, code);
        if (systrmError != null) {
            return systrmError;
        }
        if (MOBILE_PHONES_CANNOT_BE_EMPTY.equals(code)) {
            return context.getResources().getString(R.string.error_user_mobile_phones_cannot_be_empty);
        }
        if (PHONE_NUMBER_NOT_RIGHT_FORMAT.equals(code)) {
            return context.getResources().getString(R.string.error_user_phone_number_not_right_format);
        }
        if (EMAIL_NOT_RIGHT_FORMAT.equals(code)) {
            return context.getResources().getString(R.string.error_user_email_not_right_format);
        }
        if (USER_EXIST.equals(code)) {
            return context.getResources().getString(R.string.error_user_user_exist);
        }
        if (VERIFICATION_CODE_ERROR.equals(code)) {
            return context.getResources().getString(R.string.error_user_verification_code_error);
        }
        if (THE_VERIFICATION_CODE_EXPIRED.equals(code)) {
            return context.getResources().getString(R.string.error_user_the_verification_code_expired);
        }
        if (THE_VERIFICATION_CODE_MADE.equals(code)) {
            return context.getResources().getString(R.string.error_user_the_verification_code_made);
        }
        if (THE_USER_PASSWORD_INCORRECT.equals(code)) {
            return context.getResources().getString(R.string.error_user_the_user_password_incorrect);
        }
        if (EMAIL_NOT_EMPTY.equals(code)) {
            return context.getResources().getString(R.string.error_user_email_not_empty);
        }
        if (PASSWORD_NOT_EMPTY.equals(code)) {
            return context.getResources().getString(R.string.error_user_password_not_empty);
        }
        if (PASSWORD_INCONSISTENT.equals(code)) {
            return context.getResources().getString(R.string.error_user_password_inconsistent);
        }
        if (EMAIL_INPUT_TOO_LONG.equals(code)) {
            return context.getResources().getString(R.string.error_user_email_input_too_long);
        }
        if (USER_ALREADY_EXIST.equals(code)) {
            return context.getResources().getString(R.string.error_user_user_already_exist);
        }
        if (VERIFICATION_CODE_CANNOT_EMPTY.equals(code)) {
            return context.getResources().getString(R.string.error_user_verification_code_cannot_empty);
        }
        if (VERIFICATION_CODE_GET_FREQUENTLY.equals(code)) {
            return context.getResources().getString(R.string.error_user_verification_code_get_frequently);
        }
        if (VERIFICATION_CODE_LOGIN_OUTTIME.equals(code)) {
            return context.getResources().getString(R.string.error_user_verification_code_login_outtime);
        }
        if (NO_WAY_TO_KNOW_HOW_TO_LOG_IN.equals(code)) {
            return context.getResources().getString(R.string.error_user_no_way_to_know_how_to_log_in);
        }
        if (MOBILE_PHONE_FORMAT_IS_INCORRECT.equals(code)) {
            return context.getResources().getString(R.string.error_user_mobile_phone_format_is_incorrect);
        }
        if (NOT_ENOUGH_PASSWORD_LENGTH.equals(code)) {
            return context.getResources().getString(R.string.error_user_not_enough_password_length);
        }
        if (TWO_CIPHER_INCONSISTENCIES.equals(code)) {
            return context.getResources().getString(R.string.error_user_two_cipher_inconsistencies);
        }
        if (TWO_USER_RIGHT_APPLY.equals(code)) {
            return context.getResources().getString(R.string.libperson_apply_repetition);
        }
        return null;
    }

    public static String getFimwareUpdateErrorMessage(Context context, String code) {
        String systrmError = getSystemErrorMessage(context, code);
        if (systrmError != null) {
            return systrmError;
        }
        if (AIRCRAFT_ID_CANNOT_BE_EMPTY.equals(code)) {
            return context.getResources().getString(R.string.error_fimware_update_aircraft_id_cannot_empty);
        }
        return null;
    }
}
