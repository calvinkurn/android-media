package com.tokopedia.seller.product.imagepicker;

import com.tokopedia.seller.product.imagepicker.model.CatalogImage;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class CatalogImageDataSource {

    private CatalogImageDataSourceCloud catalogImageDataSourceCloud;

    @Inject
    public CatalogImageDataSource(CatalogImageDataSourceCloud catalogImageDataSourceCloud) {
        this.catalogImageDataSourceCloud = catalogImageDataSourceCloud;
    }

    public Observable<List<CatalogImage>> getCatalogImage(String catalogId) {
        return catalogImageDataSourceCloud.getCatalogImage(catalogId);
    }
}
