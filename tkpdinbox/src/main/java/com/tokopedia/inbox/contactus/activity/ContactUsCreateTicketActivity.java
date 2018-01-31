package com.tokopedia.inbox.contactus.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.contactus.fragment.CreateTicketFormFragment;

public class ContactUsCreateTicketActivity extends BasePresenterActivity implements CreateTicketFormFragment.FinishContactUsListener {

    public static final String PARAM_TITLE = "PARAM_TITLE";
    public static final String PARAM_DESCRIPTION = "PARAM_DESCRIPTION";
    public static final String PARAM_DESCRIPTION_TITLE = "PARAM_DESCRIPTION_TITLE";
    public static final String PARAM_ATTACHMENT_TITLE = "PARAM_ATTACHMENT_TITLE";

    public static Intent getCallingIntent(Activity activity,
                                          String title,
                                          String solutionId,
                                          String invoiceId,
                                          String descriptionTitle,
                                          String attachmentTitle,
                                          String description) {
        Intent intent = new Intent(activity, ContactUsCreateTicketActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_TITLE, title);
        bundle.putString(ContactUsActivity.PARAM_SOLUTION_ID, solutionId);
        bundle.putString(ContactUsActivity.PARAM_INVOICE_ID, invoiceId);
        bundle.putString(PARAM_DESCRIPTION, description);
        bundle.putString(PARAM_DESCRIPTION_TITLE, descriptionTitle);
        bundle.putString(PARAM_ATTACHMENT_TITLE, attachmentTitle);
        intent.putExtras(bundle);
        return intent;
    }

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
    protected int getLayoutId() {
        return R.layout.activity_contact_us_create_ticket;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            bundle = new Bundle();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        CreateTicketFormFragment fragment;
        if (getFragmentManager().findFragmentByTag(CreateTicketFormFragment.class.getSimpleName()) == null) {
            fragment = CreateTicketFormFragment.createInstance(bundle);
        } else {
            fragment = (CreateTicketFormFragment) getFragmentManager().findFragmentByTag(CreateTicketFormFragment.class.getSimpleName());
        }

        fragmentTransaction.replace(R.id.main_view, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

        setTitle();
    }

    private void setTitle() {
        if (getIntent().getExtras() != null &&
                getIntent().getExtras().getString(PARAM_TITLE, "").length() > 0) {
            toolbar.setTitle(getIntent().getExtras().getString(PARAM_TITLE, ""));
        } else {
            toolbar.setTitle(R.string.title_activity_contact_us);
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
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public void onFinishCreateTicket() {
        Toast.makeText(this, R.string.title_contact_finish, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
