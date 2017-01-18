package com.tokopedia.seller.gmstat.views.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.library.LoaderImageView;
import com.tokopedia.seller.gmstat.library.LoaderTextView;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class LoadingGMGrossIncome extends RecyclerView.ViewHolder{
    LoaderTextView grossIncomeHeader;
    LoaderTextView text;
    LoaderImageView dot;
    LoaderTextView textDescription;

    void initView(View itemView){
        grossIncomeHeader = (LoaderTextView) itemView.findViewById(R.id.grossIncomeHeader);
        text= (LoaderTextView) itemView.findViewById(R.id.text);
        dot= (LoaderImageView) itemView.findViewById(R.id.dot);
        textDescription= (LoaderTextView) itemView.findViewById(R.id.textDescription);
    }

    public LoadingGMGrossIncome(View itemView) {
        super(itemView);
        initView(itemView);

        grossIncomeHeader.resetLoader();
        text.resetLoader();
        dot.resetLoader();
        textDescription.resetLoader();
    }
}
