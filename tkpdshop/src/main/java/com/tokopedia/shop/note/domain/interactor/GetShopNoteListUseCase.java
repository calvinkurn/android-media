package com.tokopedia.shop.note.domain.interactor;

import com.tokopedia.shop.note.data.source.cloud.model.ShopNote;
import com.tokopedia.shop.info.domain.repository.ShopInfoRepository;
import com.tokopedia.shop.note.domain.repository.ShopNoteRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by User on 9/8/2017.
 */

public class GetShopNoteListUseCase extends UseCase<List<ShopNote>> {

    private static final String SHOP_ID = "SHOP_ID";

    private ShopNoteRepository shopNoteRepository;

    @Inject
    public GetShopNoteListUseCase(ShopNoteRepository shopNoteRepository) {
        super();
        this.shopNoteRepository = shopNoteRepository;
    }

    @Override
    public Observable<List<ShopNote>> createObservable(RequestParams requestParams) {
        String shopId = requestParams.getString(SHOP_ID, null);
        return shopNoteRepository.getShopNoteList(shopId);
    }

    public static RequestParams createRequestParam(String shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        return requestParams;
    }
}
