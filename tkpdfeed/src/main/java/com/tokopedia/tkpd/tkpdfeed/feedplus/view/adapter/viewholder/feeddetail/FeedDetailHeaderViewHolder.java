package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.feeddetail;

import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.core.util.TimeConverter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderViewModel;

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
    ImageView gmBadge;
    ImageView osBadge;

    private final static String ADD_STRING = "tambah";
    private final static String EDIT_STRING = "ubah";

    public FeedDetailHeaderViewHolder(View itemView, FeedPlusDetail.View viewListener) {
        super(itemView);
        shopName = (TextView) itemView.findViewById(R.id.shop_name);
        shopAvatar = (ImageView) itemView.findViewById(R.id.shop_avatar);
        shopSlogan = (TextView) itemView.findViewById(R.id.shop_slogan);
        gmBadge = (ImageView) itemView.findViewById(R.id.gold_merchant);
        osBadge = (ImageView) itemView.findViewById(R.id.official_store);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final FeedDetailHeaderViewModel viewModel) {

        String shopNameString = String.valueOf(MethodChecker.fromHtml(viewModel.getShopName()));
        String actionString = String.valueOf(MethodChecker.fromHtml(viewModel.getActionText()));

        StringBuilder titleText = new StringBuilder();

        if (viewModel.isGoldMerchant() || viewModel.isOfficialStore())
            titleText.append("     ");

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

        ForegroundColorSpan spanColorChange = new ForegroundColorSpan(viewListener.getColor(R.color.black_54));
        TypefaceSpan styleSpan = new TypefaceSpan("sans-serif");

        setSpan(actionSpanString, goToShopDetail, titleText, shopNameString);
        setSpan(actionSpanString, spanColorChange, titleText, ADD_STRING);
        setSpan(actionSpanString, spanColorChange, titleText, EDIT_STRING);

        setSpan(actionSpanString, styleSpan, titleText, ADD_STRING);
        setSpan(actionSpanString, styleSpan, titleText, EDIT_STRING);


        ImageHandler.LoadImage(shopAvatar, viewModel.getShopAvatar());

        if (viewModel.isOfficialStore()) {
            gmBadge.setVisibility(View.GONE);
            osBadge.setVisibility(View.VISIBLE);
        } else if (viewModel.isGoldMerchant()) {
            gmBadge.setVisibility(View.VISIBLE);
            osBadge.setVisibility(View.GONE);
        }else {
            gmBadge.setVisibility(View.GONE);
            osBadge.setVisibility(View.GONE);
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

    private void setSpan(SpannableString actionSpanString, Object object, StringBuilder titleText, String stringEdited) {
        if (object instanceof ImageSpan) {
            actionSpanString.setSpan(object, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (titleText.toString().contains(stringEdited)) {
            actionSpanString.setSpan(object, titleText.indexOf(stringEdited)
                    , titleText.indexOf(stringEdited) + stringEdited.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

}
