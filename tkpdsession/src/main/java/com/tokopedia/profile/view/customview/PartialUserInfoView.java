package com.tokopedia.profile.view.customview;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;
import com.tokopedia.session.R;

/**
 * Created by alvinatin on 13/02/18.
 */

public class PartialUserInfoView extends BaseCustomView {

    private View partialUserStatus;
    private TextView tvReputationSummaryScore;
    private TextView tvReputationPositiveScore;
    private TextView tvReputationNegativeScore;
    private TextView tvReputationNetralScore;
    private ImageView ivStatusInfo;
    private ImageView ivReputationInfo;

    private Boolean isVerified;
    private String reputationSummaryScore;
    private String reputationPositiveScore;
    private String reputationNegativeScore;
    private String reputationNetralScore;


    public PartialUserInfoView(@NonNull Context context) {
        super(context);
        init();
    }

    public PartialUserInfoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PartialUserInfoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

    private void init() {
        View view = inflate(getContext(), R.layout.partial_profile_user_info, this);
        partialUserStatus = view.findViewById(R.id.rl_partial_status);
        tvReputationPositiveScore = view.findViewById(R.id.tv_reputation_positive_score);
        tvReputationNegativeScore = view.findViewById(R.id.tv_reputation_negative_score);
        tvReputationNetralScore = view.findViewById(R.id.tv_reputation_netral_score);
        tvReputationSummaryScore = view.findViewById(R.id.tv_reputation_summary);
        ivStatusInfo = view.findViewById(R.id.iv_status_info);
        ivReputationInfo = view.findViewById(R.id.iv_reputation_info);
    }

    public void renderData(TopProfileViewModel model){
        partialUserStatus.setVisibility(model.isPhoneVerified() && model.isEmailVerified() ? VISIBLE : GONE);
        tvReputationSummaryScore.setText(model.getSummaryScore());
        tvReputationPositiveScore.setText(model.getPositiveScore());
        tvReputationNetralScore.setText(model.getNetralScore());
        tvReputationNegativeScore.setText(model.getNegativeScore());
    }

    public void setVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public void setReputationSummaryScore(String summaryScore) {
        this.reputationSummaryScore = summaryScore;
    }

    public void setReputationPositiveScore(String positiveScore) {
        this.reputationPositiveScore = positiveScore;
    }

    public void setReputationNetralScore(String netralScore) {
        this.reputationNetralScore = netralScore;
    }

    public void setReputationNegativeScore(String negativeScore) {
        this.reputationNegativeScore = negativeScore;
    }

    public void buildView() {
        if (isVerified) {
            partialUserStatus.setVisibility(VISIBLE);
        }
    }
}
