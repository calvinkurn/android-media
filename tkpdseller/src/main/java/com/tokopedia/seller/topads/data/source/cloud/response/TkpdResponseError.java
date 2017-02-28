package com.tokopedia.seller.topads.data.source.cloud.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hendry on 2/28/2017.
 */
// Example json Response
// {"errors":[  {"code":"","title":"Validate param.","detail":"Batas tertinggi biaya yang bisa Anda tentukan: Rp 2.000"},
//              {"code":"","title":"Validate param.","detail":"Anggaran minimal 5 kali lebih besar dari Penawaran Maks."}
//          ]}

public class TkpdResponseError {
    @SerializedName("errors")
    @Expose
    private List<Error> errors = null;

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}


