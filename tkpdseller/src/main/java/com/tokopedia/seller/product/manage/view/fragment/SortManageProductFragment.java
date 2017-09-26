package com.tokopedia.seller.product.manage.view.fragment;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.product.manage.view.adapter.SortMangeProductAdapter;
import com.tokopedia.seller.product.manage.view.model.SortManageProductModel;
import com.tokopedia.seller.product.manage.view.presenter.SortManageProductPresenter;

import java.util.ArrayList;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class SortManageProductFragment extends BaseListFragment<SortManageProductPresenter, SortManageProductModel> {
    @Override
    protected BaseListAdapter<SortManageProductModel> getNewAdapter() {
        return new SortMangeProductAdapter();
    }

    @Override
    protected void searchForPage(int page) {
        onSearchLoaded(new ArrayList<SortManageProductModel>(), 0);
    }

    @Override
    public void onItemClicked(SortManageProductModel sortManageProductModel) {

    }

    @Override
    protected boolean hasNextPage() {
        return false;
    }
}
