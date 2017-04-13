package com.tokopedia.inbox.rescenter.detailv2.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResCenterActApi;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.core.network.apiservices.user.apis.InboxResCenterApi;
import com.tokopedia.core.network.di.qualifier.ResolutionQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.inbox.rescenter.detailv2.data.factory.ResCenterDataSourceFactory;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.repository.ResCenterRepositoryImpl;
import com.tokopedia.inbox.rescenter.detailv2.di.scope.ResolutionDetailScope;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.AcceptAdminSolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.AcceptSolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.AskHelpResolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.CancelResolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.EditAddressUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.FinishReturSolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResCenterDetailUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.InputAddressUseCase;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.DetailResCenterFragmentImpl;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.DiscussionResCenterMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.LoadMoreMapper;
import com.tokopedia.inbox.rescenter.historyaction.data.mapper.HistoryActionMapper;
import com.tokopedia.inbox.rescenter.historyaddress.data.mapper.HistoryAddressMapper;
import com.tokopedia.inbox.rescenter.historyawb.data.mapper.HistoryAwbMapper;
import com.tokopedia.inbox.rescenter.historyawb.domain.interactor.TrackAwbReturProductUseCase;
import com.tokopedia.inbox.rescenter.product.data.mapper.ListProductMapper;
import com.tokopedia.inbox.rescenter.product.data.mapper.ProductDetailMapper;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by hangnadi on 4/11/17.
 */
@ResolutionDetailScope
@Module(includes = {HistoryActionModule.class})
public class ResolutionDetailModule {

    private String resolutionID;
    private DetailResCenterFragmentView viewListener;

    public ResolutionDetailModule(String resolutionID) {
        this.resolutionID = resolutionID;
    }

    public ResolutionDetailModule(String resolutionID, DetailResCenterFragmentView viewListener) {
        this.resolutionID = resolutionID;
        this.viewListener = viewListener;
    }

    @ResolutionDetailScope
    @Provides
    DetailResCenterFragmentImpl provideDetailResCenterFragmentPresenter(
            GetResCenterDetailUseCase getResCenterDetailUseCase,
            TrackAwbReturProductUseCase trackAwbReturProductUseCase,
            CancelResolutionUseCase cancelResolutionUseCase,
            AskHelpResolutionUseCase askHelpResolutionUseCase,
            FinishReturSolutionUseCase finishReturSolutionUseCase,
            AcceptAdminSolutionUseCase acceptAdminSolutionUseCase,
            AcceptSolutionUseCase acceptSolutionUseCase,
            InputAddressUseCase inputAddressUseCase,
            EditAddressUseCase editAddressUseCase) {
        return new DetailResCenterFragmentImpl(
                viewListener,
                getResCenterDetailUseCase,
                trackAwbReturProductUseCase,
                cancelResolutionUseCase,
                askHelpResolutionUseCase,
                finishReturSolutionUseCase,
                acceptAdminSolutionUseCase,
                acceptSolutionUseCase,
                inputAddressUseCase,
                editAddressUseCase
        );
    }

    @ResolutionDetailScope
    @Provides
    GetResCenterDetailUseCase provideGetResCenterDetailUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ResCenterRepository resCenterRepository) {
        return new GetResCenterDetailUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository);
    }

    @ResolutionDetailScope
    @Provides
    TrackAwbReturProductUseCase provideTrackAwbReturProductUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ResCenterRepository resCenterRepository) {
        return new TrackAwbReturProductUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository);
    }

    @ResolutionDetailScope
    @Provides
    CancelResolutionUseCase provideCancelResolutionUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ResCenterRepository resCenterRepository) {
        return new CancelResolutionUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository);
    }

    @ResolutionDetailScope
    @Provides
    AskHelpResolutionUseCase provideAskHelpResolutionUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ResCenterRepository resCenterRepository) {
        return new AskHelpResolutionUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository);
    }

    @ResolutionDetailScope
    @Provides
    FinishReturSolutionUseCase provideFinishReturSolutionUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ResCenterRepository resCenterRepository) {
        return new FinishReturSolutionUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository);
    }

    @ResolutionDetailScope
    @Provides
    AcceptAdminSolutionUseCase provideAcceptAdminSolutionUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ResCenterRepository resCenterRepository) {
        return new AcceptAdminSolutionUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository);
    }

    @ResolutionDetailScope
    @Provides
    AcceptSolutionUseCase provideAcceptSolutionUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ResCenterRepository resCenterRepository) {
        return new AcceptSolutionUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository);
    }

    @ResolutionDetailScope
    @Provides
    InputAddressUseCase provideInputAddressUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ResCenterRepository resCenterRepository) {
        return new InputAddressUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository);
    }

    @ResolutionDetailScope
    @Provides
    EditAddressUseCase provideEditAddressUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ResCenterRepository resCenterRepository) {
        return new EditAddressUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository);
    }

    @ResolutionDetailScope
    @Provides
    ResCenterRepository provideResCenterRepository(ResCenterDataSourceFactory resCenterDataSourceFactory) {
        return new ResCenterRepositoryImpl(resolutionID, resCenterDataSourceFactory);
    }

    @ResolutionDetailScope
    @Provides
    ResCenterDataSourceFactory provideResCenterDataSourceFactory(
            @ActivityContext Context context,
            ResolutionApi resolutionApi,
            InboxResCenterApi inboxResCenterApi,
            ResCenterActApi resCenterActApi,
            DetailResCenterMapper detailResCenterMapper,
            HistoryAwbMapper historyAwbMapper,
            HistoryAddressMapper historyAddressMapper,
            HistoryActionMapper historyActionMapper,
            ListProductMapper listProductMapper,
            ProductDetailMapper productDetailMapper,
            DiscussionResCenterMapper discussionResCenterMapper,
            LoadMoreMapper loadMoreMapper) {

        return new ResCenterDataSourceFactory(
                context,
                resolutionApi,
                inboxResCenterApi,
                resCenterActApi,
                detailResCenterMapper,
                historyAwbMapper,
                historyAddressMapper,
                historyActionMapper,
                listProductMapper,
                productDetailMapper,
                discussionResCenterMapper,
                loadMoreMapper
        );
    }

    @ResolutionDetailScope
    @Provides
    ResolutionApi provideResolutionService(@ResolutionQualifier Retrofit retrofit) {
        return retrofit.create(ResolutionApi.class);
    }

    @ResolutionDetailScope
    @Provides
    InboxResCenterApi provideInboxResCenterService(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(InboxResCenterApi.class);
    }

    @ResolutionDetailScope
    @Provides
    ResCenterActApi provideResCenterActService(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(ResCenterActApi.class);
    }

    @ResolutionDetailScope
    @Provides
    DetailResCenterMapper provideDetailResCenterMapper() {
        return new DetailResCenterMapper();
    }

    @ResolutionDetailScope
    @Provides
    HistoryAwbMapper provideHistoryAwbMapper() {
        return new HistoryAwbMapper();
    }

    @ResolutionDetailScope
    @Provides
    HistoryAddressMapper provideHistoryAddressMapper() {
        return new HistoryAddressMapper();
    }

    @ResolutionDetailScope
    @Provides
    HistoryActionMapper provideHistoryActionMapper() {
        return new HistoryActionMapper();
    }

    @ResolutionDetailScope
    @Provides
    ListProductMapper provideListProductMapper() {
        return new ListProductMapper();
    }

    @ResolutionDetailScope
    @Provides
    ProductDetailMapper provideProductDetailMapper() {
        return new ProductDetailMapper();
    }

    @ResolutionDetailScope
    @Provides
    DiscussionResCenterMapper provideDiscussionResCenterMapper() {
        return new DiscussionResCenterMapper();
    }

    @ResolutionDetailScope
    @Provides
    LoadMoreMapper provideLoadMoreMapper() {
        return new LoadMoreMapper();
    }
}
