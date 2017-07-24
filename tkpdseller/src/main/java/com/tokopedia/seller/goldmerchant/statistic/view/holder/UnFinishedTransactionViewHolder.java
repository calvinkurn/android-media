package com.tokopedia.seller.goldmerchant.statistic.view.holder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.gmstat.views.circleprogress.DonutProgress;
import com.tokopedia.seller.gmstat.views.circleprogress.DonutProgressLayout;
import com.tokopedia.seller.gmstat.views.widget.CircleTextView;
import com.tokopedia.seller.gmstat.views.widget.TitleCardView;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.BaseGMViewHelper;
import com.tokopedia.seller.goldmerchant.statistic.view.model.UnFinishedTransactionViewModel;

/**
 * Created by normansyahputa on 7/13/17.
 */

public class UnFinishedTransactionViewHolder extends BaseGMViewHelper<UnFinishedTransactionViewModel> {

    private TitleCardView notDoneTransStatisticCardView;
    private DonutProgress gmStatisticTransDonutProgress;
    private DonutProgressLayout gmStatisticTransDonutProgressLayout;
    private CircleTextView ctvOnHoldCount;
    private CircleTextView ctvOnHoldAmount;
    private CircleTextView ctvResCenterCount;
    private String rupiahFormatText;


    public UnFinishedTransactionViewHolder(@Nullable Context context) {
        super(context);

        rupiahFormatText = context.getString(R.string.rupiah_format_text);
    }

    @Override
    public void initView(@Nullable View itemView) {
        notDoneTransStatisticCardView = (TitleCardView) itemView.findViewById(R.id.not_done_trans_statistic_card_view);
        notDoneTransStatisticCardView.setLoadingState(true);

        gmStatisticTransDonutProgress = (DonutProgress) itemView.findViewById(R.id.gm_statistic_trans_donut_progress);
        gmStatisticTransDonutProgressLayout = (DonutProgressLayout) itemView.findViewById(R.id.gm_statistic_trans_donut_progress_layout);

        ctvOnHoldCount = (CircleTextView) itemView.findViewById(R.id.ctv_onhold_count);
        ctvOnHoldAmount = (CircleTextView) itemView.findViewById(R.id.ctv_onhold_amount);
        ctvResCenterCount = (CircleTextView) itemView.findViewById(R.id.ctv_res_center_count);
    }

    private void processView(UnFinishedTransactionViewModel data) {
        data.setFormatAmount(rupiahFormatText);
        data.formatText();

        ctvOnHoldCount.setValue(Long.toString(data.getOnHoldCount()));
        ctvOnHoldAmount.setValue(data.getOnHoldAmountText());
        ctvResCenterCount.setValue(Long.toString(data.getResoCount()));
    }

    @Override
    public void bind(@Nullable UnFinishedTransactionViewModel data) {
        notDoneTransStatisticCardView.setLoadingState(false);
        processView(data);

        if (data == null || data.getTotalTransactionCount() == 0) {
            gmStatisticTransDonutProgress.setUnfinishedStrokeColor(ContextCompat.getColor(context, R.color.black_12));
            gmStatisticTransDonutProgress.setProgress(0);
            gmStatisticTransDonutProgressLayout.setAmount(String.valueOf(0));
        } else {
            gmStatisticTransDonutProgress.setUnfinishedStrokeColor(ContextCompat.getColor(context, R.color.tkpd_dark_red));
            double diffHoldCount = Math.floor((data.getOnHoldCount() / data.getTotalTransactionCount() * 100) + 0.5);

            gmStatisticTransDonutProgress.setProgress((float) diffHoldCount);
            gmStatisticTransDonutProgressLayout.setAmount(
                    KMNumbers.formatDecimalString(data.getTotalTransactionCount()));
        }

    }
}
