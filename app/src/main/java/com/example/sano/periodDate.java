package com.example.sano;

public class periodDate {

    private String StartDate;
    private String EndDate;

    public periodDate(){}
    public periodDate(String StartDate, String EndDate) {
        this.StartDate = StartDate;
        this.EndDate = EndDate;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }

    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }

    public String getEndDate() {
        return EndDate;
    }
}
