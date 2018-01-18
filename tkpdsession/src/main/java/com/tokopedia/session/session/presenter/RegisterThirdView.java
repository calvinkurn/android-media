package com.tokopedia.session.session.presenter;

import android.content.Context;

import com.tokopedia.core.presenter.BaseView;

import java.util.HashMap;

/**
 * Created by noiz354 on 1/27/16.
 */
@Deprecated
public interface RegisterThirdView extends BaseView{
    void setData(HashMap<String, Object> data);
    boolean checkValidation();
    void showProgress(boolean showDialog);
    void enView(boolean enStatus);
    void showUnknownError(final Throwable e);
    void showErrorValidateEmail();
    void initDatePickerDialog(Context context, int year, int monthOfYear, int dayOfMonth);
    void initDatePicker(long maxtime, long mintime);

    void setAllowedField();
}
