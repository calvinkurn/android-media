package com.tokopedia.seller.opportunity.snapshot.customview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ShopBadge;
import com.tokopedia.core.reputationproduct.util.ReputationLevelUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.snapshot.listener.SnapShotFragmentView;


/**
 * Created by hangnadi on 3/1/17.
 */

public class ShopInfoView extends BaseView<ProductDetailData, SnapShotFragmentView> {

    ImageView ivShopAva;
    ImageView ivGoldShop;
    ImageView ivOfficialStore;
    ImageView ivLuckyShop;
    TextView tvShopName;
    TextView tvShopLoc;
    ImageView ivBtnFav;
    ImageView ivShopMessage;
    LinearLayout llRating;
    LinearLayout llReputationMedal;
    TextView tvReputationPoint;
    LinearLayout layoutOther;

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutView(), this, true);

        ivShopAva = (ImageView) findViewById(R.id.iv_ava);
        ivGoldShop = (ImageView) findViewById(R.id.iv_gold);
        ivOfficialStore = (ImageView) findViewById(R.id.iv_official);
        ivLuckyShop = (ImageView) findViewById(R.id.iv_lucky);
        tvShopName = (TextView) findViewById(R.id.tv_name);
        tvShopLoc = (TextView) findViewById(R.id.tv_location);
        ivBtnFav = (ImageView) findViewById(R.id.iv_fav);
        ivShopMessage = (ImageView) findViewById(R.id.iv_message);
        llRating = (LinearLayout) findViewById(R.id.l_rating);
        llReputationMedal = (LinearLayout) findViewById(R.id.l_medal);
        tvReputationPoint = (TextView) findViewById(R.id.tv_reputation);
        layoutOther = (LinearLayout) findViewById(R.id.l_other);

    }

    public ShopInfoView(Context context) {
        super(context);
    }

    public ShopInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(SnapShotFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_shop_info_product_info;
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
    public void renderData(@NonNull ProductDetailData data) {
        tvShopName.setText(MethodChecker.fromHtml(data.getShopInfo().getShopName()));
        tvShopLoc.setText(data.getShopInfo().getShopLocation());
        tvReputationPoint.setText(String.format("%d %s", data.getShopInfo().getShopReputation(),
                getContext().getString(R.string.title_poin)));
        if (data.getShopInfo().getShopStats().getShopBadge() != null) generateMedal(data);
        ImageHandler.loadImageCircle2(getContext(), ivShopAva, data.getShopInfo().getShopAvatar(),
                R.drawable.ic_default_shop_ava);
        LuckyShopImage.loadImage(ivLuckyShop, data.getShopInfo().getShopLucky());

        ivBtnFav.setVisibility(data.getShopInfo().getShopIsOwner() == 1 ? GONE : VISIBLE);
        ivGoldShop.setVisibility(data.getShopInfo().getShopIsGold() == 1 ? VISIBLE : GONE);
        switchOfficialStoreBadge(data.getShopInfo().getShopIsOfficial());

        ivShopAva.setOnClickListener(new ShopInfoView.ClickShopAva(data));
        tvShopName.setOnClickListener(new ShopInfoView.ClickShopName(data));
        llRating.setOnClickListener(new ShopInfoView.ClickShopRating(data));
        ivShopMessage.setVisibility(GONE);
        ivBtnFav.setVisibility(GONE);
        layoutOther.setVisibility(GONE);
        setVisibility(VISIBLE);
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
            listener.onProductShopNameClicked(data.getShopInfo().getShopId());
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
            listener.onProductShopRatingClicked(data.getShopInfo().getShopId());
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
            listener.onProductShopAvatarClicked(data.getShopInfo().getShopId());
        }
    }

    private void switchOfficialStoreBadge(int isOfficialStore) {
        if (isOfficialStore == 1) {
            ivGoldShop.setVisibility(GONE);
            ivOfficialStore.setVisibility(VISIBLE);
        }
    }

    private void generateMedal(ProductDetailData data) {
        ShopBadge shopBadge = data.getShopInfo().getShopStats().getShopBadge();
        ReputationLevelUtils.setReputationMedals(getContext(), llReputationMedal,
                shopBadge.getSet(), shopBadge.getLevel(),
                String.valueOf(data.getShopInfo().getShopReputation()));
    }
}
