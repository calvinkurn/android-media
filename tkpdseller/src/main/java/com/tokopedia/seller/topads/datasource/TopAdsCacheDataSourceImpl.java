package com.tokopedia.seller.topads.datasource;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.data.Summary_Table;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public class TopAdsCacheDataSourceImpl implements TopAdsCacheDataSource {

    @Override
    public Observable<Void> insertSummary(String shopId, int type, Date startDate, Date endDate, final Summary summary) {
        return Observable.create(
                new Observable.OnSubscribe<Void>() {
                    @Override
                    public void call(Subscriber<? super Void> subscriber) {
                        summary.save();
                        subscriber.onCompleted();
                    }
                }
        );
    }

    @Override
    public Observable<Summary> getSummary(final String shopId, final int type, final Date startDate, final Date endDate) {
        return Observable.create(
                new Observable.OnSubscribe<Summary>() {
                    @Override
                    public void call(Subscriber<? super Summary> subscriber) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat(TopAdsConstant.DATE_FORMAT, Locale.ENGLISH);
                        Summary summary = new Select().from(Summary.class)
                                .where(Summary_Table.shopId.is(shopId))
                                .and(Summary_Table.type.is(type))
                                .and(Summary_Table.startDate.is(dateFormat.format(startDate)))
                                .and(Summary_Table.endDate.is(dateFormat.format(endDate)))
                                .querySingle();
                        subscriber.onNext(summary);
                    }
                }
        );
    }
}
