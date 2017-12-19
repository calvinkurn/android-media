package com.tokopedia.seller.shop.open.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.shop.open.view.ShopOpenStepperModel;
import com.tokopedia.seller.shop.setting.view.fragment.ShopSettingLocationFragment;
import com.tokopedia.seller.shop.setting.view.model.ShopSettingLocationModel;


/**
 * @author normansyahputa
 */
public class ShopOpenMandatoryLocationFragment extends BaseDaggerFragment {

    protected ShopOpenStepperModel stepperModel;
    protected StepperListener stepperListener;

    public static ShopOpenMandatoryLocationFragment getInstance(){
        return new ShopOpenMandatoryLocationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shop_open_location, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StepperListener) {
            this.stepperListener = (StepperListener) context;
        }
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
