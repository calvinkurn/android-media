package com.tokopedia.discovery.newdiscovery.domain.usecase

import android.content.Context

import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery.newdiscovery.domain.gql.SearchProfileListGqlResponse
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileViewModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.discovery.R
import com.tokopedia.discovery.newdiscovery.helper.UrlParamHelper
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileListViewModel

import java.util.ArrayList
import java.util.HashMap

import rx.Observable

open class GetProfileListUseCase(private val context: Context,
                                 private val graphqlUseCase: GraphqlUseCase) : UseCase<ProfileListViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<ProfileListViewModel>? {
        val variables = HashMap<String, Any>()
        variables[KEY_PARAMS] = UrlParamHelper.generateUrlParamString(requestParams.paramsAllValueInString)

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context!!.getResources(),
                R.raw.gql_search_profile), SearchProfileListGqlResponse::class.java, variables)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map { graphqlResponse ->
                    val profileListGqlResponse = graphqlResponse.getData<SearchProfileListGqlResponse>(SearchProfileListGqlResponse::class.java)
                    val startRow = (requestParams.getString(KEY_START,"0").toInt())+1
                    ProfileListViewModel(
                            convertToProfileListViewModel(profileListGqlResponse,
                                    startRow
                                    ),
                            profileListGqlResponse?.aceSearchProfile?.hasNext?:false,
                            profileListGqlResponse.aceSearchProfile.count
                    )
                }
    }

    private fun convertToProfileListViewModel(
            gqlResponse: SearchProfileListGqlResponse,
            startRow : Int): List<ProfileViewModel> {
        val profileListGqlResponse = gqlResponse.aceSearchProfile

        val profileListViewModel = ArrayList<ProfileViewModel>()
        var position = startRow
        for (item in profileListGqlResponse.profiles!!) {
            val profileViewModel = ProfileViewModel(
                    item.id,
                    item.name,
                    item.avatar,
                    item.username,
                    item.bio,
                    item.followed,
                    item.iskol,
                    item.isaffiliate,
                    item.following,
                    item.followers,
                    item.postCount,
                    position++
            )
            profileListViewModel.add(profileViewModel)
        }

        return profileListViewModel
    }

    companion object {
        val KEY_QUERY = "q"
        val KEY_DEVICE = "device"
        val KEY_SOURCE = "source"
        val KEY_ROWS = "rows"
        val KEY_START = "start"

        val KEY_MOBILE = "mobile"
        val KEY_SEARCH = "search"

        val VALUE_ROWS_PER_PAGE = 12
        val KEY_PARAMS = "params"


        fun createRequestParams(query: String,
                                page: Int): RequestParams {

            val requestParams = RequestParams.create()
            val startRows : Int = (page - 1) * VALUE_ROWS_PER_PAGE

            requestParams.putString(KEY_QUERY, query)
            requestParams.putString(KEY_DEVICE, KEY_MOBILE)
            requestParams.putString(KEY_SOURCE, KEY_SEARCH)
            requestParams.putString(KEY_ROWS, VALUE_ROWS_PER_PAGE.toString())

            requestParams.putString(KEY_START, startRows.toString())
            return requestParams
        }
    }
}
