package com.tokopedia.profile.view.customview;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;
import com.tokopedia.session.R;

/**
 * Created by nakama on 27/02/18.
 */

public class PartialUserShopView extends BaseCustomView {

    private ImageView ivShopProfile;
    private ImageView ivGoldShop;
    private ImageView ivOfficialStore;
    private TextView tvShopName;
    private TextView tvShopLocation;
    private TextView tvLastOnline;
    private LinearLayout favouriteButton;
    private LinearLayout llRating;
    private LinearLayout llReputationMedal;

    private boolean isShopFavorite = false;

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchSaveInstanceState(container);
    }

    public PartialUserShopView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PartialUserShopView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PartialUserShopView(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.partial_profile_shop_info, this);
        ivShopProfile = view.findViewById(R.id.iv_shop_profile);
        ivGoldShop = view.findViewById(R.id.iv_gold_shop);
        ivOfficialStore = view.findViewById(R.id.iv_official);
        tvShopName = view.findViewById(R.id.tv_shop_name);
        tvShopLocation = view.findViewById(R.id.tv_location);
        tvLastOnline = view.findViewById(R.id.tv_last_online);
        favouriteButton = view.findViewById(R.id.ll_fav_shop);
        llRating = view.findViewById(R.id.ll_rating);
        llReputationMedal = view.findViewById(R.id.ll_medal);
    }

    public void renderData(TopProfileViewModel model) {
        ImageHandler.loadImage2(ivShopProfile, model.getShopLogo(), R.drawable.ic_default_shop_ava);
        ivGoldShop.setVisibility(model.isGoldShop() ? VISIBLE : GONE);
        switchOfficialStoreBadge(model.isOfficialShop());

        tvShopName.setText(model.getShopName());
        tvShopLocation.setText(model.getShopLocation());
        tvLastOnline.setText(model.getShopLastOnline());

        favouriteButton.setVisibility(model.isUser() ? GONE : VISIBLE);

        this.setVisibility(VISIBLE);
    }

    private void switchOfficialStoreBadge(boolean isOfficialStore) {
        if (isOfficialStore) {
            ivGoldShop.setVisibility(GONE);
            ivOfficialStore.setVisibility(VISIBLE);
        } else {
            ivOfficialStore.setVisibility(GONE);
        }
    }

}
