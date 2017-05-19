package com.tokopedia.tkpd.tkpdfeed.feedplus.view.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.FeedPlusDetailFragment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.FeedPlusFragment;

import dagger.Component;

/**
 * @author by nisie on 5/15/17.
 */

@FeedPlusScope
@Component(modules = FeedPlusModule.class, dependencies = AppComponent.class)
public interface FeedPlusComponent {

    void inject(FeedPlusFragment feedPlusFragment);

    void inject(FeedPlusDetailFragment feedPlusDetailFragment);

}
