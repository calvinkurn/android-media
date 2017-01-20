package com.tokopedia.seller.gmstat.views;

import android.content.Context;
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


import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.Router;
import com.tokopedia.seller.gmstat.models.GetTransactionGraph;
import com.tokopedia.seller.gmstat.utils.DataTransactionChartConfig;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.gmstat.views.williamchart.chart.view.LineChartView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static com.tokopedia.seller.gmstat.utils.GMStatConstant.PERCENTAGE_FORMAT;
import static com.tokopedia.seller.gmstat.views.GMStatActivityFragment.NoDataAvailable;
import static com.tokopedia.seller.gmstat.views.PopularProductViewHelper.getFormattedString;

/**
 * Created by normansyahputa on 11/10/16.
 */

public class DataTransactionViewHelper {
    private DataTransactionChartConfig williamChartUtils;

    String[] monthNamesAbrev;

    LineChartView transactionChart;

    TextView percentage;

    ImageView transactionCountIcon;

    TextView transactionCount;

    int arrowDown;

    int arrowUp;

    LinearLayout transactionDataContainerGoldMerchant;

    LinearLayout transactionDataContainerNonGoldMerchant;

//    @BindView(R2.id.transaction_data_container_upper)
//    RelativeLayout transactionDataContainerUpper;

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

    LinearLayout separator2;

    int breakLineBackground;

    int transparantColor;

    public void moveToGMSubscribe(){
        Router.goToGMSubscribe(itemView.getContext());
    }

    public DataTransactionViewHelper(View itemView, ImageHandler imageHandler, boolean isGoldMerchant){
        this.itemView = itemView;
        this.isGoldMerchant = isGoldMerchant;
        initView(itemView);

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

    private void initView(View itemView) {
        monthNamesAbrev = itemView.getResources().getStringArray(R.array.month_names_abrev);

        transactionChart = (LineChartView) itemView.findViewById(R.id.transaction_chart);

        percentage= (TextView) itemView.findViewById(R.id.percentage);

        transactionCountIcon= (ImageView) itemView.findViewById(R.id.transaction_count_icon);

        transactionCount= (TextView) itemView.findViewById(R.id.transaction_count);

        arrowDown = ResourcesCompat.getColor(itemView.getResources(), R.color.arrow_down, null);

        arrowUp = ResourcesCompat.getColor(itemView.getResources(), R.color.arrow_up, null);

        transactionDataContainerGoldMerchant= (LinearLayout) itemView.findViewById(R.id.transaction_data_container_gold_merchant);

        transactionDataContainerNonGoldMerchant= (LinearLayout) itemView.findViewById(R.id.transaction_data_container_non_gold_merchant);

        gredyColor = ResourcesCompat.getColor(itemView.getResources(), R.color.grey_400, null);

        separator2= (LinearLayout) itemView.findViewById(R.id.separator_2_transaction_data);

        breakLineBackground = ResourcesCompat.getColor(itemView.getResources(), R.color.breakline_background, null);

        transparantColor = ResourcesCompat.getColor(itemView.getResources(), android.R.color.transparent, null);

        itemView.findViewById(R.id.move_to_gmsubscribe)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToGMSubscribe();
                    }
                });
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
            percentage.setText(String.format(PERCENTAGE_FORMAT, KMNumbers.formatString(d).replace("-", "")));
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
