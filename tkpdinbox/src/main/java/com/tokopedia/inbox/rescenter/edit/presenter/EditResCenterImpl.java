package com.tokopedia.inbox.rescenter.edit.presenter;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.inbox.rescenter.edit.interactor.RetrofitInteractor;
import com.tokopedia.inbox.rescenter.edit.interactor.RetrofitInteractorImpl;
import com.tokopedia.inbox.rescenter.edit.listener.EditResCenterListener;

/**
 * Created on 8/24/16.
 */
public class EditResCenterImpl implements EditResCenterPresenter {

    private static final String TAG = EditResCenterPresenter.class.getSimpleName();

    private final EditResCenterListener listener;
    private final RetrofitInteractor retrofit;

    public EditResCenterImpl(EditResCenterListener listener) {
        this.listener = listener;
        this.retrofit = new RetrofitInteractorImpl();
    }

    @Override
    public void initView(@NonNull Context context) {
        renderFragment();
    }

    private void renderFragment() {
        if (listener.isEdit()) {
            if (listener.isCustomer()) {
                listener.inflateBuyerEditResolutionForm();
            } else {
                listener.inflateSellerEditResolutionForm();
            }
        } else {
            listener.inflateAppealFragment();
        }
    }

}
