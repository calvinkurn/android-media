package com.tokopedia.transaction.orders.orderdetails.data;

public class DetailsQueryModle {

    String query;

    Variable variables;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Variable getVariables() {
        return variables;
    }

    public void setVariables(Variable variables) {
        this.variables = variables;
    }
}