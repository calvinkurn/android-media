package com.tokopedia.tkpd.tkpdreputation.productreview.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.tkpd.tkpdreputation.R;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class ProductReviewFragment extends BaseDaggerFragment {
    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_review, container, false);
        return view;
    }
}
