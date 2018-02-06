package com.tokopedia.home.beranda.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.data.source.pojo.DynamicHomeChannel;
import com.tokopedia.home.beranda.data.source.pojo.DynamicHomeIcon;
import com.tokopedia.home.beranda.data.source.pojo.HomeData;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.LayoutSections;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TickerViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;


/**
 * Created by henrypriyono on 26/01/18.
 */

public class HomeMapper implements Func1<Response<GraphqlResponse<HomeData>>, List<Visitable>> {
    @Override
    public List<Visitable> call(Response<GraphqlResponse<HomeData>> response) {
        if (response.isSuccessful()) {
            List<Visitable> list = new ArrayList<>();

            HomeData homeData = response.body().getData();

            if (homeData.getTicker() != null && !homeData.getTicker().getTickers().isEmpty()) {
                list.add(mappingTicker(homeData.getTicker().getTickers()));
            }

            if (homeData.getSlides() != null && !homeData.getSlides().getSlides().isEmpty()) {
                list.add(mappingBanner(homeData.getSlides().getSlides()));
            }

            if (homeData.getDynamicHomeIcon() != null && !homeData.getDynamicHomeIcon().getUseCaseIcon().isEmpty()) {
                list.add(mappingUseCaseIcon(homeData.getDynamicHomeIcon().getUseCaseIcon()));
            }

            if (homeData.getDynamicHomeIcon() != null && !homeData.getDynamicHomeIcon().getDynamicIcon().isEmpty()) {
                list.add(mappingDynamicIcon(homeData.getDynamicHomeIcon().getDynamicIcon()));
            }

            if (homeData.getDynamicHomeChannel() != null && !homeData.getDynamicHomeChannel().getChannels().isEmpty()) {
                for(DynamicHomeChannel.Channels channel : homeData.getDynamicHomeChannel().getChannels()) {
                    list.add(mappingDynamicChannel(channel));
                }
            }

            list.add(new DigitalsViewModel(MainApplication.getAppContext().getString(R.string.digital_widget_title), 0));

            return list;
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private Visitable mappingTicker(ArrayList<Ticker.Tickers> tickers) {
        TickerViewModel viewModel = new TickerViewModel();
        viewModel.setTickers(tickers);
        return viewModel;
    }

    private Visitable mappingBanner(List<BannerSlidesModel> slides) {
        BannerViewModel viewModel = new BannerViewModel();
        viewModel.setSlides(slides);
        return viewModel;
    }

    private Visitable mappingUseCaseIcon(List<DynamicHomeIcon.UseCaseIcon> iconList) {
        CategorySectionViewModel viewModel = new CategorySectionViewModel();
        for (DynamicHomeIcon.UseCaseIcon icon : iconList) {
            viewModel.addSection(new LayoutSections(LayoutSections.ICON_USE_CASE, icon.getName(), icon.getImageUrl(), icon.getApplinks(), icon.getUrl()));
        }
        return viewModel;
    }

    private Visitable mappingDynamicIcon(List<DynamicHomeIcon.DynamicIcon> iconList) {
        CategorySectionViewModel viewModel = new CategorySectionViewModel();
        for (DynamicHomeIcon.DynamicIcon icon : iconList) {
            viewModel.addSection(new LayoutSections(LayoutSections.ICON_DYNAMIC_CASE, icon.getName(), icon.getImageUrl(), icon.getApplinks(), icon.getUrl()));
        }
        return viewModel;
    }

    private Visitable mappingDynamicChannel(DynamicHomeChannel.Channels channel) {
        DynamicChannelViewModel viewModel = new DynamicChannelViewModel();
        viewModel.setChannel(channel);
        return viewModel;
    }
}
