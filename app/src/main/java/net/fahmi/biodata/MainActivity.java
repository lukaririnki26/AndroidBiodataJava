package net.fahmi.biodata;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    DBHelper dbHelper;
    LayoutInflater inflater;
    View vdialog;
    TextView view_nomor, view_nama, view_tempat, view_tgl, view_jk, view_alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });
        dbHelper = new DBHelper(this);
        listView = (ListView) findViewById(R.id.list_data);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this,"in a develop",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setListView(){
        Cursor c = dbHelper.allData();
        CustomCursorAdapter cca = new CustomCursorAdapter(this, c, 1);
        listView.setAdapter(cca);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long x) {
        TextView getId = (TextView) view.findViewById(R.id.list_id);
        final long id  =  Long.parseLong(getId.getText().toString());
        final Cursor c = dbHelper.oneData(id);
        c.moveToFirst();

        final AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
        b.setTitle("Pilih Aksi");

        String [] options = {"Lihat data","Edit data","Hapus data"};
        b.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        final AlertDialog.Builder viewData = new AlertDialog.Builder(MainActivity.this);
                        inflater = getLayoutInflater();
                        vdialog = inflater.inflate(R.layout.view_data,null);
                        viewData.setView(vdialog);
                        viewData.setTitle("Lihat data");

                        view_nomor = (TextView) vdialog.findViewById(R.id.view_nomor);
                        view_nama = (TextView) vdialog.findViewById(R.id.view_nama);
                        view_tempat = (TextView) vdialog.findViewById(R.id.view_tempat);
                        view_tgl = (TextView) vdialog.findViewById(R.id.view_tgl);
                        view_jk = (TextView) vdialog.findViewById(R.id.view_alamat);
                        view_alamat= (TextView) vdialog.findViewById(R.id.view_alamat);


                        view_nomor.setText(" Nomor : "+c.getString(c.getColumnIndex(DBHelper.row_nomor)));
                        view_nama.setText(" Nama : "+c.getString(c.getColumnIndex(DBHelper.row_nama)));
                        view_tempat.setText(" Tempat lahir : "+c.getString(c.getColumnIndex(DBHelper.row_tempat)));
                        view_tgl.setText(" Tanggal lahir : "+c.getString(c.getColumnIndex(DBHelper.row_tgl)));
                        view_jk.setText(" Jenis kelamin : "+c.getString(c.getColumnIndex(DBHelper.row_jk)));
                        view_alamat.setText(" Alamat : "+c.getString(c.getColumnIndex(DBHelper.row_alamat)));

                        viewData.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        viewData.show();
                }
                switch (which){
                    case 1:
                        Intent iddata = new Intent(MainActivity.this, EditActivity.class);
                        iddata.putExtra(DBHelper.row_id,id);
                        startActivity(iddata);
                }
                switch (which){
                    case 2:
                        AlertDialog.Builder b1 = new AlertDialog.Builder(MainActivity.this);
                        b1.setMessage("Data ini akan dihapus");
                        b1.setCancelable(true);
                        b1.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.deleteData(id);
                                Toast.makeText(MainActivity.this,"data terhapus",Toast.LENGTH_SHORT).show();
                                setListView();
                            }
                        });
                        b1.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = b1.create();
                        alertDialog.show();
                }
            }
        });
        AlertDialog dialog = b.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListView();
    }
}