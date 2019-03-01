package com.tokopedia.transaction.orders.orderlist.common;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.transaction.R;

public class SurveyBottomSheet extends BottomSheets implements View.OnClickListener {

    public static final String TITLE = "Beri Tanggapan Anda";

    private ImageView veryBadRating, badRating, averageRating, goodRating, veryGoodRating;
    private TextView submitBtn;
    private EditText surveyReason;

    private int rating;
    private String comment;
    SurveyResult surveyResult;



    public SurveyBottomSheet() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        surveyResult = (SurveyResult)context;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.bom_survey_request;
    }

    @Override
    public void initView(View view) {
        veryBadRating = view.findViewById(R.id.very_bad_rating);
        badRating = view.findViewById(R.id.bad_rating);
        averageRating = view.findViewById(R.id.average_rating);
        goodRating = view.findViewById(R.id.good_rating);
        veryGoodRating = view.findViewById(R.id.very_good_rating);
        submitBtn = view.findViewById(R.id.submit_survey);

        surveyReason = view.findViewById(R.id.survey_comment);

        veryBadRating.setOnClickListener(this);
        badRating.setOnClickListener(this);
        averageRating.setOnClickListener(this);
        goodRating.setOnClickListener(this);
        veryGoodRating.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }

    @Override
    protected String title() {
        return TITLE;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.very_bad_rating) {
            veryBadRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_main_green), android.graphics.PorterDuff.Mode.SRC_IN);
            badRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            averageRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            goodRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            veryGoodRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            rating = 1;

        } else if (i == R.id.bad_rating) {
            veryBadRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            badRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_main_green), android.graphics.PorterDuff.Mode.SRC_IN);
            averageRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            goodRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            veryGoodRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), PorterDuff.Mode.SRC_IN);
            rating = 2;

        } else if (i == R.id.average_rating) {
            veryBadRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            averageRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_main_green), android.graphics.PorterDuff.Mode.SRC_IN);
            badRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            goodRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            veryGoodRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), PorterDuff.Mode.SRC_IN);
            rating = 3;

        } else if (i == R.id.good_rating) {
            veryBadRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            goodRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_main_green), android.graphics.PorterDuff.Mode.SRC_IN);
            averageRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            badRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            veryGoodRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), PorterDuff.Mode.SRC_IN);
            rating = 4;

        } else if (i == R.id.very_good_rating) {
            veryBadRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            veryGoodRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_main_green), android.graphics.PorterDuff.Mode.SRC_IN);
            averageRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            goodRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            badRating.setColorFilter(ContextCompat.getColor(getContext(), R.color.tkpd_survey_icon_gray), PorterDuff.Mode.SRC_IN);
            rating = 5;

        } else if (i == R.id.submit_survey) {
            surveyResult.setSurveyResult(rating, surveyReason.getText().toString());
            dismiss();
        } else {
            rating = 5;
        }
    }

    public interface SurveyResult{
        void setSurveyResult(int rating, String surveyComment);
    }
}
