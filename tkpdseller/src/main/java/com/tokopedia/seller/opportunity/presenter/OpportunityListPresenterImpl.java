package com.tokopedia.seller.opportunity.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.model.PagingHandler;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.data.factory.ActionReplacementSourceFactory;
import com.tokopedia.seller.opportunity.data.factory.OpportunityDataSourceFactory;
import com.tokopedia.seller.opportunity.data.repository.ReplacementRepositoryImpl;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityFilterUseCase;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityUseCase;
import com.tokopedia.seller.opportunity.domain.interactor.OpportunityListUseCase;
import com.tokopedia.seller.opportunity.listener.OpportunityListView;
import com.tokopedia.seller.opportunity.domain.param.GetOpportunityListParam;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityListPageViewModel;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nisie on 3/2/17.
 */

public class OpportunityListPresenterImpl implements OpportunityListPresenter {

    public static final String KEY_SORT = "OPPORTUNITY_SORT_CACHE";
    public static final String KEY_CATEGORY = "OPPORTUNITY_CATEGORY_CACHE";
    public static final String KEY_SHIPPING_TYPE = "OPPORTUNITY_SHIPPING_TYPE_CACHE";

    private final OpportunityListView viewListener;
    private PagingHandler pagingHandler;

    private OpportunityListUseCase opportunityListUseCase;

    private GetOpportunityListParam opportunityParam;

    public OpportunityListPresenterImpl(OpportunityListView viewListener) {
        this.viewListener = viewListener;
        this.pagingHandler = new PagingHandler();
        this.opportunityParam = new GetOpportunityListParam();

        ReplacementRepositoryImpl repository = new ReplacementRepositoryImpl(
                new ActionReplacementSourceFactory(viewListener.getActivity()),
                new OpportunityDataSourceFactory(viewListener.getActivity())
        );
        this.opportunityListUseCase = new OpportunityListUseCase(
                new JobExecutor(), new UIThread(), repository,
                new GetOpportunityUseCase(new JobExecutor(), new UIThread(), repository),
                new GetOpportunityFilterUseCase(new JobExecutor(), new UIThread(), repository)
        );
    }

    @Override
    public void getOpportunity() {
        viewListener.showLoadingList();
        CommonUtils.dumper("NISNIS " + getOpportunityParam().toString());

        opportunityListUseCase.execute(getOpportunityParam(),
                new Subscriber<OpportunityListPageViewModel>() {
                    @Override
                    public void onCompleted() {
                        CommonUtils.dumper("NISNIS onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            viewListener.onErrorGetOpportunity(viewListener.getString(R.string.msg_no_connection));
                        } else if (e instanceof SocketTimeoutException) {
                            viewListener.onErrorGetOpportunity(viewListener.getString(R.string.default_request_error_timeout));
                        } else if (e instanceof IOException) {
                            viewListener.onErrorGetOpportunity(viewListener.getString(R.string.default_request_error_internal_server));
                        } else if (e.getLocalizedMessage() != null
                                && e instanceof ErrorMessageException) {
                            viewListener.onErrorGetOpportunity(e.getLocalizedMessage());
                        } else if (e instanceof RuntimeException
                                && e.getLocalizedMessage() != null &&
                                e.getLocalizedMessage().length() <= 3) {
                            new ErrorHandler(new ErrorListener() {
                                @Override
                                public void onUnknown() {
                                    viewListener.onErrorGetOpportunity(viewListener.getString(R.string.default_request_error_unknown));
                                }

                                @Override
                                public void onTimeout() {
                                    viewListener.onErrorGetOpportunity(viewListener.getString(R.string.default_request_error_timeout));

                                }

                                @Override
                                public void onServerError() {
                                    viewListener.onErrorGetOpportunity(viewListener.getString(R.string.default_request_error_internal_server));

                                }

                                @Override
                                public void onBadRequest() {
                                    viewListener.onErrorGetOpportunity(viewListener.getString(R.string.default_request_error_bad_request));

                                }

                                @Override
                                public void onForbidden() {
                                    viewListener.onErrorGetOpportunity(viewListener.getString(R.string.default_request_error_forbidden_auth));

                                }
                            }, Integer.parseInt(e.toString()));
                        } else {
                            viewListener.onErrorGetOpportunity(viewListener.getString(R.string.default_request_error_unknown));
                        }
                    }

                    @Override
                    public void onNext(OpportunityListPageViewModel viewModel) {
                        if (pagingHandler.getPage() == 1) {
                            viewListener.getAdapter().getList().clear();
                        }
                        pagingHandler.setHasNext(checkHasNext(viewModel.getOpportunityViewModel().getPagingHandlerModel()));
                        viewListener.onSuccessGetOpportunity(viewModel);

                    }
                });

    }

    private boolean checkHasNext(PagingHandler.PagingHandlerModel pagingHandlerModel) {
        return !pagingHandlerModel.getUriNext().equals("0") && !pagingHandlerModel.getUriNext().equals("");
    }

    private RequestParams getOpportunityParam() {
        RequestParams param = RequestParams.create();
        param.putString("page", String.valueOf(pagingHandler.getPage()));
        param.putString("per_page", "10");
        if (opportunityParam.getSort() != null)
            param.putString("order_by", opportunityParam.getSort());
        if (opportunityParam.getShippingType() != null)
            param.putString("ship_type", opportunityParam.getShippingType());
        if (opportunityParam.getCategory() != null)
            param.putString("cat1", opportunityParam.getCategory());
        if (opportunityParam.getQuery() != null)
            param.putString("query", opportunityParam.getQuery());
        param.putString("user_id", SessionHandler.getLoginID(viewListener.getActivity()));
        param.putString("device_id", GCMHandler.getRegistrationId(viewListener.getActivity()));
        param.putString("os_type", "1");
        return param;
    }

    @Override
    public void loadMore() {
        if (hasNextPage()) {
            pagingHandler.nextPage();
            getOpportunity();
        }
    }

    @Override
    public void onRefresh() {
        pagingHandler.resetPage();
        getOpportunity();
    }

    @Override
    public void onDestroyView() {
        opportunityListUseCase.unsubscribe();
    }

    @Override
    public void setParamQuery(String query) {
        opportunityParam.setQuery(query);
    }

    @Override
    public void setParamSort(String sortParam) {
        opportunityParam.setSort(sortParam);
    }

    @Override
    public void setParamCategory(String categoryParam) {
        opportunityParam.setCategory(categoryParam);
    }

    @Override
    public void getParamShippingType(String shippingParam) {
        opportunityParam.setShippingType(shippingParam);
    }

    @Override
    public GetOpportunityListParam getPass() {
        return opportunityParam;
    }

    private boolean hasNextPage() {
        return pagingHandler.CheckNextPage();
    }


}
