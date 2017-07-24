package com.tokopedia.transaction.bcaoneklik;

import android.os.Bundle;

import com.bca.xco.widget.BCARegistrasiXCOWidget;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.R;

/**
 * Created by kris on 7/21/17. Tokopedia
 */

public class BcaOneClickActivity extends TActivity {

    private BCARegistrasiXCOWidget bcaRegistrasiXCOWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.bca_one_click_layout);
        bcaRegistrasiXCOWidget = (BCARegistrasiXCOWidget)
                findViewById(R.id.bca_registration_widget);
        bcaRegistrasiXCOWidget.openWidget(getIntent().getExtras().getString("access_token"),
                "540fdef7-18c3-41d0-8596-242f1c59ce29",
                "dc88dfc6-837b-44bb-b767-d168e17e80cd",
                SessionHandler.getLoginID(this), "61005");

    }
}
