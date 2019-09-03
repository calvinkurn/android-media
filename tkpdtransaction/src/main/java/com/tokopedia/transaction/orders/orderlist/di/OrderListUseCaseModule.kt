package com.tokopedia.transaction.orders.orderlist.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.transaction.R
import com.tokopedia.transaction.orders.orderlist.view.presenter.OrderListPresenterImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class OrderListUseCaseModule {

    @Provides
    @OrderListModuleScope
    fun providesContexts(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @OrderListModuleScope
    fun provideUserSessionInterface(context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @OrderListModuleScope
    fun providesGraphqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @Provides
    @OrderListModuleScope
    @Named("recommendationQuery")
    fun provideRecommendationRawQuery(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_recommendation_widget)
    }

    @Provides
    @OrderListModuleScope
    fun provideGetRecommendationUseCase(@Named("recommendationQuery") recomQuery: String,
                                        graphqlUseCase: GraphqlUseCase,
                                        userSessionInterface: UserSessionInterface): GetRecommendationUseCase {
        return GetRecommendationUseCase(recomQuery, graphqlUseCase, userSessionInterface)
    }

    @Provides
    @OrderListModuleScope
    @Named("atcMutation")
    fun provideAddToCartMutation(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_to_cart)
    }

    @Provides
    @OrderListModuleScope
    fun providesOrderListPresenterImpl(getRecommendationUseCase: GetRecommendationUseCase, addToCartUseCase: AddToCartUseCase): OrderListPresenterImpl {
        return OrderListPresenterImpl(getRecommendationUseCase, addToCartUseCase)
    }

}