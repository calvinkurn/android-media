package com.tokopedia.events.view.presenter;

import android.content.Intent;
import android.util.Log;

import com.tkpd.library.ui.widget.TouchViewPager;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.events.R;
import com.tokopedia.events.domain.GetEventsListRequestUseCase;
import com.tokopedia.events.domain.GetUserLikesUseCase;
import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.events.domain.model.LikeUpdateResultDomain;
import com.tokopedia.events.domain.model.request.likes.LikeUpdateModel;
import com.tokopedia.events.domain.model.request.likes.Rating;
import com.tokopedia.events.domain.postusecase.PostUpdateEventLikesUseCase;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.activity.EventSearchActivity;
import com.tokopedia.events.view.activity.EventsHomeActivity;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.contractor.EventsContract.AdapterCallbacks;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;
import com.tokopedia.events.view.viewmodel.SearchViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.tokopedia.events.view.utils.Utils.Constants.EXTRA_EVENT_CALENDAR;
import static com.tokopedia.events.view.utils.Utils.Constants.FAQURL;
import static com.tokopedia.events.view.utils.Utils.Constants.PROMOURL;
import static com.tokopedia.events.view.utils.Utils.Constants.TRANSATIONSURL;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class EventHomePresenter extends BaseDaggerPresenter<EventsContract.View>
        implements EventsContract.Presenter {

    private GetEventsListRequestUseCase getEventsListRequestUsecase;
    private PostUpdateEventLikesUseCase postUpdateEventLikesUseCase;
    private GetUserLikesUseCase getUserLikesUseCase;
    private CategoryViewModel carousel;
    private List<CategoryViewModel> categoryViewModels;
    private TouchViewPager mTouchViewPager;
    private int currentPage, totalPages;
    private List<AdapterCallbacks> adapterCallbacks;
    Subscription subscription;


    @Inject

    public EventHomePresenter(GetEventsListRequestUseCase getEventsListRequestUsecase,
                              PostUpdateEventLikesUseCase eventLikesUseCase,
                              GetUserLikesUseCase likesUseCase) {
        this.getEventsListRequestUsecase = getEventsListRequestUsecase;
        this.postUpdateEventLikesUseCase = eventLikesUseCase;
        this.getUserLikesUseCase = likesUseCase;
        adapterCallbacks = new ArrayList<>();
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
            return true;
        } else if (id == R.id.action_promo) {
            startGeneralWebView(PROMOURL);
            return true;
        } else if (id == R.id.action_booked_history) {
            startGeneralWebView(TRANSATIONSURL);
            return true;
        } else if (id == R.id.action_faq) {
            startGeneralWebView(FAQURL);
            return true;
        } else {
            getView().getActivity().onBackPressed();
            return true;
        }
    }

    @Override
    public void showEventDetails(CategoryItemsViewModel model) {
        Intent detailsIntent = new Intent(getView().getActivity(), EventDetailsActivity.class);
        detailsIntent.putExtra(EventDetailsActivity.FROM, EventDetailsActivity.FROM_HOME_OR_SEARCH);
        detailsIntent.putExtra("homedata", model);
        getView().getActivity().startActivity(detailsIntent);
    }

    @Override
    public void setEventLike(final CategoryItemsViewModel model, final int position) {
        if (SessionHandler.isV4Login(getView().getActivity())) {
            LikeUpdateModel requestModel = new LikeUpdateModel();
            //todo set requestmodel values
            Rating rating = new Rating();
            if (model.isLiked()) {
                rating.setIsLiked("false");
                model.setLiked(false);
            } else {
                rating.setIsLiked("true");
                model.setLiked(true);
            }
            rating.setUserId(Integer.parseInt(SessionHandler.getLoginID(getView().getActivity())));
            rating.setProductId(model.getId());
            rating.setFeedback("");
            requestModel.setRating(rating);
            com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
            requestParams.putObject("request_body", requestModel);
            postUpdateEventLikesUseCase.createObservable(requestParams).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<LikeUpdateResultDomain>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("UPDATEEVENTLIKE", e.getLocalizedMessage());
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(LikeUpdateResultDomain likeUpdateResultDomain) {
                            Log.d("UPDATEEVENTLIKE", "onNext");
                            if (likeUpdateResultDomain.isLiked() && model.isLiked()) {
                                model.setLikes(model.getLikes() + 1);
                            } else if (!likeUpdateResultDomain.isLiked() && !model.isLiked()) {
                                model.setLikes(model.getLikes() - 1);
                            }
                            for (AdapterCallbacks callbacks : adapterCallbacks)
                                callbacks.notifyDatasetChanged(position);
                        }
                    });
        } else {
            getView().showLoginSnackbar("Please Login to like or share events");
        }
    }

    @Override
    public void shareEvent(CategoryItemsViewModel model) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT,
                String.format(getView().getActivity().getString(R.string.check_this_out),
                        model.getTitle()));
        share.putExtra(Intent.EXTRA_TEXT, "https://www.tokopedia.com/events/detail/" + model.getSeoUrl());
        getView().getActivity().startActivity(Intent.createChooser(share, "Share Event!"));
    }

    @Override
    public void onActivityResult(int requestCode) {
        if (requestCode == 1099) {
            if (SessionHandler.isV4Login(getView().getActivity())) {
                getView().hideProgressBar();
                getView().showMessage("You can now like or share events");
            } else {
                getView().hideProgressBar();
            }
        }
    }

    @Override
    public void onClickEventCalendar() {
        Intent searchIntent = EventSearchActivity.getCallingIntent(getView().getActivity());
        searchIntent.putExtra(EXTRA_EVENT_CALENDAR, true);
        getView().navigateToActivityRequest(searchIntent, 1010);
    }

    @Override
    public void setupCallback(EventsContract.AdapterCallbacks callbacks) {
        this.adapterCallbacks.add(callbacks);
    }

    public void getEventsList() {
        getView().showProgressBar();
        getEventsListRequestUsecase.getExecuteObservableAsync(getView().getParams())
                .concatMap(new Func1<List<EventsCategoryDomain>, Observable<List<Integer>>>() {
                    @Override
                    public Observable<List<Integer>> call(List<EventsCategoryDomain> eventsCategoryDomains) {
                        categoryViewModels = Utils.getSingletonInstance()
                                .convertIntoCategoryListVeiwModel(eventsCategoryDomains);
                        if (SessionHandler.isV4Login(getView().getActivity()))
                            return getUserLikesUseCase.getExecuteObservable(RequestParams.create())
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread());
                        else {
                            List<Integer> empty = new ArrayList<Integer>();
                            return Observable.just(empty);
                        }
                    }
                }).concatMap(new Func1<List<Integer>, Observable<List<CategoryViewModel>>>() {
            @Override
            public Observable<List<CategoryViewModel>> call(List<Integer> integers) {
                if (!integers.isEmpty() || integers.size() > 0)
                    for (Integer id : integers) {
                        for (CategoryViewModel category : categoryViewModels) {
                            for (CategoryItemsViewModel itemsViewModel : category.getItems()) {
                                if (itemsViewModel.getId() == id)
                                    itemsViewModel.setLiked(true);
                            }
                        }
                    }
                return Observable.just(categoryViewModels);
            }
        }).subscribe(new Subscriber<List<CategoryViewModel>>() {
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
            public void onNext(List<CategoryViewModel> categoryViewModels) {
                getView().hideProgressBar();
                getCarousel(categoryViewModels);
                getView().renderCategoryList(categoryViewModels);
                getView().showSearchButton();
                CommonUtils.dumper("enter onNext");
            }
        });
//        getEventsListRequestUsecase.execute(getView().getParams(), new Subscriber<List<EventsCategoryDomain>>() {
//            @Override
//            public void onCompleted() {
//                CommonUtils.dumper("enter onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                CommonUtils.dumper("enter error");
//                e.printStackTrace();
//                getView().hideProgressBar();
//                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
//                    @Override
//                    public void onRetryClicked() {
//                        getEventsList();
//                    }
//                });
//            }
//
//            @Override
//            public void onNext(List<EventsCategoryDomain> categoryEntities) {
//                getView().hideProgressBar();
//                categoryViewModels = Utils.getSingletonInstance()
//                        .convertIntoCategoryListVeiwModel(categoryEntities);
//                getCarousel(categoryViewModels);
//                getView().renderCategoryList(categoryViewModels);
//                getView().showSearchButton();
//                CommonUtils.dumper("enter onNext");
//            }
//        });
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


}
