package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.feeddetail;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.TimeConverter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.FeedDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductCardHeaderViewModel;

/**
 * @author by nisie on 5/19/17.
 */

public class FeedDetailHeaderViewHolder extends AbstractViewHolder<FeedDetailHeaderViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.feed_detail_header;
    private final FeedPlusDetail.View viewListener;

    TextView shopName;
    ImageView shopAvatar;
    ImageView goldMerchantBadge;
    ImageView officialStoreBadge;
    TextView shopSlogan;

    public FeedDetailHeaderViewHolder(View itemView, FeedPlusDetail.View viewListener) {
        super(itemView);
        shopName = (TextView) itemView.findViewById(R.id.shop_name);
        shopAvatar = (ImageView) itemView.findViewById(R.id.shop_avatar);
        goldMerchantBadge = (ImageView) itemView.findViewById(R.id.gold_merchant);
        officialStoreBadge = (ImageView) itemView.findViewById(R.id.official_store);

        shopSlogan = (TextView) itemView.findViewById(R.id.shop_slogan);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(FeedDetailHeaderViewModel viewModel) {

        shopName.setText(MethodChecker.fromHtml(viewModel.getShopName()));
        ImageHandler.LoadImage(shopAvatar, viewModel.getShopAvatar());

        if (viewModel.isGoldMerchant())
            goldMerchantBadge.setVisibility(View.VISIBLE);
        else
            goldMerchantBadge.setVisibility(View.GONE);

        if (viewModel.isOfficialStore())
            officialStoreBadge.setVisibility(View.VISIBLE);
        else
            officialStoreBadge.setVisibility(View.GONE);

        shopSlogan.setText(TimeConverter.generateTime(viewModel.getShopSlogan()));

        shopAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToShopDetail();
            }
        });
    }
}
