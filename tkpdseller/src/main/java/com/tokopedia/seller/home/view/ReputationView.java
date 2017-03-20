package com.tokopedia.seller.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.reputationproduct.util.ReputationLevelUtils;
import com.tokopedia.core.shopinfo.models.shopmodel.Stats;
import com.tokopedia.seller.R;

import butterknife.ButterKnife;

/**
 * Created by normansyahputa on 3/20/17.
 */

public class ReputationView extends FrameLayout implements BaseView<ReputationView.ReputationViewModel> {

    LinearLayout reputationBadgeListener;

    TextView reputationPoints;

    public ReputationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.reputation_item_view, this);

        reputationBadgeListener = (LinearLayout) findViewById(R.id.reputation_badge_listener);
        reputationPoints = (TextView) findViewById(R.id.reputation_points);

        ButterKnife.bind(this);
    }

    @Override
    public void init(ReputationViewModel data) {

        ReputationLevelUtils.setReputationMedals(getContext(), reputationBadgeListener, data.typeMedal, data.levelMedal, data.reputationPoints);

        reputationPoints.setText("Reputasi : " + data.stats.shopReputationScore + " Poin");
    }

    public static class ReputationViewModel {
        public int typeMedal;
        public int levelMedal;
        public String reputationPoints;
        public Stats stats;
    }


}