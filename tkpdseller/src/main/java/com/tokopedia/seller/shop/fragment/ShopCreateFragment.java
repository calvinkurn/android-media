package com.tokopedia.seller.shop.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.gallery.ImageGalleryEntry;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.shipping.model.openshopshipping.OpenShopData;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.PhoneVerificationUtil;
import com.tokopedia.seller.shop.ShopEditorActivity;
import com.tokopedia.seller.shop.presenter.ShopCreatePresenter;
import com.tokopedia.seller.shop.presenter.ShopCreatePresenterImpl;
import com.tokopedia.seller.shop.presenter.ShopCreateView;

import java.io.File;

import static com.tokopedia.seller.shop.presenter.ShopCreatePresenter.DESC_ERROR;
import static com.tokopedia.seller.shop.presenter.ShopCreatePresenter.DOMAIN_ERROR;
import static com.tokopedia.seller.shop.presenter.ShopCreatePresenter.NAME_ERROR;
import static com.tokopedia.seller.shop.presenter.ShopCreatePresenter.TAG_ERROR;

/**
 * Created by Toped18 on 5/19/2016.
 */
public class ShopCreateFragment extends BaseFragment<ShopCreatePresenter> implements ShopCreateView {

    public static final int REQUEST_CAMERA = 111;
    // SUBMIT BUTTON
    TextView submitButton;
    // VERIFY PHONE
    TextView verifyButton;
    TextView verifyInstruction;
    // SHOP AVATAR
    ImageView shopAvatar;
    TextView imageText;
    // DOMAIN CHECKER
    EditText shopDomain;
    TextInputLayout domainInput;
    // NAME CHECKER
    EditText shopName;
    TextInputLayout nameInput;
    // TAG CHECKER
    TextInputLayout tagInput;
    EditText shopTag;
    // DESC CHECKER
    TextInputLayout descInput;
    EditText shopDesc;
    private TkpdProgressDialog progressDialog;

    public static Fragment newInstance() {
        ShopCreateFragment fragment = new ShopCreateFragment();
        return fragment;
    }

    public void showVerificationDialog(){
        ((BaseActivity)getActivity()).getPhoneVerificationUtil().showVerificationDialog();
    }

    public void startUploadDialog(){
        ImageGalleryEntry.moveToImageGallery((AppCompatActivity)getActivity(), 0, 1);
    }

    public void SubmitDialog(){
        showProgress(true);
        presenter.saveDescTag();
        presenter.finalCheckDomainName(
                shopDomain.getText().toString(),
                shopName.getText().toString()
        );
        UnifyTracking.eventCreateShop();
    }

    void domainChanged() {
        if(shopDomain.getText().toString().length() != 0) {
            domainInput.setHint(getString(R.string.title_hint_domain) + " : "
                    + getString(R.string.domain) + shopDomain.getText().toString());
            presenter.checkShopDomain(shopDomain.getText().toString());
        } else {
            domainInput.setHint(getString(R.string.title_hint_domain));
        }
    }

    void nameChanged() {
        if (shopName.getText().toString().length() != 0) {
            presenter.checkShopName(shopName.getText().toString());
        }
    }

    void tagChanged() {
        checkTagError();
    }

    void descChanged() {
        checkDescError();
    }

    @Override
    public void setShopAvatar(String imagePath) {
        if (imagePath != "") {
            imageText.setVisibility(View.GONE);
            ImageHandler.loadImageFit2(getActivity()
                    , shopAvatar
                    , MethodChecker.getUri(getActivity(), new File(imagePath)).toString());
            presenter.saveShopAvatarUrl(imagePath);
        }
    }

    @Override
    public void setShopDomain(String shopDomain) {
        if (this.shopDomain != null) {
            this.shopDomain.setText(shopDomain);
        }
    }

    @Override
    public void setShopDomainResult(String message, boolean available) {
        if(domainInput != null){
            if (!available) {
                domainInput.setErrorEnabled(true);
                domainInput.setError(message);
            } else {
                domainInput.setError("");
                domainInput.setErrorEnabled(false);
            }
        }
    }

    @Override
    public boolean checkDomainError() {
        if(shopDomain.getText().toString().isEmpty()){
            domainInput.setError(getString(R.string.error_domain_unfilled));
        }
        return domainInput.isErrorEnabled();
    }

    @Override
    public void setShopName(String shopName) {
        if (this.shopName != null) {
            this.shopName.setText(shopName);
        }
    }

    @Override
    public void setShopNameResult(String message, boolean available) {
        if(nameInput != null){
            if (!available) {
                nameInput.setErrorEnabled(true);
                nameInput.setError(message);
                nameInput.setHint(getString(R.string.title_shop_name));
            } else {
                nameInput.setError("");
                nameInput.setErrorEnabled(false);
                nameInput.setHint(getString(R.string.title_shop_name) + " (" + getString(R.string.shop_name_available) + ")");
            }
        }
    }

    @Override
    public boolean checkNameError() {
        if(shopName.getText().toString().isEmpty()){
            nameInput.setError(getString(R.string.error_name_unfilled));
        }
        return nameInput.isErrorEnabled();
    }

    @Override
    public String getShopTag() {
        return shopTag.getText().toString();
    }

    @Override
    public void setShopTag(String shopTag) {
        if(this.shopTag != null){
            this.shopTag.setText(shopTag);
        }

    }

    @Override
    public boolean checkTagError() {
        if(shopTag.getText().toString().isEmpty()){
            tagInput.setError(getString(R.string.error_tag_unfilled));
        } else if (shopTag.getText().toString().length() > 48) {
            tagInput.setError(getString(R.string.error_tag_too_long));
        } else {
            tagInput.setErrorEnabled(false);
        }
        return tagInput.isErrorEnabled();
    }

    @Override
    public String getShopDesc() {
        return shopDesc.getText().toString();
    }

    @Override
    public void setShopDesc(String shopDesc) {
        if(this.shopDesc != null){
            this.shopDesc.setText(shopDesc);
        }
    }

    @Override
    public boolean checkDescError() {
        if(shopDesc.getText().toString().isEmpty()){
            descInput.setError(getString(R.string.error_desc_unfilled));
        } else if(shopDesc.getText().toString().length() > 140) {
            descInput.setError(getString(R.string.error_desc_too_long));
        } else {
            descInput.setErrorEnabled(false);
        }
        return descInput.isErrorEnabled();
    }

    @Override
    public int getFragmentId() {
        return ShopCreatePresenter.FragmentId;
    }

    @Override
    public void ariseRetry(int type, Object... data) {

    }

    @Override
    public void setData(int type, Bundle data) {

    }

    @Override
    public void onNetworkError(int type, Object... data) {

    }

    @Override
    public void onMessageError(int type, Object... data) {
        switch(type){
            case DOMAIN_ERROR :
                CommonUtils.UniversalToast(getContext(), domainInput.getError().toString());
                break;
            case NAME_ERROR :
                CommonUtils.UniversalToast(getContext(), nameInput.getError().toString());
                break;
            case TAG_ERROR :
                CommonUtils.UniversalToast(getContext(), tagInput.getError().toString());
                break;
            case DESC_ERROR :
                CommonUtils.UniversalToast(getContext(), descInput.getError().toString());
                break;
            default:
                CommonUtils.UniversalToast(getContext(), (String) (data[0]));
                break;
        }

    }

    @Override
    protected void initPresenter() {
        presenter = new ShopCreatePresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shop_create_new;
    }


    @Override
    public View onCreateView(View parentView, Bundle savedInstanceState) {
        Point size = new Point();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display	 = wm.getDefaultDisplay();
        display.getSize(size);
        int imageWidth = (int) (size.x - 4) / 3;
        shopAvatar.setLayoutParams(new FrameLayout.LayoutParams(imageWidth, imageWidth));

        bindView(parentView);

        return super.onCreateView(parentView, savedInstanceState);
    }

    private void bindView(View parentView) {
        // SUBMIT BUTTON
        submitButton = (TextView) parentView.findViewById(R.id.submit_button);

        // VERIFY PHONE
        verifyButton = (TextView) parentView.findViewById(R.id.verify_button);
        verifyInstruction = (TextView) parentView.findViewById(R.id.verify_instruction);

        // SHOP AVATAR
        shopAvatar = (ImageView) parentView.findViewById(R.id.shop_avatar);
        imageText = (TextView) parentView.findViewById(R.id.myImageViewText);

        // DOMAIN CHECKER
        shopDomain = (EditText) parentView.findViewById(R.id.domain);
        domainInput = (TextInputLayout) parentView.findViewById(R.id.domain_input_layout);

        // NAME CHECKER
        shopName = (EditText) parentView.findViewById(R.id.shop_name);
        nameInput = (TextInputLayout) parentView.findViewById(R.id.name_input_layout);

        // TAG CHECKER
        tagInput = (TextInputLayout) parentView.findViewById(R.id.tag_input_layout);
        shopTag = (EditText) parentView.findViewById(R.id.shop_tag);

        // DESC CHECKER
        descInput = (TextInputLayout) parentView.findViewById(R.id.desc_input_layout);
        shopDesc = (EditText) parentView.findViewById(R.id.shop_desc);

        parentView.findViewById(R.id.verify_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVerificationDialog();
            }
        });
        parentView.findViewById(R.id.shop_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUploadDialog();
            }
        });

        parentView.findViewById(R2.id.submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitDialog();
            }
        });
        shopDomain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                domainChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        shopName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        shopTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tagChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        shopDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                descChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imageLocation = null;
        if(requestCode == REQUEST_CAMERA || requestCode == ImageGallery.TOKOPEDIA_GALLERY) {
            switch (resultCode) {
                case GalleryBrowser.RESULT_CODE:
                    imageLocation = data.getStringExtra(ImageGallery.EXTRA_URL);
                    break;
                case Activity.RESULT_OK:
                    break;
                default:
                    break;
            }
        }
        if (imageLocation != null) {
            ImageHandler.LoadImage(shopAvatar, imageLocation);
            imageText.setVisibility(View.GONE);
        }
    }



    @Override
    public void showProgress(boolean showDialog) {
        if (showDialog) {
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
            progressDialog.setCancelable(true);
            progressDialog.showDialog();
        } else {
            if (progressDialog != null) {// &&mProgressDialog.isProgress()
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public Context getMainContext() {
        return getActivity();
    }

    @Override
    public void showPhoneVerification(boolean needVerify) {
        if(needVerify){
            if(((BaseActivity)getActivity()).getPhoneVerificationUtil() != null)
                ((BaseActivity)getActivity()).getPhoneVerificationUtil().setMSISDNListener(new PhoneVerificationUtil.MSISDNListener() {
                    @Override
                    public void onMSISDNVerified() {
                        showPhoneVerification(false);
                    }

                    @Override
                    public void onMSISDNNotVerified() {

                    }

                    @Override
                    public void onNoConnection() {

                    }

                    @Override
                    public void onTimeout() {

                    }

                    @Override
                    public void onFailAuth() {

                    }

                    @Override
                    public void onNullData() {

                    }

                    @Override
                    public void onThrowable(Throwable e) {

                    }

                    @Override
                    public void onError(String error) {

                    }
                });
        }
        submitButton.setVisibility((needVerify)? View.GONE : View.VISIBLE);
        verifyButton.setVisibility((needVerify)? View.VISIBLE : View.GONE);
        verifyInstruction.setVisibility((needVerify)? View.VISIBLE : View.GONE);
    }

    @Override
    public void saveShippingData(OpenShopData shippingData) {
        presenter.saveShippingData(shippingData);
        presenter.sendShopRequest(getActivity());
    }

    @Override
    public void goToEditShipping(OpenShopData openShopData) {
        ShopEditorActivity.continueOpenShopEditShippingActivity((AppCompatActivity) getActivity(), openShopData);
    }

    @Override
    public void startOpenShopEditShippingActivity() {
        showProgress(false);
        ShopEditorActivity.startOpenShopEditShippingActivity((AppCompatActivity) getActivity());
    }

}
