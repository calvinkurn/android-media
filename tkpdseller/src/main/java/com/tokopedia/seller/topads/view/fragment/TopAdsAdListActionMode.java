package com.tokopedia.seller.topads.view.fragment;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tokopedia.seller.R;

/**
 * Created by Nathaniel on 12/29/2016.
 */

public class TopAdsAdListActionMode implements ActionMode.Callback {

    public interface Callback {
        void onActionTurnOn();

        void onActionTurnOff();

        void onActionModeDestroyed();
    }

    private Callback callback;
    private ActionMode actionMode;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setTitle(String title) {
        actionMode.setTitle(title);
    }

    public void finish() {
        actionMode.finish();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.promo_topads_action, menu);
        this.actionMode = mode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.action_turn_on) {
            if (callback != null) {
                callback.onActionTurnOn();
            }
        } else if (item.getItemId() == R.id.action_turn_off) {
            if (callback != null) {
                callback.onActionTurnOff();
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        if (callback != null) {
            callback.onActionModeDestroyed();
        }
    }
}