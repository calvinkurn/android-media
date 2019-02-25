package com.tokopedia.transaction.orders.orderlist.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.design.component.BottomSheets;

public class SaveDateBottomSheetActivity extends BaseSimpleActivity implements SaveDateBottomSheet.DateFilterResult, SurveyBottomSheet.SurveyResult {
    public static final String START_DATE = "START_DATE";
    public static final String END_DATE = "END_DATE";
    public static final String DATE_OR_SURVEY = "1";
    public static final String SURVEY_RATING = "1";
    public static final String SURVEY_COMMENT = "good";

    public static Intent getDateInstance(Context context, String startDate, String endDate) {
        Intent intent = new Intent(context, SaveDateBottomSheetActivity.class);
        intent.putExtra(START_DATE, startDate);
        intent.putExtra(END_DATE, endDate);
        return intent;
    }


    public static Intent getSurveyInstance(Context context, String dateOrSurvey) {
        Intent intent = new Intent(context, SaveDateBottomSheetActivity.class);
        intent.putExtra(DATE_OR_SURVEY, dateOrSurvey);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            if (getIntent().getStringExtra(DATE_OR_SURVEY).equals("2")) {
                SurveyBottomSheet surveyBottomSheet = new SurveyBottomSheet();
                surveyBottomSheet.show(getSupportFragmentManager(), "");
                surveyBottomSheet.setDismissListener(new BottomSheets.BottomSheetDismissListener() {
                    @Override
                    public void onDismiss() {
                        SaveDateBottomSheetActivity.this.finish();
                    }
                });
            } else {
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

    @Override
    public void setSurveyResult(int rating, String surveyComment) {
        Intent intent = new Intent();
        intent.putExtra(SURVEY_RATING, rating);
        if (!TextUtils.isEmpty(surveyComment)) {
            intent.putExtra(SURVEY_COMMENT, surveyComment);
        } else {
            intent.putExtra(SURVEY_COMMENT, "");
        }
        setResult(RESULT_OK, intent);
    }
}
