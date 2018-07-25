package com.tokopedia.session.changephonenumber.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.session.changephonenumber.data.model.changephonenumberrequest.CheckStatusData;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberRequestView;
import com.tokopedia.session.changephonenumber.view.presenter.ChangePhoneNumberRequestPresenter;
import com.tokopedia.session.changephonenumber.view.presenter.ChangePhoneNumberRequestPresenterImpl;

import java.io.File;
import java.util.ArrayList;

import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;

/**
 * Created by nisie on 3/2/17.
 */

public class ChangePhoneNumberRequestFragment
        extends BasePresenterFragment<ChangePhoneNumberRequestPresenter>
        implements ChangePhoneNumberRequestView {

    public static final int REQUEST_CODE_ACCOUNT_BOOK = 281;
    public static final int REQUEST_CODE_ACCOUNT_ID = 282;
    private ImagePickerBuilder imagePickerBuilder;


    public interface ChangePhoneNumberRequestListener {
        void goToThanksPage();

        void shouldHandleBackPress(boolean isBackPressHandle);
    }

    ImageView buttonUploadId;
    ImageView buttonUploadAccountBook;
    ImageView idPhoto;
    ImageView accountBookPhoto;
    View idPhotoView;
    View accountBookPhotoView;
    View newPhoneNumberView;
    View photoIdMainView;
    View accountIdMainView;
    Button buttonSubmit;
    View mainView;
    View contentView;
    private EditText newPhoneNumber;
    private TextWatcher phoneNumberTextWatcher;
    private TextView nextButton;
    TkpdProgressDialog progressDialog;

    ChangePhoneNumberRequestListener listener;

    public static ChangePhoneNumberRequestFragment createInstance(ChangePhoneNumberRequestListener listener) {
        ChangePhoneNumberRequestFragment fragment = new ChangePhoneNumberRequestFragment();
        fragment.setActionListener(listener);
        return fragment;
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
        newPhoneNumber = view.findViewById(R.id.new_phone_number_value);
        nextButton = view.findViewById(R.id.next_button);
        newPhoneNumberView = view.findViewById(R.id.new_phone_number);
        photoIdMainView = view.findViewById(R.id.photo_id_main_layout);
        accountIdMainView = view.findViewById(R.id.account_id_main_layout);
    }

    @Override
    protected void setViewListener() {
        buttonUploadAccountBook.setOnClickListener(onUploadAccountBook());
        accountBookPhotoView.setOnClickListener(onUploadAccountBook());
        buttonUploadId.setOnClickListener(onUploadImageId());
        idPhotoView.setOnClickListener(onUploadImageId());
        buttonSubmit.setOnClickListener(onSubmit());
        phoneNumberTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.onNewNumberTextChanged(editable, newPhoneNumber.getSelectionStart());
            }
        };
        newPhoneNumber.addTextChangedListener(phoneNumberTextWatcher);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.shouldHandleBackPress(false);
                presenter.submitRequest(cleanPhoneNumber(newPhoneNumber));
            }
        });
    }

    private View.OnClickListener onSubmit() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.continueToNext();
            }
        };
    }

    private View.OnClickListener onUploadImageId() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerBuilder builder = getImagePickerBuilder();
                Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
                startActivityForResult(intent, REQUEST_CODE_ACCOUNT_ID);
            }
        };
    }

    private View.OnClickListener onUploadAccountBook() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerBuilder builder = getImagePickerBuilder();
                Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
                startActivityForResult(intent, REQUEST_CODE_ACCOUNT_BOOK);
            }
        };
    }

    private ImagePickerBuilder getImagePickerBuilder() {
        if (imagePickerBuilder == null) {
            imagePickerBuilder = new ImagePickerBuilder(getString(R.string.choose_image),
                    new int[]{TYPE_GALLERY, TYPE_CAMERA}, GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                    DEFAULT_MIN_RESOLUTION, null, true,
                    null, null);
        }
        return imagePickerBuilder;
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

        switch (requestCode) {
            case REQUEST_CODE_ACCOUNT_ID:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
                    if (imageUrlOrPathList != null && imageUrlOrPathList.size() > 0) {
                        String imagePath = imageUrlOrPathList.get(0);
                        buttonUploadId.setVisibility(View.GONE);
                        idPhotoView.setVisibility(View.VISIBLE);

                        loadImageToImageView(idPhoto, imagePath);
                        presenter.setIdImage(imagePath);
                    }
                }
                break;
            case REQUEST_CODE_ACCOUNT_BOOK:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
                    if (imageUrlOrPathList != null && imageUrlOrPathList.size() > 0) {
                        String imagePath = imageUrlOrPathList.get(0);
                        buttonUploadAccountBook.setVisibility(View.GONE);
                        accountBookPhotoView.setVisibility(View.VISIBLE);
                        loadImageToImageView(accountBookPhoto, imagePath);
                        presenter.setBankBookImage(imagePath);
                    }
                }
                break;
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

    private void loadImageToImageView(ImageView idImage, String imagePath) {
        ImageHandler.loadImageFromFile(getActivity(), idImage, new File(imagePath));
    }


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
            listener.goToThanksPage();
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
        listener.shouldHandleBackPress(true);
    }

    @Override
    public void onSuccessSubmitRequest() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        finishLoading();
        onGoToWaitPage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    private String cleanPhoneNumber(EditText newPhoneNumber) {
        String newPhoneNumberString = newPhoneNumber.getText().toString();
        return newPhoneNumberString.replace("-", "");
    }

    @Override
    public void enableNextButton() {
        nextButton.setClickable(true);
        nextButton.setEnabled(true);
        nextButton.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable
                .green_button_rounded_unify));
        nextButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
    }

    @Override
    public void disableNextButton() {
        nextButton.setClickable(false);
        nextButton.setEnabled(false);
        nextButton.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable
                .grey_button_rounded));
        nextButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_12));
    }

    @Override
    public void correctPhoneNumber(String newNumber, int selection) {
        newPhoneNumber.removeTextChangedListener(phoneNumberTextWatcher);
        newPhoneNumber.setText(newNumber);
        newPhoneNumber.setSelection(selection);
        newPhoneNumber.addTextChangedListener(phoneNumberTextWatcher);
    }

    @Override
    public void onSuccessValidRequest() {
        finishLoading();
        onGoToNextPage();
    }

    @Override
    public void onGoToNextPage() {
        photoIdMainView.setVisibility(View.GONE);
        accountIdMainView.setVisibility(View.GONE);
        buttonSubmit.setVisibility(View.GONE);
        newPhoneNumberView.setVisibility(View.VISIBLE);
        listener.shouldHandleBackPress(true);
    }

    public void handleBackOnView() {
        photoIdMainView.setVisibility(View.VISIBLE);
        accountIdMainView.setVisibility(View.VISIBLE);
        buttonSubmit.setVisibility(View.VISIBLE);
        newPhoneNumberView.setVisibility(View.GONE);
        listener.shouldHandleBackPress(false);
    }
}