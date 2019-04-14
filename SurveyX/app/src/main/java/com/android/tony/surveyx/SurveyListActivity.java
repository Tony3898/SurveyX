package com.android.tony.surveyx;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
// code by https://linkedin.com/in/tejas-rana-668595128/
// Tony Rana
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
// code by https://linkedin.com/in/tejas-rana-668595128/ Tony
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SurveyListActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    ListView listView;
    ArrayList<SurveyLocal> arrayList;
    static LocalDatabaseHelper localDatabaseHelper;
    SurveyLocalAdapter stringArrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(item.getItemId() == R.id.logout)
        {
            firebaseAuth.signOut();
            SurveyListActivity.this.finish();
            startActivity(new Intent(SurveyListActivity.this,MainActivity.class));
        }
        return true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_list);

        listView = findViewById(R.id.listview);
        arrayList = new ArrayList<>();
        stringArrayAdapter = new SurveyLocalAdapter(this,arrayList);
        listView.setAdapter(stringArrayAdapter);
        localDatabaseHelper = new LocalDatabaseHelper(getApplicationContext());
        floatingActionButton = findViewById(R.id.fab);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(!firebaseAuth.getCurrentUser().getEmail().equals("tonyrana3898@gmail.com")) {
            floatingActionButton.setVisibility(View.GONE);
        }


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(SurveyListActivity.this);
                View promptView = layoutInflater.inflate(R.layout.surveylistinputlayout, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SurveyListActivity.this);
                alertDialogBuilder.setView(promptView);
                final EditText surveyName = (EditText) promptView.findViewById(R.id.inputsurveyedittext);
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(!surveyName.getText().toString().trim().isEmpty())
                                {
                                    boolean result = localDatabaseHelper.insertSurveyName(surveyName.getText().toString().trim(),LocalDatabaseHelper.TABLE_NAME_1);
                                    if(result)
                                    {
                                        localDatabaseHelper.createSurveyTableForUserInput(surveyName.getText().toString().trim());
                                        Cursor cursor = localDatabaseHelper.getAllData(LocalDatabaseHelper.TABLE_NAME_1);
                                        if(cursor.getCount()>0)
                                        {
                                            arrayList.clear();
                                            cursor.moveToFirst();
                                            while (!cursor.isAfterLast())
                                            {
                                                arrayList.add(new SurveyLocal(cursor.getString(1),cursor.getInt(0)));
                                                stringArrayAdapter.notifyDataSetChanged();
                                                cursor.moveToNext();
                                            }
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Some Error has occurred please try again \nor Survey Name already exist",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Empty Survey Name Discard",Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(SurveyListActivity.this,SurveyTableActivity.class);
                intent.putExtra("SurveyName",arrayList.get(position).getSurveyName());
                intent.putExtra("SurveyId",arrayList.get(position).getSurveyid());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = localDatabaseHelper.getAllData(LocalDatabaseHelper.TABLE_NAME_1);
        if(cursor.getCount()>0)
        {
            arrayList.clear();
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                arrayList.add(new SurveyLocal(cursor.getString(1),cursor.getInt(0)));
                stringArrayAdapter.notifyDataSetChanged();
                cursor.moveToNext();
            }
        }
    }
}
