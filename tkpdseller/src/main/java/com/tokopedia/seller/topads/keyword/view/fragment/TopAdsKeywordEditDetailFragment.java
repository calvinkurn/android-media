package com.tokopedia.seller.topads.keyword.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.widget.SpinnerTextView;

/**
 * @author sebastianuskh on 5/23/17.
 */

public abstract class TopAdsKeywordEditDetailFragment extends BaseDaggerFragment{
    public static final String TAG = "TopAdsKeywordEditDetailFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_ads_keyword_edit_detail, container, false);
//        SpinnerTextView spinnerTextView = ((SpinnerTextView) view.findViewById(R.id.spinner_text_view_top_ads_keyword_type));
//        spinnerTextView.setEntries(getResources().getStringArray(R.array.top_ads_keyword_type_list_names));
//        spinnerTextView.setValues(getResources().getStringArray(R.array.top_ads_keyword_type_list_values));
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
