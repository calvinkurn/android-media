package com.tokopedia.shop.note.domain.repository;

import com.tokopedia.shop.note.data.source.cloud.model.ShopNote;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ShopNoteRepository {

    Observable<List<ShopNote>> getShopNoteList(String shopId);
}
