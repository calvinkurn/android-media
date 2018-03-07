package com.tokopedia.profile.view.customview;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;
import com.tokopedia.session.R;

/**
 * @author by alvinatin on 13/02/18.
 */

public class PartialUserInfoView extends BaseCustomView {

    private View partialUserStatus;
    private TextView tvReputationSummaryScore;
    private TextView tvReputationPositiveScore;
    private TextView tvReputationNegativeScore;
    private TextView tvReputationNetralScore;
    private View userStatusLayout;
    private View userReputationLayout;
    private TextView tvSubtitle;

    private BottomSheetView statusBottomSheetView;
    private BottomSheetView reputationBottomSheetView;

    public PartialUserInfoView(@NonNull Context context) {
        super(context);
        init();
    }

    public PartialUserInfoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PartialUserInfoView(@NonNull Context context, @Nullable AttributeSet attrs, int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchSaveInstanceState(container);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.partial_profile_user_info, this);
        partialUserStatus = view.findViewById(R.id.rl_partial_status);
        tvReputationPositiveScore = view.findViewById(R.id.tv_reputation_positive_score);
        tvReputationNegativeScore = view.findViewById(R.id.tv_reputation_negative_score);
        tvReputationNetralScore = view.findViewById(R.id.tv_reputation_netral_score);
        tvReputationSummaryScore = view.findViewById(R.id.tv_reputation_summary);
        userStatusLayout = view.findViewById(R.id.ll_status_title);
        userReputationLayout = view.findViewById(R.id.ll_reputation_title);
        tvSubtitle = view.findViewById(R.id.tv_subtitle_user_info);

        statusBottomSheetView = new BottomSheetView(getContext());
        statusBottomSheetView.setTitleTextSize(getResources().getDimension(R.dimen
                .new_text_size_input));
        statusBottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(getContext().getString(R.string.title_user_information_status))
                .setBody(getContext().getString(R.string.status_description))
                .setCloseButton(getContext().getString(R.string.title_ok))
                .build());

        reputationBottomSheetView = new BottomSheetView(getContext());
        reputationBottomSheetView.setTitleTextSize(getResources().getDimension(R.dimen
                .new_text_size_input));
        reputationBottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(getContext().getString(R.string.title_user_information_reputation))
                .setBody(getContext().getString(R.string.reputation_description))
                .setCloseButton(getContext().getString(R.string.title_ok))
                .build());
    }

    public void renderData(TopProfileViewModel model) {
        this.setVisibility(VISIBLE);

        tvSubtitle.setVisibility(model.isUser() && model.isKol() ? VISIBLE : GONE);

        partialUserStatus.setVisibility(model.isPhoneVerified() && model.isEmailVerified() ?
                VISIBLE : GONE);

        tvReputationSummaryScore.setVisibility(VISIBLE);
        String summary = model.getSummaryScore() + "%";
        tvReputationSummaryScore.setText(summary);

        tvReputationPositiveScore.setVisibility(VISIBLE);
        tvReputationPositiveScore.setText(model.getPositiveScore());

        tvReputationNetralScore.setVisibility(VISIBLE);
        tvReputationNetralScore.setText(model.getNetralScore());

        tvReputationNegativeScore.setVisibility(VISIBLE);
        tvReputationNegativeScore.setText(model.getNegativeScore());

        userStatusLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                statusBottomSheetView.show();
            }
        });

        userReputationLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reputationBottomSheetView.show();
            }
        });
    }

}
