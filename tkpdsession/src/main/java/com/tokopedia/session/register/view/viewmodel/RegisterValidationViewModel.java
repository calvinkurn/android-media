package com.tokopedia.session.register.view.viewmodel;

/**
 * @author by alvinatin on 12/06/18.
 */

public class RegisterValidationViewModel {
    private Boolean isExist;
    private String type;
    private String view;

    public RegisterValidationViewModel(Boolean isExist, String type, String view) {
        this.isExist = isExist;
        this.type = type;
        this.view = view;
    }

    public Boolean getExist() {
        return isExist;
    }

    public void setExist(Boolean exist) {
        isExist = exist;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
}
