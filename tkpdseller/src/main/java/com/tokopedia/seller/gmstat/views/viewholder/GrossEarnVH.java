package com.tokopedia.seller.gmstat.views.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.views.models.GrossIncome;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class GrossEarnVH extends RecyclerView.ViewHolder {

    private TextView text;

    private TextView textDescription;

    public GrossEarnVH(View itemView) {
        super(itemView);
        initView(itemView);
    }

    private void initView(View itemView) {
        text = (TextView) itemView.findViewById(R.id.text);
        textDescription = (TextView) itemView.findViewById(R.id.textDescription);
    }

    public void bind(GrossIncome grossIncome) {
        text.setText(grossIncome.text);
        textDescription.setText(grossIncome.textDescription);
    }
}
