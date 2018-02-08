package com.tokopedia.home.explore.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.explore.listener.CategoryAdapterListener;
import com.tokopedia.home.explore.view.adapter.viewmodel.MyShopViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.SellViewModel;

/**
 * Created by errysuprayogi on 12/5/17.
 */

public class MyShopViewHolder extends AbstractViewHolder<MyShopViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_my_shop;

    TextView titleTxt;
    ImageView imageView;
    Button button;

    private CategoryAdapterListener listener;

    public MyShopViewHolder(View itemView, final CategoryAdapterListener listener) {
        super(itemView);
        this.listener = listener;
        titleTxt = itemView.findViewById(R.id.title);
        imageView = itemView.findViewById(R.id.image_shop);
        button = itemView.findViewById(R.id.btn_ubah);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.openShopSetting();
            }
        });
    }

    @Override
    public void bind(MyShopViewModel element) {

    }

}
