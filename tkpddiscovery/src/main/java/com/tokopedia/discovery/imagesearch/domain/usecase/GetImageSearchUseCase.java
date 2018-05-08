package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.entity.wishlist.WishlistCheckResult;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.imagesearch.data.repository.ImageSearchRepository;
import com.tokopedia.discovery.newdiscovery.domain.model.ProductModel;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class GetImageSearchUseCase<T> extends UseCase<SearchResultModel> {


    private final MojitoApi service;
    private ImageSearchRepository imageSearchRepository;
    private Context context;

    private final int MAX_WIDTH = 600;
    private final int MAX_HEIGHT = 600;
    private final static String pageSize = "100";
    private final static String pageOffset = "0";

    private String imagePath;

    public GetImageSearchUseCase(Context context, ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 ImageSearchRepository imageSearchRepository,
                                 MojitoApi service) {
        super(threadExecutor, postExecutionThread);
        this.context = context;
        this.imageSearchRepository = imageSearchRepository;
        this.service = service;
    }

    private static RequestParams initializeSearchRequestParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(BrowseApi.DEVICE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(BrowseApi.PAGE_SIZE, pageSize);
        requestParams.putString(BrowseApi.PAGE, pageOffset);
        return requestParams;
    }

    private static RequestParams initializeFormDataSearchRequestParam(String uniqueId, String imageByteArray) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(BrowseApi.IMAGE, imageByteArray);
        requestParams.putString(BrowseApi.UNIQUE_ID, uniqueId);
        return requestParams;
    }

    @Override
    public Observable<SearchResultModel> createObservable(RequestParams requestParams) {
        return Observable.just(imagePath)
                .flatMap(new Func1<String, Observable<SearchResultModel>>() {
                    @Override
                    public Observable<SearchResultModel> call(String imagePath) {
                        File imgFile = new File(imagePath);
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        myBitmap = ImageHandler.resizeImage(myBitmap, MAX_WIDTH, MAX_HEIGHT);
                        try {
                            myBitmap = ImageHandler.RotatedBitmap(myBitmap, imagePath);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        String encodePicContent = Base64.encodeToString(byteArray,
                                Base64.NO_WRAP | Base64.NO_CLOSE);

                        return imageSearchRepository.getImageSearchResults(GetImageSearchUseCase.initializeSearchRequestParam().getParameters(),
                                GetImageSearchUseCase.initializeFormDataSearchRequestParam(generateUniqueId(),
                                        encodePicContent).getParameters()).flatMap(wishlistDataEnricher(getUserId()));
                    }
                });
    }

    private Func1<SearchResultModel, Observable<SearchResultModel>> wishlistDataEnricher(final String userId) {
        return new Func1<SearchResultModel, Observable<SearchResultModel>>() {
            @Override
            public Observable<SearchResultModel> call(final SearchResultModel searchResultModel) {
                if (TextUtils.isEmpty(userId) || searchResultModel.getProductList().isEmpty()) {
                    return Observable.just(searchResultModel);
                }

                return Observable.zip(Observable.just(searchResultModel),
                        getWishListData(searchResultModel, userId),
                        new Func2<SearchResultModel, Response<WishlistCheckResult>, SearchResultModel>() {
                            @Override
                            public SearchResultModel call(SearchResultModel searchResultModel,
                                                          Response<WishlistCheckResult> wishlistCheckResultResponse) {
                                enrichWithWishListData(searchResultModel, wishlistCheckResultResponse);
                                return searchResultModel;
                            }
                        }).onErrorReturn(new Func1<Throwable, SearchResultModel>() {
                    @Override
                    public SearchResultModel call(Throwable throwable) {
                        return searchResultModel;
                    }
                });
            }
        };
    }

    private Observable<Response<WishlistCheckResult>> getWishListData(SearchResultModel searchResultModel, String userId) {
        List<String> productIdList = generateProductIdList(searchResultModel.getProductList());
        return service.checkWishlist(userId, TextUtils.join(",", productIdList))
                .onErrorReturn(new Func1<Throwable, Response<WishlistCheckResult>>() {
                    @Override
                    public Response<WishlistCheckResult> call(Throwable throwable) {
                        WishlistCheckResult wishlistCheckResult = new WishlistCheckResult();
                        WishlistCheckResult.CheckResultIds ids = new WishlistCheckResult.CheckResultIds();
                        wishlistCheckResult.setCheckResultIds(ids);
                        return Response.success(wishlistCheckResult);
                    }
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

    private String generateUniqueId() {
        SessionHandler sessionHandler = new SessionHandler(context);
        GCMHandler gcmHandler = new GCMHandler(context);
        return sessionHandler.isV4Login() ?
                AuthUtil.md5(sessionHandler.getLoginID()) :
                AuthUtil.md5(gcmHandler.getRegistrationId());
    }

    private String getUserId() {
        SessionHandler sessionHandler = new SessionHandler(context);
        return sessionHandler.isV4Login() ?
                sessionHandler.getLoginID() :
                "";
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
