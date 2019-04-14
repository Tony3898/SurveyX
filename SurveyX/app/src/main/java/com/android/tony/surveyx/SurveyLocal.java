package com.android.tony.surveyx;

public class SurveyLocal {
    String surveyName;
    int Surveyid;

    SurveyLocal(String surveyName,int id)
    {
        this.surveyName = surveyName;
        this.Surveyid = id;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public int getSurveyid() {
        return Surveyid;
    }
}
