
package com.tokopedia.events.domain.model;

import com.tokopedia.events.data.entity.response.AddressDetail;
import com.tokopedia.events.data.entity.response.Group;

import java.util.List;

public class ScheduleDomain {

    private ScheduleDetailDomain schedule;
    private AddressDetail addressDetail;
    private List<Group> groups = null;

    public ScheduleDetailDomain getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleDetailDomain schedule) {
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
