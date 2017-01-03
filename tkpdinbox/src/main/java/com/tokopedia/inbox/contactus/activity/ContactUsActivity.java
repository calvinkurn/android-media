package com.tokopedia.inbox.contactus.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.inbox.contactus.ContactUsConstant;
import com.tokopedia.inbox.contactus.fragment.ContactUsFaqFragment;
import com.tokopedia.inbox.contactus.fragment.ContactUsFaqFragment.ContactUsFaqListener;
import com.tokopedia.inbox.contactus.fragment.CreateTicketFormFragment;

/**
 * Created by nisie on 8/12/16.
 */
public class ContactUsActivity extends BasePresenterActivity implements
        ContactUsFaqListener,
        CreateTicketFormFragment.FinishContactUsListener,
        ContactUsConstant {

    public static final String PARAM_SOLUTION_ID = "PARAM_SOLUTION_ID";
    public static final String PARAM_ORDER_ID = "PARAM_ORDER_ID";
    public static final String PARAM_TAG = "PARAM_TAG";


    public interface BackButtonListener {
        void onBackPressed();

        boolean canGoBack();
    }

    private BackButtonListener listener;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CONTACT_US;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact_us;
    }

    @Override
    protected void initView() {

        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            bundle = new Bundle();
        if (bundle.getString(PARAM_URL, "").equals("")) {
            ContactUsFaqFragment fragment = ContactUsFaqFragment.createInstance(bundle);
            listener = fragment.getBackButtonListener();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.add(R.id.main_view, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.addToBackStack(ContactUsFaqFragment.class.getSimpleName());
            fragmentTransaction.commit();
        } else {
            bundle.putString(PARAM_SOLUTION_ID, Uri.parse(bundle.getString(PARAM_URL)).getQueryParameter("solution_id"));
            onGoToCreateTicket(bundle);
        }
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

    @Override
    public void onGoToCreateTicket(Bundle bundle) {
        CreateTicketFormFragment fragment = CreateTicketFormFragment.createInstance(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right);
        transaction.add(R.id.main_view, fragment, "second");
        transaction.addToBackStack(CreateTicketFormFragment.class.getSimpleName());
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else if (listener != null && listener.canGoBack()) {
            listener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFinishCreateTicket() {
        CommonUtils.UniversalToast(this, getString(R.string.title_contact_finish));
        if (GlobalConfig.isSellerApp()) {
            Intent intent = SellerAppRouter.getSellerHomeActivity(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Intent intent = HomeRouter.getHomeActivity(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}