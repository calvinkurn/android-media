package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.feeddetail;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
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

import static com.tokopedia.core.R.id.title;
import static com.tokopedia.core.R.id.view;

/**
 * @author by nisie on 5/19/17.
 */

public class FeedDetailHeaderViewHolder extends AbstractViewHolder<FeedDetailHeaderViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.feed_detail_header;
    private final FeedPlusDetail.View viewListener;

    TextView shopName;
    ImageView shopAvatar;
    TextView shopSlogan;

    public FeedDetailHeaderViewHolder(View itemView, FeedPlusDetail.View viewListener) {
        super(itemView);
        shopName = (TextView) itemView.findViewById(R.id.shop_name);
        shopAvatar = (ImageView) itemView.findViewById(R.id.shop_avatar);
        shopSlogan = (TextView) itemView.findViewById(R.id.shop_slogan);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final FeedDetailHeaderViewModel viewModel) {

        String shopNameString = String.valueOf(MethodChecker.fromHtml(viewModel.getShopName()));
        String actionString = String.valueOf(MethodChecker.fromHtml(viewModel.getActionText()));

        StringBuilder titleText = new StringBuilder();

        if (viewModel.isGoldMerchant() || viewModel.isOfficialStore())
            titleText.append("  ");

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
                ds.setColor(viewListener.getColor(R.color.black_70));
                ds.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            }
        };

        actionSpanString.setSpan(goToShopDetail, titleText.indexOf(shopNameString)
                , titleText.indexOf(shopNameString) + shopNameString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ImageHandler.LoadImage(shopAvatar, viewModel.getShopAvatar());

        if (viewModel.isGoldMerchant()) {
            setBadge(actionSpanString, R.drawable.ic_shop_gold);
        } else if (viewModel.isOfficialStore()) {
            setBadge(actionSpanString, R.drawable.ic_badge_official);
        }

        shopName.setText(actionSpanString);
        shopName.setMovementMethod(LinkMovementMethod.getInstance());

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

    private void setBadge(SpannableString actionSpanString, int resId) {
        int size = viewListener.getResources().getDimensionPixelOffset(R.dimen.ic_badge_size);
        Drawable badge = MethodChecker.getDrawable(viewListener.getActivity(), resId);
        badge.setBounds(0, 0, size, size);
        ImageSpan is = new ImageSpan(badge, DynamicDrawableSpan.ALIGN_BASELINE);
        actionSpanString.setSpan(is, 0, 1, 0);
    }


}
