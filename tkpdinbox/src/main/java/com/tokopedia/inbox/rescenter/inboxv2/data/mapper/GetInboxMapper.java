package com.tokopedia.inbox.rescenter.inboxv2.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.inboxv2.data.pojo.AutoExecuteTimeResponse;
import com.tokopedia.inbox.rescenter.inboxv2.data.pojo.FilterResponse;
import com.tokopedia.inbox.rescenter.inboxv2.data.pojo.InboxDataResponse;
import com.tokopedia.inbox.rescenter.inboxv2.data.pojo.InboxResponse;
import com.tokopedia.inbox.rescenter.inboxv2.data.pojo.ProductResponse;
import com.tokopedia.inbox.rescenter.inboxv2.data.pojo.QuickFilterResponse;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.FilterViewModel;
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

    public static final int STATUS_UNREAD = 1;
    public static final int STATUS_READ = 2;
    public static final int STATUS_RESO_CANCELED = 0;
    public static final int STATUS_RESO_FINISHED = 500;

    private static final String STATUS_SELLER = "Penjual :";
    private static final String STATUS_BUYER = "Pembeli :";
    private static final String STATUS_OTHER_PRODUCT = "Produk Lainnya";
    private static final String COLOR_BLACK_70 = "#b2000000";
    private static final String COLOR_WHITE = "#ffffff";
    private static final String STRING_YES = "Ya";

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
                        null,
                dataResponse.getQuickFilter() != null ?
                        mappingFilterItem(dataResponse.getQuickFilter()) :
                        null,
                dataResponse.isCanLoadMore()
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

    private List<FilterViewModel> mappingFilterItem(QuickFilterResponse response) {
        List<FilterViewModel> modelList = new ArrayList<>();

        FilterResponse unreadResponse = response.getUnread();
        FilterViewModel unreadModel = new FilterViewModel(
                unreadResponse.getTitle(),
                unreadResponse.getFilterWithDateString(),
                convertStringToHaveBreak(unreadResponse.getTitleCountFullString()),
                unreadResponse.getCount(),
                unreadResponse.getOrderValue(),
                false);
        modelList.add(unreadModel);

        FilterResponse unansweredResponse = response.getUnanswered();
        FilterViewModel unansweredModel = new FilterViewModel(
                unansweredResponse.getTitle(),
                unansweredResponse.getFilterWithDateString(),
                convertStringToHaveBreak(unansweredResponse.getTitleCountFullString()),
                unansweredResponse.getCount(),
                unansweredResponse.getOrderValue(),
                false);
        modelList.add(unansweredModel);

        FilterResponse finishedResponse = response.getFinished();
        FilterViewModel finishedModel = new FilterViewModel(
                finishedResponse.getTitle(),
                finishedResponse.getFilterWithDateString(),
                convertStringToHaveBreak(finishedResponse.getTitleCountFullString()),
                finishedResponse.getCount(),
                finishedResponse.getOrderValue(),
                false);
        modelList.add(finishedModel);

        FilterResponse autoExcecuteResponse = response.getAutoExecution();
        FilterViewModel autoExecuteModel = new FilterViewModel(
                autoExcecuteResponse.getTitle(),
                autoExcecuteResponse.getFilterWithDateString(),
                convertStringToHaveBreak(autoExcecuteResponse.getTitleCountFullString()),
                autoExcecuteResponse.getCount(),
                autoExcecuteResponse.getOrderValue(),
                false);
        modelList.add(autoExecuteModel);

        FilterResponse unfinishedResponse = response.getUnfinished();
        FilterViewModel unfinishedModel = new FilterViewModel(
                unfinishedResponse.getTitle(),
                unfinishedResponse.getFilterWithDateString(),
                convertStringToHaveBreak(unfinishedResponse.getTitleCountFullString()),
                unfinishedResponse.getCount(),
                unfinishedResponse.getOrderValue(),
                false);
        modelList.add(unfinishedModel);
        return modelList;
    }

    private List<InboxItemViewModel> mappingInboxItem(List<InboxDataResponse> responseList, int actionBy) {
        List<InboxItemViewModel> itemList = new ArrayList<>();
        for (InboxDataResponse response : responseList) {
            itemList.add(mappingItem(response, actionBy));
        }
        return itemList;
    }

    private String convertStringToHaveBreak(String string) {
        String[] strings = string.split(" ", 2);
        return strings[0] + "\n" + strings[1];
    }

    public static InboxItemViewModel mappingItem(InboxDataResponse response, int actionBy) {
        return new InboxItemViewModel(
                response.getId(),
                response.getResolution().getId(),
                actionBy,
                response.getResolution().getStatus().getString(),
                response.getResolution().getStatus().getBgColor(),
                response.getResolution().getStatus().getFontColor().startsWith("#") ?
                        response.getResolution().getStatus().getFontColor() :
                        COLOR_BLACK_70,
                response.getOrder().getRefNum(),
                response.getResolution().getRead() == STATUS_UNREAD,
                actionBy == ACTION_BY_USER ?
                        STATUS_SELLER :
                        STATUS_BUYER,
                actionBy == ACTION_BY_USER ?
                        response.getShop().getName() :
                        response.getCustomer().getName(),
                isShowAutoExecution(response.getResolution().getAutoExecuteTime(),
                        response.getResolution().getStatus().getIntX()) ?
                        response.getResolution().getAutoExecuteTime().getTimeLeft() :
                        "-",
                isShowAutoExecution(response.getResolution().getAutoExecuteTime(),
                        response.getResolution().getStatus().getIntX())?
                        response.getResolution().getAutoExecuteTime().getColor() : "",
                isShowAutoExecution(response.getResolution().getAutoExecuteTime(),
                        response.getResolution().getStatus().getIntX())?
                        COLOR_WHITE :
                        COLOR_BLACK_70,
                response.getResolution().getLastReplyTime().getFullString(),
                response.getResolution().getFreeReturn() == 1 ?
                        STRING_YES :
                        "-",
                response.getResolution().getProduct() != null ?
                        mappingProductImage(response.getResolution().getProduct()) :
                        null,
                response.getResolution().getProduct() != null ?
                        buildStringForExtraImage(response.getResolution().getProduct()) :
                        "",
                response.getCustomer() != null ?
                        response.getCustomer().getName() :
                        "",
                response.getShop() != null ?
                        response.getShop().getName() :
                        ""
        );
    }

    private static boolean isShowAutoExecution(AutoExecuteTimeResponse response, int status) {
        if (TextUtils.isEmpty(response.getTimeLeft())) return false;
        if (status == STATUS_RESO_FINISHED || status == STATUS_RESO_CANCELED) return false;
        return true;
    }

    private static List<String> mappingProductImage(List<ProductResponse> responseList) {
        List<String> dataList = new ArrayList<>();
        for (ProductResponse response : responseList) {
            String data = response.getImages().get(0).getThumb();
            dataList.add(data);
        }
        return dataList;
    }

    private static String buildStringForExtraImage(List<ProductResponse> responseList) {
        return responseList.size() > 3 ?
                "+" + String.valueOf(responseList.size() - 3)+ " " + STATUS_OTHER_PRODUCT :
                "";
    }
}
