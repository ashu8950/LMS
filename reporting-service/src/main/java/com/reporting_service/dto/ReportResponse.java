package com.reporting_service.dto;

import java.util.List;

public class ReportResponse<T> {
    private T meta;
    private List<?> details;

    public ReportResponse() {}

    public ReportResponse(T meta, List<?> details) {
        this.meta = meta;
        this.details = details;
    }

    public T getMeta() {
        return meta;
    }

    public void setMeta(T meta) {
        this.meta = meta;
    }

    public List<?> getDetails() {
        return details;
    }

    public void setDetails(List<?> details) {
        this.details = details;
    }
}
