package com.tokopedia.flight.search.view.model.statistic;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.search.view.model.DepartureTimeEnum;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.RefundableEnum;
import com.tokopedia.flight.search.view.model.TransitEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 10/30/2017.
 */

public class FlightSearchStatisticModel implements Parcelable {
    private int minPrice;
    private int maxPrice;
    private int minDuration;
    private int maxDuration;
    private List<TransitStat> transitTypeStatList;
    private List<AirlineStat> airlineStatList;
    private List<DepartureStat> departureTimeStatList;
    private List<RefundableEnum> refundableTypeList;

    public FlightSearchStatisticModel(List<FlightSearchViewModel> flightSearchViewModelList) {
        minPrice = Integer.MAX_VALUE;
        maxPrice = Integer.MIN_VALUE;
        minDuration = Integer.MAX_VALUE;
        maxDuration = Integer.MIN_VALUE;
        transitTypeStatList = new ArrayList<>();
        airlineStatList = new ArrayList<>();
        departureTimeStatList = new ArrayList<>();
        refundableTypeList = new ArrayList<>();

        SparseIntArray transitIDTrackArray = new SparseIntArray();
        HashMap<String, Integer> airlineIDTrackArray = new HashMap<>();
        SparseIntArray departureIDTrackArray = new SparseIntArray();

        for (int i = 0, sizei = flightSearchViewModelList.size(); i < sizei; i++) {
            FlightSearchViewModel flightSearchViewModel = flightSearchViewModelList.get(i);
            int price = flightSearchViewModel.getTotalNumeric();
            if (price < minPrice) {
                minPrice = price;
            }
            if (price > maxPrice) {
                maxPrice = price;
            }
            int duration = flightSearchViewModel.getDurationMinute();
            if (duration < minDuration) {
                minDuration = duration;
            }
            if (duration > maxDuration) {
                maxDuration = duration;
            }
            // populate total transit and minprice per each transit
            int totalTransit = flightSearchViewModel.getTotalTransit();
            TransitEnum transitTypeDef;
            switch (totalTransit) {
                case 0:
                    transitTypeDef = TransitEnum.DIRECT;
                    break;
                case 1:
                    transitTypeDef = TransitEnum.ONE;
                    break;
                case 2:
                    transitTypeDef = TransitEnum.TWO;
                    break;
                default:
                    transitTypeDef = TransitEnum.THREE_OR_MORE;
                    break;
            }
            if (transitIDTrackArray.get(transitTypeDef.getId(), -1) == -1) {
                transitTypeStatList.add(new TransitStat(transitTypeDef, price));
                transitIDTrackArray.put(transitTypeDef.getId(), transitTypeStatList.size() - 1);
            } else {
                int index = transitIDTrackArray.get(transitTypeDef.getId());
                TransitStat prevTransitStat = transitTypeStatList.get(index);
                if (price < prevTransitStat.getMinPrice()) {
                    prevTransitStat.setMinPrice(price);
                }
            }

            // populate airline and minprice per each airline
            List<FlightAirlineDB> airlineData = flightSearchViewModel.getAirlineList();
            if (airlineData != null) {
                for (int j = 0, sizej = airlineData.size(); j < sizej; j++) {
                    FlightAirlineDB flightAirlineDB = airlineData.get(j);
                    String airlineID = flightAirlineDB.getId();

                    if (!airlineIDTrackArray.containsKey(airlineID)) {
                        airlineStatList.add(new AirlineStat(flightAirlineDB, price));
                        airlineIDTrackArray.put(airlineID, airlineStatList.size() - 1);
                    } else {
                        int index = airlineIDTrackArray.get(airlineID);
                        AirlineStat prevAirlineStat = airlineStatList.get(index);
                        if (price < prevAirlineStat.getMinPrice()) {
                            prevAirlineStat.setMinPrice(price);
                        }
                    }
                }
            }

            // populate departureTime and minprice per each time
            int departureTimeInt = flightSearchViewModel.getDepartureTimeInt();
            DepartureTimeEnum departureTimeDef;
            if (departureTimeInt >= 0 && departureTimeInt <= 559) {
                departureTimeDef = DepartureTimeEnum._00;
            } else if (departureTimeInt >= 600 && departureTimeInt <= 1159) {
                departureTimeDef = DepartureTimeEnum._06;
            } else if (departureTimeInt >= 1200 && departureTimeInt <= 1759) {
                departureTimeDef = DepartureTimeEnum._12;
            } else {
                departureTimeDef = DepartureTimeEnum._18;
            }
            if (departureIDTrackArray.get(departureTimeDef.getId(), -1) == -1) {
                departureTimeStatList.add(new DepartureStat(departureTimeDef, price));
                departureIDTrackArray.put(departureTimeDef.getId(), departureTimeStatList.size() - 1);
            } else {
                int index = departureIDTrackArray.get(departureTimeDef.getId());
                DepartureStat prevDepartureStat = departureTimeStatList.get(index);
                if (price < prevDepartureStat.getMinPrice()) {
                    prevDepartureStat.setMinPrice(price);
                }
            }

            // populate total transit and minprice per each transit
            boolean refundable = flightSearchViewModel.isRefundable();
            RefundableEnum refundableTypeDef = refundable? RefundableEnum.REFUNDABLE : RefundableEnum.NOT_REFUNDABLE;

            if (!refundableTypeList.contains(refundableTypeDef)) {
                refundableTypeList.add(refundableTypeDef);
            }

        }

        //sort array
        Collections.sort(transitTypeStatList, new Comparator<TransitStat>() {
            @Override
            public int compare(TransitStat o1, TransitStat o2) {
                return o1.getTransitType().getId() - o2.getTransitType().getId();
            }
        });
        Collections.sort(airlineStatList, new Comparator<AirlineStat>() {
            @Override
            public int compare(AirlineStat o1, AirlineStat o2) {
                return o1.getAirlineDB().getName().compareTo(o2.getAirlineDB().getName());
            }
        });
        Collections.sort(departureTimeStatList, new Comparator<DepartureStat>() {
            @Override
            public int compare(DepartureStat o1, DepartureStat o2) {
                return o1.getDepartureTime().getId() - o2.getDepartureTime().getId();
            }
        });
        Collections.sort(refundableTypeList, new Comparator<RefundableEnum>() {
            @Override
            public int compare(RefundableEnum o1, RefundableEnum o2) {
                return o1.getId() - o2.getId();
            }
        });
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public int getMinDuration() {
        return minDuration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public List<AirlineStat> getAirlineStatList() {
        return airlineStatList;
    }

    public List<DepartureStat> getDepartureTimeStatList() {
        return departureTimeStatList;
    }

    public List<TransitStat> getTransitTypeStatList() {
        return transitTypeStatList;
    }

    public List<RefundableEnum> getRefundableTypeList() {
        return refundableTypeList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.minPrice);
        dest.writeInt(this.maxPrice);
        dest.writeInt(this.minDuration);
        dest.writeInt(this.maxDuration);
        dest.writeTypedList(this.transitTypeStatList);
        dest.writeTypedList(this.airlineStatList);
        dest.writeTypedList(this.departureTimeStatList);
        dest.writeList(this.refundableTypeList);
    }

    protected FlightSearchStatisticModel(Parcel in) {
        this.minPrice = in.readInt();
        this.maxPrice = in.readInt();
        this.minDuration = in.readInt();
        this.maxDuration = in.readInt();
        this.transitTypeStatList = in.createTypedArrayList(TransitStat.CREATOR);
        this.airlineStatList = in.createTypedArrayList(AirlineStat.CREATOR);
        this.departureTimeStatList = in.createTypedArrayList(DepartureStat.CREATOR);
        this.refundableTypeList = new ArrayList<RefundableEnum>();
        in.readList(this.refundableTypeList, RefundableEnum.class.getClassLoader());
    }

    public static final Parcelable.Creator<FlightSearchStatisticModel> CREATOR = new Parcelable.Creator<FlightSearchStatisticModel>() {
        @Override
        public FlightSearchStatisticModel createFromParcel(Parcel source) {
            return new FlightSearchStatisticModel(source);
        }

        @Override
        public FlightSearchStatisticModel[] newArray(int size) {
            return new FlightSearchStatisticModel[size];
        }
    };
}
