package com.tokopedia.seller.gmsubscribe.view.checkout.widget;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel.GMAutoSubscribeViewModel;

/**
 * Created by sebastianuskh on 1/30/17.
 */

public class AutoSubscribeViewHolder {


    private final AutoSubscribeViewHolderCallback callback;
    private final CheckBox checkbox;
    private final TextView title;
    private final TextView subtitle;
    private final LinearLayout detailLayout;
    private final TextView autoSubscribePackageTitle;
    private final TextView autoSubscribePackagePrice;
    private final TextView autoSubscribePackageNextSubscribe;
    private final TextView autoSubscribePackagePaymentMethod;
    private final LinearLayout autoSubscribeChangePackage;

    public AutoSubscribeViewHolder(AutoSubscribeViewHolderCallback callback, View view) {
        this.callback = callback;
        checkbox = (CheckBox) view.findViewById(R.id.check_box_to_open_auto_subscribe);
        checkbox.setOnCheckedChangeListener(getOpenAutoSubscribeView());
        title = (TextView) view.findViewById(R.id.open_auto_subscribe_title);
        subtitle = (TextView) view.findViewById(R.id.open_auto_subscribe_subtitle);
        detailLayout = (LinearLayout) view.findViewById(R.id.gm_subscribe_auto_detail);
        autoSubscribePackageTitle = (TextView) view.findViewById(R.id.text_view_current_auto_package_title);
        autoSubscribePackagePrice = (TextView) view.findViewById(R.id.text_view_current_auto_package_price);
        autoSubscribePackageNextSubscribe = (TextView) view.findViewById(R.id.text_view_current_auto_package_next_subscribe);
        autoSubscribePackagePaymentMethod = (TextView) view.findViewById(R.id.text_view_current_auto_package_payment_method);
        autoSubscribeChangePackage = (LinearLayout) view.findViewById(R.id.icon_change_gm_subscribe_auto);
        autoSubscribeChangePackage.setOnClickListener(getChangeAutoSubscribePackage());

    }

    public void setShowAutoSubscribeDetail(boolean isShow){
        int isVisible = isShow? View.VISIBLE : View.GONE;
        subtitle.setVisibility(isVisible);
        detailLayout.setVisibility(isVisible);
    }

    public View.OnClickListener getChangeAutoSubscribePackage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.changeAutoSubscribePackage();
            }
        };
    }

    public CompoundButton.OnCheckedChangeListener getOpenAutoSubscribeView() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if (callback.isAutoSubscribeUnselected()) {
                        callback.selectAutoSubscribePackageFirstTime();
                    } else {
                        callback.changeAutoSubscribePackage();
                    }
                } else {
                    setShowAutoSubscribeDetail(false);
                }
            }
        };
    }

    public void renderAutoSubscribeProduct(GMAutoSubscribeViewModel gmAutoSubscribeViewModel) {
        setShowAutoSubscribeDetail(true);
        renderView(gmAutoSubscribeViewModel);
    }

    private void renderView(GMAutoSubscribeViewModel gmAutoSubscribeViewModel) {
        autoSubscribePackageTitle.setText(gmAutoSubscribeViewModel.getTitle());
        autoSubscribePackagePrice.setText(gmAutoSubscribeViewModel.getPrice());
        autoSubscribePackageNextSubscribe.setText(gmAutoSubscribeViewModel.getNextAutoSubscribe());
        autoSubscribePackagePaymentMethod.setText(gmAutoSubscribeViewModel.getPaymentMethod());
    }
}
