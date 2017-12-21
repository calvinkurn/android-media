package com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.GetSendReviewFormUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SetReviewFormCacheUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.ImageUploadPreviewActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.ImageUploadPreviewFragmentView;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.sendreview.SendReviewPass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import rx.Subscriber;

/**
 * Created by Nisie on 2/12/16.
 */
public class ImageUploadFragmentPresenterImpl implements ImageUploadFragmentPresenter {

    private static final String TAG = ImageUploadFragmentPresenterImpl.class.getSimpleName();

    ImageUploadPreviewFragmentView viewListener;
    ImageUploadHandler imageUploadHandler;
    GetSendReviewFormUseCase getSendReviewFormUseCase;
    SetReviewFormCacheUseCase setReviewFormCacheUseCase;
    List<ImageUpload> deletedImageUploads;
    String cameraFileLoc;

    public ImageUploadFragmentPresenterImpl(ImageUploadPreviewFragmentView viewListener) {
        this.viewListener = viewListener;
        this.imageUploadHandler = ImageUploadHandler.createInstance(viewListener.getActivity());
        this.deletedImageUploads = new ArrayList<>();
        GlobalCacheManager globalCacheManager = new GlobalCacheManager();
        this.getSendReviewFormUseCase = new GetSendReviewFormUseCase(new JobExecutor(),
                new UIThread(), globalCacheManager);
        this.setReviewFormCacheUseCase = new SetReviewFormCacheUseCase(new JobExecutor(),
                new UIThread(), globalCacheManager);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == ImageUploadHandler.REQUEST_CODE)
                && (resultCode == Activity.RESULT_OK || resultCode == GalleryBrowser.RESULT_CODE)) {

            int position = viewListener.getAdapter().getList().size();
            ImageUpload image = new ImageUpload();
            image.setPosition(position);
            image.setImageId(SendReviewUseCase.IMAGE + UUID.randomUUID().toString());

            switch (resultCode) {
                case GalleryBrowser.RESULT_CODE:
                    image.setFileLoc(data.getStringExtra(ImageGallery.EXTRA_URL));
                    break;
                case Activity.RESULT_OK:
                    image.setFileLoc(cameraFileLoc);
                    break;
                default:
                    break;
            }
            viewListener.getAdapter().addImage(image);
            viewListener.setPreviewImage(image);

        }
    }

    @Override
    public void setImages(final Bundle arguments) {
        if (arguments.getBoolean(ImageUploadPreviewActivity.IS_UPDATE, false)) {

            getSendReviewFormUseCase.execute(RequestParams.EMPTY, new Subscriber<SendReviewPass>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, e.getMessage());
                }

                @Override
                public void onNext(SendReviewPass sendReviewPass) {

                    for (int i = 0; i < sendReviewPass.getListImage().size(); i++) {
                        sendReviewPass.getListImage().get(i).setPosition(i);
                    }
                    viewListener.getAdapter().addList(sendReviewPass.getListImage());
                    ImageUpload image = sendReviewPass.getListImage().get(arguments.getInt
                            (ImageUploadPreviewActivity.ARGS_POSITION, 0));
                    viewListener.setCurrentPosition(arguments.getInt(
                            ImageUploadPreviewActivity.ARGS_POSITION, 0));
                    viewListener.setDescription(image.getDescription());
                    viewListener.setPreviewImage(image);

                }
            });

        } else {
            File imgFile = new File(arguments.getString(ImageUploadHandler.FILELOC, ""));

            if (imgFile.exists()) {
                final ImageUpload image = new ImageUpload();
                image.setFileLoc(arguments.getString(ImageUploadHandler.FILELOC, ""));
                image.setImageId(SendReviewUseCase.IMAGE + UUID.randomUUID().toString());

                getSendReviewFormUseCase.execute(RequestParams.EMPTY, new Subscriber<SendReviewPass>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(SendReviewPass sendReviewPass) {

                        if (sendReviewPass.getListImage().isEmpty()) {
                            viewListener.getAdapter().addImage(image);
                            viewListener.setPreviewImage(image);
                        } else {
                            for (int i = 0; i < sendReviewPass.getListImage().size(); i++) {
                                sendReviewPass.getListImage().get(i).setPosition(i);
                            }
                            viewListener.getAdapter().addList(sendReviewPass.getListImage());
                            image.setPosition(viewListener.getAdapter().getList().size());
                            viewListener.getAdapter().addImage(image);
                            viewListener.setPreviewImage(image);
                        }

                    }
                });

            }
        }
    }

    @Override
    public void onDeleteImage(final int currentPosition) {

        getSendReviewFormUseCase.execute(RequestParams.EMPTY, new Subscriber<SendReviewPass>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (hasOnlyOneImage()) {
                    viewListener.getActivity().finish();
                }
            }

            @Override
            public void onNext(SendReviewPass sendReviewPass) {
                if (!viewListener.getAdapter().getList().get(currentPosition).getImageId()
                        .startsWith(SendReviewUseCase.IMAGE)) {
                    deletedImageUploads.add(viewListener.getAdapter().getList().get(currentPosition));
                }

                if (hasOnlyOneImage()) {
                    sendReviewPass.getListImage().clear();
                    sendReviewPass.getListDeleted().addAll(deletedImageUploads);
                    setReviewFormCacheUseCase.executeSync(SetReviewFormCacheUseCase.getParam
                            (sendReviewPass));
                    viewListener.getActivity().setResult(Activity.RESULT_OK);
                    viewListener.getActivity().finish();
                } else if (isLastItem(currentPosition)) {
                    viewListener.getAdapter().removeImage(currentPosition);
                    viewListener.getPagerAdapter().resetAdapter();
                    viewListener.getPagerAdapter().notifyDataSetChanged();
                    viewListener.setCurrentPosition(currentPosition - 1);
                    viewListener.setDescription(viewListener.getAdapter().getList().get(currentPosition - 1).getDescription());
                    viewListener.setPreviewImage(viewListener.getAdapter().getList().get(currentPosition - 1));
                } else {
                    viewListener.getAdapter().removeImage(currentPosition);
                    viewListener.getPagerAdapter().resetAdapter();
                    viewListener.getPagerAdapter().notifyDataSetChanged();
                    viewListener.setPreviewImage(viewListener.getAdapter().getList().get(currentPosition));
                }
            }
        });

    }

    @Override
    public void onSubmitImageUpload(final ArrayList<ImageUpload> list) {

        getSendReviewFormUseCase.execute(RequestParams.EMPTY, new Subscriber<SendReviewPass>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onNext(SendReviewPass sendReviewPass) {
                Log.i(TAG, "Get The Cache!! " + sendReviewPass.toString());

                for (ImageUpload imageUpload : list) {
                    imageUpload.setIsSelected(false);
                }
                sendReviewPass.setListImage(list);
                sendReviewPass.getListDeleted().addAll(deletedImageUploads);
                setReviewFormCacheUseCase.executeSync(SetReviewFormCacheUseCase.getParam
                        (sendReviewPass));

            }
        });

        viewListener.getActivity().setResult(Activity.RESULT_OK);
        viewListener.getActivity().finish();


    }

    @Override
    public void openImageGallery() {
        imageUploadHandler.actionImagePicker();
    }

    @Override
    public void openCamera() {
        cameraFileLoc = imageUploadHandler.actionCamera2();
    }

    @Override
    public String getCameraFileLoc() {
        return cameraFileLoc;
    }

    @Override
    public void setCameraFileLoc(String cameraFileLoc) {
        this.cameraFileLoc = cameraFileLoc;
    }

    private boolean isLastItem(int currentPosition) {
        return currentPosition == viewListener.getAdapter().getList().size() - 1;
    }

    private boolean hasOnlyOneImage() {
        return viewListener.getAdapter().getList().size() == 1;
    }

}
