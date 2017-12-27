package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.EditReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.EditReviewValidateUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.GetSendReviewFormUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewValidateUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SetReviewFormCacheUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SkipReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationForm;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.EditReviewSubscriber;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.EditReviewWithoutImageSubscriber;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.SendReviewSubscriber;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.SendReviewWithoutImageSubscriber;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.SkipReviewSubscriber;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ShareModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.sendreview.SendReviewPass;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 8/20/17.
 */

public class InboxReputationFormPresenter
        extends BaseDaggerPresenter<InboxReputationForm.View>
        implements InboxReputationForm.Presenter {

    private final SendReviewUseCase sendReviewUseCase;
    private final SetReviewFormCacheUseCase setReviewFormCacheUseCase;
    private final GetSendReviewFormUseCase getSendReviewFormUseCase;
    private final SendReviewValidateUseCase sendReviewValidateUseCase;
    private final SkipReviewUseCase skipReviewUseCase;
    private final SessionHandler sessionHandler;
    private final EditReviewUseCase editReviewUseCase;
    private final EditReviewValidateUseCase editReviewValidateUseCase;
    private InboxReputationForm.View viewListener;
    private ImageUploadHandler imageUploadHandler;
    private String cameraFileLoc;

    @Inject
    InboxReputationFormPresenter(SendReviewValidateUseCase sendReviewValidateUseCase,
                                 SendReviewUseCase sendReviewUseCase,
                                 SetReviewFormCacheUseCase setReviewFormCacheUseCase,
                                 GetSendReviewFormUseCase getSendReviewFormUseCase,
                                 SkipReviewUseCase skipReviewUseCase,
                                 EditReviewUseCase editReviewUseCase,
                                 EditReviewValidateUseCase editReviewValidateUseCase,
                                 SessionHandler sessionHandler) {
        this.sendReviewValidateUseCase = sendReviewValidateUseCase;
        this.sendReviewUseCase = sendReviewUseCase;
        this.setReviewFormCacheUseCase = setReviewFormCacheUseCase;
        this.getSendReviewFormUseCase = getSendReviewFormUseCase;
        this.skipReviewUseCase = skipReviewUseCase;
        this.editReviewUseCase = editReviewUseCase;
        this.editReviewValidateUseCase = editReviewValidateUseCase;
        this.sessionHandler = sessionHandler;
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
        sendReviewValidateUseCase.unsubscribe();
        setReviewFormCacheUseCase.unsubscribe();
        getSendReviewFormUseCase.unsubscribe();
        skipReviewUseCase.unsubscribe();
        editReviewUseCase.unsubscribe();
        editReviewValidateUseCase.unsubscribe();
    }

    public void sendReview(String reviewId, String reputationId, String productId, String shopId,
                           String review, float rating, ArrayList<ImageUpload> list,
                           List<ImageUpload> deletedList, boolean shareFb, boolean anonymous,
                           String productName, String productAvatar, String productUrl) {
        viewListener.showLoadingProgress();
        if (list.isEmpty()) {
            sendReviewWithoutImage(reviewId, reputationId, productId, shopId, review, rating,
                    shareFb, anonymous, productName, productAvatar, productUrl);
        } else {
            sendReviewWithImage(reviewId, reputationId, productId, shopId, review, rating, list,
                    deletedList, shareFb, anonymous, productName, productAvatar, productUrl);
        }

    }

    private void sendReviewWithImage(String reviewId, String reputationId, String productId,
                                     String shopId, String review, float rating,
                                     ArrayList<ImageUpload> list, List<ImageUpload> deletedList,
                                     boolean shareFb, boolean isAnonymous,
                                     String productName, String productAvatar, String productUrl) {
        sendReviewUseCase.execute(SendReviewUseCase.getParam(reviewId,
                productId,
                reputationId,
                shopId,
                String.valueOf(rating),
                review,
                list,
                deletedList,
                isAnonymous),
                new SendReviewSubscriber(viewListener, shareFb, new ShareModel(
                        productName,
                        review,
                        productUrl,
                        productAvatar
                )));
    }

    private void sendReviewWithoutImage(String reviewId, String reputationId, String productId,
                                        String shopId, String review, float rating, boolean shareFb,
                                        boolean isAnonymous, String productName, String
                                                productAvatar, String productUrl) {
        sendReviewValidateUseCase.execute(SendReviewValidateUseCase.getParam(
                reviewId,
                productId,
                reputationId,
                shopId,
                String.valueOf(rating),
                review,
                isAnonymous
        ), new SendReviewWithoutImageSubscriber(viewListener, shareFb, new ShareModel(
                productName,
                review,
                productUrl,
                productAvatar
        )));
    }

    public void openCamera() {
        cameraFileLoc = imageUploadHandler.actionCamera2();
    }

    @Override
    public void openImageGallery() {
        imageUploadHandler.actionImagePicker();
    }

    @Override
    public void setFormToCache(int position, SendReviewPass sendReviewPass) {
        setReviewFormCacheUseCase.executeSync(SetReviewFormCacheUseCase.getParam(
                sendReviewPass));
    }

    @Override
    public String getFileLocFromCamera() {
        return cameraFileLoc;
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

    @Override
    public void skipReview(String reputationId, String shopId, String productId) {
        viewListener.showLoadingProgress();
        skipReviewUseCase.execute(SkipReviewUseCase.getParam(reputationId, shopId, productId,
                sessionHandler.getLoginID()), new SkipReviewSubscriber(viewListener));
    }

    @Override
    public void editReview(String reviewId, String reputationId, String productId,
                           String shopId, String review, float rating,
                           ArrayList<ImageUpload> list, List<ImageUpload> deletedList,
                           boolean shareFb, boolean isAnonymous, String productName, String
                                   productAvatar, String productUrl) {
        viewListener.showLoadingProgress();
        if (list.isEmpty() && deletedList.isEmpty()) {
            editReviewWithoutImage(reviewId, reputationId, productId, shopId, review, rating,
                    shareFb, productName, productAvatar, productUrl, isAnonymous);
        } else if (list.isEmpty() && !deletedList.isEmpty()) {
            editReviewRemoveImage(reviewId, reputationId, productId, shopId, review, rating, list,
                    deletedList, shareFb, isAnonymous, productName, productAvatar, productUrl);
        } else {
            editReviewWithImage(reviewId, reputationId, productId, shopId, review, rating, list,
                    deletedList, shareFb, isAnonymous, productName, productAvatar, productUrl);
        }
    }

    private void editReviewRemoveImage(String reviewId, String reputationId, String productId,
                                       String shopId, String review, float rating,
                                       ArrayList<ImageUpload> list, List<ImageUpload> deletedList,
                                       boolean shareFb, boolean isAnonymous,
                                       String productName, String productAvatar, String productUrl) {
        editReviewValidateUseCase.execute(EditReviewValidateUseCase.getParamWithImage(
                reviewId, productId,
                reputationId, shopId,
                String.valueOf(rating),
                review, list,
                deletedList, isAnonymous
                ),
                new EditReviewWithoutImageSubscriber(viewListener, shareFb, new ShareModel(
                        productName,
                        review,
                        productUrl,
                        productAvatar
                )));
    }

    private void editReviewWithImage(String reviewId, String reputationId, String productId,
                                     String shopId, String review, float rating,
                                     ArrayList<ImageUpload> list,
                                     List<ImageUpload> deletedList,
                                     boolean shareFb, boolean isAnonymous,
                                     String productName, String
                                             productAvatar, String productUrl) {
        editReviewUseCase.execute(EditReviewUseCase.getParam(
                reviewId, productId, reputationId, shopId,
                String.valueOf(rating), review, list, deletedList,
                isAnonymous), new EditReviewSubscriber(viewListener, shareFb, new ShareModel(
                productName,
                review,
                productUrl,
                productAvatar
        )));

    }

    private void editReviewWithoutImage(String reviewId, String reputationId,
                                        String productId, String shopId,
                                        String review, float rating, boolean shareFb,
                                        String productName, String productAvatar, String productUrl, boolean isAnonymous) {
        editReviewValidateUseCase.execute(EditReviewValidateUseCase.getParam(
                reviewId, productId,
                reputationId, shopId,
                String.valueOf(rating), review, isAnonymous
                ),
                new EditReviewWithoutImageSubscriber(viewListener, shareFb, new ShareModel(
                        productName,
                        review,
                        productUrl,
                        productAvatar
                )));
    }

    public void setCameraFileLoc(String cameraFileLoc) {
        this.cameraFileLoc = cameraFileLoc;
    }
}
