package com.tokopedia.tkpdpdp.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ShopBadge;
import com.tokopedia.core.reputationproduct.util.ReputationLevelUtils;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;



/**
 * @author Angga.Prasetiyo on 27/10/2015.
 */
public class ShopInfoViewV2 extends BaseView<ProductDetailData, ProductDetailView> {

    private ImageView ivShopAva;
    private ImageView ivGoldShop;
    private ImageView ivOfficialStore;
    private ImageView ivLuckyShop;
    private TextView tvShopName;
    private TextView tvLastOnline;
    private Button sendMsgButton;
    private LinearLayout favoriteButton;
    private TextView favoriteText;
    private LinearLayout llReputationMedal;
    private TextView tvShopLocation;

    private boolean isShopFavorite = false;

    private ProductDetailData productDetailData;

    public ShopInfoViewV2(Context context) {
        super(context);
    }

    public ShopInfoViewV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_shop_info_product_info_v2;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        ivGoldShop.setVisibility(GONE);
        setVisibility(GONE);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        ivShopAva = (ImageView) findViewById(R.id.iv_ava);
        ivOfficialStore = (ImageView) findViewById(R.id.iv_official);
        ivGoldShop = (ImageView) findViewById(R.id.iv_gold_shop);
        ivLuckyShop = (ImageView) findViewById(R.id.iv_lucky);
        tvShopName = (TextView) findViewById(R.id.tv_name);
        tvLastOnline = (TextView) findViewById(R.id.last_online_textview);
        sendMsgButton = (Button) findViewById(R.id.send_msg_button);
        favoriteButton = (LinearLayout) findViewById(R.id.favorite_button);
        favoriteText = (TextView) findViewById(R.id.favorite_tv);
        llReputationMedal = (LinearLayout) findViewById(R.id.l_medal);
        tvShopLocation = findViewById(R.id.tv_shop_location);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void renderData(@NonNull final ProductDetailData data) {
        this.productDetailData = data;
        tvShopName.setText(MethodChecker.fromHtml(data.getShopInfo().getShopName()));
        if (data.getShopInfo().getShopStats().getShopBadge() != null) generateMedal(data);
        ImageHandler.loadImage2(ivShopAva, data.getShopInfo().getShopAvatar(),
                R.drawable.ic_default_shop_ava);
        LuckyShopImage.loadImage(ivLuckyShop, data.getShopInfo().getShopLucky());

        displayLastLogin(data);

        tvShopLocation.setText(
                data.getShopInfo().getShopLocation()
        );
        favoriteButton.setVisibility(data.getShopInfo().getShopIsAllowManage() == 1 ? GONE : VISIBLE);
        ivGoldShop.setVisibility(showGoldBadge(data) ? VISIBLE : GONE);
        switchOfficialStoreBadge(data.getShopInfo().getShopIsOfficial());

        adjustSendMsgAndFavoriteTextSize();
        sendMsgButton.setVisibility(data.getShopInfo().getShopId()
                .equals(SessionHandler.getShopID(getContext())) ? GONE : VISIBLE);

        updateFavoriteStatus(data.getShopInfo().getShopAlreadyFavorited());

        ivShopAva.setOnClickListener(new ClickShopAva(data));
        tvShopName.setOnClickListener(new ClickShopName(data));
        sendMsgButton.setOnClickListener(new ClickShopMessage(data));
        favoriteButton.setOnClickListener(new ClickBtnFave(data));
        setVisibility(VISIBLE);
    }

    private void displayLastLogin(@NonNull ProductDetailData data) {
        if (data.getShopInfo().getShopOwnerLastLogin() != null
                && data.getShopInfo().getShopOwnerLastLogin().length() > 0) {

            tvLastOnline.setText(
                    String.format(getContext().getString(R.string.value_shop_last_online),
                            data.getShopInfo().getShopOwnerLastLogin())
                            );
            tvLastOnline.setVisibility(VISIBLE);
        } else {
            tvLastOnline.setVisibility(GONE);
        }
    }

    private void generateMedal(ProductDetailData data) {
        ShopBadge shopBadge = data.getShopInfo().getShopStats().getShopBadge();
        ReputationLevelUtils.setReputationMedals(getContext(), llReputationMedal,
                shopBadge.getSet(), shopBadge.getLevel(),
                String.valueOf(data.getShopInfo().getShopReputation()));
    }

    public void reverseFavorite() {
        if (isShopFavorite) {
            updateFavoriteStatus(0);
            TrackingUtils.sendMoEngageFavoriteEvent(getContext(), productDetailData.getShopInfo().getShopName(), productDetailData.getShopInfo().getShopId(), productDetailData.getShopInfo().getShopDomain(), productDetailData.getShopInfo().getShopLocation(), productDetailData.getShopInfo().getShopIsOfficial() == 1 ? true : false, false);
        } else {
            updateFavoriteStatus(1);
            TrackingUtils.sendMoEngageFavoriteEvent(getContext(), productDetailData.getShopInfo().getShopName(), productDetailData.getShopInfo().getShopId(), productDetailData.getShopInfo().getShopDomain(), productDetailData.getShopInfo().getShopLocation(), productDetailData.getShopInfo().getShopIsOfficial() == 1 ? true : false, true);
        }
    }

    public void updateFavoriteStatus(int statFave) {
        int screenDensityDpi = getResources().getDisplayMetrics().densityDpi;
        switch (statFave) {
            case 1:
                favoriteButton.setSelected(true);
                favoriteButton.setClickable(true);
                favoriteText.setText(getContext().getString(R.string.favorited));
                favoriteText.setTextColor(ContextCompat.getColor(getContext(), R.color.tkpd_main_green));
                isShopFavorite = true;
                if (screenDensityDpi <= DisplayMetrics.DENSITY_HIGH) {
                    favoriteText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_check_green_12dp, 0, 0, 0);

                } else {
                    favoriteText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_check_green_24dp, 0, 0, 0);
                }
                break;
            case 0:
            default:
                isShopFavorite = false;
                favoriteButton.setSelected(false);
                favoriteButton.setClickable(true);
                favoriteText.setText(getContext().getString(R.string.fave));
                favoriteText.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                if (screenDensityDpi <= DisplayMetrics.DENSITY_HIGH) {
                    favoriteText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_add_black_12dp, 0, 0, 0);

                } else {
                    favoriteText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_add_black_24dp, 0, 0, 0);
                }
                break;
        }
    }

    private void adjustSendMsgAndFavoriteTextSize(){
        int screenDensityDpi = getResources().getDisplayMetrics().densityDpi;
        if (screenDensityDpi <= DisplayMetrics.DENSITY_HIGH) {
            sendMsgButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
            favoriteText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
            favoriteText.setPadding(0, 4, 0, 4);
        } else {
            sendMsgButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
            favoriteText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
        }
    }

    private class ClickBtnFave implements OnClickListener {
        private final ProductDetailData data;

        ClickBtnFave(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            listener.onProductShopFaveClicked(data.getShopInfo().getShopId(), data.getInfo().getProductId());
        }
    }

    private class ClickShopMessage implements OnClickListener {
        private final ProductDetailData data;

        ClickShopMessage(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            if (MainApplication.getAppContext() instanceof TkpdInboxRouter) {
                Intent intent = ((TkpdInboxRouter) MainApplication.getAppContext())
                        .getAskSellerIntent(v.getContext(),
                                String.valueOf(data.getShopInfo().getShopId()),
                                data.getShopInfo().getShopName(),
                                data.getInfo().getProductName(),
                                data.getInfo().getProductUrl(),
                                TkpdInboxRouter.PRODUCT,
                                data.getShopInfo().getShopAvatar());
                listener.onProductShopMessageClicked(intent);
            }
        }
    }

    private class ClickShopAva implements OnClickListener {
        private final ProductDetailData data;

        ClickShopAva(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("shop_id", data.getShopInfo().getShopId());
            bundle.putString("shop_name", data.getShopInfo().getShopName());
            bundle.putString("shop_avatar", data.getShopInfo().getShopAvatar());
            bundle.putInt("shop_favorite", data.getShopInfo().getShopAlreadyFavorited());
            listener.onProductShopAvatarClicked(bundle);
        }
    }

    private class ClickShopName implements OnClickListener {
        private final ProductDetailData data;

        ClickShopName(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("shop_id", data.getShopInfo().getShopId());
            bundle.putString("shop_name", data.getShopInfo().getShopName());
            bundle.putString("shop_avatar", data.getShopInfo().getShopAvatar());
            bundle.putInt("shop_favorite", data.getShopInfo().getShopAlreadyFavorited());
            listener.onProductShopNameClicked(bundle);
        }
    }

    private class ClickShopRating implements OnClickListener {
        private final ProductDetailData data;

        ClickShopRating(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putInt("tab", 2);
            bundle.putString("shop_id", data.getShopInfo().getShopId());
            listener.onProductShopRatingClicked(bundle);
        }
    }

    private void switchOfficialStoreBadge(int isOfficialStore) {
        if (isOfficialStore == 1) {
            ivGoldShop.setVisibility(GONE);
            ivOfficialStore.setVisibility(VISIBLE);
        }
    }

    private boolean showGoldBadge(ProductDetailData data) {
        return data.getShopInfo().getShopIsGold() == 1 && data.getShopInfo().shopIsGoldBadge();
    }
}
