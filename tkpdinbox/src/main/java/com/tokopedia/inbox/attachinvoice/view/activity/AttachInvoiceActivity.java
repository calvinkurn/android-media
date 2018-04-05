package com.tokopedia.inbox.attachinvoice.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.attachinvoice.di.DaggerAttachInvoiceComponent;
import com.tokopedia.inbox.attachinvoice.view.AttachInvoiceContract;
import com.tokopedia.inbox.attachinvoice.view.fragment.AttachInvoiceFragment;
import com.tokopedia.inbox.attachproduct.view.activity.AttachProductActivity;
import com.tokopedia.inbox.attachproduct.view.fragment.AttachProductFragment;
import com.tokopedia.inbox.attachproduct.view.resultmodel.ResultProduct;

import java.util.ArrayList;

/**
 * Created by Hendri on 22/03/18.
 */

public class AttachInvoiceActivity extends BaseSimpleActivity implements AttachInvoiceContract.Activity, HasComponent {
    public static String TOKOPEDIA_ATTACH_INVOICE_USER_ID_KEY = "ATTACH_INVOICE_USER_ID";
    public static String TOKOPEDIA_ATTACH_INVOICE_MSG_ID_KEY = "ATTACH_INVOICE_MSG_ID";
    public static final int TOKOPEDIA_ATTACH_INVOICE_REQ_CODE = 114;
    public static final int TOKOPEDIA_ATTACH_INVOICE_RESULT_CODE_OK = 325;
    public static String TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY = "SELECTED_INVOICE";
    public static Intent createInstance(Context context, String userId, int messageId) {
        Intent intent = new Intent(context, AttachInvoiceActivity.class);
        intent.putExtra(TOKOPEDIA_ATTACH_INVOICE_USER_ID_KEY,userId);
        intent.putExtra(TOKOPEDIA_ATTACH_INVOICE_MSG_ID_KEY,messageId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if(fragment != null){
            return fragment;
        }else{
            fragment = AttachInvoiceFragment.newInstance(this);
            return fragment;
        }
    }

    @Override
    public String getUserId() {
        String userId = getIntent().getStringExtra(TOKOPEDIA_ATTACH_INVOICE_USER_ID_KEY);
        return userId;
    }

    @Override
    public int getMessageId() {
        int msgId = 0;
        if(getIntent().hasExtra(TOKOPEDIA_ATTACH_INVOICE_MSG_ID_KEY))
        {
            msgId = getIntent().getIntExtra(TOKOPEDIA_ATTACH_INVOICE_MSG_ID_KEY,0);
        }
        return msgId;
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        setTitle(getString(R.string.string_attach_invoice_activity_title));
        super.setupLayout(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_white_toolbar_drop_shadow));
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_close_default));
        toolbar.setSubtitleTextAppearance(this,R.style.AttachProductToolbarSubTitle_SansSerif);
        toolbar.setTitleTextAppearance(this,R.style.AttachProductToolbarTitle_SansSerif);
    }

    @Override
    public AppComponent getComponent() {
        return ((MainApplication)getApplication()).getAppComponent();
    }
}
