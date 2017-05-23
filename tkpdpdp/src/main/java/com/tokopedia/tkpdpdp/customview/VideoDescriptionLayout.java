package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

/**
 * Created by kris on 11/3/16. Tokopedia
 */

public class VideoDescriptionLayout extends BaseView<ProductDetailData, ProductDetailView> {

    private boolean isExpand = false;
    private DescriptionTextView tvDesc;
    private ImageView ivToggle;
    private LinearLayout descriptionContainer;
    private ProductVideoHorizontalScroll productVideoHorizontalScroll;

    public VideoDescriptionLayout(Context context) {
        super(context);
        initView(context);
    }

    public VideoDescriptionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.youtube_video_list_place_holder;
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        tvDesc = (DescriptionTextView) findViewById(R.id.tv_description);
        ivToggle = (ImageView) findViewById(R.id.iv_toggle);
        descriptionContainer = (LinearLayout) findViewById(R.id.tv_desc);
        productVideoHorizontalScroll
                = (ProductVideoHorizontalScroll) findViewById(R.id.product_video_horizontal_scroll);
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    private void renderCollapse() {
        ivToggle.setImageResource(R.drawable.chevron_down);
        tvDesc.setMaxLines(5);
        tvDesc.requestFocus();
        isExpand = false;
    }

    private void renderExpand() {
        ivToggle.setImageResource(R.drawable.chevron_up);
        tvDesc.setMaxLines(Integer.MAX_VALUE);
        tvDesc.requestFocus();
        isExpand = true;
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        ivToggle.setImageResource(R.drawable.chevron_down);
        tvDesc.setMaxLines(5);
        isExpand = false;
        ivToggle.setOnClickListener(new VideoDescriptionLayout.ClickToggle());
        tvDesc.setText(data.getInfo().getProductDescription() == null
                || data.getInfo().getProductDescription().equals("")
                || data.getInfo().getProductDescription().equals("0")
                ? "Tidak ada deskripsi" : data.getInfo().getProductDescription());
        tvDesc.post(new Runnable() {
            @Override
            public void run() {
                int lineCnt = tvDesc.getLineCount();
                if (lineCnt > 5)
                    ivToggle.setVisibility(VISIBLE);
                else
                    ivToggle.setVisibility(INVISIBLE);
            }
        });
        tvDesc.setAutoLinkMask(0);
        Linkify.addLinks(tvDesc, Linkify.WEB_URLS);
        tvDesc.setText(MethodChecker.fromHtml(tvDesc.getText().toString()));
        descriptionContainer.setVisibility(VISIBLE);
        setVisibility(VISIBLE);

        //makeTextViewResizable(tvDesc, 5, "Selengkapnya", true);

    }

    public void renderVideoData(VideoData data) {

        productVideoHorizontalScroll.renderData(data);
    }

    public void destroyVideoLayoutProcess() {
        productVideoHorizontalScroll.destroyAllOnGoingYoutubeProcess();
    }

    public void refreshVideo() {
        productVideoHorizontalScroll.clearYoutubeVideo();
    }

    private class ClickToggle implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (!isExpand) {
                renderExpand();
                UnifyTracking.eventPDPExpandDescription();
            } else {
                renderCollapse();
            }
        }
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    public static class MySpannable extends ClickableSpan {

        private boolean isUnderline = false;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {

            ds.setUnderlineText(isUnderline);

        }

        @Override
        public void onClick(View widget) {

        }
    }


    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "Selengkapnya", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 5, "Sembunyikan", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

}