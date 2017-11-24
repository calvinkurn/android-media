package com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.adapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProductAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.ChatProveAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatCreateLeftViewModel;
import com.tokopedia.inbox.rescenter.product.ListProductActivity;

import org.w3c.dom.Text;

/**
 * Created by yoasfs on 26/10/17.
 */

public class ChatCreateLeftViewHolder extends AbstractViewHolder<ChatCreateLeftViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_detail_create_left;

    public static final int COUNT_MAX_PRODUCT = 5;

    DetailResChatFragmentListener.View mainView;
    View layoutTitle, layoutDate1, layoutDate2, layoutDate3;
    TextView tvTitle, tvBuyerSolution, tvBuyerText, tvSellerChoice, tvUserTitle, tvUsername, tvDate1, tvDate2, tvDate3;
    RecyclerView rvProve, rvProduct;
    Button btnSeeAllProduct;
    ChatProveAdapter proveAdapter;
    ChatProductAdapter productAdapter;
    FrameLayout flSeeAllProducts;

    public ChatCreateLeftViewHolder(View itemView, DetailResChatFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        tvTitle = itemView.findViewById(R.id.tv_create_title);
        tvBuyerSolution = itemView.findViewById(R.id.tv_buyer_solution);
        tvBuyerText = itemView.findViewById(R.id.tv_buyer_text);
        tvSellerChoice = itemView.findViewById(R.id.tv_seller_choice);
        rvProve = itemView.findViewById(R.id.rv_prove);
        rvProduct = itemView.findViewById(R.id.rv_complained_product);
        btnSeeAllProduct = itemView.findViewById(R.id.btn_see_all_product);
        flSeeAllProducts = itemView.findViewById(R.id.fl_see_all_product);

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
        tvTitle.setText(context.getResources().getString(R.string.string_complaint_title)
                .replace(
                        context.getResources().getString(R.string.string_complaint_title_identifier),
                        element.getShopDomain().getName()));
        tvBuyerSolution.setText(element.getConversationDomain().getSolution().getName());
        tvBuyerText.setText(MethodChecker.fromHtml(element.getConversationDomain().getMessage()));

        tvUserTitle.setText(context.getResources().getString(R.string.string_tokopedia_system));
        tvUsername.setText(context.getResources().getString(R.string.string_tokopedia));
        String date = DateFormatUtils.formatDateForResoChatV2(
                element.getConversationDomain().getCreateTime().getTimestamp());
        tvDate1.setText(date);
        tvDate2.setText(date);
        tvDate3.setText(date);

        flSeeAllProducts.setVisibility(
                element.getConversationDomain().getAttachment().size() < 2 ?
                        View.GONE :
                        View.VISIBLE);

        btnSeeAllProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.intentToSeeAllProducts();
            }
        });

        rvProve.setLayoutManager(new LinearLayoutManager(context));
        proveAdapter = new ChatProveAdapter(context, element.getConversationDomain().getAttachment());
        rvProve.setAdapter(proveAdapter);

        rvProduct.setLayoutManager(new GridLayoutManager(context, COUNT_MAX_PRODUCT));
        productAdapter = new ChatProductAdapter(context,
                element.getConversationDomain().getProduct(),
                COUNT_MAX_PRODUCT);
        rvProduct.setAdapter(productAdapter);
    }
}
