package com.android.tony.surveyx;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// code by https://linkedin.com/in/tejas-rana-668595128/
// Tony Rana
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SurveyLocalAdapter extends ArrayAdapter<SurveyLocal> {
    TextView surveyTextView;

    SurveyLocalAdapter(Activity mActivity, ArrayList<SurveyLocal> arrayList)
    {
        super(mActivity,0,arrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.survey_listview,parent,false);

        SurveyLocal current = getItem(position);
        surveyTextView = convertView.findViewById(R.id.surveytextView);
        surveyTextView.setText(current.getSurveyName());

        return  convertView;
    }
}
