package com.tokopedia.seller.myproduct.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.database.manager.DbManager;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.CategoryDB_Table;
import com.tokopedia.core.database.model.CurrencyDB;
import com.tokopedia.core.database.model.EtalaseDB;
import com.tokopedia.core.database.model.PictureDB;
import com.tokopedia.core.database.model.ProductDB;
import com.tokopedia.core.etalase.EtalaseVariable;
import com.tokopedia.seller.myproduct.fragment.AddProductFragment;
import com.tokopedia.core.myproduct.model.CatalogDataModel;
import com.tokopedia.core.myproduct.model.DepartmentParentModel;
import com.tokopedia.seller.myproduct.model.EtalaseModel;
import com.tokopedia.core.myproduct.model.GetEtalaseModel;
import com.tokopedia.core.myproduct.model.ImageModel;
import com.tokopedia.seller.myproduct.model.SimpleTextModel;
import com.tokopedia.core.myproduct.model.TextDeleteModel;
import com.tokopedia.core.myproduct.model.WholeSaleAdapterModel;
import com.tokopedia.seller.myproduct.model.editProductForm.EditProductForm;
import com.tokopedia.seller.myproduct.utils.ProductEditHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.product.model.productdetail.ProductPreOrder;
import com.tokopedia.core.product.model.productdetail.ProductWholesalePrice;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.MethodChecker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;
import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * Created by noiz354 on 5/18/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class AddProductPresenterImpl implements AddProductPresenter
        , NetworkInteractorImpl.FetchDepartment
        , NetworkInteractorImpl.FetchEtalase
        , ErrorListener
        , NetworkInteractorImpl.FetchDepartmentChild
        , NetworkInteractorImpl.FetchCatalog
        , NetworkInteractorImpl.FetchEditData {

    public static final int ETALASE_GUDANG_INDEX = 0;
    public static final String FETCH_DEP_PARENT = "fetch_dep_parent";
    public static final String FETCH_DEP_CHILD = "fetch_dep_child";
    public static final String FETCH_ETALASE = "fetch_etalase";
    private final NetworkInteractor networkInteractorImpl;
    private final DbManager dbManager;
    private AddProductView addProductView;
    private final Gson gson;
    private Map<String, Object> originalEditData;
    private LocalCacheHandler fetchDepParentTimer;
    private LocalCacheHandler fetchDepChildTimer;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private LocalCacheHandler fetchEtalaseTimer;
    private ProductNameListener productNameListener;


    public AddProductPresenterImpl(AddProductView addProductView) {
        this.addProductView = addProductView;
        networkInteractorImpl = new NetworkInteractorImpl();
        dbManager = DbManagerImpl.getInstance();
        gson = new GsonBuilder().create();
    }


    @Override
    public void setView(AddProductView addProductView) {
        this.addProductView = addProductView;
    }

    /**
     * fetch if expired more than 1 week, and if already at database then show.
     *
     * @param context
     */
    @Override
    public void fetchDepartmentParent(Context context) {
        fetchDepParentTimer = initCacheIfNotNull(context, FETCH_DEP_PARENT);
        List<CategoryDB> categoryDBs = new Select().from(CategoryDB.class).where(CategoryDB_Table.levelId.is(0)).queryList();
        if (fetchDepParentTimer.isExpired() || checkCollectionNotNull(categoryDBs)) {
            ((NetworkInteractorImpl) networkInteractorImpl).setFetchDepartment(this);
            ((NetworkInteractorImpl) networkInteractorImpl).setCompositeSubscription(compositeSubscription);
            networkInteractorImpl.fetchDepartment(context);
        } else {
            addProductView.addLevelZeroCategory();
            addProductView.fetchEtalase();
        }
    }

    /**
     * init cache if not null
     *
     * @param context
     * @param text
     * @return
     */
    public static LocalCacheHandler initCacheIfNotNull(Context context, String text) {
        switch (text) {
            case FETCH_DEP_PARENT:
            case FETCH_DEP_CHILD:
            case FETCH_ETALASE:
                return new LocalCacheHandler(context, text);
            default:
                return null;
        }
    }

    @Override
    public void fetchEtalase(Context context) {
        fetchEtalaseTimer = initCacheIfNotNull(context, FETCH_ETALASE);
        if (fetchEtalaseTimer.isExpired()) {
            ((NetworkInteractorImpl) networkInteractorImpl).setFetchEtalase(this);
            ((NetworkInteractorImpl) networkInteractorImpl).setCompositeSubscription(compositeSubscription);
            networkInteractorImpl.fetchEtalase(context);
        } else {
            proceedEtalase();
        }

    }

    public static void clearEtalaseCache(Context context) {
        LocalCacheHandler fetchEtalaseTimer = initCacheIfNotNull(context, FETCH_ETALASE);
        fetchEtalaseTimer.setExpire(0);
    }

    public static void clearDepartementCache(Context context) {
        LocalCacheHandler.clearCache(context, AddProductPresenterImpl.FETCH_DEP_CHILD);
        LocalCacheHandler.clearCache(context, AddProductPresenterImpl.FETCH_DEP_PARENT);
    }


    @Override
    public void fetchDepartmentChild(Context context, int depId, int level) {
        List<CategoryDB> departmentChild = dbManager.getDepartmentChild(level, depId);
        fetchDepChildTimer = initCacheIfNotNull(context, FETCH_DEP_CHILD);
        if (!fetchDepChildTimer.isExpired() && checkCollectionNotNull(departmentChild)) {
            addProductView.processFetchDepartmentChild(null, depId, level);
        } else {
            ((NetworkInteractorImpl) networkInteractorImpl).setFetchDepartmentChild(this);
            ((NetworkInteractorImpl) networkInteractorImpl).setCompositeSubscription(compositeSubscription);
            networkInteractorImpl.fetchDepartmentChild(context, depId, level);
        }
    }

    @Override
    public void fetchCatalog(Context context, String productDepId, String productName) {
        ArrayList<CatalogDataModel.Catalog> catalogList = dbManager.getCatalogList(productDepId, productName);
        if (catalogList == null) {
            ((NetworkInteractorImpl) networkInteractorImpl).setFetchCatalog(this);
            ((NetworkInteractorImpl) networkInteractorImpl).setCompositeSubscription(compositeSubscription);
            networkInteractorImpl.fetchCatalog(context, productDepId, productName);
        } else {
            onSuccessFetchCatalog(catalogList);
        }
    }

    @Override
    public void fetchEditData(Context context, String productId) {
        ((NetworkInteractorImpl) networkInteractorImpl).setFetchEditData(this);
        networkInteractorImpl.editProductDetail(compositeSubscription, context, productId, "", "");
    }

    @Override
    public Map<String, Object> getOriginalEditData() {
        return originalEditData;
    }

    @Override
    public void subscribe() {
        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
    }

    @Override
    public void unsubscribe() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    @Override
    public void getProductDb(Context activity, long productDb) {
        ProductDB produk = DbManagerImpl.getInstance().getProductDb(productDb);
        produk.setPictureDBs(produk.getImages());
        produk.setWholesalePriceDBs(produk.getWholeSales());

        // tampilkan image models 1
        if (produk.getPictureDBs() != null) {// && object instanceof List<?>
            List<ImageModel> imageModelList = new ArrayList<>();
            int positionPrimaryImage = 0;
            for (int i = 0; i < produk.getPictureDBs().size(); i++) {
                PictureDB pictureDB = produk.getPictureDBs().get(i);
                ImageModel imageModel = new ImageModel();
                imageModel.setDbId(pictureDB.getId());
                imageModel.setPath(pictureDB.getPath());
                if (pictureDB.getPicturePrimary() == 1) {
                    positionPrimaryImage = i;
                }
                imageModelList.add(imageModel);
            }

            ImageModel primaryImage = imageModelList.remove(positionPrimaryImage);
            imageModelList.add(0, primaryImage);
            Log.d(TAG, "[gambar] firsttime " + imageModelList.toString());
            //[END] set primary image position

            int remainSize = 5 - imageModelList.size();
            for (int i = 0; i < remainSize; i++) {
                imageModelList.add(new ImageModel(R.drawable.addproduct));
            }
            addProductView.initPhotoAdapter(imageModelList);
        }

        // set preorder 5 - SKIPPED need to asked tom team
        if (checkNotNull(produk.getProductPreOrder()) && produk.getProductPreOrder() != 0)
            addProductView.setProductPreOrder(String.valueOf(produk.getProductPreOrder()));

        String deleteThis = "[Preorder %s Hari]";
        String productName = produk.getNameProd();
        String deleteThisWithText = String.format(deleteThis, String.valueOf(produk.getProductPreOrder()));
        if (productName.contains(deleteThisWithText)) {
            productName = productName.replace(deleteThisWithText, "");
        }

        // set Product Name 2
        addProductView.setProductName(productName);

        // set deskripsi 4
        String deleteThis2 = "Produk ini adalah produk preorder dan membutuhkan waktu proses %s Hari";
        String deleteThis2WithText = String.format(deleteThis2, String.valueOf(produk.getProductPreOrder()));
        String productDescription = produk.getDescProd();
        if (productDescription.toLowerCase().contains(deleteThis2WithText.toLowerCase())) {
            productDescription = productDescription.replace(deleteThis2WithText, "").trim();
        }


        productDescription = productDescription.replaceAll("(\r\n|\n)", "<br />");

        if (productDescription.equals("0")) {
            productDescription = "";
        }

        // [https://phab.tokopedia.com/T6924 BUG] Edit Product - Display Tag HTML
        addProductView.setProductDesc(MethodChecker.fromHtml(productDescription).toString());
        // [BUG] Edit Product - Display Tag HTML

        // set kategori 3
        List<List<SimpleTextModel>> lists = new ArrayList<>();
        ArrayList<TextDeleteModel> textDeleteModels = new ArrayList<>();

        Pair<List<List<SimpleTextModel>> , ArrayList<TextDeleteModel>> pairCategory = getCategoryTree(lists, textDeleteModels, produk.getCategoryDB());

        addProductView.initCategoryAdapter(pairCategory.first, pairCategory.second);


        String productCurrencyId = String.valueOf(produk.getUnitCurrencyDB().getWsInput());
        String productPrice = new DecimalFormat("#.##").format(produk.getPriceProd());


        // set currency unit 6
        // just for rupiah
        String singkatan = "";
        if (productCurrencyId.equals("1")) {// Rupiah
            singkatan = "Rp";
        } else {// Dollar
            singkatan = "US$";
        }
        List<CurrencyDB> currencyDBs = new Select().from(CurrencyDB.class)
                .queryList();
        for (int i=0;i<currencyDBs.size();i++){
            if(currencyDBs.get(i).getAbrvCurr().toLowerCase().contains(singkatan.toLowerCase())){
                addProductView.setProductCurrencyUnit(currencyDBs.get(i).getAbrvCurr());
            }
        }

        // set currency  7
        addProductView.setProductPrice(productPrice);


        // set currency unit 6
        // just for rupiah
//            String productPrice = productDetailData.getInfo().getProductPrice().replace(".","");
//            String[] hargaDanUnitHarga = productPrice.split(" ");
//            List<MataUang> mataUangs = new Select().from(MataUang.class)
//                    .execute();
//            for(MataUang mataUang : mataUangs){
//                if(hargaDanUnitHarga[0].contains(mataUang.getAbrvCurr())){
//                    addProductView.setProductCurrencyUnit(mataUang.getAbrvCurr());
//                }
//            }
//            // set currency  7
//            addProductView.setProductPrice(hargaDanUnitHarga[1]);

        // set weight unit 8
        String productWeightUnit = String.valueOf(produk.getWeightUnitDB().getWsInput());
        addProductView.setWeightUnit(productWeightUnit);

        // set weight 9
        String productWeight = String.valueOf(produk.getWeightProd());
        addProductView.setProductWeight(productWeight.replace(".", ""));

        // set minimum order 15
        String productMinOrder = String.valueOf(produk.getMinOrderProd());
        addProductView.setProductMinOrder(productMinOrder);

        // set whole grosir 10

        List<ProductWholesalePrice> wholesalePrice = new ArrayList<>();
        for (int i = 0; i < produk.getWholesalePriceDBs().size(); i++) {
            ProductWholesalePrice productWholesalePrice = new ProductWholesalePrice();
            productWholesalePrice.setWholesaleMin(produk.getWholesalePriceDBs().get(i).getMin() + "");
            productWholesalePrice.setWholesaleMax(produk.getWholesalePriceDBs().get(i).getMax() + "");
            productWholesalePrice.setWholesalePrice(String.format("%.00f", produk.getWholesalePriceDBs().get(i).getPriceWholesale()));
            wholesalePrice.add(productWholesalePrice);
        }

        ArrayList<WholeSaleAdapterModel> wholeSaleAdapterModels = convertToWholeSaleAdapterModel(wholesalePrice, addProductView.getProductCurrencyUnit());

        if (checkCollectionNotNull(wholeSaleAdapterModels)) {
            addProductView.initWholeSaleAdapter(wholeSaleAdapterModels);
        } else {
            addProductView.initWholeSaleAdapter();
        }

        // set etalase 11
        String productEtalaseId = String.valueOf(produk.getEtalaseDB().getEtalaseId());
        switch (produk.getStockStatusDB().getStockDetail()) {
            case AddProductView.ETALASE_ETALASE:
                addProductView.setProductEtalase(false, productEtalaseId);
                break;
            case EtalaseVariable.ETALASE_GUDANG:
                addProductView.setProductEtalase(true, productEtalaseId);
                break;
        }

        // set bekas/baru 13
        switch (produk.getConditionProd()) {
            case AddProductView.CONDITION_OLD:
                addProductView.setProductCondition(false);
                break;
            case AddProductView.CONDITION_NEW:
                addProductView.setProductCondition(true);
                break;
        }

        // set asuransi 14
        if (produk.getKebijakanReturnableDB() != null) {
            addProductView.setProductInsurance(true);
        } else {
            addProductView.setProductInsurance(false);
        }


        // TODO : CATALOG IS NOT REDERED YET ON VIEW

        // set catalog 15 in here
        long catalog = produk.getCatalogid();
        if (catalog != 0) {
            ArrayList<CatalogDataModel.Catalog> catalogItemDB = dbManager.getCatalogList(String.valueOf(produk.getCategoryDB().getDepartmentId()), produk.getNameProd());
            onSuccessFetchCatalog(catalogItemDB);
            int selection = 0;
            if (checkCollectionNotNull(catalogItemDB)) {
                A:
                for (CatalogDataModel.Catalog t : catalogItemDB) {
                    if (t.getCatalogId().equals(String.valueOf(produk.getCatalogid()))) {
                        break A;
                    }
                    selection++;
                }
                addProductView.setProductCatalog(selection);
            }
        }

        addProductView.dismissDialog();


    }

    @Override
    public void setupNameDebounceListener(final Context context) {
        compositeSubscription.add(Observable.create(new Observable.OnSubscribe<CatalogParams>() {
            @Override
            public void call(final Subscriber<? super CatalogParams> subscriber) {
                productNameListener = new ProductNameListener() {
                    @Override
                    public void onNameChange(CatalogParams params) {
                        subscriber.onNext(params);
                    }
                };
            }
        })
        .debounce(500, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<CatalogParams>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CatalogParams params) {
                fetchCatalog(context, params.depId, params.productName);
            }
        }));
    }

    @Override
    public void onNameChange(String depId, String productName) {
        productNameListener.onNameChange(new CatalogParams(depId, productName));
    }

    @Override
    public void onFailureFetchDepartment(Throwable e) {
        defaultMessageError(e);
    }

    @Override
    public void onSuccessFetchDepartment(DepartmentParentModel departmentParentModel) {
        if (checkNotNull(fetchDepParentTimer)) {
            fetchDepParentTimer.setExpire(SplashScreen.WEEK_IN_SECONDS);// set expired for 1 week
        }
        dbManager.saveDepartmentParent(departmentParentModel);
        addProductView.addLevelZeroCategory();
        addProductView.fetchEtalase();
    }

    @Override
    public void onFailureFetchEtalase(Throwable e) {
        defaultMessageError(e);
    }

    @Override
    public void onSuccessFetchEtalase(Response<TkpdResponse> responseData) {
        if (responseData.isSuccessful()) {
            TkpdResponse response = responseData.body();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response.getStringData());
            } catch (JSONException je) {
                Log.e(TAG, messageTAG + je.getLocalizedMessage());
            }
            if (!response.isError()) {
                if (checkNotNull(fetchEtalaseTimer)) {
                    fetchEtalaseTimer.setExpire(SplashScreen.DAYS_IN_SECONDS);// set expired for 1 week
                }

                GetEtalaseModel.Data data = gson.fromJson(jsonObject.toString(), GetEtalaseModel.Data.class);

                //[START] add gudang jika belum ada.
                dbManager.saveGudangIfNotInDb();
                //[OLD] add gudang jika belum ada.

                for (GetEtalaseModel.EtalaseModel etalaseModel : data.getEtalaseModels()) {
                    dbManager.saveEtalase(etalaseModel);
                }

                proceedEtalase();
            } else {
                addProductView.showMessageError(response.getErrorMessages());
            }
        } else {
            if (addProductView instanceof ErrorListener) {
                ErrorListener errorlistener = (ErrorListener) addProductView;
                new ErrorHandler(errorlistener, responseData.code());
            }
        }
    }

    private void proceedEtalase() {
        ArrayList<EtalaseModel> etalaseModels = new ArrayList<EtalaseModel>() {
            {
                add(new EtalaseModel().setText(EtalaseVariable.ETALASE_GUDANG));
                add(new EtalaseModel().setText(AddProductView.ETALASE_ETALASE));
            }
        };

        for (EtalaseDB etalse : dbManager.getEtalases()) {
            GetEtalaseModel.EtalaseModel etalaseModel = new GetEtalaseModel.EtalaseModel();
            etalaseModel.setEtalase_id(etalse.getEtalaseId() + "");
            etalaseModel.setEtalase_name(etalse.getEtalaseName());
            etalaseModel.setEtalase_total_product(etalse.getEtalaseTotal() + "");
            etalaseModel.setDbId(etalse.getId());

            // simpan etalase model ke stock tersedia
            etalaseModels.set(AddProductView.ETALASE_ETALASE_INDEX,
                    etalaseModels.get(AddProductView.ETALASE_ETALASE_INDEX).add(etalaseModel));
            // simpan etalase model ke stock kosong
            etalaseModels.set(ETALASE_GUDANG_INDEX,
                    etalaseModels.get(ETALASE_GUDANG_INDEX).add(etalaseModel));
        }

        // tamahkan "tambah baru"
        etalaseModels.set(AddProductView.ETALASE_ETALASE_INDEX,
                etalaseModels.get(1).add(AddProductView.ETALASE_TAMBAH_BARU));
        etalaseModels.set(ETALASE_GUDANG_INDEX,
                etalaseModels.get(0).add(AddProductView.ETALASE_TAMBAH_BARU));

        addProductView.addEtalaseChooseText();
        addProductView.initEtalaseAdapter(etalaseModels);
        addProductView.fetchProductData();
    }

    @Override
    public void onUnknown() {
        addProductView.onUnknown();
    }

    @Override
    public void onTimeout() {
        addProductView.onTimeout();
    }

    @Override
    public void onServerError() {
        addProductView.onServerError();
    }

    @Override
    public void onBadRequest() {
        addProductView.onBadRequest();
    }

    @Override
    public void onForbidden() {
        addProductView.onForbidden();
    }

    @Override
    public void onFailureFetchDepartmentChild(Throwable e) {
        defaultMessageError(e);
    }

    @Override
    public void onSuccessFetchDepartmentChild(DepartmentParentModel departmentParentModel, int depId, int level) {
        if (checkNotNull(fetchDepChildTimer)) {
            fetchDepChildTimer.setExpire(SplashScreen.WEEK_IN_SECONDS);
        }
        addProductView.processFetchDepartmentChild(departmentParentModel, depId, level);
    }

    @Override
    public void onFailureFetchCatalog(Throwable e) {
        defaultMessageError(e);
    }

    public void defaultMessageError(final Throwable e) {
        addProductView.showMessageError(new ArrayList<String>() {{
            add(e.getMessage());
        }});
    }

    @Override
    public void onSuccessFetchCatalog(CatalogDataModel catalogDataModel) {
        onSuccessFetchCatalog(catalogDataModel.getList());
    }

    public void onSuccessFetchCatalog(ArrayList<CatalogDataModel.Catalog> catalogArrayList) {
        addProductView.saveCatalogs(catalogArrayList);
        addProductView.setToCatalogView(catalogArrayList);
    }

    @Override
    public void onFailureFetchEditData(final Throwable e) {
        defaultMessageError(e);
    }


    @Override
    public void onSuccessFetchEditData(Map<String, Object> map) {
        originalEditData = map;

        // tampilkan image models 1
        Object object = map.get(NetworkInteractor.IMAGE_MODEL_DOWNLOADS);
        if (object != null) {// && object instanceof List<?>
            List<ImageModel> imageModelList = (List<ImageModel>) object;
            int positionPrimaryImage = findPrimaryImage(imageModelList, dbManager);

            if (positionPrimaryImage != -1) {
                ImageModel primaryImage = imageModelList.remove(positionPrimaryImage);
                imageModelList.add(0, primaryImage);
                Log.d(TAG, "[gambar] firsttime " + imageModelList.toString());
                //[END] set primary image position
            }

            int remainSize = 5 - imageModelList.size();
            for (int i = 0; i < remainSize; i++) {
                imageModelList.add(new ImageModel(R.drawable.addproduct));
            }
            addProductView.initPhotoAdapter(imageModelList);
        }

        object = map.get(NetworkInteractor.EDIT_PRODUCT_FORM);
        if (object != null && object instanceof EditProductForm) {
            EditProductForm editProductForm = (EditProductForm) object;
            EditProductForm.Data data = editProductForm.getData();
            if (checkNotNull(data)) {
                // set preorder 5 - SKIPPED need to asked tom team
                ProductPreOrder preOrder = data.getPreorder();
                preOrder.convertToDay();
                if (checkNotNull(preOrder.getPreorderProcessTime()) && !preOrder.getPreorderProcessTime().equals("0"))
                    addProductView.setProductPreOrder(preOrder.getPreorderProcessTime());

                String deleteThis = "[Preorder %s Hari]";
                String productName = MethodChecker.fromHtml(data.getProduct().getProductName()).toString();
                String deleteThisWithText = String.format(deleteThis, preOrder.getPreorderProcessTime());
                if (productName.contains(deleteThisWithText)) {
                    productName = productName.replace(deleteThisWithText, "");
                }
                // set Product Name 2
                addProductView.setProductName(productName);
                addProductView.disableProductNameEdit();

                // set deskripsi 4
                String deleteThis2 = "Produk ini adalah produk preorder dan membutuhkan waktu proses %s Hari";
                String deleteThis2WithText = String.format(deleteThis2, preOrder.getPreorderProcessTime());
                String productDescription = data.getProduct().getProductShortDesc();
                if (productDescription.toLowerCase().contains(deleteThis2WithText.toLowerCase())) {
                    productDescription = productDescription.replace(deleteThis2WithText, "").trim();
                }

                if (productDescription.equals("0")) {
                    productDescription = "";
                }
                // [https://phab.tokopedia.com/T6924 BUG] Edit Product - Display Tag HTML
                addProductView.setProductDesc(MethodChecker.fromHtml(productDescription).toString());
                // [BUG] Edit Product - Display Tag HTML

                // set kategori 3
                List<EditProductForm.Breadcrumb> breadcrumb = data.getBreadcrumb();
                List<List<SimpleTextModel>> lists = new ArrayList<>();
                ArrayList<TextDeleteModel> textDeleteModels = new ArrayList<>();
                for (int i = 1, j = 0; i <= breadcrumb.size(); i++, j++) {
                    String departmentId = "0";
                    if(j != 0) {
                        departmentId = breadcrumb.get(j-1).getDepartmentId();
                    }
                    List<SimpleTextModel> levelSelection = AddProductFragment.toSimpleText(i, Integer.parseInt(departmentId));
                    lists.add(levelSelection);

                    TextDeleteModel textToDisplay = new TextDeleteModel(breadcrumb.get(j).getDepartmentName());
                    textToDisplay.setDepartmentId(Integer.parseInt(breadcrumb.get(j).getDepartmentId()));
                    textDeleteModels.add(textToDisplay);
                }
                addProductView.initCategoryAdapter(lists, textDeleteModels);

                EditProductForm.Product product = data.getProduct();
                String productCurrencyId = product.getProductCurrencyId();
                String productPrice = product.getProductPrice();
                String isEditableName = product.getProductNameEditable();

                if (isEditableName != null && isEditableName.equals("1")) {
                    addProductView.enableProductNameEdit();
                }

                // set currency unit 6
                // just for rupiah
                String singkatan = "";
                if (productCurrencyId.equals("1")) {// Rupiah
                    singkatan = "Rp";
                } else {// Dollar
                    singkatan = "US$";
                }
                List<CurrencyDB> currencyDBs = new Select().from(CurrencyDB.class)
                        .queryList();
                for (int i = 0; i < currencyDBs.size(); i++) {
                    if (currencyDBs.get(i).getAbrvCurr().toLowerCase().contains(singkatan.toLowerCase())) {
                        addProductView.setProductCurrencyUnit(currencyDBs.get(i).getAbrvCurr());
                    }
                }

                // set currency  7
                addProductView.setProductPrice(productPrice);


                // set whole grosir 10
                List<ProductWholesalePrice> wholesalePrice = new ArrayList<>();
                for (EditProductForm.WholesalePrice wholesale : data.getWholesalePrice()) {
                    ProductWholesalePrice productWholesalePrice = new ProductWholesalePrice();
                    productWholesalePrice.setWholesaleMax(wholesale.getWholesaleMax());
                    productWholesalePrice.setWholesaleMin(wholesale.getWholesaleMin());
                    productWholesalePrice.setWholesalePrice(wholesale.getWholesalePrice());
                    wholesalePrice.add(productWholesalePrice);
                }

                ArrayList<WholeSaleAdapterModel> wholeSaleAdapterModels = convertToWholeSaleAdapterModel(wholesalePrice, addProductView.getProductCurrencyUnit());

                if (checkCollectionNotNull(wholeSaleAdapterModels)) {
                    addProductView.initWholeSaleAdapter(wholeSaleAdapterModels);
                } else {
                    addProductView.initWholeSaleAdapter();
                }

                // set weight unit 8
                String productWeightUnit = ProductEditHelper.convertProductWeight(data.getProduct().getProductWeightUnit());
                addProductView.setWeightUnit(productWeightUnit);

                // set weight 9
                String productWeight = data.getProduct().getProductWeight();
                addProductView.setProductWeight(productWeight.replace(".", ""));

                // set minimum order 15
                String productMinOrder = data.getProduct().getProductMinOrder().replace(".", "");
                addProductView.setProductMinOrder(productMinOrder);

                // set etalase 11
                int productStatus = Integer.parseInt(data.getProduct().getProductStatus());
                String productEtalaseId = data.getProduct().getProductEtalaseId();
                switch (productStatus) {
                    case NetworkInteractor.PRD_STATE_ACTIVE:
                        addProductView.setProductEtalase(false, productEtalaseId);
                        break;
                    case NetworkInteractor.PRD_STATE_WAREHOUSE:
                        addProductView.setProductEtalase(true, productEtalaseId);
                        break;
                }

                // set bekas/baru 13
                switch (data.getProduct().getProductConditionName().toLowerCase()) {
                    case "bekas":
                        addProductView.setProductCondition(false);
                        break;
                    case "baru":
                        addProductView.setProductCondition(true);
                        break;
                }

                // set asuransi 14
                if (data.getProduct().getProductInsurance().toLowerCase().equals("ya")) {
                    addProductView.setProductInsurance(true);
                } else {
                    addProductView.setProductInsurance(false);
                }

                // set catalog 15 in here
                object = map.get(NetworkInteractor.CATALOG_MODEL_EDIT);
                if (object != null && object instanceof CatalogDataModel) {
                    CatalogDataModel catalogDataModel = (CatalogDataModel) object;

                    onSuccessFetchCatalog(catalogDataModel);

                    int selection = 0;
                    if (data.getCatalog().getCatalogId().equals("0")) {
                        addProductView.setProductCatalog(-1);
                    } else {
                        ArrayList<CatalogDataModel.Catalog> list = catalogDataModel.getList();
                        if (checkCollectionNotNull(list)) {
                            int x = -1;
                            A:
                            for (CatalogDataModel.Catalog t : list) {
                                if (t.getCatalogName().equals(data.getCatalog().getCatalogName())) {
                                    x = selection;
                                    break A;
                                }
                                selection++;
                            }
                            addProductView.setProductCatalog(x);
                        }
                    }
                }

                addProductView.constructOriginalEditData();
                addProductView.showProgress(false);
            }else {
                throw new RuntimeException("edit is null");
            }
        } else {
            throw new RuntimeException("edit form is null");
        }

    }


    public static int findPrimaryImage(List<ImageModel> imageModelList, DbManager dbManager) {
        //[START] set primary image position
        // search for primary image
        int positionPrimaryImage = -1, count = 0;
        A:
        for (ImageModel image :
                imageModelList) {
            PictureDB pictureDB = dbManager.getGambarById(image.getDbId());
            if (pictureDB.getPicturePrimary() == PictureDB.PRIMARY_IMAGE) {
                positionPrimaryImage = count;
                break A;
            }
            count++;
        }
        return positionPrimaryImage;
    }

    public static int findPrimaryImage(List<PictureDB> imageModelList) {
        //[START] set primary image position
        // search for primary image
        int positionPrimaryImage = -1, count = 0;
        A:
        for (PictureDB image :
                imageModelList) {
            if (image.getPicturePrimary() == PictureDB.PRIMARY_IMAGE) {
                positionPrimaryImage = count;
                break A;
            }
            count++;
        }
        return positionPrimaryImage;
    }

    @NonNull
    public static ArrayList<WholeSaleAdapterModel> convertToWholeSaleAdapterModel(List<ProductWholesalePrice> wholesalePrice, String currency) {
        ArrayList<WholeSaleAdapterModel> wholeSaleAdapterModels = new ArrayList<>();
        for (ProductWholesalePrice productWholesalePrice : wholesalePrice) {
            WholeSaleAdapterModel wholeSaleAdapterModel = new WholeSaleAdapterModel(
                    productWholesalePrice.getWholesaleMin(),
                    productWholesalePrice.getWholesaleMax(),
                    productWholesalePrice.getWholesalePrice(),
                    currency
            );
            wholeSaleAdapterModels.add(wholeSaleAdapterModel);
        }
        return wholeSaleAdapterModels;
    }

    /**
     * Recursive method to get the model for filling category in add product
     * @param lists
     * @param textDeleteModels
     * @param categoryDB
     * @return
     */
    public static Pair<List<List<SimpleTextModel>>, ArrayList<TextDeleteModel>> getCategoryTree(List<List<SimpleTextModel>> lists, ArrayList<TextDeleteModel> textDeleteModels, CategoryDB categoryDB) {

        List<SimpleTextModel> levelSelection = AddProductFragment.toSimpleText(categoryDB.getLevelId(), categoryDB.getDepartmentId());
        lists.add(0, levelSelection);

        TextDeleteModel textToDisplay = new TextDeleteModel(categoryDB.getNameCategory());
        textToDisplay.setDepartmentId(Integer.parseInt(String.valueOf(categoryDB.getDepartmentId())));
        textDeleteModels.add(0, textToDisplay);

        if(categoryDB.getParentId() != 0) {
            CategoryDB parentDB = new Select().from(CategoryDB.class)
                    .where(CategoryDB_Table.departmentId.is(categoryDB.getParentId()))
                    .querySingle();
            return getCategoryTree(lists, textDeleteModels, parentDB);
        } else {
            return new Pair<> (lists, textDeleteModels);
        }
    }

    private class CatalogParams{
        public String productName;
        public String depId;

        public CatalogParams(String depId, String productName) {
            this.depId = depId;
            this.productName = productName;
        }
    }


    interface ProductNameListener{
        void onNameChange(CatalogParams name);
    }
}
