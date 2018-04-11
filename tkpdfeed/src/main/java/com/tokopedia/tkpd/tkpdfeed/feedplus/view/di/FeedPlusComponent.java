package com.tokopedia.tkpd.tkpdfeed.feedplus.view.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.FollowKolPostUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.LikeKolPostUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.FeedPlusDetailFragment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.FeedPlusFragment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.KolCommentFragment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.KolFollowingListFragment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.RecentViewFragment;

import dagger.Component;

/**
 * @author by nisie on 5/15/17.
 */

@FeedPlusScope
@Component(modules = FeedPlusModule.class, dependencies = AppComponent.class)
public interface FeedPlusComponent {

    void inject(FeedPlusFragment feedPlusFragment);

    void inject(FeedPlusDetailFragment feedPlusDetailFragment);

    void inject(RecentViewFragment recentViewFragment);

    void inject(KolCommentFragment kolCommentFragment);

    void inject(KolFollowingListFragment kolFollowingListFragment);

    LikeKolPostUseCase getLikeKolPostUseCase();

    FollowKolPostUseCase getFollowKolPostUseCase();
}
