package com.tokopedia.seller.topads.keyword.view.fragment;

import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.widget.SpinnerTextView;
import com.tokopedia.seller.topads.keyword.view.model.TopAdsKeywordEditDetailViewModel;

/**
 * @author sebastianuskh on 5/26/17.
 */

public class TopAdsKeywordEditDetailPositiveFragment extends TopAdsKeywordEditDetailFragment {

    public static TopAdsKeywordEditDetailPositiveFragment createInstance(TopAdsKeywordEditDetailViewModel model) {
        TopAdsKeywordEditDetailPositiveFragment fragment = new TopAdsKeywordEditDetailPositiveFragment();
        fragment.setArguments(createArguments(model));
        return fragment;
    }

}
