package com.tokopedia.movies.view.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.movies.data.entity.response.MovieSeatResponseEntity;
import com.tokopedia.movies.data.entity.response.SeatLayoutItem;
import com.tokopedia.movies.data.entity.response.seatlayoutresponse.SeatLayoutResponse;
import com.tokopedia.movies.domain.GetSeatLayoutUseCase;
import com.tokopedia.movies.view.contractor.EventBookTicketContract;
import com.tokopedia.movies.view.contractor.SeatSelectionContract;
import com.tokopedia.movies.view.customview.CustomShowTimeLayout;
import com.tokopedia.movies.view.customview.SeatLayoutInfo;
import com.tokopedia.movies.view.mapper.SeatLayoutResponseToSeatLayoutViewModelMapper;
import com.tokopedia.movies.view.utils.MovieInfoClass;
import com.tokopedia.movies.view.viewmodel.SeatLayoutViewModel;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by naveengoyal on 1/16/18.
 */

public class SeatSelectionPresenter extends BaseDaggerPresenter<SeatSelectionContract.SeatSelectionView>
        implements SeatSelectionContract.Presenter {

    GetSeatLayoutUseCase getSeatLayoutUseCase;
    private SeatLayoutViewModel seatLayoutViewModel;
    String url;
    String title;
    SeatLayoutInfo seatLayoutInfo;


    @Inject
    public SeatSelectionPresenter(GetSeatLayoutUseCase seatLayoutUseCase) {
        this.getSeatLayoutUseCase = seatLayoutUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void attachView(SeatSelectionContract.SeatSelectionView view) {
        super.attachView(view);
        seatLayoutInfo = getView().getActivity().getIntent().getParcelableExtra("SEATINFO");
        url = "https://booking.tokopedia.com/v1/api/seat-layout/category/2/product/322/schedule/804/group/17491/package/42472";
        title = seatLayoutInfo.getMovieName();
//        getView().renderSeatSelection(seatLayoutInfo, null);
    }


    @Override
    public void getSeatSelectionDetails() {
        getView().showProgressBar();
        getSeatLayoutUseCase.setUrl(url);
        getSeatLayoutUseCase.execute(RequestParams.EMPTY, new Subscriber<List<SeatLayoutItem>>() {
            @Override
            public void onCompleted() {
                Log.d("Naveen", " on Completed");
                getView().hideProgressBar();
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("Naveen", " on Error" + throwable.getMessage());
            }

            @Override
            public void onNext(List<SeatLayoutItem> response) {
                Log.d("Naveen", " on Next" + response.get(0));
                getView().renderSeatSelection(seatLayoutInfo, convertResponseToViewModel(convertoSeatLayoutResponse(response.get(0))));

            }

        });

    }

    private SeatLayoutResponse convertoSeatLayoutResponse(SeatLayoutItem responseEntity) {

            String  data = responseEntity.getLayout();
            Log.d("Naveen"," data is " + data);
            Gson gson = new Gson();
            SeatLayoutResponse seatLayoutResponse = gson.fromJson(data, SeatLayoutResponse.class);
            return seatLayoutResponse;
    }


    private SeatLayoutViewModel convertResponseToViewModel(SeatLayoutResponse response) {
        seatLayoutViewModel = new SeatLayoutViewModel();
        seatLayoutViewModel = SeatLayoutResponseToSeatLayoutViewModelMapper.map(response, seatLayoutViewModel);

        return seatLayoutViewModel;
    }

    @Override
    public void validateSelection() {

    }

    public void setTicketPrice(int numOfTickets) {
        getView().setTicketPrice(numOfTickets);

    }
}
