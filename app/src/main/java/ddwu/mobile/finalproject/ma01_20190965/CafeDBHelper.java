package ddwu.mobile.finalproject.ma01_20190965;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CafeDBHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "cafe_db";
    public final static String TABLE_NAME = "cafe_table";
    public final static String COL_ID = "_id";
    public final static String COL_NAME = "name";
    public final static String COL_ADDRESS = "address";
    public final static String COL_PHONE = "phone";
    public final static String COL_REVIEW = "review";
    public final static String COL_RATING = "rating";
    public final static String COL_TYPE = "type";
    public final static String COL_IMG = "img";
    public final static String COL_LAT = "lat";
    public final static String COL_LNG = "lng";
    public final static String COL_PLACEID = "placeId";

    public CafeDBHelper(Context context) { super(context, DB_NAME, null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement,"
                + COL_NAME + " TEXT, " + COL_ADDRESS + " TEXT, " + COL_PHONE + " TEXT, " + COL_REVIEW + " TEXT, " + COL_RATING + " TEXT, "
                + COL_TYPE + " TEXT, " + COL_IMG + " TEXT, " + COL_LAT + " TEXT, " + COL_LNG + " TEXT, " + COL_PLACEID + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
    }
}
