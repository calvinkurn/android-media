package com.tokopedia.seller.product.imagepicker;

import com.tokopedia.seller.product.imagepicker.model.CatalogImage;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class CatalogImageRepositoryImpl implements CatalogImageRepository {

    private CatalogImageDataSource catalogImageDataSource;

    public CatalogImageRepositoryImpl(CatalogImageDataSource catalogImageDataSource) {
        this.catalogImageDataSource = catalogImageDataSource;
    }

    @Override
    public Observable<List<CatalogImage>> getCatalogImage(String catalogId) {
        return catalogImageDataSource.getCatalogImage(catalogId);
    }
}
