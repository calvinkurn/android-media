package com.tokopedia.seller.product.draft.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.product.manage.common.draft.data.db.AddEditProductDraftDao;
import com.tokopedia.product.manage.common.draft.data.db.AddEditProductDraftDb;
import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository;
import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepositoryImpl;
import com.tokopedia.product.manage.common.draft.data.db.source.AddEditProductDraftDataSource;
import com.tokopedia.seller.manageitem.data.db.ProductDraftDB;
import com.tokopedia.seller.manageitem.data.db.ProductDraftDao;
import com.tokopedia.seller.manageitem.data.source.ProductDraftDataSource;
import com.tokopedia.seller.manageitem.di.module.ProductAddModule;
import com.tokopedia.seller.manageitem.di.scope.ProductAddScope;
import com.tokopedia.seller.manageitem.domain.repository.ProductDraftRepository;
import com.tokopedia.seller.manageitem.domain.repository.ProductDraftRepositoryImpl;
import com.tokopedia.seller.manageitem.domain.usecase.UpdateUploadingDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductLegacyUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.DeleteSingleDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.FetchAllDraftProductLegacyUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.FetchAllDraftProductUseCase;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListPresenter;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListPresenterImpl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by User on 6/21/2017.
 */
@ProductAddScope
@Module
public class ProductDraftListModule extends ProductAddModule {

    private final Context context;

    public ProductDraftListModule(Context context) {
        this.context = context;
    }

    @ProductAddScope
    @ApplicationContext
    @Provides
    Context provideApplicationContext() {
        return context;
    }

    @ProductAddScope
    @Provides
    ProductDraftListPresenter providePresenterDraft(FetchAllDraftProductUseCase fetchAllDraftProductUseCase,
                                                    FetchAllDraftProductLegacyUseCase fetchAllDraftProductLegacyUseCase,
                                                    DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase,
                                                    UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase,
                                                    ClearAllDraftProductUseCase clearAllDraftProductUseCase,
                                                    ClearAllDraftProductLegacyUseCase clearAllDraftProductLegacyUseCase) {
        return new ProductDraftListPresenterImpl(
                fetchAllDraftProductUseCase,
                fetchAllDraftProductLegacyUseCase,
                deleteSingleDraftProductUseCase,
                updateUploadingDraftProductUseCase,
                clearAllDraftProductUseCase,
                clearAllDraftProductLegacyUseCase);
    }

    // this is for product_manage_item
    @ProductAddScope
    @Provides
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource productDraftDataSource, @ApplicationContext Context context) {
        return new ProductDraftRepositoryImpl(productDraftDataSource, context);
    }

    // this is for product_manage_item
    @ProductAddScope
    @Provides
    ProductDraftDB provideProductDraftDb(@ApplicationContext Context context) {
        return ProductDraftDB.getInstance(context);
    }

    // this is for product_manage_item
    @ProductAddScope
    @Provides
    ProductDraftDao provideProductDraftDao(ProductDraftDB productDraftDB) {
        return productDraftDB.getProductDraftDao();
    }

    @ProductAddScope
    @Provides
    UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @ProductAddScope
    @Provides
    AddEditProductDraftDb provideAddEditProductDraftDb(@ApplicationContext Context context) {
        return AddEditProductDraftDb.getInstance(context);
    }

    @ProductAddScope
    @Provides
    AddEditProductDraftDao provideAddEditProductDraftDao(AddEditProductDraftDb draftDb) {
        return draftDb.getDraftDao();
    }

    @ProductAddScope
    @Provides
    AddEditProductDraftRepository provideAddEditProductDraftRepository(
            AddEditProductDraftDataSource draftDataSource,
            UserSessionInterface userSession
    ) {
        return new AddEditProductDraftRepositoryImpl(draftDataSource, userSession);
    }
}

