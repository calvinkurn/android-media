package com.tokopedia.transaction.purchase.detail.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.transaction.purchase.detail.fragment.BookingCodeFragment;
import com.tokopedia.transaction.purchase.detail.model.detail.response.OnlineBooking;

public class BookingCodeActivity extends BaseSimpleActivity {

    public static Intent createInstance(Context context, OnlineBooking data) {
        Intent i = new Intent(context, BookingCodeActivity.class);
        i.putExtra("data", data);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", getIntent().getExtras().getParcelable("data"));
        BookingCodeFragment fragment = new BookingCodeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }
}
