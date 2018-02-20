package com.tokopedia.wishlist.common.domain.interactor;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.wishlist.common.domain.repository.WishListCommonRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by User on 9/8/2017.
 */

public class AddToWishListUseCase extends UseCase<Boolean> {

    private static final String USER_ID = "USER_ID";
    private static final String PRODUCT_ID = "PRODUCT_ID";

    private WishListCommonRepository wishListCommonRepository;

    public AddToWishListUseCase(WishListCommonRepository wishListCommonRepository) {
        this.wishListCommonRepository = wishListCommonRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        String userId = requestParams.getString(USER_ID, null);
        String productId = requestParams.getString(PRODUCT_ID, null);
        return wishListCommonRepository.addToWishList(userId, productId);
    }

    public static RequestParams createRequestParam(String shopId, String productId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(USER_ID, shopId);
        requestParams.putObject(PRODUCT_ID, productId);
        return requestParams;
    }
}
