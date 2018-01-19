package com.tokopedia.session.session.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.core.session.model.CreatePasswordModel;
import com.tokopedia.core.session.model.InfoModel;
import com.tokopedia.core.session.model.LoginFacebookViewModel;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.session.model.RegisterViewModel;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.session.session.interactor.RegisterInteractor;
import com.tokopedia.session.session.interactor.RegisterInteractorImpl;
import com.tokopedia.session.session.service.RegisterService;

import org.parceler.Parcels;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by m.normansyah on 1/25/16.
 */
public class RegisterNewImpl extends RegisterNew implements TextWatcher{
    RegisterService registerService;
    RegisterViewModel registerViewModel;
    LocalCacheHandler loginUuid;
    LocalCacheHandler providerListCache;
    RegisterInteractor facade;
    boolean isSelect;
    boolean isEmailEdit;
    boolean isSaving;
    boolean isChecked;


    public RegisterNewImpl(RegisterNewView view){
        super(view);
        registerService = new RegisterService();
        facade = RegisterInteractorImpl.createInstance();
    }

    public static class RegisterUtil{
        public static boolean checkRegexNameLocal(String param){
            String regex = "[A-Za-z]+";
            return !param.replaceAll("\\s","").matches(regex);
        }
        public static boolean isExceedMaxCharacter(String text) {
            return text.length()>35;
        }

        public static String formatDateText(int mDateDay, int mDateMonth, int mDateYear) {
            return String.format("%d / %d / %d", mDateDay, mDateMonth, mDateYear);
        }

        public static String formatDateTextString(int mDateDay, int mDateMonth, int mDateYear) {
            String bulan;
            switch(mDateMonth) {
                case 1: bulan = "Januari"; break;
                case 2: bulan = "Februari"; break;
                case 3: bulan = "Maret"; break;
                case 4: bulan = "April"; break;
                case 5: bulan = "May"; break;
                case 6: bulan = "Juni"; break;
                case 7: bulan = "Juli"; break;
                case 8: bulan = "Agustus"; break;
                case 9: bulan = "September"; break;
                case 10: bulan = "Oktober"; break;
                case 11: bulan = "November"; break;
                case 12: bulan = "Desember"; break;
                default: bulan = "inputan salah"; break;
            }
            return String.format("%d %s %d", mDateDay, bulan, mDateYear);
        }
    }

    @Override
    public void validateEmail(final Context context, final String name, final String email, final String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email",email);
        facade.validateEmail(context, params , new RegisterInteractor.ValidateEmailListener() {
            @Override
            public void onSuccess(boolean isActive) {
                if(isActive){
                    view.showErrorValidateEmail();
                }else{
                    view.enableDisableAllFieldsForEmailValidationForm(true);
                    view.moveToRegisterNext(name, email,password);
                    sendGTMClick();
                }
            }

            @Override
            public void onError(String s) {
                view.showErrorValidateEmail(s);
            }

            @Override
            public void onTimeout() {
                view.showErrorValidateEmail(context.getString(R.string.default_request_error_timeout));
            }

            @Override
            public void onThrowable(Throwable e) {
                if(e instanceof UnknownHostException){
                    view.showErrorValidateEmail(context.getString(R.string.default_request_error_unknown));
                }else if(e instanceof SocketTimeoutException) {
                    view.showErrorValidateEmail(context.getString(R.string.default_request_error_timeout));
                }else{
                    view.showErrorValidateEmail(context.getString(R.string.msg_network_error));
                }
            }
        });
    }

    @Override
    public void saveData(HashMap<String, Object> data) {
        if(data.containsKey(EMAIL)){
            String email = (String)data.get(EMAIL);
            registerViewModel.setmEmail(email);
        }else if(data.containsKey(PASSWORD)){
            String password = (String)data.get(PASSWORD);
            registerViewModel.setmPassword(password);
        }else if(data.containsKey(IS_SELECT_FROM_AUTO_TEXT_VIEW)){
            isSelect = (boolean)data.get(IS_SELECT_FROM_AUTO_TEXT_VIEW);
        }else if(data.containsKey(IS_EMAIL_EDITTED_FOR_THE_FIRSTTIME)){
            isEmailEdit = (boolean)data.get(IS_EMAIL_EDITTED_FOR_THE_FIRSTTIME);
        }else if(data.containsKey(IS_SAVING)){
            isSaving = (boolean)data.get(IS_SAVING);
        }else if(data.containsKey(IS_CHECKED)){
            isChecked = (boolean)data.get(IS_CHECKED);
        }
    }


    @Override
    public void setData(Context context, int type, Bundle data) {
        switch (type){
            case DownloadService.LOGIN_ACCOUNTS_INFO:
                data.putString(UUID_KEY, getUUID());
                InfoModel infoModel = data.getParcelable(DownloadServiceConstant.INFO_BUNDLE);
                Parcelable parcelable = data.getParcelable(DownloadServiceConstant.EXTRA_TYPE);

                if (infoModel.isCreatedPassword()) {
                    ((SessionView) context).sendDataFromInternet(DownloadService.MAKE_LOGIN, data);
                } else {
                    CreatePasswordModel createPasswordModel = new CreatePasswordModel();
                    createPasswordModel = setModelFromParcelable(createPasswordModel,parcelable,infoModel);
                    data.putBoolean(DownloadServiceConstant.LOGIN_MOVE_REGISTER_THIRD, true);
                    data.putParcelable(DownloadServiceConstant.LOGIN_GOOGLE_MODEL_KEY, Parcels.wrap(createPasswordModel));
                    ((SessionView) context).moveToRegisterPassPhone(createPasswordModel, infoModel.getCreatePasswordList(),data);
                }
            break;

            case DownloadServiceConstant.MAKE_LOGIN:
                view.showProgress(false);
                view.finishActivity();
                break;

        }
    }

    private CreatePasswordModel setModelFromParcelable(CreatePasswordModel createPasswordModel, Parcelable parcelable, InfoModel infoModel) {
        if (Parcels.unwrap(parcelable) instanceof LoginGoogleModel) {
            LoginGoogleModel loginGoogleModel = Parcels.unwrap(parcelable);
            if (loginGoogleModel.getFullName() != null) {
                createPasswordModel.setFullName(loginGoogleModel.getFullName());
            }
            if (loginGoogleModel.getGender().contains("male")) {
                createPasswordModel.setGender(RegisterViewModel.GENDER_MALE + "");
            } else {
                createPasswordModel.setGender(RegisterViewModel.GENDER_FEMALE + "");
            }
            if (loginGoogleModel.getBirthday() != null) {
                createPasswordModel.setDateText(loginGoogleModel.getBirthday());
            }
            if (loginGoogleModel.getEmail() != null) {
                createPasswordModel.setEmail(loginGoogleModel.getEmail());
            }
        }else if(Parcels.unwrap(parcelable) instanceof LoginFacebookViewModel){
            LoginFacebookViewModel loginFacebookViewModel = Parcels.unwrap(parcelable);
            if (loginFacebookViewModel.getFullName() != null) {
                createPasswordModel.setFullName(loginFacebookViewModel.getFullName());
            }
            if (loginFacebookViewModel.getGender().contains("male")) {
                createPasswordModel.setGender(RegisterViewModel.GENDER_MALE + "");
            } else {
                createPasswordModel.setGender(RegisterViewModel.GENDER_FEMALE + "");
            }
            if (loginFacebookViewModel.getBirthday() != null) {
                createPasswordModel.setDateText(loginFacebookViewModel.getBirthday());
            }
            if (loginFacebookViewModel.getEmail() != null) {
                createPasswordModel.setEmail(loginFacebookViewModel.getEmail());
            }
        }else{
            createPasswordModel.setFullName(infoModel.getName());
            createPasswordModel.setEmail(infoModel.getEmail());
        }
        return createPasswordModel;
    }

    @Override
    public void initData(@NonNull Context context) {
        if(isAfterRotate){
            view.setData(convertToMap(EMAIL, registerViewModel.getmEmail()));// 1. email
            view.setData(convertToMap(PASSWORD, registerViewModel.getmPassword()));// 2. password
            view.setData(convertToMap(IS_CHECKED, isChecked));
        }
        loginUuid = new LocalCacheHandler(context, LOGIN_UUID_KEY);

    }


    @Override
    public void sendGTMRegisterError(String label) {
        UnifyTracking.eventRegisterError(label);
    }

    @Override
    public void unSubscribeFacade() {
        facade.unSubscribe();
    }

    public static HashMap<String, Object> convertToMap(String key, Object value){
        HashMap<String, Object> temp = new HashMap<>();
        temp.put(key, value);
        return temp;
    }

    @Override
    public void fetchArguments(Bundle argument) {

    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {
        registerViewModel = Parcels.unwrap(argument.getParcelable(DATA));
        isSelect = argument.getBoolean(IS_SELECT_FROM_AUTO_TEXT_VIEW);
        isEmailEdit = argument.getBoolean(IS_EMAIL_EDITTED_FOR_THE_FIRSTTIME);
        isSaving = argument.getBoolean(IS_SAVING);
        isChecked = argument.getBoolean(IS_CHECKED);
    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {
        argument.putParcelable(DATA, Parcels.wrap(registerViewModel));
        argument.putBoolean(IS_SELECT_FROM_AUTO_TEXT_VIEW, isSelect);
        argument.putBoolean(IS_EMAIL_EDITTED_FOR_THE_FIRSTTIME, isEmailEdit);
        argument.putBoolean(IS_SAVING, isSaving);
        argument.putBoolean(IS_CHECKED, isChecked);
    }

    @Override
    public void initDataInstance(Context context) {
        if(!isAfterRotate){
            registerViewModel = new RegisterViewModel();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (before < s.length()) {
            // means user deleted character
            if (!isEmailEdit && (CommonUtils.EmailValidation(s.toString())) &&
                    !isSelect) {
                isEmailEdit = true;
                view.alertBox();// registerName.getText().toString()
            }
        }
        isSelect = false;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public String getUUID() {
        return loginUuid.getString(UUID_KEY, DEFAULT_UUID_VALUE);
    }

    private void sendGTMClick(){
        UnifyTracking.eventRegister(AppEventTracking.EventLabel.REGISTER_STEP_1);
    }
}
