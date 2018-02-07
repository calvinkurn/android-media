package com.tokopedia.seller.shopsettings.edit.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.tkpd.library.utils.DownloadResultReceiver;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.R;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.prototype.ShopCache;
import com.tokopedia.core.prototype.ShopSettingCache;
import com.tokopedia.seller.shopsettings.edit.view.ShopEditService;
import com.tokopedia.seller.shopsettings.edit.constant.ShopEditServiceConstant;
import com.tokopedia.seller.shopsettings.edit.view.ShopScheduleDialog;
import com.tokopedia.core.shop.model.ShopEditorModel;
import com.tokopedia.core.shop.model.ShopScheduleModel;
import com.tokopedia.core.shop.model.UpdateShopImageModel;
import com.tokopedia.core.shop.model.responseEdit.ResponseEdit;
import com.tokopedia.core.shop.model.shopData.ClosedScheduleDetail;
import com.tokopedia.core.shop.model.shopData.Data;
import com.tokopedia.core.shop.model.shopData.Image;
import com.tokopedia.core.shop.model.shopData.Info;
import com.tokopedia.core.util.SessionHandler;

import org.parceler.Parcels;

import static android.text.TextUtils.isEmpty;


/**
 * Created by Toped10 on 5/19/2016.
 */
public class ShopEditorPresenterImpl extends ShopEditorPresenter implements DownloadResultReceiver.Receiver {

    private Context context;
    ShopEditorModel shopEditorModel = new ShopEditorModel();
    private DownloadResultReceiver mReceiver;
    private boolean isViewActive = true;
    private Data modelShopData;
    ShopScheduleDialog editScheduleDialog;
    private final GlobalCacheManager cacheManager;

    public ShopEditorPresenterImpl(ShopEditorView view) {
        super(view);
        cacheManager = new GlobalCacheManager();
    }

    @Override
    public void initData(@NonNull Context context) {
        view.initView();
        if(!isAfterRotate){
            view.initAnalytics();
            view.hideShopEditor();
            view.showDialog();
            getShopData();
        }else{
            view.setData(SHOP_NAME, shopEditorModel.getmShopNameText());
            view.setData(SHOP_SLOGAN, shopEditorModel.getmShopSloganText());
            view.setData(SHOP_DESC, shopEditorModel.getmShopDescText());
            view.loadImageAva(shopEditorModel.getmShopAvaUri());
            modelShopData = shopEditorModel.getModelShopData();

            if(modelShopData != null) {
                view.hideDialog();
                if (modelShopData.getInfo() != null && modelShopData.getInfo().getShopIsGold() == 1) {
                    view.setShopIsGold(modelShopData.getInfo().getShopGoldExpiredTime());
                } else {
                    view.setShopReguler();
                }

                if (modelShopData.getClosedScheduleDetail().getCloseStatus() == 1) {
                    view.setOpenShop();
                } else if (modelShopData.getClosedScheduleDetail().getCloseStatus() == 2) {
                    view.setCloseShop(modelShopData.getClosedScheduleDetail().getCloseEnd());
                } else if (modelShopData.getClosedScheduleDetail().getCloseStatus() == 3) {
                    view.setCloseShopWithSchedule(modelShopData.getClosedScheduleDetail().getCloseStart());
                } else {
                    view.setOpenShop();
                }
            }
        }
    }

    @Override
    public void initDataInstance(Context context) {
        this.context = context;
        view.initViewInstance();
        mReceiver = new DownloadResultReceiver(new Handler());
    }

    @Override
    public void fetchArguments(Bundle argument) {

    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {
        shopEditorModel = Parcels.unwrap(argument.getParcelable(DATA));
    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {
        shopEditorModel.setmShopNameText(view.getData(SHOP_NAME).toString());
        shopEditorModel.setmShopSloganText(view.getData(SHOP_SLOGAN).toString());
        shopEditorModel.setmShopDescText(view.getData(SHOP_DESC).toString());
        shopEditorModel.setModelShopData(modelShopData);
        argument.putParcelable(DATA, Parcels.wrap(shopEditorModel));
    }

    @Override
    public void sendDataShop() {
        if(shopEditorModel.isUploadingAvatar()){
            view.showToast(context.getString(R.string.error_upload_gambar));
            return;
        }
        view.hideSoftInputWindow();
        boolean cancel = false;
        shopEditorModel.setmShopName((String) view.getData(SHOP_NAME));
        shopEditorModel.setmShopSlogan((String) view.getData(SHOP_SLOGAN));
        shopEditorModel.setmShopDesc((String) view.getData(SHOP_DESC));

        if(isEmpty(shopEditorModel.getmShopName())) {
            view.setData(SHOP_NAME_ERROR, context.getString(R.string.error_field_required));
            cancel = true;
        }

        if(isEmpty(shopEditorModel.getmShopSlogan())) {
            view.setData(SHOP_SLOGAN_ERROR, context.getString(R.string.error_field_required));
            cancel = true;
        }

        if(isEmpty(shopEditorModel.getmShopDesc())) {
            view.setData(SHOP_DESC_ERROR, context.getString(R.string.error_field_required));
            cancel = true;
        }

        if (!cancel) {
            postShopData();
        }
    }

    @Override
    public void onClickCloseShop(ShopEditorPresenter presenter) {
        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        if (modelShopData != null && modelShopData != null) {
            editScheduleDialog = ShopScheduleDialog.newInstance(modelShopData.getClosedDetail(), modelShopData.getClosedScheduleDetail());
            editScheduleDialog.setShopEditorPresenter(presenter);
            editScheduleDialog.show(ft, "edit_chedusle_dialog");
        }
    }

    @Override
    public String getMessageTAG() {
        return null;
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return null;
    }

    @Override
    public void uploadUpdateImage(Intent data) {
        Bundle bundle = new Bundle();
        String fileLoc = data.getStringExtra(ImageGallery.EXTRA_URL);
        bundle.putString(ShopEditService.INPUT_IMAGE, fileLoc);
        sendShopData(ShopEditService.UPDATE_SHOP_IMAGE, bundle);
    }

    @Override
    public void uploadUpdateImage(String data) {
        Bundle bundle = new Bundle();
        bundle.putString(ShopEditService.INPUT_IMAGE, data);
        sendShopData(ShopEditService.UPDATE_SHOP_IMAGE, bundle);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        int type = resultData.getInt(ShopEditService.TYPE, ShopEditService.INVALID_TYPE);
        switch (resultCode) {
            case ShopEditService.STATUS_RUNNING:
                switch (type) {
                    case ShopEditServiceConstant.POST_EDIT_DATA:
                        if(isViewActive) {
                            view.showDialogNormal();
                        }
                        break;
                    case ShopEditService.GET_SHOP_DATA:
                        if(isViewActive) {
                            view.showDialog();
                        }
                        break;
                    case ShopEditService.UPDATE_SHOP_IMAGE:
                        if(isViewActive){
                            shopEditorModel.setUploadingAvatar(true);
                            view.hideAvaImage();
                            view.showProgress();
                        }
                        break;
                    case ShopEditService.UPDATE_SHOP_SCHEDULE:
                        if(isViewActive){
                            view.showDialogNormal();
                        }
                        break;
                }
                break;
            case ShopEditService.STATUS_FINISHED:
                switch (type) {
                    case ShopEditService.GET_SHOP_DATA:
                        modelShopData = Parcels.unwrap(resultData.getParcelable(ShopEditService.MODEL_GET_SHOP_DATA));
                        if(isViewActive){
                            view.hideDialog();
                            SetToUI(modelShopData);
                        }
                        String jsonShopDataCache = resultData.getString(ShopEditService.JSON_SHOP_DATA_CACHE);
                        ShopSettingCache.SaveCache(ShopSettingCache.CODE_SHOP_INFO, jsonShopDataCache, context);

                        //((BaseView) fragment).setData(type, resultData);
                        break;
                    case ShopEditServiceConstant.POST_EDIT_DATA:
                        ResponseEdit.DataBean dataBean = Parcels.unwrap(resultData.getParcelable(ShopEditService.MODEL_RESPONSE_EDIT_SHOP));
                        ShopSettingCache.DeleteCache(ShopSettingCache.CODE_SHOP_INFO, context);
                        if(dataBean.getIs_success() == 1){
                            if(isViewActive){
                                view.showToast(context.getResources().getString(R.string.title_success_update_shop));
                                view.finishActivity();
                            }
                            ShopCache.DeleteCache(SessionHandler.getShopID(context), (Activity)context);
                            view.deleteShopCachev2();
                        }
                        break;
                    case ShopEditServiceConstant.UPDATE_SHOP_IMAGE:
                        UpdateShopImageModel updateShopImageModel = Parcels.unwrap(resultData.getParcelable(ShopEditService.UPLOAD_SHOP_LOGO_DATA));
                        if (updateShopImageModel == null || updateShopImageModel.getData() == null) {
                            return;
                        }
                        if(updateShopImageModel.getData().getIs_success() == 1){
                            ShopCache.DeleteCache(SessionHandler.getShopID(context), (Activity)context);
                            view.deleteShopCachev2();
                            ShopSettingCache.DeleteCache(ShopSettingCache.CODE_SHOP_INFO, context);
                            cacheManager.delete(ProfileSourceFactory.KEY_PROFILE_DATA);
                            shopEditorModel.setUploadingAvatar(false);
                            if(isViewActive) {
                                view.loadImageAva(resultData.getString(ShopEditService.PIC_SRC));
                                view.showAvaImage();
                                view.hideProgress();
                            }
                        }
                        break;
                    case ShopEditService.UPDATE_SHOP_SCHEDULE:
                        ResponseEdit.DataBean dataBean1 = Parcels.unwrap(resultData.getParcelable(ShopEditService.MODEL_RESPONSE_UPDATE_SHOP_CLOSE));
                        ShopSettingCache.DeleteCache(ShopSettingCache.CODE_SHOP_INFO, context);
                        if(dataBean1.getIs_success() == 1){
                            if(isViewActive){
                                if(editScheduleDialog != null){
                                    editScheduleDialog.closeDialog();
                                }
                                view.hideDialog();
                                view.showToast(context.getResources().getString(R.string.title_success_update_shop_schedule_close));
                                view.hideShopEditor();
                                getShopData();
                            }
                            ShopCache.DeleteCache(SessionHandler.getShopID(context), (Activity)context);
                            view.deleteShopCachev2();
                        }

                        break;
                }
                break;
            case ShopEditService.STATUS_ERROR:
                switch (type) {
                    case ShopEditService.UPDATE_SHOP_SCHEDULE:
                    case ShopEditServiceConstant.POST_EDIT_DATA:
                    case ShopEditService.GET_SHOP_DATA:
                        if(isViewActive){
                            view.hideDialog();
                        }
                        break;
                }
                switch (resultData.getInt(ShopEditService.NETWORK_ERROR_FLAG, ShopEditService.INVALID_NETWORK_ERROR_FLAG)) {
                    case NetworkConfig.BAD_REQUEST_NETWORK_ERROR:
                        if(isViewActive) {
                            view.showToast(" BAD_REQUEST_NETWORK_ERROR !!!");
                        }
                        break;
                    case NetworkConfig.INTERNAL_SERVER_ERROR:
                        if(isViewActive){
                            view.showToast(" INTERNAL_SERVER_ERROR !!!");
                        }
                        break;
                    case NetworkConfig.FORBIDDEN_NETWORK_ERROR:
                        if(isViewActive){
                            view.showToast(" FORBIDDEN_NETWORK_ERROR !!!");
                        }
                        break;
                    case ShopEditService.INVALID_NETWORK_ERROR_FLAG:
                    default:
                        String messageError = resultData.getString(ShopEditService.MESSAGE_ERROR_FLAG, ShopEditService.INVALID_MESSAGE_ERROR);
                        if (!messageError.equals(ShopEditService.INVALID_MESSAGE_ERROR)) {
                            if(isViewActive){
                                switch (type){
                                    case ShopEditService.GET_SHOP_DATA:
                                        if(ShopSettingCache.getSetting(ShopSettingCache.CODE_SHOP_INFO, context)!=null){
                                            view.showToast(messageError);
                                        }
                                        else {
                                            NetworkErrorHelper.showEmptyState(context, view.getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                                                @Override
                                                public void onRetryClicked() {
                                                    getShopData();
                                                }
                                            });
                                        }
                                        break;
                                    default:
                                        view.showToast(messageError);
                                        break;
                                }
                            }
                        }
                }
                break;
        }// end of status download service
    }


    @Override
    public void unSubscribe() {
        super.unSubscribe();
        isViewActive = false;
    }

    @Override
    public void subscribe() {
        super.subscribe();
        mReceiver.setReceiver(this);
        isViewActive = true;
    }

    @Override
    public void updateShopSchedule(ShopScheduleModel shopScheduleModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ShopEditService.MODEL_SCHEDULE_SHOP, Parcels.wrap(shopScheduleModel));
        sendShopData(ShopEditService.UPDATE_SHOP_SCHEDULE, bundle);
    }

    private void SetToUI(Data dataShopModel){
        try {
            shopEditorModel.setIsAllowShop(dataShopModel.getIsAllow().toString());
            Info InfoObj = dataShopModel.getInfo();

            ClosedScheduleDetail closedScheduleDetail = dataShopModel.getClosedScheduleDetail();
            view.setData(SHOP_NAME, TextUtils.isEmpty(InfoObj.getShopName()) ? "" : InfoObj.getShopName());
            Image mShopAvaObj = dataShopModel.getImage();
            if(!isEmpty(mShopAvaObj.getLogo())) {
                shopEditorModel.setmShopAvaUri(mShopAvaObj.getLogo());
            }
            if(shopEditorModel.getIsAllowShop().equals("0")) {
                view.hideButttonSend();
            } else {
                view.showButtonSend();
            }
            view.setData(SHOP_SLOGAN, isEmpty(InfoObj.getShopTagline()) ? "" : InfoObj.getShopTagline());
            view.setData(SHOP_DESC, isEmpty(InfoObj.getShopDescription()) ? "" : InfoObj.getShopDescription());
            view.hideDialog();

            view.showShopEditor();
            view.loadImageAva(shopEditorModel.getmShopAvaUri(), R.drawable.ic_default_shop_ava);

            if(closedScheduleDetail.getCloseStatus() == 1){
                view.setOpenShop();
            }else if(closedScheduleDetail.getCloseStatus() == 2){
                view.setCloseShop(closedScheduleDetail.getCloseEnd());
            }else if(closedScheduleDetail.getCloseStatus() == 3){
                view.setCloseShopWithSchedule(closedScheduleDetail.getCloseStart());
            } else {
                view.setOpenShop();
            }

            if(InfoObj.getShopIsGold() == 1){
                view.setShopIsGold(InfoObj.getShopGoldExpiredTime());
            }else{
                view.setShopReguler();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getShopData() {
        sendShopData(ShopEditService.GET_SHOP_DATA, null);
    }

    public void postShopData() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ShopEditService.MODEL_EDIT_SHOP_DATA, Parcels.wrap(shopEditorModel));

        sendShopData(ShopEditService.POST_EDIT_DATA, bundle);
    }

    public void sendShopData(int type, Bundle data) {
        switch (type) {
            case ShopEditService.UPDATE_SHOP_SCHEDULE:
            case ShopEditService.UPDATE_SHOP_IMAGE:
            case ShopEditServiceConstant.POST_EDIT_DATA:
            case ShopEditService.GET_SHOP_DATA:
                ShopEditService.startShopService(context, mReceiver, data, type);
                break;
            default:
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }
    }

    /*public void CheckCache(Context context) {

        if(ShopSettingCache.getSetting(ShopSettingCache.CODE_SHOP_INFO, context)!=null){
            Gson gson = new GsonBuilder().create();
            Data shopCache = gson.fromJson(ShopSettingCache.getSetting(ShopSettingCache.CODE_SHOP_INFO, context).toString(), Data.class);
            modelShopData = shopCache;
            SetToUI(shopCache);
            view.hideDialog();
        }
        else {
            getShopData();
        }
    }*/

}
