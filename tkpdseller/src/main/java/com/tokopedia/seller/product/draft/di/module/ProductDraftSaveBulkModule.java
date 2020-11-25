package com.tokopedia.seller.product.draft.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.product.manage.common.draft.data.db.AddEditProductDraftDao;
import com.tokopedia.product.manage.common.draft.data.db.AddEditProductDraftDb;
import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepository;
import com.tokopedia.product.manage.common.draft.data.db.repository.AddEditProductDraftRepositoryImpl;
import com.tokopedia.product.manage.common.draft.data.db.source.AddEditProductDraftDataSource;
import com.tokopedia.seller.manageitem.di.module.ProductAddModule;
import com.tokopedia.seller.manageitem.di.scope.ProductAddScope;
import com.tokopedia.seller.product.draft.domain.interactor.SaveInstagramToProductDraftUseCase;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftSaveBulkPresenter;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftSaveBulkPresenterImpl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by User on 6/21/2017.
 */
@ProductAddScope
@Module
public class ProductDraftSaveBulkModule extends ProductAddModule {

    @ProductAddScope
    @Provides
    ProductDraftSaveBulkPresenter provideProductDraftSaveBulkPresenter(SaveInstagramToProductDraftUseCase saveInstagramToProductDraftUseCase){
        return new ProductDraftSaveBulkPresenterImpl(saveInstagramToProductDraftUseCase);
    }

    //this is for seller app
    @ProductAddScope
    @Provides
    UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    //this is for seller app
    @ProductAddScope
    @Provides
    AddEditProductDraftDb provideAddEditProductDraftDb(@ApplicationContext Context context) {
        return AddEditProductDraftDb.getInstance(context);
    }

    //this is for seller app
    @ProductAddScope
    @Provides
    AddEditProductDraftDao provideAddEditProductDraftDao(AddEditProductDraftDb draftDb) {
        return draftDb.getDraftDao();
    }

    //this is for seller app
    @ProductAddScope
    @Provides
    AddEditProductDraftRepository provideAddEditProductDraftRepository(
            AddEditProductDraftDataSource draftDataSource,
            UserSessionInterface userSession
    ) {
        return new AddEditProductDraftRepositoryImpl(draftDataSource, userSession);
    }
}

