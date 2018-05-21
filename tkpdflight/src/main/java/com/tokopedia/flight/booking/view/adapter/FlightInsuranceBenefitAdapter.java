package com.tokopedia.flight.booking.view.adapter;

import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightInsuranceBenefitViewModel;

import java.util.List;

public class FlightInsuranceBenefitAdapter extends RecyclerView.Adapter<FlightInsuranceBenefitAdapter.ViewHolder> {
    private List<FlightInsuranceBenefitViewModel> benefitViewModels;

    public FlightInsuranceBenefitAdapter(List<FlightInsuranceBenefitViewModel> benefitViewModels) {
        this.benefitViewModels = benefitViewModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight_benefit_insurance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(benefitViewModels.get(position));
    }

    @Override
    public int getItemCount() {
        return benefitViewModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView logoImageView;
        private AppCompatTextView titleTextView;
        private AppCompatTextView descriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            logoImageView = itemView.findViewById(R.id.iv_logo);
            titleTextView = itemView.findViewById(R.id.tv_title);
            descriptionTextView = itemView.findViewById(R.id.tv_description);
        }

        public void bind(FlightInsuranceBenefitViewModel benefitViewModel) {
            ImageHandler.loadImageWithoutPlaceholder(logoImageView, benefitViewModel.getIcon(),
                    VectorDrawableCompat.create(itemView.getResources(), R.drawable.ic_airline_default, itemView.getContext().getTheme()));
            titleTextView.setText(benefitViewModel.getTitle());
            descriptionTextView.setText(benefitViewModel.getDescription());
        }
    }
}
