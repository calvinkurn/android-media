package com.tokopedia.seller.myproduct.presenter;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.myproduct.api.Department;
import com.tokopedia.core.myproduct.model.CatalogDataModel;
import com.tokopedia.core.myproduct.model.DepartmentParentModel;
import com.tokopedia.core.myproduct.model.ImageModel;
import com.tokopedia.core.myproduct.model.MyShopInfoModel;
import com.tokopedia.seller.myproduct.fragment.AddProductFragment;
import com.tokopedia.seller.myproduct.model.EditPriceParam;
import com.tokopedia.seller.myproduct.model.GetShopNoteModel;
import com.tokopedia.seller.myproduct.model.editProductForm.EditProductForm;
import com.tokopedia.seller.myproduct.utils.UploadPhotoTask;
import com.tokopedia.core.network.apiservices.ace.AceSearchService;
import com.tokopedia.core.network.apiservices.etc.DepartmentService;
import com.tokopedia.core.network.apiservices.product.ProductService;
import com.tokopedia.core.network.apiservices.shop.MyShopNoteService;
import com.tokopedia.core.network.apiservices.shop.ShopService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.product.interactor.RetrofitInteractorImpl;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.selling.network.apiservices.ProductActService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tkpd.library.utils.CommonUtils.checkErrorMessageEmpty;
import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * Created by noiz354 on 5/18/16.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class NetworkInteractorImpl implements NetworkInteractor {
    public static final int MOVE_TO_WAREHOUSE = 0;
    CompositeSubscription compositeSubscription;
    private FetchDepartment fetchDepartment;

    private FetchEtalase fetchEtalase;

    private FetchDepartmentChild fetchDepartmentChild;
    private FetchCatalog fetchCatalog;
    private FetchEditData fetchEditData;
    //LISTENER FROM MANAGE PRODUCT
    private GetProductList getProductList;
    private ChangeCategories changeCategories;
    private ChangeInsurance changeInsurance;
    private DeleteProduct deleteProduct;
    private EditPrice editPrice;
    private EditEtalase editEtalase;

    private MyShopNoteService myShopNoteService;
    private ShopService shopService;
    private ProductService productService;
    private ProductActService productActService;
    private RetrofitInteractorImpl retrofitInteractorImpl;
    private DepartmentService departmentService;


    public NetworkInteractorImpl() {
        shopService = new ShopService();
        myShopNoteService = new MyShopNoteService();
        productService = new ProductService();
        productActService = new ProductActService();
        retrofitInteractorImpl = new RetrofitInteractorImpl();
        departmentService = new DepartmentService();
    }

    public FetchEditData getFetchEditData() {
        return fetchEditData;
    }

    public void setFetchEditData(FetchEditData fetchEditData) {
        this.fetchEditData = fetchEditData;
    }

    public CompositeSubscription getCompositeSubscription() {
        return compositeSubscription;
    }

    public void setCompositeSubscription(CompositeSubscription compositeSubscription) {
        this.compositeSubscription = compositeSubscription;
    }

    public FetchEtalase getFetchEtalase() {
        return fetchEtalase;
    }

    public void setFetchEtalase(FetchEtalase fetchEtalase) {
        this.fetchEtalase = fetchEtalase;
    }

    public FetchDepartment getFetchDepartment() {
        return fetchDepartment;
    }

    public void setFetchDepartment(FetchDepartment fetchDepartment) {
        this.fetchDepartment = fetchDepartment;
    }

    public FetchDepartmentChild getFetchDepartmentChild() {
        return fetchDepartmentChild;
    }

    public void setFetchDepartmentChild(FetchDepartmentChild fetchDepartmentChild) {
        this.fetchDepartmentChild = fetchDepartmentChild;
    }

    public FetchCatalog getFetchCatalog() {
        return fetchCatalog;
    }

    public void setFetchCatalog(FetchCatalog fetchCatalog) {
        this.fetchCatalog = fetchCatalog;
    }


    public DeleteProduct getDeleteProduct() {
        return deleteProduct;
    }

    public void setDeleteProduct(DeleteProduct deleteProduct) {
        this.deleteProduct = deleteProduct;
    }

    public ChangeCategories getChangeCategories() {
        return changeCategories;
    }

    public void setChangeCategories(ChangeCategories changeCategories) {
        this.changeCategories = changeCategories;
    }

    public void setEditEtalase(EditEtalase editEtalase) {
        this.editEtalase = editEtalase;
    }

    public ChangeInsurance getChangeInsurance() {
        return changeInsurance;
    }

    public void setChangeInsurance(ChangeInsurance changeInsurance) {
        this.changeInsurance = changeInsurance;
    }

    public GetProductList getGetProductList() {
        return getProductList;
    }

    public void setGetProductList(GetProductList getProductList) {
        this.getProductList = getProductList;
    }

    public EditPrice getEditPrice() {
        return editPrice;
    }

    public void setEditPrice(EditPrice editPrice) {
        this.editPrice = editPrice;
    }

    @Override
    public void fetchDepartment(Context context) {

        Observable<Response<TkpdResponse>> parent = departmentService.getApi().getParent(AuthUtil.generateParams(context, new HashMap<String, String>()));
        Observable<DepartmentParentModel> map = parent.map(new Func1<Response<TkpdResponse>, DepartmentParentModel>() {
            @Override
            public DepartmentParentModel call(Response<TkpdResponse> response) {
                TkpdResponse body = response.body();
                if (response.isSuccessful()) {
                    if (!checkErrorMessageEmpty(body.getErrorMessages().toString())) {
                        throw new RuntimeException(body.getErrorMessages().toString());
                    }
                } else {
                    new RetrofitUtils.DefaultErrorHandler(response.code());
                }

                String strResponse = response.body().getStrResponse();
                DepartmentParentModel departmentParentModel = null;
                if (checkNotNull(strResponse) && !strResponse.isEmpty()) {
                    departmentParentModel = new GsonBuilder().create().fromJson(strResponse, DepartmentParentModel.class);
                }
                return departmentParentModel;
            }
        });
        compositeSubscription.add(map.retry(RetrofitUtils.RETRY_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<DepartmentParentModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (!checkNotNull(fetchDepartment))
                                    return;

//                                if(BuildConfig.DEBUG){
//                                    e = new Throwable("fetchDepartment ["+e.getMessage()+"]");
//                                }

                                fetchDepartment.onFailureFetchDepartment(e);
                            }

                            @Override
                            public void onNext(DepartmentParentModel departmentParentModel) {
                                if (!checkNotNull(departmentParentModel) && !checkNotNull(fetchDepartment))
                                    return;

                                fetchDepartment.onSuccessFetchDepartment(departmentParentModel);
                            }
                        }
                ));
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
                                Log.d(TAG, messageTAG + " fetchEtalase onCompleted()");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, messageTAG + " fetchEtalase " + e.getLocalizedMessage());
//                                if(BuildConfig.DEBUG){
//                                    e = new Throwable("fetchEtalase ["+e.getMessage()+"]");
//                                }
                                fetchEtalase.onFailureFetchEtalase(e);
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                fetchEtalase.onSuccessFetchEtalase(responseData);
                            }
                        }
                ));
    }

    private HashMap<String, String> generateParamReturnPolicyDetail(
            MyShopInfoModel.Info info, GetShopNoteModel.ShopNoteModel returnPolicy
    ) {
        HashMap<String, String> param = new HashMap<>();
        param.put("shop_id", info.getShop_id());
        param.put("shop_domain", info.getShop_domain());
        param.put("terms", "0");
        param.put("note_id", returnPolicy.getNoteId());
        return param;
    }

    @Override
    public void fetchDepartmentChild(Context context, final int depId, final int level) {
        if (!checkNotNull(fetchDepartmentChild))
            return;

        Map<String, String> param = new HashMap<>();
        param.put(Department.DEPARTMENT_ID, depId + "");
        Observable<Response<TkpdResponse>> child = departmentService.getApi().getChild(AuthUtil.generateParams(context, param));
        Observable<DepartmentParentModel> map = child.map(new Func1<Response<TkpdResponse>, DepartmentParentModel>() {
            @Override
            public DepartmentParentModel call(Response<TkpdResponse> response) {
                TkpdResponse body = response.body();
                if (response.isSuccessful()) {
                    if (!checkErrorMessageEmpty(body.getErrorMessages().toString())) {
                        throw new RuntimeException(body.getErrorMessages().toString());
                    }
                } else {
                    new RetrofitUtils.DefaultErrorHandler(response.code());
                }

                String strResponse = response.body().getStrResponse();
                DepartmentParentModel departmentParentModel = null;
                if (checkNotNull(strResponse) && !strResponse.isEmpty()) {
                    departmentParentModel = new GsonBuilder().create().fromJson(strResponse, DepartmentParentModel.class);
                }
                return departmentParentModel;
            }
        });
        compositeSubscription.add(
                map.retry(RetrofitUtils.RETRY_COUNT)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                new Subscriber<DepartmentParentModel>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
//                                        if(BuildConfig.DEBUG){
//                                            e = new Throwable("fetchDepartmentChild ["+e.getMessage()+"]");
//                                        }
                                        fetchDepartmentChild.onFailureFetchDepartmentChild(e);
                                    }

                                    @Override
                                    public void onNext(DepartmentParentModel departmentParentModel) {
                                        fetchDepartmentChild.onSuccessFetchDepartmentChild(departmentParentModel, depId, level);
                                    }
                                }
                        ));

    }

    @Override
    public void fetchCatalog(Context context, final String productDepId, final String productName) {
        if (!checkNotNull(fetchCatalog))
            return;
        Map<String, String> param = new HashMap<>();
        param.put("sc", productDepId);
        param.put("q", productName);
        param.put("source", "search_catalog");

        compositeSubscription.add(
                new AceSearchService().getApi().getCatalog(
                        AuthUtil.generateParams(context, param)
                ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                new Subscriber<CatalogDataModel>() {
                                    @Override
                                    public void onCompleted() {
                                        Log.d(TAG, messageTAG + "onCompleted() !!");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (e != null) {
                                            Log.e(TAG, messageTAG + e.getLocalizedMessage());
                                        }

                                        fetchCatalog.onFailureFetchCatalog(e);
                                    }

                                    @Override
                                    public void onNext(CatalogDataModel catalogDataModel) {
                                        DbManagerImpl.getInstance().saveCatalog(catalogDataModel, productDepId, productName);
                                        fetchCatalog.onSuccessFetchCatalog(catalogDataModel);
                                    }
                                }
                        ));

    }

    @Override
    public void editProductDetail(CompositeSubscription compositeSubscription, final Context context, final String productId, String productName, String shopDomain) {

        if (!checkNotNull(fetchEditData))
            return;

        Map<String, Object> result = new HashMap<String, Object>();
        Observable<Map<String, Object>> resultObservable = Observable.just(result)
                .flatMap(new Func1<Map<String, Object>, Observable<Map<String, Object>>>() {
                    @Override
                    public Observable<Map<String, Object>> call(Map<String, Object> stringObjectMap) {
                        Observable<EditProductForm> editProductFormObservable = getEditProductForm(context, "300", productId, SessionHandler.getShopID(context), SessionHandler.getLoginID(context));
                        return Observable.zip(Observable.just(stringObjectMap), editProductFormObservable, new Func2<Map<String, Object>, EditProductForm, Map<String, Object>>() {
                            @Override
                            public Map<String, Object> call(Map<String, Object> stringObjectMap, EditProductForm editProductForm) {
                                stringObjectMap.put(EDIT_PRODUCT_FORM, editProductForm);
                                return stringObjectMap;
                            }
                        });
                    }
                })
                .flatMap(new Func1<Map<String, Object>, Observable<Map<String, Object>>>() {
                    @Override
                    public Observable<Map<String, Object>> call(Map<String, Object> stringObjectMap) {
                        EditProductForm editProductForm1 = (EditProductForm) stringObjectMap.get(EDIT_PRODUCT_FORM);
                        List<EditProductForm.ProductImage> images = new ArrayList<EditProductForm.ProductImage>();
                        for (EditProductForm.ProductImage image : editProductForm1.getData().getProductImages()){
                            if (!image.getImageSrc().isEmpty() && !image.getImageSrc().equals("0")){
                                images.add(image);
                            }
                        }
                        Observable<List<ImageModel>> imageDownload = Observable.from(images)
//                                .filter(new Func1<EditProductForm.ProductImage, Boolean>() {
//                                    @Override
//                                    public Boolean call(EditProductForm.ProductImage productImage) {
//                                        return !productImage.getImageSrc().isEmpty() && !productImage.getImageSrc().equals("0");
//                                    }
//                                })
                                .flatMap(new Func1<EditProductForm.ProductImage, Observable<ImageModel>>() {
                                    @Override
                                    public Observable<ImageModel> call(EditProductForm.ProductImage productImage) {
                                        FutureTarget<File> future = Glide.with(context)
                                                .load(productImage.getImageSrc())
                                                .downloadOnly(4096, 2160);
                                        File photo = null;
                                        try {
                                            File cacheFile = future.get();
                                            photo = UploadPhotoTask.writeImageToTkpdPath(cacheFile);
                                            Log.d(TAG, messageTAG + "path -> " + (photo != null ? photo.getAbsolutePath() : "kosong"));
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                            throw new RuntimeException(e.getMessage());
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                            throw new RuntimeException(e.getMessage());
                                        }
                                        ImageModel photoModel = AddProductFragment.getImageModel(photo.getAbsolutePath(), photo, productImage);
                                        return Observable.just(photoModel);
                                    }
                                }).toList();
                        return Observable.zip(imageDownload, Observable.just(stringObjectMap), new Func2<List<ImageModel>, Map<String, Object>, Map<String, Object>>() {
                            @Override
                            public Map<String, Object> call(List<ImageModel> imageModels, Map<String, Object> stringObjectMap) {
                                stringObjectMap.put(IMAGE_MODEL_DOWNLOADS, imageModels);
                                return stringObjectMap;
                            }
                        });
                    }
                })
                .flatMap(new Func1<Map<String, Object>, Observable<Map<String, Object>>>() {
                    @Override
                    public Observable<Map<String, Object>> call(Map<String, Object> stringObjectMap) {
                        EditProductForm editProductForm = (EditProductForm) stringObjectMap.get(EDIT_PRODUCT_FORM);
                        final Observable<List<DepartmentParentModel>> departmentData = Observable.from(editProductForm.getData().getBreadcrumb())
                                .flatMap(new Func1<EditProductForm.Breadcrumb, Observable<DepartmentParentModel>>() {
                                    @Override
                                    public Observable<DepartmentParentModel> call(EditProductForm.Breadcrumb productBreadcrumb) {
                                        final String departmentId = productBreadcrumb.getDepartmentId();
                                        final NetworkCalculator depChild = new NetworkCalculator(
                                                NetworkConfig.GET,
                                                SessionHandler.getLoginID(context),
                                                GCMHandler.getRegistrationId(context),
                                                TkpdBaseURL.Etc.URL_DEPARTMENT + "/" + TkpdBaseURL.Etc.PATH_GET_DEPARTMENT_CHILD)
                                                .setIdentity()
                                                .addParam(Department.DEPARTMENT_ID, departmentId + "")
                                                .compileAllParam()
                                                .finish();


                                        Map<String, String> param = new HashMap<String, String>();
                                        param.put("department_id", depChild.getContent().get(Department.DEPARTMENT_ID));

                                        Observable<DepartmentParentModel> child = new DepartmentService()
                                                .getApi()
                                                .getChild2(AuthUtil.generateParams(context, param));
                                        return child;
                                    }
                                }).toList();
                        return Observable.zip(departmentData, Observable.just(stringObjectMap), new Func2<List<DepartmentParentModel>, Map<String, Object>, Map<String, Object>>() {
                            @Override
                            public Map<String, Object> call(List<DepartmentParentModel> departmentParentModels, Map<String, Object> stringObjectMap) {
                                stringObjectMap.put(DEPARTMENT_DATA, departmentParentModels);
                                return stringObjectMap;
                            }
                        });
                    }
                })
                .flatMap(new Func1<Map<String, Object>, Observable<Map<String, Object>>>() {
                    @Override
                    public Observable<Map<String, Object>> call(Map<String, Object> stringObjectMap) {
                        EditProductForm editProductForm = (EditProductForm) stringObjectMap.get(EDIT_PRODUCT_FORM);
                        int lastIndex = editProductForm.getData().getBreadcrumb().size() - 1;
                        EditProductForm.Breadcrumb productBreadcrumb = editProductForm.getData().getBreadcrumb().get(lastIndex);

                        Map<String, String> param = new HashMap<>();
                        param.put("sc", productBreadcrumb.getDepartmentId());
                        param.put("q", editProductForm.getData().getProduct().getProductName());
                        param.put("source", "search_catalog");
                        Observable<CatalogDataModel> catalog = new AceSearchService().getApi().getCatalog(
                                AuthUtil.generateParams(context, param));
                        return Observable.zip(catalog, Observable.just(stringObjectMap), new Func2<CatalogDataModel, Map<String, Object>, Map<String, Object>>() {
                            @Override
                            public Map<String, Object> call(CatalogDataModel response, Map<String, Object> stringObjectMap) {
                                stringObjectMap.put(CATALOG_MODEL_EDIT, response);
                                return stringObjectMap;
                            }
                        });
                    }
                });

        compositeSubscription.add(resultObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<Map<String, Object>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, messageTAG + e);
                                fetchEditData.onFailureFetchEditData(e);
                            }

                            @Override
                            public void onNext(Map<String, Object> stringObjectMap) {
                                Log.d(TAG, messageTAG + stringObjectMap);
                                List<DepartmentParentModel> dep = (List<DepartmentParentModel>) stringObjectMap.get(DEPARTMENT_DATA);

                                EditProductForm editProductForm = (EditProductForm) stringObjectMap.get(EDIT_PRODUCT_FORM);

                                if (editProductForm.getData().getBreadcrumb().size() == 3) {
                                    saveKategoriToDb(dep, editProductForm, 1, 3);
                                } else if (editProductForm.getData().getBreadcrumb().size() == 2) {
                                    saveKategoriToDb(dep, editProductForm, 0, 1);
                                } else {
                                    saveKategoriToDb(dep, editProductForm, -1, -1);
                                }
                                fetchEditData.onSuccessFetchEditData(stringObjectMap);
                            }
                        }
                ));
    }

    public Observable<EditProductForm> getEditProductForm(Context context, String imageSize, String productId, String shopId, String userId) {
        HashMap<String, String> param = new HashMap<>();
        param.put("image_size", imageSize);
        param.put("product_id", productId);
        param.put("shop_id", shopId);
        param.put("user_id", userId);
        return productService.getApi().getEditForm(AuthUtil.generateParams(context, param))
                .flatMap(new Func1<Response<TkpdResponse>, Observable<EditProductForm>>() {
                    @Override
                    public Observable<EditProductForm> call(Response<TkpdResponse> response) {
                        Log.d(TAG, response.toString());
                        if (response.isSuccessful()) {
                            if (checkErrorMessageEmpty(response.body().getErrorMessages().toString())) {
                                EditProductForm editProductForm = new GsonBuilder().create().fromJson(response.body().getStrResponse(), EditProductForm.class);
                                return Observable.just(editProductForm);
                            } else {
                                if (response.body().isNullData()) {
                                    throw new RuntimeException("product detail is null ");
                                } else {
                                    throw new RuntimeException(response.body().getErrorMessages().toString());
                                }
                            }
                        }
                        return null;
                    }
                });
    }

    private void saveKategoriToDb(List<DepartmentParentModel> dep, EditProductForm productDetailData, int startIndex, int limit) {
        int count = startIndex;
        List<EditProductForm.Breadcrumb> breadcrumb = productDetailData.getData().getBreadcrumb();
        for (DepartmentParentModel departmentParentModel : dep) {
            if (count == limit)
                break;

            String departmentId = breadcrumb.get(count).getDepartmentId();

            DepartmentParentModel.DepartmentParent[] list = departmentParentModel.getData().getList();
            DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
            database.beginTransaction();
            try {
                for (int i = 0; i < list.length; i++) {
                    DepartmentParentModel.DepartmentParent parent = list[i];
                    int departmentTree = Integer.parseInt(parent.getDepartmentTree());
                    int parentId = Integer.parseInt(departmentId);
                    int childId = Integer.parseInt(parent.getDepartmentId());
                    String childIdentifier = parent.getDepartmentIdentifier();
                    CategoryDB categoryDB = DbManagerImpl.getInstance().getCategoryDb(childIdentifier);
                    if(categoryDB == null) {
                        categoryDB = new CategoryDB(
                                parent.getDepartmentName(), // product name
                                departmentTree, // department tree
                                0,
                                parentId,  // parent id
                                childId, // child department id
                                childIdentifier// child identifier
                        );
                        Log.d(TAG, messageTAG + " before save kategori : " + categoryDB);
                        categoryDB.save();
                        Long save = categoryDB.getId();
                        Log.d(TAG, messageTAG + " db Id Kategori : " + save);
                    }else{
                        Log.d(TAG, messageTAG + " db Id Kategori already saved : " + categoryDB.getId());
                    }
                }
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }

            count++;
        }
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

    private static Map<String, String> generateEditReturnableParam(String returnableCondition, String ID) {
        HashMap<String, String> param = new HashMap<>();
        param.put("product_id", ID);
        param.put("product_returnable", returnableCondition);
        return param;
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

    private boolean checkIsValidPrice(String price) {
        return price.length() > 0;
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


    public static class ManageProductResult {
        public boolean isSuccess;
        public String temp;
    }


    // LISTENER HERE
    public interface FetchDepartment {
        void onFailureFetchDepartment(Throwable e);

        void onSuccessFetchDepartment(DepartmentParentModel departmentParentModel);
    }

    public interface FetchEtalase {
        void onFailureFetchEtalase(Throwable e);

        void onSuccessFetchEtalase(Response<TkpdResponse> departmentParentModel);
    }

    public interface FetchDepartmentChild {
        void onFailureFetchDepartmentChild(Throwable e);

        void onSuccessFetchDepartmentChild(DepartmentParentModel departmentParentModel, int depId, int level);
    }

    public interface FetchCatalog {
        void onFailureFetchCatalog(Throwable e);

        void onSuccessFetchCatalog(CatalogDataModel catalogDataModel);
    }

    public interface FetchEditData {
        void onFailureFetchEditData(Throwable e);

        void onSuccessFetchEditData(Map<String, Object> catalogDataModel);
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
