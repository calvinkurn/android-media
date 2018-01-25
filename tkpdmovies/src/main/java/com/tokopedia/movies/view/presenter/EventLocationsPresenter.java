package com.tokopedia.movies.view.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.movies.domain.GetEventsLocationListRequestUseCase;
import com.tokopedia.movies.domain.model.EventLocationDomain;
import com.tokopedia.movies.view.contractor.EventsLocationContract;
import com.tokopedia.movies.view.viewmodel.EventLocationViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class EventLocationsPresenter extends BaseDaggerPresenter<EventsLocationContract.View> implements EventsLocationContract.Presenter {

    public GetEventsLocationListRequestUseCase getEventsLocationListRequestUseCase;

    @Inject
    public EventLocationsPresenter(GetEventsLocationListRequestUseCase getEventsLocationListRequestUseCase) {
        this.getEventsLocationListRequestUseCase = getEventsLocationListRequestUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {

    }

    public void getLocationsListList() {

        getEventsLocationListRequestUseCase.execute(getView().getParams(), new Subscriber<List<EventLocationDomain>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
            }

            @Override
            public void onNext(List<EventLocationDomain> eventLocationDomains) {
                getView().renderLocationList(convertIntoVeiwModel(eventLocationDomains));
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    private List<EventLocationViewModel> convertIntoVeiwModel(List<EventLocationDomain> eventLocationDomains) {
        List<EventLocationViewModel> eventLocationViewModels = new ArrayList<>();
        EventLocationViewModel eventLocationViewModel;
        if (eventLocationDomains != null) {
            for (EventLocationDomain moviesCategoryDomain : eventLocationDomains) {
                eventLocationViewModel =new EventLocationViewModel();
                eventLocationViewModel.setId(moviesCategoryDomain.getId());
                eventLocationViewModel.setName(moviesCategoryDomain.getName());
                eventLocationViewModel.setSearchName(moviesCategoryDomain.getSearchName());
                eventLocationViewModel.setDistrict(moviesCategoryDomain.getDistrict());
                eventLocationViewModel.setCategoryId(moviesCategoryDomain.getCategoryId());
                eventLocationViewModel.setIcon(moviesCategoryDomain.getIcon());

                eventLocationViewModels.add(eventLocationViewModel);
            }
        }
        return eventLocationViewModels;
    }

}
