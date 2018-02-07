package com.tokopedia.seller.seller.info.view;

import android.support.annotation.NonNull;

import com.tokopedia.seller.base.view.listener.BaseListViewListener;
import com.tokopedia.seller.seller.info.data.model.ResponseSellerInfoModel;
import com.tokopedia.seller.seller.info.view.model.SellerInfoModel;

import java.util.List;

/**
 * Created by normansyahputa on 12/5/17.
 */

public interface SellerInfoView extends BaseListViewListener<SellerInfoModel> {
    void onSearchLoaded(@NonNull List<SellerInfoModel> list, int totalItem, boolean hasNext);
}
