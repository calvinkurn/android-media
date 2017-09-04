package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.customadapter.ImageUpload;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.SendReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationForm;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber.SendReviewSubscriber;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 8/20/17.
 */

public class InboxReputationFormPresenter
        extends BaseDaggerPresenter<InboxReputationForm.View>
        implements InboxReputationForm.Presenter {

    private final SendReviewUseCase sendReviewUseCase;
    private final GlobalCacheManager globalCacheManager;
    private InboxReputationForm.View viewListener;
    private ImageUploadHandler imageUploadHandler;

    @Inject
    InboxReputationFormPresenter(SendReviewUseCase sendReviewUseCase,
                                 GlobalCacheManager globalCacheManager) {
        this.sendReviewUseCase = sendReviewUseCase;
        this.globalCacheManager = globalCacheManager;
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
    public void onImageUploadClicked(int position) {

    }
}
