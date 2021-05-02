package net.fahmi.biodata;
;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CustomCursorAdapter extends CursorAdapter {

    private LayoutInflater layoutInflater;

    public CustomCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = layoutInflater.inflate(R.layout.row_data, parent, false);
        MyHolder holder = new MyHolder();
        holder.list_id = (TextView) v.findViewById(R.id.list_id);
        holder.list_nama = (TextView) v.findViewById(R.id.list_nama);
        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MyHolder holder = (MyHolder) view.getTag();
        holder.list_id.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_id)));
        holder.list_nama.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_nama)));
    }
    class MyHolder{
        TextView list_id;
        TextView list_nama;
    }
}
