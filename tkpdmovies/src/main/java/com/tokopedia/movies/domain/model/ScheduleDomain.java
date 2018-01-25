
package com.tokopedia.movies.domain.model;

import com.tokopedia.movies.data.entity.response.AddressDetail;
import com.tokopedia.movies.data.entity.response.Group;

import java.util.List;

public class ScheduleDomain {

    private ScheduleDomain_ schedule;
    private AddressDetail addressDetail;
    private List<Group> groups = null;

    public ScheduleDomain_ getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleDomain_ schedule) {
        this.schedule = schedule;
    }

    public AddressDetail getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(AddressDetail addressDetail) {
        this.addressDetail = addressDetail;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

}
