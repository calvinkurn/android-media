package com.tokopedia.discovery.newdiscovery.hotlistRevamp.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery.categoryrevamp.domain.usecase.*
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.domain.usecases.HotlistDetailUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named


@HotlistNavScope
@Module
class HotlistNavUseCaseModule {

    @HotlistNavScope
    @Provides
    fun providesContext(@ApplicationContext context: Context): Context {
        return context
    }

    @HotlistNavScope
    @Named("productGqlUseCaseObject")
    @Provides
    fun provideRxGQLProductUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @HotlistNavScope
    @Provides
    fun getHotListDetailUseCase(context: Context): HotlistDetailUseCase {
        return HotlistDetailUseCase(context)
    }

    @HotlistNavScope
    @Provides
    fun provideCategoryProductUseCase(context: Context,
                                      @Named("productGqlUseCaseObject") graphqlUseCase: GraphqlUseCase):
            CategoryProductUseCase {
        return CategoryProductUseCase(context, graphqlUseCase)
    }

    @HotlistNavScope
    @Provides
    fun provideTopAdsUseCase(context: Context): TopAdsProductsUseCase {
        return TopAdsProductsUseCase(context)
    }

    @HotlistNavScope
    @Provides
    fun getProductListUseCase(categoryProductUseCase: CategoryProductUseCase
                              , topAdsProductsUseCase
                              : TopAdsProductsUseCase)
            : GetProductListUseCase {
        return GetProductListUseCase(categoryProductUseCase, topAdsProductsUseCase)
    }

    @HotlistNavScope
    @Provides
    fun provideDynamicFilterUseCase(context: Context): DynamicFilterUseCase {
        return DynamicFilterUseCase(context)
    }

    @HotlistNavScope
    @Provides
    fun provideQuickFilterUseCase(context: Context): QuickFilterUseCase {
        return QuickFilterUseCase(context)
    }

    @HotlistNavScope
    @Provides
    fun getAddWishListUseCase(context: Context)
            : AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @HotlistNavScope
    @Provides
    fun getRemoveWishListUseCase(context: Context)
            : RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

}