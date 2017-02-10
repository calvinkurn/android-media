package com.tokopedia.seller.shop.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.tokopedia.core.app.TActivity;
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

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.tokopedia.seller.shop.presenter.ShopCreatePresenter.DESC_ERROR;
import static com.tokopedia.seller.shop.presenter.ShopCreatePresenter.DOMAIN_ERROR;
import static com.tokopedia.seller.shop.presenter.ShopCreatePresenter.NAME_ERROR;
import static com.tokopedia.seller.shop.presenter.ShopCreatePresenter.TAG_ERROR;

/**
 * Created by Toped18 on 5/19/2016.
 */
public class ShopCreateFragment extends BaseFragment<ShopCreatePresenter> implements ShopCreateView {

    public static final int REQUEST_CAMERA = 111;
    private TkpdProgressDialog progressDialog;

    // SUBMIT BUTTON
    @BindView(R2.id.submit_button)
    TextView submitButton;

    // VERIFY PHONE
    @BindView(R2.id.verify_button)
    TextView verifyButton;
    @BindView(R2.id.verify_instruction)
    TextView verifyInstruction;

    // SHOP AVATAR
    @BindView(R2.id.shop_avatar)
    ImageView shopAvatar;
    @BindView(R2.id.myImageViewText)
    TextView imageText;

    // DOMAIN CHECKER
    @BindView(R2.id.domain)
    EditText shopDomain;
    @BindView(R2.id.domain_input_layout)
    TextInputLayout domainInput;

    // NAME CHECKER
    @BindView(R2.id.shop_name)
    EditText shopName;
    @BindView(R2.id.name_input_layout)
    TextInputLayout nameInput;

    // TAG CHECKER
    @BindView(R2.id.tag_input_layout)
    TextInputLayout tagInput;
    @BindView(R2.id.shop_tag)
    EditText shopTag;

    // DESC CHECKER
    @BindView(R2.id.desc_input_layout)
    TextInputLayout descInput;
    @BindView(R2.id.shop_desc)
    EditText shopDesc;

    @OnClick(R2.id.verify_button)
    public void showVerificationDialog(){
        ((TActivity)getActivity()).phoneVerificationUtil.showVerificationDialog();
    }

    @Override
    public void setShopAvatar(String imagePath) {
        if(imagePath != ""){
            imageText.setVisibility(View.GONE);
            ImageHandler.loadImageFit2(getActivity()
                    , shopAvatar
                    , MethodChecker.getUri(getActivity(), new File(imagePath)).toString());
            presenter.saveShopAvatarUrl(imagePath);
        }
    }

    @OnClick(R2.id.shop_avatar)
    public void startUploadDialog(){
        ImageGalleryEntry.moveToImageGallery((AppCompatActivity)getActivity(), 0, 1);
    }

    @OnClick(R2.id.submit_button)
    public void SubmitDialog(){
        showProgress(true);
        presenter.saveDescTag();
        presenter.finalCheckDomainName(
                shopDomain.getText().toString(),
                shopName.getText().toString()
        );
        UnifyTracking.eventCreateShop();
    }

    @Override
    public void setShopDomain(String shopDomain) {
        if (this.shopDomain != null){
            this.shopDomain.setText(shopDomain);
        }
    }

    @OnTextChanged(R2.id.domain)
    void domainChanged() {
        if(shopDomain.getText().toString().length() != 0) {
            domainInput.setHint(getString(R.string.title_hint_domain) + " : "
                    + getString(R.string.domain) + shopDomain.getText().toString());
            presenter.checkShopDomain(shopDomain.getText().toString());
        } else {
            domainInput.setHint(getString(R.string.title_hint_domain));
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

    @OnTextChanged(R2.id.shop_name)
    void nameChanged(){
        if(shopName.getText().toString().length() != 0) {
            presenter.checkShopName(shopName.getText().toString());
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

    @OnTextChanged(R2.id.shop_tag)
    void tagChanged(){
        checkTagError();
    }

    @Override
    public void setShopTag(String shopTag) {
        if(this.shopTag != null){
            this.shopTag.setText(shopTag);
        }

    }

    @Override
    public String getShopTag() {
        return shopTag.getText().toString();
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

    @OnTextChanged(R2.id.shop_desc)
    void descChanged(){
        checkDescError();
    }

    @Override
    public void setShopDesc(String shopDesc) {
        if(this.shopDesc != null){
            this.shopDesc.setText(shopDesc);
        }
    }

    @Override
    public String getShopDesc() {
        return shopDesc.getText().toString();
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

    public static Fragment newInstance(){
        ShopCreateFragment fragment = new ShopCreateFragment();
        return fragment;
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
        return super.onCreateView(parentView, savedInstanceState);
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
            if(((TActivity)getActivity()).phoneVerificationUtil != null)
                ((TActivity)getActivity()).phoneVerificationUtil.setMSISDNListener(new PhoneVerificationUtil.MSISDNListener() {
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
