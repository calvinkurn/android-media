package com.tokopedia.inbox.rescenter.detailv2.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.rescenter.detailv2.di.module.ResolutionDetailModule;
import com.tokopedia.inbox.rescenter.detailv2.di.scope.ResolutionDetailScope;
import com.tokopedia.inbox.rescenter.detailv2.view.DetailResCenterFragment;
import com.tokopedia.inbox.rescenter.discussion.view.fragment.ResCenterDiscussionFragment;
import com.tokopedia.inbox.rescenter.historyaction.HistoryActionFragment;
import com.tokopedia.inbox.rescenter.historyaddress.HistoryAddressFragment;
import com.tokopedia.inbox.rescenter.historyawb.HistoryShippingFragment;
import com.tokopedia.inbox.rescenter.product.ListProductFragment;
import com.tokopedia.inbox.rescenter.product.ProductDetailFragment;

import dagger.Component;

/**
 * Created by hangnadi on 4/11/17.
 */
@ResolutionDetailScope
@Component(modules = ResolutionDetailModule.class, dependencies = AppComponent.class)
public interface ResolutionDetailComponent {
    void inject(DetailResCenterFragment fragment);

    void inject(HistoryActionFragment fragment);

    void inject(HistoryAddressFragment fragment);

    void inject(HistoryShippingFragment fragment);

    void inject(ListProductFragment fragment);

    void inject(ProductDetailFragment fragment);

    void inject(ResCenterDiscussionFragment fragment);
}
