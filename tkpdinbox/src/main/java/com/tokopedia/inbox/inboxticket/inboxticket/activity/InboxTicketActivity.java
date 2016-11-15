package com.tokopedia.inbox.inboxticket.inboxticket.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.inbox.inboxticket.inboxticket.fragment.InboxTicketFragment;
import com.tokopedia.inbox.inboxticket.inboxticket.presenter.InboxTicketPresenter;
import com.tokopedia.inbox.inboxticket.inboxticket.presenter.InboxTicketPresenterImpl;
import com.tokopedia.core.var.TkpdState;

/**
 * Created by Nisie on 4/21/16.
 */
public class InboxTicketActivity extends DrawerPresenterActivity<InboxTicketPresenter> {

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_INBOX_TICKET;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        presenter = new InboxTicketPresenterImpl();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inbox_ticket_2;
    }

    @Override
    protected void initView() {
        super.initView();
        if (getFragmentManager().findFragmentByTag(InboxTicketFragment.class.getSimpleName()) == null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.add(R.id.container, InboxTicketFragment.createInstance(), InboxTicketFragment.class.getSimpleName());
            fragmentTransaction.commit();
        }
    }

    @Override
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.INBOX_TICKET;
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
