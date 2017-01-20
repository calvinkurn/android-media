package com.tokopedia.seller.gmstat.views.viewholder;

import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.presenters.GMStat;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.gmstat.views.models.CommomGMModel;

import static com.tokopedia.seller.gmstat.views.GMStatActivityFragment.NoDataAvailable;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class CommonGMVH extends RecyclerView.ViewHolder{

    void initView(View itemView){
        text = (TextView) itemView.findViewById(R.id.text);
        textDescription = (TextView) itemView.findViewById(R.id.textDescription);
        percentage = (TextView) itemView.findViewById(R.id.percentage);
        arrowIcon = (ImageView) itemView.findViewById(R.id.arrow_icon);
        arrowDown = ResourcesCompat.getColor(itemView.getResources(), R.color.arrow_down, null);
        arrowUp = ResourcesCompat.getColor(itemView.getResources(), R.color.arrow_up, null);
        gredyColor = ResourcesCompat.getColor(itemView.getResources(), R.color.grey_400, null);
    }

    TextView text;

    TextView textDescription;

    TextView percentage;

    ImageView arrowIcon;

    int arrowDown;

    int arrowUp;

    int gredyColor;

    //        @BindDrawable(R.drawable.ic_rectangle_down)
    Drawable icRectagleDown;

    //        @BindDrawable(R.drawable.ic_rectangle_up)
    Drawable icRectagleUp;

    public GMStat gmStat;

    public CommonGMVH(View itemView) {
        super(itemView);
        initView(itemView);

        icRectagleDown = AppCompatDrawableManager.get().getDrawable(itemView.getContext(),
                R.drawable.ic_rectangle_down);
        icRectagleUp = AppCompatDrawableManager.get().getDrawable(itemView.getContext(),
                R.drawable.ic_rectangle_up);
    }

    public void bind(CommomGMModel commomGMModel){
        text.setText(commomGMModel.text);
        textDescription.setText(commomGMModel.textDescription);


        // image for arrow is here
        boolean isDefault;
        if(commomGMModel.percentage == 0){
            arrowIcon.setVisibility(View.GONE);
            percentage.setTextColor(arrowUp);
            isDefault = true;
        }else if(commomGMModel.percentage < 0){// down here
            if(commomGMModel.percentage == NoDataAvailable*100){
                arrowIcon.setVisibility(View.GONE);
                percentage.setTextColor(gredyColor);
                isDefault = false;
            }else{
                arrowIcon.setVisibility(View.VISIBLE);
                arrowIcon.setImageDrawable(icRectagleDown);
//                gmStat.getImageHandler().loadImage(arrowIcon, R.mipmap.arrow_down_percentage);
                percentage.setTextColor(arrowDown);
                isDefault = true;
            }
        }else{// up here
            arrowIcon.setVisibility(View.VISIBLE);
            arrowIcon.setImageDrawable(icRectagleUp);
//                gmStat.getImageHandler().loadImage(arrowIcon, R.mipmap.arrow_up_percentage);
            percentage.setTextColor(arrowUp);
            isDefault = true;
        }

        if(isDefault) {
//                DecimalFormat formatter = new DecimalFormat("#0.00");
//                double d = commomGMModel.percentage;
//                String text;
//                System.out.println(text = formatter.format(d));

            double d = commomGMModel.percentage;
            percentage.setText(String.format("%s%%", KMNumbers.formatString(d).replace("-", "")));
        }else{
            percentage.setText(R.string.no_data);
        }
    }
}
