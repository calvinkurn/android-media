package com.tokopedia.session.register.view.viewmodel;

/**
 * @author by alvinatin on 12/06/18.
 */

public class RegisterValidationViewModel {
    private Boolean isExist;
    private String type;
    private String viewString;

    public RegisterValidationViewModel(Boolean isExist, String type, String viewString) {
        this.isExist = isExist;
        this.type = type;
        this.viewString = viewString;
    }

    public Boolean isExist() {
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
        return viewString;
    }

    public void setView(String viewString) {
        this.viewString = viewString;
    }
}
