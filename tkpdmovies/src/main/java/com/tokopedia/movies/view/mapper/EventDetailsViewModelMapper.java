package com.tokopedia.movies.view.mapper;

import android.util.Log;

import com.tokopedia.movies.data.entity.response.Group;
import com.tokopedia.movies.data.entity.response.Package;
import com.tokopedia.movies.domain.model.EventDetailsDomain;
import com.tokopedia.movies.domain.model.GroupDomain;
import com.tokopedia.movies.domain.model.ScheduleDomain;
import com.tokopedia.movies.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.movies.view.viewmodel.GroupViewModel;
import com.tokopedia.movies.view.viewmodel.PackageViewModel;
import com.tokopedia.movies.view.viewmodel.SchedulesViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by pranaymohapatra on 27/11/17.
 */

public class EventDetailsViewModelMapper {
    public static void mapDomainToViewModel(EventDetailsDomain source, EventsDetailsViewModel target) {
        target.setTitle(source.getTitle());
        target.setCensortext(source.getCensortext());
        target.setConvenienceFee(source.getConvenienceFee());
        target.setDateRange(source.getDateRange());
        target.setDuration(source.getDuration());
        target.setGenre(source.getGenre());
        target.setHasSeatLayout(source.getHasSeatLayout());
        target.setImageApp(source.getImageApp());
        target.setIsFeatured(source.getIsFeatured());
        target.setIsFoodAvailable(source.getIsFoodAvailable());
        target.setLongRichDesc(source.getLongRichDesc());
        target.setMrp(source.getMrp());
        target.setOfferText(source.getOfferText());
        target.setPromotionText(source.getPromotionText());
        target.setRating(source.getRating());
        target.setSalesPrice(source.getSalesPrice());
        target.setSeatChartTypeId(source.getSeatChartTypeId());
        target.setTnc(source.getTnc());
        target.setDisplayTags(source.getDisplayTags());
        target.setUrl(source.getUrl());
        target.setThumbnailApp(source.getThumbnailApp());
        target.setThumbsDown(source.getThumbsDown());
        target.setThumbsUp(source.getThumbsUp());
        target.setForms(source.getForms());
        target.setCustomText1(source.getCustomText1());
        target.setCustomText2(source.getCustomText2());
        target.setCustomText3(source.getCustomText3());
        target.setTimeRange(convertEpochToString(source.getMinStartDate()) + " - " + convertEpochToString(source.getMaxEndDate()));
        int size = source.getSchedules().size();
        List<SchedulesViewModel> schedules = new ArrayList<>(size);
        if (source.getSchedules() != null) {
            for (ScheduleDomain item : source.getSchedules()) {
                SchedulesViewModel s = new SchedulesViewModel();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(item.getAddressDetail().getName()).
                        append(", ").append(item.getAddressDetail().getAddress())
                        .append(", ").append(item.getAddressDetail().getCity());
                s.setaDdress(stringBuilder.substring(0));
                s.setStartDate(item.getSchedule().getStartDate());
                s.setEndDate(item.getSchedule().getEndDate());
                //add groups to Schedules
                List<GroupViewModel> groupsList = new ArrayList<>();
                if (item.getGroups() != null) {
                    for (Group group : item.getGroups()) {
                        GroupViewModel gvm = new GroupViewModel();
                        gvm.setName(group.getName());
                        gvm.setDescription(group.getDescription());
                         List<PackageViewModel> packages = new ArrayList<>();
                        if (group.getPackages() != null) {
                            for (Package p : group.getPackages()) {
                                PackageViewModel pVM = new PackageViewModel();
                                pVM.setId(p.getId());
                                pVM.setProductId(p.getProductId());
                                pVM.setProductGroupId(p.getProductGroupId());
                                pVM.setProductScheduleId(p.getProductScheduleId());
                                pVM.setProviderScheduleId(p.getProviderScheduleId());
                                pVM.setDescription(p.getDescription());
                                pVM.setDisplayName(p.getDisplayName());
                                pVM.setAvailable(p.getAvailable());
                                pVM.setBooked(p.getBooked());
                                pVM.setTnc(p.getTnc());
                                pVM.setCommission(p.getCommission());
                                pVM.setCommissionType(p.getCommissionType());
                                pVM.setMaxQty(p.getMaxQty());
                                pVM.setMinQty(p.getMinQty());
                                pVM.setMrp(p.getMrp());
                                pVM.setProviderStatus(p.getProviderStatus());
                                pVM.setDisplayName(p.getDisplayName());
                                pVM.setSalesPrice(p.getSalesPrice());
                                pVM.setSold(p.getSold());
                                pVM.setConvenienceFee(p.getConvenienceFee());
                                pVM.setTimeRange(target.getTimeRange());
                                pVM.setThumbnailApp(target.getThumbnailApp());
                                pVM.setAddress(s.getaDdress());
                                pVM.setShowDate(p.getShowDate());
                                pVM.setFetchSectionUrl(p.getFetchSectionUrl());
                                packages.add(pVM);
                            }
                            gvm.setPackages(packages);
                            groupsList.add(gvm);
                        }
                    }
                    s.setGroups(groupsList);
                }
                schedules.add(s);
            }
        }

        target.setSchedulesViewModels(schedules);
    }

    static String convertEpochToString(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Long epochTime = time * 1000L;
        Date date = new Date(epochTime);
        String dateString = sdf.format(date);
        return dateString;
    }

}
