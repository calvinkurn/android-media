package com.tokopedia.seller.shop.setting.view.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.lib.widget.TkpdHintTextInputLayout;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.view.model.ShopOpenStepperModel;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingInfoComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingInfoComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingInfoModule;
import com.tokopedia.seller.shop.setting.view.listener.ShopSettingInfoView;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingInfoPresenter;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopSettingInfoFragment extends BaseDaggerFragment implements ShopSettingInfoView {

    public static final int MAX_SELECTION_PICK_IMAGE = 1;
    public static final int REQUEST_CODE_IMAGE_PICKER = 532;

    @Inject
    public ShopSettingInfoPresenter presenter;
    private TkpdHintTextInputLayout shopDescTextInputLayout;
    private EditText shopDescEditText;
    private TkpdHintTextInputLayout shopSloganTextInputLayout;
    private EditText shopSloganEditText;
    private View containerBrowseFile;
    private View containerImagePicker;
    private ImageView imagePicker;
    private TextView errorImageEmpty;
    private TextView welcomeText;
    private Button buttonNext;
    private ProgressDialog progressDialog;
    private String uriPathImage = "";
    private ShopSettingInfoComponent component;
    protected StepperListener<ShopOpenStepperModel> onShopStepperListener;

    public static ShopSettingInfoFragment createInstance() {
        return new ShopSettingInfoFragment();
    }

    @Override
    protected void initInjector() {
        component = DaggerShopSettingInfoComponent
                .builder()
                .shopSettingInfoModule(new ShopSettingInfoModule())
                .shopOpenDomainComponent(getComponent(ShopOpenDomainComponent.class))
                .build();
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_setting_info, container, false);
        initView(view);
        presenter.attachView(this);
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
        errorImageEmpty = (TextView) view.findViewById(R.id.error_image_empty);
        welcomeText = view.findViewById(R.id.welcome_shop_label);

        String helloName = getString(R.string.hello_x, onShopStepperListener.getStepperModel().getShopName());
        welcomeText.setText(MethodChecker.fromHtml(helloName));
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));

    }

    private void setActionVar() {
        shopSloganEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isSloganFieldValid(true);
            }
        });
        shopDescEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isDescriptionFieldValid(true);
            }
        });
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
        if (!isFormValid()) {
            return;
        }
        presenter.submitShopInfo(uriPathImage, shopSloganEditText.getText().toString(),
                shopDescEditText.getText().toString());
    }

    private boolean isSloganFieldValid(boolean showError) {
        if (TextUtils.isEmpty(shopSloganEditText.getText().toString())) {
            if (showError) {
                shopSloganTextInputLayout.requestFocus();
                shopSloganTextInputLayout.setError(getString(R.string.label_shop_setting_error_slogan_should_fill));
            }
            return false;
        }
        shopSloganTextInputLayout.setError(null);
        return true;
    }

    private boolean isDescriptionFieldValid(boolean showError) {
        if (TextUtils.isEmpty(shopDescEditText.getText().toString())) {
            if (showError) {
                shopDescTextInputLayout.requestFocus();
                shopDescTextInputLayout.setError(getString(R.string.label_shop_setting_error_desc_should_fill));
            }
            return false;
        }
        shopDescTextInputLayout.setError(null);
        return true;
    }

    private boolean isShopImageValid(boolean showError) {
        if (TextUtils.isEmpty(uriPathImage)) {
            if (showError) {
                errorImageEmpty.requestFocus();
                errorImageEmpty.setVisibility(View.VISIBLE);
            }
            return false;
        }
        errorImageEmpty.setVisibility(View.GONE);
        return true;
    }

    private boolean isFormValid() {
        if (isSloganFieldValid(true) && isDescriptionFieldValid(true) && isShopImageValid(true)) {
            return true;
        }
        return false;
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

    }

    @Override
    public void onFailedSaveInfoShop(Throwable t) {

    }

    private void onClickBrowseImage() {
        startActivityForResult(GalleryActivity.createIntent(getActivity(), GalleryType.ofImageOnly()), REQUEST_CODE_IMAGE_PICKER);
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

    protected void onAttachListener(Context context){
        onShopStepperListener = (StepperListener<ShopOpenStepperModel>) context;
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
