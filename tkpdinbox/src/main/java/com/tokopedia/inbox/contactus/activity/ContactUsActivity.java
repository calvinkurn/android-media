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
import com.tokopedia.inbox.contactus.ContactUsConstant;
import com.tokopedia.inbox.contactus.fragment.ContactUsCategoryFragment;
import com.tokopedia.inbox.contactus.fragment.ContactUsFaqFragment;
import com.tokopedia.inbox.contactus.fragment.ContactUsFaqFragment.ContactUsFaqListener;
import com.tokopedia.inbox.contactus.fragment.CreateTicketFormFragment;
import com.tokopedia.core.home.ParentIndexHome;

import java.util.ArrayList;

/**
 * Created by nisie on 8/12/16.
 */
public class ContactUsActivity extends BasePresenterActivity implements
        ContactUsFaqListener, ContactUsCategoryFragment.ContactUsCategoryFragmentListener,
        CreateTicketFormFragment.FinishContactUsListener,
        ContactUsConstant {

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
    protected String getScreenName() {
        return AppScreen.SCREEN_CONTACT_US;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact_us;
    }

    @Override
    protected void initView() {

        if (getIntent().getBooleanExtra(PARAM_REDIRECT,false)) {
            onGoToCreateTicket();
        }else{
            Bundle bundle = getIntent().getExtras();
            if(bundle == null )
                bundle = new Bundle();
            ContactUsFaqFragment fragment = ContactUsFaqFragment.createInstance(bundle);
            listener = fragment.getBackButtonListener();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.add(R.id.main_view, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.commit();
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
    public void onGoToCreateTicket() {
        ContactUsCategoryFragment fragment = ContactUsCategoryFragment.createInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right);
        transaction.add(R.id.main_view, fragment, "second");
        transaction.addToBackStack("secondStack");
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }else if (listener!= null && listener.canGoBack()){
            listener.onBackPressed();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onOpenWebView(String url) {

        Bundle bundle = new Bundle();
        bundle.putString(PARAM_URL, url);
        ContactUsFaqFragment fragment = ContactUsFaqFragment.createInstance(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right);
        fragmentTransaction.add(R.id.main_view, fragment, "second");
        fragmentTransaction.addToBackStack("secondStack");
        fragmentTransaction.commit();

    }

    @Override
    public void onOpenContactUsTicketForm(int lastCatId, ArrayList<String> path) {
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_LAST_CATEGORY_ID, lastCatId);
        bundle.putStringArrayList(PARAM_PATH,path);
        CreateTicketFormFragment fragment = CreateTicketFormFragment.createInstance(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right);
        transaction.add(R.id.main_view, fragment, "second");
        transaction.addToBackStack("secondStack");
        transaction.commit();
    }

    @Override
    public void onFinishCreateTicket() {
        CommonUtils.UniversalToast(this,getString(R.string.title_contact_finish));
        Intent intent = new Intent(this,ParentIndexHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
