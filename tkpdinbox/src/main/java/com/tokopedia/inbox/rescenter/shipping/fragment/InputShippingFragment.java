package com.tokopedia.inbox.rescenter.shipping.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.model.ResCenterAttachment;
import com.tokopedia.core.database.repository.ResCenterAttachmentRepository;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core2.R2;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.create.customdialog.BaseUploadImageDialog;
import com.tokopedia.inbox.rescenter.shipping.customadapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.shipping.customadapter.ShippingSpinnerAdapter;
import com.tokopedia.inbox.rescenter.shipping.customdialog.UploadImageShippingResCenterDialog;
import com.tokopedia.inbox.rescenter.shipping.di.DaggerResolutionShippingComponent;
import com.tokopedia.inbox.rescenter.shipping.di.ResolutionShippingComponent;
import com.tokopedia.inbox.rescenter.shipping.model.InputShippingParamsGetModel;
import com.tokopedia.inbox.rescenter.shipping.model.ResCenterKurir;
import com.tokopedia.inbox.rescenter.shipping.presenter.InputShippingFragmentImpl;
import com.tokopedia.inbox.rescenter.shipping.presenter.InputShippingFragmentPresenter;
import com.tokopedia.inbox.rescenter.shipping.view.InputShippingFragmentView;
import com.tokopedia.inbox.util.analytics.InboxAnalytics;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.track.TrackApp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Retrofit;

import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;

/**
 * Created by hangnadi on 12/13/16.
 */
public class InputShippingFragment extends BasePresenterFragment<InputShippingFragmentPresenter>
        implements InputShippingFragmentView, AttachmentAdapter.AttachmentAdapterListener {

    public static final String EXTRA_PARAM_ATTACHMENT = "params_attachment";
    public static final String EXTRA_PARAM_MODEL = "params_model";
    public static final String URL_IMG = "https://ecs7.tokopedia.net/img/android/others/img_awb_example.png";
    private static final int REQUEST_CODE_IMAGE_RESI = 3124;
    private PermissionCheckerHelper permissionCheckerHelper;


    @BindView(R2.id.ref_number)
    EditText shippingRefNum;
    @BindView(R2.id.spinner_kurir)
    Spinner shippingSpinner;
    @BindView(R2.id.error_spinner)
    TextView errorSpinner;
    @BindView(R2.id.list_upload_proof)
    RecyclerView listAttachment;
    @BindView(R2.id.loading)
    View loadingView;
    @BindView(R2.id.main_view)
    View mainView;
    @BindView(R2.id.confirm_button)
    TextView confirmButton;
    ImageView imgAwb;

    private AttachmentAdapter attachmentAdapter;
    private InputShippingParamsGetModel paramsModel;
    private UploadImageShippingResCenterDialog uploadImageDialog;
    private ArrayList<ResCenterAttachment> attachmentData;
    private ResolutionShippingComponent daggerShippingComponent;

    private boolean isConfirmButtonEnabled = false;

    public static Fragment newInstance(InputShippingParamsGetModel model) {
        InputShippingFragment fragment = new InputShippingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PARAM_MODEL, model);
        fragment.setArguments(bundle);
        return fragment;
    }

    @OnClick(R2.id.confirm_button)
    public void setOnConfirmButtonClick() {
        if (paramsModel.isFromChat()) {
            if (paramsModel.isEdit())
                TrackApp.getInstance().getGTM().sendGeneralEvent(
                        InboxAnalytics.eventResoChatClickSaveEditAWB(paramsModel.getResolutionID()).getEvent());
            else
                TrackApp.getInstance().getGTM().sendGeneralEvent(
                        InboxAnalytics.eventResoChatClickSaveInputAWB(paramsModel.getResolutionID()).getEvent());
        }
        presenter.onConfirrmButtonClick();
    }

    @Override
    public void dropKeyBoard() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    @Override
    public Retrofit getRetrofit() {
        return daggerShippingComponent.uploadWsV4Retrofit();
    }

    @Override
    public InputShippingParamsGetModel getParamsModel() {
        return paramsModel;
    }

    @Override
    public void setParamsModel(InputShippingParamsGetModel paramsModel) {
        this.paramsModel = paramsModel;
    }

    @Override
    public ArrayList<ResCenterAttachment> getAttachmentData() {
        return attachmentData;
    }

    @Override
    public void setAttachmentData(ArrayList<ResCenterAttachment> attachmentData) {
        this.attachmentData = attachmentData;
    }

    @Override
    public EditText getShippingRefNum() {
        return shippingRefNum;
    }

    @Override
    public Spinner getShippingSpinner() {
        return shippingSpinner;
    }

    @Override
    public RecyclerView getListAttachment() {
        return listAttachment;
    }

    @Override
    public TextView getErrorSpinner() {
        return errorSpinner;
    }

    @Override
    public View getLoadingView() {
        return loadingView;
    }

    @Override
    public View getMainView() {
        return mainView;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.onFirstTimeLaunched();
    }

    @Override
    public void onSaveState(Bundle state) {
        presenter.onSaveState(state);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        presenter.onRestoreState(savedState);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new InputShippingFragmentImpl(getActivity().getApplication(), this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        paramsModel = arguments.getParcelable(EXTRA_PARAM_MODEL);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_input_shipping_ref_num;
    }

    @Override
    protected void initView(View view) {
        imgAwb = (ImageView) view.findViewById(R.id.img_awb);
        ImageHandler.LoadImage(imgAwb, URL_IMG);
        renderAttachmentAdapter();
    }

    @Override
    public void renderAttachmentAdapter() {
        uploadImageDialog = new UploadImageShippingResCenterDialog(this, paramsModel.getResolutionID());
        attachmentData = new ArrayList<>();
        attachmentAdapter = new AttachmentAdapter(this, attachmentData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        listAttachment.setLayoutManager(layoutManager);
        listAttachment.setAdapter(attachmentAdapter);
    }

    @Override
    public void renderSpinner(List<ResCenterKurir.Kurir> shippingList) {
        ShippingSpinnerAdapter shippingAdapter = new ShippingSpinnerAdapter(
                context,
                android.R.layout.simple_spinner_item,
                shippingList
        );
        shippingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shippingSpinner.setAdapter(shippingAdapter);
    }

    @Override
    protected void setViewListener() {
        shippingRefNum.setOnTouchListener(new View.OnTouchListener() {
            @SuppressWarnings("unused")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                EditText shippingRefNum = (EditText) view;
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (shippingRefNum.getRight() - shippingRefNum.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        String[] listOfPermission = {PermissionCheckerHelper.Companion.PERMISSION_CAMERA,
                                PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE};

                        if(null == permissionCheckerHelper ){
                            permissionCheckerHelper = new PermissionCheckerHelper();
                        }

                        permissionCheckerHelper.checkPermissions(getActivity(), listOfPermission, new PermissionCheckerHelper.PermissionCheckListener() {
                            @Override
                            public void onPermissionDenied(@NotNull String permissionText) {
                                permissionCheckerHelper.onPermissionDenied(getActivity(), permissionText);
                            }

                            @Override
                            public void onNeverAskAgain(@NotNull String permissionText) {
                                permissionCheckerHelper.onNeverAskAgain(getActivity(), permissionText);
                            }

                            @Override
                            public void onPermissionGranted() {
                                presenter.onScanBarcodeClick(getActivity());
                            }
                        },listOfPermission.toString());

                        return true;
                    }
                }
                return false;
            }
        });

        shippingRefNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.onShippingRefChanged(editable);
            }
        });

        shippingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.onShippingSpinnerChanged(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        attachmentAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                presenter.onListAttachmentChanged(attachmentAdapter.getItemCount());
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                presenter.onListAttachmentChanged(attachmentAdapter.getItemCount());
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                presenter.onListAttachmentChanged(attachmentAdapter.getItemCount());
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                presenter.onListAttachmentChanged(attachmentAdapter.getItemCount());
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                presenter.onListAttachmentChanged(attachmentAdapter.getItemCount());
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                presenter.onListAttachmentChanged(attachmentAdapter.getItemCount());
            }
        });
    }

    @Override
    public void renderInputShippingRefNum(String text) {
        shippingRefNum.setText(text);
    }

    @Override
    public void setConfirmButtonEnabled() {
        if(!isConfirmButtonEnabled) {
            confirmButton.setClickable(true);
            confirmButton.setEnabled(true);
            confirmButton.setBackground(MethodChecker.getDrawable(getActivity(),R.drawable.bg_button_save_enable));
            confirmButton.setTextColor(MethodChecker.getColor(getActivity(),R.color.white));

            isConfirmButtonEnabled = true;
        }
    }

    @Override
    public void setConfirmButtonDisabled() {
        if(isConfirmButtonEnabled) {
            confirmButton.setClickable(false);
            confirmButton.setEnabled(false);
            confirmButton.setBackground(MethodChecker.getDrawable(getActivity(),R.drawable.bg_button_save_disable));
            confirmButton.setTextColor(MethodChecker.getColor(getActivity(),R.color.black_38));

            isConfirmButtonEnabled = false;
        }
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void showTimeOutMessage(NetworkErrorHelper.RetryClickedListener listener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), listener);
    }

    @Override
    public void showErrorMessage(String message) {
        if (message != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, null);
        } else {
            showTimeOutMessage(null);
        }
    }

    @Override
    public void toastTimeOutMessage() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void toastErrorMessage(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void finishAsSuccessResult() {
        getActivity().setResult(Activity.RESULT_OK, new Intent());
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_RESI && resultCode == Activity.RESULT_OK && data!= null) {
            uploadImageDialog.processImageDataFromGallery(data, new BaseUploadImageDialog.UploadImageDialogListener() {
                @Override
                public void onSuccess(List<ResCenterAttachment> data) {
                    attachmentData.clear();
                    attachmentData.addAll(data);
                    attachmentAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed() {
                    showErrorMessage(getActivity().getString(com.tokopedia.core2.R.string.error_gallery_valid));
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onClickAddAttachment(View view) {
        openImagePicker();
    }

    private void openImagePicker() {
        ImagePickerBuilder builder = new ImagePickerBuilder(getString(R.string.choose_image),
                new int[]{TYPE_GALLERY, TYPE_CAMERA}, com.tokopedia.imagepicker.picker.gallery.type.GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.ORIGINAL, true,
                null
                , null);
        Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_RESI);
    }

    @Override
    public void onClickOpenAttachment(View view, final int position) {
        ResCenterAttachmentRepository resCenterRepository = new ResCenterAttachmentRepository(getActivity().getApplication());

        uploadImageDialog.showRemoveDialog(new UploadImageShippingResCenterDialog.onRemoveAttachmentListener() {
            @Override
            public void onRemoveClickListener() {
                resCenterRepository.deleteAttachment(attachmentData.get(position));
                attachmentData.remove(position);
                attachmentAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onCancelClickListener() {

            }
        });
    }

    @Override
    public void onClickRemoveAttachment(View view, int position) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       /* InputShippingFragmentPermissionsDispatcher.onRequestPermissionsResult(
                InputShippingFragment.this, requestCode, grantResults);*/
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(getActivity(), requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        daggerShippingComponent = DaggerResolutionShippingComponent.builder()
                .appComponent(((MainApplication) (getActivity().getApplication())).getAppComponent())
                .build();
    }
}
