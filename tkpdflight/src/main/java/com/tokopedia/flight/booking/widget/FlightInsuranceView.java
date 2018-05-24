package com.tokopedia.flight.booking.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.adapter.FlightInsuranceBenefitAdapter;
import com.tokopedia.flight.booking.view.viewmodel.FlightInsuranceBenefitViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightInsuranceViewModel;

public class FlightInsuranceView extends LinearLayout {

    private AppCompatTextView tvName;
    private AppCompatTextView tvDescription;
    private AppCompatCheckBox cbInsurance;
    private AppCompatImageView ivHighlight;
    private AppCompatImageView ivProtectionImageView;
    private AppCompatTextView tvHighlight;
    private AppCompatTextView tvHighlightDetail;
    private AppCompatTextView tvHighlightTnc;
    private AppCompatTextView protectionLabelTextView;
    private RecyclerView benefitsRecyclerView;
    private LinearLayout highlightContainer;
    private LinearLayout otherProtection;
    private View dividerBenefit;

    private FlightInsuranceViewModel flightInsuranceViewModel;
    private ActionListener listener;

    public interface ActionListener {
        void onInsuranceChecked(FlightInsuranceViewModel insurance, boolean checked);

        void onMoreInfoClicked(String tncUrl, String title);
    }

    public FlightInsuranceView(Context context) {
        super(context);
        init();
    }

    public FlightInsuranceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FlightInsuranceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlightInsuranceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_flight_insurance_view, this);
        findView(view);
    }

    private void findView(View view) {
        tvName = (AppCompatTextView) view.findViewById(R.id.tv_name);
        tvDescription = (AppCompatTextView) view.findViewById(R.id.tv_description);
        cbInsurance = (AppCompatCheckBox) view.findViewById(R.id.cb_insurance);
        ivHighlight = (AppCompatImageView) view.findViewById(R.id.iv_highlight);
        ivProtectionImageView = (AppCompatImageView) view.findViewById(R.id.iv_protection_expand);
        tvHighlight = (AppCompatTextView) view.findViewById(R.id.tv_highlight);
        tvHighlightDetail = (AppCompatTextView) view.findViewById(R.id.tv_highlight_detail);
        tvHighlightTnc = (AppCompatTextView) view.findViewById(R.id.tv_highlight_tnc);
        protectionLabelTextView = (AppCompatTextView) view.findViewById(R.id.tv_protection_label);
        benefitsRecyclerView = (RecyclerView) view.findViewById(R.id.rv_benefits);
        highlightContainer = (LinearLayout) view.findViewById(R.id.highlight_container);
        otherProtection = (LinearLayout) view.findViewById(R.id.other_protection);
        dividerBenefit = (View) view.findViewById(R.id.divider_benefit);
        protectionLabelTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                benefitsRecyclerView.setVisibility(benefitsRecyclerView.getVisibility() == VISIBLE ? GONE : VISIBLE);
                if (benefitsRecyclerView.getVisibility() == VISIBLE) {
                    ivProtectionImageView.setRotation(180);
                } else {
                    ivProtectionImageView.setRotation(0);
                }
            }
        });
        cbInsurance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (listener != null) {
                    listener.onInsuranceChecked(flightInsuranceViewModel, b);
                }
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        renderData(this.flightInsuranceViewModel);
    }

    public void renderData(FlightInsuranceViewModel insuranceViewModel) {
        if (insuranceViewModel == null) return;
        this.flightInsuranceViewModel = insuranceViewModel;
        tvName.setText(insuranceViewModel.getName());
        tvDescription.setText(insuranceViewModel.getDescription());
        cbInsurance.setChecked(flightInsuranceViewModel.isDefaultChecked());
        if (insuranceViewModel.getBenefits() != null && insuranceViewModel.getBenefits().size() > 0) {
            highlightContainer.setVisibility(VISIBLE);
            FlightInsuranceBenefitViewModel highlightBenefit = insuranceViewModel.getBenefits().get(0);
            insuranceViewModel.getBenefits().remove(0);
            tvHighlight.setText(highlightBenefit.getTitle());
            tvHighlightDetail.setText(highlightBenefit.getDescription());
            ImageHandler.loadImageWithoutPlaceholder(ivHighlight, highlightBenefit.getIcon(),
                    VectorDrawableCompat.create(getResources(), R.drawable.ic_airline_default, getContext().getTheme()));

            if (insuranceViewModel.getBenefits().size() > 0) {
                otherProtection.setVisibility(VISIBLE);
                protectionLabelTextView.setText(String.format(getContext().getString(R.string.flight_insurance_additional_benefits_prefix), insuranceViewModel.getBenefits().size()));
                benefitsRecyclerView.setVisibility(GONE);
                FlightInsuranceBenefitAdapter benefitAdapter = new FlightInsuranceBenefitAdapter(insuranceViewModel.getBenefits());
                benefitsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                benefitsRecyclerView.setAdapter(benefitAdapter);
                dividerBenefit.setVisibility(VISIBLE);
            } else {
                dividerBenefit.setVisibility(GONE);
                otherProtection.setVisibility(GONE);
            }
        } else {
            highlightContainer.setVisibility(GONE);
        }
        tvHighlightTnc.setText(buildTncText(
                insuranceViewModel.getTncAggreement(),
                insuranceViewModel.getTncUrl(),
                insuranceViewModel.getName())
        );
        tvHighlightTnc.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private SpannableString buildTncText(String tncAggreement, String tncUrl, String title) {
        final int color = getResources().getColor(R.color.green_300);
        String fullText = tncAggreement + ". ";
        if (tncUrl != null && tncUrl.length() > 0) {
            fullText += getContext().getString(R.string.flight_insurance_learn_more_label);
        }
        int stopIndex = fullText.length();
        SpannableString descriptionStr = new SpannableString(fullText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (listener != null && tncUrl != null && tncUrl.length() > 0) {
                    listener.onMoreInfoClicked(tncUrl, title);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(color);
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            }
        };
        descriptionStr.setSpan(clickableSpan, tncAggreement.length() + 2, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return descriptionStr;
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }
}
