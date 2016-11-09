package com.tokopedia.inbox.contactus.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.inbox.contactus.interactor.ContactUsRetrofitInteractor;
import com.tokopedia.inbox.contactus.interactor.ContactUsRetrofitInteractorImpl;
import com.tokopedia.inbox.contactus.listener.ContactUsCategoryFragmentView;
import com.tokopedia.inbox.contactus.model.contactuscategory.ContactUsCategory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nisie on 8/12/16.
 */
public class ContactUsCategoryFragmentPresenterImpl implements ContactUsCategoryFragmentPresenter {


    ContactUsCategoryFragmentView viewListener;
    ContactUsRetrofitInteractor networkInteractor;

    public ContactUsCategoryFragmentPresenterImpl(ContactUsCategoryFragmentView viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new ContactUsRetrofitInteractorImpl();
    }

    @Override
    public void initCategory() {
        viewListener.showProgressDialog();
        networkInteractor.getCategory(viewListener.getActivity(), getCategoryParam(), new ContactUsRetrofitInteractor.GetCategoryListener() {
            @Override
            public void onSuccess(ContactUsCategory result) {
                viewListener.finishLoading();
                viewListener.setCategory(result);
                CommonUtils.dumper("NISNIS "+result.toString());
            }

            @Override
            public void onNoNetworkConnection() {
                viewListener.finishLoading();
                viewListener.showError("");

            }

            @Override
            public void onTimeout(String error) {
                viewListener.finishLoading();
                viewListener.showError(error);

            }

            @Override
            public void onError(String s) {
                viewListener.finishLoading();
                viewListener.showError(s);

            }

            @Override
            public void onNullData() {
                viewListener.finishLoading();
                viewListener.showError(viewListener.getString(R.string.default_request_error_null_data));

            }
        });
    }

    private Map<String, String> getCategoryParam() {
        return new HashMap<>();
    }

    @Override
    public void onDestroyView() {
        networkInteractor.unsubscribe();
    }
}
