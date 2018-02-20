package com.tokopedia.topads.sdk.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.domain.model.Badge;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.presenter.BannerAdsPresenter;
import com.tokopedia.topads.sdk.utils.ImageLoader;
import com.tokopedia.topads.sdk.utils.ImpresionTask;

import org.apache.commons.text.StringEscapeUtils;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class TopAdsBannerView extends LinearLayout implements BannerAdsContract.View {

    private static final String TAG = TopAdsBannerView.class.getSimpleName();
    private BannerAdsPresenter presenter;
    private TopAdsListener adsListener;
    private TopAdsBannerClickListener topAdsBannerClickListener;
    private ImageLoader imageLoader;

    public TopAdsBannerView(Context context) {
        super(context);
        init();
    }

    public TopAdsBannerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TopAdsBannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public static String escapeHTML(String s) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return Html.fromHtml(StringEscapeUtils.unescapeHtml4(s), Html.FROM_HTML_MODE_LEGACY).toString();
            } else {
                return Html.fromHtml(StringEscapeUtils.unescapeHtml4(s)).toString();
            }
        } catch (Exception e) {
            return "";
        }
    }

    private void createViewCpmShop(Context context, final CpmData.Cpm cpm) {
        if (checkActivityIsExist(context))
            return;
        inflate(getContext(), R.layout.layout_ads_banner_shop, this);
        final ImageView iconImg = (ImageView) findViewById(R.id.icon);
        TextView promotedTxt = (TextView) findViewById(R.id.title_promote);
        TextView nameTxt = (TextView) findViewById(R.id.shop_name);
        TextView descriptionTxt = (TextView) findViewById(R.id.description);
        LinearLayout badgeContainer = (LinearLayout) findViewById(R.id.badges_container);
        Glide.with(context).load(cpm.getCpmImage().getFullEcs()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                iconImg.setImageBitmap(resource);
                new ImpresionTask().execute(cpm.getCpmImage().getFullUrl());
            }
        });
        promotedTxt.setText(cpm.getPromotedText());
        nameTxt.setText(escapeHTML(cpm.getName()));

        String desc = String.format("%s %s", escapeHTML(cpm.getDecription()), cpm.getCta());
        setTextColor(descriptionTxt, desc, cpm.getCta(), ContextCompat.getColor(context, R.color.tkpd_main_green));

        if (cpm.getBadges().size() > 0) {
            badgeContainer.removeAllViews();
            badgeContainer.setVisibility(VISIBLE);
            for (Badge badge : cpm.getBadges()) {
                ImageView badgeImg = new ImageView(context);
                badgeImg.setLayoutParams(new LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.badge_size),
                        context.getResources().getDimensionPixelSize(R.dimen.badge_size)));
                Glide.with(context).load(badge.getImageUrl()).into(badgeImg);
                badgeContainer.addView(badgeImg);
            }
        } else {
            badgeContainer.setVisibility(GONE);
        }
    }

    private boolean checkActivityIsExist(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return activity.isFinishing();
        }
        return false;
    }

    private void setTextColor(TextView view, String fulltext, String subtext, int color) {
        view.setText(fulltext, TextView.BufferType.SPANNABLE);
        Spannable str = (Spannable) view.getText();
        int i = fulltext.indexOf(subtext);
        str.setSpan(new ForegroundColorSpan(color), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new TypefaceSpan("sans-serif"), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void createViewCpmDigital(Context context, final CpmData.Cpm cpm) {
        if (checkActivityIsExist(context))
            return;
        inflate(getContext(), R.layout.layout_ads_banner_digital, this);
        final ImageView iconImg = (ImageView) findViewById(R.id.icon);
        TextView nameTxt = (TextView) findViewById(R.id.name);
        TextView descriptionTxt = (TextView) findViewById(R.id.description);
        Glide.with(context).load(cpm.getCpmImage().getFullEcs()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                iconImg.setImageBitmap(resource);
                new ImpresionTask().execute(cpm.getCpmImage().getFullUrl());
            }
        });
        nameTxt.setText(escapeHTML(cpm.getName()));
        String desc = String.format("%s %s", escapeHTML(cpm.getDecription()), cpm.getCta());
        setTextColor(descriptionTxt, desc, cpm.getCta(), ContextCompat.getColor(context, R.color.tkpd_main_green));
    }

    public void setConfig(Config config) {
        presenter.setConfig(config);
    }

    public void setAdsListener(TopAdsListener adsListener) {
        this.adsListener = adsListener;
    }

    public void setTopAdsBannerClickListener(TopAdsBannerClickListener topAdsBannerClickListener) {
        this.topAdsBannerClickListener = topAdsBannerClickListener;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void displayAds(CpmModel cpmModel) {
        if (cpmModel.getData().size() > 0) {
            final CpmData data = cpmModel.getData().get(0);
            if (data.getCpm().getCpmShop() != null && isResponseValid(data)) {
                createViewCpmShop(getContext(), data.getCpm());
            } else if (data.getCpm().getTemplateId() == 4) {
                createViewCpmDigital(getContext(), data.getCpm());
            }
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (topAdsBannerClickListener != null) {
                        topAdsBannerClickListener.onBannerAdsClicked(data.getApplinks());
                        new ImpresionTask().execute(data.getAdClickUrl());
                    }
                }
            });
        }
    }

    private boolean isResponseValid(CpmData data) {
        return !data.getCpm().getCta().isEmpty()
                && !data.getCpm().getPromotedText().isEmpty()
                && data.getCpm().getBadges().size() > 0;
    }

    @Override
    public void onCanceled() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void loadTopAds() {
        presenter.loadTopAds();
    }

    @Override
    public void notifyAdsErrorLoaded(int errorCode, String message) {
        if (adsListener != null) {
            adsListener.onTopAdsFailToLoad(errorCode, message);
        }
    }

    public void init() {
        imageLoader = new ImageLoader(getContext());
        presenter = new BannerAdsPresenter(getContext());
        presenter.attachView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.detachView();
    }
}
