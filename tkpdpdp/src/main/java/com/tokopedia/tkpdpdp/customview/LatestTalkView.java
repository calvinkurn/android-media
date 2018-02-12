package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.discussion.LatestTalkViewModel;
import com.tokopedia.core.util.LabelUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.Locale;

/**
 * Created by hangnadi on 8/22/17.
 */

public class LatestTalkView extends BaseView<ProductDetailData, ProductDetailView> {

    public static final int DATE_TEXT_LENGTH_MAX = 35;

    private ImageView avatarTalk;
    private TextView textTalkName;
    private TextView textTalkDate;
    private TextView textTalkMessage;
    private ImageView avatarComment;
    private TextView textCommentName;
    private TextView textCommentDate;
    private TextView textCommentMessage;
    private TextView textAllDiscussion;
    private View layoutComment;

    public LatestTalkView(Context context) {
        super(context);
        initView(context);
    }

    public LatestTalkView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_latest_discussion;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }


    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        avatarTalk = (ImageView) findViewById(R.id.avatar_talk);
        textTalkName = (TextView) findViewById(R.id.text_talk_name);
        textTalkDate = (TextView) findViewById(R.id.text_talk_date);
        textTalkMessage = (TextView) findViewById(R.id.text_talk_message);
        layoutComment = findViewById(R.id.layout_comment);
        avatarComment = (ImageView) findViewById(R.id.avatar_comment);
        textCommentName = (TextView) findViewById(R.id.text_comment_name);
        textCommentDate = (TextView) findViewById(R.id.text_comment_date);
        textCommentMessage = (TextView) findViewById(R.id.text_comment_message);
        textAllDiscussion = (TextView) findViewById(R.id.text_all_discussion);
    }

    @Override
    public void renderData(@NonNull ProductDetailData productDetailData) {

        LatestTalkViewModel data = productDetailData.getLatestTalkViewModel();
        layoutComment.setVisibility(GONE);
        if (data != null) {

            ImageHandler.loadImageRounded2(getContext(), avatarTalk, data.getTalkUserAvatar());
            textTalkName.setText(data.getTalkUsername());
            textTalkDate.setText(data.getTalkDate());
            textTalkMessage.setText(MethodChecker.fromHtml((data.getTalkMessage())));

            if (data.getCommentId() != null) {
                layoutComment.setVisibility(VISIBLE);
                ImageHandler.loadImageRounded2(getContext(), avatarComment, data.getCommentUserAvatar());
                textCommentName.setText(data.getCommentUserName());
                textCommentDate.setText(data.getCommentDate());
                textCommentMessage.setText(MethodChecker.fromHtml(data.getCommentMessage()));
                LabelUtils label = LabelUtils.getInstance(getContext(), textCommentDate, DATE_TEXT_LENGTH_MAX);
                label.giveSquareLabel(data.getCommentUserLabel());
            }

            String button = getResources().getString(R.string.title_all_discussion);
            String buttonFormat = button + String.format(Locale.getDefault(), " (%s)", productDetailData.getStatistic().getProductTalkCount());
            textAllDiscussion.setText(buttonFormat);

            textAllDiscussion.setOnClickListener(new DiscussionClick(productDetailData));
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }

    private class DiscussionClick implements View.OnClickListener {

        private final ProductDetailData data;

        DiscussionClick(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putString("product_id", String.valueOf(data.getInfo().getProductId()));
            bundle.putString("shop_id", String.valueOf(data.getShopInfo().getShopId()));
            bundle.putString("prod_name", data.getInfo().getProductName());
            bundle.putString("is_owner", String.valueOf(data.getShopInfo().getShopIsOwner()));
            bundle.putString("product_image", data.getProductImages().get(0).getImageSrc300());
            listener.onProductTalkClicked(bundle);
        }
    }
}
