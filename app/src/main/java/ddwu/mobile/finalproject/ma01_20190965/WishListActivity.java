package ddwu.mobile.finalproject.ma01_20190965;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class WishListActivity extends AppCompatActivity {

    ListView listView = null;
    CafeDBManager cafeDBManager;
    CafeDBHelper helper;
    Cursor cursor;
    MyCursorAdapter adapter;
    ArrayList<Cafe> wishList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        listView = (ListView)findViewById(R.id.lvCafe);
        wishList = new ArrayList<Cafe>();
        cafeDBManager = new CafeDBManager(this);
        helper = new CafeDBHelper(this);
        adapter = new MyCursorAdapter(this, R.layout.listview_layout, null);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WishListActivity.this, WishCafeActivity.class);
                Cafe cafe = wishList.get(position);
                intent.putExtra("cafe", cafe);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                Cafe cafe = wishList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(WishListActivity.this);
                builder.setTitle("삭제 확인")
                        .setMessage(cafe.getName() + "을(를) WishList에서 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean result = cafeDBManager.removeCafe(wishList.get(pos).get_id());
                                if(result){
                                    Toast.makeText(WishListActivity.this, "WishList에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    onResume();
                                } else {
                                    Toast.makeText(WishListActivity.this, "삭제가 실패되었습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(WishListActivity.this, "삭제를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setCancelable(false)
                        .show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLiteDatabase db = helper.getReadableDatabase();
        wishList.clear();
        wishList.addAll(cafeDBManager.getWishList());
        cursor = db.rawQuery("SELECT * FROM " + CafeDBHelper.TABLE_NAME + " WHERE " + CafeDBHelper.COL_TYPE + "='w'", null);
        adapter.changeCursor(cursor);
        helper.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wish, menu);
        return true;
    }

    public void onMenuItemClick(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.GoSearch:
                intent = new Intent(this, SearchActivity.class);
                break;
            case R.id.GoMyList:
                intent = new Intent(this, MyListActivity.class);
                break;
            case R.id.exit:
                builder.setTitle("앱 종료")
                        .setMessage("앱을 종료하시겠습니까?")
                        .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.finishAffinity(WishListActivity.this);
                                System.runFinalization();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
                break;
        }
        if (intent != null) startActivity(intent);
    }

}
