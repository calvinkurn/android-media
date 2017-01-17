package com.tokopedia.seller.topads.interactor;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.topads.model.data.DataCredit;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.data.GroupAdBulkAction;
import com.tokopedia.seller.topads.model.data.Product;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.data.TotalAd;
import com.tokopedia.seller.topads.model.request.DataRequest;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.model.request.SearchProductRequest;
import com.tokopedia.seller.topads.model.request.ShopRequest;
import com.tokopedia.seller.topads.model.request.StatisticRequest;
import com.tokopedia.seller.topads.model.response.DataResponse;
import com.tokopedia.seller.topads.model.response.PageDataResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public interface TopAdsDatePickerInteractor {

    void resetDate();

    void saveDate(Date startDate, Date endDate);

    Date getStartDate(Date defaultDate);

    Date getEndDate(Date defaultDate);

    void saveSelectionDatePicker(int selectionDatePickerType, int selectionDatePeriodIndex);

    int getLastSelectionDatePickerType();

    int getLastSelectionDatePickerIndex();
}