package com.tokopedia.discovery.presenter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.discovery.model.HotListBannerModel;
import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.network.entity.discovery.BrowseShopModel;
import com.tokopedia.core.product.interactor.CacheInteractorImpl;
import com.tokopedia.core.product.interactor.RetrofitInteractor;
import com.tokopedia.core.product.interactor.RetrofitInteractorImpl;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.home.SimpleHomeRouter;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.Pair;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.adapter.ProductAdapter;
import com.tokopedia.discovery.fragment.ProductFragment;
import com.tokopedia.discovery.interactor.DiscoveryInteractor;
import com.tokopedia.discovery.interactor.DiscoveryInteractorImpl;
import com.tokopedia.discovery.interfaces.DiscoveryListener;
import com.tokopedia.discovery.model.ErrorContainer;
import com.tokopedia.discovery.model.NetworkParam;
import com.tokopedia.discovery.model.ProductModel;
import com.tokopedia.discovery.util.PagingHandlerUtil;
import com.tokopedia.discovery.view.FragmentBrowseProductView;
import com.tokopedia.tkpdpdp.fragment.ProductDetailFragment;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//import com.tokopedia.core.product.fragment.ProductDetailFragment;

/**
 * Created by noiz354 on 3/24/16.
 */
public class FragmentDiscoveryPresenterImpl extends FragmentDiscoveryPresenter implements DiscoveryListener {

    String TAG = FragmentDiscoveryPresenterImpl.class.getSimpleName();
    DiscoveryInteractor discoveryInteractor;
    WeakReference<Context> context;
    private List<ProductItem> currTopAdsItem;
    private BrowseProductModel browseProductModel;
    int spanCount;
    private int topAdsPaging = 1;
    private CacheInteractorImpl cacheInteractor;
    private RetrofitInteractorImpl retrofitInteractor;

    public FragmentDiscoveryPresenterImpl(FragmentBrowseProductView view) {
        super(view);
    }


    @Override
    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    private void checkTAG() {
        if (TAG == null || TAG.equals(""))
            throw new RuntimeException(getMessageTAG() + "need supply TAG !!!");

    }

    @Override
    public void loadMore(Context context) {
        checkTAG();
        this.context = new WeakReference<Context>(context);

        switch (TAG) {
            case ProductFragment.TAG:
                int startIndexForQuery = view.getStartIndexForQuery(TAG);
                if (context != null && context instanceof BrowseView) {

                    NetworkParam.Product productParam = ((BrowseView) context).getProductParam();

                    Log.d(TAG, "Product Params " + productParam.toString());
                    if (productParam == null)
                        return;

                    productParam.start = Integer.toString(startIndexForQuery);
                    productParam.breadcrumb = false;
                    discoveryInteractor.getProducts(NetworkParam.generateNetworkParamProduct(productParam));
                }

                break;
            default:
                Log.i(TAG, getMessageTAG() + " not implemented yet for this TAG");
                break;
        }
    }

    @Override
    public void sendGTMNoResult(Context context) {
        BrowseProductModel browseProductModel = ((BrowseView) context).getDataForBrowseProduct();
        if (browseProductModel != null) {
            if (browseProductModel.result.products == null || browseProductModel.result.products.length == 0) {
                NetworkParam.Product params = ((BrowseView) context).getProductParam();
                if (params != null) {
                    UnifyTracking.eventDiscoveryNoResult(params.q);
                }
            }
        }
    }

    @Override
    public void getCategoryHeader(String categoryId) {

    }

    @Override
    public void onWishlistButtonClick(ProductItem data, int itemPosition, Context context) {
        int productId = Integer.parseInt(data.getId());
        if (SessionHandler.isV4Login(context)) {
            if (data.productAlreadyWishlist) {
                requestRemoveWishList(context, productId, itemPosition);
            } else {
                requestAddWishList(context, productId, itemPosition);
            }
        } else {
            cacheInteractor.deleteProductDetail(productId);
            Intent intent = SessionRouter.getLoginActivityIntent(context);
            intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
            intent.putExtra("product_id", String.valueOf(productId));
            view.navigateToActivityRequest(intent, ProductDetailFragment.REQUEST_CODE_LOGIN);
        }
    }

    private void requestAddWishList(final Context context, final Integer productId, final int itemPosition) {
        view.loadingWishList();
        TrackingUtils.eventLoca(AppScreen.EVENT_ADDED_WISHLIST);
        retrofitInteractor.addToWishList(context, productId,
                new RetrofitInteractor.AddWishListListener() {
                    @Override
                    public void onSuccess() {
                        view.finishLoadingWishList();
                        view.showDialog(createSuccessWishListDialog(context));
                        view.updateWishListStatus(true, itemPosition);
                        cacheInteractor.deleteProductDetail(productId);
                    }

                    @Override
                    public void onError(String error) {
                        view.finishLoadingWishList();
                        view.showWishListRetry(error);
                    }
                });

    }

    private void requestRemoveWishList(final Context context, final Integer productId, final int itemPosition) {
        view.loadingWishList();
        retrofitInteractor.removeFromWishList(context, productId,
                new RetrofitInteractor.RemoveWishListListener() {
                    @Override
                    public void onSuccess() {
                        view.finishLoadingWishList();
                        view.showToastMessage(context
                                .getString(com.tokopedia.core.R.string.msg_remove_wishlist));
                        view.updateWishListStatus(false, itemPosition);
                        cacheInteractor.deleteProductDetail(productId);
                    }

                    @Override
                    public void onError(String error) {
                        view.finishLoadingWishList();
                        view.showWishListRetry(error);
                    }
                });
    }

    private Dialog createSuccessWishListDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(com.tokopedia.core.R.string.msg_add_wishlist));
        builder.setPositiveButton(context.getString(com.tokopedia.core.R.string.go_to_wishlist),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, SimpleHomeRouter.getSimpleHomeActivityClass());
                        intent.putExtra(
                                SimpleHomeRouter.FRAGMENT_TYPE,
                                SimpleHomeRouter.WISHLIST_FRAGMENT);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        view.navigateToActivity(intent);
                        view.closeView();
                    }
                });
        builder.setNegativeButton(context.getString(com.tokopedia.core.R.string.prompt_shop_again),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public String getMessageTAG() {
        return FragmentDiscoveryPresenterImpl.class.getSimpleName();
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return getMessageTAG() + " : ";
    }

    @Override
    public void initData(@NonNull Context context) {
        ((DiscoveryInteractorImpl) discoveryInteractor).setCompositeSubscription(compositeSubscription);

        switch (TAG) {
            case ProductFragment.TAG:
                if (!isAfterRotate && view.setupRecyclerView()) {
                    if (context != null && context instanceof BrowseView) {
                        browseProductModel = ((BrowseView) context).getDataForBrowseProduct();

                        Log.d(TAG, getMessageTAG() + browseProductModel);
                        if (browseProductModel == null || browseProductModel.result.products == null)
                            return;
                        Pair<List<ProductItem>, PagingHandler.PagingHandlerModel> listPagingHandlerModelPair = parseBrowseProductModel(browseProductModel);
                        HotListBannerModel hotListBannerModel = browseProductModel.hotListBannerModel;
                        if (hotListBannerModel != null) {
                            view.addHotListHeader(new ProductAdapter.HotListBannerModel(hotListBannerModel, browseProductModel.result.hashtag));
                            view.setHotlistData(listPagingHandlerModelPair.getModel1(), listPagingHandlerModelPair.getModel2());
                        } else if (browseProductModel != null
                                && listPagingHandlerModelPair.getModel1() != null
                                && listPagingHandlerModelPair.getModel2() != null) {
                            if (browseProductModel.header != null) {
                                view.updateTotalProduct(browseProductModel.header.getTotalData());
                            }
                            if (browseProductModel.result.products.length == 0) {
                                view.displayEmptyResult();
                            } else {
                                processBrowseProduct(listPagingHandlerModelPair.getModel1(), listPagingHandlerModelPair.getModel2());
                            }
                        }
                        sendGTMNoResult(context);
                    }
                }
                break;
            default: {

            }
            break;
        }
    }

    public BrowseProductModel getBrowseProductModel() {
        return browseProductModel;
    }

    private Pair<List<ProductItem>, PagingHandler.PagingHandlerModel> parseBrowseProductModel(BrowseProductModel browseProductModel) {
        List<ProductItem> productItems = BrowseProductModel.Products.toProductItemList(browseProductModel.result.products);

        PagingHandler.PagingHandlerModel pagingHandlerModel = getPagingHandlerModel(browseProductModel.result.paging.getUriNext(), browseProductModel.result.paging.getUriPrevious());

        return new Pair<>(productItems, pagingHandlerModel);
    }

    @NonNull
    public static PagingHandler.PagingHandlerModel getPagingHandlerModel(String uriNext, String uriPrevious) {
        String start = "-1";
        try {
            String temp = PagingHandlerUtil.getCertainKeyword(uriNext, "start");
            if (temp != null) {
                start = temp;
            }
        } catch (UnsupportedEncodingException | MalformedURLException e) {
            e.printStackTrace();
            Log.e("STUART", "error parse" + e.getMessage());
        }
        PagingHandler.PagingHandlerModel pagingHandlerModel = new PagingHandler.PagingHandlerModel();
        pagingHandlerModel.setStartIndex(Integer.parseInt(start));
        pagingHandlerModel.setUriNext(uriNext);
        pagingHandlerModel.setUriPrevious(uriPrevious);
        return pagingHandlerModel;
    }

    public void processBrowseProduct(final List<ProductItem> productItems,
                                     final PagingHandler.PagingHandlerModel pagingHandlerModel) {

        if (browseProductModel.getCategoryData() != null) {
            view.addCategoryHeader(browseProductModel.getCategoryData());
        }

        if (TextUtils.isEmpty(view.getUserId()) || productItems.isEmpty()) {
            view.onCallProductServiceResult(productItems, pagingHandlerModel);
            return;
        }

        Log.d(TAG, "getProduct2 startMojito");

        discoveryInteractor.checkProductsInWishlist(view.getUserId(), productItems)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, Boolean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Map<String, Boolean> checkResultMap) {
                        Log.d(TAG, "getProduct2 finishMojito");
                        for (ProductItem item : productItems) {
                            if (checkResultMap.get(item.getId()) != null) {
                                item.setProductAlreadyWishlist(true);
                            } else {
                                item.setProductAlreadyWishlist(false);
                            }
                        }
                        view.onCallProductServiceResult(productItems, pagingHandlerModel);
                    }
                });
    }

    public void processBrowseProductLoadMore(final List<ProductItem> productItems,
                                             final PagingHandler.PagingHandlerModel pagingHandlerModel) {

        if (TextUtils.isEmpty(view.getUserId()) || productItems.isEmpty()) {
            view.onCallProductServiceLoadMore(productItems, pagingHandlerModel);
            return;
        }

        Log.d(TAG, "getProduct2 startMojito");

        discoveryInteractor.checkProductsInWishlist(view.getUserId(), productItems)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, Boolean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Map<String, Boolean> checkResultMap) {
                        Log.d(TAG, "getProduct2 finishMojito");
                        for (ProductItem item : productItems) {
                            if (checkResultMap.get(item.getId()) != null) {
                                item.setProductAlreadyWishlist(true);
                            } else {
                                item.setProductAlreadyWishlist(false);
                            }
                        }
                        view.onCallProductServiceLoadMore(productItems, pagingHandlerModel);
                    }
                });
    }

    @Override
    public void fetchArguments(Bundle argument) {

    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {
        view.restorePaging(argument);
    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {
        view.savePaging(argument);
    }

    @Override
    public void initDataInstance(Context context) {
        view.setupAdapter();
        retrofitInteractor = new RetrofitInteractorImpl();
        cacheInteractor = new CacheInteractorImpl();
        discoveryInteractor = new DiscoveryInteractorImpl();
        discoveryInteractor.setDiscoveryListener(this);
    }

    @Override
    public void onComplete(int type, Pair<String, ? extends ObjContainer> data) {

    }

    @Override
    public void onFailed(int type, Pair<String, ? extends ObjContainer> data) {
        Log.e(TAG, "onFailed " + data.toString() + " type " + type);
        switch (type) {
            case DiscoveryListener.TOPADS:
                break;
            default:
                view.ariseRetry(type, ((ErrorContainer) data.getModel2()).body().getMessage());
                break;
        }
    }

    //berantakan banget, tiap case dibikin fungsi masing masing biar lebih rapi lah -rico-

    @Override
    public void onSuccess(int type, Pair<String, ? extends ObjContainer> data) {
        switch (type) {
            case DiscoveryListener.BROWSE_PRODUCT:
                Context context = this.context.get();
                if (context != null && context instanceof BrowseView) {
                    BrowseProductModel.BrowseProductContainer temp = (BrowseProductModel.BrowseProductContainer) data.getModel2();
                    browseProductModel = temp.body();
                    Pair<List<ProductItem>, PagingHandler.PagingHandlerModel> listPagingHandlerModelPair = parseBrowseProductModel(browseProductModel);
                    processBrowseProductLoadMore(listPagingHandlerModelPair.getModel1(), listPagingHandlerModelPair.getModel2());
                }
                break;
            case DiscoveryListener.BROWSE_SHOP:
                BrowseShopModel.BrowseShopContainer browseShopContainer = (BrowseShopModel.BrowseShopContainer) data.getModel2();
                BrowseShopModel browseShopModel = browseShopContainer.body();

                Log.d(TAG, getMessageTAG() + browseShopModel);
                if (browseShopModel == null)
                    return;

                List<ProductModel> result = new ArrayList<>();
                for (BrowseShopModel.Shops temp : browseShopModel.result.shops) {
                    ProductModel productModel = new ProductModel();
                    productModel.productImage = temp.shopImage;
                    productModel.productName = temp.shopName;
                    productModel.productPrice = temp.shopId;
                    productModel.shopName = temp.shopName;

                    // add to collection
                    result.add(productModel);
                }

                break;
            case DiscoveryListener.BROWSE_CATALOG:

                break;
            default:

                break;
        }
    }

}
