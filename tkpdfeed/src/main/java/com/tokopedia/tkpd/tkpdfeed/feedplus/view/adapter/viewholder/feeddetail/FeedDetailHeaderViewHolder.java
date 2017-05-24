package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.feeddetail;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.R2;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.TimeConverter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductCardHeaderViewModel;

import butterknife.BindView;

/**
 * @author by nisie on 5/19/17.
 */

public class FeedDetailHeaderViewHolder extends AbstractViewHolder<ProductCardHeaderViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.feed_detail_product_header;
    private final FeedPlusDetail.View viewListener;

    TextView title;
    ImageView shopAvatar;
    ImageView goldMerchantBadge;
    TextView time;

    public FeedDetailHeaderViewHolder(View itemView, FeedPlusDetail.View viewListener) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        shopAvatar = (ImageView) itemView.findViewById(R.id.shop_avatar);
        goldMerchantBadge = (ImageView) itemView.findViewById(R.id.gold_merchant);
        time = (TextView) itemView.findViewById(R.id.time);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(ProductCardHeaderViewModel productCardHeaderViewModel) {
        String titleText = "<b>" + productCardHeaderViewModel.getShopName() + "</b> "
                + productCardHeaderViewModel.getActionText();
        title.setText(MethodChecker.fromHtml(titleText));
        ImageHandler.LoadImage(shopAvatar, productCardHeaderViewModel.getShopAvatar());

        if (productCardHeaderViewModel.isGoldMerchant())
            goldMerchantBadge.setVisibility(View.VISIBLE);
        else
            goldMerchantBadge.setVisibility(View.GONE);

        time.setText(TimeConverter.generateTime(productCardHeaderViewModel.getPostTime()));

        shopAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToShopDetail();
            }
        });
    }
}
