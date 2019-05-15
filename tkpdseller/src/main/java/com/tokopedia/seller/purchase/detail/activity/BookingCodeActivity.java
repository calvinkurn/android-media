package com.tokopedia.seller.purchase.detail.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.purchase.detail.fragment.BookingCodeFragment;
import com.tokopedia.seller.purchase.detail.model.detail.viewmodel.BookingCodeData;

public class BookingCodeActivity extends BaseSimpleActivity {

    public static final String JOB_CODE_EXTRA = "job_parcel";

    public static Intent createInstance(Context context, BookingCodeData data) {
        Intent intent = new Intent(context, BookingCodeActivity.class);
        intent.putExtra(JOB_CODE_EXTRA, data);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        BookingCodeFragment fragment = new BookingCodeFragment();
        if(getIntent().getExtras() != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(JOB_CODE_EXTRA, getIntent().getExtras().getParcelable(JOB_CODE_EXTRA));
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }
}
