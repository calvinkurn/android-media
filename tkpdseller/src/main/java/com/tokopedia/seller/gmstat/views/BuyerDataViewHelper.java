package com.tokopedia.seller.gmstat.views;

import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.models.GetBuyerData;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.gmstat.views.circleprogress.DonutProgress;

import java.util.Locale;

import static com.tokopedia.seller.gmstat.views.GMStatActivityFragment.NoDataAvailable;
import static com.tokopedia.seller.gmstat.views.PopularProductViewHelper.getFormattedString;

/**
 * Created by normansyahputa on 11/11/16.
 */

public class BuyerDataViewHelper {

    DonutProgress buyerDataPieChart;

    TextView buyerCount;

    ImageView buyerCountIcon;

    TextView percentageBuyer;

    TextView femalePie;

    TextView malePie;

    int arrowDown;

    int arrowUp;

    int gredyColor;

//    @BindDrawable(R.drawable.ic_rectangle_down)
    Drawable icRectagleDown;

//    @BindDrawable(R.drawable.ic_rectangle_up)
    Drawable icRectagleUp;

    String[] gender;

    TextView headerPieBuyerData;

    private static final Locale locale = new Locale("in","ID");

    public BuyerDataViewHelper(View itemView){
        initView(itemView);

        icRectagleDown = AppCompatDrawableManager.get().getDrawable(itemView.getContext(),
                R.drawable.ic_rectangle_down);
        icRectagleUp = AppCompatDrawableManager.get().getDrawable(itemView.getContext(),
                R.drawable.ic_rectangle_up);
    }

    private void initView(View itemView) {

        buyerDataPieChart = (DonutProgress) itemView.findViewById(R.id.buyer_data_pie_chart);

        buyerCount= (TextView) itemView.findViewById(R.id.buyer_count);

        buyerCountIcon= (ImageView) itemView.findViewById(R.id.buyer_count_icon);

        percentageBuyer= (TextView) itemView.findViewById(R.id.percentage_buyer);

        femalePie= (TextView) itemView.findViewById(R.id.female_pie);

        malePie= (TextView) itemView.findViewById(R.id.male_pie);

        arrowDown = ResourcesCompat.getColor(itemView.getResources(), R.color.arrow_down, null);

        arrowUp = ResourcesCompat.getColor(itemView.getResources(), R.color.arrow_up, null);

        gredyColor = ResourcesCompat.getColor(itemView.getResources(), R.color.grey_400, null);

        gender = itemView.getResources().getStringArray(R.array.gender);

        headerPieBuyerData = (TextView) itemView.findViewById(R.id.header_pie_buyer_data);


    }

    public void bindData(GetBuyerData getBuyerData) {

        /* this is empty state */
        if(getBuyerData.getTotalBuyer()==0 &&
                (getBuyerData.getMaleBuyer()==0 || getBuyerData.getFemaleBuyer()==0)){
            buyerCount.setText(getFormattedString(getBuyerData.getTotalBuyer()));
            buyerDataPieChart.setProgress(0f);
            femalePie.setTextColor(gredyColor);
            femalePie.setText("0 %");
            malePie.setVisibility(View.GONE);
            percentageBuyer.setText(R.string.no_data);
            percentageBuyer.setTextColor(gredyColor);
            buyerCountIcon.setVisibility(View.GONE);
            headerPieBuyerData.setVisibility(View.GONE);
//            malePie.setText(String.format("%.2f%% Pria", 0.0f));
//            femalePie.setText(String.format("%.2f%%", 0.0f));

            return;
        }else{
            femalePie.setTextColor(arrowDown);

            headerPieBuyerData.setVisibility(View.VISIBLE);
            malePie.setVisibility(View.VISIBLE);
            femalePie.setVisibility(View.VISIBLE);

            double malePercentage = (double)getBuyerData.getMaleBuyer() / (double)getBuyerData.getTotalBuyer();
            double malePercent = Math.floor((malePercentage * 100) + 0.5);

            double femalePercentage = (double)getBuyerData.getFemaleBuyer() /  (double)getBuyerData.getTotalBuyer();
            double femalePercent = Math.floor((femalePercentage * 100) + 0.5);

            String biggerGender = "";
            if(malePercent >= femalePercent){
                biggerGender += gender[0];
                headerPieBuyerData.setText(biggerGender);
//                malePie.setText(String.format(locale, "%.2f%% %s", femalePercent, gender[1]));
//                femalePie.setText(String.format(locale, "%.2f%%", malePercent));
                malePie.setText(String.format(locale, "%3d%% %s", (int)femalePercent, gender[1]));
                femalePie.setText(String.format(locale, "%3d%%", (int)malePercent));
                buyerDataPieChart.setProgress((float) malePercent);
            }else{
                biggerGender += gender[1];
                headerPieBuyerData.setText(biggerGender);
//                malePie.setText(String.format(locale, "%.2f%% %s", malePercent, gender[0]));
//                femalePie.setText(String.format(locale, "%.2f%%", femalePercent));
                malePie.setText(String.format(locale, "%3d%% %s", (int)malePercent, gender[0]));
                femalePie.setText(String.format(locale, "%3d%%", (int)femalePercent));
                buyerDataPieChart.setProgress((float) femalePercent);
            }
        }

        buyerCount.setText(getFormattedString(getBuyerData.getTotalBuyer()));

        double percentage = getBuyerData.getDiffTotal() * 100D;
        // image for arrow is here
        boolean isDefault;
        if(percentage == 0){
            buyerCountIcon.setVisibility(View.GONE);
            percentageBuyer.setTextColor(arrowUp);
            isDefault = true;
        }else if(percentage < 0){// down here
            if(percentage == NoDataAvailable*100){
                buyerCountIcon.setVisibility(View.GONE);
                percentageBuyer.setTextColor(gredyColor);
                isDefault = false;
            }else{
                buyerCountIcon.setVisibility(View.VISIBLE);
                buyerCountIcon.setImageDrawable(icRectagleDown);
//            imageHandler.loadImage(buyerCountIcon, R.mipmap.arrow_down_percentage);
                percentageBuyer.setTextColor(arrowDown);
                isDefault = true;
            }
        }else{// up here
            buyerCountIcon.setVisibility(View.VISIBLE);
            buyerCountIcon.setImageDrawable(icRectagleUp);
//            imageHandler.loadImage(buyerCountIcon, R.mipmap.arrow_up_percentage);
            percentageBuyer.setTextColor(arrowUp);
            isDefault = true;
        }

        if(isDefault){
            double d = percentage;
            percentageBuyer.setText(String.format("%s%%", KMNumbers.formatString(d).replace("-", "")));

//            DecimalFormat formatter = new DecimalFormat("#0.00");
//            String text;
//            System.out.println(text = formatter.format(percentage));
//            percentageBuyer.setText(String.format("%s%%", text.replace("-", "")));
        }else{
            percentageBuyer.setText(R.string.no_data);
        }

        // set icon up or down or netral
    }
}
