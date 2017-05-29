package com.tokopedia.seller.topads.keyword.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;

import java.util.ArrayList;

/**
 * Created by hendry on 5/18/2017.
 */

public class TopAdsKeywordAddPositiveSummaryFragment extends AbsTopAdsKeywordAddSummaryFragment {

    public static TopAdsKeywordAddPositiveSummaryFragment newInstance(int groupId, String groupName) {
        TopAdsKeywordAddPositiveSummaryFragment fragment = new TopAdsKeywordAddPositiveSummaryFragment();
        fragment.setArguments(createBundle(groupId,groupName));
        return fragment;
    }

}
