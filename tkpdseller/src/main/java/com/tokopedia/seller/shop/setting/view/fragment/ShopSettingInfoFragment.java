package com.tokopedia.seller.shop.setting.view.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.tokopedia.seller.R;
import com.tokopedia.seller.app.BaseDiFragment;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopSettingInfoComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingInfoComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopSettingInfoModule;
import com.tokopedia.seller.shop.setting.view.listener.ListenerShopSettingInfo;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingInfoPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingInfoView;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopSettingInfoFragment extends BaseDiFragment<ShopSettingInfoComponent, ShopSettingInfoPresenter>
        implements ShopSettingInfoView, Step, ListenerShopSettingInfo.ListenerOnImagePickerReady {

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

    String uriPathImage;

    public static ShopSettingInfoFragment createInstance() {
        return new ShopSettingInfoFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ListenerShopSettingInfo) {
            listenerShopSettingInfoActivity = (ListenerShopSettingInfo) activity;
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shop_setting_info;
    }

    @Override
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
    }

    @Override
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
    protected ShopSettingInfoComponent initInjection() {
        return DaggerShopSettingInfoComponent
                .builder()
                .shopSettingInfoModule(new ShopSettingInfoModule(this))
                .shopSettingComponent(getComponent(ShopSettingComponent.class))
                .build();
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
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

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

}
