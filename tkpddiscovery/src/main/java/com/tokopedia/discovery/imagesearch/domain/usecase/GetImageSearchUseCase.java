package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.entity.wishlist.WishlistCheckResult;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.imagesearch.data.mapper.ImageProductMapper;
import com.tokopedia.discovery.imagesearch.network.apiservice.ImageSearchService;
import com.tokopedia.discovery.imagesearch.search.exception.ImageNotSupportedException;
import com.tokopedia.discovery.newdiscovery.domain.model.ProductModel;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class GetImageSearchUseCase<T> extends UseCase<SearchResultModel> {


    private static final String QUERY_KEY = "query";
    private static final String IMAGE_CONTENT = "image";
    private static final String PARAMS = "params";
    private static final String VARIABLE_KEY = "variables";
    private static final String OPERATIONS_NAME = "operationName";
    private final MojitoApi service;
    private ImageProductMapper productMapper;
    private ImageSearchService imageSearchService;
    private Context context;
    private UserSessionInterface userSession;

    private final int OPTIMUM_WIDTH = 300;
    private final int OPTIMUM_HEIGHT = 300;

    private final int MAX_WIDTH = 1280;
    private final int MAX_HEIGHT = 720;

    private final static String pageSize = "100";
    private final static String pageOffset = "0";

    private String imagePath;
    private final int MIN_WIDTH = 200;
    private final int MIN_HEIGHT = 200;

    public GetImageSearchUseCase(Context context, ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 ImageSearchService imageSearchService,
                                 ImageProductMapper imageProductMapper,
                                 MojitoApi service,
                                 UserSessionInterface userSession) {
        super(threadExecutor, postExecutionThread);
        this.context = context;
        this.service = service;
        this.imageSearchService = imageSearchService;
        this.productMapper = imageProductMapper;
        this.userSession = userSession;
    }

    private static String initializeSearchRequestParamForGql() {
        return "page=" + String.valueOf(pageOffset) + "&page_size=" + pageSize + "&device=" + BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE;
    }

    @Override
    public Observable<SearchResultModel> createObservable(RequestParams params) {
        return Observable.just(imagePath)
                .flatMap(imagePath -> {
                    File imgFile = new File(imagePath);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    try {
                        if (myBitmap == null) {
                            myBitmap = ImageHandler.getBitmapFromUri(context, Uri.parse(imagePath),
                                    OPTIMUM_WIDTH, OPTIMUM_HEIGHT);
                        }

                        if (myBitmap.getWidth() < MIN_WIDTH ||
                                myBitmap.getHeight() < MIN_HEIGHT) {

                            throw new ImageNotSupportedException();

                        } else if (myBitmap.getWidth() > OPTIMUM_WIDTH ||
                                myBitmap.getHeight() > OPTIMUM_HEIGHT) {
                            myBitmap = ImageHandler.resizeImage(myBitmap, OPTIMUM_WIDTH, OPTIMUM_HEIGHT);
                        }

                        if (myBitmap.getHeight() > MAX_HEIGHT ||
                                myBitmap.getWidth() > MAX_WIDTH) {
                            throw new ImageNotSupportedException();
                        }

                        myBitmap = ImageHandler.RotatedBitmap(myBitmap, imagePath);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    String encodePicContent = Base64.encodeToString(byteArray,
                            Base64.NO_WRAP | Base64.NO_CLOSE);

                    com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();

                    Map<String, Object> mapContentVariable = new HashMap<>();
                    mapContentVariable.put(IMAGE_CONTENT, encodePicContent);
                    mapContentVariable.put(PARAMS, initializeSearchRequestParamForGql());

                    requestParams.putString(QUERY_KEY, getRequestImageSearchPayload());
                    requestParams.putObject(VARIABLE_KEY, mapContentVariable);
                    requestParams.putObject(OPERATIONS_NAME, "image_search");
                    return imageSearchService.getApi().getImageSearchResults(requestParams.getParameters())
                            .map(productMapper)
                            .flatMap(wishlistDataEnricher(getUserId()));
                });
    }

    private String getRequestImageSearchPayload() {
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.query_image_search);
    }

    private Func1<SearchResultModel, Observable<SearchResultModel>> wishlistDataEnricher(final String userId) {
        return searchResultModel -> {
            if (TextUtils.isEmpty(userId) || searchResultModel.getProductList().isEmpty()) {
                return Observable.just(searchResultModel);
            }

            return Observable.zip(Observable.just(searchResultModel),
                    getWishListData(searchResultModel, userId),
                    (searchResultModel1, wishlistCheckResultResponse) -> {
                        enrichWithWishListData(searchResultModel1, wishlistCheckResultResponse);
                        return searchResultModel1;
                    }).onErrorReturn(throwable -> searchResultModel);
        };
    }

    private Observable<Response<WishlistCheckResult>> getWishListData(SearchResultModel searchResultModel, String userId) {
        List<String> productIdList = generateProductIdList(searchResultModel.getProductList());
        return service.checkWishlist(userId, TextUtils.join(",", productIdList))
                .onErrorReturn(throwable -> {
                    WishlistCheckResult wishlistCheckResult = new WishlistCheckResult();
                    WishlistCheckResult.CheckResultIds ids = new WishlistCheckResult.CheckResultIds();
                    wishlistCheckResult.setCheckResultIds(ids);
                    return Response.success(wishlistCheckResult);
                });
    }

    private List<String> generateProductIdList(List<ProductModel> productModelList) {
        List<String> productIdList = new ArrayList<>();
        for (ProductModel productModel : productModelList) {
            productIdList.add(productModel.getProductID());
        }
        return productIdList;
    }

    private void enrichWithWishListData(SearchResultModel searchResultModel,
                                        Response<WishlistCheckResult> wishlistCheckResultResponse) {

        List<String> wishListedIdList = wishlistCheckResultResponse.body().getCheckResultIds().getIds();

        for (ProductModel productModel : searchResultModel.getProductList()) {
            productModel.setWishlisted(wishListedIdList.contains(productModel.getProductID()));
        }
    }

    private String getUserId() {
        return userSession.isLoggedIn() ? userSession.getUserId() : "";
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
