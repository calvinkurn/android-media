package com.tokopedia.inbox.contactus.fragment;

/**
 * Created by nisie on 8/12/16.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.inbox.contactus.ContactUsConstant;
import com.tokopedia.inbox.contactus.adapter.ImageUploadAdapter;
import com.tokopedia.inbox.contactus.listener.CreateTicketFormFragmentView;
import com.tokopedia.inbox.contactus.model.solution.SolutionResult;
import com.tokopedia.inbox.contactus.presenter.CreateTicketFormFragmentPresenter;
import com.tokopedia.inbox.contactus.presenter.CreateTicketFormFragmentPresenterImpl;
import com.tokopedia.core.inboxreputation.model.ImageUpload;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.RequestPermissionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Tkpd_Eka on 8/13/2015.
 */
@RuntimePermissions
public class CreateTicketFormFragment extends BasePresenterFragment<CreateTicketFormFragmentPresenter>
        implements CreateTicketFormFragmentView, ContactUsConstant {

    public interface FinishContactUsListener {
        void onFinishCreateTicket();
    }

    @BindView(R2.id.main_category)
    EditText mainCategory;

    @BindView(R2.id.detail)
    EditText detail;

    @BindView(R2.id.attachment_text)
    View attachmentText;

    @BindView(R2.id.main)
    View mainView;

    @BindView(R2.id.attachment)
    RecyclerView attachment;

    @BindView(R2.id.email)
    EditText email;

    @BindView(R2.id.phone_number)
    EditText phoneNumber;

    ImageUploadAdapter imageAdapter;
    TkpdProgressDialog progressDialog;
    ImageUploadHandler imageUploadHandler;


    public static CreateTicketFormFragment createInstance(Bundle extras) {
        CreateTicketFormFragment fragment = new CreateTicketFormFragment();
        Bundle bundle = new Bundle();
        bundle.putAll(extras);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.initForm();
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.talk_add_new, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_send)
            KeyboardHandler.DropKeyboard(getActivity(), getView());
        presenter.sendTicket();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initialPresenter() {
        presenter = new CreateTicketFormFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_create_ticket;
    }

    @Override
    protected void initView(View view) {
        imageAdapter = ImageUploadAdapter.createAdapter(getActivity());
        imageAdapter.setCanUpload(true);

        attachment.setLayoutManager(new LinearLayoutManager(getActivity(),
                android.support.v7.widget.LinearLayoutManager.HORIZONTAL, false));
        attachment.setAdapter(imageAdapter);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

    }

    @Override
    protected void setViewListener() {
        imageAdapter.setListener(new ImageUploadAdapter.ProductImageListener() {
            @Override
            public View.OnClickListener onUploadClicked(final int position) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showImagePickerDialog();

                    }
                };
            }

            @Override
            public View.OnClickListener onImageClicked(final int position, ImageUpload imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                };
            }

        });

    }

    private void showImagePickerDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
        myAlertDialog.setMessage(context.getString(R.string.dialog_upload_option));
        myAlertDialog.setPositiveButton(context.getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CreateTicketFormFragmentPermissionsDispatcher.actionImagePickerWithCheck(
                        CreateTicketFormFragment.this);
            }
        });
        myAlertDialog.setNegativeButton(context.getString(R.string.title_camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CreateTicketFormFragmentPermissionsDispatcher.actionCameraWithCheck(
                        CreateTicketFormFragment.this);
            }


        });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void actionCamera() {
        imageUploadHandler.actionCamera();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionImagePicker() {
        imageUploadHandler.actionImagePicker();
    }

    @Override
    protected void initialVar() {
        imageUploadHandler = ImageUploadHandler.createInstance(this);
    }

    @Override
    protected void setActionVar() {

    }


    @Override
    public String getTicketCategoryId() {
        return String.valueOf(getArguments().getInt(PARAM_LAST_CATEGORY_ID));
    }

    @Override
    public void showLoading() {
        progressDialog.showDialog();
    }

    @Override
    public void finishLoading() {
        if (progressDialog != null)
            progressDialog.dismiss();

    }

    @Override
    public void setResult(SolutionResult solutionResult) {
        mainView.setVisibility(View.VISIBLE);
        mainCategory.setText(solutionResult.getSolutions().getName());
        finishLoading();
    }

    @Override
    public EditText getMessage() {
        return detail;
    }

    @Override
    public void showError(String error) {
        if (error.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), error);


    }

    @Override
    public ArrayList<ImageUpload> getAttachment() {
        return imageAdapter.getList();
    }

    @Override
    public void removeErrorEmptyState() {
        NetworkErrorHelper.hideEmptyState(getView());
    }

    @Override
    public void showErrorEmptyState(String error, NetworkErrorHelper.RetryClickedListener retryClickedListener) {
        if (error.equals(""))
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), retryClickedListener);
        else
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, retryClickedListener);


    }

    @Override
    public EditText getEmail() {
        return email;
    }

    @Override
    public void showErrorValidation(EditText view, String error) {
        view.setError(error);
        view.requestFocus();
    }

    @Override
    public EditText getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == ImageUploadHandler.REQUEST_CODE)
                && (resultCode == Activity.RESULT_OK || resultCode == GalleryBrowser.RESULT_CODE)) {

            int position = imageAdapter.getList().size();
            ImageUpload image = new ImageUpload();
            image.setPosition(position);
            image.setImageId("image" + UUID.randomUUID().toString());

            switch (resultCode) {
                case GalleryBrowser.RESULT_CODE:
                    image.setFileLoc(data.getStringExtra(ImageGallery.EXTRA_URL));
                    break;
                case Activity.RESULT_OK:
                    image.setFileLoc(imageUploadHandler.getCameraFileloc());
                    break;
                default:
                    break;
            }
            imageAdapter.addImage(image);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CreateTicketFormFragmentPermissionsDispatcher.onRequestPermissionsResult(
                CreateTicketFormFragment.this, requestCode, grantResults);
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