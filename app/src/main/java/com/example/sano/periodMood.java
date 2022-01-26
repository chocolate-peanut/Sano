package com.example.sano;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class periodMood {

    private LocalDate date;
    private List<String> symptom;
    private String intercourse;
    private String period;
    private String mood;
    private String sport;
    private String activity;

    //periodDate
    private LocalDate startDate;
    private LocalDate endDate;

    public periodMood(){}

    public periodMood(LocalDate date, List<String> symptom, String intercourse, String period, String mood, String sport, String activity, LocalDate startDate, LocalDate endDate) {
        this.date = date;
        this.symptom = symptom;
        this.intercourse = intercourse;
        this.period = period;
        this.mood = mood;
        this.sport = sport;
        this.activity = activity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate(){return startDate; }

    public void setStartDate(LocalDate startDate){this.startDate = startDate;}

    public LocalDate getEndDate(){ return endDate;}

    public void setEndDate(LocalDate endDate){this.endDate = endDate;}

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<String> getSymptom() {
        return symptom;
    }

    public void setSymptom(List<String> symptom) {
        this.symptom = symptom;
    }

    public String getIntercourse() {
        return intercourse;
    }

    public void setIntercourse(String intercourse) {
        this.intercourse = intercourse;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
