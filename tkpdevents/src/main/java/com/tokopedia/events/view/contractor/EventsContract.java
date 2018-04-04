package com.tokopedia.events.view.contractor;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.widget.TouchViewPager;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;

import java.util.List;

/**
 * Created by ashwanityagi on 03/11/17.
 */

public class EventsContract {
    public interface View extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderCategoryList(List<CategoryViewModel> categoryList);

        RequestParams getParams();

        android.view.View getRootView();

        void showProgressBar();

        void hideProgressBar();

        void hideSearchButton();

        void showSearchButton();

    }

    public interface Presenter extends CustomerPresenter<View> {

        void initialize();

        void onDestroy();

        void startBannerSlide(TouchViewPager viewPager);

        void onBannerSlide(int page);

        boolean onOptionMenuClick(int id);
    }
}
