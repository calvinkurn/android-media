package com.tokopedia.inbox.rescenter.shipping.view;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tokopedia.core.database.model.ResCenterAttachment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.shipping.model.InputShippingParamsGetModel;
import com.tokopedia.inbox.rescenter.shipping.model.ResCenterKurir;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by hangnadi on 12/13/16.
 */
public interface InputShippingFragmentView {

    Activity getActivity();

    InputShippingParamsGetModel getParamsModel();

    void setParamsModel(InputShippingParamsGetModel paramsModel);

    ArrayList<ResCenterAttachment> getAttachmentData();

    void setAttachmentData(ArrayList<ResCenterAttachment> attachmentData);

    EditText getShippingRefNum();

    Spinner getShippingSpinner();

    RecyclerView getListAttachment();

    TextView getErrorSpinner();

    View getLoadingView();

    View getMainView();

    void renderAttachmentAdapter();

    void renderSpinner(List<ResCenterKurir.Kurir> shippingList);

    void showTimeOutMessage(NetworkErrorHelper.RetryClickedListener listener);

    void showErrorMessage(String message);

    void startActivityForResult(Intent intent, int requestCode);

    void startActivity(Intent intent);

    void renderInputShippingRefNum(String text);

    void setConfirmButtonEnabled();

    void setConfirmButtonDisabled();

    void toastTimeOutMessage();

    void toastErrorMessage(String message);

    void finishAsSuccessResult();

    void dropKeyBoard();

    Retrofit getRetrofit();
}
