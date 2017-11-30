package com.tokopedia.seller.seller.info.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.seller.info.view.model.SellerInfoModel;
import com.tokopedia.seller.seller.info.view.util.SellerInfoDateUtil;

/**
 * Created by normansyahputa on 11/30/17.
 */

public class SellerInfoAdapter extends BaseListAdapter<SellerInfoModel> {

    SellerInfoDateUtil sellerInfoDateUtil;

    public SellerInfoAdapter(SellerInfoDateUtil sellerInfoDateUtil) {
        this.sellerInfoDateUtil = sellerInfoDateUtil;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case SellerInfoModel.TYPE:
                return null;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }
}
