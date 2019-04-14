package com.android.tony.surveyx;
// code by https://linkedin.com/in/tejas-rana-668595128/
// Tony Rana

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SurveyTableActivity extends AppCompatActivity {
    Intent intent;
    FloatingActionButton floatingActionButton;
    LayoutInflater layoutInflater;
    View promptView;
    AlertDialog.Builder alertDialogBuilder;
    EditText password,questionEditText,optionOneEditText,optionTwoEditText,optionThreeEditText,optionFourEditText;
    ListView listView;
    ArrayList<SurveyLocal> arrayList;
    SurveyLocalAdapter stringArrayAdapter;
    TextView responseTextView;
    Button addResponseButton;
    int count;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_table);

        intent = getIntent();
        setTitle(intent.getStringExtra("SurveyName"));

        responseTextView = findViewById(R.id.responsetextview);
        listView = findViewById(R.id.listviewsurveytable);
        arrayList = new ArrayList<>();
        stringArrayAdapter = new SurveyLocalAdapter(this,arrayList);
        listView.setAdapter(stringArrayAdapter);
        floatingActionButton = findViewById(R.id.fab2);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(!firebaseAuth.getCurrentUser().getEmail().equals("tonyrana3898@gmail.com")) {
            floatingActionButton.setVisibility(View.GONE);
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater = LayoutInflater.from(SurveyTableActivity.this);
                promptView = layoutInflater.inflate(R.layout.surveylistinputlayout, null);
                alertDialogBuilder = new AlertDialog.Builder(SurveyTableActivity.this);
                alertDialogBuilder.setView(promptView);
                password = (EditText) promptView.findViewById(R.id.inputsurveyedittext);
                password.setHint("Enter Password!!!");
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(!password.getText().toString().trim().isEmpty() && password.getText().toString().trim().equals("123"))
                                {
                                    Toast.makeText(getApplicationContext(),"Welcome Admin",Toast.LENGTH_SHORT).show();
                                    layoutInflater = LayoutInflater.from(SurveyTableActivity.this);
                                    promptView = layoutInflater.inflate(R.layout.add_coulmn_layout, null);
                                    alertDialogBuilder = new AlertDialog.Builder(SurveyTableActivity.this);
                                    alertDialogBuilder.setView(promptView);
                                    questionEditText = (EditText) promptView.findViewById(R.id.questionedittext);
                                    optionOneEditText = promptView.findViewById(R.id.optiononeedittext);
                                    optionTwoEditText = promptView.findViewById(R.id.optiontwoedittext);
                                    optionThreeEditText = promptView.findViewById(R.id.optionthreeedittext);
                                    optionFourEditText = promptView.findViewById(R.id.optionfouredittext);
                                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Toast.makeText(getApplicationContext(),"Questions Added",Toast.LENGTH_SHORT).show();

                                            boolean result = SurveyListActivity.localDatabaseHelper.insertSurveyQuestions(intent.getIntExtra("SurveyId",-1),questionEditText.getText().toString().trim(),optionOneEditText.getText().toString().trim(),optionTwoEditText.getText().toString().trim(),optionThreeEditText.getText().toString().trim(),optionFourEditText.getText().toString().trim());
                                            if(result)
                                            {
                                                //Toast.makeText(getApplicationContext(),"Questions",Toast.LENGTH_SHORT).show();
                                                SurveyListActivity.localDatabaseHelper.alterSurveyTableForUserInput(intent.getStringExtra("SurveyName"),questionEditText.getText().toString().trim());
                                                Cursor cursor = SurveyListActivity.localDatabaseHelper.getAllData(LocalDatabaseHelper.TABLE_NAME_2);
                                                responseTextView.setText("Total Questions " + String.valueOf(cursor.getCount()));
                                                if(cursor.getCount()>0)
                                                {
                                                    arrayList.clear();
                                                    cursor.moveToFirst();
                                                    while (!cursor.isAfterLast())
                                                    {
                                                        if(cursor.getInt(0) == intent.getIntExtra("SurveyId",-1))
                                                        {
                                                           // Toast.makeText(getApplicationContext(),"Questions to",Toast.LENGTH_SHORT).show();
                                                            arrayList.add(new SurveyLocal(cursor.getString(2),cursor.getInt(1)));
                                                            stringArrayAdapter.notifyDataSetChanged();
                                                            count++;
                                                        }
                                                        cursor.moveToNext();
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).create().show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Wrong Password",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),arrayList.get(position).getSurveyid() + arrayList.get(position).getSurveyName(),Toast.LENGTH_SHORT).show();
                Cursor cursor = SurveyListActivity.localDatabaseHelper.getAllData(LocalDatabaseHelper.TABLE_NAME_2);
                if(cursor.getCount()>0)
                {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast())
                    {
                        if(cursor.getInt(1) == arrayList.get(position).getSurveyid())
                        {
                            if(cursor.getString(3).isEmpty() && cursor.getString(4).isEmpty() && cursor.getString(5).isEmpty() && cursor.getString(6).isEmpty())
                                Toast.makeText(getApplicationContext(),"User Input Question",Toast.LENGTH_SHORT).show();
                            else
                            {
                                Toast.makeText(getApplicationContext(),String.format("Option are: \n %s \n %s \n %s \n %s",cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6)),Toast.LENGTH_SHORT).show();
                            }
                        }
                        cursor.moveToNext();
                    }
                }
            }
        });

        addResponseButton = findViewById(R.id.addresponsebutton);
        addResponseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>0)
                {
                    Intent intentLocal = new Intent(SurveyTableActivity.this,UserInputActivity.class);
                    intentLocal.putExtra("SurveyName",intent.getStringExtra("SurveyName"));
                    intentLocal.putExtra("SurveyId",intent.getIntExtra("SurveyId",-1));
                    startActivity(intentLocal);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Add Questions First",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        count=0;
        Cursor cursor = SurveyListActivity.localDatabaseHelper.getAllData(LocalDatabaseHelper.TABLE_NAME_2);
        Cursor cursor1 = SurveyListActivity.localDatabaseHelper.getAllData(intent.getStringExtra("SurveyName"));
        if(cursor.getCount()>0)
        {
            arrayList.clear();
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                if(cursor.getInt(0)== intent.getIntExtra("SurveyId",-1))
                {
                    count++;
                    arrayList.add(new SurveyLocal(cursor.getString(2),cursor.getInt(1)));
                    stringArrayAdapter.notifyDataSetChanged();
                }
                cursor.moveToNext();
            }
        }
        responseTextView.setText("Total Questions: " + count + "\tTotal Response: " + cursor1.getCount());
        responseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SurveyTableActivity.this,ShowAllActivity.class);
                intent1.putExtra("SurveyName",intent.getStringExtra("SurveyName"));
                startActivity(intent1);
            }
        });
    }
}
