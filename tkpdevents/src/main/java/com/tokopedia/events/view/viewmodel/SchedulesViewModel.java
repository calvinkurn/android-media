
package com.tokopedia.events.view.viewmodel;

import com.tokopedia.events.data.entity.response.AddressDetail;
import com.tokopedia.events.data.entity.response.Group;

import java.util.List;

public class SchedulesViewModel {

    private ScheduleViewModel schedule;
    private AddressDetail addressDetail;
    private List<Group> groups = null;

    public ScheduleViewModel getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleViewModel schedule) {
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
