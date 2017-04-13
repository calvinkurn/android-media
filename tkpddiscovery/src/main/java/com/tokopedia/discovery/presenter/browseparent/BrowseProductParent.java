package com.tokopedia.discovery.presenter.browseparent;

import android.content.Context;

import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.session.base.BaseImpl;
import com.tokopedia.discovery.model.NetworkParam;
import com.tokopedia.discovery.view.BrowseProductParentView;

import java.util.List;

/**
 * Created by Erry on 6/30/2016.
 */
public abstract class BrowseProductParent extends BaseImpl<BrowseProductParentView> {

    public BrowseProductParent(BrowseProductParentView view) {
        super(view);
    }

    @Override
    public String getMessageTAG() {
        return null;
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return null;
    }

    public abstract BrowseProductModel getDataForBrowseProduct();

    public abstract NetworkParam.Product getProductParam();

    public abstract List<Breadcrumb> getBreadCrumb();

    public abstract void fetchFromNetwork(Context context);
}
