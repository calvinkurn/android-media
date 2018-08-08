package com.tokopedia.seller.product.etalase.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.etalase.view.model.MyEtalaseItemViewModel;

/**
 * @author sebastianuskh on 4/5/17.
 */

class MyEtalaseViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemName;
    private final View etalaseRadioButton;
    private MyEtalaseItemViewModel viewModel;

    public MyEtalaseViewHolder(View view, final EtalasePickerAdapterListener listener) {
        super(view);
        itemName = (TextView) view.findViewById(R.id.etalase_picker_item_name);
        etalaseRadioButton = view.findViewById(R.id.etalase_picker_radio_button);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(listener);
            }
        });
        etalaseRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(listener);
            }
        });
    }

    private void selectItem(EtalasePickerAdapterListener listener) {
        MyEtalaseItemViewModel viewModel = getEtalaseViewModel();
        listener.selectEtalase(viewModel.getEtalaseId(), viewModel.getEtalaseName());
    }

    private MyEtalaseItemViewModel getEtalaseViewModel() {
        if (viewModel == null){
            throw new RuntimeException("ViewModel still empty");
        }
        return viewModel;
    }

    public void renderView(MyEtalaseItemViewModel etalaseViewModel, boolean isSelected) {
        Spanned etalaseName = Html.fromHtml(etalaseViewModel.getEtalaseName());
        itemName.setText(etalaseName);
        this.viewModel = etalaseViewModel;
        if (etalaseRadioButton instanceof RadioButton) {
            ((RadioButton) etalaseRadioButton).setChecked(isSelected);
        } else {
            etalaseRadioButton.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        }

    }
}