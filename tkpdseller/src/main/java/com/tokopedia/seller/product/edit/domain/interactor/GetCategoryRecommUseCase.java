package com.tokopedia.seller.product.edit.domain.interactor;

import com.tokopedia.seller.product.edit.data.mapper.CategoryRecommDataToDomainMapper;
import com.tokopedia.seller.product.edit.domain.CategoryRecommRepository;
import com.tokopedia.seller.product.edit.domain.model.CategoryRecommDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class GetCategoryRecommUseCase extends UseCase<CategoryRecommDomainModel> {

    public static final String TITLE = "title";
    public static final String ROW = "row";
    private final CategoryRecommRepository categoryRecommRepository;
    private final CategoryRecommDataToDomainMapper categoryRecommDataToDomainMapper;

    @Inject
    public GetCategoryRecommUseCase(CategoryRecommRepository categoryRecommRepository,
                                    CategoryRecommDataToDomainMapper categoryRecommDataToDomainMapper) {
        this.categoryRecommRepository = categoryRecommRepository;
        this.categoryRecommDataToDomainMapper = categoryRecommDataToDomainMapper;
    }

    public static RequestParams createRequestParams(String productTitle, int expectRow) {
        RequestParams params = RequestParams.create();
        params.putString(TITLE, productTitle);
        params.putInt(ROW, expectRow);

        return params;
    }

    @Override
    public Observable<CategoryRecommDomainModel> createObservable(RequestParams requestParams) {
        String title = requestParams.getString(TITLE, "");
        int row = requestParams.getInt(ROW, 0);
        return categoryRecommRepository.fetchCategoryRecomm(title, row).map(categoryRecommDataToDomainMapper);
    }

}
