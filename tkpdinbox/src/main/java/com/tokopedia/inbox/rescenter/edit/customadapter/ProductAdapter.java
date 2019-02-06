package com.tokopedia.inbox.rescenter.edit.customadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.PassProductTrouble;
import com.tokopedia.inbox.rescenter.edit.model.passdata.ProductData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 7/29/16.
 */
public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SELECT_PRODUCT = 1991;
    private static final int TYPE_FORM_PRODUCT = 1990;
    private EditResCenterFormData.TroubleCategoryData categoryChoosen;
    private EditResCenterFormData formData;
    private Context context;
    private List<ProductData> listProduct;
    private List<EditResCenterFormData.TroubleData> listTrouble;
    private List<PassProductTrouble> listProductTroubleChoosen;
    private TroubleSpinnerAdapter troubleAdapter;

    public ProductAdapter(EditResCenterFormData formData) {
        this.formData = formData;
        this.listProduct = formData.getListProd();
        this.listProductTroubleChoosen = new ArrayList<>();
    }

    public ProductAdapter(List<PassProductTrouble> listProduct,
                          EditResCenterFormData.TroubleCategoryData categoryChoosen,
                          EditResCenterFormData formData) {
        this.listProductTroubleChoosen = listProduct;
        this.categoryChoosen = categoryChoosen;
        this.formData = formData;
    }

    public class FormViewHolder extends RecyclerView.ViewHolder {

        public ImageView avatar;
        public TextView title;
        public Spinner troubleSpinner;
        public View viewTotalValue;
        public View viewBoxDesc;
        public View actionDecrease;
        public View actionIncrease;
        public TextView value;
        public TextView fromTotalValue;
        public EditText boxDesc;

        public FormViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar_product);
            title = (TextView) itemView.findViewById(R.id.title_product);
            troubleSpinner = (Spinner) itemView.findViewById(R.id.spinner_trouble);
            viewTotalValue = itemView.findViewById(R.id.view_total_value);
            viewBoxDesc = itemView.findViewById(R.id.view_desc);
            actionDecrease = itemView.findViewById(R.id.decrease);
            actionIncrease = itemView.findViewById(R.id.increase);
            value = (TextView) itemView.findViewById(R.id.total_input);
            fromTotalValue = (TextView) itemView.findViewById(R.id.from_total_value);
            boxDesc = (EditText) itemView.findViewById(R.id.box_desc);
        }
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {

        public ImageView avatar;
        public TextView title;
        public CheckBox checkView;
        public View mainView;
        public View freeReturnView;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            mainView = itemView;
            avatar = (ImageView) itemView.findViewById(R.id.avatar_product);
            title = (TextView) itemView.findViewById(R.id.title_product);
            checkView = (CheckBox) itemView.findViewById(R.id.checkbox_product);
            freeReturnView = itemView.findViewById(R.id.view_free_return);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == TYPE_FORM_PRODUCT) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.layout_product_create_rescenter, parent, false);
            return new FormViewHolder(view);
        } else {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.layout_product_select_rescenter, parent, false);
            return new SimpleViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (listProductTroubleChoosen.isEmpty()) {
            return TYPE_SELECT_PRODUCT;
        } else {
            return TYPE_FORM_PRODUCT;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FORM_PRODUCT) {
            bindFormViewHolder((ProductAdapter.FormViewHolder) holder, position);
        } else {
            bindSimpleViewHolder((ProductAdapter.SimpleViewHolder) holder, position);
        }
    }

    public void bindSimpleViewHolder(final SimpleViewHolder holder, int position) {
        ImageHandler
                .loadImageRounded2(context, holder.avatar, listProduct.get(position).getPrimaryPhoto());
        holder.title.setText(listProduct.get(position).getProductName());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.checkView.setChecked(!holder.checkView.isChecked());
            }
        });
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.checkView.setChecked(!holder.checkView.isChecked());
            }
        });
        holder.freeReturnView.setVisibility(listProduct.get(position).getIsFreeReturn() == 1 ? View.VISIBLE : View.GONE);
        if (formData.getForm().getResolutionLast().getLastProductTrouble() != null) {
            for (EditResCenterFormData.LastProductTrouble singleData : formData.getForm().getResolutionLast().getLastProductTrouble()) {
                if (listProduct.get(position).getProductId().equals(singleData.getPtProductId())) {
                    holder.checkView.setChecked(true);
                }
            }
        }
    }

    /* ================================================================================= */
    /* ==================          for form product trouble            ================= */
    /* ================================================================================= */

    public void bindFormViewHolder(FormViewHolder holder, int position) {
        ProductData productData = listProductTroubleChoosen.get(position).getProductData();
        holder.viewBoxDesc.setVisibility(View.GONE);
        holder.viewTotalValue.setVisibility(View.GONE);
        renderSpinner(holder, productData);
        renderProductDetail(holder, productData);
        renderTotalValue(holder, productData.getQuantity());
        setPreviousProductTroubleData(holder, position);
    }

    private void renderSpinner(final FormViewHolder holder, ProductData productData) {
        troubleAdapter = new TroubleSpinnerAdapter(context, android.R.layout.simple_spinner_item, getListTrouble(productData));
        troubleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.troubleSpinner.setAdapter(troubleAdapter);
        holder.troubleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position != 0) {
                    holder.viewTotalValue.setVisibility(View.VISIBLE);
                    holder.viewBoxDesc.setVisibility(View.VISIBLE);
                } else {
                    holder.viewTotalValue.setVisibility(View.GONE);
                    holder.viewBoxDesc.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private List<EditResCenterFormData.TroubleData> getListTrouble(ProductData productData) {
        if (productData.getIsFreeReturn() == 1) {
            return categoryChoosen.getTroubleListFreeReturn();
        } else {
            return categoryChoosen.getTroubleList();
        }
    }

    private void renderProductDetail(FormViewHolder holder, ProductData productData) {
        ImageHandler
                .loadImageRounded2(context, holder.avatar, productData.getPrimaryPhoto());
        holder.title.setText(productData.getProductName());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 7/29/16 startactivity product
            }
        });
    }

    private void renderTotalValue(final FormViewHolder holder, final int quantity) {
        holder.fromTotalValue.setText(
                context.getString(R.string.retur_from_invoice).replace("XXX", String.valueOf(quantity))
        );
        holder.actionIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentValue = Integer.parseInt(holder.value.getText().toString());
                if (currentValue < quantity) {
                    holder.value.setText(String.valueOf(currentValue + 1));
                }
            }
        });
        holder.actionDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentValue = Integer.parseInt(holder.value.getText().toString());
                if (currentValue > 1) {
                    holder.value.setText(String.valueOf(currentValue - 1));
                }
            }
        });
    }

    private void setPreviousProductTroubleData(FormViewHolder holder, int position) {
        if (formData.getForm().getResolutionLast().getLastProductTrouble() == null || formData.getForm().getResolutionLast().getLastProductTrouble().isEmpty()) {
            return;
        }
        List<EditResCenterFormData.LastProductTrouble> list = formData.getForm().getResolutionLast().getLastProductTrouble();
        for (EditResCenterFormData.LastProductTrouble previousData : list) {

            if (previousData.getPtProductId().equals(listProductTroubleChoosen.get(position).getProductData().getProductId())) {
                holder.value.setText(String.valueOf(previousData.getPtQuantity()));
                holder.boxDesc.setText(previousData.getPtSolutionRemark());
                String previousTroubleID = String.valueOf(previousData.getPtTroubleId());
                for (int i = 0; i < troubleAdapter.getCount() - 1; i++) {
                    if (troubleAdapter.getItem(i) != null) {
                        if (troubleAdapter.getItem(i).getTroubleId().equals(previousTroubleID)) {
                            holder.troubleSpinner.setSelection(i + 1, true);
                        }
                    }
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        if (listProductTroubleChoosen == null || listProductTroubleChoosen.isEmpty()) {
            return listProduct.size();
        } else {
            return listProductTroubleChoosen.size();
        }
    }

    public List<ProductData> getAllProduct() {
        return listProduct;
    }

    public List<PassProductTrouble> getSelectedProduct() {
        return listProductTroubleChoosen;
    }

}
