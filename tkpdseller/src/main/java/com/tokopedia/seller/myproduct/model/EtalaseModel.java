package com.tokopedia.seller.myproduct.model;

import com.tokopedia.core.myproduct.model.GetEtalaseModel;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by m.normansyah on 10/12/2015.
 */
@Parcel
public class EtalaseModel {
    String text;
    ArrayList<String> childs;
    ArrayList<GetEtalaseModel.EtalaseModel> childsComplete;

    /**
     * This is for parcelable
     */
    public EtalaseModel(){
        childs = new ArrayList<>();
        childsComplete = new ArrayList<>();
    }

    public EtalaseModel add(GetEtalaseModel.EtalaseModel childComplete){
        childsComplete.add(childComplete);
        childs.add(childComplete.getEtalase_name());
        return this;
    }

    public EtalaseModel add(String child){
        childs.add(child);
        return this;
    }

    public String getText() {
        return text;
    }

    public EtalaseModel setText(String temp) {
        this.text = temp;
        return this;
    }
}
