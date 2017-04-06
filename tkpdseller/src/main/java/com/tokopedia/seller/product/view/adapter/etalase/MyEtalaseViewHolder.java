package com.tokopedia.seller.product.view.adapter.etalase;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
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
    private MyEtalaseViewModel viewModel;

    public MyEtalaseViewHolder(View view, final EtalasePickerAdapterListener listener) {
        super(view);
        itemName = (TextView) view.findViewById(R.id.etalase_picker_item_name);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyEtalaseViewModel viewModel = getEtalaseViewModel();
                listener.selectEtalase(viewModel.getEtalaseId(), viewModel.getEtalaseName());
            }
        });
    }

    private MyEtalaseViewModel getEtalaseViewModel() {
        if (viewModel == null){
            throw new RuntimeException("ViewModel still empty");
        }
        return viewModel;
    }

    public void renderView(MyEtalaseViewModel etalaseViewModel) {
        Spanned etalaseName = Html.fromHtml(etalaseViewModel.getEtalaseName());
        itemName.setText(etalaseName);
        this.viewModel = etalaseViewModel;
    }
}
