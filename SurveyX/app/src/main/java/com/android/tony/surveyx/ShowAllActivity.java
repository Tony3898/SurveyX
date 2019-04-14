package com.android.tony.surveyx;

import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
// code by https://linkedin.com/in/tejas-rana-668595128/
// Tony Rana
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ShowAllActivity extends AppCompatActivity {
    String msg="";
    Intent intent;
    ListView listView;
    ArrayList<SurveyLocal> arrayList;
    SurveyLocalAdapter stringArrayAdapter;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);
        listView = findViewById(R.id.showalllistview);
        arrayList = new ArrayList<>();
        stringArrayAdapter = new SurveyLocalAdapter(this,arrayList);
        listView.setAdapter(stringArrayAdapter);


        setTitle(intent.getStringExtra("SurveyName"));
// code by https://linkedin.com/in/tejas-rana-668595128/
// Tony Rana

        floatingActionButton = findViewById(R.id.exportfloatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = SurveyListActivity.localDatabaseHelper.getAllData(intent.getStringExtra("SurveyName"));
                File sd = Environment.getExternalStorageDirectory();
                String fileName = String.format("%s.xls",intent.getStringExtra("SurveyName"));
                File directory = new File(sd.getAbsolutePath());
                if(!directory.isDirectory())
                    directory.mkdirs();
                try {
                    File file = new File(directory,fileName);
                    WorkbookSettings wbSettings = new WorkbookSettings();
                    wbSettings.setLocale(new Locale("en", "EN"));
                    WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
                    WritableSheet sheet = workbook.createSheet(intent.getStringExtra("SurveyName"), 0);
                    for(int i=0;i<cursor.getColumnCount();i++)
                        sheet.addCell(new Label(i,0,cursor.getColumnName(i)));
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast())
                    {
                        for(int j=0;j<cursor.getColumnCount();j++)
                            sheet.addCell(new Label(j,cursor.getPosition()+1,cursor.getString(j)));
                        cursor.moveToNext();
                    }
                    cursor.close();
                    workbook.write();
                    workbook.close();
                    Toast.makeText(getApplication(), "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        int k=0;
        arrayList.clear();
        Cursor cursor = SurveyListActivity.localDatabaseHelper.getAllData(intent.getStringExtra("SurveyName"));
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                msg="";
                for(int i=1;i<cursor.getColumnCount();i++)
                    msg += cursor.getColumnName(i)+": " + cursor.getString(i) + "\n";
                arrayList.add(new SurveyLocal(msg,++k));
                stringArrayAdapter.notifyDataSetChanged();
                cursor.moveToNext();
            }
        }
    }
}
