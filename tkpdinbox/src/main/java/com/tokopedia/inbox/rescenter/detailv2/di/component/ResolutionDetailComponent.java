package com.tokopedia.inbox.rescenter.detailv2.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.di.module.ResolutionDetailModule;
import com.tokopedia.inbox.rescenter.detailv2.di.scope.ResolutionDetailScope;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.UploadImageRepository;
import com.tokopedia.inbox.rescenter.detailv2.view.DetailResCenterFragment;
import com.tokopedia.inbox.rescenter.detailv2.view.fragment.DetailResChatFragment;
import com.tokopedia.inbox.rescenter.detailv2.view.fragment.NextActionFragment;
import com.tokopedia.inbox.rescenter.detailv2.view.fragment.TrackShippingFragment;
import com.tokopedia.inbox.rescenter.historyawb.domain.interactor.TrackAwbReturProductUseCase;

import dagger.Component;

/**
 * Created by hangnadi on 4/11/17.
 */
@ResolutionDetailScope
@Component(modules = ResolutionDetailModule.class, dependencies = AppComponent.class)
public interface ResolutionDetailComponent {

    void inject(DetailResCenterFragment fragment);

    void inject(DetailResChatFragment fragment);

    void inject(NextActionFragment fragment);

    void inject(TrackShippingFragment fragment);

    TrackAwbReturProductUseCase trackAwbReturProductUseCase();

    ResCenterRepository resCenterRepository();

    UploadImageRepository uploadImageRepository();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

}
