package com.tokopedia.transaction.checkout.view.holderitemdata;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.data.DropshipperShippingOptionModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Aghny A. Putra on 09/02/18
 */

public class DropshipperOptionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.rl_dropshipper_option_header)
    RelativeLayout mRlDropshipperOptionLayout;
    @BindView(R2.id.ll_detail_dropshipper)
    LinearLayout mLlDetailDropshipperLayout;
    @BindView(R2.id.sw_dropshipper)
    Switch mSwDropshipper;

    DropshipperOptionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bindViewHolder(DropshipperShippingOptionModel model) {
        mLlDetailDropshipperLayout.setVisibility(getVisibility(model.isDropshipping()));
        mSwDropshipper.setChecked(model.isDropshipping());
        mSwDropshipper.setOnClickListener(dropshipperSwitchListener(model));
    }

    private View.OnClickListener dropshipperSwitchListener(final DropshipperShippingOptionModel model) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setDropshipping(!model.isDropshipping());

                mSwDropshipper.setChecked(model.isDropshipping());
                mLlDetailDropshipperLayout.setVisibility(getVisibility(model.isDropshipping()));
            }
        };
    }

    private int getVisibility(boolean isDropshipping) {
        return isDropshipping ? View.VISIBLE : View.GONE;
    }


}
