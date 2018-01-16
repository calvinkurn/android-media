package com.tokopedia.session.session.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.session.base.BaseImpl;

import java.util.HashMap;

/**
 * Created by m.normansyah on 1/25/16.
 */
public abstract class RegisterNew extends BaseImpl<RegisterNewView> {
    public static final String
        EMAIL = "EMAIL",
        PASSWORD = "PASSWORD",
        IS_SELECT_FROM_AUTO_TEXT_VIEW = "IS_SELECT_FROM_AUTO_TEXT_VIEW",
        IS_EMAIL_EDITTED_FOR_THE_FIRSTTIME = "IS_EMAIL_EDITTED_FOR_THE_FIRSTTIME",
        IS_SAVING = "IsSaving", IS_CHECKED = "IS_CHECKED", DATA = "DATA"
                ;
    String LOGIN_UUID_KEY = "LOGIN_UUID";
    String UUID_KEY = "uuid";
    String DEFAULT_UUID_VALUE = "";

    @Override
    public String getMessageTAG() {
        return getMessageTAG(RegisterNew.class);
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return className.getSimpleName()+" : ";
    }

    public RegisterNew(RegisterNewView view) {
        super(view);
    }

    public abstract void validateEmail(Context context, String name, String email, String password);

    public abstract void saveData(HashMap<String, Object> data);

    public abstract void setData(Context baseContext, int type, Bundle data);

    public abstract void unSubscribeFacade();

    public abstract void sendGTMRegisterError(String label);

}
