package com.tokopedia.session.register.domain.model;

import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;

/**
 * @author by nisie on 10/23/17.
 */

public class CreatePasswordLoginDomain {

    CreatePasswordDomain createPasswordDomain;
    MakeLoginDomain makeLoginDomain;

    public CreatePasswordDomain getCreatePasswordDomain() {
        return createPasswordDomain;
    }

    public void setCreatePasswordDomain(CreatePasswordDomain createPasswordDomain) {
        this.createPasswordDomain = createPasswordDomain;
    }

    public MakeLoginDomain getMakeLoginDomain() {
        return makeLoginDomain;
    }

    public void setMakeLoginDomain(MakeLoginDomain makeLoginDomain) {
        this.makeLoginDomain = makeLoginDomain;
    }
}
