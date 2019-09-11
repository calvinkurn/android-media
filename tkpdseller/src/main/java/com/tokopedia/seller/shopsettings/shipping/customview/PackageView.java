package com.tokopedia.seller.shopsettings.shipping.customview;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.seller.R;
import com.tokopedia.seller.shopsettings.shipping.fragment.EditShippingViewListener;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.Courier;

/**
 * Created by Kris on 6/6/2016.
 * TOKOPEDIA
 */
public class PackageView extends EditShippingCourierView<Courier,
        EditShippingViewListener>{

    LinearLayout checkBoxHolder;

    private EditShippingViewListener mainView;

    public PackageView(Context context) {
        super(context);
    }

    public PackageView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public PackageView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.shipping_courier_children_adapter;
    }

    @Override
    protected void bindView(View view) {
        checkBoxHolder = (LinearLayout) view.findViewById(R.id.checkbox_holder);
    }

    @Override
    public void renderData(@NonNull Courier courier, int courierIndex) {
        for(int serviceIndex = 0; serviceIndex < courier.services.size(); serviceIndex++){
            PackageViewCheckBox packageCheckBox = new PackageViewCheckBox(getContext());
            packageCheckBox.setViewListener(mainView);
            packageCheckBox.renderData(courier.services.get(serviceIndex), serviceIndex);
            packageCheckBox.setServiceCheckBoxListener(courierIndex);
            checkBoxHolder.addView(packageCheckBox);
        }
    }

    @Override
    public void setViewListener(EditShippingViewListener mainView) {
        this.mainView = mainView;
    }
}
