package com.tokopedia.seller.myproduct.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.network.apiservices.product.ProductService;
import com.tokopedia.core.network.apiservices.shop.ShopService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.myproduct.model.EditPriceParam;
import com.tokopedia.seller.selling.network.apiservices.ProductActService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * Created by noiz354 on 5/18/16.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class NetworkInteractorImpl implements NetworkInteractor {
    CompositeSubscription compositeSubscription;

    private FetchEtalase fetchEtalase;

    //LISTENER FROM MANAGE PRODUCT
    private GetProductList getProductList;
    private ChangeCategories changeCategories;
    private ChangeInsurance changeInsurance;
    private DeleteProduct deleteProduct;
    private EditPrice editPrice;
    private EditEtalase editEtalase;

    private ShopService shopService;
    private ProductService productService;
    private ProductActService productActService;


    public NetworkInteractorImpl() {
        shopService = new ShopService();
        productService = new ProductService();
        productActService = new ProductActService();
    }

    public CompositeSubscription getCompositeSubscription() {
        return compositeSubscription;
    }

    public void setCompositeSubscription(CompositeSubscription compositeSubscription) {
        this.compositeSubscription = compositeSubscription;
    }

    public void setFetchEtalase(FetchEtalase fetchEtalase) {
        this.fetchEtalase = fetchEtalase;
    }


    public void setDeleteProduct(DeleteProduct deleteProduct) {
        this.deleteProduct = deleteProduct;
    }


    public void setChangeCategories(ChangeCategories changeCategories) {
        this.changeCategories = changeCategories;
    }

    public void setEditEtalase(EditEtalase editEtalase) {
        this.editEtalase = editEtalase;
    }

    public void setChangeInsurance(ChangeInsurance changeInsurance) {
        this.changeInsurance = changeInsurance;
    }

    public void setGetProductList(GetProductList getProductList) {
        this.getProductList = getProductList;
    }

    public void setEditPrice(EditPrice editPrice) {
        this.editPrice = editPrice;
    }


    private HashMap<String, String> generateFetchEtalaseParam(Context context) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("shop_id", SessionHandler.getShopID(context));
        param.put("show_empty", "1");// show all etalase
        return param;
    }

    @Override
    public void fetchEtalase(Context context) {

        if (!checkNotNull(fetchEtalase))
            return;

        compositeSubscription.add(shopService.getApi().getEtalase(AuthUtil.generateParams(context, generateFetchEtalaseParam(context)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                fetchEtalase.onFailureFetchEtalase(e);
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                fetchEtalase.onSuccessFetchEtalase(responseData);
                            }
                        }
                ));
    }

    // FOR MANAGE PRODUCT

    private static Map<String, String> generateManageProductParam(
            String sort, String keyword, String page,
            String etalase_id, String catalog_id, String department_id,
            String picture_status, String condition
    ) {
        HashMap<String, String> param = new HashMap<>();

        param.put("per_page", "8");
        param.put("sort", sort);
        param.put("keyword", keyword);
        param.put("page", page);

        if (etalase_id.length() > 0)
            param.put("etalase_id", etalase_id);
        if (catalog_id.length() > 0)
            param.put("catalog_id", catalog_id);
        if (department_id.length() > 0)
            param.put("department_id", department_id);
        if (picture_status.length() > 0)
            param.put("picture_status", picture_status);
        if (condition.length() > 0)
            param.put("condition", condition);
        return param;
    }

    @Override
    public void getProductList(Context context,
                               String sort, String keyword, String page,
                               String etalase_id, String catalog_id, String department_id,
                               String picture_status, String condition
    ) {
        if (!checkNotNull(getProductList))
            return;

        compositeSubscription.add(productService.getApi()
                .manage(AuthUtil.generateParams(context, generateManageProductParam(
                        sort, keyword, page,
                        etalase_id, catalog_id, department_id,
                        picture_status, condition
                )))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                getProductList.onFailureGetProductList(e);
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                if (responseData.body().isError()) {
                                    getProductList.onFailureGetProductList(new Throwable(responseData.body().getErrorMessages().get(0)));
                                } else {
                                    getProductList.onSuccessGetProductList(responseData);
                                }
                            }
                        }
                ));
    }

    private static Map<String, String> generateEditEtalaseParam(String ID, String etalaseID,
                                                                String EtalaseName, int addTo) {
        HashMap<String, String> param = new HashMap<>();
        param.put("product_id", ID);
        param.put("product_etalase_id", etalaseID);
        param.put("product_upload_to", addTo + "");
        if (!EtalaseName.isEmpty()) {
            param.put("product_etalase_name", EtalaseName);
        }
        return param;
    }

    private static Map<String, String> generateEditCategoryParam(String shopID, String ID, String CtgID) {
        HashMap<String, String> param = new HashMap<>();
        param.put("department_id", CtgID);
        param.put("product_id", ID);
        param.put("shop_id", shopID);
        return param;
    }

    @Override
    public void changeCategories(Context context,
                                 final String CtgID, final String ID, final String shopID) {
        if (!checkNotNull(changeCategories))
            return;

        compositeSubscription.add(productActService.getApi()
                .editCategory(AuthUtil.generateParams(context, generateEditCategoryParam(shopID, ID, CtgID)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                changeCategories.onFailureChangeCategories(e, CtgID, ID, shopID);
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                if (responseData.body().isError()) {
                                    changeCategories.onFailureChangeCategories(new Throwable(responseData.body().getErrorMessages().get(0)), CtgID, ID, shopID);
                                } else {
                                    changeCategories.onSuccessChangeCategories(responseData);
                                }
                            }

                        }
                ));

    }

    private static Map<String, String> generateEditInsuranceParam(String insuranceID, String ID) {
        HashMap<String, String> param = new HashMap<>();
        param.put("product_id", ID);
        param.put("product_must_insurance", insuranceID);
        return param;
    }

    @Override
    public void changeInsurance(Context context, final String insuranceID, final String ID) {
        if (!checkNotNull(changeInsurance))
            return;

        compositeSubscription.add(productActService.getApi()
                .editInsurance(AuthUtil.generateParams(context, generateEditInsuranceParam(insuranceID, ID)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                changeInsurance.onFailureChangeInsurance(e, insuranceID, ID);
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                if (responseData.body().isError()) {
                                    changeInsurance.onFailureChangeInsurance(new Throwable(responseData.body().getErrorMessages().get(0)), insuranceID, ID);
                                } else {
                                    changeInsurance.onSuccessChangeInsurance(responseData);
                                }
                            }
                        }
                ));
    }

    private static Map<String, String> generateDeleteParam(String ID) {
        HashMap<String, String> param = new HashMap<>();
        param.put("product_id", ID);
        return param;
    }

    @Override
    public void deleteProduct(Context context, final String ID) {
        if (!checkNotNull(deleteProduct))
            return;

        compositeSubscription.add(productActService.getApi()
                .delete(AuthUtil.generateParams(context, generateDeleteParam(ID)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                deleteProduct.onFailureDeleteProduct(e, ID);
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                if (responseData.body().isError()) {
                                    deleteProduct.onFailureDeleteProduct(new Throwable(responseData.body().getErrorMessages().get(0)), ID);
                                } else {
                                    deleteProduct.onSuccessDeleteProduct(responseData);
                                }
                            }
                        }
                ));
    }

    @Override
    public void editPrice(Context context, final EditPriceParam param) {
        if (!checkNotNull(editPrice))
            return;

        compositeSubscription.add(productActService.getApi()
                .editPrice(AuthUtil.generateParams(context, param.generateEditPriceParam()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                editPrice.onFailureEditPrice(e.getLocalizedMessage(), param);
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                if (responseData.body().isError()) {
                                    editPrice.onFailureEditPrice(responseData.body().getErrorMessages().get(0), param);
                                } else {
                                    editPrice.onSuccessEditPrice(responseData);
                                }

                            }
                        }
                ));

    }

    @Override
    public void editEtalase(Context context, final String productId, final String etalaseID, final String etalaseName, final int addTo) {
        if (!checkNotNull(editEtalase)) {
            return;
        }
        compositeSubscription.add(productActService.getApi()
                .editEtalase(AuthUtil.generateParams(context, generateEditEtalaseParam(productId, etalaseID, etalaseName, addTo)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                editEtalase.onFailureEditEtalase(e, etalaseID, productId, etalaseName, addTo);
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                if (responseData.body().isError()) {
                                    editEtalase.onFailureEditEtalase(new Throwable(responseData.body().getErrorMessages().get(0)), etalaseID, productId, etalaseName, addTo);
                                } else {
                                    editEtalase.onSuccessEditEtalase(responseData);
                                }
                            }
                        }
                ));
    }

    public interface FetchEtalase {
        void onFailureFetchEtalase(Throwable e);

        void onSuccessFetchEtalase(Response<TkpdResponse> departmentParentModel);
    }

    // FROM MANAGE PRODUCT
    public interface GetProductList {
        void onFailureGetProductList(Throwable e);

        void onSuccessGetProductList(Response<TkpdResponse> departmentParentModel);
    }

    public interface ChangeCategories {
        void onFailureChangeCategories(Throwable e, final String CtgID, final String ID, final String shopID);

        void onSuccessChangeCategories(Response<TkpdResponse> departmentParentModel);
    }

    public interface ChangeInsurance {
        void onFailureChangeInsurance(Throwable e, final String insuranceID, final String ID);

        void onSuccessChangeInsurance(Response<TkpdResponse> departmentParentModel);
    }

    public interface DeleteProduct {
        void onFailureDeleteProduct(Throwable e, String ID);

        void onSuccessDeleteProduct(Response<TkpdResponse> departmentParentModel);
    }

    public interface EditPrice {
        void onFailureEditPrice(String e, EditPriceParam param);

        void onSuccessEditPrice(Response<TkpdResponse> departmentParentModel);
    }

    public interface EditEtalase {
        void onFailureEditEtalase(Throwable e, String productId, String etalaseId, String etalaseName, int addTo);

        void onSuccessEditEtalase(Response<TkpdResponse> departmentParentModel);
    }


}
