package com.tokopedia.seller.shop.open.view.widget;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.seller.R;
import com.tokopedia.seller.logistic.model.Courier;
import com.tokopedia.seller.shop.open.view.model.CourierServiceIdWrapper;

import java.util.ArrayList;
import java.util.List;

public class ShopOpenCourierListViewGroup extends LinearLayout implements ShopOpenCourierExpandableOption.OnShopCourierExpandableOptionListener {
    private List<Courier> courierList;
    private boolean hasPinPointLocation;

    private ShopOpenCourierExpandableOption.OnShopCourierExpandableOptionListener onShopCourierExpandableOptionListener;

    public void setOnShopCourierExpandableOptionListener(ShopOpenCourierExpandableOption.OnShopCourierExpandableOptionListener
                                                                 onShopCourierExpandableOptionListener) {
        this.onShopCourierExpandableOptionListener = onShopCourierExpandableOptionListener;
    }

    public ShopOpenCourierListViewGroup(Context context) {
        super(context);
        init();
    }

    public ShopOpenCourierListViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShopOpenCourierListViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ShopOpenCourierListViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setLayoutTransition(new LayoutTransition());
        LayoutTransition layoutTransition = getLayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
    }

    public void setCourierList(List<Courier> courierList, CourierServiceIdWrapper selectedCourierService, boolean hasPinPointLocation) {
        if (courierList == null) {
            this.courierList = new ArrayList<>();
        } else {
            this.courierList = courierList;
        }
        this.hasPinPointLocation = hasPinPointLocation;
        setUIBasedOnCourierList(selectedCourierService);
    }

    private void setUIBasedOnCourierList(CourierServiceIdWrapper selectedCourierServiceList) {
        this.removeAllViews();
        for (int i = 0, sizei = courierList.size(); i < sizei; i++) {
            Courier courier = courierList.get(i);
            ShopOpenCourierExpandableOption shopCourierExpandableOption =
                    (ShopOpenCourierExpandableOption) LayoutInflater.from(getContext()).inflate(R.layout.item_shop_courier, this, false);
            shopCourierExpandableOption.setCourier(courier, hasPinPointLocation);
            shopCourierExpandableOption.setOnShopCourierExpandableOptionListener(this);
            if (selectedCourierServiceList != null && selectedCourierServiceList.contains(courier.getId())) {
                shopCourierExpandableOption.setChecked(true);
                shopCourierExpandableOption.setSelectedChild(
                        selectedCourierServiceList.getCourierServiceIdList(courier.getId()));
            } else {
                shopCourierExpandableOption.setChecked(false);
            }
            this.addView(shopCourierExpandableOption);
        }

    }

    public CourierServiceIdWrapper getSelectedCourierList() {
        CourierServiceIdWrapper courierServiceIdWrapper = new CourierServiceIdWrapper();
        for (int i = 0, sizei = this.getChildCount(); i < sizei; i++) {
            View child = this.getChildAt(i);
            if (child instanceof ShopOpenCourierExpandableOption) {
                ShopOpenCourierExpandableOption shopCourierExpandableOption = (ShopOpenCourierExpandableOption) child;
                boolean isChecked = shopCourierExpandableOption.isChecked();
                if (isChecked) {
                    courierServiceIdWrapper.add(courierList.get(i).getId(),
                            shopCourierExpandableOption.getSelectedChild());
                }
            }
        }
        return courierServiceIdWrapper;
    }

    @Override
    public void onDisabledHeaderClicked(Courier courier) {
        if (onShopCourierExpandableOptionListener != null) {
            onShopCourierExpandableOptionListener.onDisabledHeaderClicked(courier);
        }
    }

    @Override
    public void onCourierServiceInfoIconClicked(String title, String description) {
        if (onShopCourierExpandableOptionListener != null) {
            onShopCourierExpandableOptionListener.onCourierServiceInfoIconClicked(title, description);
        }
    }
}
