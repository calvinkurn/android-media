package com.tokopedia.flight.booking.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.design.label.selection.SelectionItem;
import com.tokopedia.design.label.selection.SelectionLabelView;
import com.tokopedia.design.label.selection.text.SelectionTextLabelView;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 11/22/17.
 */

public class FlightBookingPassengerMealAdapter extends RecyclerView.Adapter<FlightBookingPassengerMealAdapter.ViewHolder> {
    private List<FlightBookingMealMetaViewModel> viewModels;
    private List<FlightBookingMealMetaViewModel> selecetedViewModels;
    private OnAdapterInteractionListener interactionListener;

    public interface OnAdapterInteractionListener {
        void onItemDeleteMeal(FlightBookingMealMetaViewModel viewModel);

        void onItemOptionClicked(FlightBookingMealMetaViewModel viewModel);
    }

    public FlightBookingPassengerMealAdapter() {
        viewModels = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight_booking_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(viewModels.get(position));
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SelectionTextLabelView selectionTextLabelView;

        public ViewHolder(View itemView) {
            super(itemView);
            selectionTextLabelView = (SelectionTextLabelView) itemView.findViewById(R.id.stl_text_label);
        }

        public void bind(final FlightBookingMealMetaViewModel viewModel) {
            List<SelectionItem<String>> selectionItemList = new ArrayList<>();
            FlightBookingMealMetaViewModel selected = null;
            if (selecetedViewModels != null && selecetedViewModels.size() > 0) {
                int index = selecetedViewModels.indexOf(viewModel);
                if (index != -1) {
                    selected = selecetedViewModels.get(index);
                    for (FlightBookingMealViewModel mealViewModel : selected.getMealViewModels()) {
                        SelectionItem<String> selectionItem = new SelectionItem<>();
                        selectionItem.setKey(String.valueOf(mealViewModel.getId()));
                        selectionItem.setValue(mealViewModel.getTitle());
                        selectionItemList.add(selectionItem);
                    }
                }
            }

            selectionTextLabelView.setOnDeleteListener(new SelectionLabelView.OnDeleteListener<SelectionItem<String>>() {
                @Override
                public void onDelete(SelectionItem<String> stringSelectionItem) {
                    if (interactionListener != null) {
                        int index = selecetedViewModels.indexOf(viewModel);
                        if (index != -1) {
                            FlightBookingMealMetaViewModel selected = selecetedViewModels.get(index);
                            List<FlightBookingMealViewModel> viewModels = selected.getMealViewModels();
                            for (int i = 0, size = viewModels.size(); i < size; i++) {
                                FlightBookingMealViewModel viewModel1 = viewModels.get(i);
                                if (viewModel1.getId().equalsIgnoreCase(stringSelectionItem.getKey())) {
                                    viewModels.remove(i);
                                    interactionListener.onItemDeleteMeal(selected);
                                    break;
                                }
                            }
                        }

                    }
                }
            });
            selectionTextLabelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (interactionListener != null)
                        interactionListener.onItemOptionClicked(viewModel);

                }
            });
            if (selectionItemList.size() > 0) {
                selectionTextLabelView.setContentText(itemView.getContext().getString(R.string.flight_booking_passenger_meal_change_label));
            } else {
                selectionTextLabelView.setContentText(itemView.getContext().getString(R.string.flight_booking_passenger_meal_fill_label));
            }
            selectionTextLabelView.setArrow(true);
            selectionTextLabelView.setTitle(viewModel.getDescription());
            selectionTextLabelView.setItemList(selectionItemList);
        }
    }

    public void setViewModels(List<FlightBookingMealMetaViewModel> viewModels) {
        this.viewModels = viewModels;
    }

    public void setSelecetedViewModels(List<FlightBookingMealMetaViewModel> viewModels) {
        this.selecetedViewModels = viewModels;
    }

    public void setInteractionListener(OnAdapterInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }
}
