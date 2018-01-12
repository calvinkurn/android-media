package com.tokopedia.transaction.purchase.detail.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.WrongProductPriceWeightEditable;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public class RejectOrderProductPriceWeightEditFragment extends TkpdFragment{

    private static final String EDITABLE_EXTRA = "EDITABLE_EXTRA";

    public static RejectOrderProductPriceWeightEditFragment createFragment(
            WrongProductPriceWeightEditable editable
    ) {
        RejectOrderProductPriceWeightEditFragment fragment = new RejectOrderProductPriceWeightEditFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EDITABLE_EXTRA, editable);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        WrongProductPriceWeightEditable editable = getArguments()
                .getParcelable(EDITABLE_EXTRA);
        View view = inflater.inflate(R.layout.order_reject_price_weight_edit_page, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected String getScreenName() {
        return null;
    }


}
