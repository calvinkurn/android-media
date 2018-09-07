package com.tokopedia.inbox.rescenter.inbox.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.tokopedia.core.R;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.inbox.rescenter.inbox.activity.InboxResCenterActivity;
import com.tokopedia.inbox.rescenter.inbox.listener.ResCenterView;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import java.util.List;

/**
 * Created on 3/30/16.
 */
public class ResCenterImpl implements ResCenterPresenter {

    private final ResCenterView view;

    public ResCenterImpl(ResCenterView view) {
        this.view = view;
    }

    @Override
    public void setLocalyticFlow(@NonNull Context context) {
    }

    @Override
    public void initAnalytics(@NonNull Context context) {
    }

    @Override
    public void clearNotif(@NonNull Context context, @NonNull Intent intent) {
        if (intent.getBooleanExtra(Constants.EXTRA_FROM_PUSH, false)) {
            new NotificationModHandler(context).cancelNotif();
        }
    }

    @Override
    public void initFragmentList(@NonNull Context context, @NonNull List<InboxResCenterActivity.Model> list) {
        if (!isSellerApp()) {
            if (isHasShop(context)) {
                list.add(new InboxResCenterActivity.Model(TkpdState.InboxResCenter.RESO_ALL, context.getString(R.string.title_all_dispute)));
                list.add(new InboxResCenterActivity.Model(TkpdState.InboxResCenter.RESO_MINE, context.getString(R.string.title_my_dispute)));
                list.add(new InboxResCenterActivity.Model(TkpdState.InboxResCenter.RESO_BUYER, context.getString(R.string.title_buyer_dispute)));
            } else {
                list.add(new InboxResCenterActivity.Model(TkpdState.InboxResCenter.RESO_ALL, context.getString(R.string.title_all_dispute)));
            }
        } else {
            list.add(new InboxResCenterActivity.Model(TkpdState.InboxResCenter.RESO_BUYER, context.getString(R.string.title_buyer_dispute)));
        }
    }

    public boolean isSellerApp() {
        return view.getApplication().getClass().getSimpleName().equals("SellerMainApplication");
    }

    private boolean isHasShop(Context context) {
        return SessionHandler.isUserHasShop(context);
    }

    @Override
    public void onActionVar(Context context) {
        view.setAdapter();
        view.setTabLayout();
        view.setOffScreenPageLimit();
        setTabPosition(context);
    }

    @Override
    public void initView(Context context) {
        if (isSellerApp() || !isHasShop(context)) {
            view.getTabLayout().setVisibility(View.GONE);
        } else {
            view.getTabLayout().setVisibility(View.VISIBLE);
        }
    }

    private void setTabPosition(Context context) {
        if (isHasShop(context) && view.getBundleArguments() != null) {
            Bundle bundle = view.getBundleArguments();
            switch (bundle.getInt(InboxRouter.EXTRA_STATE_TAB_POSITION, 2)) {
                case 1:
                    // TkpdState.InboxResCenter.RESO_BUYER;
                    view.setTabPosition(2);
                    break;
                case 0:
                    // TkpdState.InboxResCenter.RESO_MINE;
                    view.setTabPosition(1);
                    break;
                default:
                    break;
            }
        }
    }
}
