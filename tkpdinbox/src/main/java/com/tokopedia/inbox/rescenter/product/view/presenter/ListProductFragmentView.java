package com.tokopedia.inbox.rescenter.product.view.presenter;

import com.tokopedia.inbox.rescenter.product.view.model.ListProductViewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/23/17.
 */

public interface ListProductFragmentView {
    void setResolutionID(String resolutionID);

    void setLoadingView(boolean param);

    String getResolutionID();

    void setErrorMessage(String messageError);

    void onGetHistoryAwbFailed(String messageError);

    void onGetHistoryAwbTimeOut();

    List<ListProductViewItem> getViewData();

    void renderData();

    void setViewData(ArrayList<ListProductViewItem> viewData);

    void showLoadingDialog(boolean show);

    void showSnackBar(String messageError);

    void showTimeOutMessage();

    void setOnProductItemClick(String productID, String productName);
}
