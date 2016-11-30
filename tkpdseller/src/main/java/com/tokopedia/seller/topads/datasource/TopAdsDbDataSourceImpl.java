package com.tokopedia.seller.topads.datasource;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.seller.topads.model.data.Cell;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.data.Summary_Table;
import com.tokopedia.seller.topads.model.exchange.StatisticRequest;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public class TopAdsDbDataSourceImpl implements TopAdsDbDataSource {

    @Override
    public Observable<Summary> insertSummary(final StatisticRequest statisticRequest, final Summary summary) {
        return Observable.create(
                new Observable.OnSubscribe<Summary>() {
                    @Override
                    public void call(Subscriber<? super Summary> subscriber) {
                        summary.setType(statisticRequest.getType());
                        summary.setShopId(statisticRequest.getShopId());
                        summary.setStartDate(statisticRequest.getFormattedStartDate());
                        summary.setEndDate(statisticRequest.getFormattedEndDate());
                        summary.save();
                        subscriber.onNext(summary);
                    }
                }
        );
    }

    @Override
    public Observable<Summary> getSummary(final StatisticRequest statisticRequest) {
        return Observable.create(
                new Observable.OnSubscribe<Summary>() {
                    @Override
                    public void call(Subscriber<? super Summary> subscriber) {
                        Summary summary = new Select().from(Summary.class)
                                .where(Summary_Table.shopId.is(statisticRequest.getShopId()))
                                .and(Summary_Table.type.is(statisticRequest.getType()))
                                .and(Summary_Table.startDate.is(statisticRequest.getFormattedStartDate()))
                                .and(Summary_Table.endDate.is(statisticRequest.getFormattedEndDate()))
                                .querySingle();
                        subscriber.onNext(summary);
                    }
                }
        );
    }

    @Override
    public Observable<List<Cell>> insertCellList(StatisticRequest statisticRequest, final List<Cell> cellList) {
        return Observable.create(
                new Observable.OnSubscribe<List<Cell>>() {
                    @Override
                    public void call(Subscriber<? super List<Cell>> subscriber) {
                        for (Cell cell : cellList) {

                        }
                        subscriber.onNext(cellList);
                    }
                }
        );
    }

    @Override
    public Observable<Void> deleteStatisticData() {
        return Observable.create(
                new Observable.OnSubscribe<Void>() {
                    @Override
                    public void call(Subscriber<? super Void> subscriber) {
                        new Delete().from(Summary.class).execute();
                        subscriber.onNext(null);
                    }
                }
        );
    }
}
