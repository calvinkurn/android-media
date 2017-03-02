package com.tokopedia.session.session.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.session.model.CreatePasswordModel;
import com.tokopedia.session.session.service.RegisterService;
import com.tokopedia.core.util.AppEventTracking;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nisie on 3/28/16.
 */
public class RegisterPassPhoneImpl extends RegisterThird implements DatePickerUtil.onDateSelectedListener {
    public static final String DEMO_EMAIL = "pentolan.jakarta@gmail.com";
    CreatePasswordModel createPassModel;
    RegisterService registerService;
    boolean isValidateEmail;

    public RegisterPassPhoneImpl(RegisterThirdView view) {
        super(view);
        registerService = new RegisterService();
    }

    @Override
    public void setData(HashMap<String, Object> data) {
        if (data.containsKey(FULLNAME)) {
            String name = (String) data.get(FULLNAME);
            createPassModel.setFullName(name);
        } else if (data.containsKey(GENDER)) {
            String gender = (String) data.get(GENDER);
            createPassModel.setGender(gender);
        } else if (data.containsKey(EMAIL)) {
            String email = (String) data.get(EMAIL);
            Log.d(TAG, getMessageTAG() + " email : " + email);
        } else if (data.containsKey(PASSWORD)) {
            String password = (String) data.get(PASSWORD);
            createPassModel.setNewPass(password);
        } else if (data.containsKey(CONFIRM_PASSWORD)) {
            String confirmPassword = (String) data.get(CONFIRM_PASSWORD);
            createPassModel.setConfirmPass(confirmPassword);
        } else if (data.containsKey(IS_CHECKED)) {
            boolean isChecked = (boolean) data.get(IS_CHECKED);
            createPassModel.setRegisterTos(isChecked ? "1" : "0");
        } else if (data.containsKey(PHONENUMBER)) {
            String phoneNumber = (String) data.get(PHONENUMBER);
            createPassModel.setMsisdn(phoneNumber);
        } else if (data.containsKey(DATE_YEAR)) {
            int year = (int) data.get(DATE_YEAR);
            createPassModel.setBdayYear(year);
        } else if (data.containsKey(DATE_MONTH)) {
            int month = (int) data.get(DATE_MONTH);
            createPassModel.setBdayMonth(month);
        } else if (data.containsKey(DATE_DAY)) {
            int day = (int) data.get(DATE_DAY);
            createPassModel.setBdayDay(day);
        } else if (data.containsKey(ALLOWED_FIELDS)){
            List allowedFieldList = (List) data.get(ALLOWED_FIELDS);
            createPassModel.setAllowedFieldList(allowedFieldList);
        }
    }

    @Override
    public void register(Context context, Bundle bundle) {
        if (context != null && context instanceof SessionView) {
            view.showProgress(true);
            boolean isNeedLogin = true;

            Bundle data = new Bundle();
            if (bundle.getInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, 0) != 0){
                data.putInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE,
                        bundle.getInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, 0));
            }
            data.putParcelable(DownloadService.CREATE_PASSWORD_MODEL_KEY, Parcels.wrap(createPassModel));
            data.putBoolean(DownloadService.IS_NEED_LOGIN, isNeedLogin);
            ((SessionView) context).sendDataFromInternet(DownloadService.REGISTER_PASS_PHONE, data);
        }
    }

    @Override
    public Object getData(String type) {
        switch (type) {
            case EMAIL:
                return createPassModel.getEmail();
            case DATE_YEAR:
                return createPassModel.getBdayYear();
            case DATE_MONTH:
                return createPassModel.getBdayMonth();
            case DATE_DAY:
                return createPassModel.getBdayDay();
            default:
                return null;
        }
    }

    @Override
    public String getMessageTAG() {
        return "RegisterPassPhone";
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return className.getSimpleName();
    }

    @Override
    public void initData(@NonNull Context context) {
        view.setAllowedField();
        if (isAfterRotate) {
            view.setData(RegisterNewImpl.convertToMap(ALLOWED_FIELDS,createPassModel.getAllowedFieldList()));
            view.setData(RegisterNewImpl.convertToMap(PASSWORD, createPassModel.getNewPass()));
            view.setData(RegisterNewImpl.convertToMap(CONFIRM_PASSWORD, createPassModel.getConfirmPass()));
            view.setData(RegisterNewImpl.convertToMap(IS_CHECKED, createPassModel.getRegisterTos()));
            view.setData(RegisterNewImpl.convertToMap(PHONENUMBER, createPassModel.getMsisdn()));
        } else {
            calculateDateTime();
        }

        view.setData(RegisterNewImpl.convertToMap(FULLNAME, createPassModel.getFullName()));
        if (createPassModel.getMsisdn() != null)
            view.setData(RegisterNewImpl.convertToMap(PHONENUMBER, createPassModel.getMsisdn()));

        if (createPassModel.getBdayYear() == 0) {
            view.initDatePickerDialog(context, 2002,
                    createPassModel.getBdayMonth(), createPassModel.getBdayDay());
        } else {
            view.initDatePickerDialog(context, createPassModel.getBdayYear(),
                    createPassModel.getBdayMonth(), createPassModel.getBdayDay());
        }
        view.setData(RegisterNewImpl.convertToMap(BIRTHDAY,
                RegisterNewImpl.RegisterUtil.formatDateText(createPassModel.getBdayDay(),
                        createPassModel.getBdayMonth(), createPassModel.getBdayYear())));
    }

    @Override
    public void fetchArguments(Bundle argument) {
        if (argument != null) {
            createPassModel = Parcels.unwrap(argument.getParcelable(DATA));
//            switch (argument.getString(REGISTER_TYPE)){
//                case LoginModel.GoogleType:// email, gender, birthday, fullname
//                case LoginModel.FacebookType:// name, gender birthday, email
            Log.d(TAG, getMessageTAG() + " : " + createPassModel);
//                    break;
//            }
        }
    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {
        createPassModel = Parcels.unwrap(argument.getParcelable(DATA));
        isValidateEmail = argument.getBoolean(VALIDATE_EMAIL);
    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {
        argument.putParcelable(DATA, Parcels.wrap(createPassModel));
        argument.putBoolean(VALIDATE_EMAIL, isValidateEmail);
    }

    @Override
    public void initDataInstance(Context context) {

    }

    @Override
    public void calculateDateTime() {
        Calendar now = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();
        Calendar minDate = Calendar.getInstance();
        maxDate.set(maxDate.get(Calendar.YEAR) - 14, maxDate.getMaximum(Calendar.MONTH), maxDate.getMaximum(Calendar.DATE));
        minDate.set(1933, minDate.getMinimum(Calendar.MONTH), minDate.getMinimum(Calendar.DATE));
        long maxtime = maxDate.getTimeInMillis();
        long mintime = minDate.getTimeInMillis();
        int dateYear = maxDate.get(Calendar.YEAR);
        int dateMonth = minDate.get(Calendar.MONTH) + 1;
        int dateDay = minDate.get(Calendar.DATE);
        Log.d(TAG, getMessageTAG() + dateDay + " - " + dateMonth + " -" + dateYear + " max time : " + maxtime + " minTime : " + mintime);

        //[REVIEW CODE] http://dexter.tkpd:9000/issues/search#issues=AVMciZ5ushXTbmCrwh9m
        if (createPassModel != null) {
            if (createPassModel.getDateText() != null
                    && createPassModel.getDateText().length() > 0) {
                String[] bDay = createPassModel.getDateText().split("/");
                createPassModel.setBdayYear(Integer.parseInt(bDay[2].trim()));
                createPassModel.setBdayMonth(Integer.parseInt(bDay[1].trim()));
                createPassModel.setBdayDay(Integer.parseInt(bDay[0].trim()));
            } else {
                createPassModel.setBdayDay(dateYear);
                createPassModel.setBdayMonth(dateMonth);
                createPassModel.setBdayDay(dateDay);
            }
        }
    }

    @Override
    public void makeLogin(Context context, Bundle data) {
        LocalCacheHandler loginUuid = new LocalCacheHandler(context, LOGIN_UUID_KEY);
        String uuid = loginUuid.getString(UUID_KEY, DEFAULT_UUID_VALUE);
        data.putString(UUID_KEY, uuid);
        ((SessionView) context).sendDataFromInternet(DownloadService.MAKE_LOGIN, data);
    }

    @Override
    public void onDateSelected(int year, int month, int dayOfMonth) {
        createPassModel.setBdayYear(year);
        createPassModel.setBdayMonth(month);
        createPassModel.setBdayDay(dayOfMonth);
        view.setData(RegisterNewImpl.convertToMap(BIRTHDAY,
                RegisterNewImpl.RegisterUtil.formatDateText(
                        createPassModel.getBdayDay(), createPassModel.getBdayMonth(),
                        createPassModel.getBdayYear())));
    }
}
