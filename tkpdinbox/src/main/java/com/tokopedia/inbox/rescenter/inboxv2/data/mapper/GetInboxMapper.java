package com.tokopedia.inbox.rescenter.inboxv2.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.inboxv2.data.pojo.InboxDataResponse;
import com.tokopedia.inbox.rescenter.inboxv2.data.pojo.InboxResponse;
import com.tokopedia.inbox.rescenter.inboxv2.data.pojo.ProductResponse;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemResultViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

import static com.tokopedia.core.network.ErrorMessageException.DEFAULT_ERROR;

/**
 * Created by yfsx on 24/01/18.
 */

public class GetInboxMapper implements Func1<Response<TkpdResponse>, InboxItemResultViewModel> {

    public static final int ACTION_BY_USER = 1;
    public static final int ACTION_BY_SELLER = 2;
    public static final int ACTION_BY_ADMIN = 3;
    public static final int ACTION_BY_SYSTEM = 4;

    @Override
    public InboxItemResultViewModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private InboxItemResultViewModel mappingResponse(Response<TkpdResponse> response) {
        InboxResponse dataResponse = response.body().convertDataObj(
                InboxResponse.class);
        InboxItemResultViewModel model = new InboxItemResultViewModel(
              dataResponse.getInboxes() != null ?
                      mappingInboxItem(dataResponse.getInboxes(), dataResponse.getActionBy()) :
                      null
        );
        if (response.isSuccessful()) {
            if (response.raw().code() == ResponseStatus.SC_OK) {
                if (response.body().isNullData()) {
                    if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                        throw new ErrorMessageException(response.body().getErrorMessageJoined());
                    } else {
                        throw new ErrorMessageException(DEFAULT_ERROR);
                    }
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }

    private List<InboxItemViewModel> mappingInboxItem(List<InboxDataResponse> responseList, int actionBy) {
        List<InboxItemViewModel> itemList = new ArrayList<>();
        for (InboxDataResponse response : responseList) {
            InboxItemViewModel item = new InboxItemViewModel(
                    response.getId(),
                    actionBy,
                    response.getResolution().getStatus().getString(),
                    "#ffffff",
                    "#000000",
                    response.getOrder().getRefNum(),
                    true,
                    actionBy == ACTION_BY_USER ? "Penjual:" : "Pembeli:",
                    actionBy == ACTION_BY_USER ? response.getShop().getName() : response.getCustomer().getName(),
                    response.getResolution().getAutoExecuteTime().getString(),
                    "#ffffff",
                    "#000000",
                    response.getResolution().getLastReplyTime().getString(),
                    response.getResolution().getFreeReturn() == 1 ? "Ya" : "-",
                    response.getResolution().getProduct() != null ? mappingProductImage(response.getResolution().getProduct()) : null,
                    buildStringForExtraImage(response.getResolution().getProduct()),
                    response.getCustomer() != null ? response.getCustomer().getName() : "",
                    response.getShop() != null ? response.getShop().getName() : ""
            );
            itemList.add(item);
        }
        return itemList;
    }

    private List<String> mappingProductImage(List<ProductResponse> responseList) {
        List<String> dataList = new ArrayList<>();
        for (ProductResponse response : responseList) {
            String data = response.getImages().get(0).getThumb();
            dataList.add(data);
        }
        return dataList;
    }

    private String buildStringForExtraImage(List<ProductResponse> responseList) {
        return responseList.size() > 3 ?
                "+" + String.valueOf(responseList.size() - 3).concat(" Produk Lainnya") :
                "";
    }
}
