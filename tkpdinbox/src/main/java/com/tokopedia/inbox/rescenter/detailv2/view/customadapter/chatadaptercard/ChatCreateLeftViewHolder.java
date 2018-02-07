package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProductAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProveAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatCreateLeftViewModel;

/**
 * Created by yoasfs on 26/10/17.
 */

public class ChatCreateLeftViewHolder extends AbstractViewHolder<ChatCreateLeftViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_create_left;

    public static final String CREATE = "create";
    public static final String RECOMPLAIN = "recomplain";
    public static final int COUNT_MAX_PRODUCT = 5;

    DetailResChatFragmentListener.View mainView;
    View layoutTitle, layoutDate1, layoutDate2, layoutDate3;
    TextView tvTitle, tvBuyerProblem, tvBuyerSolution, tvBuyerText, tvProve, tvSellerChoice,
            tvUserTitle, tvUsername, tvDate1, tvDate2, tvDate3, tvBuyerTextTitle;
    RecyclerView rvProve, rvProduct;
    Button btnSeeAllProduct;
    ChatProveAdapter proveAdapter;
    ChatProductAdapter productAdapter;
    FrameLayout flSeeAllProducts;
    LinearLayout ffBubble2;

    public ChatCreateLeftViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvTitle = itemView.findViewById(R.id.tv_create_title);
        tvBuyerProblem = itemView.findViewById(R.id.tv_buyer_problem);
        tvBuyerSolution = itemView.findViewById(R.id.tv_buyer_solution);
        tvBuyerText = itemView.findViewById(R.id.tv_buyer_text);
        tvSellerChoice = itemView.findViewById(R.id.tv_seller_choice);
        tvProve = itemView.findViewById(R.id.tv_prove);
        rvProve = itemView.findViewById(R.id.rv_prove);
        rvProduct = itemView.findViewById(R.id.rv_complained_product);
        btnSeeAllProduct = itemView.findViewById(R.id.btn_see_all_product);
        flSeeAllProducts = itemView.findViewById(R.id.fl_see_all_product);
        ffBubble2 = itemView.findViewById(R.id.ff_bubble_left_2);
        tvBuyerTextTitle = itemView.findViewById(R.id.tv_buyer_text_title);

        layoutTitle = itemView.findViewById(R.id.layout_title);
        layoutDate1 = itemView.findViewById(R.id.layout_date_1);
        layoutDate2 = itemView.findViewById(R.id.layout_date_2);
        layoutDate3 = itemView.findViewById(R.id.layout_date_3);

        tvUserTitle = layoutTitle.findViewById(R.id.tv_user_title);
        tvUsername = layoutTitle.findViewById(R.id.tv_username);
        tvDate1 = layoutDate1.findViewById(R.id.tv_date);
        tvDate2 = layoutDate2.findViewById(R.id.tv_date);
        tvDate3 = layoutDate3.findViewById(R.id.tv_date);
    }

    @Override
    public void bind(final ChatCreateLeftViewModel element) {
        final Context context = itemView.getContext();
        if (element.getActionType().equals(CREATE)) {
            tvTitle.setText(MethodChecker.fromHtml(
                    context.getResources().getString(R.string.string_complaint_title)
                            .replace(context.getResources().getString(R.string.string_complaint_title_identifier),
                                    buildComplaintStoreName(element.getShopDomain().getName()))));
        } else {
            tvTitle.setText(element.getConversationDomain().getAction().getTitle());
        }
        tvBuyerProblem.setText(element.getConversationDomain().getTrouble().getString());
        tvBuyerSolution.setText(element.getConversationDomain().getSolution().getName());
        if (element.getConversationDomain().getMessage().isEmpty()) {
            tvBuyerTextTitle.setVisibility(View.GONE);
            tvBuyerText.setVisibility(View.GONE);
        }
        tvBuyerText.setText(MethodChecker.fromHtml(element.getConversationDomain().getMessage()));

        tvUserTitle.setText(context.getResources().getString(R.string.string_tokopedia_system));
        tvUsername.setText(context.getResources().getString(R.string.string_tokopedia));
        String date = element.getConversationDomain().getCreateTime().getTimestamp();
        tvDate1.setText(date);
        tvDate2.setText(date);
        tvDate3.setText(date);


        btnSeeAllProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.intentToSeeAllProducts();
            }
        });
        rvProve.setVisibility(View.GONE);
        tvProve.setVisibility(View.GONE);
        if (element.getConversationDomain().getAttachment() != null && element.getConversationDomain().getAttachment().size() != 0) {
            rvProve.setVisibility(View.VISIBLE);
            tvProve.setVisibility(View.VISIBLE);
            rvProve.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            proveAdapter = new ChatProveAdapter(context, element.getConversationDomain().getAttachment());
            rvProve.setAdapter(proveAdapter);
            rvProve.setHasFixedSize(true);
        }

        ffBubble2.setVisibility(View.GONE);
        layoutDate2.setVisibility(View.GONE);
        if (element.getConversationDomain().getProduct() != null && element.getConversationDomain().getProduct().size() != 0) {
            ffBubble2.setVisibility(View.VISIBLE);
            layoutDate2.setVisibility(View.VISIBLE);
            rvProduct.setLayoutManager(new GridLayoutManager(context, COUNT_MAX_PRODUCT));
            productAdapter = new ChatProductAdapter(mainView,
                    context,
                    element.getConversationDomain().getProduct(),
                    COUNT_MAX_PRODUCT);
            rvProduct.setAdapter(productAdapter);
            rvProduct.setHasFixedSize(true);
            flSeeAllProducts.setVisibility(
                    element.getConversationDomain().getProduct().size() < 2 ?
                            View.GONE :
                            View.VISIBLE);
        }
    }

    private String buildComplaintStoreName(String shopName) {
        return "<b>" + shopName + "</b>";
    }
}
