package com.tokopedia.inbox.rescenter.detailv2.di.module;

import android.content.Context;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResCenterActApi;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.apiservices.upload.apis.GeneratedHostActApi;
import com.tokopedia.core.network.apiservices.user.apis.InboxResCenterApi;
import com.tokopedia.core.network.di.qualifier.ResolutionQualifier;
import com.tokopedia.core.network.di.qualifier.UploadWsV4Qualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.inbox.rescenter.detailv2.data.factory.ResCenterDataSourceFactory;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.repository.ResCenterRepositoryImpl;
import com.tokopedia.inbox.rescenter.detailv2.di.scope.ResolutionDetailScope;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.UploadImageRepository;
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
import com.tokopedia.inbox.rescenter.discussion.data.mapper.CreatePictureMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.DiscussionResCenterMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.LoadMoreMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.ReplyResolutionMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.ReplyResolutionSubmitMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.SubmitImageMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.UploadImageMapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.UploadImageV2Mapper;
import com.tokopedia.inbox.rescenter.discussion.data.mapper.UploadVideoMapper;
import com.tokopedia.inbox.rescenter.discussion.data.repository.UploadImageRepositoryImpl;
import com.tokopedia.inbox.rescenter.discussion.data.source.UploadImageSourceFactory;
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
@Module
public class ResolutionDetailModule {

    private DetailResCenterFragmentView viewListener;

    public ResolutionDetailModule() {

    }

    public ResolutionDetailModule(DetailResCenterFragmentView viewListener) {
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
    TrackAwbReturProductUseCase provideTrackAwbReturProductUseCase(ThreadExecutor threadExecutor,
                                                                   PostExecutionThread postExecutionThread,
                                                                   ResCenterRepository resCenterRepository) {
        return new TrackAwbReturProductUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository
        );
    }

    @ResolutionDetailScope
    @Provides
    ResCenterRepository provideResCenterRepository(ResCenterDataSourceFactory resCenterDataSourceFactory) {
        return new ResCenterRepositoryImpl(resCenterDataSourceFactory);
    }

    @ResolutionDetailScope
    @Provides
    ResCenterDataSourceFactory provideResCenterDataSourceFactory(
            @ApplicationContext Context context,
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
            LoadMoreMapper loadMoreMapper,
            ReplyResolutionMapper replyResolutionMapper,
            ReplyResolutionSubmitMapper replyResolutionSubmitMapper) {

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
                loadMoreMapper,
                replyResolutionMapper,
                replyResolutionSubmitMapper
        );
    }

    @ResolutionDetailScope
    @Provides
    GeneratedHostActApi provideGeneratedHostActApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(GeneratedHostActApi.class);
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
    ResCenterActApi provideResCenterActService(@UploadWsV4Qualifier Retrofit retrofit) {
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

    @ResolutionDetailScope
    @Provides
    UploadImageRepository provideUploadImageRepository(UploadImageSourceFactory uploadImageSourceFactory) {
        return new UploadImageRepositoryImpl(uploadImageSourceFactory);
    }

    @ResolutionDetailScope
    @Provides
    UploadImageSourceFactory provideUploadImageSourceFactory(@ApplicationContext Context context,
                                                             GeneratedHostActApi generatedHostActApi,
                                                             ResCenterActApi resCenterActApi,
                                                             GenerateHostMapper generateHostMapper,
                                                             UploadImageMapper uploadImageMapper,
                                                             UploadImageV2Mapper uploadImageV2Mapper,
                                                             UploadVideoMapper uploadVideoMapper,
                                                             CreatePictureMapper createPictureMapper,
                                                             SubmitImageMapper submitImageMapper) {
        return new UploadImageSourceFactory(
                context,
                generatedHostActApi,
                resCenterActApi,
                generateHostMapper,
                uploadImageMapper,
                uploadImageV2Mapper,
                uploadVideoMapper,
                createPictureMapper,
                submitImageMapper
        );
    }

    @ResolutionDetailScope
    @Provides
    GenerateHostActService provideGenerateHostActService() {
        return new GenerateHostActService();
    }

    @ResolutionDetailScope
    @Provides
    GenerateHostMapper provideGenerateHostMapper() {
        return new GenerateHostMapper();
    }

    @ResolutionDetailScope
    @Provides
    UploadImageMapper provideUploadImageMapper() {
        return new UploadImageMapper();
    }

    @ResolutionDetailScope
    @Provides
    UploadImageV2Mapper provideUploadImageV2Mapper() {
        return new UploadImageV2Mapper();
    }

    @ResolutionDetailScope
    @Provides
    UploadVideoMapper provideUploadVideoMapper() {
        return new UploadVideoMapper();
    }

    @ResolutionDetailScope
    @Provides
    CreatePictureMapper provideCreatePictureMapper() {
        return new CreatePictureMapper();
    }

    @ResolutionDetailScope
    @Provides
    SubmitImageMapper provideSubmitImageMapper() {
        return new SubmitImageMapper();
    }

    @ResolutionDetailScope
    @Provides
    ReplyResolutionMapper provideReplyResolutionMapper() {
        return new ReplyResolutionMapper();
    }

    @ResolutionDetailScope
    @Provides
    ReplyResolutionSubmitMapper provideReplyResolutionSubmitMapper() {
        return new ReplyResolutionSubmitMapper();
    }

}
