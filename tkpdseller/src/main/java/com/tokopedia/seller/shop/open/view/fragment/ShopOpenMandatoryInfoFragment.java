package com.tokopedia.seller.shop.open.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.gallery.GalleryType;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.common.gallery.GalleryCropActivity;
import com.tokopedia.seller.lib.widget.TkpdHintTextInputLayout;
import com.tokopedia.seller.product.edit.view.dialog.ImageEditDialogFragment;
import com.tokopedia.seller.shop.open.analytic.ShopOpenTracking;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.UserData;
import com.tokopedia.seller.shop.open.di.component.DaggerShopSettingInfoComponent;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.component.ShopSettingInfoComponent;
import com.tokopedia.seller.shop.open.domain.model.ShopOpenSaveInfoResponseModel;
import com.tokopedia.seller.shop.open.util.ShopErrorHandler;
import com.tokopedia.seller.shop.open.view.listener.ShopOpenInfoView;
import com.tokopedia.seller.shop.open.view.model.ShopOpenStepperModel;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenInfoPresenter;

import java.io.File;
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
 * Created by Nathaniel on 3/16/2017.
 */

@RuntimePermissions
public class ShopOpenMandatoryInfoFragment extends BaseDaggerFragment implements ShopOpenInfoView {

    public static final int REQUEST_CODE_IMAGE_PICKER = 532;

    @Inject
    public ShopOpenInfoPresenter presenter;
    private TkpdHintTextInputLayout shopDescTextInputLayout;
    private EditText shopDescEditText;
    private TkpdHintTextInputLayout shopSloganTextInputLayout;
    private EditText shopSloganEditText;
    private View containerImagePicker;
    private ImageView imagePicker;
    private TextView welcomeText;
    private Button buttonNext;
    private TkpdProgressDialog tkpdProgressDialog;
    private String uriPathImage = "";
    private StepperListener<ShopOpenStepperModel> onShopStepperListener;

    @Inject
    ShopOpenTracking trackingOpenShop;

    public static ShopOpenMandatoryInfoFragment createInstance() {
        return new ShopOpenMandatoryInfoFragment();
    }

    @Override
    protected void initInjector() {
        ShopSettingInfoComponent component = DaggerShopSettingInfoComponent
                .builder()
                .shopOpenDomainComponent(getComponent(ShopOpenDomainComponent.class))
                .build();
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_setting_info, container, false);
        presenter.attachView(this);
        initView(view);
        setActionVar();
        return view;
    }

    private void initView(View view) {
        shopDescTextInputLayout = view.findViewById(R.id.shop_desc_input_layout);
        shopDescEditText = (EditText) view.findViewById(R.id.shop_desc_input_text);
        shopSloganTextInputLayout = view.findViewById(R.id.shop_slogan_input_layout);
        shopSloganEditText = (EditText) view.findViewById(R.id.shop_slogan_input_text);
        containerImagePicker = view.findViewById(R.id.image_picker_container);
        imagePicker = (ImageView) view.findViewById(R.id.image_picker);
        buttonNext = (Button) view.findViewById(R.id.button_next);
        welcomeText = view.findViewById(R.id.welcome_shop_label);

        if (onShopStepperListener != null) {
            if (onShopStepperListener.getStepperModel().getResponseIsReserveDomain() == null) {
                presenter.getisReserveDomain();
            } else {
                if (onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getUserData() != null) {
                    UserData userData = onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getUserData();
                    updateView(userData);
                }
            }
        }

    }

    private void updateView(UserData userData) {
        if (userData.getShopName() != null) {
            String helloName = getString(R.string.hello_x, userData.getShopName());
            welcomeText.setText(MethodChecker.fromHtml(helloName));
        }
        shopDescEditText.setText(userData.getShortDesc());
        shopSloganEditText.setText(userData.getTagLine());
        Glide.with(imagePicker.getContext())
                .load(userData.getLogo())
                .dontAnimate()
                .placeholder(R.drawable.ic_add_photo_box)
                .error(R.drawable.ic_add_photo_box)
                .centerCrop()
                .into(imagePicker);
    }

    private void setActionVar() {
        containerImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBrowseImage();
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextButtonClicked();
            }
        });
    }

    protected void onNextButtonClicked() {
        if (TextUtils.isEmpty(uriPathImage) && onShopStepperListener.getStepperModel().getResponseIsReserveDomain() != null
                && onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getUserData() != null) {
            UserData userData = onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getUserData();
            presenter.submitShopInfo(uriPathImage, shopSloganEditText.getText().toString(),
                    shopDescEditText.getText().toString(), userData.getLogo(),
                    userData.getServerId(), userData.getPhotoObj());
        } else {
            presenter.submitShopInfo(uriPathImage, shopSloganEditText.getText().toString(),
                    shopDescEditText.getText().toString(), "", "", "");
        }
    }

    @Override
    public void dismissProgressDialog() {
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }
    }

    @Override
    public void showProgressDialog() {
        if (tkpdProgressDialog == null) {
            tkpdProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS,
                    getString(R.string.title_loading));
        }
        tkpdProgressDialog.showDialog();
    }

    @Override
    public void onSuccessSaveInfoShop(ShopOpenSaveInfoResponseModel responseModel) {
        if(onShopStepperListener != null && onShopStepperListener.getStepperModel().getResponseIsReserveDomain() != null){
            UserData userData = onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getUserData();
            userData.setShortDesc(responseModel.getShopDesc());
            userData.setLogo(responseModel.getPicSrc());
            userData.setTagLine(responseModel.getShopTagLine());
        }
        trackingOpenShop.eventOpenShopFormSuccess();
        if (onShopStepperListener != null) {
            onShopStepperListener.goToNextPage(null);
        }
    }

    @Override
    public void onFailedSaveInfoShop(Throwable t) {
        Crashlytics.logException(t);
        String errorMessage = ShopErrorHandler.getErrorMessage(getActivity(), t);
        trackingOpenShop.eventOpenShopFormError(errorMessage);
        onErrorGetReserveDomain(errorMessage);
    }

    private void onErrorGetReserveDomain(String errorMessage){
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessGetReserveDomain(ResponseIsReserveDomain responseIsReserveDomain) {
        if (onShopStepperListener != null) {
            onShopStepperListener.getStepperModel().setResponseIsReserveDomain(responseIsReserveDomain);
        }
        updateView(responseIsReserveDomain.getUserData());
    }

    @Override
    public void onErrorGetReserveDomain(Throwable e) {
        NetworkErrorHelper.showSnackbar(getActivity(), ShopErrorHandler.getErrorMessage(getActivity(), e));
    }

    private void onClickBrowseImage() {
        CommonUtils.hideKeyboard(getActivity(), getView());
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ShopOpenMandatoryImageDialogFragment dialogFragment = ShopOpenMandatoryImageDialogFragment.newInstance(0);
        dialogFragment.show(fm, ImageEditDialogFragment.FRAGMENT_TAG);
        dialogFragment.setOnImageEditListener(new ImageEditDialogFragment.OnImageEditListener() {

            @Override
            public void clickEditProductFromCamera(int position) {
                ShopOpenMandatoryInfoFragmentPermissionsDispatcher.goToCameraWithCheck(ShopOpenMandatoryInfoFragment.this);
            }

            @Override
            public void clickEditProductFromGallery(int position) {
                ShopOpenMandatoryInfoFragmentPermissionsDispatcher.goToGalleryWithCheck(ShopOpenMandatoryInfoFragment.this);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case REQUEST_CODE_IMAGE_PICKER:
                    if (resultCode == Activity.RESULT_OK) {
                        if (data != null && data.getStringExtra(GalleryCropActivity.RESULT_IMAGE_CROPPED) != null) {
                            uriPathImage = data.getStringExtra(GalleryCropActivity.RESULT_IMAGE_CROPPED);
                            ImageHandler.loadImageFromFile(getActivity(), imagePicker, new File(uriPathImage));
                        }
                    }
                    break;
                case com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY:
                    if(data != null) {
                        String imageUrl = data.getStringExtra(GalleryActivity.IMAGE_URL);
                        if (!TextUtils.isEmpty(imageUrl)) {
                            uriPathImage = imageUrl;
                            ImageHandler.loadImageFromFile(getActivity(), imagePicker, new File(uriPathImage));
                        }
                    }
                    break;
                default:
                    break;
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachListener(activity);
        }
    }

    @TargetApi(23)
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        onAttachListener(context);
    }

    // Permission part
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ShopOpenMandatoryInfoFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @TargetApi(16)
    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForExternalStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @TargetApi(16)
    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.CAMERA);
    }

    @TargetApi(16)
    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForExternalStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @TargetApi(16)
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.CAMERA);
    }

    @TargetApi(16)
    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationale(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        RequestPermissionUtil.onShowRationale(getActivity(), request, listPermission);
    }

    @TargetApi(16)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void goToGallery() {
        startActivityForResult(GalleryCropActivity.createIntent(getActivity(), GalleryType.ofImageOnly(), true), REQUEST_CODE_IMAGE_PICKER);
    }

    @TargetApi(16)
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void goToCamera() {
        startActivityForResult(com.tokopedia.seller.common.imageeditor.GalleryCropActivity.createIntent(getActivity(), 1, true, 1,true),
                com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY);
    }

    protected void onAttachListener(Context context) {
        onShopStepperListener = (StepperListener<ShopOpenStepperModel>) context;
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
