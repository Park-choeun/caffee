package ddwu.mobile.finalproject.ma01_20190965;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter {

    LayoutInflater inflater;
    int layout;

    public MyCursorAdapter(Context context, int layout, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);
        ViewHolder holder = new ViewHolder();
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();

        if (holder.tvName == null) {
            holder.tvName = view.findViewById(R.id.tvName);
            holder.tvAddress = view.findViewById(R.id.tvAddress);
            holder.lvcafeimg = view.findViewById(R.id.lvcafeimg);
        }

        holder.tvName.setText(cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_NAME)));
        holder.tvAddress.setText(cursor.getString(cursor.getColumnIndex(CafeDBHelper.COL_ADDRESS)));
//        holder.lvcafeimg.setImageBitmap();
    }

    static class ViewHolder {
        public ViewHolder() {
            tvName = null;
            tvAddress = null;
            lvcafeimg = null;
        }
        TextView tvName;
        TextView tvAddress;
        ImageView lvcafeimg;
    }
}
