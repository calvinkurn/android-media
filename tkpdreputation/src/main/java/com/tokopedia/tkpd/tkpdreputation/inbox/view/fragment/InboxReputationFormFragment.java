package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.ImageUploadPreviewActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFormActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.ImageUploadAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationForm;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter.InboxReputationFormPresenter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageAttachmentViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.sendreview.SendReviewPass;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author by nisie on 8/20/17.
 */

@RuntimePermissions
public class InboxReputationFormFragment extends BaseDaggerFragment
        implements InboxReputationForm.View, InboxReputationFormActivity.SkipListener {

    RatingBar rating;
    TextView ratingText;
    EditText review;
    TextInputLayout reviewLayout;
    ImageView uploadInfo;
    RecyclerView listImageUpload;
    Switch shareFbSwitch;
    Switch anomymousSwitch;
    Button sendButton;
    ImageUploadAdapter adapter;
    boolean isValidRating = false;
    boolean isValidReview = false;

    TkpdProgressDialog progressDialog;

    @Inject
    InboxReputationFormPresenter presenter;


    public static InboxReputationFormFragment createInstance(String reviewId, String
            reputationId, String productId, String shopId) {
        InboxReputationFormFragment fragment = new InboxReputationFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString(InboxReputationFormActivity.ARGS_SHOP_ID, shopId);
        bundle.putString(InboxReputationFormActivity.ARGS_PRODUCT_ID, productId);
        bundle.putString(InboxReputationFormActivity.ARGS_REVIEW_ID, reviewId);
        bundle.putString(InboxReputationFormActivity.ARGS_REPUTATION_ID, reputationId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment createInstanceEdit(String reviewId, String reputationId,
                                              String productId, String shopId,
                                              int rating, String review,
                                              ArrayList<ImageAttachmentViewModel> listImage) {
        InboxReputationFormFragment fragment = new InboxReputationFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString(InboxReputationFormActivity.ARGS_SHOP_ID, shopId);
        bundle.putString(InboxReputationFormActivity.ARGS_PRODUCT_ID, productId);
        bundle.putString(InboxReputationFormActivity.ARGS_REVIEW_ID, reviewId);
        bundle.putString(InboxReputationFormActivity.ARGS_REPUTATION_ID, reputationId);
        bundle.putInt(InboxReputationFormActivity.ARGS_RATING, rating);
        bundle.putString(InboxReputationFormActivity.ARGS_REVIEW, review);
        bundle.putParcelableArrayList(InboxReputationFormActivity.ARGS_REVIEW_IMAGES, listImage);
        bundle.putBoolean(InboxReputationFormActivity.ARGS_IS_EDIT, true);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_REVIEW_FORM;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerReputationComponent reputationComponent =
                (DaggerReputationComponent) DaggerReputationComponent
                        .builder()
                        .appComponent(appComponent)
                        .build();
        reputationComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View parentView = inflater.inflate(R.layout.fragment_inbox_reputation_form, container,
                false);
        rating = (RatingBar) parentView.findViewById(R.id.rating);
        ratingText = (TextView) parentView.findViewById(R.id.rating_text);
        review = (EditText) parentView.findViewById(R.id.review);
        reviewLayout = (TextInputLayout) parentView.findViewById(R.id.review_layout);
        uploadInfo = (ImageView) parentView.findViewById(R.id.upload_info);
        listImageUpload = (RecyclerView) parentView.findViewById(R.id.list_image_upload);
        shareFbSwitch = (Switch) parentView.findViewById(R.id.switch_facebook);
        anomymousSwitch = (Switch) parentView.findViewById(R.id.switch_anonym);
        sendButton = (Button) parentView.findViewById(R.id.send_button);
        prepareView();
        presenter.attachView(this);
        return parentView;
    }

    private void prepareView() {

        adapter = ImageUploadAdapter.createAdapter(getContext());
        adapter.setCanUpload(true);
        adapter.setListener(new ImageUploadAdapter.ProductImageListener() {
            @Override
            public View.OnClickListener onUploadClicked(final int position) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        review.clearFocus();
                        KeyboardHandler.DropKeyboard(getActivity(), review);
                        presenter.setFormToCache(position, new SendReviewPass(
                                getArguments().getString(InboxReputationFormActivity.ARGS_REVIEW_ID),
                                getArguments().getString(InboxReputationFormActivity.ARGS_REPUTATION_ID),
                                getArguments().getString(InboxReputationFormActivity.ARGS_PRODUCT_ID),
                                getArguments().getString(InboxReputationFormActivity.ARGS_SHOP_ID),
                                String.valueOf(rating.getRating()),
                                review.getText().toString(),
                                adapter.getList(),
                                shareFbSwitch.isChecked(),
                                anomymousSwitch.isChecked()
                        ));
                        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
                        myAlertDialog.setMessage(getActivity().getString(R.string.dialog_upload_option));
                        myAlertDialog.setPositiveButton(getActivity().getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                InboxReputationFormFragmentPermissionsDispatcher.actionImagePickerWithCheck(InboxReputationFormFragment.this);
                            }
                        });
                        myAlertDialog.setNegativeButton(getActivity().getString(R.string.title_camera), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                InboxReputationFormFragmentPermissionsDispatcher.actionCameraWithCheck(InboxReputationFormFragment.this);
                            }
                        });
                        Dialog dialog = myAlertDialog.create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                    }
                };
            }

            @Override
            public View.OnClickListener onImageClicked(final int position, final ImageUpload imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        review.clearFocus();
                        KeyboardHandler.DropKeyboard(getActivity(), review);
                        presenter.setFormToCache(position, new SendReviewPass(
                                getArguments().getString(InboxReputationFormActivity.ARGS_REVIEW_ID),
                                getArguments().getString(InboxReputationFormActivity.ARGS_REPUTATION_ID),
                                getArguments().getString(InboxReputationFormActivity.ARGS_PRODUCT_ID),
                                getArguments().getString(InboxReputationFormActivity.ARGS_SHOP_ID),
                                String.valueOf(rating.getRating()),
                                review.getText().toString(),
                                adapter.getList(),
                                shareFbSwitch.isChecked(),
                                anomymousSwitch.isChecked()
                        ));
                        startActivityForResult(
                                ImageUploadPreviewActivity.getUpdateCallingIntent(getActivity(),
                                        position),
                                ImageUploadHandler.CODE_UPLOAD_IMAGE);
                    }
                };
            }

        });
        listImageUpload.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        listImageUpload.setAdapter(adapter);

        uploadInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog uploadInfoDialog = new BottomSheetDialog(getActivity());
                uploadInfoDialog.setContentView(R.layout.upload_info_dialog);
                Button closeDialog = (Button) uploadInfoDialog.findViewById(R.id.close_button);
                closeDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadInfoDialog.dismiss();
                    }
                });
                uploadInfoDialog.show();
            }
        });

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating == 1.0f) {
                    ratingText.setText(MainApplication.getAppContext().getString(R.string
                            .rating_title_1));
                    isValidRating = true;
                } else if (rating == 2.0f) {
                    ratingText.setText(MainApplication.getAppContext().getString(R.string
                            .rating_title_2));
                    isValidRating = true;
                } else if (rating == 3.0f) {
                    ratingText.setText(MainApplication.getAppContext().getString(R.string
                            .rating_title_3));
                    isValidRating = true;
                } else if (rating == 4.0f) {
                    ratingText.setText(MainApplication.getAppContext().getString(R.string
                            .rating_title_4));
                    isValidRating = true;
                } else if (rating == 5.0f) {
                    ratingText.setText(MainApplication.getAppContext().getString(R.string
                            .rating_title_5));
                    isValidRating = true;
                } else if (rating == 0.0f) {
                    ratingText.setText(MainApplication.getAppContext().getString(R.string
                            .rating_title_0));
                    isValidRating = false;
                }

                setButtonEnabled();
            }
        });

        review.addTextChangedListener(watcher(reviewLayout));

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments() != null
                        && getArguments().getBoolean(InboxReputationFormActivity.ARGS_IS_EDIT)) {
                    presenter.editReview(
                            getArguments().getString(InboxReputationFormActivity.ARGS_REVIEW_ID),
                            getArguments().getString(InboxReputationFormActivity.ARGS_REPUTATION_ID),
                            getArguments().getString(InboxReputationFormActivity.ARGS_PRODUCT_ID),
                            getArguments().getString(InboxReputationFormActivity.ARGS_SHOP_ID),
                            review.getText().toString(),
                            rating.getRating(),
                            adapter.getList(),
                            adapter.getDeletedList(),
                            shareFbSwitch.isChecked(),
                            anomymousSwitch.isChecked());
                } else {
                    presenter.sendReview(
                            getArguments().getString(InboxReputationFormActivity.ARGS_REVIEW_ID),
                            getArguments().getString(InboxReputationFormActivity.ARGS_REPUTATION_ID),
                            getArguments().getString(InboxReputationFormActivity.ARGS_PRODUCT_ID),
                            getArguments().getString(InboxReputationFormActivity.ARGS_SHOP_ID),
                            review.getText().toString(),
                            rating.getRating(),
                            adapter.getList(),
                            adapter.getDeletedList(),
                            shareFbSwitch.isChecked(),
                            anomymousSwitch.isChecked());
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null && getArguments().getBoolean(InboxReputationFormActivity
                .ARGS_IS_EDIT)) {
            review.setText(getArguments().getString(InboxReputationFormActivity.ARGS_REVIEW));
            rating.setRating(getArguments().getInt(InboxReputationFormActivity.ARGS_RATING));
            adapter.addList(convertToImageList(getArguments().<ImageAttachmentViewModel>getParcelableArrayList
                    (InboxReputationFormActivity.ARGS_REVIEW_IMAGES)));
        }
    }

    private ArrayList<ImageUpload> convertToImageList(ArrayList<ImageAttachmentViewModel> parcelableArrayList) {
        ArrayList<ImageUpload> list = new ArrayList<>();
        for (ImageAttachmentViewModel imageAttachmentViewModel : parcelableArrayList) {
            list.add(new ImageUpload(
                    imageAttachmentViewModel.getUriThumbnail(),
                    imageAttachmentViewModel.getUriLarge(),
                    imageAttachmentViewModel.getDescription(),
                    String.valueOf(imageAttachmentViewModel.getAttachmentId())
            ));
        }
        return list;
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void actionCamera() {
        presenter.openCamera();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionImagePicker() {
        presenter.openImageGallery();
    }


    private void setButtonEnabled() {
        if (isValidRating && isValidReview) {
            sendButton.setEnabled(true);
            sendButton.setTextColor(getResources().getColor(R.color.white));
            MethodChecker.setBackground(sendButton, getResources().getDrawable(R.drawable.green_button_rounded));
        } else {
            sendButton.setEnabled(false);
            sendButton.setTextColor(getResources().getColor(R.color.grey_700));
            MethodChecker.setBackground(sendButton, getResources().getDrawable(R.drawable.bg_button_disabled));
        }
    }

    private TextWatcher watcher(final TextInputLayout wrapper) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    setWrapperError(wrapper, null);
                    isValidReview = true;
                    setButtonEnabled();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    setWrapperError(wrapper, getString(R.string.error_field_required));
                    isValidReview = false;
                    setButtonEnabled();
                }
            }
        };
    }

    private void setWrapperError(TextInputLayout wrapper, String s) {
        if (s == null) {
            wrapper.setError(s);
            wrapper.setErrorEnabled(false);
        } else {
            wrapper.setErrorEnabled(true);
            wrapper.setError(s);
        }
    }

    @Override
    public void showLoadingProgress() {
        if (progressDialog == null && getActivity() != null) {
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        }

        if (progressDialog != null)
            progressDialog.showDialog();
    }

    @Override
    public void onErrorSendReview(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessSendReview() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void finishLoadingProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void setFormFromCache(SendReviewPass sendReviewPass) {
        rating.setRating(Float.parseFloat(sendReviewPass.getRating()));
        if (!TextUtils.isEmpty(sendReviewPass.getReviewMessage()))
            review.setText(sendReviewPass.getReviewMessage());
        shareFbSwitch.setChecked(sendReviewPass.isShareFb());
        anomymousSwitch.setChecked(sendReviewPass.isAnonymous());
        adapter.addList(sendReviewPass.getListImage());
        adapter.setDeletedList(sendReviewPass.getListDeleted());
    }

    @Override
    public void onErrorSkipReview(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessSkipReview() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onErrorEditReview(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessEditReview() {

    }


    @Override
    public void skipReview() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.title_skip_review));
        builder.setMessage(getString(R.string.dialog_skip_review_confirmation));
        builder.setPositiveButton(getString(R.string.action_skip),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.skipReview(
                                getArguments().getString(InboxReputationFormActivity.ARGS_REPUTATION_ID),
                                getArguments().getString(InboxReputationFormActivity.ARGS_PRODUCT_ID),
                                getArguments().getString(InboxReputationFormActivity.ARGS_SHOP_ID));
                    }
                });
        builder.setNegativeButton(getString(R.string.title_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int param) {
                        dialog.dismiss();
                    }
                });
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageUploadHandler.REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            String fileLoc = "";
            if (presenter != null)
                fileLoc = presenter.getFileLocFromCamera();
            startActivityForResult(ImageUploadPreviewActivity.getCallingIntent(getActivity(),
                    fileLoc), ImageUploadHandler.CODE_UPLOAD_IMAGE);
        } else if (requestCode == ImageUploadHandler.REQUEST_CODE
                && resultCode == GalleryBrowser.RESULT_CODE) {
            String fileLoc = "";
            if (data != null && data.getStringExtra(ImageGallery.EXTRA_URL) != null) {
                fileLoc = data.getStringExtra(ImageGallery.EXTRA_URL);
            }
            startActivityForResult(ImageUploadPreviewActivity.getCallingIntent(getActivity(),
                    fileLoc), ImageUploadHandler.CODE_UPLOAD_IMAGE);
        } else if (requestCode == ImageUploadHandler.CODE_UPLOAD_IMAGE) {
            presenter.restoreFormFromCache();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        InboxReputationFormFragmentPermissionsDispatcher.onRequestPermissionsResult(
                InboxReputationFormFragment.this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onShowRationale(getActivity(), request, listPermission);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.CAMERA);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onPermissionDenied(getActivity(), listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onNeverAskAgain(getActivity(), listPermission);
    }
}
