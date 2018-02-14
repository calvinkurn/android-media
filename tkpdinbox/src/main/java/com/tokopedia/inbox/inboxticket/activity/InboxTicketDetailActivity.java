package com.tokopedia.inbox.inboxticket.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.inbox.inboxticket.InboxTicketConstant;
import com.tokopedia.inbox.inboxticket.applink.InboxTicketAppLink;
import com.tokopedia.inbox.inboxticket.fragment.InboxTicketDetailFragment;
import com.tokopedia.inbox.inboxticket.fragment.InboxTicketFragment;
import com.tokopedia.inbox.inboxticket.intentservice.InboxTicketIntentService;
import com.tokopedia.inbox.inboxticket.intentservice.InboxTicketResultReceiver;
import com.tokopedia.inbox.inboxticket.presenter.InboxTicketDetailPresenter;
import com.tokopedia.inbox.inboxticket.presenter.InboxTicketDetailPresenterImpl;

/**
 * Created by Nisie on 4/22/16.
 */
public class InboxTicketDetailActivity extends BasePresenterActivity<InboxTicketDetailPresenter>
        implements InboxTicketDetailFragment.DoActionInboxTicketListener, InboxTicketConstant,
        InboxTicketResultReceiver.Receiver {

    public static final String PARAM_TICKET_ID = "ticket_id";

    InboxTicketResultReceiver mReceiver;

    @DeepLink(InboxTicketAppLink.CUSTOMER_CARE)
    public static TaskStackBuilder getCallingIntent(Context context, Bundle bundle) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Intent parentIntent = new Intent(context, InboxTicketActivity.class);
        Intent destinationIntent = new Intent(context, InboxTicketDetailActivity.class);
        String ticketId = bundle.getString(PARAM_TICKET_ID, "");
        destinationIntent.putExtra(PARAM_TICKET_ID, ticketId);
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(destinationIntent);
        return taskStackBuilder;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_INBOX_TICKET_DETAIL;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        presenter = new InboxTicketDetailPresenterImpl();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inbox_ticket_2;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentByTag(InboxTicketDetailFragment.class.getSimpleName());
        if (fragment != null && fragment.getActivity() == null) {
            finish();
            startActivity(getIntent());
        } else if (fragment == null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.add(R.id.container, InboxTicketDetailFragment.createInstance(getIntent().getExtras()), InboxTicketFragment.class.getSimpleName());
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void setViewListener() {
    }


    @Override
    protected void initVar() {
        mReceiver = new InboxTicketResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void sendRating(Bundle param) {
        InboxTicketIntentService.startAction(this,
                param, mReceiver, ACTION_SET_RATING);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);

        if (fragment != null) {
            switch (resultCode) {
                case STATUS_SUCCESS:
                    onReceiveResultSuccess(fragment, resultData);
                    break;
                case STATUS_ERROR:
                    onReceiveResultError(fragment, resultData);
                    break;
            }
        }
    }

    private void onReceiveResultError(Fragment fragment, Bundle resultData) {
        int type = resultData.getInt(EXTRA_TYPE, 0);
        switch (type) {
            case ACTION_SET_RATING:
                ((InboxTicketDetailFragment) fragment).onFailedSetRating(resultData);
                break;
            default:
                throw new UnsupportedOperationException("Invalid Type Action");
        }
    }

    private void onReceiveResultSuccess(Fragment fragment, Bundle resultData) {
        int type = resultData.getInt(EXTRA_TYPE, 0);
        switch (type) {
            case ACTION_SET_RATING:
                ((InboxTicketDetailFragment) fragment).onSuccessSetRating(resultData);
                break;
            default:
                throw new UnsupportedOperationException("Invalid Type Action");
        }
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
