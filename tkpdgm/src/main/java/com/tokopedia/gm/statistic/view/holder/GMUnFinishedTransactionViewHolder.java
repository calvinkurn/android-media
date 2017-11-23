package com.tokopedia.gm.statistic.view.holder;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.tokopedia.design.card.TitleCardView;
import com.tokopedia.design.loading.LoadingStateView;
import com.tokopedia.gm.R;
import com.tokopedia.seller.common.utils.KMNumbers;
import com.tokopedia.gm.statistic.view.model.GMUnFinishedTransactionViewModel;
import com.tokopedia.gm.statistic.view.widget.CircleTextView;
import com.tokopedia.gm.statistic.view.widget.circleprogress.DonutProgress;
import com.tokopedia.gm.statistic.view.widget.circleprogress.DonutProgressLayout;

/**
 * Created by normansyahputa on 7/13/17.
 */

public class GMUnFinishedTransactionViewHolder implements GMStatisticViewHolder {

    private TitleCardView titleCardView;
    private DonutProgress donutProgress;
    private DonutProgressLayout donutProgressLayout;
    private CircleTextView onHoldCountCircleTextView;
    private CircleTextView onHoldAmountCircleTextView;
    private CircleTextView resCenterCountCircleTextView;
    private String rupiahFormatText;


    public GMUnFinishedTransactionViewHolder(View view) {
        titleCardView = (TitleCardView) view.findViewById(R.id.not_done_trans_statistic_card_view);
        donutProgress = (DonutProgress) view.findViewById(R.id.gm_statistic_trans_donut_progress);
        donutProgressLayout = (DonutProgressLayout) view.findViewById(R.id.gm_statistic_trans_donut_progress_layout);

        onHoldCountCircleTextView = (CircleTextView) view.findViewById(R.id.ctv_onhold_count);
        onHoldAmountCircleTextView = (CircleTextView) view.findViewById(R.id.ctv_onhold_amount);
        resCenterCountCircleTextView = (CircleTextView) view.findViewById(R.id.ctv_res_center_count);

        rupiahFormatText = view.getContext().getString(R.string.gm_statistic_rupiah_format_text);
    }

    private void processView(GMUnFinishedTransactionViewModel GMUnFinishedTransactionViewModel) {
        GMUnFinishedTransactionViewModel.setFormatAmount(rupiahFormatText);
        GMUnFinishedTransactionViewModel.formatText();
        onHoldCountCircleTextView.setValue(Long.toString(GMUnFinishedTransactionViewModel.getOnHoldCount()));
        onHoldAmountCircleTextView.setValue(GMUnFinishedTransactionViewModel.getOnHoldAmountText());
        resCenterCountCircleTextView.setValue(Long.toString(GMUnFinishedTransactionViewModel.getResoCount()));
    }

    public void bind(@Nullable GMUnFinishedTransactionViewModel GMUnFinishedTransactionViewModel) {
        setViewState(LoadingStateView.VIEW_CONTENT);
        processView(GMUnFinishedTransactionViewModel);
        if (GMUnFinishedTransactionViewModel == null || GMUnFinishedTransactionViewModel.getTotalTransactionCount() == 0) {
            donutProgress.setUnfinishedStrokeColor(ContextCompat.getColor(titleCardView.getContext(), R.color.black_12));
            donutProgress.setProgress(0);
            donutProgressLayout.setAmount(String.valueOf(0));
        } else {
            donutProgress.setUnfinishedStrokeColor(ContextCompat.getColor(titleCardView.getContext(), R.color.tkpd_dark_red));
            double diffHoldCount = Math.floor((GMUnFinishedTransactionViewModel.getOnHoldCount() / GMUnFinishedTransactionViewModel.getTotalTransactionCount() * 100) + 0.5);
            donutProgress.setProgress((float) diffHoldCount);
            donutProgressLayout.setAmount(KMNumbers.formatDecimalString(GMUnFinishedTransactionViewModel.getTotalTransactionCount()));
        }
    }

    @Override
    public void setViewState(int state) {
        titleCardView.setViewState(state);
    }
}