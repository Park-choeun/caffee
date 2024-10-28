package ddwu.mobile.finalproject.ma01_20190965;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class CafeDBManager {
    CafeDBHelper cafeDBHelper = null;
    Cursor cursor = null;

    public CafeDBManager(Context context) { cafeDBHelper = new CafeDBHelper(context); }

    //위시리스트 반환
    public ArrayList<Cafe> getWishList() {
        ArrayList wishList = new ArrayList();
        SQLiteDatabase db = cafeDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CafeDBHelper.TABLE_NAME + " WHERE " + CafeDBHelper.COL_TYPE + "='w'", null);

        while (cursor.moveToNext()) {
            long _id = cursor.getInt(cursor.getColumnIndex(CafeDBHelper.COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_NAME));
            String address = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_ADDRESS));
            String phone = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_PHONE));
            String review = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_REVIEW));
            String rating = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_RATING));
            String type = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_TYPE));
            Double lat = cursor.getDouble(cursor.getColumnIndex(CafeDBHelper.COL_LAT));
            Double lng = cursor.getDouble(cursor.getColumnIndex(CafeDBHelper.COL_LNG));
            String placeId = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_PLACEID));
            wishList.add(new Cafe(_id, name, address, phone, review, rating, type, lat, lng, placeId));
        }
        cursor.close();
        cafeDBHelper.close();
        return wishList;
    }

    //마이리스트 반환
    public ArrayList<Cafe> getMyList() {
        ArrayList myList = new ArrayList();
        SQLiteDatabase db = cafeDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CafeDBHelper.TABLE_NAME + " WHERE " + CafeDBHelper.COL_TYPE + " = 'm'", null);

        while (cursor.moveToNext()) {
            long _id = cursor.getInt(cursor.getColumnIndex(CafeDBHelper.COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_NAME));
            String address = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_ADDRESS));
            String phone = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_PHONE));
            String review = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_REVIEW));
            String rating = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_RATING));
            String type = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_TYPE));
            String img = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_IMG));
            Double lat = cursor.getDouble(cursor.getColumnIndex(CafeDBHelper.COL_LAT));
            Double lng = cursor.getDouble(cursor.getColumnIndex(CafeDBHelper.COL_LNG));
            String placeId = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_PLACEID));
            myList.add(new Cafe(_id, name, address, phone, review, rating, type, img, lat, lng, placeId));
        }
        cursor.close();
        cafeDBHelper.close();
        return myList;
    }

//    //중복검사
//    public Cafe getCafe(String pId, String t) {
//        Cafe result = null;
//        SQLiteDatabase db = cafeDBHelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + CafeDBHelper.TABLE_NAME + " WHERE "
//                + CafeDBHelper.COL_TYPE + " ='" + t +"' AND "
//                + CafeDBHelper.COL_PLACEID + "='" + pId +"'", null);
//
//        while (cursor.moveToNext()) {
//            long _id = cursor.getInt(cursor.getColumnIndex(CafeDBHelper.COL_ID));
//            String name = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_NAME));
//            String address = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_ADDRESS));
//            String phone = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_PHONE));
//            String review = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_REVIEW));
//            String rating = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_RATING));
//            String type = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_TYPE));
//            String img = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_IMG));
//            Double lat = cursor.getDouble(cursor.getColumnIndex(CafeDBHelper.COL_LAT));
//            Double lng = cursor.getDouble(cursor.getColumnIndex(CafeDBHelper.COL_LNG));
//            String placeId = cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_PLACEID));
//            result = new Cafe(_id, name, address, phone, review, rating, type, img, lat, lng, placeId);
//        }
//        cursor.close();
//        cafeDBHelper.close();
//        return result;
//    }

    //새로운 정보 추가
    public boolean addNewCafe(Cafe newCafe) {
//        Cafe flag = getCafe(newCafe.getPlaceId(), newCafe.getType());
        SQLiteDatabase db = cafeDBHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(CafeDBHelper.COL_NAME, newCafe.getName());
        value.put(CafeDBHelper.COL_ADDRESS, newCafe.getAddress());
        value.put(CafeDBHelper.COL_PHONE, newCafe.getPhone());
        value.put(CafeDBHelper.COL_REVIEW, newCafe.getReview());
        value.put(CafeDBHelper.COL_RATING, newCafe.getRating());
        value.put(CafeDBHelper.COL_TYPE, newCafe.getType());
        value.put(CafeDBHelper.COL_IMG, newCafe.getImg());
        value.put(CafeDBHelper.COL_LAT, newCafe.getLat());
        value.put(CafeDBHelper.COL_LNG, newCafe.getLng());

        long count = db.insert(CafeDBHelper.TABLE_NAME, null, value);
        cafeDBHelper.close();
        if (count > 0) return true;
        return false;
    }

    //_id를 기준으로 정보 변경
    public boolean modifyCafe(Cafe cafe) {
        SQLiteDatabase db = cafeDBHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(CafeDBHelper.COL_NAME, cafe.getName());
        value.put(CafeDBHelper.COL_ADDRESS, cafe.getAddress());
        value.put(CafeDBHelper.COL_PHONE, cafe.getPhone());
        value.put(CafeDBHelper.COL_REVIEW, cafe.getReview());
        value.put(CafeDBHelper.COL_RATING, cafe.getRating());
        value.put(CafeDBHelper.COL_TYPE, cafe.getType());
        value.put(CafeDBHelper.COL_IMG, cafe.getImg());
        value.put(CafeDBHelper.COL_LAT, cafe.getLat());
        value.put(CafeDBHelper.COL_LNG, cafe.getLng());

        String whereClause = CafeDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(cafe.get_id()) };

        int result = db.update(CafeDBHelper.TABLE_NAME, value, whereClause, whereArgs);
        cafeDBHelper.close();
        if (result > 0) return true;
        return false;
    }

    //_id를 기준으로 삭제
    public boolean removeCafe(long _id) {
        SQLiteDatabase db = cafeDBHelper.getWritableDatabase();
        String whereClause = CafeDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(_id) };

        int result = db.delete(CafeDBHelper.TABLE_NAME, whereClause, whereArgs);
        cafeDBHelper.close();
        if (result > 0) return true;
        return false;
    }

    //close 수행
    public void close() {
        if (cafeDBHelper != null) cafeDBHelper.close();
        if (cursor != null) cursor.close();
    };
}
