package com.tokopedia.topads.keyword.view.listener;

import com.tokopedia.topads.keyword.view.adapter.TopAdsKeywordGroupListAdapter;

/**
 * Created by normansyahputa on 6/5/17.
 */

public interface TopAdsKeywordGroupListListener extends TopAdsKeywordGroupListAdapter.Listener {
    void resetSelection(int position);
}
