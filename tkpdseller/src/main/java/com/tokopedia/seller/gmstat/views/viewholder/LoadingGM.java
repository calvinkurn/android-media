package com.tokopedia.seller.gmstat.views.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.library.LoaderTextView;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class LoadingGM extends RecyclerView.ViewHolder{

    LoaderTextView textDescription;

    LoaderTextView text;

    LoaderTextView noDataText;

    void initView(View itemView){
        textDescription = (LoaderTextView) itemView.findViewById(R.id.textDescription);
        text = (LoaderTextView) itemView.findViewById(R.id.text);
        noDataText = (LoaderTextView) itemView.findViewById(R.id.no_data_text);
    }

    public LoadingGM(View itemView) {
        super(itemView);
        initView(itemView);

        textDescription.resetLoader();
        text.resetLoader();
        noDataText.resetLoader();
    }
}
