package com.tokopedia.loyalty.domain.repository;

import android.annotation.SuppressLint;
import android.net.Uri;

import com.tokopedia.loyalty.domain.entity.response.promo.Children;
import com.tokopedia.loyalty.domain.entity.response.promo.GroupCode;
import com.tokopedia.loyalty.domain.entity.response.promo.MenuPromoResponse;
import com.tokopedia.loyalty.domain.entity.response.promo.PromoCode;
import com.tokopedia.loyalty.domain.entity.response.promo.PromoResponse;
import com.tokopedia.loyalty.view.data.PromoCodeViewModel;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.PromoMenuData;
import com.tokopedia.loyalty.view.data.PromoSubMenuData;
import com.tokopedia.loyalty.view.data.SingleCodeViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class PromoResponseMapper implements IPromoResponseMapper {

    private static final String TYPE_FILTER_ALL = "all";
    private static final String TITLE_FILTER_ALL = "Lihat Semua";
    private static final String QUERY_FLAG_APP = "flag_app";
    private static final String DEFAULT_VALUE_QUERY_FLAG_APP = "1";

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
            promoData.setTermsAndConditions(parseContent(promoResponse.getContent().getRendered()));
            promoData.setAppLink(promoResponse.getMeta().getAppLink());
            promoData.setMultiplePromoCodeCount(promoResponse.getPromoCodes().size());
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
            String urlPromo = Uri.parse(promoResponse.getLink())
                    .buildUpon()
                    .appendQueryParameter(QUERY_FLAG_APP, DEFAULT_VALUE_QUERY_FLAG_APP)
                    .build().toString();
            promoData.setPromoLink(urlPromo);
            promoData.setThumbnailImage(promoResponse.getMeta().getThumbnailImage());
            promoData.setMinTransaction(promoResponse.getMeta().getMinTransaction());
            promoData.setStartDate(promoResponse.getMeta().getStartDate());
            promoData.setEndDate(promoResponse.getMeta().getEndDate());
            promoData.setMultiplePromo(promoResponse.getPromoCodes().size() > 0);

            promoData.setPromoCodeList(getPromoCodes(promoResponse));
            promoData.setPromoCode(promoResponse.getMeta().getPromoCode());
            promoDataList.add(promoData);
        }
        return promoDataList;
    }

    private List<PromoCodeViewModel> getPromoCodes(PromoResponse promoResponse) {
        List<PromoCodeViewModel> promoCodeList = new ArrayList<>();

        if (promoResponse.getPromoCodes().isEmpty()) {
            List<SingleCodeViewModel> singleCodeViewModelList = new ArrayList<>();

            SingleCodeViewModel singleCodeViewModel = new SingleCodeViewModel();
            singleCodeViewModel.setSingleCode(promoResponse.getMeta().getPromoCode());
            singleCodeViewModelList.add(singleCodeViewModel);

            PromoCodeViewModel promoCodeViewModel = new PromoCodeViewModel();
            promoCodeViewModel.setGroupCode(singleCodeViewModelList);

            promoCodeList.add(promoCodeViewModel);

        } else {
            for (PromoCode promoCode : promoResponse.getPromoCodes()) {
                PromoCodeViewModel promoCodeViewModel = new PromoCodeViewModel();
                promoCodeViewModel.setGroupCodeTitle(promoCode.getGroupCodeTitle());
                promoCodeViewModel.setGroupCodeDescription(promoCode.getGroupCodeDescription());

                List<SingleCodeViewModel> singleCodeViewModelList = new ArrayList<>();
                for (GroupCode groupCode : promoCode.getGroupCode()) {
                    SingleCodeViewModel singleCodeViewModel = new SingleCodeViewModel();
                    singleCodeViewModel.setSingleCode(groupCode.getSingleCode());

                    singleCodeViewModelList.add(singleCodeViewModel);
                }
                promoCodeViewModel.setGroupCode(singleCodeViewModelList);

                promoCodeList.add(promoCodeViewModel);
            }
        }

        return promoCodeList;
    }

    private List<String> parseContent(String content) {
        List<String> termAndConditions = new ArrayList<>();

//        String regex = "[<li style=\"font-weight: 400;\">|<li>](.*?)</li>|<p>(.*?)</p>";
//
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(content);
//
//        while (matcher.find()) {
//            termAndConditions.add(matcher.group(1));
//        }

        termAndConditions.add(content);

        return termAndConditions;
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
