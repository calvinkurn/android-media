package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.FeedProductAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.TimeConverter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductCardViewModel;

/**
 * @author by nisie on 5/16/17.
 */

public class ProductCardViewHolder extends AbstractViewHolder<ProductCardViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_product_multi;

    TextView title;
    ImageView shopAvatar;
    ImageView goldMerchantBadge;
    TextView time;
    View shareButton;
    RecyclerView recyclerView;

    private FeedProductAdapter adapter;
    private FeedPlus.View viewListener;

    public ProductCardViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.title);
        shopAvatar = (ImageView) itemView.findViewById(R.id.shop_avatar);
        goldMerchantBadge = (ImageView) itemView.findViewById(R.id.gold_merchant);
        time = (TextView) itemView.findViewById(R.id.time);
        shareButton = itemView.findViewById(R.id.share_button);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.product_list);

        this.viewListener = viewListener;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                itemView.getContext(),
                6,
                LinearLayoutManager.VERTICAL,
                false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getData().getListProduct().size() == 1) {
                    return 6;
                } else if (adapter.getData().getListProduct().size() % 3 == 0
                        || adapter.getData().getListProduct().size() > 6) {
                    return 2;
                } else if (adapter.getData().getListProduct().size() % 2 == 0) {
                    return 3;
                } else if (adapter.getData().getListProduct().size() == 5) {
                    return getSpanSizeFor5Item(position);
                } else {
                    return 0;
                }
            }
        });
        adapter = new FeedProductAdapter(itemView.getContext(), viewListener);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private int getSpanSizeFor5Item(int position) {
        switch (position) {
            case 0:
            case 1:
                return 3;
            case 2:
            case 3:
            case 4:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public void bind(ProductCardViewModel productCardViewModel) {
        setHeader(productCardViewModel);
        adapter.setData(productCardViewModel);
        setFooter(productCardViewModel);
    }

    public void setHeader(final ProductCardViewModel productCardViewModel) {
        String titleText = "<b>" + productCardViewModel.getHeader().getShopName() + "</b> "
                + productCardViewModel.getHeader().getActionText();
        title.setText(MethodChecker.fromHtml(titleText));
        ImageHandler.LoadImage(shopAvatar, productCardViewModel.getHeader().getShopAvatar());

        if (productCardViewModel.getHeader().isGoldMerchant())
            goldMerchantBadge.setVisibility(View.VISIBLE);
        else
            goldMerchantBadge.setVisibility(View.GONE);

        time.setText(TimeConverter.generateTime(productCardViewModel.getHeader().getPostTime()));

        shopAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToShopDetail();
            }
        });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToFeedDetail(productCardViewModel);
            }
        });

    }

    public void setFooter(ProductCardViewModel footer) {
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onShareButtonClicked();
            }
        });
    }
}
