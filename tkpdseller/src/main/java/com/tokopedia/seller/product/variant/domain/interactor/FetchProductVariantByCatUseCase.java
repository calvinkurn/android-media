package com.tokopedia.seller.product.variant.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.repository.ProductVariantRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author hendry on 4/28/17.
 * {"status":"OK","data":[{"variant_id":1,"name":"Warna","identifier":"colour","status":2,"has_unit":0,"units":[{"unit_id":0,"name":"","short_name":"","values":[{"value_id":1,"value":"Putih","hex_code":"#ffffff","icon":""},{"value_id":2,"value":"Hitam","hex_code":"#000000","icon":""}]}]},{"variant_id":6,"name":"Ukuran Pakaian","identifier":"size","status":1,"has_unit":1,"units":[{"unit_id":7,"name":"International","short_name":"Intl","values":[{"value_id":22,"value":"XXS","hex_code":"","icon":""},{"value_id":23,"value":"XS","hex_code":"","icon":""}]},{"unit_id":8,"name":"US","short_name":"US","values":[{"value_id":29,"value":"0","hex_code":"","icon":""},{"value_id":30,"value":"2","hex_code":"","icon":""}]},{"unit_id":9,"name":"UK","short_name":"UK","values":[{"value_id":36,"value":"4","hex_code":"","icon":""}]}]}]}
 * warna - no unit (merah, etc); ukuran - Inter (XS, S), UK (29, 30), US(36, etc)
 */

public class FetchProductVariantByCatUseCase extends UseCase<List<ProductVariantByCatModel>> {
    private static final int UNSELECTED = -2;
    private static final String CATEGORY_ID = "cat_id";

    private final ProductVariantRepository productVariantRepository;

    @Inject
    public FetchProductVariantByCatUseCase(ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread,
                                           ProductVariantRepository productVariantRepository) {
        super(threadExecutor, postExecutionThread);
        this.productVariantRepository = productVariantRepository;
    }

    @Override
    public final Observable<List<ProductVariantByCatModel>> createObservable(RequestParams requestParams) {
        return productVariantRepository.fetchProductVariantByCat(requestParams.getLong(CATEGORY_ID, UNSELECTED));
    }

    public static RequestParams generateParam(long categoryId) {
        RequestParams requestParam = RequestParams.create();
        requestParam.putLong(CATEGORY_ID, categoryId);
        return requestParam;
    }
}
