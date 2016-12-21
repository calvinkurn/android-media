package com.tokopedia.session.session.intentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.R;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.core.session.model.CreatePasswordModel;
import com.tokopedia.session.session.model.RegisterSuccessModel;
import com.tokopedia.core.session.model.RegisterViewModel;
import com.tokopedia.core.session.presenter.Login;
import com.tokopedia.core.session.presenter.RegisterNext;
import com.tokopedia.session.session.presenter.RegisterPassPhone;
import com.tokopedia.session.session.subscriber.BaseAccountSubscriber;
import com.tokopedia.core.util.AppEventTracking;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterService extends IntentService implements DownloadServiceConstant{

    public static final String TAG = "RegisterService";

    private Gson gson;
    private ResultReceiver receiver;
    private SessionHandler sessionHandler;
    int typeAccess;

    public RegisterService() {
        super("RegisterService");
    }


    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;


    public static void startRegister(Context context, RegisterResultReceiver receiver, Bundle bundle, int type) {
        Intent intent = new Intent(context, RegisterService.class);
        intent.putExtra(TYPE, type);
        if(receiver!=null)
            intent.putExtra(DownloadService.RECEIVER, receiver);

        if (bundle.getInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, 0) != 0){
            intent.putExtra(AppEventTracking.GTMKey.ACCOUNTS_TYPE,
                    bundle.getInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, 0));
        }

        switch (type){
            case REGISTER:
                RegisterViewModel registerViewModel = Parcels.unwrap(bundle.getParcelable(REGISTER_MODEL_KEY));
                intent.putExtra(REGISTER_MODEL_KEY, Parcels.wrap(registerViewModel));
                break;
            case REGISTER_PASS_PHONE:
                CreatePasswordModel model = Parcels.unwrap(bundle.getParcelable(CREATE_PASSWORD_MODEL_KEY));
                intent.putExtra(CREATE_PASSWORD_MODEL_KEY, Parcels.wrap(model));
                break;
            default:
                throw new RuntimeException("unknown type for starting register !!!");
        }

        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        receiver = intent.getParcelableExtra(RECEIVER);
        int type = intent.getIntExtra(TYPE, INVALID_TYPE);
        if (intent.getIntExtra(AppEventTracking.GTMKey.ACCOUNTS_TYPE, 0) != 0){
            typeAccess = intent.getIntExtra(AppEventTracking.GTMKey.ACCOUNTS_TYPE, 0);
        } else {
            typeAccess = type;
        }

        gson = new GsonBuilder().create();
        sessionHandler = new SessionHandler(getApplicationContext());

        Bundle running = new Bundle();
        Bundle bundle  = new Bundle();
        AccountsService accountsService;
        Map<String, String> params = new HashMap<>();

        running.putInt(TYPE, type);

        switch (type) {
            case REGISTER:

                running.putBoolean(REGISTER_QUESTION_LOADING, true);
                receiver.send(STATUS_RUNNING, running);

                RegisterViewModel registerViewModel = Parcels.unwrap(intent.getParcelableExtra(REGISTER_MODEL_KEY));

                params.put(RegisterNext.BIRTHDAY, registerViewModel.getmDateDay() + "");
                params.put(RegisterNext.BIRTHMONTH, registerViewModel.getmDateMonth() + "");
                params.put(RegisterNext.BIRTHYEAR, registerViewModel.getmDateYear() + "");
                params.put(RegisterNext.CONFIRM_PASSWORD, registerViewModel.getmConfirmPassword());
                params.put(RegisterNext.EMAIL, registerViewModel.getmEmail());
                params.put(RegisterNext.FACEBOOK_USERID, "");
                params.put(RegisterNext.FULLNAME, registerViewModel.getmName());
                params.put(RegisterNext.GENDER, registerViewModel.getmGender() + "");
                params.put(RegisterNext.PASSWORD, registerViewModel.getmPassword());
                params.put(RegisterNext.PHONE, registerViewModel.getmPhone());
                params.put(RegisterNext.IS_AUTO_VERIFY, (registerViewModel.isAutoVerify() ? 1 : 0) + "");// change to this "1" to auto verify


                bundle.putBoolean(AccountsService.USING_HMAC, true);
                bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);

                accountsService = new AccountsService(bundle);
                accountsService.getApi().doRegister(AuthUtil.generateParams(getApplicationContext(),params))
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(getApplicationContext(),type,receiver,sessionHandler));

                break;


            case REGISTER_PASS_PHONE:

                running.putBoolean(LOGIN_SHOW_DIALOG, true);
                receiver.send(STATUS_RUNNING, running);

                CreatePasswordModel model = Parcels.unwrap(intent.getParcelableExtra(CREATE_PASSWORD_MODEL_KEY));

                params = AuthUtil.generateParams(getApplicationContext(),params);
                params.put(RegisterPassPhone.BIRTHDAY, String.valueOf(model.getBdayDay()));
                params.put(RegisterPassPhone.BIRTHMONTH, String.valueOf(model.getBdayMonth()));
                params.put(RegisterPassPhone.BIRTHYEAR, String.valueOf(model.getBdayYear()));
                params.put(RegisterPassPhone.CONFIRM_PASSWORD, model.getConfirmPass());
                params.put(RegisterPassPhone.NEW_PASSWORD, model.getNewPass());
                params.put(RegisterPassPhone.MSISDN, model.getMsisdn());
                params.put(RegisterPassPhone.FULLNAME, model.getFullName());
                params.put(RegisterPassPhone.GENDER, model.getGender());
                params.put(RegisterPassPhone.REGISTER_TOS, model.getRegisterTos());
                params.put(Login.USER_ID, SessionHandler.getTempLoginSession(this));

                String authKey = sessionHandler.getAccessToken(this);
                authKey = sessionHandler.getTokenType(this) +" "+ authKey;

                bundle.putString(AccountsService.AUTH_KEY, authKey);

                accountsService = new AccountsService(bundle);
                accountsService.getApi().createPassword(params)
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber(getApplicationContext(),type,receiver,sessionHandler));

                break;
        }
    }

    private class Subscriber extends BaseAccountSubscriber {
        public Subscriber(Context context, int type, ResultReceiver receiver, SessionHandler sessionHandler) {
            super(context, type, receiver, sessionHandler);
        }

        public void onSuccessResponse(JSONObject jsonObject) {
            Bundle result;
            switch (type) {
                case REGISTER:
                    result = new Bundle();
                    result.putInt(TYPE, type);
                    RegisterSuccessModel registerSuccessModel = gson.fromJson(jsonObject.toString(), RegisterSuccessModel.class);
                    result.putParcelable(REGISTER_MODEL_KEY, registerSuccessModel);
                    result.putInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, typeAccess);
                    receiver.send(STATUS_FINISHED, result);
                    break;

                case REGISTER_PASS_PHONE:
                    result = new Bundle();
                    result.putInt(TYPE, type);
                    result.putInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, typeAccess);
                    if (jsonObject.optInt("is_success", 0) == 1) {
                        receiver.send(STATUS_FINISHED, result);
                    } else {
                        sendMessageErrorToReceiver(context.getString(R.string.default_request_error_unknown));
                    }
                    break;
            }
        }
    }
}
