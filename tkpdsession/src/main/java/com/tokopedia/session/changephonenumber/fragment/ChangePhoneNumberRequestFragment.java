package com.tokopedia.session.changephonenumber.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.entity.changephonenumberrequest.CheckStatusData;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.listener.ChangePhoneNumberRequestView;
import com.tokopedia.session.changephonenumber.presenter.ChangePhoneNumberRequestPresenter;
import com.tokopedia.session.changephonenumber.presenter.ChangePhoneNumberRequestPresenterImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by nisie on 3/2/17.
 */

@RuntimePermissions
public class ChangePhoneNumberRequestFragment
        extends BasePresenterFragment<ChangePhoneNumberRequestPresenter>
        implements ChangePhoneNumberRequestView {

    private static final String PARAM_UPLOAD_TYPE = "PARAM_UPLOAD_TYPE";

    public interface ChangePhoneNumberRequestListener {
        void goToThanksPage();
    }

    private static final String UPLOAD_ID = "UPLOAD_ID";
    private static final String UPLOAD_ACCOUNT_BOOK = "UPLOAD_ACCOUNT_BOOK";
    ImageView buttonUploadId;
    ImageView buttonUploadAccountBook;
    ImageView idPhoto;
    ImageView accountBookPhoto;
    View idPhotoView;
    View accountBookPhotoView;
    ImageUploadHandler imageUploadHandler;
    Button buttonSubmit;
    View mainView;
    View contentView;

    TkpdProgressDialog progressDialog;

    String uploadType;
    ChangePhoneNumberRequestListener listener;

    public static ChangePhoneNumberRequestFragment createInstance(ChangePhoneNumberRequestListener listener) {
        ChangePhoneNumberRequestFragment fragment = new ChangePhoneNumberRequestFragment();
        fragment.setActionListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null)
        uploadType = savedInstanceState.getString(PARAM_UPLOAD_TYPE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString(PARAM_UPLOAD_TYPE, uploadType);
    }

    public void setActionListener(ChangePhoneNumberRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        presenter.checkStatus();
        contentView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSaveState(Bundle state) {
    }

    @Override
    public void onRestoreState(Bundle savedState) {
    }





    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ChangePhoneNumberRequestPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_change_phone_number_request;
    }

    @Override
    protected void initView(View view) {
        buttonUploadAccountBook = (ImageView) view.findViewById(R.id.upload_account_book_button);
        buttonUploadId = (ImageView) view.findViewById(R.id.upload_id_photo_button);
        idPhoto = (ImageView) view.findViewById(R.id.photo_id);
        accountBookPhoto = (ImageView) view.findViewById(R.id.photo_account_book);
        buttonSubmit = (Button) view.findViewById(R.id.button_submit);
        mainView = view.findViewById(R.id.main_view);
        contentView = view.findViewById(R.id.content_view);
        idPhotoView = view.findViewById(R.id.upload_id_photo_view);
        accountBookPhotoView = view.findViewById(R.id.upload_account_book_photo_view);
        imageUploadHandler = ImageUploadHandler.createInstance(this);
    }

    @Override
    protected void setViewListener() {
        buttonUploadAccountBook.setOnClickListener(onUploadAccountBook());
        accountBookPhotoView.setOnClickListener(onUploadAccountBook());
        buttonUploadId.setOnClickListener(onUploadImageId());
        idPhotoView.setOnClickListener(onUploadImageId());
        buttonSubmit.setOnClickListener(onSubmit());
    }

    private View.OnClickListener onSubmit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.submitRequest();
            }
        };
    }

    private View.OnClickListener onUploadImageId() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
                myAlertDialog.setMessage(getActivity().getString(com.tokopedia.core.R.string.dialog_upload_option));
                myAlertDialog.setPositiveButton(getActivity().getString(com.tokopedia.core.R.string.title_gallery), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ChangePhoneNumberRequestFragmentPermissionsDispatcher.uploadIdImageGalleryWithCheck(ChangePhoneNumberRequestFragment.this);
                    }
                });
                myAlertDialog.setNegativeButton(getActivity().getString(com.tokopedia.core.R.string.title_camera), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ChangePhoneNumberRequestFragmentPermissionsDispatcher.uploadIdImageCameraWithCheck(ChangePhoneNumberRequestFragment.this);

                    }


                });
                Dialog dialog = myAlertDialog.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        };
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void uploadIdImageCamera() {
        uploadType = UPLOAD_ID;
        imageUploadHandler.actionCamera();

    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void uploadIdImageGallery() {
        uploadType = UPLOAD_ID;
        imageUploadHandler.actionImagePicker();

    }

    private View.OnClickListener onUploadAccountBook() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
                myAlertDialog.setMessage(getActivity().getString(com.tokopedia.core.R.string.dialog_upload_option));
                myAlertDialog.setPositiveButton(getActivity().getString(com.tokopedia.core.R.string.title_gallery), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ChangePhoneNumberRequestFragmentPermissionsDispatcher.uploadAccountBookImageGalleryWithCheck(ChangePhoneNumberRequestFragment.this);

                    }
                });
                myAlertDialog.setNegativeButton(getActivity().getString(com.tokopedia.core.R.string.title_camera), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ChangePhoneNumberRequestFragmentPermissionsDispatcher.uploadAccountBookImageCameraWithCheck(ChangePhoneNumberRequestFragment.this);

                    }


                });
                Dialog dialog = myAlertDialog.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        };
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void uploadAccountBookImageCamera() {
        uploadType = UPLOAD_ACCOUNT_BOOK;
        imageUploadHandler.actionCamera();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void uploadAccountBookImageGallery() {
        uploadType = UPLOAD_ACCOUNT_BOOK;
        imageUploadHandler.actionImagePicker();
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (imageUploadHandler != null
                && imageUploadHandler.getCameraFileloc() != null
                && requestCode == ImageUploadHandler.REQUEST_CODE
                && (resultCode == Activity.RESULT_OK)
                && uploadType.equals(UPLOAD_ID)) {
            buttonUploadId.setVisibility(View.GONE);
            idPhotoView.setVisibility(View.VISIBLE);

            loadImageToImageView(idPhoto, imageUploadHandler.getCameraFileloc());
            presenter.setIdImage(imageUploadHandler.getCameraFileloc());
        } else if (data != null
                && requestCode == ImageUploadHandler.REQUEST_CODE
                && (resultCode == GalleryBrowser.RESULT_CODE)
                && uploadType.equals(UPLOAD_ID)) {
            buttonUploadId.setVisibility(View.GONE);
            idPhotoView.setVisibility(View.VISIBLE);
            loadImageToImageView(idPhoto, data.getStringExtra(ImageGallery.EXTRA_URL));
            presenter.setIdImage(data.getStringExtra(ImageGallery.EXTRA_URL));
        } else if (imageUploadHandler != null
                && imageUploadHandler.getCameraFileloc() != null
                && requestCode == ImageUploadHandler.REQUEST_CODE
                && (resultCode == Activity.RESULT_OK)
                && uploadType.equals(UPLOAD_ACCOUNT_BOOK)) {
            buttonUploadAccountBook.setVisibility(View.GONE);
            accountBookPhotoView.setVisibility(View.VISIBLE);
            loadImageToImageView(accountBookPhoto, imageUploadHandler.getCameraFileloc());
            presenter.setBankBookImage(imageUploadHandler.getCameraFileloc());
        } else if (data != null
                && requestCode == ImageUploadHandler.REQUEST_CODE
                && (resultCode == GalleryBrowser.RESULT_CODE)
                && uploadType.equals(UPLOAD_ACCOUNT_BOOK)) {
            buttonUploadAccountBook.setVisibility(View.GONE);
            accountBookPhotoView.setVisibility(View.VISIBLE);
            loadImageToImageView(accountBookPhoto, data.getStringExtra(ImageGallery.EXTRA_URL));
            presenter.setBankBookImage(data.getStringExtra(ImageGallery.EXTRA_URL));
        }

        setSubmitButton();

    }

    private void setSubmitButton() {
        if (presenter.isValidParam()) {
            MethodChecker.setBackground(buttonSubmit,
                    MethodChecker.getDrawable(getActivity(),
                            R.drawable.green_button_rounded
                    ));
            buttonSubmit.setTextColor(MethodChecker.getColor(getActivity(),
                    R.color.white));
        }
    }

    private void loadImageToImageView(ImageView idImage, String fileLoc) {
        if (fileLoc == null)
            ImageHandler.LoadImage(idImage, imageUploadHandler.getCameraFileloc());
        else
            ImageHandler.loadImageFromFile(getActivity(), idImage, new File(fileLoc));
    }

    @Override
    public void onGoToWaitPage() {
        listener.goToThanksPage();
    }

    @Override
    public void showLoading() {
        if (getActivity() != null && progressDialog == null) {
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
            progressDialog.showDialog();
        } else if (getActivity() != null) {
            progressDialog.showDialog();
        }
    }

    public void finishLoading() {
        if (getActivity() != null && progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onSuccessCheckStatus(CheckStatusData checkStatusData) {
        contentView.setVisibility(View.VISIBLE);
        finishLoading();
        if (checkStatusData.isPending()) {
            onGoToWaitPage();
        }
    }

    @Override
    public void onErrorcheckStatus(String errorMessage) {
        finishLoading();
        if (errorMessage.equals(""))
            NetworkErrorHelper.showEmptyState(getActivity(), mainView, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.checkStatus();
                }
            });
        else
            NetworkErrorHelper.showEmptyState(getActivity(), mainView, errorMessage, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.checkStatus();
                }
            });
    }

    @Override
    public void onErrorSubmitRequest(String errorMessage) {
        finishLoading();
        if (errorMessage.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessSubmitRequest() {
        finishLoading();
        onGoToWaitPage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ChangePhoneNumberRequestFragmentPermissionsDispatcher.onRequestPermissionsResult(ChangePhoneNumberRequestFragment.this,
                requestCode, grantResults);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
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
