package com.tokopedia.seller.gmstat.views.helper;

import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.library.LoaderImageView;
import com.tokopedia.seller.gmstat.library.LoaderTextView;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class BuyerDataLoading {
    private final View parentView;

    LoaderImageView buyerdataHeaderIc;

    LoaderTextView buyerDataHeaderText;

    LoaderTextView buyerNumberHeaderLoading;

    LoaderTextView buyerCountLoading;

    LoaderImageView buyerCountIconLoading;

    LoaderImageView dataBuyerLoading;

    void initView(View itemView){

        buyerdataHeaderIc= (LoaderImageView) itemView.findViewById(R.id.buyer_data_header_ic);

        buyerDataHeaderText= (LoaderTextView) itemView.findViewById(R.id.buyer_data_header_text);

        buyerNumberHeaderLoading= (LoaderTextView) itemView.findViewById(R.id.buyer_number_header_loading);

        buyerCountLoading= (LoaderTextView) itemView.findViewById(R.id.buyer_count_loading);

        buyerCountIconLoading= (LoaderImageView) itemView.findViewById(R.id.buyer_count_icon_loading);

        dataBuyerLoading= (LoaderImageView) itemView.findViewById(R.id.data_buyer_loading);
    }


    public BuyerDataLoading(View itemView){
        initView(itemView);

        parentView = itemView.findViewById(R.id.buyer_data_loading);

        buyerdataHeaderIc.resetLoader();
        buyerDataHeaderText.resetLoader();
        buyerNumberHeaderLoading.resetLoader();
        buyerCountLoading.resetLoader();
        buyerCountIconLoading.resetLoader();
        dataBuyerLoading.resetLoader();
    }

    public void displayLoading() {
        parentView.setVisibility(View.VISIBLE);
    }

    public void hideLoading(){
        parentView.setVisibility(View.GONE);
    }
}
