package com.tokopedia.seller.product.manage.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.manage.view.listener.SortManageProductView;
import com.tokopedia.seller.product.manage.view.model.SortManageProductModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class SortManageProductPresenter extends BaseDaggerPresenter<SortManageProductView> {

    private final Context context;

    @Inject
    public SortManageProductPresenter(@ApplicationContext Context context) {
        this.context = context;
    }

    public void getListSortManageProduct(String[] stringArray) {
        List<SortManageProductModel> sortManageProductModels = new ArrayList<>();
        for(String title : stringArray){
            SortManageProductModel sortManageProductModel = new SortManageProductModel();
            if(title.equals(context.getString(R.string.sort_position))){
                sortManageProductModel.setSortId(SortProductOption.POSITION);
                sortManageProductModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_new_product))){
                sortManageProductModel.setSortId(SortProductOption.NEW_PRODUCT);
                sortManageProductModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_last))){
                sortManageProductModel.setSortId(SortProductOption.LAST_UPDATE);
                sortManageProductModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_product_name))){
                sortManageProductModel.setSortId(SortProductOption.PRODUCT_NAME);
                sortManageProductModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_most_viewed))){
                sortManageProductModel.setSortId(SortProductOption.MOST_VIEW);
                sortManageProductModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_most_discussed))){
                sortManageProductModel.setSortId(SortProductOption.MOST_TALK);
                sortManageProductModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_most_reviewed))){
                sortManageProductModel.setSortId(SortProductOption.MOST_REVIEW);
                sortManageProductModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_most_buy))){
                sortManageProductModel.setSortId(SortProductOption.MOST_BUY);
                sortManageProductModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_lowest_price))){
                sortManageProductModel.setSortId(SortProductOption.LOWEST_PRICE);
                sortManageProductModel.setTitleSort(title);
            }else if(title.equals(context.getString(R.string.sort_highest_price))){
                sortManageProductModel.setSortId(SortProductOption.HIGHEST_PRICE);
                sortManageProductModel.setTitleSort(title);
            }else{
                sortManageProductModel.setSortId(SortProductOption.POSITION);
                sortManageProductModel.setTitleSort(title);
            }
            sortManageProductModels.add(sortManageProductModel);
        }
        getView().onSuccessGetListSort(sortManageProductModels);
    }
}
