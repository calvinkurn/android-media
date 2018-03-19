package com.tokopedia.shop.page.view.holder;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.reputation.ShopReputationView;
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed;
import com.tokopedia.shop.R;
import com.tokopedia.shop.common.constant.ShopStatusDef;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.page.view.model.ShopPageViewModel;
import com.tokopedia.shop.page.view.widget.ShopPageSubDetailView;
import com.tokopedia.shop.page.view.widget.ShopWarningTickerView;

/**
 * Created by nathan on 3/10/18.
 */

public class ShopPageHeaderViewHolder {

    public interface Listener {

        void goToShopInfo();

        void onTotalFavouriteClicked();

        void onTotalProductClicked();

        void onManageShopClicked();

        void onAddProductClicked();

        void onChatSellerClicked();

        void onToggleFavouriteShop(boolean favouriteShop);

        void displayQualityInfo(String qualityAverage, float rating, String totalReview);

        void displayReputationInfo(int reputationMedalType, int reputationLevel, String reputationScore);

        void displayReputationSpeedInfo(@DrawableRes int speedIcon, int speedLevel, String speedLevelDescription);

        void onShopIconClicked();

        void onShopNameClicked();

        void onShopInfoClicked();
    }

    private static final int REPUTATION_SPEED_LEVEL_VERY_FAST = 5;
    private static final int REPUTATION_SPEED_LEVEL_FAST = 4;
    private static final int REPUTATION_SPEED_LEVEL_NORMAL = 3;
    private static final int REPUTATION_SPEED_LEVEL_SLOW = 2;
    private static final int REPUTATION_SPEED_LEVEL_VERY_SLOW = 1;
    private static final int REPUTATION_SPEED_LEVEL_DEFAULT = 0;

    private ImageView backgroundImageView;
    private ImageView shopIconImageView;
    private ImageView shopStatusImageView;
    private ImageView locationImageView;
    private TextView shopNameTextView;
    private TextView shopInfoLocationTextView;
    private LinearLayout shopTitleLinearLayout;

    private ShopPageSubDetailView totalFavouriteDetailView;
    private ShopPageSubDetailView totalProductDetailView;
    private ShopPageSubDetailView reputationDetailView;
    private ShopPageSubDetailView productQualityDetailView;
    private ShopPageSubDetailView reputationSpeedDetailView;
    private ShopWarningTickerView shopWarningTickerView;
    private ShopReputationView reputationView;
    private TextView totalFavouriteTextView;
    private TextView totalProductTextView;
    private RatingBar ratingBarImageView;
    private TextView qualityValueTextView;
    private ImageView speedImageView;
    private TextView speedValueTextView;
    private Button buttonManageShop;
    private Button buttonAddProduct;
    private Button buttonChatSeller;
    private Button buttonFavouriteShop;
    private Button buttonAlreadyFavouriteShop;
    private ImageView shopInfo;

    private boolean favouriteShop;
    private Listener listener;

    public boolean isFavouriteShop() {
        return favouriteShop;
    }

    public void toggleFavouriteShopStatus() {
        favouriteShop = !favouriteShop;
    }

    public ShopPageHeaderViewHolder(View view, final Listener listener) {
        this.listener = listener;

        shopWarningTickerView = view.findViewById(R.id.shop_warning_ticker_view);
        backgroundImageView = view.findViewById(R.id.image_view_shop_background);
        shopIconImageView = view.findViewById(R.id.image_view_shop_icon);
        shopStatusImageView = view.findViewById(R.id.image_view_shop_status);
        shopNameTextView = view.findViewById(R.id.text_view_shop_name);
        locationImageView = view.findViewById(R.id.image_view_location);
        shopInfoLocationTextView = view.findViewById(R.id.text_view_location);
        shopTitleLinearLayout = view.findViewById(R.id.linear_layout_header_title);
        shopInfo = view.findViewById(R.id.shop_info);

        totalFavouriteDetailView = view.findViewById(R.id.sub_detail_view_total_favourite);
        totalProductDetailView = view.findViewById(R.id.sub_detail_view_total_product);
        reputationDetailView = view.findViewById(R.id.sub_detail_view_reputation);
        productQualityDetailView = view.findViewById(R.id.sub_detail_view_product_quality);
        reputationSpeedDetailView = view.findViewById(R.id.sub_detail_view_reputation_speed);

        totalFavouriteTextView = view.findViewById(R.id.text_view_total_favourite);
        totalProductTextView = view.findViewById(R.id.text_view_total_product);
        ratingBarImageView = view.findViewById(R.id.product_rating);
        qualityValueTextView = view.findViewById(R.id.text_view_product_quality_value);
        speedImageView = view.findViewById(R.id.image_view_speed);
        speedValueTextView = view.findViewById(R.id.text_view_speed_value);
        reputationView = view.findViewById(R.id.shop_reputation_view);

        buttonManageShop = view.findViewById(R.id.button_manage_shop);
        buttonAddProduct = view.findViewById(R.id.button_add_product);
        buttonChatSeller = view.findViewById(R.id.button_chat_seller);
        buttonFavouriteShop = view.findViewById(R.id.button_favourite_shop);
        buttonAlreadyFavouriteShop = view.findViewById(R.id.button_already_favourite_shop);

        shopTitleLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.goToShopInfo();
            }
        });
        totalFavouriteDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTotalFavouriteClicked();
            }
        });
        totalProductDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTotalProductClicked();
            }
        });

        buttonFavouriteShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFavouriteShop();
            }
        });
        buttonAlreadyFavouriteShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFavouriteShop();
            }
        });
        buttonManageShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onManageShopClicked();
            }
        });
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddProductClicked();
            }
        });
        buttonChatSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChatSellerClicked();
            }
        });

        shopIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onShopIconClicked();
            }
        });
        shopNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onShopNameClicked();
            }
        });
        shopInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onShopInfoClicked();
            }
        });
    }

    private void toggleFavouriteShop() {
        buttonFavouriteShop.setEnabled(false);
        buttonAlreadyFavouriteShop.setEnabled(false);
        listener.onToggleFavouriteShop(favouriteShop);
    }

    public void renderData(ShopPageViewModel shopPageViewModel, boolean myShop) {
        updateShopInfo(shopPageViewModel.getShopInfo(), myShop);
        updateReputationSpeed(shopPageViewModel.getReputationSpeed());
        updateViewShopOpen(shopPageViewModel.getShopInfo());
    }

    public void updateShopInfo(ShopInfo shopInfo, boolean myShop) {
        favouriteShop = TextApiUtils.isValueTrue(shopInfo.getInfo().getShopAlreadyFavorited());
        shopNameTextView.setText(MethodChecker.fromHtml(shopInfo.getInfo().getShopName()).toString());

        ImageHandler.LoadImage(backgroundImageView, shopInfo.getInfo().getShopCover());
        ImageHandler.LoadImage(shopIconImageView, shopInfo.getInfo().getShopAvatar());

        if (TextApiUtils.isValueTrue(shopInfo.getInfo().getShopIsOfficial())) {
            displayAsOfficialStoreView(shopInfo);
        } else if (shopInfo.getInfo().isShopIsGoldBadge()) {
            displayAsGoldMerchantView(shopInfo);
            displayAsGeneralShop(shopInfo);
        } else {
            displayAsGeneralShop(shopInfo);
        }
        if (myShop) {
            displayAsSellerShop();
        } else {
            displayAsBuyerShop();
        }
    }

    private void displayAsOfficialStoreView(ShopInfo shopInfo) {
        shopStatusImageView.setVisibility(View.VISIBLE);
        shopStatusImageView.setImageResource(R.drawable.ic_badge_shop_official);
        locationImageView.setImageResource(R.drawable.ic_info_checked_grey);
        shopInfoLocationTextView.setText(shopInfoLocationTextView.getContext().getString(R.string.shop_page_label_authorized));

        totalFavouriteDetailView.setVisibility(View.VISIBLE);
        totalProductDetailView.setVisibility(View.VISIBLE);
        totalFavouriteTextView.setText(String.valueOf(shopInfo.getInfo().getShopTotalFavorit()));
        totalProductTextView.setText(String.valueOf(shopInfo.getInfo().getTotalActiveProduct()));
    }

    private void displayAsGoldMerchantView(ShopInfo shopInfo) {
        shopStatusImageView.setVisibility(View.VISIBLE);
        shopStatusImageView.setImageResource(R.drawable.ic_badge_shop_gm);
    }

    private void displayAsGeneralShop(final ShopInfo shopInfo) {
        reputationDetailView.setVisibility(View.VISIBLE);
        productQualityDetailView.setVisibility(View.VISIBLE);
        locationImageView.setImageResource(R.drawable.ic_info_location_grey);
        shopInfoLocationTextView.setText(shopInfo.getInfo().getShopLocation());

        final int reputationMedalType = (int) shopInfo.getStats().getShopBadgeLevel().getSet();
        final int reputationLevel = (int) shopInfo.getStats().getShopBadgeLevel().getLevel();
        final String reputationScore = shopInfo.getStats().getShopReputationScore();
        reputationView.setValue(reputationMedalType, reputationLevel, reputationScore);
        reputationDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.displayReputationInfo(reputationMedalType, reputationLevel, reputationScore);
            }
        });
        final String qualityAverage = shopInfo.getRatings().getQuality().getAverage();
        qualityValueTextView.setText(qualityAverage);
        float rating;
        try {
             rating = Float.parseFloat(qualityAverage);
        }catch (Exception e){
            rating = shopInfo.getRatings().getQuality().getRatingStar();
        }
        ratingBarImageView.setRating(rating);
        final float finalRating = rating;
        productQualityDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.displayQualityInfo(qualityAverage, finalRating, shopInfo.getRatings().getQuality().getCountTotal());
            }
        });
    }

    private void displayAsBuyerShop() {
        buttonChatSeller.setVisibility(View.VISIBLE);
        updateFavouriteButtonView();
    }

    private void displayAsSellerShop() {
        buttonManageShop.setVisibility(View.VISIBLE);
        buttonAddProduct.setVisibility(View.VISIBLE);
    }

    public void updateFavouriteButtonView() {
        buttonFavouriteShop.setEnabled(true);
        buttonAlreadyFavouriteShop.setEnabled(true);
        if (favouriteShop) {
            buttonFavouriteShop.setVisibility(View.GONE);
            buttonAlreadyFavouriteShop.setVisibility(View.VISIBLE);
        } else {
            buttonFavouriteShop.setVisibility(View.VISIBLE);
            buttonAlreadyFavouriteShop.setVisibility(View.GONE);
        }
    }

    public void updateReputationSpeed(ReputationSpeed reputationSpeed) {
        final int speedLevel = reputationSpeed.getRecent12Month().getSpeedLevel();
        final int reputationIcon = getReputationSpeedIcon(speedLevel);
        final String speedLevelDescription = reputationSpeed.getRecent12Month().getSpeedLevelDescription();
        speedImageView.setImageResource(getReputationSpeedIcon(speedLevel));
        if (TextUtils.isEmpty(speedLevelDescription)) {
            speedValueTextView.setText(R.string.shop_page_speed_shop_not_available);
        } else {
            speedValueTextView.setText(speedLevelDescription);
        }
        reputationSpeedDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.displayReputationSpeedInfo(reputationIcon, speedLevel, speedLevelDescription);
            }
        });
    }

    @DrawableRes
    private int getReputationSpeedIcon(int level) {
        switch (level) {
            case REPUTATION_SPEED_LEVEL_VERY_FAST:
                return R.drawable.ic_shop_reputation_speed_very_fast;
            case REPUTATION_SPEED_LEVEL_FAST:
                return R.drawable.ic_shop_reputation_speed_fast;
            case REPUTATION_SPEED_LEVEL_NORMAL:
                return R.drawable.ic_shop_reputation_speed_normal;
            case REPUTATION_SPEED_LEVEL_SLOW:
                return R.drawable.ic_shop_reputation_speed_slow;
            case REPUTATION_SPEED_LEVEL_VERY_SLOW:
                return R.drawable.ic_shop_reputation_speed_very_slow;
            default:
                return R.drawable.ic_shop_reputation_speed_default;
        }
    }

    private void updateViewShopOpen(ShopInfo shopInfo) {
        switch ((int) shopInfo.getInfo().getShopStatus()) {
            case ShopStatusDef.CLOSED:
                showShopClosed(shopInfo);
                break;
            case ShopStatusDef.MODERATED:
                showShopModerated(shopInfo);
                break;
            case ShopStatusDef.NOT_ACTIVE:
                showShopNotActive(shopInfo);
                break;
            default:
                shopWarningTickerView.setVisibility(View.GONE);
        }
    }

    private void showShopClosed(ShopInfo shopInfo) {
        String shopCloseUntilString = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_DD_MM_YYYY,
                DateFormatUtils.FORMAT_D_MMMM_YYYY,
                shopInfo.getClosedInfo().getUntil());
        showShopStatusTicker(R.drawable.ic_shop_label_closed,
                shopWarningTickerView.getContext().getString(R.string.shop_page_header_shop_closed_info, shopCloseUntilString),
                shopInfo.getClosedInfo().getNote(),
                R.color.green_ticker);
    }

    private void showShopModerated(ShopInfo shopInfo) {
        showShopStatusTicker(R.drawable.ic_info_moderation,
                shopWarningTickerView.getContext().getString(R.string.shop_page_header_shop_in_moderation),
                shopWarningTickerView.getContext().getString(R.string.shop_page_header_closed_reason, shopInfo.getClosedInfo().getReason()),
                R.color.yellow_ticker);
    }

    private void showShopNotActive(ShopInfo shopInfo) {
        showShopStatusTicker(R.drawable.ic_info_inactive,
                shopWarningTickerView.getContext().getString(R.string.shop_page_header_shop_not_active_title),
                shopWarningTickerView.getContext().getString(R.string.shop_page_header_shop_not_active_description),
                R.color.yellow_ticker);
    }

    private void showShopStatusTicker(@DrawableRes int iconRes, String title, String description, @ColorRes int colorRes) {
        shopWarningTickerView.setIcon(iconRes);
        shopWarningTickerView.setTitle(title);
        shopWarningTickerView.setDescription(description);
        shopWarningTickerView.setTickerColor(ContextCompat.getColor(shopWarningTickerView.getContext(), colorRes));
        shopWarningTickerView.setAction(null, null);
        shopWarningTickerView.setVisibility(View.VISIBLE);
    }

    /**
     * Temporary solution to provide very small rating bar on shop page, hard to implement on various screen dimension
     *
     * @param rating
     * @return
     */
    @Deprecated
    @DrawableRes
    private int getRatingImageRes(int rating) {
        switch (rating) {
            case 1:
                return R.drawable.ic_rating_star_1;
            case 2:
                return R.drawable.ic_rating_star_2;
            case 3:
                return R.drawable.ic_rating_star_3;
            case 4:
                return R.drawable.ic_rating_star_4;
            case 5:
                return R.drawable.ic_rating_star_5;
            default:
                return R.drawable.ic_rating_star_0;
        }
    }
}
