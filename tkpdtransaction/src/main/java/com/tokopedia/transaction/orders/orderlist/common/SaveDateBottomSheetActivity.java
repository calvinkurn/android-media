package com.tokopedia.transaction.orders.orderlist.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.design.component.BottomSheets;

public class SaveDateBottomSheetActivity extends BaseSimpleActivity implements SaveDateBottomSheet.DateFilterResult {
    public static final String START_DATE = "START_DATE";
    public static final String END_DATE = "END_DATE";

    public static Intent getInstance(Context context, String startDate, String endDate) {
        Intent intent = new Intent(context, SaveDateBottomSheetActivity.class);
        intent.putExtra(START_DATE, startDate);
        intent.putExtra(END_DATE, endDate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            SaveDateBottomSheet saveDateBottomSheet = SaveDateBottomSheet.newInstance(getIntent().getExtras());
            saveDateBottomSheet.show(getSupportFragmentManager(), "");
            saveDateBottomSheet.setDismissListener(new BottomSheets.BottomSheetDismissListener() {
                @Override
                public void onDismiss() {
                    SaveDateBottomSheetActivity.this.finish();
                }
            });
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
    }

    @Override
    public void setResult(String startDate, String endDate) {
        Intent i = new Intent();
        i.putExtra(START_DATE, startDate);
        i.putExtra(END_DATE, endDate);
        setResult(RESULT_OK, i);
    }
}
