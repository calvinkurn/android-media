package com.tokopedia.topads.common.view.listener;

import com.tokopedia.seller.base.view.listener.BaseListViewListener;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;

/**
 * Created by normansyahputa on 10/25/17.
 */

public interface TopAdsSuggestionViewListener<T> extends BaseListViewListener<T> {
    void onSuggestionSuccess(GetSuggestionResponse s);
}
