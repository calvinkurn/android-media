package com.tokopedia.otp.phoneverification.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.session.R;

import butterknife.BindView;

/**
 * Created by nisie on 2/23/17.
 */

public class ChangePhoneNumberActivity extends BasePresenterActivity {

    public static final int ACTION_CHANGE_PHONE_NUMBER = 111;
    public static final String EXTRA_PHONE_NUMBER = "EXTRA_PHONE_NUMBER";

    @BindView(R2.id.phone_number)
    EditText phoneNumberEditText;

    @BindView(R2.id.change_phone_number_button)
    TextView changePhoneNumberButton;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        getPhoneVerificationUtil().setHasShown(true);
    }

    @Override
    protected void onResume() {
        getPhoneVerificationUtil().setHasShown(true);
        super.onResume();
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
        return R.layout.activity_change_phone_number;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void setViewListener() {
        changePhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra(EXTRA_PHONE_NUMBER, phoneNumberEditText.getText().toString());
                setResult(RESULT_OK, intent);
            }
        });
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    public static Intent getChangePhoneNumberIntent(Context context) {
        Intent intent = new Intent(context, ChangePhoneNumberActivity.class);
        return intent;
    }
}
