package com.tokopedia.inbox.attachinvoice.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.inbox.attachinvoice.view.AttachInvoiceContract;
import com.tokopedia.inbox.attachinvoice.view.fragment.AttachInvoiceFragment;
import com.tokopedia.inbox.attachproduct.view.activity.AttachProductActivity;
import com.tokopedia.inbox.attachproduct.view.fragment.AttachProductFragment;
import com.tokopedia.inbox.attachproduct.view.resultmodel.ResultProduct;

import java.util.ArrayList;

/**
 * Created by Hendri on 22/03/18.
 */

public class AttachInvoiceActivity extends BaseSimpleActivity implements AttachInvoiceContract.Activity {
    public static String TOKOPEDIA_ATTACH_INVOICE_USER_ID_KEY = "";
    public static final int TOKOPEDIA_ATTACH_INVOICE_REQ_CODE = 114;
    public static final int TOKOPEDIA_ATTACH_INVOICE_RESULT_CODE_OK = 325;

    public static Intent createInstance(Context context, String userId) {
        Intent intent = new Intent(context, AttachInvoiceActivity.class);
        intent.putExtra(TOKOPEDIA_ATTACH_INVOICE_USER_ID_KEY,userId);
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
        return "7977933";
    }

    @Override
    public void finishActivityWithResult(ArrayList<ResultProduct> products) {

    }
}
