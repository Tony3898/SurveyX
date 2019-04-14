package com.android.tony.surveyx;
// code by https://linkedin.com/in/tejas-rana-668595128/
// Tony Rana
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserInputActivity extends AppCompatActivity {
    Intent intent;
    ArrayList<String> question,answer,optOneList,optTwoList,optThreeList,optFourList;
    Cursor cursor;
    TextView questionTextView,optOneTextview,optTwoTextview,optThreeTextview,optFourTextview;
    EditText userInputEdittext;
    ConstraintLayout bodyConstraintLayout,bodyConstraintLayout2;
    int countQuestion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        intent = getIntent();
        setTitle(intent.getStringExtra("SurveyName") + " " + intent.getIntExtra("SurveyId",-1));

        question = new ArrayList<>();
        answer = new ArrayList<>();
        optOneList = new ArrayList<>();
        optTwoList = new ArrayList<>();
        optThreeList = new ArrayList<>();
        optFourList = new ArrayList<>();
        bodyConstraintLayout = findViewById(R.id.bodyconstraintLayout);
        bodyConstraintLayout2 = findViewById(R.id.bodyconstraintLayout2);
        userInputEdittext = findViewById(R.id.userinputeditText);
        questionTextView = findViewById(R.id.questiontextView);
        optOneTextview = findViewById(R.id.optonetextView);
        optTwoTextview = findViewById(R.id.opttwotextView);
        optThreeTextview = findViewById(R.id.optthreetextView);
        optFourTextview = findViewById(R.id.optfourtextView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        cursor = SurveyListActivity.localDatabaseHelper.getAllData(LocalDatabaseHelper.TABLE_NAME_2);
        if(cursor.getCount()>0)
        {
            question.clear();
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                if(cursor.getInt(0)== intent.getIntExtra("SurveyId",-1))
                {
                    question.add(cursor.getString(2));

                    if(cursor.getString(3).isEmpty()) optOneList.add("");
                    else optOneList.add(cursor.getString(3));

                    if(cursor.getString(4).isEmpty()) optTwoList.add("");
                    else optTwoList.add(cursor.getString(4));

                    if(cursor.getString(5).isEmpty()) optThreeList.add("");
                    else optThreeList.add(cursor.getString(5));

                    if(cursor.getString(6).isEmpty()) optFourList.add("");
                    else optFourList.add(cursor.getString(6));
                }
                cursor.moveToNext();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       cursor = SurveyListActivity.localDatabaseHelper.getAllData(intent.getStringExtra("SurveyName"));
       updateQuestions(countQuestion);
       for(int i=0;i<question.size();i++)
       {
           Log.i("Options",intent.getStringExtra("SurveyName")+ " " + question.get(i)+" " +optOneList.get(i)+" "+ optTwoList.get(i)+" "+ optThreeList.get(i)+" "+ optFourList.get(i));
       }

       for(int i=0;i<cursor.getColumnCount();i++)
           Log.i("Options Table column",cursor.getColumnName(i));

    }

    void answerSelected(View v)
    {
        switch (v.getId())
        {
            case R.id.sumbitbutton:
                if(userInputEdittext.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Cannot be empty",Toast.LENGTH_SHORT).show();
                else
                {
                    answer.add(userInputEdittext.getText().toString().trim());
                    userInputEdittext.setText("");
                    updateQuestions(++countQuestion);
                }
                break;
            case R.id.optonetextView:
                answer.add(String.valueOf(optOneTextview.getText()));
                updateQuestions(++countQuestion);
                break;
            case R.id.opttwotextView:
                answer.add(String.valueOf(optTwoTextview.getText()));
                updateQuestions(++countQuestion);
                break;
            case R.id.optthreetextView:
                answer.add(String.valueOf(optThreeTextview.getText()));
                updateQuestions(++countQuestion);
                break;
            case R.id.optfourtextView:
                answer.add(String.valueOf(optFourTextview.getText()));
                updateQuestions(++countQuestion);
                break;
        }
    }

    void updateQuestions(int pos)
    {
        Log.i("Options Pos",String.valueOf(pos) + " " + cursor.getColumnCount());
        if(pos<question.size())
        {
            questionTextView.setText(question.get(pos));
            if(optOneList.get(pos).isEmpty() && optTwoList.get(pos).isEmpty() && optThreeList.get(pos).isEmpty() && optFourList.get(pos).isEmpty() )
            {
                bodyConstraintLayout2.setVisibility(View.VISIBLE);
                bodyConstraintLayout.setVisibility(View.GONE);
            }
            else
            {
                bodyConstraintLayout2.setVisibility(View.GONE);
                bodyConstraintLayout.setVisibility(View.VISIBLE);
                optOneTextview.setText(optOneList.get(pos));
                optTwoTextview.setText(optTwoList.get(pos));
                optThreeTextview.setText(optThreeList.get(pos));
                optFourTextview.setText(optFourList.get(pos));
            }
        }
        else
        {
           new AlertDialog.Builder(this).setIcon(getResources().getDrawable(R.mipmap.surveyx)).setTitle(intent.getStringExtra("SurveyName")).setMessage("Thank you for conducting survey").setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
                    SurveyListActivity.localDatabaseHelper.insertUserInput(intent.getStringExtra("SurveyName"),question,answer);
                    UserInputActivity.this.finish();
                    startActivity(new Intent(UserInputActivity.this,SurveyListActivity.class));
                }
            }).setCancelable(false).create().show();

        }
    }
}
