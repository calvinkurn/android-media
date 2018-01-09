package com.tokopedia.transaction.bcaoneklik.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.transaction.R;

/**
 * @author Aghny A. Putra on 09/01/18
 */

public class CreditCardDetailActivity extends TActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.credit_card_detail_layout);
    }
}
