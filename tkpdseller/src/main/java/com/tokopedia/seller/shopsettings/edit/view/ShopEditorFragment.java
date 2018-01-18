package com.tokopedia.seller.shopsettings.edit.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.common.imageeditor.GalleryCropActivity;
import com.tokopedia.seller.instoped.InstopedSellerCropperActivity;
import com.tokopedia.seller.product.edit.view.dialog.ImageEditDialogFragment;
import com.tokopedia.seller.shopsettings.edit.presenter.ShopEditorPresenter;
import com.tokopedia.seller.shopsettings.edit.presenter.ShopEditorPresenterImpl;
import com.tokopedia.seller.shopsettings.edit.presenter.ShopEditorView;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Toped10 on 5/19/2016.
 */
@RuntimePermissions
public class ShopEditorFragment extends BaseFragment<ShopEditorPresenter> implements ShopEditorView {

    private static final String TOP_SELLER_APPLICATION_PACKAGE = "com.tokopedia.sellerapp";

    EditText mShopNameText;
    EditText mShopSloganText;
    EditText mShopDescText;
    TextView mBtnSend;
    ProgressBar progressBar;
    ImageView mShopAva;
    ScrollView mShopEditor;
    ImageButton editShopSchedule;
    ImageView closeImage;
    TextView shopStatus;
    TextView scheduleInfo;
    TextView scheduleDate;
    ImageView timeIcon;
    ImageView icon_gold_merchant;
    TextView status_gold;
    TextView desc_status;
    TextView about_gm;
    private TkpdProgressDialog mProgressDialog;

    OnShopEditorFragmentListener onShopEditorFragmentListener;
    public interface OnShopEditorFragmentListener{
        void deleteCacheShopInfov2();
    }

    public void uploadImage(View view) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ImageEditDialogFragment dialogFragment = ImageEditDialogFragment.newInstance(0);
        dialogFragment.show(fm, ImageEditDialogFragment.FRAGMENT_TAG);
        dialogFragment.setOnImageEditListener(new ImageEditDialogFragment.OnImageEditListener() {

            @Override
            public void clickEditProductFromCamera(int position) {
                ShopEditorFragmentPermissionsDispatcher.goToCameraWithCheck(ShopEditorFragment.this, 0);
            }

            @Override
            public void clickEditProductFromGallery(int position) {
                ShopEditorFragmentPermissionsDispatcher.goToGalleryWithCheck(ShopEditorFragment.this, 0);
            }
        });
    }

    @TargetApi(16)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void goToGallery(int imagePosition) {
        GalleryCropActivity.moveToImageGallery(getActivity(), imagePosition, 1, true);
    }

    @TargetApi(16)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void goToCamera(int imagePosition) {
        GalleryCropActivity.moveToImageGalleryCamera(getActivity(), imagePosition, true, 1,true);
    }

    public void kirimData(View view) {
        presenter.sendDataShop();
    }

    void showEditShopScheduleDialog() {
        presenter.onClickCloseShop(presenter);
    }

    void showAboutGM() {
        if(GlobalConfig.isSellerApp()) {
            if(getActivity().getApplication() instanceof TkpdCoreRouter){
                ((TkpdCoreRouter) getActivity().getApplication()).goToMerchantRedirect(getActivity());
            }
        }else{
            Intent launchIntent = getActivity().getPackageManager()
                    .getLaunchIntentForPackage(TOP_SELLER_APPLICATION_PACKAGE);

            if (launchIntent != null) {
                UnifyTracking.eventGMSwitcherManageShop(AppEventTracking.EventLabel.OPEN_TOP_SELLER+AppEventTracking.EventLabel.OPEN_APP);
                getActivity().startActivity(launchIntent);
            } else if (getActivity().getApplication() instanceof TkpdCoreRouter) {
                UnifyTracking.eventGMSwitcherManageShop(AppEventTracking.EventLabel.OPEN_GM+AppEventTracking.Category.SWITCHER);
                ((TkpdCoreRouter) getActivity().getApplication()).goToCreateMerchantRedirect(getActivity());
            }
        }
    }

    @Override
    public View onCreateView(View parentView, Bundle savedInstanceState) {
        mShopNameText = (EditText) parentView.findViewById(R.id.shop_name);
        mShopSloganText = (EditText) parentView.findViewById(R.id.shop_slogan);
        mShopDescText = (EditText) parentView.findViewById(R.id.shop_desc);
        mBtnSend = (TextView) parentView.findViewById(R.id.button_send);
        progressBar = (ProgressBar) parentView.findViewById(R.id.progress_bar);
        mShopAva = (ImageView) parentView.findViewById(R.id.shop_ava);
        mShopEditor = (ScrollView) parentView.findViewById(R.id.shop_editor_scrollview);
        editShopSchedule = (ImageButton) parentView.findViewById(R.id.edit_shop_schedule);
        closeImage = (ImageView) parentView.findViewById(R.id.close_image);
        shopStatus = (TextView) parentView.findViewById(R.id.shop_status);
        scheduleInfo = (TextView) parentView.findViewById(R.id.schedule_info);
        scheduleDate = (TextView) parentView.findViewById(R.id.schedule_date);
        timeIcon = (ImageView) parentView.findViewById(R.id.time_icon);
        icon_gold_merchant = (ImageView) parentView.findViewById(R.id.icon_gold_merchant);
        status_gold = (TextView) parentView.findViewById(R.id.status_gold);
        desc_status = (TextView) parentView.findViewById(R.id.desc_status_gold);
        about_gm = (TextView) parentView.findViewById(R.id.about_gm);

        mShopAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(v);
            }
        });

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimData(v);
            }
        });

        editShopSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAdded()) {
                    return;
                }
                showEditShopScheduleDialog();
            }
        });

        about_gm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutGM();
            }
        });

        return super.onCreateView(parentView, savedInstanceState);
    }

    @Override
    public int getFragmentId() {
        return ShopEditorPresenter.FragmentId;
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
        CommonUtils.UniversalToast(getContext(), (String) (data[0]));
    }

    @Override
    protected void initPresenter() {
        presenter = new ShopEditorPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_editor;
    }

    @Override
    public void initView() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void initViewInstance() {
        mProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.MAIN_PROGRESS, getActivity().getWindow().getDecorView().getRootView());
        mProgressDialog.setLoadingViewId(R.id.include_loading);
    }

    @Override
    public View getRootView() {
        return getView();
    }

    @Override
    public void initAnalytics() {
//        AnalyticsHandler.init(getActivity()).
//                type(Type.GA).sendScreen(AppScreen.SCREEN_CONFIG_S_INFO);
    }

    @Override
    public void uploadImage(String data) {
        presenter.uploadUpdateImage(data);
    }

    @Override
    public void setData(int type, Object... data) {
        switch (type) {
            case ShopEditorPresenter.SHOP_DESC_ERROR:
                mShopDescText.setError((String) data[0]);
                break;
            case ShopEditorPresenter.SHOP_NAME_ERROR:
                mShopNameText.setError((String) data[0]);
                break;
            case ShopEditorPresenter.SHOP_SLOGAN_ERROR:
                mShopSloganText.setError((String) data[0]);
                break;
            case ShopEditorPresenter.SHOP_NAME:
                mShopNameText.setText(MethodChecker.fromHtml((String) data[0]));
                break;
            case ShopEditorPresenter.SHOP_SLOGAN:
                mShopSloganText.setText(MethodChecker.fromHtml((String) data[0]));
                break;
            case ShopEditorPresenter.SHOP_DESC:
                mShopDescText.setText(MethodChecker.fromHtml((String) data[0]));
                break;
            default:
                throw new RuntimeException("please register type here!!!");
        }
    }

    @Override
    public Object getData(int type) {
        switch (type) {
            case ShopEditorPresenter.SHOP_NAME:
                return mShopNameText.getText().toString();
            case ShopEditorPresenter.SHOP_SLOGAN:
                return mShopSloganText.getText().toString();
            case ShopEditorPresenter.SHOP_DESC:
                return mShopDescText.getText().toString();
            default:
                return null;
        }
    }

    @Override
    public void loadImageAva(String mShopAvaUri, int ic_default_shop_ava) {
        ImageHandler.loadImage2(mShopAva, mShopAvaUri, ic_default_shop_ava);
    }

    @Override
    public void loadImageAva(String url) {
        ImageHandler.loadImageFit2(getContext(), mShopAva, url);
    }

    @Override
    public void deleteShopCachev2() {
        onShopEditorFragmentListener.deleteCacheShopInfov2();
    }

    @Override
    public void showButtonSend() {
        mBtnSend.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButttonSend() {
        mBtnSend.setVisibility(View.GONE);
    }

    @Override
    public void showDialog() {
        mProgressDialog.showDialog();
    }

    @Override
    public void showDialogNormal() {
        mProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        mProgressDialog.showDialog();
    }

    @Override
    public void hideDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAvaImage() {
        mShopAva.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAvaImage() {
        mShopAva.setVisibility(View.GONE);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideSoftInputWindow() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mShopNameText.getWindowToken(), 0);
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void showShopEditor() {
        mShopEditor.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideShopEditor() {
        mShopEditor.setVisibility(View.GONE);
    }

    @Override
    public void setOpenShop() {
        closeImage.setImageResource(R.drawable.icon_open);
        shopStatus.setText(getString(R.string.open));
        scheduleInfo.setText(getString(R.string.tidak_ada_jadwal));
        scheduleDate.setVisibility(View.GONE);
        timeIcon.setVisibility(View.GONE);
    }

    @Override
    public void setCloseShop(String closeEnd) {
        closeImage.setImageResource(R.drawable.icon_closed);
        shopStatus.setText(getString(R.string.close));
        scheduleInfo.setText("Sampai dengan " + closeEnd);
        scheduleDate.setVisibility(View.GONE);
        timeIcon.setVisibility(View.GONE);
    }

    @Override
    public void setCloseShopWithSchedule(String closeEnd) {
        closeImage.setImageResource(R.drawable.icon_open);
        shopStatus.setText(getString(R.string.open));
        scheduleInfo.setText(getString(R.string.jadwal_tutup_toko));
        scheduleDate.setText(closeEnd);
        scheduleDate.setVisibility(View.VISIBLE);
        timeIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void setShopIsGold(String until) {
        icon_gold_merchant.setVisibility(View.VISIBLE);
        status_gold.setText("Gold Merchant");
        desc_status.setText("Berlaku Sampai :" + until);
        about_gm.setText("Perpanjang Gold Merchant");
    }

    @Override
    public void setShopReguler() {
        icon_gold_merchant.setVisibility(View.GONE);
        status_gold.setText("Regular Merchant");
        desc_status.setText("Anda Belum Berlangganan Gold Merchant");
        about_gm.setText("Tentang Gold Merchant");
    }

    @TargetApi(23)
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    protected void onAttachToContext(Context context) {
        onShopEditorFragmentListener = (OnShopEditorFragmentListener) context;
    }
}
