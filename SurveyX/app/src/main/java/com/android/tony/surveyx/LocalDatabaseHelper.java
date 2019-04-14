package com.android.tony.surveyx;
// code by https://linkedin.com/in/tejas-rana-668595128/
// Tony Rana
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class LocalDatabaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "SurveyX", USER_INPUT_TABLE_COL_1="User_ID";
    public final static String TABLE_NAME_1 = "Survey_List",TABLE_NAME_1_COL_1  = "Survey_ID",TABLE_NAME_1_COL_2 = "Survey_Name";
    public final static String TABLE_NAME_2 = "Questions",TABLE_NAME_2_COL_1  = "Survey_ID",TABLE_NAME_2_COL_2 = "Question_ID",TABLE_NAME_2_COL_3="Questions" ,TABLE_NAME_2_COL_4="Options_One" ,TABLE_NAME_2_COL_5="Options_Two",TABLE_NAME_2_COL_6="Options_Three",TABLE_NAME_2_COL_7="Options_Four" ;

    LocalDatabaseHelper(Context context)
    {
        super(context, LocalDatabaseHelper.DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT UNIQUE NOT NULL)", LocalDatabaseHelper.TABLE_NAME_1, LocalDatabaseHelper.TABLE_NAME_1_COL_1, LocalDatabaseHelper.TABLE_NAME_1_COL_2 ));
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(%s INTEGER,%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT)",
                LocalDatabaseHelper.TABLE_NAME_2,LocalDatabaseHelper.TABLE_NAME_2_COL_1,LocalDatabaseHelper.TABLE_NAME_2_COL_2,
                LocalDatabaseHelper.TABLE_NAME_2_COL_3,LocalDatabaseHelper.TABLE_NAME_2_COL_4,LocalDatabaseHelper.TABLE_NAME_2_COL_5,
                LocalDatabaseHelper.TABLE_NAME_2_COL_6,LocalDatabaseHelper.TABLE_NAME_2_COL_7));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    void createSurveyTableForUserInput(String Table_Name)
    {
        if(Table_Name.contains(" "))
            Table_Name = Table_Name.replaceAll(" ","_");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(%s INTEGER PRIMARY KEY AUTOINCREMENT)",Table_Name,LocalDatabaseHelper.USER_INPUT_TABLE_COL_1));
    }

    void alterSurveyTableForUserInput(String TABLE_NAME,String Col_Name)
    {
        if(TABLE_NAME.contains(" "))
            TABLE_NAME = TABLE_NAME.replaceAll(" ","_");
        if(Col_Name.contains(" "))
            Col_Name = Col_Name.replaceAll(" ","_");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(String.format("ALTER TABLE %s ADD COLUMN %s TEXT",TABLE_NAME,Col_Name));
    }

    public boolean insertSurveyName(String surveyName,String TABLE_NAME)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(TABLE_NAME.equals(LocalDatabaseHelper.TABLE_NAME_1))
        {
            contentValues.put(LocalDatabaseHelper.TABLE_NAME_1_COL_2,surveyName);
            long result = sqLiteDatabase.insert(LocalDatabaseHelper.TABLE_NAME_1,null,contentValues);
            if (result == -1) return false;
            else return true;
        }
        else
        {
            return false;
        }
    }
    public boolean insertSurveyQuestions(int surveyID,String questions,String col_1,String col_2,String col_3,String col_4)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocalDatabaseHelper.TABLE_NAME_2_COL_1,surveyID);
        contentValues.put(LocalDatabaseHelper.TABLE_NAME_2_COL_3,questions);
        contentValues.put(LocalDatabaseHelper.TABLE_NAME_2_COL_4,col_1);
        contentValues.put(LocalDatabaseHelper.TABLE_NAME_2_COL_5,col_2);
        contentValues.put(LocalDatabaseHelper.TABLE_NAME_2_COL_6,col_3);
        contentValues.put(LocalDatabaseHelper.TABLE_NAME_2_COL_7,col_4);
        long result = sqLiteDatabase.insert(LocalDatabaseHelper.TABLE_NAME_2,null,contentValues);
        if (result == -1) return false;
        else return true;
    }

    boolean insertUserInput(String TABLE_NAME, ArrayList<String> questions, ArrayList<String> answer)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(TABLE_NAME.contains(" "))
            TABLE_NAME = TABLE_NAME.replaceAll(" ","_");
        for(int i=0; i<questions.size();i++ )
        {
            if(questions.get(i).contains(" "))
                questions.set(i,questions.get(i).replaceAll(" ","_"));
            contentValues.put(questions.get(i),answer.get(i));
        }
        long result = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        if(result == -1) return false;
        else return true;
    }

    public Cursor getAllData(String Table_Name)
    {
        Cursor cursor;
        if(Table_Name.contains(" "))
            Table_Name = Table_Name.replaceAll(" ","_");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if(Table_Name.equals(LocalDatabaseHelper.TABLE_NAME_1))
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Table_Name + " ORDER BY "+ LocalDatabaseHelper.TABLE_NAME_1_COL_2,null);
        else
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + Table_Name,null);
        return cursor;
    }


}
