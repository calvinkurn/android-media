package com.tokopedia.profilecompletion.di

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.core.base.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.profilecompletion.data.ProfileCompletionQueriesConstant
import com.tokopedia.profilecompletion.data.pojo.StatusPinPojo
import com.tokopedia.session.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * Created by Ade Fulki on 2019-09-12.
 * ade.hadian@tokopedia.com
 */

@ProfileCompletionScope
@Module
class ProfileCompletionQueriesModule {

    @Provides
    @IntoMap
    @StringKey(ProfileCompletionQueriesConstant.QUERY_STATUS_PIN)
    fun provideRawQueryStatusPin(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_status_pin)


    @Provides
    fun provideStatusPinGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<StatusPinPojo> = GraphqlUseCase(graphqlRepository)
}