package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.SendReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.GetSendReviewFormUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SetReviewFormCacheUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationForm;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.SendReviewSubscriber;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.sendreview.SendReviewPass;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 8/20/17.
 */

public class InboxReputationFormPresenter
        extends BaseDaggerPresenter<InboxReputationForm.View>
        implements InboxReputationForm.Presenter {

    private final SendReviewUseCase sendReviewUseCase;
    private final GlobalCacheManager globalCacheManager;
    private final SetReviewFormCacheUseCase setReviewFormCacheUseCase;
    private final GetSendReviewFormUseCase getSendReviewFormUseCase;
    private InboxReputationForm.View viewListener;
    private ImageUploadHandler imageUploadHandler;


    @Inject
    InboxReputationFormPresenter(SendReviewUseCase sendReviewUseCase,
                                 GlobalCacheManager globalCacheManager,
                                 SetReviewFormCacheUseCase setReviewFormCacheUseCase,
                                 GetSendReviewFormUseCase getSendReviewFormUseCase) {
        this.sendReviewUseCase = sendReviewUseCase;
        this.globalCacheManager = globalCacheManager;
        this.setReviewFormCacheUseCase = setReviewFormCacheUseCase;
        this.getSendReviewFormUseCase = getSendReviewFormUseCase;
    }

    @Override
    public void attachView(InboxReputationForm.View view) {
        super.attachView(view);
        this.viewListener = view;
        imageUploadHandler = ImageUploadHandler.createInstance(viewListener.getActivity());

    }

    @Override
    public void detachView() {
        super.detachView();
        sendReviewUseCase.unsubscribe();
    }

    public void sendReview(String reviewId, String reputationId, String productId, String shopId,
                           String review, float rating, ArrayList<ImageUpload> list,
                           boolean shareFb, boolean anonymous) {
        viewListener.showLoadingProgress();
        sendReviewUseCase.execute(SendReviewUseCase.getParam(reviewId,
                productId,
                reputationId,
                shopId,
                String.valueOf(rating),
                review), new SendReviewSubscriber(viewListener));

    }

    public void openCamera() {
        imageUploadHandler.actionCamera();
    }

    @Override
    public void openImageGallery() {
        imageUploadHandler.actionImagePicker();
    }

    @Override
    public void setFormToCache(int position, SendReviewPass sendReviewPass) {
        setReviewFormCacheUseCase.execute(SetReviewFormCacheUseCase.getParam(
                sendReviewPass));
    }

    @Override
    public String getFileLocFromCamera() {
        if (imageUploadHandler != null)
            return imageUploadHandler.getCameraFileloc();
        else
            return "";
    }

    @Override
    public void restoreFormFromCache() {
        getSendReviewFormUseCase.execute(RequestParams.EMPTY, new Subscriber<SendReviewPass>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SendReviewPass sendReviewPass) {
                viewListener.setFormFromCache(sendReviewPass);

            }
        });
    }
}
