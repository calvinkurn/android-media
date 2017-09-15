package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.dashboard.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.topads.dashboard.data.model.data.Cell;
import com.tokopedia.topads.dashboard.data.model.data.DataStatistic;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.request.StatisticRequest;
import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zulfikarrahman on 12/30/16.
 * Created by Nathaniel on 12/29/2016.
 */

public class TopAdsProductAdInteractorImpl implements TopAdsProductAdInteractor {

    private CompositeSubscription compositeSubscription;
    private TopAdsManagementService topAdsManagementService;
    private TopAdsCacheDataSourceImpl topAdsCacheDataSourceImpl;

    public TopAdsProductAdInteractorImpl(TopAdsManagementService topAdsManagementService, TopAdsCacheDataSourceImpl topAdsCacheDataSourceImpl) {
        this.compositeSubscription = new CompositeSubscription();
        this.topAdsManagementService = topAdsManagementService;
        this.topAdsCacheDataSourceImpl = topAdsCacheDataSourceImpl;
    }

    @Override
    public void searchAd(SearchAdRequest searchAdRequest, final ListenerInteractor<PageDataResponse<List<ProductAd>>> listener) {
        Observable<Response<PageDataResponse<List<ProductAd>>>> observable = topAdsManagementService.getApi().getProductAd(searchAdRequest.getParams());
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<PageDataResponse<List<ProductAd>>>, Observable<PageDataResponse<List<ProductAd>>>>() {
                    @Override
                    public Observable<PageDataResponse<List<ProductAd>>> call(Response<PageDataResponse<List<ProductAd>>> response) {
                        return Observable.just(response.body());
                    }
                })
                .subscribe(new SubscribeOnNext<PageDataResponse<List<ProductAd>>>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void bulkAction(DataRequest<ProductAdBulkAction> bulkActionDataRequest, ListenerInteractor<ProductAdBulkAction> listener) {
        Observable<Response<DataResponse<ProductAdBulkAction>>> observable = topAdsManagementService.getApi().bulkActionProductAd(bulkActionDataRequest);
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<DataResponse<ProductAdBulkAction>>, Observable<ProductAdBulkAction>>() {
                    @Override
                    public Observable<ProductAdBulkAction> call(Response<DataResponse<ProductAdBulkAction>> response) {
                        return Observable.just(response.body().getData());
                    }
                })
                .subscribe(new SubscribeOnNext<ProductAdBulkAction>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void getDetailProductAd(ListenerInteractor<List<ProductAd>> listenerInteractor) {

    }

    @Override
    public void getStatistic(final StatisticRequest statisticRequest, final ListenerInteractor<List<Cell>> listener) {
        Observable<Response<DataResponse<DataStatistic>>> statisticApiObservable = topAdsManagementService.getApi().getDashboardStatistic(statisticRequest.getParams());
        compositeSubscription.add(statisticApiObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Response<DataResponse<DataStatistic>>, Observable<List<Cell>>>() {
                    @Override
                    public Observable<List<Cell>> call(Response<DataResponse<DataStatistic>> dataResponseResponse) {
                        final List<Cell> cellsResponse = dataResponseResponse.body().getData().getCells();
                        return Observable.just(cellsResponse);
                    }
                })
                .subscribe(new SubscribeOnNext<List<Cell>>(listener), new SubscribeOnError(listener)));
    }

    /**
     * generate range date between date start request and last date request
     *
     * @return
     */
    public Observable<List<Cell>> getDefaultCellList(final Date startDate, final Date endDate) {
        return Observable.create(
                new Observable.OnSubscribe<List<Cell>>() {
                    @Override
                    public void call(Subscriber<? super List<Cell>> subscriber) {
                        List<Cell> cellList = new ArrayList<>();
                        Calendar start = Calendar.getInstance();
                        start.setTime(startDate);
                        Calendar end = Calendar.getInstance();
                        end.setTime(endDate);
                        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            Cell cell = new Cell();
                            cell.setDateDay(calendar.get(Calendar.DATE));
                            cell.setDateMonth(calendar.get(Calendar.MONTH));
                            cell.setDateYear(calendar.get(Calendar.YEAR));
                            cellList.add(cell);
                        }
                        subscriber.onNext(cellList);
                    }
                }
        );
    }

    private Observable<List<Cell>> getCombinedCellList(final List<Cell> cellList, final List<Cell> networkCellList) {
        return Observable.create(
                new Observable.OnSubscribe<List<Cell>>() {
                    @Override
                    public void call(Subscriber<? super List<Cell>> subscriber) {
                        for (int i = 0; i < cellList.size(); i++) {
                            Cell cell = cellList.get(i);
                            Cell networkCell = getSameCell(cell, networkCellList);
                            if (networkCell != null) {
                                cellList.set(i, networkCell);
                            }
                        }
                        subscriber.onNext(cellList);
                    }

                    private Cell getSameCell(Cell cell, List<Cell> networkCellList) {
                        for (Cell networkCell : networkCellList) {
                            if (cell.getDateDay() == networkCell.getDateDay() && cell.getDateMonth() == networkCell.getDateMonth() && cell.getDateYear() == networkCell.getDateYear()) {
                                return networkCell;
                            }
                        }
                        return null;
                    }
                }
        );
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }
}