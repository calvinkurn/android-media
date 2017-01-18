package com.tokopedia.seller.gmstat.views.helper;

import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.library.LoaderImageView;
import com.tokopedia.seller.gmstat.library.LoaderTextView;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class PopularProductLoading {
    LoaderTextView popularProductDescription;

    LoaderTextView textPopularProduct;

    LoaderImageView imagePopularProduct;

    LoaderTextView dataProductTitle;

    LoaderImageView dataProductIcon;

    View parentView;

    void initView(View itemView){
        popularProductDescription = (LoaderTextView) itemView.findViewById(R.id.popular_product_description_loading);
        textPopularProduct = (LoaderTextView) itemView.findViewById(R.id.text_popular_product_loading);
        imagePopularProduct = (LoaderImageView) itemView.findViewById(R.id.image_popular_product_loading);
        dataProductTitle = (LoaderTextView) itemView.findViewById(R.id.data_product_title_loading);
        dataProductIcon = (LoaderImageView) itemView.findViewById(R.id.data_product_icon_loading);
    }

    public PopularProductLoading(View itemView){
        initView(itemView);

        parentView = itemView.findViewById(R.id.popular_product_loading);

        popularProductDescription.resetLoader();
        textPopularProduct.resetLoader();
        imagePopularProduct.resetLoader();
        dataProductTitle.resetLoader();
        dataProductIcon.resetLoader();
    }

    public void displayLoading() {
        parentView.setVisibility(View.VISIBLE);
    }

    public void hideLoading(){
        parentView.setVisibility(View.GONE);
    }
}
