package com.tokopedia.movies.view.viewmodel;

import com.tokopedia.movies.data.entity.response.seatlayoutresponse.SeatLayoutResponse;

/**
 * Created by naveengoyal on 1/18/18.
 */

public class SeatLayoutItemViewModel {

    private String layout;

    public void setLayout(String layout){
        this.layout = layout;
    }

    public String getLayout(){
        return layout;
    }
}
