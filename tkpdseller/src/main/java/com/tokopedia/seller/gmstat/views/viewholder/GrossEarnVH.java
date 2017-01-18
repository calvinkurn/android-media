package com.tokopedia.seller.gmstat.views.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.views.models.GrossIncome;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class GrossEarnVH extends RecyclerView.ViewHolder{

    void initView(View itemView){
        text= (TextView) itemView.findViewById(R.id.text);
        textDescription = (TextView) itemView.findViewById(R.id.textDescription);
        dot= (ImageView) itemView.findViewById(R.id.dot);
    }

    TextView text;

    TextView textDescription;

    ImageView dot;

    public GrossEarnVH(View itemView) {
        super(itemView);
        initView(itemView);
    }

    public void bind(GrossIncome grossIncome){
        text.setText(grossIncome.text);
        textDescription.setText(grossIncome.textDescription);
    }
}
