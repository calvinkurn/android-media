package com.tokopedia.seller.manageitem.di.module;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.common.category.data.repository.CategoryRepositoryImpl;
import com.tokopedia.core.common.category.data.source.CategoryDataSource;
import com.tokopedia.core.common.category.data.source.FetchCategoryDataSource;
import com.tokopedia.core.common.category.data.source.db.CategoryDB;
import com.tokopedia.core.common.category.data.source.db.CategoryDao;
import com.tokopedia.core.common.category.domain.CategoryRepository;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.seller.manageitem.di.scope.ProductAddScope;
import com.tokopedia.shop.common.di.ShopCommonModule;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ProductAddScope
@Module(includes = ShopCommonModule.class)
public class ProductAddModule {

    protected final Context context;

    public ProductAddModule(Context context) {
        this.context = context;
    }

    @ApplicationContext
    @Provides
    Context context(@ApplicationContext Context context){
        return context;
    }

    @ProductAddScope
    @Provides
    AbstractionRouter abstractionRouter(@ApplicationContext Context context){
        if(context instanceof AbstractionRouter){
            return (AbstractionRouter) context;
        }
        return null;
    }

    @ProductAddScope
    @Provides
    HttpLoggingInterceptor httpLoggingInterceptor(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return logging;
    }

    @ProductAddScope
    @Provides
    CategoryDao provideCategoryDao(@ApplicationContext Context context){
        return CategoryDB.getInstance(context).getCategoryDao();
    }

    @ProductAddScope
    @Provides
    CategoryRepository provideCategoryRepository(CategoryDataSource categoryDataSource,
                                                 FetchCategoryDataSource fetchCategoryDataSource){
        return new CategoryRepositoryImpl(categoryDataSource, fetchCategoryDataSource);
    }

    @ProductAddScope
    @Provides
    MultiRequestGraphqlUseCase provideMultiRequestGraphqlUseCase() {
        return GraphqlInteractor.getInstance().getMultiRequestGraphqlUseCase();
    }

    @ProductAddScope
    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
