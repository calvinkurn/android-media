package com.tokopedia.inbox.contactus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.tokopedia.core.R;
import com.tokopedia.inbox.contactus.ContactUsConstant;
import com.tokopedia.inbox.contactus.model.contactuscategory.TicketCategory;
import com.tokopedia.core.customadapter.SpinnerMultiLine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 8/12/16.
 */
public class ContactUsAdapter implements ContactUsConstant{

    public interface AdapterListener {
        void onShowDescription(TicketCategory model);
        void addSpinner();
        void onRemoveDescription();
    }

    View view;
    Spinner category;
    LinearLayout layout;
    SpinnerMultiLine adapter;
    ContactUsAdapter childSpinner;
    Context context;
    AdapterListener listener;


    ArrayList<TicketCategory> modelList = new ArrayList<>();
    ArrayList<String> itemLists = new ArrayList<>();
    ArrayList<String> path = new ArrayList<>();
    String selectedMainCategory;


    public ContactUsAdapter(LinearLayout layout, AdapterListener listener) {
        this.layout = layout;
        this.modelList = new ArrayList<>();
        this.listener = listener;
    }

    public void appendSpinner(Context context, List<TicketCategory> data) {
        this.context = context;
        this.modelList.clear();
        this.modelList.add(generateFirstIndexModel(data));
        this.modelList.addAll(data);
        generateItemLists();

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(0, 1, 0, 0);
        view = LayoutInflater.from(context).inflate(R.layout.contact_us_spinner, null);
        view.setLayoutParams(param);
        category = (Spinner) view.findViewById(R.id.category);
        adapter = new SpinnerMultiLine(context, R.layout.simple_spinner_tv_res, itemLists);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);
        category.setOnItemSelectedListener(onCategorySelectedListener(context));
        layout.addView(view);
    }

    private TicketCategory generateFirstIndexModel(List<TicketCategory> data) {
        TicketCategory model = new TicketCategory();
        if (data.get(0)!= null && data.get(0).getTicketCategoryTreeNo() == 2) {
            model.setTicketCategoryName(context.getString(R.string.title_choose_problem));
        } else {
            model.setTicketCategoryName(context.getString(R.string.title_choose_detail_problem));
        }
        return model;
    }

    private void generateItemLists() {
        for (TicketCategory model : modelList) {
            itemLists.add(model.getTicketCategoryName());
        }
    }

    private AdapterView.OnItemSelectedListener onCategorySelectedListener(final Context context) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0)
                    onCategoryItemSelected(context, i);
                else {
                    removeChild();
                    listener.onRemoveDescription();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private void onCategoryItemSelected(Context context, int pos) {
        if (modelList.get(pos).getTicketCategoryChild() != null
                && modelList.get(pos).getTicketCategoryChild().size() != 0) {
            updateChildSpinner(context, pos);
            listener.onRemoveDescription();
        } else {
            listener.onShowDescription(modelList.get(pos));
            removeChild();
        }
    }

    private void updateChildSpinner(Context context, int pos) {
        if (childSpinner == null) {
            childSpinner = new ContactUsAdapter(layout, listener);
            childSpinner.appendSpinner(context, modelList.get(pos).getTicketCategoryChild());
            listener.addSpinner();
        } else {
            childSpinner.updateData(modelList.get(pos).getTicketCategoryChild());
        }
    }

    private void removeChild() {
        if (childSpinner != null) {
            childSpinner.removeView();
            childSpinner = null;
        }
    }

    public void updateData(List<TicketCategory> data) {
        category.setSelection(0);
        modelList.clear();
        itemLists.clear();
        this.modelList.add(generateFirstIndexModel(data));
        this.modelList.addAll(data);
        generateItemLists();
        adapter.notifyDataSetChanged();
        removeChild();
    }

    public void removeView() {
        removeChild();
        layout.removeView(view);
    }

    public TicketCategory getData(int type) {
        for (TicketCategory category : modelList){
            if(category.getTicketCategoryId() == type)
                return category;
        }
        return null;
    }

    public int getLastCatId() {
        if(childSpinner == null)
            return modelList.get(category.getSelectedItemPosition()).getTicketCategoryId();
        else
            return childSpinner.getLastCatId();
    }

    public void setSelectedMainCategory(String selectedMainCategory) {
        this.selectedMainCategory = selectedMainCategory;
    }

    public ArrayList<String> getPath() {
        path.clear();
        path.add(selectedMainCategory);
        getCatPath(path);
        return path;
    }

    public void getCatPath(ArrayList<String> path){
        path.add(modelList.get(category.getSelectedItemPosition()).getTicketCategoryName());
        if(childSpinner != null){
            childSpinner.getCatPath(path);
        }
    }
}
