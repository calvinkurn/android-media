package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.feeddetail;

import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
    public void bind(final FeedDetailHeaderViewModel viewModel) {

        String shopNameString = String.valueOf(MethodChecker.fromHtml(viewModel.getShopName()));
        String actionString = String.valueOf(MethodChecker.fromHtml(viewModel.getActionText()));

        StringBuilder titleText = new StringBuilder();
        titleText.append(shopNameString)
                .append(" ")
                .append(actionString);
        SpannableString actionSpanString = new SpannableString(titleText);

        ClickableSpan goToShopDetail = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                viewListener.onGoToShopDetail(
                        viewModel.getShopId());
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setTypeface(Typeface.DEFAULT_BOLD);
                ds.setColor(viewListener.getColor(R.color.black));
            }
        };

        actionSpanString.setSpan(goToShopDetail, titleText.indexOf(shopNameString)
                , titleText.indexOf(shopNameString) + shopNameString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        shopName.setText(actionSpanString);
        shopName.setMovementMethod(LinkMovementMethod.getInstance());

        ImageHandler.LoadImage(shopAvatar, viewModel.getShopAvatar());

        if (viewModel.isGoldMerchant())
            goldMerchantBadge.setVisibility(View.VISIBLE);
        else
            goldMerchantBadge.setVisibility(View.GONE);

        if (viewModel.isOfficialStore()) {
            officialStoreBadge.setVisibility(View.VISIBLE);
            goldMerchantBadge.setVisibility(View.GONE);
        } else
            officialStoreBadge.setVisibility(View.GONE);

        shopSlogan.setText(TimeConverter.generateTime(viewModel.getTime()));

        shopAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToShopDetail(viewModel.getShopId());
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onGoToShopDetail(viewModel.getShopId());
            }
        });
    }
}
