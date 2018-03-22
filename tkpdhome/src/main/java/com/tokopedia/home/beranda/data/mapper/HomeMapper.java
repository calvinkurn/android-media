package com.tokopedia.home.beranda.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.analytics.HomePageTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon;
import com.tokopedia.home.beranda.domain.model.HomeData;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.LayoutSections;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SixGridViewModel;
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

            if (homeData.getTicker() != null
                    && homeData.getTicker().getTickers() != null
                    && !homeData.getTicker().getTickers().isEmpty()) {
                list.add(mappingTicker(homeData.getTicker().getTickers()));
            }

            if (homeData.getSlides() != null
                    && homeData.getSlides().getSlides() != null
                    && !homeData.getSlides().getSlides().isEmpty()) {
                list.add(mappingBanner(homeData.getSlides().getSlides()));
            }

            if (homeData.getDynamicHomeIcon() != null
                    && homeData.getDynamicHomeIcon().getUseCaseIcon() != null
                    && !homeData.getDynamicHomeIcon().getUseCaseIcon().isEmpty()) {
                list.add(mappingUseCaseIcon(homeData.getDynamicHomeIcon().getUseCaseIcon()));
            }

            if (homeData.getDynamicHomeIcon() != null
                    && homeData.getDynamicHomeIcon().getDynamicIcon() != null
                    && !homeData.getDynamicHomeIcon().getDynamicIcon().isEmpty()) {
                list.add(mappingDynamicIcon(homeData.getDynamicHomeIcon().getDynamicIcon()));
            }

            if (homeData.getDynamicHomeChannel() != null
                    && homeData.getDynamicHomeChannel().getChannels() != null
                    && !homeData.getDynamicHomeChannel().getChannels().isEmpty()) {
                int position = 1;
                for(DynamicHomeChannel.Channels channel : homeData.getDynamicHomeChannel().getChannels()) {
                    if (channel.getLayout().equals(DynamicHomeChannel.Channels.LAYOUT_SPRINT)) {
                        HomePageTracking.eventEnhancedImpressionSprintSaleHomePage(
                                channel.getEnhanceImpressionSprintSaleHomePage()
                        );
                    } else {
                        position++;
                        channel.setPromoName(String.format("/ - p%d - %s", position, channel.getHeader().getName()));
                        HomePageTracking.eventEnhancedImpressionDynamicChannelHomePage(
                                channel.getEnhanceImpressionDynamicChannelHomePage()
                        );
                    }
                    list.add(mappingDynamicChannel(channel));
                }
            }

            list.add(convertToSixGridViewModel());

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

    private SixGridViewModel convertToSixGridViewModel() {
        SixGridViewModel model = new SixGridViewModel();
        List<SixGridViewModel.Item> itemList = new ArrayList<>();
        itemList.add(new SixGridViewModel.Item("https://lh3.googleusercontent.com/pZE0nimKu2hW95ivHmXBQoLrEgQVs5rHJ-TqX4Yqay8dViGVZSWs6wfP_iZiHbcxI9EqS9PNzDDC8ROnBXjzLDGkxsv9fgO88LJ-HmAXBTKnndXDk7K7HFUhMO4DzA0LhNpSoZsYjmukJWg0FT3SWt1lIBdJIJYpjR5sqeSWp7lV0pFEVWcY0AryMty90D9CwV_vjg0pFTVEmpHxCscCdpRFbjdtHzAGJYmNShpagI-QnaYMah26F8WZ6aOXVbdcRaFvkSooxOmiqf1Qmp3vH226n0l3ghD_FdwAtJsx2TKjEl8SYr_zc5WiLxg2jqquZQnrLG324ycwr72ADVBN_85d9zxj9AU9oi1-Oi_Dd2CuAcWBkCFL6THw1myZC284bU8eiFzQXYx-qlxz3gIXyaLbSjlT2-4NoMkSkuxz9pqQcyOfGMVMtHtPzf1lwBfkx77Va2CPo76yuuKJWoyAtkxnkImS9iD0QyBVwVSFZ0cqgoih9zbMeSlKM6qjQvP11YjwI__GoggfDcrFUdttp5Jv3gENhjdpc6tewtj4vKBImpcA2_YoTG28jaFcuZvRahAlqslvsvE3CKeTTE0RWKavYmYjF7DGGLN4sg=s200-no",
                "https://www.tokopedia.com/p/fashion-pria",
                ""));
        itemList.add(new SixGridViewModel.Item("https://lh3.googleusercontent.com/-QgWXadhzhp0648GhmnR5cmGfcpW1KNpZzBkDHE0YsCDBAUuSX9-cwekMq-T_zRvyCSMs88LAklLnEeiLH1mipu7HFBh3yzTr5w3fYaGko1emChWi1WwHROBprHxjKYoNdmV2Y9L8TkSRACVVKx-ArBFPZjlW1p1ofppchnfyCCHoODq2knqq8VyV2apx8UM5YtxjlDVIziZKlhG_T3-7eSoGIyJ5TTnVbf8c2YYPZ4ayt20fcKWDCVZ6UkHkGBeH4sahcvavxITckxqxUPzKPyP9qJBUBcrxuI7gcMWDDDnv-3tmOos8HEmUKZFj9ONwin58B3fsLW4Adp0ppvQT3KRj2ysT77UdTRiHDE-9sKX8uqrRjankzIT9NFNm5ChUsBF1R7_3J1We1Jpf4imQLiPl235HX4ae5oXqN70ww8xOMThje3lnfzgL7UEn6pvV1B7YTFv1zL2p4Lhf67bBUw0eXXrDadfD1xcww55lQrs2DfHfPccetRPCYqSGQrZrqp3-Rllo6JIG7od9irDJMwJfy-R-9C6TFGYC6EEz3OWzt332ElLbscv22xGydCIor_mZ8HFafs1qwI39jyIlrQuIoJKkx6Fl4rRxw=s200-no",
                "https://www.tokopedia.com/p/fashion-wanita",
                ""));
        itemList.add(new SixGridViewModel.Item("https://lh3.googleusercontent.com/nR5LdawyQQQ-_-ZMmCw-eZXNNC5xyZ0UiIuzIwRfLtccfiZTuzVqD8eJv6zUPOZnBIZwH09rZGATdLMKu1jOZX3j-N5fpNn37HeLNRF6YnIDepPuUfm0NJNEuaZrNbImMb-c6LcAT1kuBTBm6aSni4Yd-dfso_B2Y2Rv5vfSu-x1Jeelo7tmFloHAMFdacEfiogl9Sa9x8p8RX_0ATAtNZ4Iytxd_2t1Puw7-TbCh3PkaYezYK6oqzXMnJqKeElnEUg586G3AZ9UF1bdjqXgUfMDhC0CDEWORJYmuQMbItQAVOdg7eHZ471NZmNpbZSFbIk1_CiovfC4gbr7jRPa94bwIihF-Zi6axbRSfqPdo60g47iSBzibSFLS82vu8Ju5yKmN14KDkkfFT5tDflZokKmHO6GFyDwZzLKdxXShH0olGMDi0oRO9olu5p8To_uyDhDoRTJ9reZC81DUN45cC9lQ7LjljL181_II0bDcCF3abUrT4esQlCRJB7s-MvzuWbn_KEdhn8H-eGDDDeAgNAmDk5Yuhon37EcXCCVyaNwpmwUh_TTULlQaLTD8aPKWMMbgGbRqRD5qjQufViJ6mA3phwZZx7Obkh6iQ=s200-no",
                "https://www.tokopedia.com/p/souvenir-kado",
                ""));
        itemList.add(new SixGridViewModel.Item("https://lh3.googleusercontent.com/bUmmLhUdpLfXb5GLQbofb7FjudXsT_iZRf-mImmnvhTWKkk5AUujN1RsonPCwumU-wqMV7iIPF4Lt_n9SuCOsib6mzB1qmaL-9-bqxN0goQ_hZNEvyGydvP1Z4mV7kt85KrKqZga2PIdzGGqAKcaOSwOI0Sl0ZZquN5tBuSMXzBAqboDjUC9vQ8OSCX-JmRG5UwSqknHCd080XoIIwRuZJ7UdFWjEv4oLPTb-u378go1RjT_FvWaD7wBtleW_-pNTTuwnB2BhTyKdAwy2w4fhSvr_JNxv9NmBwGbxyrzKRD7KLGeUAS_kqYAFw4CD1qeNj6FnwQVsMK5KmC3n13MFhr7egVbOVGZahn-61u-qHqACq6eG_gd9Gdtcb7tuZyVWbVGK_b7m8DjeIqkmLs7Rlt97qRjvz6gPZ1S_bzo4jgpXVf-u9kiCJE0HyAUsnQ7MbOVQS0hjtcN-fKSkDl5xVZ38QrFG5K5Y4jrb8uwAdmSoEXe-VpqF5Z1nBWw2alOqvYllDSu8nQ-XUh7iRAMTNuwIdmdmeDBzhXCrqRo1l5HuXEi4zTtJ7fHwsjzLOQjxdo6tyWDn5piRu2mkei8vVF595E9Vo-dllJ31g=s200-no",
                "https://www.tokopedia.com/p/souvenir-kado",
                "tokopedia://search?q=baju"));
        itemList.add(new SixGridViewModel.Item("https://lh3.googleusercontent.com/BFltBsZhBidWpmScfREuU6_DIidXZpzUWlFpS9yZczxIlvSF01znG9E8lmIfIApbIQoXoNPzOSRp14S0NqTL8LYZVT046kMC8OXxtiBYn_hcIT4Z_aC-MdCZy6rwCjc5kNZLR5u2SzX6_4ju5brOq7FX0kLdwTmYBYjeohph9y2wtP-Y6-CguuyyfB0esRuJ94pwqXpcrQr4oZF_SqfCsPQ3RRebSoNgUAMcetewNsOqKNOZtbJYi3ewb_-zrrkuu0r1G2Qdel14ntDf30NHodUa78-kCli2ZWqAWwjpsJ3bWdWbsoZX5_MVpzmEjpNjsZ2EHk7uGonXv-i03CVmz0xrYkvd7ZidR74_wiG51qB1cniwpa3N2lPXmDJ2GEF5mp_9Rr58YELDdWLJsZVklib4XFvozoRmp249ScD-N9WVYnUifLFm9df6KJq4eRB8hWHOEAeb0QVfnH1g3rFoaJFKSDTBmoKSs8zVI8dZGIoXUYN1f88CM2Mz2BLyfBtPi_wOuE3bH8r5HvnG-Jq7_xVEmg3zk72q-1IprbmTeXRNgbfppEu_uUC_Fy25-ijJtwtmjErF5MBGbqzSUKzwedJnd7pps6SLm2fgrw=s200-no",
                "https://www.tokopedia.com/p/souvenir-kado",
                "tokopedia://search?q=buku"));
        itemList.add(new SixGridViewModel.Item("https://lh3.googleusercontent.com/OD6Ui8Q5oVdJha8dhA5owawgnG6J10fMP2V36db0AIT-tsOdbD9dafIYudluFPUElP12_MSWzEmJVYTDYKzFIVnmJ7D1LrF4oHyUkrW847OES8C5P4pSN1WUavzCWhNfkU4zarwfZT_MfLBvzAeibn6isJ0zFyKFbvIpkg9WVFPf6-ujo8zKLdm-MVp4wDefExKQX6n0nMzuggBODgBoDJZreetxYLPJnptajyfJcHPIzwpZJtJjPpAfyOBioLwnDzkHDeQi47MHN3XB_8iHFgFqpBEZdNtHPv-zy5jIb6mgXhm4vCiRsdeUGubL9r9Q-iZnAK7pWWXiNWeMIwtgy5nf-LGR3Nc44R0QL3QeX-QtPswgHETh_7IP3kVh1h8TZGiSNlzsszl1Equ7ewozbnCqdsqyf3mIVOrFy5aF4St10OGU3W7_QKCwqwi91gGe3wRtMhyglFohA1SI1twD9DWTj9cYh0TeeOf9lhP0jwDYEZhJ3D8kTj1TbqS3HT8rksNh2y8-B46DgAuhtB21j3ROHKvY6eOnrgMs5-27YwYe9ogekPT94TvIt8soMAcIjsSV483XCroIKFBFv988quga8An2Aqkmm2Lsjg=s200-no",
                "",
                "tokopedia://search?q=sepatu"));
        model.setItemList(itemList);
        return model;
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
