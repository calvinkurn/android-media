package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.tkpd.library.utils.CommonUtils;
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
import com.tokopedia.discovery.imagesearch.domain.model.ImageSearchItemRequest;
import com.tokopedia.discovery.imagesearch.domain.model.ImageSearchItemResponse;
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

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class GetImageSearchUseCase<T> extends UseCase<SearchResultModel> {


    private final MojitoApi service;
    private ImageSearchRepository imageSearchRepository;
    private Context context;

    private final String REGION_ID = "ap-southeast-1";
    private final String ACCESS_KEY_ID = AuthUtil.KEY.ALIYUN_ACCESS_KEY_ID;
    private final String SECRET_KEY = AuthUtil.KEY.ALIYUN_SECRET_KEY;
    private final String END_POINT_NAME = "ap-southeast-1";
    private final String PRODUCT = "ImageSearch";
    private final String IMAGE_SEARCH_ALIYUN_DOMAIN = "imagesearch.ap-southeast-1.aliyuncs.com";
    private final String IMAGE_SEARCH_INSTANCE = "productsearch01";

    private final int MAX_WIDTH = 600;
    private final int MAX_HEIGHT = 600;

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
        requestParams.putString("page_size", String.valueOf(100));// String.valueOf(imageSearchProductParameter.getStartRow()));
        requestParams.putString("page", String.valueOf(0)); // imageSearchProductParameter.getQueryKey());
        return requestParams;
    }

    private static RequestParams initializeFormDataSearchRequestParam(String uniqueId, String imageByteArray) {

        RequestParams requestParams = RequestParams.create();
        requestParams.putString("image", imageByteArray);
        requestParams.putString("unique_id", uniqueId);
        return requestParams;
    }

    @Override
    public Observable<SearchResultModel> createObservable(RequestParams requestParams) {
        return Observable.just(imagePath)
                /*.flatMap(new Func1<String, Observable<ImageSearchItemResponse>>() {
                    @Override
                    public Observable<ImageSearchItemResponse> call(String s) {
                        File imgFile = new File(s);

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        myBitmap = ImageHandler.resizeImage(myBitmap, MAX_WIDTH, MAX_HEIGHT);
                        try {
                            myBitmap = ImageHandler.RotatedBitmap(myBitmap, s);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        return getImageSearchItemResponse(byteArray);
                    }
                })*/
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

                        /*if (imageSearchResponse == null || imageSearchResponse.getAuctionsArrayList() == null) {
                            throw new GetImageSearchException("b");
                        }

                        List<String> productIDList = new ArrayList<>();
                        int productCount = imageSearchResponse.getAuctionsArrayList().size();
                        StringBuilder productIDs = new StringBuilder();

                        productIDList.clear();

                        for (int i = 0; i < productCount; i++) {
                            String itemId = imageSearchResponse.getAuctionsArrayList().get(i).getItemId();
                            productIDList.add(itemId);
                            productIDs.append(itemId);
                            if (i != productCount - 1) {
                                productIDs.append(",");
                            }
                        }*/

                        /*return Observable.zip(
                                Observable.just(productIDList),
                                imageSearchRepository.getImageSearchResults(
                                        GetImageSearchUseCase.initializeSearchRequestParam(imageSearchProductParameter).getParameters()
                                ),
                                new Func2<List<String>, SearchResultModel, SearchResultModel>() {
                                    @Override
                                    public SearchResultModel call(List<String> strings, SearchResultModel searchResultModel) {
//                                            HashMap<String, ProductModel> productItemHashMap = new HashMap<>();
//                                            for (ProductModel productItem : searchResultModel.getProductList()) {
//                                                productItemHashMap.put(productItem.getProductID(), productItem);
//                                            }
//                                            List<ProductModel> productItemList = new ArrayList<>();
//                                            for (String productId : strings) {
//                                                if (productItemHashMap.get(productId) != null) {
//                                                    ProductModel productModel = productItemHashMap.get(productId);
//                                                    productItemList.add(productModel);
//                                                    productItemHashMap.remove(productModel.getProductID());
//                                                }
//                                            }
//                                            productItemList.addAll(productItemHashMap.values());
//                                            searchResultModel.setProductList(productItemList);
//                                            searchResultModel.setTotalData(productItemList.size());
                                        return searchResultModel;
                                    }
                                }
                        );*/

                        String encodePicContent = Base64.encodeToString(byteArray,
                                Base64.NO_WRAP | Base64.NO_CLOSE);

                        return imageSearchRepository.getImageSearchResults(GetImageSearchUseCase.initializeSearchRequestParam().getParameters(),
                                GetImageSearchUseCase.initializeFormDataSearchRequestParam(generateUniqueId(),
                                        encodePicContent).getParameters());//.flatMap(wishlistDataEnricher(getUserId()));
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
                        getWishlistData(searchResultModel, userId),
                        new Func2<SearchResultModel, Response<WishlistCheckResult>, SearchResultModel>() {
                            @Override
                            public SearchResultModel call(SearchResultModel searchResultModel,
                                                          Response<WishlistCheckResult> wishlistCheckResultResponse) {
                                enrichWithWishlistData(searchResultModel, wishlistCheckResultResponse);
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

    private Observable<Response<WishlistCheckResult>> getWishlistData(SearchResultModel searchResultModel, String userId) {

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

    private void enrichWithWishlistData(SearchResultModel searchResultModel,
                                        Response<WishlistCheckResult> wishlistCheckResultResponse) {

        List<String> wishlistedIdList = wishlistCheckResultResponse.body().getCheckResultIds().getIds();

        for (ProductModel productModel : searchResultModel.getProductList()) {
            productModel.setWishlisted(wishlistedIdList.contains(productModel.getProductID()));
        }
    }

    public Observable<ImageSearchItemResponse> getImageSearchItemResponse(byte[] imageByteArray) {
        return Observable.just(imageByteArray)
                .map(new Func1<byte[], ImageSearchItemResponse>() {
                    @Override
                    public ImageSearchItemResponse call(byte[] bytes) {
                        IClientProfile profile = DefaultProfile.getProfile(REGION_ID, ACCESS_KEY_ID,
                                SECRET_KEY);
                        try {
                            DefaultProfile.addEndpoint(END_POINT_NAME, REGION_ID,
                                    PRODUCT, IMAGE_SEARCH_ALIYUN_DOMAIN);
                        } catch (ClientException e) {
                            e.printStackTrace();
                        }

                        IAcsClient client = new DefaultAcsClient(profile);

                        ImageSearchItemRequest request = new ImageSearchItemRequest();
                        request.setInstanceName(IMAGE_SEARCH_INSTANCE);
                        request.setSearchPicture(bytes);

                        if (!request.buildPostContent()) {
                            CommonUtils.dumper("Image Search build post content failed.");
                            return new ImageSearchItemResponse();
                        }

                        ImageSearchItemResponse response = null;
                        try {
                            response = client.getAcsResponse(request);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return response;
                    }
                });
    }

    private String generateUniqueId() {

        SessionHandler sessionHandler = new SessionHandler(getContext());
        GCMHandler gcmHandler = new GCMHandler(getContext());

        return sessionHandler.isV4Login() ?
                AuthUtil.md5(sessionHandler.getLoginID()) :
                AuthUtil.md5(gcmHandler.getRegistrationId());
    }

    private String getUserId() {

        SessionHandler sessionHandler = new SessionHandler(getContext());

        return sessionHandler.isV4Login() ?
                sessionHandler.getLoginID() :
                "";
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public static class GetImageSearchException extends RuntimeException {
        public GetImageSearchException(String s) {
        }
    }

    public static class HandleImageSearchResponseError extends RuntimeException {
        public HandleImageSearchResponseError(String s) {
        }
    }
}
