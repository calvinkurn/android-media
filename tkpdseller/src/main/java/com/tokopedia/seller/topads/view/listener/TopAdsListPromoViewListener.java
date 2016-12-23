package com.tokopedia.seller.topads.view.listener;

import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public interface TopAdsListPromoViewListener {
    void setActionMode(ActionMode actionMode);

    void setMenuInflater(Menu menu);

    void disableRefreshPull();

    boolean getActionOnSelectedMenu(ActionMode actionMode, MenuItem menuItem);

    void enableRefreshPull();

    void startSupportActionMode(ModalMultiSelectorCallback selectionMode);

    void setTitleMode(String title);

    void finishActionMode();

    void moveToDetail(int position);
}
