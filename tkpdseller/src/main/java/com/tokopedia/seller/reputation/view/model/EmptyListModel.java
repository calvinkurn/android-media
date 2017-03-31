package com.tokopedia.seller.reputation.view.model;

import com.tokopedia.seller.topads.view.model.TypeBasedModel;

/**
 * Created by normansyahputa on 3/30/17.
 */

public class EmptyListModel extends TypeBasedModel {
    public static final int TYPE = 128912;

    SetDateHeaderModel setDateHeaderModel;

    public EmptyListModel() {
        super(TYPE);
    }

    public SetDateHeaderModel getSetDateHeaderModel() {
        return setDateHeaderModel;
    }

    public void setSetDateHeaderModel(SetDateHeaderModel setDateHeaderModel) {
        this.setDateHeaderModel = setDateHeaderModel;
    }
}
