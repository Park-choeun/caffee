package ddwu.mobile.finalproject.ma01_20190965;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.btnLocation:
                intent = new Intent(this, SearchActivity.class);
                break;
            case R.id.btnWishList:
                intent = new Intent(this, WishListActivity.class);
                break;
            case R.id.btnMyList:
                intent = new Intent(this, MyListActivity.class);
                break;
            case R.id.btnExit:
                finish();
                break;
        }

        if (intent != null) startActivity(intent);
    }
}