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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.gallery.GalleryActivity;
import com.tokopedia.core.gallery.GallerySelectedFragment;
import com.tokopedia.core.gallery.GalleryType;
import com.tokopedia.core.gallery.MediaItem;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.lib.widget.TkpdHintTextInputLayout;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.view.model.ShopOpenStepperModel;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.UserData;
import com.tokopedia.seller.shop.open.data.source.cloud.ShopSettingException;
import com.tokopedia.seller.shop.open.di.component.DaggerShopSettingInfoComponent;
import com.tokopedia.seller.shop.open.di.component.ShopSettingInfoComponent;
import com.tokopedia.seller.shop.open.view.listener.ShopOpenInfoView;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenInfoPresenter;
import com.tokopedia.seller.shop.open.view.fragment.ShopOpenInfoFragmentPermissionsDispatcher;

import java.io.File;

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
public class ShopOpenInfoFragment extends BaseDaggerFragment implements ShopOpenInfoView {

    public static final int REQUEST_CODE_IMAGE_PICKER = 532;

    @Inject
    public ShopOpenInfoPresenter presenter;
    private TkpdHintTextInputLayout shopDescTextInputLayout;
    private EditText shopDescEditText;
    private TkpdHintTextInputLayout shopSloganTextInputLayout;
    private EditText shopSloganEditText;
    private View containerBrowseFile;
    private View containerImagePicker;
    private ImageView imagePicker;
    private TextView welcomeText;
    private Button buttonNext;
    private ProgressDialog progressDialog;
    private String uriPathImage = "";
    private StepperListener<ShopOpenStepperModel> onShopStepperListener;

    public static ShopOpenInfoFragment createInstance() {
        return new ShopOpenInfoFragment();
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
        shopDescTextInputLayout =  view.findViewById(R.id.shop_desc_input_layout);
        shopDescEditText = (EditText) view.findViewById(R.id.shop_desc_input_text);
        shopSloganTextInputLayout = view.findViewById(R.id.shop_slogan_input_layout);
        shopSloganEditText = (EditText) view.findViewById(R.id.shop_slogan_input_text);
        containerBrowseFile = view.findViewById(R.id.container_browse_file);
        containerImagePicker = view.findViewById(R.id.image_picker_container);
        imagePicker = (ImageView) view.findViewById(R.id.image_picker);
        buttonNext = (Button) view.findViewById(R.id.button_next);
        welcomeText = view.findViewById(R.id.welcome_shop_label);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));

        if(onShopStepperListener != null ){
            if(onShopStepperListener.getStepperModel().getResponseIsReserveDomain() == null){
                presenter.getisReserveDomain();
            }else{
                if(onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getData().getUserData() != null) {
                    UserData userData = onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getData().getUserData();
                    if(userData.getShopName()!= null) {
                        String helloName = getString(R.string.hello_x, userData.getShopName());
                        welcomeText.setText(MethodChecker.fromHtml(helloName));
                    }
                    shopDescEditText.setText(onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getData().getUserData().getShortDesc());
                    shopSloganEditText.setText(onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getData().getUserData().getTagLine());
                    ImageHandler.loadImage(getActivity(), imagePicker,
                            onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getData().getUserData().getLogo(), R.drawable.ic_add_photo_box);
                }
            }
        }
    }

    private void setActionVar() {
        containerBrowseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBrowseImage();
            }
        });
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
        if(TextUtils.isEmpty(uriPathImage) && onShopStepperListener.getStepperModel().getResponseIsReserveDomain()!= null
        && onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getData().getUserData() != null) {
            UserData userData = onShopStepperListener.getStepperModel().getResponseIsReserveDomain().getData().getUserData();
            presenter.submitShopInfo(userData.getLogo(), shopSloganEditText.getText().toString(),
                    shopDescEditText.getText().toString(), userData.getLogo(),
                    userData.getServerId(), userData.getPhotoObj());
        }else{
            presenter.submitShopInfo(uriPathImage, shopSloganEditText.getText().toString(),
                    shopDescEditText.getText().toString(), "", "", "");
        }
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onSuccessSaveInfoShop() {
        if(onShopStepperListener != null) {
            onShopStepperListener.goToNextPage(null);
        }
    }

    @Override
    public void onFailedSaveInfoShop(Throwable t) {
        String errorMessage;
        if(t instanceof ShopSettingException){
            errorMessage = t.getMessage();
        }else{
            errorMessage = ErrorHandler.getErrorMessage(t, getActivity());
        }
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                onNextButtonClicked();
            }
        });
    }

    @Override
    public void onSuccessGetReserveDomain(ResponseIsReserveDomain responseIsReserveDomain) {
        if(onShopStepperListener != null) {
            onShopStepperListener.getStepperModel().setResponseIsReserveDomain(responseIsReserveDomain);
        }
    }

    @Override
    public void onErrorGetReserveDomain(Throwable e) {
        NetworkErrorHelper.showSnackbar(getActivity(), ErrorHandler.getErrorMessage(e, getActivity()));
    }

    private void onClickBrowseImage() {
        ShopOpenInfoFragmentPermissionsDispatcher.goToGalleryWithCheck(ShopOpenInfoFragment.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_IMAGE_PICKER:
                    if (data != null && data.getParcelableExtra(GallerySelectedFragment.EXTRA_RESULT_SELECTION) != null) {
                        MediaItem item = data.getParcelableExtra(GallerySelectedFragment.EXTRA_RESULT_SELECTION);
                        uriPathImage = item.getRealPath();
                        ImageHandler.loadImageFromFile(getActivity(), imagePicker, new File(item.getRealPath()));
                    }
                    break;
                default:
                    break;
            }
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
        ShopOpenInfoFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @TargetApi(16)
    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForExternalStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @TargetApi(16)
    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForExternalStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @TargetApi(16)
    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForExternalStorage(final PermissionRequest request) {
        request.proceed();
    }

    @TargetApi(16)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void goToGallery() {
        startActivityForResult(GalleryActivity.createIntent(getActivity(), GalleryType.ofImageOnly()), REQUEST_CODE_IMAGE_PICKER);
    }

    protected void onAttachListener(Context context){
        onShopStepperListener = (StepperListener<ShopOpenStepperModel>) context;
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
