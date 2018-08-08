package com.tokopedia.seller.product.manage.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.manage.view.listener.ProductManageSortView;
import com.tokopedia.seller.product.manage.view.model.ProductManageSortModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class ProductManageSortPresenter extends BaseDaggerPresenter<ProductManageSortView> {

    private final Context context;

    @Inject
    public ProductManageSortPresenter(@ApplicationContext Context context) {
        this.context = context;
    }

    public void getListSortManageProduct(String[] stringArray) {
        List<ProductManageSortModel> productManageSortModels = new ArrayList<>();
        for(String title : stringArray){
            ProductManageSortModel productManageSortModel = new ProductManageSortModel();
            if(title.equals(context.getString(R.string.sort_position))){
                productManageSortModel.setSortId(SortProductOption.POSITION);
                productManageSortModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_new_product))){
                productManageSortModel.setSortId(SortProductOption.NEW_PRODUCT);
                productManageSortModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_last))){
                productManageSortModel.setSortId(SortProductOption.LAST_UPDATE);
                productManageSortModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_product_name))){
                productManageSortModel.setSortId(SortProductOption.PRODUCT_NAME);
                productManageSortModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_most_viewed))){
                productManageSortModel.setSortId(SortProductOption.MOST_VIEW);
                productManageSortModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_most_discussed))){
                productManageSortModel.setSortId(SortProductOption.MOST_TALK);
                productManageSortModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_most_reviewed))){
                productManageSortModel.setSortId(SortProductOption.MOST_REVIEW);
                productManageSortModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_most_buy))){
                productManageSortModel.setSortId(SortProductOption.MOST_BUY);
                productManageSortModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_lowest_price))){
                productManageSortModel.setSortId(SortProductOption.LOWEST_PRICE);
                productManageSortModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_highest_price))){
                productManageSortModel.setSortId(SortProductOption.HIGHEST_PRICE);
                productManageSortModel.setTitleSort(title);
            }else{
                productManageSortModel.setSortId(SortProductOption.POSITION);
                productManageSortModel.setTitleSort(title);
            }
            productManageSortModels.add(productManageSortModel);
        }
        getView().onSuccessGetListSort(productManageSortModels);
    }
}
