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
import com.tokopedia.seller.gmstat.utils.GMStatConstant;
import com.tokopedia.seller.gmstat.utils.KMNumbers;
import com.tokopedia.seller.gmstat.views.models.CommonGMModel;

import static com.tokopedia.seller.gmstat.utils.GMStatConstant.PERCENTAGE_FORMAT;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class CommonGMVH extends RecyclerView.ViewHolder {

    public GMStat gmStat;
    private TextView text;

    private TextView textDescription;

    private TextView percentage;

    private ImageView arrowIcon;

    private int arrowDown;

    private int arrowUp;

    private int gredyColor;

    private Drawable icRectagleDown;

    private Drawable icRectagleUp;

    public CommonGMVH(View itemView) {
        super(itemView);
        initView(itemView);

        icRectagleDown = AppCompatDrawableManager.get().getDrawable(itemView.getContext(),
                R.drawable.ic_rectangle_down);
        icRectagleUp = AppCompatDrawableManager.get().getDrawable(itemView.getContext(),
                R.drawable.ic_rectangle_up);
    }

    private void initView(View itemView) {
        text = (TextView) itemView.findViewById(R.id.text);
        textDescription = (TextView) itemView.findViewById(R.id.textDescription);
        percentage = (TextView) itemView.findViewById(R.id.percentage);
        arrowIcon = (ImageView) itemView.findViewById(R.id.arrow_icon);
        arrowDown = ResourcesCompat.getColor(itemView.getResources(), R.color.arrow_down, null);
        arrowUp = ResourcesCompat.getColor(itemView.getResources(), R.color.arrow_up, null);
        gredyColor = ResourcesCompat.getColor(itemView.getResources(), R.color.grey_400, null);
    }

    public void bind(CommonGMModel commomGMModel) {
        text.setText(commomGMModel.text);
        textDescription.setText(commomGMModel.textDescription);


        // image for arrow is here
        boolean isDefault;
        if (commomGMModel.percentage == 0) {
            arrowIcon.setVisibility(View.GONE);
            percentage.setTextColor(arrowUp);
            isDefault = true;
        } else if (commomGMModel.percentage < 0) {// down here
            if (commomGMModel.percentage == GMStatConstant.NoDataAvailable * 100) {
                arrowIcon.setVisibility(View.GONE);
                percentage.setTextColor(gredyColor);
                isDefault = false;
            } else {
                arrowIcon.setVisibility(View.VISIBLE);
                arrowIcon.setImageDrawable(icRectagleDown);
                percentage.setTextColor(arrowDown);
                isDefault = true;
            }
        } else {// up here
            arrowIcon.setVisibility(View.VISIBLE);
            arrowIcon.setImageDrawable(icRectagleUp);
            percentage.setTextColor(arrowUp);
            isDefault = true;
        }

        if (isDefault) {
            double d = commomGMModel.percentage;
            percentage.setText(String.format(PERCENTAGE_FORMAT, KMNumbers.formatString(d).replace("-", "")));
        } else {
            percentage.setText(R.string.no_data);
        }
    }
}
