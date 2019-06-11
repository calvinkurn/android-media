package com.tokopedia.seller.shopsettings.shipping;

import android.os.Bundle;
import android.view.WindowManager;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shopsettings.shipping.fragment.FragmentEditShipping;

@DeepLink({ApplinkConst.SELLER_SHIPPING_EDITOR})
public class EditShippingActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        inflateView(R.layout.activity_shipping_shop_editor);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.main_view, FragmentEditShipping.createInstance()).commit();
        }
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CONFIG_S_SHIPPING;
    }
}
