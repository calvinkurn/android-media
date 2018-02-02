package com.tokopedia.home.beranda.domain.interactor;

import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.home.beranda.data.repository.HomeRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class GetTickerUseCase extends UseCase<Ticker> {

    private final HomeRepository repository;

    public GetTickerUseCase(HomeRepository homeRepository) {
        this.repository = homeRepository;
    }

    @Override
    public Observable<Ticker> createObservable(RequestParams requestParams) {
        return repository.getTickers();
    }
}
