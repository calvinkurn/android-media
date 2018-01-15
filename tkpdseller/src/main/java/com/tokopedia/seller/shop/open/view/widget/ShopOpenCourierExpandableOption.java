package com.tokopedia.seller.shop.open.view.widget;

import android.animation.LayoutTransition;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.expandable.BaseExpandableOption;
import com.tokopedia.seller.R;
import com.tokopedia.seller.logistic.model.Courier;
import com.tokopedia.seller.logistic.model.CourierServiceModel;
import com.tokopedia.seller.shop.open.view.adapter.ShopOpenCourierAdapter;

import java.util.List;


public class ShopOpenCourierExpandableOption extends BaseExpandableOption implements ShopOpenCourierAdapter.OnShopServiceCourierAdapterListener {
    private ImageView ivIcon;
    private TextView tvTitle;
    private TextView tvDesc;
    private SwitchCompat switchCompat;
    private Courier courier;

    private boolean mEnabled;

    private OnShopCourierExpandableOptionListener onShopCourierExpandableOptionListener;

    public interface OnShopCourierExpandableOptionListener {
        void onDisabledHeaderClicked(Courier courier);
        void onCourierServiceInfoIconClicked(String title, String description);
    }

    public void setOnShopCourierExpandableOptionListener(OnShopCourierExpandableOptionListener onShopCourierExpandableOptionListener) {
        this.onShopCourierExpandableOptionListener = onShopCourierExpandableOptionListener;
    }

    private String logo;

    private ShopOpenCourierAdapter shopServiceCourierAdapter;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    public ShopOpenCourierExpandableOption(Context context) {
        super(context);
    }

    public ShopOpenCourierExpandableOption(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShopOpenCourierExpandableOption(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ShopOpenCourierExpandableOption(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {
        setHeaderLayoutRes(R.layout.item_shop_courier_expandable_header);
        setFooterLayoutRes(R.layout.item_shop_courier_expandable_child);
        shopServiceCourierAdapter = new ShopOpenCourierAdapter(getContext(), null, null, this);
        super.init();
    }

    @Override
    protected void initView(View view) {
        initRootLayout(view);

        ivIcon = view.findViewById(R.id.iv_icon);
        tvTitle = view.findViewById(R.id.tv_title);
        tvDesc = view.findViewById(R.id.tv_desc);
        switchCompat = view.findViewById(R.id.switch_button);

        final RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(shopServiceCourierAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        final Runnable expandRunnable = new Runnable() {
            @Override
            public void run() {
                setExpand(false);
            }
        };
        onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkedAllChild();
                    recyclerView.removeCallbacks(expandRunnable);
                    setExpand(true);
                } else {
                    uncheckedAllChild();
                    recyclerView.postDelayed(expandRunnable,300);
                }
            }
        };
        switchCompat.setOnCheckedChangeListener(onCheckedChangeListener);

        setChecked(isExpanded());
        setUITitle();
        setUILogo();
        setUIDescription();
    }

    private void checkedAllChild(){
        shopServiceCourierAdapter.checkAll();
    }

    private void uncheckedAllChild(){
        shopServiceCourierAdapter.unCheckAll();
    }

    public void setChecked(boolean checked) {
        if (switchCompat!=null) {
            switchCompat.setChecked(checked);
        }
    }

    public boolean isChecked(){
        return switchCompat.isChecked();
    }

    @Override
    public void checkGroupFromChild() {
        if (!switchCompat.isChecked()) {
            switchCompat.setOnCheckedChangeListener(null);
            switchCompat.setChecked(true);
            switchCompat.setOnCheckedChangeListener(onCheckedChangeListener);
        }
    }

    @Override
    public void unCheckGroupFromChild() {
        if (switchCompat.isChecked()) {
            switchCompat.setOnCheckedChangeListener(null);
            switchCompat.setChecked(false);
            switchCompat.setOnCheckedChangeListener(onCheckedChangeListener);
        }
    }

    @Override
    public void onInfoIconClicked(String title, String description) {
        if (onShopCourierExpandableOptionListener!= null) {
            onShopCourierExpandableOptionListener.onCourierServiceInfoIconClicked(title, description);
        }
    }

    private void initRootLayout(View view) {
        LayoutTransition layoutTransition = ((ViewGroup) view).getLayoutTransition();
        layoutTransition.disableTransitionType(LayoutTransition.CHANGE_APPEARING);
        layoutTransition.disableTransitionType(LayoutTransition.APPEARING);

        View vgRoot = view.findViewById(R.id.vg_root);
        if (vgRoot instanceof LinearLayout) {
            LinearLayout linearLayout = (LinearLayout) vgRoot;
            linearLayout.setShowDividers(SHOW_DIVIDER_MIDDLE);
            linearLayout.setDividerDrawable(ContextCompat.getDrawable(getContext(), R.drawable.line_divider));
        }
    }

    @Override
    public void setExpand(boolean isExpanded) {
        super.setExpand(isExpanded);
    }

    public void setCourier(Courier courier, boolean hasPinPointLocation) {
        this.courier = courier;
        setTitleText(courier.getName());
        setLogo(courier.getLogo());
        boolean isEnabled = (courier.isExpressCourierId() && hasPinPointLocation)
                || (!courier.isExpressCourierId() && courier.isAvailable());
        setEnabled(isEnabled);
        setChild(courier.getServices());
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
        if (mEnabled) {
            switchCompat.setEnabled(true);
            switchCompat.setVisibility(VISIBLE);
        } else {
            switchCompat.setEnabled(false);
            switchCompat.setVisibility(GONE);
        }
        setUIDescription();
        setUITitle();
    }

    @Override
    protected void onHeaderClicked() {
        if (mEnabled) {
            switchCompat.setChecked(!switchCompat.isChecked());
        } else {
            onDisabledHeaderClicked();
        }
    }

    private void onDisabledHeaderClicked(){
        if (onShopCourierExpandableOptionListener != null) {
            onShopCourierExpandableOptionListener.onDisabledHeaderClicked(courier);
        }
    }

    public void setChild(List<CourierServiceModel> courierServiceModelList) {
        shopServiceCourierAdapter.setCourierServiceModelList(courierServiceModelList);
        shopServiceCourierAdapter.notifyDataSetChanged();
    }

    public void setSelectedChild(List<String> selectedIds) {
        shopServiceCourierAdapter.setSelectedIds(selectedIds);
        shopServiceCourierAdapter.notifyDataSetChanged();
    }

    public List<String> getSelectedChild(){
        return shopServiceCourierAdapter.getSelectedIds();
    }

    @Override
    public void setTitleText(String titleText) {
        super.setTitleText(titleText);
        setUITitle();
    }

    public void setLogo(String logo) {
        this.logo = logo;
        setUILogo();
    }

    private void setUITitle() {
        if (tvTitle == null) {
            return;
        }
        if (mEnabled) {
            tvTitle.setTextColor(ContextCompat.getColor(getContext(),R.color.font_black_primary_70));
        } else {
            tvTitle.setTextColor(ContextCompat.getColor(getContext(),R.color.font_black_disabled_38));
        }
        if (TextUtils.isEmpty(titleText)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(titleText);
            tvTitle.setVisibility(View.VISIBLE);
        }
    }

    private void setUIDescription() {
        if (tvDesc == null) {
            return;
        }
        if (mEnabled) {
            tvDesc.setTextColor(ContextCompat.getColor(getContext(),R.color.font_black_secondary_54));
            tvDesc.setText(getContext().getString(R.string.shop_open_choose_delivery_packet));
        } else {
            tvDesc.setTextColor(ContextCompat.getColor(getContext(),R.color.font_black_disabled_38));
            tvDesc.setText(getContext().getString(R.string.shop_open_delivery_not_available));
        }
    }

    private void setUILogo() {
        if (ivIcon == null) {
            return;
        }
        if (TextUtils.isEmpty(titleText)) {
            ivIcon.setVisibility(View.GONE);
        } else {
            ImageHandler.LoadImage(ivIcon, logo);
            ivIcon.setVisibility(View.VISIBLE);
        }
    }

}
