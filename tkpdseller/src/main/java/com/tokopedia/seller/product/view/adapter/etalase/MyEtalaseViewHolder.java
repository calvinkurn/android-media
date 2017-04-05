package com.tokopedia.seller.product.view.adapter.etalase;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.etalase.EtalaseViewModel;
import com.tokopedia.seller.product.view.model.etalase.MyEtalaseViewModel;

/**
 * Created by sebastianuskh on 4/5/17.
 */

class MyEtalaseViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemName;

    public MyEtalaseViewHolder(View view) {
        super(view);
        itemName = (TextView) view.findViewById(R.id.etalase_picker_item_name);
    }

    public void renderView(MyEtalaseViewModel etalaseViewModel) {
        itemName.setText(etalaseViewModel.getEtalaseName());
    }
}
