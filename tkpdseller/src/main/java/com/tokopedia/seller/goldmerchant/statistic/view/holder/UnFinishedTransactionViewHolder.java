package com.tokopedia.seller.goldmerchant.statistic.view.holder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.views.circleprogress.DonutProgress;
import com.tokopedia.seller.gmstat.views.circleprogress.DonutProgressLayout;
import com.tokopedia.seller.gmstat.views.widget.TitleCardView;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.BaseGMViewHelper;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.WidgetUnfinishedTransHelper;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.UnFinishedTransViewHelperModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.UnFinishedTransactionViewModel;

/**
 * Created by normansyahputa on 7/13/17.
 */

public class UnFinishedTransactionViewHolder extends BaseGMViewHelper<UnFinishedTransactionViewModel> {

    private TitleCardView notDoneTransStatisticCardView;
    private DonutProgress gmStatisticTransDonutProgress;
    private DonutProgressLayout gmStatisticTransDonutProgressLayout;
    private RelativeLayout onHoldCountUnfinishedTransaction;
    private WidgetUnfinishedTransHelper onHoldCountHelper;
    private WidgetUnfinishedTransHelper onHoldAmountHelper;
    private WidgetUnfinishedTransHelper resCenterCountHelper;

    public UnFinishedTransactionViewHolder(@Nullable Context context) {
        super(context);
    }

    @Override
    public void initView(@Nullable View itemView) {
        notDoneTransStatisticCardView = (TitleCardView) itemView.findViewById(R.id.not_done_trans_statistic_card_view);
        notDoneTransStatisticCardView.setLoadingViewRes(R.layout.item_loading_gmstat_pie_chart);
        notDoneTransStatisticCardView.setLoadingState(true);
        gmStatisticTransDonutProgress = (DonutProgress) itemView.findViewById(R.id.gm_statistic_trans_donut_progress);
        gmStatisticTransDonutProgressLayout = (DonutProgressLayout) itemView.findViewById(R.id.gm_statistic_trans_donut_progress_layout);

        onHoldCountHelper = new WidgetUnfinishedTransHelper((RelativeLayout) itemView.findViewById(R.id.onhold_count_unfinished_transaction));

        //hide the green dot image
        RelativeLayout onHoldAmountUnfinishedTransView= (RelativeLayout) itemView.findViewById(R.id.onhold_amount_unfinished_transaction);
        onHoldAmountUnfinishedTransView.findViewById(R.id.unfinished_transaction_description_image).setVisibility(View.INVISIBLE);
        onHoldAmountHelper = new WidgetUnfinishedTransHelper(onHoldAmountUnfinishedTransView);

        resCenterCountHelper = new WidgetUnfinishedTransHelper((RelativeLayout) itemView.findViewById(R.id.res_center_count_unfinished_transaction));
    }

    private void setUnfinishedTransCardView() {
        notDoneTransStatisticCardView.setTitle(context.getString(R.string.unfinished_transaction_text));
    }

    private void processView(UnFinishedTransactionViewModel data) {
        UnFinishedTransViewHelperModel unFinishedTransViewHelperModel =
                new UnFinishedTransViewHelperModel();
        unFinishedTransViewHelperModel.drawableRes = R.drawable.circle_green;
        unFinishedTransViewHelperModel.rightText = Long.toString(data.getOnHoldCount());
        unFinishedTransViewHelperModel.leftText = context.getString(R.string.on_hold_trans_text);

        onHoldCountHelper.bind(unFinishedTransViewHelperModel);

        unFinishedTransViewHelperModel = new UnFinishedTransViewHelperModel();
        unFinishedTransViewHelperModel.drawableRes = R.drawable.circle_green;
        unFinishedTransViewHelperModel.rightText = data.getOnHoldAmountText();
        unFinishedTransViewHelperModel.leftText = context.getString(R.string.trans_amount_text);

        onHoldAmountHelper.bind(unFinishedTransViewHelperModel);

        unFinishedTransViewHelperModel = new UnFinishedTransViewHelperModel();
        unFinishedTransViewHelperModel.drawableRes = R.drawable.circle_grey;
        unFinishedTransViewHelperModel.rightText = Long.toString(data.getResoCount());
        unFinishedTransViewHelperModel.leftText = context.getString(R.string.trans_in_res_center_text);

        resCenterCountHelper.bind(unFinishedTransViewHelperModel);
    }

    @Override
    public void bind(@Nullable UnFinishedTransactionViewModel data) {
        notDoneTransStatisticCardView.setLoadingState(false);

        setUnfinishedTransCardView();
        processView(data);

        double diffHoldCount = Math.floor((data.getOnHoldCount() / data.getTotalTransactionCount() * 100) + 0.5);

        gmStatisticTransDonutProgress.setProgress((float) diffHoldCount);

        gmStatisticTransDonutProgressLayout.setTitle(context.getString(R.string.total_trans_text));
        gmStatisticTransDonutProgressLayout.setAmount(Long.toString(data.getTotalTransactionCount()));

    }
}
