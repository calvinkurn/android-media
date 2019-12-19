package com.tokopedia.seller.home.view;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.reputationproduct.util.ReputationLevelUtils;
import com.tokopedia.core.shopinfo.models.shopmodel.Stats;
import com.tokopedia.seller.R;

/**
 * @author normansyahputa on 3/20/17.
 */

public class ReputationView extends FrameLayout implements BaseView<ReputationView.ReputationViewModel> {

    private final String defaultLayout;
    LinearLayout reputationBadgeListener;
    TextView reputationPoints;
    @LayoutRes
    private int defaultLayoutId;

    public ReputationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.ReputationView);
        try {
            defaultLayout = styledAttributes.getString(R.styleable.ReputationView_default_layout);
            if (defaultLayout != null) {
                defaultLayoutId = getLayoutByName(separate(defaultLayout));
            } else {
                defaultLayoutId = R.layout.reputation_item_view;
            }
        } finally {
            styledAttributes.recycle();
        }

        LayoutInflater.from(context).inflate(defaultLayoutId, this);

        reputationBadgeListener = (LinearLayout) findViewById(R.id.reputation_badge_listener);
        reputationPoints = (TextView) findViewById(R.id.reputation_points);
    }

    private String separate(String layoutName) {
        return layoutName.split("/")[2].replace(".xml", "");
    }

    private int getLayoutByName(String layoutName) {
        String packageName = getContext().getPackageName();
        int resId = getResources().getIdentifier(layoutName, "layout", packageName);
        return resId;
    }

    @Override
    public void init(ReputationViewModel data) {

        ReputationLevelUtils.setReputationMedalsWithoutDialog(getContext(), reputationBadgeListener, data.typeMedal, data.levelMedal, data.reputationPoints);

        reputationPoints.setText(String.format("%s %s", data.stats.shopReputationScore, getContext().getString(R.string.point)));
    }

    public static class ReputationViewModel {
        public int typeMedal;
        public int levelMedal;
        public String reputationPoints;
        public Stats stats;
    }


}