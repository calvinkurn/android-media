package com.tokopedia.seller.gmstat.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.Router;
import com.tokopedia.seller.gmstat.models.GetTransactionGraph;
import com.tokopedia.seller.gmstat.utils.GMStatConstant;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartModel;
import com.tokopedia.seller.goldmerchant.statistic.view.activity.GMStatisticTransactionActivity;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.GMSeeDetailViewHelper;
import com.tokopedia.seller.lib.williamchart.util.DataTransactionChartConfig;
import com.tokopedia.seller.lib.williamchart.util.DataTransactionDataSetConfig;
import com.tokopedia.seller.lib.williamchart.util.EmptyDataTransactionDataSetConfig;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.seller.gmstat.views.PopularProductViewHelper.getFormattedString;

/**
 * Created on 11/10/16.
 * @author normansyahputa
 */

public class DataTransactionViewHelper {
    private LineChartView transactionChart;
    private TextView percentage;
    private ImageView transactionCountIcon;
    private TextView transactionCount;
    private int arrowDown;
    private int arrowUp;
    private LinearLayout transactionDataContainerGoldMerchant;
    private LinearLayout transactionDataContainerNonGoldMerchant;
    private Drawable icRectagleDown;
    private Drawable icRectagleUp;
    private View itemView;
    private boolean isGoldMerchant;
    private float[] mValues = new float[10];
    private String[] mLabels = new String[10];
    private LinearLayout separator2;
    private BaseWilliamChartConfig baseWilliamChartConfig;
    private DataTransactionChartConfig dataTransactionChartConfig;
    private GMSeeDetailViewHelper gmSeeDetailViewHelper;
    private int greyColor;

    public DataTransactionViewHelper(View itemView, boolean isGoldMerchant) {
        baseWilliamChartConfig = new BaseWilliamChartConfig();

        this.itemView = itemView;
        this.isGoldMerchant = isGoldMerchant;

        gmSeeDetailViewHelper = new GMSeeDetailViewHelper(itemView.getContext());
        gmSeeDetailViewHelper.initView(itemView);

        initView(itemView);

        icRectagleDown = AppCompatDrawableManager.get().getDrawable(itemView.getContext(),
                R.drawable.ic_rectangle_down);
        icRectagleUp = AppCompatDrawableManager.get().getDrawable(itemView.getContext(),
                R.drawable.ic_rectangle_up);
        for (int i = 0; i < mLabels.length; i++) {
            mLabels[i] = "";
        }

        if (isGoldMerchant) {
            transactionDataContainerGoldMerchant.setVisibility(View.VISIBLE);
            transactionDataContainerNonGoldMerchant.setVisibility(View.GONE);
        }
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    public void setDataTransactionChartConfig(DataTransactionChartConfig dataTransactionChartConfig) {
        this.dataTransactionChartConfig = dataTransactionChartConfig;
    }

    public void moveToGMSubscribe() {
        Router.goToGMSubscribe(itemView.getContext());
    }

    private void initView(final View itemView) {

        transactionChart = (LineChartView) itemView.findViewById(R.id.transaction_chart);

        percentage = (TextView) itemView.findViewById(R.id.percentage);

        transactionCountIcon = (ImageView) itemView.findViewById(R.id.transaction_count_icon);

        transactionCount = (TextView) itemView.findViewById(R.id.transaction_count);

        arrowDown = ResourcesCompat.getColor(itemView.getResources(), R.color.arrow_down, null);

        arrowUp = ResourcesCompat.getColor(itemView.getResources(), R.color.arrow_up, null);

        transactionDataContainerGoldMerchant = (LinearLayout) itemView.findViewById(R.id.transaction_data_container_gold_merchant);

        transactionDataContainerNonGoldMerchant = (LinearLayout) itemView.findViewById(R.id.transaction_data_container_non_gold_merchant);

        separator2 = (LinearLayout) itemView.findViewById(R.id.separator_2_transaction_data);

        greyColor = ResourcesCompat.getColor(itemView.getResources(), R.color.grey_400, null);

        itemView.findViewById(R.id.move_to_gmsubscribe)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToGMSubscribe();
                    }
                });

        gmSeeDetailViewHelper.bind(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // force to move to new statistic
                Intent intent = new Intent(itemView.getContext(), GMStatisticTransactionActivity.class);
                intent.putExtra(BaseGMStatActivity.SHOP_ID, SessionHandler.getShopID(itemView.getContext()));
                intent.putExtra(BaseGMStatActivity.IS_GOLD_MERCHANT, SessionHandler.isGoldMerchant(itemView.getContext()));
                itemView.getContext().startActivity(intent);
                // force to move to new statistic
            }
        });
    }

    public void bindData(GetTransactionGraph getTransactionGraph) {

        if (dataTransactionChartConfig == null)
            throw new RuntimeException("please pass configuration for graphic !!");

        if (isGoldMerchant) {
            separator2.removeAllViews();
        } else {
            View view = new View(itemView.getContext());
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) dpToPx(itemView.getContext(), 16)));
            view.setBackgroundResource(R.color.breakline_background);
            separator2.addView(view);
        }

        /* non gold merchant */
        if (!isGoldMerchant) {
            transactionDataContainerGoldMerchant.setVisibility(View.VISIBLE);
            transactionDataContainerNonGoldMerchant.setVisibility(View.VISIBLE);
            setEmptyStatePercentage();

            return;
        }

        /* empty state */
        if (getTransactionGraph == null || getTransactionGraph.getFinishedTrans() == 0) {

            setEmptyStatePercentage();

            displayGraphic(getTransactionGraph, true);
            return;
        }

        /* non empty state */
        transactionCountIcon.setVisibility(View.VISIBLE);
        transactionCount.setText(getFormattedString(getTransactionGraph.getFinishedTrans()));

        Double diffSuccessTrans = getTransactionGraph.getDiffFinishedTrans() * 100;
        boolean isDefault;
        if (diffSuccessTrans == 0) {
            transactionCountIcon.setVisibility(View.GONE);
            percentage.setTextColor(arrowUp);
            isDefault = true;
        } else if (diffSuccessTrans < 0) {// down here
            if (diffSuccessTrans == GMStatConstant.NoDataAvailable * 100) {
                transactionCountIcon.setVisibility(View.GONE);
                percentage.setTextColor(greyColor);
                isDefault = false;
            } else {
                transactionCountIcon.setImageDrawable(icRectagleDown);
                percentage.setTextColor(arrowDown);
                isDefault = true;
            }
        } else {// up here
            transactionCountIcon.setImageDrawable(icRectagleUp);
            percentage.setTextColor(arrowUp);
            isDefault = true;
        }

        if (isDefault) {
            double d = diffSuccessTrans;
            percentage.setText(String.format(GMStatConstant.PERCENTAGE_FORMAT, KMNumbers.formatString(d).replace("-", "")));
        } else {
            percentage.setText(R.string.no_data);
        }

        displayGraphic(getTransactionGraph, false);
    }

    protected void setEmptyStatePercentage() {
        transactionCountIcon.setVisibility(View.GONE);
        percentage.setTextColor(greyColor);
        percentage.setText(R.string.no_data);
    }

    public void displayGraphic(GetTransactionGraph getTransactionGraph, boolean emptyState) {
        List<Integer> successTransGraph = getTransactionGraph.getSuccessTransGraph();
        List<Integer> rejectedTransGraph = getTransactionGraph.getRejectedTransGraph();

        if (successTransGraph == null || rejectedTransGraph == null)
            return;

        int size = (successTransGraph.size() >= rejectedTransGraph.size())
                ? rejectedTransGraph.size() : successTransGraph.size();
        List<Integer> merge = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            merge.add(successTransGraph.get(i) + rejectedTransGraph.get(i));
        }

        List<NExcel> nExcels = joinDateAndGrossGraph(
                getTransactionGraph.getDateGraph(),
                merge);
        if (nExcels == null)
            return;


        int i = 0;
        mLabels = new String[nExcels.size()];
        mValues = new float[nExcels.size()];
        for (NExcel nExcel : nExcels) {
            mLabels[i] = nExcel.getXmsg(); //getDateRaw(nExcel.getXmsg(), monthNamesAbrev);
            mValues[i] = nExcel.getUpper();
            i++;
        }

        BaseWilliamChartModel baseWilliamChartModel
                = new BaseWilliamChartModel(mLabels, mValues);
        baseWilliamChartConfig.reset();
        baseWilliamChartConfig.setBasicGraphConfiguration(dataTransactionChartConfig);

        if (emptyState) {
            baseWilliamChartConfig.addBaseWilliamChartModels(
                    baseWilliamChartModel, new EmptyDataTransactionDataSetConfig());
        } else {
            baseWilliamChartConfig.addBaseWilliamChartModels(
                    baseWilliamChartModel, new DataTransactionDataSetConfig());
        }
        baseWilliamChartConfig.buildChart(transactionChart);
    }

    private List<NExcel> joinDateAndGrossGraph(List<Integer> dateGraph, List<Integer> successTransGrpah) {
        List<NExcel> nExcels = new ArrayList<>();
        if (dateGraph == null || successTransGrpah == null)
            return null;

        int lowerSize;
        if (dateGraph.size() > successTransGrpah.size()) {
            lowerSize = successTransGrpah.size();
        } else {
            lowerSize = dateGraph.size();
        }

        for (int i = 0; i < lowerSize; i++) {
            Integer gross = successTransGrpah.get(i);

            nExcels.add(new NExcel(gross, ""));
        }

        return nExcels;
    }
}
