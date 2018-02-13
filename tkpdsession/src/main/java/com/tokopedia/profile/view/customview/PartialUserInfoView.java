package com.tokopedia.profile.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.session.R;

/**
 * Created by alvinatin on 13/02/18.
 */

public class PartialUserInfoView extends BaseCustomView{

    RelativeLayout partialUserStatus;
    TextView reputationSummaryScore;
    TextView reputationPositiveScore;
    TextView reputationNegativeScore;
    TextView reputationNetralScore;
    ImageView ivStatusInfo;
    ImageView ivReputationInfo;


    public PartialUserInfoView(@NonNull Context context) {
        super(context);
    }

    public PartialUserInfoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PartialUserInfoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){
        View view = inflate(getContext(), R.layout.partial_profile_user_info, this);
        partialUserStatus = view.findViewById(R.id.rl_partial_status);
        reputationPositiveScore = view.findViewById(R.id.tv_reputation_positive_score);
        reputationNegativeScore = view.findViewById(R.id.tv_reputation_negative_score);
        reputationNetralScore = view.findViewById(R.id.tv_reputation_netral_score);
        reputationSummaryScore = view.findViewById(R.id.tv_reputation_summary);
        ivStatusInfo = view.findViewById(R.id.iv_status_info);
        ivReputationInfo = view.findViewById(R.id.iv_reputation_info);
    }
}
