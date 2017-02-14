package com.tokopedia.seller.myproduct.presenter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.seller.myproduct.model.ManageProductModel;
import com.tokopedia.seller.myproduct.model.getProductList.ProductList;
import com.tokopedia.core.util.PagingHandler;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * Created by noiz354 on 6/17/16.
 */
public class ManageProductPresenterImpl implements ManageProductPresenter{
    private ManageProductView manageProductView;
    private GlobalCacheManager cacheManager;

    public ManageProductPresenterImpl(ManageProductView manageProductView) {
        this.manageProductView = manageProductView;
    }

    @Override
    public ManageProductModel convertTo(ProductList.Product prodDetail) {
        ManageProductModel manageProductModel = new ManageProductModel();
        manageProductModel.setProdName(prodDetail.getProductName());
        manageProductModel.setProdImgUri(prodDetail.getProductImage());
        manageProductModel.setProdID(prodDetail.getProductId());
        manageProductModel.setPStatus(prodDetail.getProductStatus());
        manageProductModel.setDepName(prodDetail.getProductDepartment());
        manageProductModel.setEtalaseLoc(prodDetail.getProductEtalase());
        manageProductModel.setCurrencyCode(prodDetail.getProductCurrency());
        int currencyCodeInt = Integer.parseInt(prodDetail.getProductCurrencyId());
        manageProductModel.setCurrencyCodeInt(currencyCodeInt);

        if (checkNotNull(prodDetail.getProductReturnable()))
            manageProductModel.setReturnablePolicy(prodDetail.getProductReturnable());
        else
            manageProductModel.setReturnablePolicy(0);

        if (currencyCodeInt == CurrencyFormatHelper.RUPIAH)
            manageProductModel.setPrice(CurrencyFormatHelper
                    .ConvertToRupiah(prodDetail
                            .getProductNoIdrPrice()));
        else
            manageProductModel.setPrice(CurrencyFormatHelper
                    .ConvertToDollar(prodDetail
                            .getProductNoIdrPrice()));
        manageProductModel.setChecked(false);
        return manageProductModel;
    }

    public CacheManageProduct getCache(){
        if(cacheManager==null){
            cacheManager = new GlobalCacheManager();
        }

        // initialize class you want to be converted from string
        Type type = new TypeToken<CacheManageProduct>() {}.getType();

        cacheManager.setKey(CACHE_KEY);

        if(cacheManager.isAvailable(CACHE_KEY)){
            try {
                String valueString = cacheManager.getValueString(CACHE_KEY);
                if(checkNotNull(valueString)) {// update existing value
                    CacheManageProduct manageProduct = CacheUtil.convertStringToModel(valueString, type);
                    return manageProduct;
                }
            }catch (RuntimeException e){
                return null;
            }
        }else {
            return null;
        }

        return null;
    }

    public void resetCache(){
        resetCache(cacheManager);
    }

    public static void resetCache(GlobalCacheManager globalCacheManager){
        if(globalCacheManager==null){
            globalCacheManager = new GlobalCacheManager();
        }
        globalCacheManager.delete(CACHE_KEY);
    }

    @Override
    public void saveCache(CacheManageProduct newManageProduct) throws MalformedURLException, UnsupportedEncodingException {
        if(cacheManager==null){
            cacheManager = new GlobalCacheManager();
        }

        // initialize class you want to be converted from string
        Type type = new TypeToken<CacheManageProduct>() {}.getType();

        cacheManager.setKey(CACHE_KEY);


        if(cacheManager.isAvailable(CACHE_KEY)){
            try {
                String valueString = cacheManager.getValueString(CACHE_KEY);
                if(checkNotNull(valueString)) {// update existing value
                    // OLD
                    CacheManageProduct manageProduct = CacheUtil.convertStringToModel(valueString, type);
                    int uriNext = getNext(manageProduct);
                    int uriPrevious = getPrevious(manageProduct);

                    // NEW
                    int newUriNext = getNext(newManageProduct);
                    int newUriPrevious = getPrevious(newManageProduct);

                    if(newUriNext == -1){
                        manageProduct.pagingHandlerModel = PagingHandler.createPagingHandlerModel(Integer.MAX_VALUE, null, null);
                    }else{
                        if(newUriNext>uriNext){// jika benar
                            manageProduct.pagingHandlerModel = newManageProduct.pagingHandlerModel;
                        }
                    }

                    manageProduct.productModels.addAll(newManageProduct.productModels);
                    manageProduct.isProdManager = newManageProduct.isProdManager;
                    manageProduct.IsAllowShop = newManageProduct.IsAllowShop;

                    // simpan yang baru
                    cacheManager.setValue(CacheUtil.convertModelToString(manageProduct, type));
                    cacheManager.setCacheDuration(duration);
                    cacheManager.store();

                    Log.d(TAG, MESSAGE_TAG+" [uriNext] "+uriNext+" [uriPrevious] "+uriPrevious);
                }
            }catch (RuntimeException e){
                // simpan yang baru
                cacheManager.setValue(CacheUtil.convertModelToString(newManageProduct, type));
                cacheManager.setCacheDuration(duration);
                cacheManager.store();
            }
        }else {
            // simpan yang baru
            cacheManager.setValue(CacheUtil.convertModelToString(newManageProduct, type));
            cacheManager.setCacheDuration(duration);
            cacheManager.store();
        }

    }

    @Nullable
    private int getNext(CacheManageProduct manageProduct) throws UnsupportedEncodingException, MalformedURLException {
        String uriNext = manageProduct.pagingHandlerModel.getUriNext();
        int next = -1;
        if(checkNotNull(uriNext)&& !uriNext.isEmpty()&& !uriNext.equals("0")) {
            Map<String, String> splitQuery = PagingHandler.splitQuery(new URL(uriNext));
            if(splitQuery != null && !splitQuery.isEmpty() && splitQuery.get("page") != null)
                next = Integer.parseInt(splitQuery.get("page"));
        }
        return next;
    }

    @Nullable
    private int getPrevious(CacheManageProduct manageProduct) throws UnsupportedEncodingException, MalformedURLException {
        int previous = -1;
        String uriPrevious = manageProduct.pagingHandlerModel.getUriPrevious();
        if(checkNotNull(uriPrevious) && !uriPrevious.isEmpty() && !uriPrevious.equals("0")){
            Map<String, String> splitQuery = PagingHandler.splitQuery(new URL(uriPrevious));
            if(splitQuery != null && !splitQuery.isEmpty() && splitQuery.get("page") != null)
                previous = Integer.parseInt(splitQuery.get("page"));
        }
        return previous;
    }


    /**
     * This class for cache
     */
    public static class CacheManageProduct{
        public List<ManageProductModel> productModels;
        public int isProdManager;
        public PagingHandler.PagingHandlerModel pagingHandlerModel;
        public String IsAllowShop;
    }
}
