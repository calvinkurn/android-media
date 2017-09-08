package com.tokopedia.seller.goldmerchant.featured.view.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.featured.di.component.DaggerFeaturedProductComponent;
import com.tokopedia.seller.goldmerchant.featured.domain.interactor.FeaturedProductPOSTUseCase;
import com.tokopedia.seller.goldmerchant.featured.helper.ItemTouchHelperAdapter;
import com.tokopedia.seller.goldmerchant.featured.helper.OnStartDragListener;
import com.tokopedia.seller.goldmerchant.featured.helper.SimpleItemTouchHelperCallback;
import com.tokopedia.seller.goldmerchant.featured.view.adapter.FeaturedProductAdapter;
import com.tokopedia.seller.goldmerchant.featured.view.adapter.model.FeaturedProductModel;
import com.tokopedia.seller.goldmerchant.featured.view.listener.FeaturedProductView;
import com.tokopedia.seller.goldmerchant.featured.view.presenter.FeaturedProductPresenterImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class FeaturedProductFragment extends BaseListFragment<BlankPresenter, FeaturedProductModel> implements FeaturedProductView, OnStartDragListener, FeaturedProductAdapter.UseCaseListener {

    @Inject
    FeaturedProductPresenterImpl featuredProductPresenter;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void initInjector() {
        DaggerFeaturedProductComponent
                .builder()
                .goldMerchantComponent(getComponent(GoldMerchantComponent.class))
                .build().inject(this);
        featuredProductPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        featuredProductPresenter.detachView();
    }

    @Override
    public void onItemClicked(FeaturedProductModel featuredProductModel) {

    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback((ItemTouchHelperAdapter) adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected BaseListAdapter<FeaturedProductModel> getNewAdapter() {
        FeaturedProductAdapter featuredProductAdapter = new FeaturedProductAdapter(this);
        featuredProductAdapter.setUseCaseListener(this);
        return featuredProductAdapter;
    }

    @Override
    protected void searchForPage(int page) {
        featuredProductPresenter.loadData();
    }

    @Override
    public void onPostSuccess() {

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


    @Override
    public void postData(List<FeaturedProductModel> data) {
        featuredProductPresenter.postData(FeaturedProductPOSTUseCase.createParam(data));
    }
}
