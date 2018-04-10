package com.tokopedia.events.view.presenter;

import android.content.Intent;

import com.tkpd.library.ui.widget.TouchViewPager;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.events.R;
import com.tokopedia.events.domain.GetEventsListByLocationRequestUseCase;
import com.tokopedia.events.domain.GetEventsListRequestUseCase;
import com.tokopedia.events.domain.GetSearchEventsListRequestUseCase;
import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.activity.EventSearchActivity;
import com.tokopedia.events.view.activity.EventsHomeActivity;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;
import com.tokopedia.events.view.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class EventHomePresenter extends BaseDaggerPresenter<EventsContract.View>
        implements EventsContract.Presenter {

    private GetEventsListRequestUseCase getEventsListRequestUsecase;
    private GetEventsListByLocationRequestUseCase getEventsListByLocationRequestUseCase;
    private CategoryViewModel carousel;
    private List<CategoryViewModel> categoryViewModels;
    private TouchViewPager mTouchViewPager;
    private int currentPage, totalPages;
    private String PROMOURL = "https://www.tokopedia.com/promo/tiket/events/";
    private String FAQURL = "https://www.tokopedia.com/bantuan/faq-tiket-event/";
    private String TRANSATIONSURL = "https://pulsa.tokopedia.com/order-list/";
    private String SCREEN_NAME = "Digital_Events_Home";

    @Inject
    public EventHomePresenter(GetEventsListRequestUseCase getEventsListRequestUsecase, GetEventsListByLocationRequestUseCase getEventsListByLocationRequestUseCase, GetSearchEventsListRequestUseCase getSearchEventsListRequestUseCase) {
        this.getEventsListRequestUsecase = getEventsListRequestUsecase;
        this.getEventsListByLocationRequestUseCase = getEventsListByLocationRequestUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void startBannerSlide(TouchViewPager viewPager) {
        this.mTouchViewPager = viewPager;
        currentPage = viewPager.getCurrentItem();
        try {
            totalPages = viewPager.getAdapter().getCount();
        } catch (Exception e) {
            e.printStackTrace();
            totalPages = viewPager.getChildCount();
        }
        Observable.interval(5000, 5000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (currentPage + 1 < totalPages)
                            ++currentPage;
                        else if (currentPage + 1 >= totalPages) {
                            currentPage = 0;
                        }
                        mTouchViewPager.setCurrentItem(currentPage, true);
                        UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_PROMO_IMPRESSION, mTouchViewPager.getAdapter().getPageTitle(currentPage).toString() +
                                " - " + currentPage);
                    }
                });
    }

    @Override
    public void onBannerSlide(int page) {
        currentPage = page;
    }

    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == R.id.action_menu_search) {
            ArrayList<SearchViewModel> searchViewModelList = Utils.getSingletonInstance()
                    .convertIntoSearchViewModel(categoryViewModels);
            Intent searchIntent = EventSearchActivity.getCallingIntent(getView().getActivity());
            searchIntent.putParcelableArrayListExtra("TOPEVENTS", searchViewModelList);
            getView().navigateToActivityRequest(searchIntent,
                    EventsHomeActivity.REQUEST_CODE_EVENTSEARCHACTIVITY);
            UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_SEARCH, "");
            return true;
        } else if (id == R.id.action_promo) {
            startGeneralWebView(PROMOURL);
            UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_PROMO, "");
            return true;
        } else if (id == R.id.action_booked_history) {
            startGeneralWebView(TRANSATIONSURL);
            UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_DAFTAR_TRANSAKSI, "");
            return true;
        } else if (id == R.id.action_faq) {
            startGeneralWebView(FAQURL);
            UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_BANTUAN, "");

            return true;
        } else {
            getView().getActivity().onBackPressed();
            UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_BACK, getSCREEN_NAME());
            return true;
        }
    }

    public void getEventsList() {
        getView().showProgressBar();
        getEventsListRequestUsecase.execute(getView().getParams(), new Subscriber<List<EventsCategoryDomain>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
                getView().hideProgressBar();
                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getEventsList();
                    }
                });
            }

            @Override
            public void onNext(List<EventsCategoryDomain> categoryEntities) {
                getView().hideProgressBar();
                categoryViewModels = Utils.getSingletonInstance()
                        .convertIntoCategoryListVeiwModel(categoryEntities);
                getCarousel(categoryViewModels);
                getView().renderCategoryList(categoryViewModels);
                getView().showSearchButton();
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    public void getEventsListByLocation(String location) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(getEventsListByLocationRequestUseCase.LOCATION, location);
        getEventsListByLocationRequestUseCase.execute(requestParams, new Subscriber<List<EventsCategoryDomain>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
            }

            @Override
            public void onNext(List<EventsCategoryDomain> categoryEntities) {
                List<CategoryViewModel> categoryViewModels = Utils.getSingletonInstance()
                        .convertIntoCategoryListVeiwModel(categoryEntities);
                getCarousel(categoryViewModels);
                getView().renderCategoryList(categoryViewModels);
            }
        });
    }

    public ArrayList<String> getCarouselImages(List<CategoryItemsViewModel> categoryItemsViewModels) {
        ArrayList<String> imagesList = new ArrayList<>();
        if (categoryItemsViewModels != null) {
            for (CategoryItemsViewModel categoryItemsViewModel : categoryItemsViewModels
                    ) {
                imagesList.add(categoryItemsViewModel.getImageApp());
            }
        }
        return imagesList;
    }

    public void onClickBanner() {
        CategoryItemsViewModel categoryItemsViewModel = carousel.getItems().get(currentPage);
        if (categoryItemsViewModel.getUrl().contains("www.tokopedia.com")
                || categoryItemsViewModel.getUrl().contains("docs.google.com")) {
            startGeneralWebView(categoryItemsViewModel.getUrl());
        } else {
            Intent intent = new Intent(getView().getActivity(), EventDetailsActivity.class);
            intent.putExtra("homedata", categoryItemsViewModel);
            getView().getActivity().startActivity(intent);
        }
        UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_PROMO_CLICK,
                categoryItemsViewModel.getTitle() + "-" + String.valueOf(currentPage));
    }

    private void getCarousel(List<CategoryViewModel> categoryViewModels) {
        for (CategoryViewModel model : categoryViewModels) {
            if (model.getTitle().equals("Carousel")) {
                carousel = model;
                break;
            }
        }
    }

    private void startGeneralWebView(String url) {
        if (getView().getActivity().getApplication() instanceof TkpdCoreRouter) {
            ((TkpdCoreRouter) getView().getActivity().getApplication())
                    .actionOpenGeneralWebView(getView().getActivity(), url);
        }
    }


    @Override
    public String getSCREEN_NAME() {
        return EventsGAConst.EVENTS_HOMEPAGE;
    }

}
