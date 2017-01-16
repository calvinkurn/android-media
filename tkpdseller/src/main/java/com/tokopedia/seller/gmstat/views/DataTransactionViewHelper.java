package com.tokopedia.seller.gmstat.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.db.chart.view.LineChartView;
import com.tokopedia.sellerapp.R;
import com.tokopedia.seller.gmstat.models.GetTransactionGraph;
import com.tokopedia.seller.gmstat.utils.DataTransactionChartConfig;
import com.tokopedia.sellerapp.gmsubscribe.GMSubscribeActivity;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.sellerapp.home.utils.ImageHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tokopedia.seller.gmstat.views.GMStatActivityFragment.NoDataAvailable;
import static com.tokopedia.seller.gmstat.views.PopularProductViewHelper.getFormattedString;

/**
 * Created by normansyahputa on 11/10/16.
 */

public class DataTransactionViewHelper {
    private DataTransactionChartConfig williamChartUtils;

    @BindArray(R.array.month_names_abrev)
    String[] monthNamesAbrev;

    @BindView(R.id.transaction_chart)
    LineChartView transactionChart;

    @BindView(R.id.percentage)
    TextView percentage;

    @BindView(R.id.transaction_count_icon)
    ImageView transactionCountIcon;

    @BindView(R.id.transaction_count)
    TextView transactionCount;

    @BindColor(R.color.arrow_down)
    int arrowDown;

    @BindColor(R.color.arrow_up)
    int arrowUp;

    @BindView(R.id.transaction_data_container_gold_merchant)
    LinearLayout transactionDataContainerGoldMerchant;

    @BindView(R.id.transaction_data_container_non_gold_merchant)
    LinearLayout transactionDataContainerNonGoldMerchant;

//    @BindView(R.id.transaction_data_container_upper)
//    RelativeLayout transactionDataContainerUpper;

    @BindColor(R.color.grey_400)
    int gredyColor;

//    @BindDrawable(R.drawable.ic_rectangle_down)
    Drawable icRectagleDown;

//    @BindDrawable(R.drawable.ic_rectangle_up)
    Drawable icRectagleUp;

    private View itemView;
    private boolean isGoldMerchant;

    private float[] mValues = new float[10];
    private String[] mLabels = new String[10];
    ImageHandler imageHandler;

    @BindView(R.id.separator_2_transaction_data)
    LinearLayout separator2;

    @BindColor(R.color.breakline_background)
    int breakLineBackground;

    @BindColor(android.R.color.transparent)
    int transparantColor;

    @OnClick(R.id.move_to_gmsubscribe)
    public void moveToGMSubscribe(){
        itemView.getContext().startActivity(new Intent(itemView.getContext(), GMSubscribeActivity.class));
    }

    public DataTransactionViewHelper(View itemView, ImageHandler imageHandler, boolean isGoldMerchant){
        this.itemView = itemView;
        this.isGoldMerchant = isGoldMerchant;
        ButterKnife.bind(this, itemView);

        icRectagleDown = AppCompatDrawableManager.get().getDrawable(itemView.getContext(),
                R.drawable.ic_rectangle_down);
        icRectagleUp = AppCompatDrawableManager.get().getDrawable(itemView.getContext(),
                R.drawable.ic_rectangle_up);
        for(int i=0;i<mLabels.length;i++){
            mLabels[i] = "";
        }
        williamChartUtils = new DataTransactionChartConfig(mLabels, mValues);
        this.imageHandler = imageHandler;

        if(isGoldMerchant){
            transactionDataContainerGoldMerchant.setVisibility(View.VISIBLE);
            transactionDataContainerNonGoldMerchant.setVisibility(View.GONE);
        }
    }

    public void bindData(GetTransactionGraph getTransactionGraph){

        if(isGoldMerchant){
            separator2.removeAllViews();
        }else{
            View view = new View(itemView.getContext());
            view.setLayoutParams(new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) dpToPx(itemView.getContext(), 16)));
            view.setBackgroundResource(R.color.breakline_background);
            separator2.addView(view);
        }

        /* non gold merchant */
        if(!isGoldMerchant){
            transactionDataContainerGoldMerchant.setVisibility(View.VISIBLE);
            transactionDataContainerNonGoldMerchant.setVisibility(View.VISIBLE);

            return;
        }

        /* empty state */
        if(getTransactionGraph == null || getTransactionGraph.getFinishedTrans() == 0){

            transactionCountIcon.setVisibility(View.GONE);
            percentage.setTextColor(gredyColor);
            percentage.setText(R.string.no_data);

            displayGraphic(getTransactionGraph, true);
            return;
        }

        /* non empty state */
        transactionCountIcon.setVisibility(View.VISIBLE);
        transactionCount.setText(getFormattedString(getTransactionGraph.getFinishedTrans()));

        // percentage is missing and icon is missing too
//        Double diffSuccessTrans = getTransactionGraph.getDiffSuccessTrans()*100;
        Double diffSuccessTrans = getTransactionGraph.getDiffFinishedTrans()*100;
        // image for arrow is here
        boolean isDefault;
        if(diffSuccessTrans == 0){
            transactionCountIcon.setVisibility(View.GONE);
            percentage.setTextColor(arrowUp);
            isDefault = true;
        }else if(diffSuccessTrans < 0){// down here
            if(diffSuccessTrans == NoDataAvailable*100){
                transactionCountIcon.setVisibility(View.GONE);
                percentage.setTextColor(gredyColor);
                isDefault = false;
            }else{
//            imageHandler.loadImage(transactionCountIcon, R.mipmap.arrow_down_percentage);
                transactionCountIcon.setImageDrawable(icRectagleDown);
                percentage.setTextColor(arrowDown);
                isDefault = true;
            }
        }else{// up here
//            imageHandler.loadImage(transactionCountIcon, R.mipmap.arrow_up_percentage);
            transactionCountIcon.setImageDrawable(icRectagleUp);
            percentage.setTextColor(arrowUp);
            isDefault = true;
        }

        if(isDefault){
//            DecimalFormat formatter = new DecimalFormat("#0.00");
//            double d = diffSuccessTrans;
//            String text;
//            System.out.println(text = formatter.format(d));
            double d = diffSuccessTrans;
            percentage.setText(String.format("%s%%", KMNumbers.formatString(d).replace("-", "")));
        }else{
            percentage.setText(R.string.no_data);
        }

        displayGraphic(getTransactionGraph, false);
    }

    public void displayGraphic(GetTransactionGraph getTransactionGraph, boolean emptyState) {

        List<Integer> successTransGraph = getTransactionGraph.getSuccessTransGraph();
        List<Integer> rejectedTransGraph = getTransactionGraph.getRejectedTransGraph();
        int size = (successTransGraph.size() >= rejectedTransGraph.size())
                ? rejectedTransGraph.size() : successTransGraph.size();
        List<Integer> merge = new ArrayList<>();
        for(int i=0;i<size;i++){
            merge.add(successTransGraph.get(i)+rejectedTransGraph.get(i));
        }

        List<NExcel> nExcels = joinDateAndGrossGraph(
                getTransactionGraph.getDateGraph(),
                merge);
        if(nExcels == null)
            return;

//        if(nExcels!= null) {
//            transactionChart.cmdFill(nExcels);
//        }
        //[]START] try used willam chart
        int i = 0;
        mLabels = new String[nExcels.size()];
        mValues = new float[nExcels.size()];
        for (NExcel nExcel : nExcels) {
            mLabels[i] = nExcel.getXmsg(); //getDateRaw(nExcel.getXmsg(), monthNamesAbrev);
            mValues[i] = nExcel.getUpper();
            i++;
        }
        williamChartUtils.setmLabels(mLabels);
        williamChartUtils.setmValues(mValues);

        int bottomMargin = 5;
        williamChartUtils.buildChart(
                williamChartUtils.buildLineChart(transactionChart,
                        (int) dpToPx(itemView.getContext(),
                                bottomMargin), emptyState));
        //[END] try used willam chart
    }

    private List<NExcel> joinDateAndGrossGraph(List<Integer> dateGraph, List<Integer> successTransGrpah){
        List<NExcel> nExcels = new ArrayList<>();
        if(dateGraph == null || successTransGrpah == null)
            return null;

        int lowerSize;
        if(dateGraph.size()>successTransGrpah.size()){
            lowerSize = successTransGrpah.size();
        }else{
            lowerSize = dateGraph.size();
        }

        for(int i=0;i<lowerSize;i++){
            Integer gross = successTransGrpah.get(i);

            nExcels.add(new NExcel(gross, ""));
        }

        return nExcels;
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }
}
