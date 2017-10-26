package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatCreateLeftViewModel;

import org.w3c.dom.Text;

/**
 * Created by yoasfs on 26/10/17.
 */

public class ChatCreateLeftViewHolder extends AbstractViewHolder<ChatCreateLeftViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_create_left;

    DetailResChatFragmentListener.View mainView;
    View layoutTitle, layoutDate1, layoutDate2, layoutDate3;
    TextView tvTitle, tvBuyerSolution, tvBuyerText, tvSellerChoice;
    RecyclerView rvProve, rvProduct;
    Button btnSeeAllProduct;

    public ChatCreateLeftViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvBuyerSolution = (TextView) itemView.findViewById(R.id.tv_buyer_solution);
        tvBuyerText = (TextView) itemView.findViewById(R.id.tv_buyer_text);
        tvSellerChoice = (TextView) itemView.findViewById(R.id.tv_seller_choice);
        rvProve = (RecyclerView) itemView.findViewById(R.id.rv_prove);
        rvProduct = (RecyclerView) itemView.findViewById(R.id.rv_complained_product);
        btnSeeAllProduct = (Button) itemView.findViewById(R.id.btn_see_all_product);
    }

    @Override
    public void bind(ChatCreateLeftViewModel element) {

    }
}
