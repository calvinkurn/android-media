package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.MethodChecker;
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
    TextView tvTitle, tvBuyerSolution, tvBuyerText, tvSellerChoice, tvUserTitle, tvUsername, tvDate1, tvDate2, tvDate3;
    RecyclerView rvProve, rvProduct;
    Button btnSeeAllProduct;

    public ChatCreateLeftViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvTitle = (TextView) itemView.findViewById(R.id.tv_create_title);
        tvBuyerSolution = (TextView) itemView.findViewById(R.id.tv_buyer_solution);
        tvBuyerText = (TextView) itemView.findViewById(R.id.tv_buyer_text);
        tvSellerChoice = (TextView) itemView.findViewById(R.id.tv_seller_choice);
        rvProve = (RecyclerView) itemView.findViewById(R.id.rv_prove);
        rvProduct = (RecyclerView) itemView.findViewById(R.id.rv_complained_product);
        btnSeeAllProduct = (Button) itemView.findViewById(R.id.btn_see_all_product);
        layoutTitle = itemView.findViewById(R.id.layout_title);
        layoutDate1 = itemView.findViewById(R.id.layout_date_1);
        layoutDate2 = itemView.findViewById(R.id.layout_date_2);
        layoutDate3 = itemView.findViewById(R.id.layout_date_3);

        tvUserTitle = (TextView) layoutTitle.findViewById(R.id.tv_user_title);
        tvUsername = (TextView) layoutTitle.findViewById(R.id.tv_username);
        tvDate1 = (TextView) layoutDate1.findViewById(R.id.tv_date);
        tvDate2 = (TextView) layoutDate2.findViewById(R.id.tv_date);
        tvDate3 = (TextView) layoutDate3.findViewById(R.id.tv_date);
    }

    @Override
    public void bind(ChatCreateLeftViewModel element) {
        Context context = itemView.getContext();
        tvTitle.setText(context.getResources().getString(R.string.string_complaint_title)
                .replace(
                        context.getResources().getString(R.string.string_complaint_title_identifier),
                        element.getShopDomain().getName()));
        tvBuyerSolution.setText(element.getConversationDomain().getSolution().getName());
        tvBuyerText.setText(MethodChecker.fromHtml(element.getConversationDomain().getMessage()));

        tvUserTitle.setText("Sistem Tokopedia");
        tvUsername.setText("Toped");
        String date = DateFormatUtils.formatDate(
                DateFormatUtils.FORMAT_T_Z,
                DateFormatUtils.FORMAT_DD_MMM_YYYY_HH_MM,
                element.getConversationDomain().getCreateTime().getTimestamp());
        tvDate1.setText(date);
        tvDate2.setText(date);
        tvDate3.setText(date);
    }
}
