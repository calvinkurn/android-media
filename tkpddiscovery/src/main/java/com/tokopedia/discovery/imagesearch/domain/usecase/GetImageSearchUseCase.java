package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

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
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.discovery.imagesearch.data.repository.ImageSearchRepository;
import com.tokopedia.discovery.imagesearch.domain.model.ImageSearchItemRequest;
import com.tokopedia.discovery.imagesearch.domain.model.ImageSearchItemResponse;
import com.tokopedia.discovery.newdiscovery.domain.model.ProductModel;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class GetImageSearchUseCase<T> extends UseCase<SearchResultModel> {


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
                                 ImageSearchRepository imageSearchRepository) {
        super(threadExecutor, postExecutionThread);
        this.context = context;
        this.imageSearchRepository = imageSearchRepository;
    }

    public static RequestParams initializeSearchRequestParam(SearchParameter imageSearchProductParameter) {

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(BrowseApi.SOURCE, !TextUtils.isEmpty(
                imageSearchProductParameter.getSource()) ? imageSearchProductParameter.getSource() : BrowseApi.DEFAULT_VALUE_SOURCE_SEARCH);
        requestParams.putString(BrowseApi.DEVICE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(BrowseApi.ROWS, String.valueOf(imageSearchProductParameter.getStartRow()));
        requestParams.putString(BrowseApi.ID, imageSearchProductParameter.getQueryKey());
        return requestParams;
    }

    @Override
    public Observable<SearchResultModel> createObservable(RequestParams requestParams) {
        return Observable.just(imagePath)
                .flatMap(new Func1<String, Observable<ImageSearchItemResponse>>() {
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
                })
                .flatMap(new Func1<ImageSearchItemResponse, Observable<SearchResultModel>>() {
                    @Override
                    public Observable<SearchResultModel> call(ImageSearchItemResponse imageSearchResponse) {
                        if (imageSearchResponse == null || imageSearchResponse.getAuctionsArrayList() == null) {
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
                        }

                        if (StringUtils.isNotBlank(productIDs.toString())) {
                            SearchParameter imageSearchProductParameter = new SearchParameter();
                            imageSearchProductParameter.setStartRow(productIDList.size());
                            imageSearchProductParameter.setQueryKey(String.valueOf(productIDs));
                            imageSearchProductParameter.setSource("imagesearch");
                            return Observable.zip(
                                    Observable.just(productIDList),
                                    imageSearchRepository.getImageSearchResults(
                                            GetImageSearchUseCase.initializeSearchRequestParam(imageSearchProductParameter).getParameters()
                                    ),
                                    new Func2<List<String>, SearchResultModel, SearchResultModel>() {
                                        @Override
                                        public SearchResultModel call(List<String> strings, SearchResultModel searchResultModel) {
                                            HashMap<String, ProductModel> productItemHashMap = new HashMap<>();
                                            for (ProductModel productItem : searchResultModel.getProductList()) {
                                                productItemHashMap.put(productItem.getProductID(), productItem);
                                            }
                                            List<ProductModel> productItemList = new ArrayList<>();
                                            for (String productId : strings) {
                                                if (productItemHashMap.get(productId) != null) {
                                                    ProductModel productModel = productItemHashMap.get(productId);
                                                    productItemList.add(productModel);
                                                    productItemHashMap.remove(productModel.getProductID());
                                                }
                                            }
                                            productItemList.addAll(productItemHashMap.values());
                                            searchResultModel.setProductList(productItemList);
                                            searchResultModel.setTotalData(productItemList.size());
                                            return searchResultModel;
                                        }
                                    }
                            );
                        } else {
                            throw new HandleImageSearchResponseError("c");
                        }
                    }
                });
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
