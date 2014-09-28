package com.kashsoft.timer;

import android.provider.BaseColumns;

public final class ResultContract {
    public ResultContract() {};
    
	public static final String TEXT_TYPE = " TEXT";
	public static final String INT_TYPE = " INT";
	public static final String LONG_TYPE = " LONG";
	public static final String COMMA_SEP = ",";
	public static final String SQL_CREATE_ENTRIES =
	    "CREATE TABLE " + ResultEntry.TABLE_NAME + " (" +
	    ResultEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + 
	    ResultEntry.COLUMN_NAME_DATE + LONG_TYPE + COMMA_SEP +
	    ResultEntry.COLUMN_NAME_TIME + LONG_TYPE + COMMA_SEP +
	    ResultEntry.COLUMN_NAME_ATTEMPT + INT_TYPE + COMMA_SEP +
	    ResultEntry.COLUMN_NAME_SCRAMBLE + TEXT_TYPE + COMMA_SEP +
	    ResultEntry.COLUMN_NAME_RESULT + LONG_TYPE + COMMA_SEP +
	    "UNIQUE" + " (" + ResultEntry.COLUMN_NAME_DATE + COMMA_SEP + ResultEntry.COLUMN_NAME_TIME + ")" +
	    " )";

	public static final String SQL_DELETE_ENTRIES =
	    "DROP TABLE IF EXISTS " + ResultEntry.TABLE_NAME;

    public static abstract class ResultEntry implements BaseColumns {
        public static final String TABLE_NAME = "results";
        //public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_ATTEMPT = "attempt";
        public static final String COLUMN_NAME_SCRAMBLE = "scramble";
        public static final String COLUMN_NAME_RESULT = "result";
    }
}
