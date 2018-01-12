package com.tokopedia.loyalty.domain.repository;

import android.annotation.SuppressLint;

import com.tokopedia.loyalty.domain.entity.response.promo.Children;
import com.tokopedia.loyalty.domain.entity.response.promo.GroupCode;
import com.tokopedia.loyalty.domain.entity.response.promo.MenuPromoResponse;
import com.tokopedia.loyalty.domain.entity.response.promo.PromoCode;
import com.tokopedia.loyalty.domain.entity.response.promo.PromoResponse;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.PromoMenuData;
import com.tokopedia.loyalty.view.data.PromoSubMenuData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class PromoResponseMapper implements IPromoResponseMapper {

    private static final String TYPE_FILTER_ALL = "all";
    private static final String TITLE_FILTER_ALL = "Lihat Semua";

    @Inject
    public PromoResponseMapper() {
    }

    @Override
    public List<PromoData> convertPromoDataList(List<PromoResponse> promoResponseList) {
        List<PromoData> promoDataList = new ArrayList<>();
        for (PromoResponse promoResponse : promoResponseList) {
            PromoData promoData = new PromoData();
            promoData.setId(String.valueOf(promoResponse.getId()));
            promoData.setTitle(promoResponse.getTitle().getRendered());
            promoData.setAppLink(promoResponse.getMeta().getAppLink());
            promoData.setMultiplePromoCodeCount(promoResponse.getAcf().getPromoCodeList().size());
            try {
                promoData.setPeriodFormatted(
                        getDatePeriodPromo(
                                promoResponse.getMeta().getStartDate(),
                                promoResponse.getMeta().getEndDate()
                        )
                );
            } catch (ParseException e) {
                e.printStackTrace();
            }
            promoData.setPromoLink(promoResponse.getMeta().getPromoLink());
            promoData.setThumbnailImage(promoResponse.getMeta().getThumbnailImage());
            promoData.setMinTransaction(promoResponse.getMeta().getMinTransaction());
            promoData.setStartDate(promoResponse.getMeta().getStartDate());
            promoData.setEndDate(promoResponse.getMeta().getEndDate());
            promoData.setMultiplePromo(promoResponse.getAcf().isMultiplePromoCode());
            List<String> promoCodeList = new ArrayList<>();
            for (PromoCode promoCode : promoResponse.getAcf().getPromoCodeList()) {
                for (GroupCode groupCode : promoCode.getGroupCode()) {
                    promoCodeList.add(groupCode.getSingleCode());
                }
            }
            promoData.setPromoCodeList(promoCodeList);
            promoData.setPromoCode(promoResponse.getMeta().getPromoCode());
            promoDataList.add(promoData);
        }
        return promoDataList;
    }


    private String getDatePeriodPromo(String startDate, String endDate) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date dateStart = sdf.parse(startDate);
        Calendar cStart = Calendar.getInstance();
        cStart.setTime(dateStart);

        Date dateEnd = sdf.parse(endDate);
        Calendar cEnd = Calendar.getInstance();
        cEnd.setTime(dateEnd);

        int mStart = cStart.get(Calendar.MONTH);
        int mEnd = cEnd.get(Calendar.MONTH);

        String result;
        Locale localeId = new Locale("id", "ID");
        if (mStart == mEnd) {
            result = cStart.get(Calendar.DAY_OF_MONTH)
                    + " - "
                    + cEnd.get(Calendar.DAY_OF_MONTH)
                    + " "
                    + cEnd.getDisplayName(Calendar.MONTH, Calendar.LONG, localeId)
                    + " "
                    + cEnd.get(Calendar.YEAR);
        } else {
            result = cStart.get(Calendar.DAY_OF_MONTH)
                    + " "
                    + cStart.getDisplayName(Calendar.MONTH, Calendar.LONG, localeId)
                    + " - "
                    + cEnd.get(Calendar.DAY_OF_MONTH)
                    + " "
                    + cEnd.getDisplayName(Calendar.MONTH, Calendar.LONG, localeId)
                    + " "
                    + cEnd.get(Calendar.YEAR);
        }

        return result;
    }

    @Override
    public List<PromoMenuData> convertPromoMenuDataList(List<MenuPromoResponse> menuPromoResponseList) {
        List<PromoMenuData> promoMenuDataList = new ArrayList<>();
        for (MenuPromoResponse menuPromoResponse : menuPromoResponseList) {
            PromoMenuData promoMenuData = new PromoMenuData();
            promoMenuData.setTitle(menuPromoResponse.getTitle());
            promoMenuData.setMenuId(String.valueOf(menuPromoResponse.getIdMenu()));
            promoMenuData.setIconActive(menuPromoResponse.getIcon());
            promoMenuData.setIconNormal(menuPromoResponse.getIconOff());

            List<PromoSubMenuData> promoSubMenuDataList = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();
            for (Children children : menuPromoResponse.getChildrenList()) {
                PromoSubMenuData promoSubMenuData = new PromoSubMenuData();
                promoSubMenuData.setId(String.valueOf(children.getSubCategory().getTermId()));
                promoSubMenuData.setTitle(children.getSubCategory().getName());
                promoSubMenuDataList.add(promoSubMenuData);
                stringBuilder.append(children.getSubCategory().getTermId()).append(",");
            }
            String allCategoryIds = stringBuilder.toString();
            if (allCategoryIds.length() > 0 && allCategoryIds.charAt(allCategoryIds.length() - 1) == ',') {
                allCategoryIds = allCategoryIds.substring(0, allCategoryIds.length() - 1);
            }
            promoMenuData.setAllSubCategoryId(allCategoryIds);

            PromoSubMenuData promoSubMenuData = new PromoSubMenuData();
            promoSubMenuData.setId(TYPE_FILTER_ALL);
            promoSubMenuData.setTitle(TITLE_FILTER_ALL);
            promoSubMenuData.setSelected(true);
            promoSubMenuDataList.add(0, promoSubMenuData);
            promoMenuData.setPromoSubMenuDataList(promoSubMenuDataList);

            promoMenuDataList.add(promoMenuData);
        }
        return promoMenuDataList;
    }
}
