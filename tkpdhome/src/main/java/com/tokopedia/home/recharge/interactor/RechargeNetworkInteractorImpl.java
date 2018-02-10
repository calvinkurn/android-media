package com.tokopedia.home.recharge.interactor;

import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;
import com.tokopedia.digital.widget.view.model.category.Category;
import com.tokopedia.digital.widget.view.model.mapper.CategoryMapper;
import com.tokopedia.digital.widget.view.model.mapper.StatusMapper;
import com.tokopedia.digital.widget.view.model.status.Status;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author ricoharisin on 7/11/16.
 *         Modified by kulomady on 08/23/2016
 *         Modified by Nabilla Sabbaha on 08/07/2017
 *         Modified by rizkyfadillah at 10/6/17.
 */

// TODO: this interactor should be replaced with usecase
// don't use DigitalWidgetRepository anymore

@Deprecated
public class RechargeNetworkInteractorImpl implements RechargeNetworkInteractor {

    private CompositeSubscription compositeSubscription;
    private DigitalWidgetRepository repository;
    private CategoryMapper categoryMapper;
    private StatusMapper statusMapper;

    public RechargeNetworkInteractorImpl(DigitalWidgetRepository repository,
                                         CategoryMapper categoryMapper,
                                         StatusMapper statusMapper) {
        compositeSubscription = new CompositeSubscription();
        this.repository = repository;
        this.categoryMapper = categoryMapper;
        this.statusMapper = statusMapper;
    }

    @Override
    public void getCategoryData(Subscriber<List<Category>> subscriber) {
        compositeSubscription.add(
                repository.getObservableCategoryData()
                        .map(categoryMapper)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber));
    }

    public void getStatus(Subscriber<Status> subscriber) {
        compositeSubscription.add(
                repository.getObservableStatus()
                        .map(statusMapper)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber));
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription != null)
            compositeSubscription.unsubscribe();
    }
}