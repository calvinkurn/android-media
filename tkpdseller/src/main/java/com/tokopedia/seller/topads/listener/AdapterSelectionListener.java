package com.tokopedia.seller.topads.listener;

/**
 * Created by normansyahputa on 2/13/17.
 */

public interface AdapterSelectionListener<T> {
    void onChecked(int position, T data);

    void onUnChecked(int position, T data);
}
