package com.tokopedia.seller.shop.setting.view;

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
import com.tokopedia.seller.shop.open.data.model.CourierServiceModel;
import com.tokopedia.seller.shop.setting.view.adapter.ShopServiceCourierAdapter;

import java.util.List;

/**
 * Created by nakama on 22/12/17.
 */

public class ShopCourierExpandableOption extends BaseExpandableOption implements ShopServiceCourierAdapter.OnShopServiceCourierAdapterListener {
    private ImageView ivIcon;
    private TextView tvTitle;
    private TextView tvDesc;
    private SwitchCompat switchCompat;

    private String logo;

    private ShopServiceCourierAdapter shopServiceCourierAdapter;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    public ShopCourierExpandableOption(Context context) {
        super(context);
    }

    public ShopCourierExpandableOption(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShopCourierExpandableOption(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ShopCourierExpandableOption(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {
        setHeaderLayoutRes(R.layout.item_shop_courier_expandable_header);
        setFooterLayoutRes(R.layout.item_shop_courier_expandable_child);
        shopServiceCourierAdapter = new ShopServiceCourierAdapter(getContext(), null, null, this);
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

        onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkedAllChild();
                    setExpand(true);
                } else {
                    uncheckedAllChild();
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setExpand(false);
                        }
                    },300);
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

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        switchCompat.setEnabled(enabled);
        setUIDescription();
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
        if (isEnabled()) {
            tvDesc.setText(getContext().getString(R.string.choose_delivery_packet));
        } else {
            tvDesc.setText(getContext().getString(R.string.delivery_not_avaible));
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
