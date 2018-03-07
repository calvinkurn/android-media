package com.tokopedia.profile.view.customview;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.product.facade.NetworkParam;
import com.tokopedia.core.product.interactor.RetrofitInteractor;
import com.tokopedia.core.product.interactor.RetrofitInteractorImpl;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;
import com.tokopedia.session.R;

/**
 * @author by alvinatin on 27/02/18.
 */

public class PartialUserShopView extends BaseCustomView {

    private ImageView ivShopProfile;
    private ImageView ivGoldShop;
    private ImageView ivOfficialStore;
    private TextView tvShopName;
    private TextView tvShopLocation;
    private TextView tvLastOnline;
    private TextView tvFavouriteButton;
    private LinearLayout favouriteButton;
    private LinearLayout llRating;
    private ImageView ivReputationMedal;

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

    public PartialUserShopView(@NonNull Context context, @Nullable AttributeSet attrs, int
            defStyleAttr) {
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
        tvFavouriteButton = view.findViewById(R.id.favorite_tv);
        favouriteButton = view.findViewById(R.id.ll_fav_shop);
        llRating = view.findViewById(R.id.ll_rating);
        ivReputationMedal = view.findViewById(R.id.iv_medal);
    }

    public void renderData(TopProfileViewModel model) {

        if (model.getShopId() != 0) {
            this.setVisibility(VISIBLE);
            ImageHandler.loadImage2(ivShopProfile, model.getShopLogo(), R.drawable
                    .ic_default_shop_ava);
            ivGoldShop.setVisibility(model.isGoldShop() ? VISIBLE : GONE);
            switchOfficialStoreBadge(model.isOfficialShop());
            ImageHandler.LoadImage(ivReputationMedal, model.getShopBadge());
            tvShopName.setText(MethodChecker.fromHtml(model.getShopName()));
            tvShopLocation.setText(model.getShopLocation());
            tvLastOnline.setText(model.getShopLastOnline());
            favouriteButton.setVisibility(model.getIsUser() ? GONE : VISIBLE);
            if (!model.getIsUser()) {
                favouriteButton.setVisibility(VISIBLE);
                isShopFavorite = model.isFavorite();
                updateFavoriteStatus(isShopFavorite);
                favouriteButton.setOnClickListener(new ClickFavouriteShop(model));
            }else{
                favouriteButton.setVisibility(GONE);
            }
            tvShopName.setOnClickListener(new ClickShopInfo(model));
            ivShopProfile.setOnClickListener(new ClickShopInfo(model));
            llRating.setOnClickListener(new ClickShopInfo(model));
        } else {
            this.setVisibility(GONE);
        }
    }

    private void switchOfficialStoreBadge(boolean isOfficialStore) {
        if (isOfficialStore) {
            ivGoldShop.setVisibility(GONE);
            ivOfficialStore.setVisibility(VISIBLE);
        } else {
            ivOfficialStore.setVisibility(GONE);
        }
    }

    private class ClickFavouriteShop implements OnClickListener {

        private final TopProfileViewModel data;

        ClickFavouriteShop(TopProfileViewModel model) {
            data = model;
        }

        @Override
        public void onClick(View v) {
            new RetrofitInteractorImpl().favoriteShop(
                    getContext(),
                    NetworkParam.paramFaveShop(String.valueOf(data.getShopId())),
                    new RetrofitInteractor.FaveListener() {
                        @Override
                        public void onSuccess(boolean status) {
                            reverseFavorite();
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }
    }

    private class ClickShopInfo implements  OnClickListener{

        private final TopProfileViewModel data;

        ClickShopInfo(TopProfileViewModel model){
            data = model;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), ShopInfoActivity.class);
            intent.putExtras(
                    ShopInfoActivity.
                            createBundle(String.valueOf(data.getShopId()),
                                    "",
                                    data.getShopName(),
                                    data.getShopLogo(),
                                    data.isFavorite() ? 1 : 0));
            getContext().startActivity(intent);
        }
    }


    public void reverseFavorite() {
        if (isShopFavorite) {
            updateFavoriteStatus(false);
        } else {
            updateFavoriteStatus(true);
        }
    }

    public void updateFavoriteStatus(boolean isShopFavorite) {
        int screenDensityDpi = getResources().getDisplayMetrics().densityDpi;

        if (isShopFavorite) {
            favouriteButton.setSelected(true);
            favouriteButton.setClickable(true);
            tvFavouriteButton.setText(getContext().getString(R.string.shop_favorited));
            tvFavouriteButton.setTextColor(ContextCompat.getColor(getContext(), R.color
                    .tkpd_main_green));
            this.isShopFavorite = true;
            if (screenDensityDpi <= DisplayMetrics.DENSITY_HIGH) {
                tvFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_check_green_12dp, 0, 0, 0);

            } else {
                tvFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_check_green_24dp, 0, 0, 0);
            }
        } else {
            this.isShopFavorite = false;
            favouriteButton.setSelected(false);
            favouriteButton.setClickable(true);
            tvFavouriteButton.setText(getContext().getString(R.string.shop_not_favorite));
            tvFavouriteButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            if (screenDensityDpi <= DisplayMetrics.DENSITY_HIGH) {
                tvFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_add_12dp, 0, 0, 0);

            } else {
                tvFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_add_24dp, 0, 0, 0);
            }
        }
    }
}
