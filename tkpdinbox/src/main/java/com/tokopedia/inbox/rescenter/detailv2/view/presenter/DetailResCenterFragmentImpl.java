package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.rescenter.ResolutionService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.rescenter.detailv2.data.factory.ResCenterDataSourceFactory;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
import com.tokopedia.inbox.rescenter.detailv2.data.repository.ResCenterRepositoryImpl;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResCenterDetailUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AddressReturData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AwbData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ButtonData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.HistoryData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.HistoryItem;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ProductData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ProductItem;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.SolutionData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.StatusData;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/9/17.
 */

public class DetailResCenterFragmentImpl implements DetailResCenterFragmentPresenter {

    private final DetailResCenterFragmentView fragmentView;
    private final GetResCenterDetailUseCase getResCenterDetailUseCase;

    public DetailResCenterFragmentImpl(Context context, DetailResCenterFragmentView fragmentView) {
        this.fragmentView = fragmentView;
        String resolutionID = fragmentView.getResolutionID();
        String accessToken = new SessionHandler(context).getAccessToken(context);

        JobExecutor jobExecutor = new JobExecutor();
        UIThread uiThread = new UIThread();
        ResolutionService resolutionService = new ResolutionService();
        resolutionService.setToken(accessToken);
        DetailResCenterMapper detailResCenterMapper = new DetailResCenterMapper();

        ResCenterDataSourceFactory dataSourceFactory
                = new ResCenterDataSourceFactory(context, resolutionService, detailResCenterMapper);
        ResCenterRepository resCenterRepository
                = new ResCenterRepositoryImpl(resolutionID, dataSourceFactory);

        this.getResCenterDetailUseCase
                = new GetResCenterDetailUseCase(jobExecutor, uiThread, resCenterRepository);
    }

    @Override
    public void setOnFirstTimeLaunch() {
        fragmentView.showLoading(true);
        getResCenterDetailUseCase.execute(getInitResCenterDetailParam(), new GetResCenterDetailSubscriber());
    }

    private RequestParams getInitResCenterDetailParam() {
        return RequestParams.EMPTY;
    }

    private class GetResCenterDetailSubscriber extends rx.Subscriber<DetailResCenter> {
        @Override
        public void onCompleted() {
            fragmentView.setOnInitResCenterDetailComplete();
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof IOException) {
                fragmentView.setViewData(mappingTimeOutViewModel());
            } else {
                fragmentView.setViewData(mappingDefaultErrorViewModel());
            }
        }

        @Override
        public void onNext(DetailResCenter detailResCenter) {
            fragmentView.setViewData(mappingViewModel(detailResCenter));
        }
    }

    private DetailViewModel mappingDefaultErrorViewModel() {
        return mappingTimeOutViewModel();
    }

    private DetailViewModel mappingTimeOutViewModel() {
        DetailViewModel model = new DetailViewModel();
        model.setSuccess(false);
        model.setTimeOut(true);
        return model;
    }

    private DetailViewModel mappingViewModel(DetailResCenter detailResCenter) {
        DetailViewModel model = new DetailViewModel();
        if (true) {
            model.setSuccess(true);
            model.setAddressReturData(mappingAddressReturData(detailResCenter));
            model.setAwbData(mappingAwbReturData(detailResCenter));
            model.setButtonData(mappingButtonData(detailResCenter));
            model.setDetailData(mappingDetailData(detailResCenter));
            model.setHistoryData(mappingHistoryData(detailResCenter));
            model.setProductData(mappingProductData(detailResCenter));
            model.setSolutionData(mappingSolutionData(detailResCenter));
            model.setStatusData(mappingStatusData(detailResCenter));
        } else {
            model.setSuccess(false);
            model.setMessageError(detailResCenter != null ? detailResCenter.getMessageError() : null);
        }
        return model;
    }

    private AddressReturData mappingAddressReturData(DetailResCenter detailResCenter) {
        AddressReturData data = new AddressReturData();
        data.setAddressText("<b>Rumah Jali</b>" + "\n" +
                "Jl. Letjen S. Parman Kav.77 " + "\n" +
                "DKI Jakarta 11410" + "\n" +
                "Telp: 08788101732"
        );
        data.setAddressReturDate("24 Juli 2016 12:22 WIB");
        return data;
    }

    private AwbData mappingAwbReturData(DetailResCenter detailResCenter) {
        AwbData data = new AwbData();
        data.setAwbText("<b>CGK1233454546</b>");
        data.setAwbDate("24 Juli 2016 12:22 WIB");
        return data;
    }

    private ButtonData mappingButtonData(DetailResCenter detailResCenter) {
        ButtonData data = new ButtonData();
        data.setShowAcceptProduct(true);
        data.setShowAcceptSolution(false);
        data.setShowEdit(false);
        return data;
    }

    private DetailData mappingDetailData(DetailResCenter detailResCenter) {
        DetailData data = new DetailData();
        data.setAwbNumber("BKS0001234567");
        data.setBuyerDeadlineVisibility(false);
        data.setBuyerID("1234009");
        data.setBuyerName("Arif Riyanto");
        data.setComplaintDate("22 Juli 2016 10:10 WIB");
        data.setInvoice("INV/20162090/XV/III/1234");
        data.setResponseDeadline("");
        data.setSellerDeadlineVisibility(false);
        data.setShopID("67890");
        data.setShopName("ST. Toms Store");
        return data;
    }

    private HistoryData mappingHistoryData(DetailResCenter detailResCenter) {
        HistoryData data = new HistoryData();
        List<HistoryItem> historyItems = new ArrayList<>();
        HistoryItem item = new HistoryItem();
        item.setDate("30 Juli 2016 09:33 WIB");
        item.setLatest(true);
        item.setProvider("Pembeli");
        item.setHistoryText("Produk telah dikirim kembali oleh Pembeli. " +
                "Menunggu konfirmasi terima produk oleh Penjual");
        historyItems.add(item);

        // ---------------- carefull delete this if ws ready
        HistoryItem item2 = new HistoryItem();
        item2.setDate("28 Juli 2016 09:33 WIB");
        item2.setLatest(false);
        item2.setProvider("Penjual");
        item2.setHistoryText("Penjual telah memberi alamat retur produk. " +
                "Menunggu nomor resi retur produk dari Pembeli");
        historyItems.add(item2);
        // ---------------- carefull delete this if ws ready

        data.setHistoryList(historyItems);
        return data;
    }

    private ProductData mappingProductData(DetailResCenter detailResCenter) {
        ProductData data = new ProductData();
        data.setProductRelatedComplaint(true);
        List<ProductItem> productItems = new ArrayList<>();
        ProductItem item = new ProductItem();
        item.setProductImageUrl("https://ecs7.tokopedia.net/img/cache/300/product-1/2016/9/4/279371/279371_c6424ac7-a5b5-406d-80ec-de298a8a14b8");
        item.setProductName("Boneka Star Wars Plush BB8 Droid R2D2 Storm Trooper Darth Vader");
        productItems.add(item);
        data.setProductList(productItems);
        return data;
    }

    private SolutionData mappingSolutionData(DetailResCenter detailResCenter) {
        SolutionData data = new SolutionData();
        data.setSolutionDate("28 Juli 2016 12:11 WIB");
        data.setSolutionProvider("Penjual");
        data.setSolutionText("Penjual minta retur produk dan pengembalian dana sebesar Rp. 1.750.000");
        return data;
    }

    private StatusData mappingStatusData(DetailResCenter detailResCenter) {
        StatusData data = new StatusData();
        data.setStatusText("Menunggu konfirmasi terima produk oleh penjual");
        return data;
    }
}
