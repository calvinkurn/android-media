package com.tokopedia.ride.bookingride.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;
import com.tokopedia.ride.bookingride.view.fragment.AddCreditCardFragment;
import com.tokopedia.ride.bookingride.view.fragment.EditDeleteCreditCardFragment;

/**
 * Created by sandeepgoyal on 28/09/17.
 */

public class EditDeleteCreditCardActivity extends BaseActivity{


    public static final String CARD_DETIALS_KEY = "CardDetails";
    public static Intent getCallingActivity(Activity activity, PaymentMethodViewModel paymentMethodViewModel) {
        Intent intent = new Intent(activity, EditDeleteCreditCardActivity.class);
        intent.putExtra(CARD_DETIALS_KEY,paymentMethodViewModel);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_credit_card);

        PaymentMethodViewModel paymentMethodViewModel = (PaymentMethodViewModel) getIntent().getParcelableExtra(CARD_DETIALS_KEY);
        setupToolbar(paymentMethodViewModel.getType());
        EditDeleteCreditCardFragment fragment = EditDeleteCreditCardFragment.newInstance(paymentMethodViewModel);
        replaceFragment(R.id.fl_container, fragment);
    }

    private void replaceFragment(int containerViewId, Fragment fragment) {
        if (!isFinishing()) {
            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerViewId, fragment);
            fragmentTransaction.commit();
        }
    }

    private void setupToolbar(String title) {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(title);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            invalidateOptionsMenu();
        }
    }

}
