package com.tokopedia.flight.search.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.intdef.CurrencyEnum;
import com.tokopedia.design.label.selection.SelectionItem;
import com.tokopedia.design.label.selection.SelectionLabelView;
import com.tokopedia.design.label.selection.text.SelectionTextLabelView;
import com.tokopedia.design.text.DecimalRangeInputView;
import com.tokopedia.design.text.RangeInputView;
import com.tokopedia.design.text.watcher.CurrencyTextWatcher;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.fragment.flightinterface.OnFlightFilterListener;
import com.tokopedia.flight.search.view.fragment.flightinterface.OnFlightBaseFilterListener;
import com.tokopedia.flight.search.view.model.filter.DepartureTimeEnum;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.filter.RefundableEnum;
import com.tokopedia.flight.search.view.model.filter.TransitEnum;
import com.tokopedia.flight.search.view.model.resultstatistics.FlightSearchStatisticModel;
import com.tokopedia.flight.search.view.textwatcher.DurationTextWatcher;

import java.util.ArrayList;
import java.util.List;

public class FlightSearchFilterFragment extends BaseDaggerFragment implements OnFlightBaseFilterListener {

    private DurationTextWatcher minValueDurationTextWatcher;
    private DurationTextWatcher maxValueDurationTextWatcher;
    private CurrencyTextWatcher minValueCurrencyTextWatcher;
    private CurrencyTextWatcher maxValueCurrencyTextWatcher;

    public static FlightSearchFilterFragment newInstance() {

        Bundle args = new Bundle();

        FlightSearchFilterFragment fragment = new FlightSearchFilterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private OnFlightSearchFilterFragmentListener onFilterFragmentListener;

    public interface OnFlightSearchFilterFragmentListener extends OnFlightFilterListener {
        void onTransitLabelClicked();

        void onAirlineLabelClicked();

        void onRefundLabelClicked();

        void onDepartureLabelClicked();
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_search_filter, container, false);
        FlightFilterModel filterModel = onFilterFragmentListener.getFlightFilterModel();
        FlightSearchStatisticModel statModel = onFilterFragmentListener.getFlightSearchStatisticModel();

        populateViews(view, filterModel, statModel);

        view.requestFocus();
        return view;
    }

    private void populateViews(View view, FlightFilterModel filterModel, FlightSearchStatisticModel statModel){
        populatePrice(view, filterModel, statModel);
        populateDuration(view, filterModel, statModel);
        populateTransitLabel(view, filterModel);
        populateAirlineLabel(view, filterModel, statModel);
        populateDepartureLabel(view, filterModel);
        populateRefundLabel(view, filterModel);
    }

    public void resetFilter() {
        View view = getView();
        if (view == null) {
            return;
        }
        FlightFilterModel filterModel = new FlightFilterModel();
        FlightSearchStatisticModel statModel = onFilterFragmentListener.getFlightSearchStatisticModel();
        populateViews(view, filterModel, statModel);
        onFilterFragmentListener.onFilterModelChanged(filterModel);
    }

    @Override
    public void changeFilterToOriginal() {
        // no operation, this fragment is root.
    }

    private void populateDuration(View view, FlightFilterModel filterModel, FlightSearchStatisticModel statModel) {
        RangeInputView durationDecimalRangeInputView = (RangeInputView) view.findViewById(R.id.duration_range_input_view);
        int statMinDur = statModel.getMinDuration();
        int statMaxDur = statModel.getMaxDuration();
        int filterMinDur = filterModel.getDurationMin();
        int filterMaxDur = filterModel.getDurationMax();
        if (filterMinDur < statMinDur) {
            filterMinDur = statMinDur;
            filterModel.setDurationMin(statMinDur);
        }
        if (filterMaxDur > statMaxDur) {
            filterMaxDur = statMaxDur;
            filterModel.setDurationMax(statMaxDur);
        }
        EditText minValueEditText = durationDecimalRangeInputView.getMinValueEditText();
        if (minValueDurationTextWatcher!= null) {
            minValueEditText.removeTextChangedListener(minValueDurationTextWatcher);
        }
        minValueDurationTextWatcher = new DurationTextWatcher(minValueEditText);
        minValueEditText.addTextChangedListener(minValueDurationTextWatcher);
        EditText maxValueEditText = durationDecimalRangeInputView.getMaxValueEditText();
        if (maxValueDurationTextWatcher != null) {
            maxValueEditText.removeTextChangedListener(maxValueDurationTextWatcher);
        }
        maxValueDurationTextWatcher = new DurationTextWatcher(maxValueEditText);
        maxValueEditText.addTextChangedListener(maxValueDurationTextWatcher);

        durationDecimalRangeInputView.setPower(1);
        durationDecimalRangeInputView.setData(statMinDur, statMaxDur, filterMinDur, filterMaxDur);
        durationDecimalRangeInputView.setOnValueChangedListener(new DecimalRangeInputView.OnValueChangedListener() {
            @Override
            public void onValueChanged(int minValue, int maxValue, int minBound, int maxBound) {
                FlightFilterModel flightFilterModel = onFilterFragmentListener.getFlightFilterModel();
                flightFilterModel.setDurationMin(minValue);
                flightFilterModel.setDurationMax(maxValue);
                onFilterFragmentListener.onFilterModelChanged(flightFilterModel);
            }
        });
    }

    private void populatePrice(View view, FlightFilterModel filterModel, FlightSearchStatisticModel statModel) {
        RangeInputView priceRangeInputView = (RangeInputView) view.findViewById(R.id.price_range_input_view);
        int statMinPrice = statModel.getMinPrice();
        int statMaxPrice = statModel.getMaxPrice();
        int filterMinPrice = filterModel.getPriceMin();
        int filterMaxPrice = filterModel.getPriceMax();
        if (filterMinPrice < statMinPrice) {
            filterMinPrice = statMinPrice;
            filterModel.setPriceMin(statMinPrice);
        }
        if (filterMaxPrice > statMaxPrice) {
            filterMaxPrice = statMaxPrice;
            filterModel.setPriceMax(filterMaxPrice);
        }

        EditText minValueEditText = priceRangeInputView.getMinValueEditText();
        if (minValueCurrencyTextWatcher != null) {
            minValueEditText.removeTextChangedListener(minValueCurrencyTextWatcher);
        }
        minValueCurrencyTextWatcher = new CurrencyTextWatcher(minValueEditText, CurrencyEnum.RP);
        minValueEditText.addTextChangedListener(minValueCurrencyTextWatcher);
        EditText maxValueEditText = priceRangeInputView.getMaxValueEditText();
        if (maxValueCurrencyTextWatcher != null) {
            maxValueEditText.removeTextChangedListener(maxValueCurrencyTextWatcher);
        }
        maxValueCurrencyTextWatcher = new CurrencyTextWatcher(maxValueEditText, CurrencyEnum.RP);
        maxValueEditText.addTextChangedListener(maxValueCurrencyTextWatcher);

        priceRangeInputView.setPower(1);
        priceRangeInputView.setData(statMinPrice, statMaxPrice, filterMinPrice, filterMaxPrice);
        priceRangeInputView.setOnValueChangedListener(new DecimalRangeInputView.OnValueChangedListener() {
            @Override
            public void onValueChanged(int minValue, int maxValue, int minBound, int maxBound) {
                FlightFilterModel flightFilterModel = onFilterFragmentListener.getFlightFilterModel();
                flightFilterModel.setPriceMin(minValue);
                flightFilterModel.setPriceMax(maxValue);
                onFilterFragmentListener.onFilterModelChanged(flightFilterModel);
            }
        });
    }

    private void populateTransitLabel(View view, FlightFilterModel filterModel) {
        SelectionTextLabelView transitSelectionTextLabelView = (SelectionTextLabelView) view.findViewById(R.id.selection_text_label_layout_transit);
        final List<SelectionItem<String>> selectionItemList = new ArrayList<>();
        if (filterModel.getTransitTypeList() != null) {
            for (int i = 0, sizei = filterModel.getTransitTypeList().size(); i < sizei; i++) {
                TransitEnum transitEnum = filterModel.getTransitTypeList().get(i);
                SelectionItem<String> selectionItem = new SelectionItem<>();
                selectionItem.setKey(String.valueOf(transitEnum.getId()));
                selectionItem.setValue(getString(transitEnum.getValueRes()));
                selectionItemList.add(selectionItem);
            }
        }
        transitSelectionTextLabelView.setItemList(selectionItemList);
        transitSelectionTextLabelView.setOnDeleteListener(new SelectionLabelView.OnDeleteListener<SelectionItem<String>>() {
            @Override
            public void onDelete(SelectionItem<String> selectionItem) {
                onDeleteTransit(selectionItem.getKey());
            }
        });
        transitSelectionTextLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterFragmentListener.onTransitLabelClicked();
            }
        });
    }

    private void populateDepartureLabel(View view, FlightFilterModel filterModel) {
        SelectionTextLabelView departureTimeSelectionTextLabelView = (SelectionTextLabelView) view.findViewById(R.id.selection_text_label_layout_departure_time);

        final List<SelectionItem<String>> selectionItemList = new ArrayList<>();
        List<DepartureTimeEnum> departureTimeEnumList = filterModel.getDepartureTimeList();
        if (departureTimeEnumList != null) {
            for (int i = 0, sizei = departureTimeEnumList.size(); i < sizei; i++) {
                DepartureTimeEnum departureTimeEnum = departureTimeEnumList.get(i);
                SelectionItem<String> selectionItem = new SelectionItem<>();
                selectionItem.setKey(String.valueOf(departureTimeEnum.getId()));
                selectionItem.setValue(getString(departureTimeEnum.getValueRes()));
                selectionItemList.add(selectionItem);
            }
        }
        departureTimeSelectionTextLabelView.setItemList(selectionItemList);

        departureTimeSelectionTextLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterFragmentListener.onDepartureLabelClicked();
            }
        });
        departureTimeSelectionTextLabelView.setOnDeleteListener(new SelectionLabelView.OnDeleteListener<SelectionItem<String>>() {
            @Override
            public void onDelete(SelectionItem<String> selectionItem) {
                onDeleteDeparture(selectionItem.getKey());
            }
        });
    }

    private void populateAirlineLabel(View view, FlightFilterModel filterModel, FlightSearchStatisticModel statModel) {
        SelectionTextLabelView airlineSelectionTextLabelView = (SelectionTextLabelView) view.findViewById(R.id.selection_text_label_layout_airline);

        final List<SelectionItem<String>> selectionItemList = new ArrayList<>();
        List<String> airlineList = filterModel.getAirlineList();
        if (airlineList != null) {
            for (int i = 0, sizei = airlineList.size(); i < sizei; i++) {
                String airline = airlineList.get(i);
                SelectionItem<String> selectionItem = new SelectionItem<>();
                selectionItem.setKey(airline);
                selectionItem.setValue(statModel.getAirline(airline).getName());
                selectionItemList.add(selectionItem);
            }
        }
        airlineSelectionTextLabelView.setItemList(selectionItemList);

        airlineSelectionTextLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterFragmentListener.onAirlineLabelClicked();
            }
        });
        airlineSelectionTextLabelView.setOnDeleteListener(new SelectionLabelView.OnDeleteListener<SelectionItem<String>>() {
            @Override
            public void onDelete(SelectionItem<String> selectionItem) {
                onDeleteAirline(selectionItem.getKey());
            }
        });
    }

    private void populateRefundLabel(View view, FlightFilterModel filterModel) {
        SelectionTextLabelView refundPolicySelectionTextLabelView = (SelectionTextLabelView) view.findViewById(R.id.selection_text_label_layout_refund_policy);

        final List<SelectionItem<String>> selectionItemList = new ArrayList<>();
        List<RefundableEnum> refundableEnumList = filterModel.getRefundableTypeList();
        if (refundableEnumList != null) {
            for (int i = 0, sizei = refundableEnumList.size(); i < sizei; i++) {
                RefundableEnum refundableEnum = refundableEnumList.get(i);
                SelectionItem<String> selectionItem = new SelectionItem<>();
                selectionItem.setKey(String.valueOf(refundableEnum.getId()));
                selectionItem.setValue(getString(refundableEnum.getValueRes()));
                selectionItemList.add(selectionItem);
            }
        }
        refundPolicySelectionTextLabelView.setItemList(selectionItemList);

        refundPolicySelectionTextLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterFragmentListener.onRefundLabelClicked();
            }
        });
        refundPolicySelectionTextLabelView.setOnDeleteListener(new SelectionLabelView.OnDeleteListener<SelectionItem<String>>() {
            @Override
            public void onDelete(SelectionItem<String> selectionItem) {
                onDeleteRefund(selectionItem.getKey());
            }
        });
    }

    private void onDeleteTransit(String transitKey) {
        FlightFilterModel flightFilterModel = onFilterFragmentListener.getFlightFilterModel();
        List<TransitEnum> transitEnumList = flightFilterModel.getTransitTypeList();
        for (int i = 0, sizei = transitEnumList.size(); i < sizei; i++) {
            TransitEnum transitEnum = transitEnumList.get(i);
            if (transitKey.equals(String.valueOf(transitEnum.getId()))) {
                transitEnumList.remove(transitEnum);
                break;
            }
        }
        flightFilterModel.setTransitTypeList(transitEnumList);
        onFilterFragmentListener.onFilterModelChanged(flightFilterModel);
    }

    private void onDeleteDeparture(String departureKey) {
        FlightFilterModel flightFilterModel = onFilterFragmentListener.getFlightFilterModel();
        List<DepartureTimeEnum> departureTimeEnumList = flightFilterModel.getDepartureTimeList();
        for (int i = 0, sizei = departureTimeEnumList.size(); i < sizei; i++) {
            DepartureTimeEnum departureTimeEnum = departureTimeEnumList.get(i);
            if (departureKey.equals(String.valueOf(departureTimeEnum.getId()))) {
                departureTimeEnumList.remove(departureTimeEnum);
                break;
            }
        }
        flightFilterModel.setDepartureTimeList(departureTimeEnumList);
        onFilterFragmentListener.onFilterModelChanged(flightFilterModel);
    }

    private void onDeleteAirline(String airlineKey) {
        FlightFilterModel flightFilterModel = onFilterFragmentListener.getFlightFilterModel();
        List<String> airlineList = flightFilterModel.getAirlineList();
        for (int i = 0, sizei = airlineList.size(); i < sizei; i++) {
            String airline = airlineList.get(i);
            if (airlineKey.equals(airline)) {
                airlineList.remove(airline);
                break;
            }
        }
        flightFilterModel.setAirlineList(airlineList);
        onFilterFragmentListener.onFilterModelChanged(flightFilterModel);
    }

    private void onDeleteRefund(String refundKey) {
        FlightFilterModel flightFilterModel = onFilterFragmentListener.getFlightFilterModel();
        List<RefundableEnum> refundableEnumList = flightFilterModel.getRefundableTypeList();
        for (int i = 0, sizei = refundableEnumList.size(); i < sizei; i++) {
            RefundableEnum refundableEnum = refundableEnumList.get(i);
            if (refundKey.equals(String.valueOf(refundableEnum.getId()))) {
                refundableEnumList.remove(refundableEnum);
                break;
            }
        }
        flightFilterModel.setRefundableTypeList(refundableEnumList);
        onFilterFragmentListener.onFilterModelChanged(flightFilterModel);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void onAttachActivity(Context context) {
        onFilterFragmentListener = (OnFlightSearchFilterFragmentListener) context;
    }
}
