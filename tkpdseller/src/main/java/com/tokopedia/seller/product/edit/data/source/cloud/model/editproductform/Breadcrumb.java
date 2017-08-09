
package com.tokopedia.seller.product.edit.data.source.cloud.model.editproductform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Breadcrumb {

    @SerializedName("department_name")
    @Expose
    private String departmentName;
    @SerializedName("department_identifier")
    @Expose
    private String departmentIdentifier;
    @SerializedName("department_dir_view")
    @Expose
    private int departmentDirView;
    @SerializedName("department_id")
    @Expose
    private String departmentId;
    @SerializedName("department_tree")
    @Expose
    private int departmentTree;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentIdentifier() {
        return departmentIdentifier;
    }

    public void setDepartmentIdentifier(String departmentIdentifier) {
        this.departmentIdentifier = departmentIdentifier;
    }

    public int getDepartmentDirView() {
        return departmentDirView;
    }

    public void setDepartmentDirView(int departmentDirView) {
        this.departmentDirView = departmentDirView;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public int getDepartmentTree() {
        return departmentTree;
    }

    public void setDepartmentTree(int departmentTree) {
        this.departmentTree = departmentTree;
    }

}
