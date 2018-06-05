package com.tokopedia.seller.product.imagepicker;

import com.tokopedia.seller.product.imagepicker.model.CatalogImage;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class CatalogImageMapper implements Func1<List<CatalogImage>, Observable<List<CatalogModelView>>> {
    @Inject
    public CatalogImageMapper() {
    }

    @Override
    public Observable<List<CatalogModelView>> call(List<CatalogImage> catalogImages) {
        return null;
    }
}
