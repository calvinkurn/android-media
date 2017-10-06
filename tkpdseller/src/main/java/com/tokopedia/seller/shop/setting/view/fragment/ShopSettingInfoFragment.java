package com.tokopedia.seller.shop.setting.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingInfoComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingInfoComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingInfoModule;
import com.tokopedia.seller.shop.setting.view.listener.ListenerShopSettingInfo;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingInfoPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingInfoView;

import javax.inject.Inject;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopSettingInfoFragment extends BaseDaggerFragment
        implements ShopSettingInfoView, ListenerShopSettingInfo.ListenerOnImagePickerReady {

    @Inject
    public ShopSettingInfoPresenter presenter;
    ListenerShopSettingInfo listenerShopSettingInfoActivity;
    TextInputLayout shopDescInputLayout;
    EditText shopDescInputText;
    TextInputLayout shopSloganInputLayout;
    EditText shopSloganInputText;
    View containerBrowseFile;
    View containerImagePicker;
    ImageView imagePicker;
    TextView errorImageEmpty;
    Button buttonNext;
    ProgressDialog progressDialog;
    String uriPathImage = "";
    private ShopSettingInfoComponent component;

    public static ShopSettingInfoFragment createInstance() {
        return new ShopSettingInfoFragment();
    }

    @Override
    protected void initInjector() {
        component = DaggerShopSettingInfoComponent
                .builder()
                .shopSettingInfoModule(new ShopSettingInfoModule())
                .shopSettingComponent(getComponent(ShopSettingComponent.class))
                .build();
        component.inject(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListenerShopSettingInfo) {
            listenerShopSettingInfoActivity = (ListenerShopSettingInfo) context;
        }
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

    protected void initView(View view) {
        shopDescInputLayout = (TextInputLayout) view.findViewById(R.id.shop_desc_input_layout);
        shopDescInputText = (EditText) view.findViewById(R.id.shop_desc_input_text);
        shopSloganInputLayout = (TextInputLayout) view.findViewById(R.id.shop_slogan_input_layout);
        shopSloganInputText = (EditText) view.findViewById(R.id.shop_slogan_input_text);
        containerBrowseFile = view.findViewById(R.id.container_browse_file);
        containerImagePicker = view.findViewById(R.id.image_picker_container);
        imagePicker = (ImageView) view.findViewById(R.id.image_picker);
        buttonNext = (Button) view.findViewById(R.id.button_next);
        errorImageEmpty = (TextView) view.findViewById(R.id.error_image_empty);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    protected void setActionVar() {
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
                presenter.submitShopInfo(uriPathImage, shopSloganInputText.getText().toString(),
                        shopDescInputText.getText().toString());
            }
        });
    }

    @Override
    public void onErrorEmptyImage() {
        errorImageEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onErrorEmptyImageFalse() {
        errorImageEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onErrorSloganEmpty() {
        shopSloganInputLayout.setError(getString(R.string.label_shop_setting_error_slogan_should_fill));
    }

    @Override
    public void onErrorSloganEmptyFalse() {
        shopSloganInputLayout.setError(null);
    }

    @Override
    public void onErrorDescriptionEmpty() {
        shopDescInputLayout.setError(getString(R.string.label_shop_setting_error_desc_should_fill));
    }

    @Override
    public void onErrorDescriptionEmptyFalse() {
        shopDescInputLayout.setError(null);
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
    public void onFailedSaveInfoShop() {

    }

    @Override
    public void onImageReady(String uriPathImage) {
        this.uriPathImage = uriPathImage;
        imagePicker.setImageDrawable(Drawable.createFromPath(uriPathImage));
    }

    private void onClickBrowseImage() {
        if (listenerShopSettingInfoActivity != null) {
            listenerShopSettingInfoActivity.onBrowseImageAction(ShopSettingInfoFragment.this);
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
