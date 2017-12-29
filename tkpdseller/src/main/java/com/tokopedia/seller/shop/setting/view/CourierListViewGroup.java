package com.tokopedia.seller.shop.setting.view;

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
import com.tokopedia.seller.shop.open.data.model.Courier;
import com.tokopedia.seller.shop.open.view.model.CourierServiceIdWrapper;

import java.util.ArrayList;
import java.util.List;

public class CourierListViewGroup extends LinearLayout implements ShopCourierExpandableOption.OnDisabledHeaderClickedListener {
    private List<Courier> courierList;

    private ShopCourierExpandableOption.OnDisabledHeaderClickedListener onDisabledHeaderClickedListener;
    public void setOnDisabledHeaderClickedListener(ShopCourierExpandableOption.OnDisabledHeaderClickedListener
                                                           onDisabledHeaderClickedListener) {
        this.onDisabledHeaderClickedListener = onDisabledHeaderClickedListener;
    }

    public CourierListViewGroup(Context context) {
        super(context);
        init();
    }

    public CourierListViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CourierListViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CourierListViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setLayoutTransition(new LayoutTransition());
        LayoutTransition layoutTransition = getLayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
    }

    public void setCourierList(List<Courier> courierList, CourierServiceIdWrapper selectedCourierService) {
        if (courierList == null) {
            this.courierList = new ArrayList<>();
        } else {
            this.courierList = courierList;
        }
        setUIBasedOnCourierList(selectedCourierService);
    }

    private void setUIBasedOnCourierList(CourierServiceIdWrapper selectedCourierServiceList) {
        this.removeAllViews();
        for (int i = 0, sizei = courierList.size(); i < sizei; i++) {
            Courier courier = courierList.get(i);
            ShopCourierExpandableOption shopCourierExpandableOption =
                    (ShopCourierExpandableOption) LayoutInflater.from(getContext()).inflate(R.layout.item_shop_courier, this, false);
            shopCourierExpandableOption.setTitleText(courier.getName());
            shopCourierExpandableOption.setLogo(courier.getLogo());
            shopCourierExpandableOption.setEnabled(courier.isAvailable());
            shopCourierExpandableOption.setChild(courier.getServices());
            shopCourierExpandableOption.setOnDisabledHeaderClickedListener(this);
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
            if (child instanceof ShopCourierExpandableOption) {
                ShopCourierExpandableOption shopCourierExpandableOption = (ShopCourierExpandableOption) child;
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
    public void onDisabledHeaderClicked() {
        if (onDisabledHeaderClickedListener!= null) {
            onDisabledHeaderClickedListener.onDisabledHeaderClicked();
        }
    }
}
