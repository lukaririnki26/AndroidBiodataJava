package net.fahmi.biodata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    public EditText edit_nomor, edit_nama, edit_tempat, edit_tgl, edit_alamat;
    public Spinner edit_jk;
    private long id;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormat;
    private CircularImageView imageView;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        uri =null;
        dbHelper = new DBHelper(this);
        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        edit_nomor = (EditText) findViewById(R.id.edit_nomor);
        edit_nama = (EditText) findViewById(R.id.edit_nama);
        edit_tempat = (EditText) findViewById(R.id.edit_tempat);
        edit_tgl = (EditText) findViewById(R.id.edit_tgl);
        edit_alamat = (EditText) findViewById(R.id.edit_alamat);
        edit_jk = (Spinner) findViewById(R.id.edit_jk);
        imageView =(CircularImageView) findViewById(R.id.edit_img);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        edit_tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity(uri).start(EditActivity.this);
            }
        });
        getData();
    }

    private void getData() {
        Cursor c =  dbHelper.oneData(id);
        if(c.moveToFirst()){
            String nomor = c.getString(c.getColumnIndex(DBHelper.row_nomor));
            String nama = c.getString(c.getColumnIndex(DBHelper.row_nama));
            String tempat = c.getString(c.getColumnIndex(DBHelper.row_tempat));
            String tgl = c.getString(c.getColumnIndex(DBHelper.row_tgl));
            String jk = c.getString(c.getColumnIndex(DBHelper.row_jk));
            String alamat = c.getString(c.getColumnIndex(DBHelper.row_alamat));
            String foto = c.getString(c.getColumnIndex(DBHelper.row_img));
            edit_nomor.setText(nomor);
            edit_nama.setText(nama);
            if(jk.equals("Laki-Laki")){
                edit_jk.setSelection(0);
            }else if (jk.equals("Perempuan")){
                edit_jk.setSelection(1);
            }
            edit_tempat.setText(tempat);
            edit_tgl.setText(tgl);
            edit_alamat.setText(alamat);
            if(foto.equals("null")){
                imageView.setImageResource(R.drawable.ic_person);
            }else{
                imageView.setImageURI(Uri.parse(foto));
            }

        }
        c.close();
    }

    private void showDateDialog() {
        Calendar c = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,month,dayOfMonth);
                edit_tgl.setText(dateFormat.format(newDate.getTime()));
            }
        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_save :
                String nomor = edit_nomor.getText().toString().trim();
                String nama = edit_nama.getText().toString().trim();
                String tempat = edit_tempat.getText().toString().trim();
                String tgl = edit_tgl.getText().toString().trim();
                String jk = edit_jk.getSelectedItem().toString().trim();
                String alamat = edit_alamat.getText().toString().trim();
                Cursor c =  dbHelper.oneData(id);

                ContentValues values = new ContentValues();

                values.put(DBHelper.row_nomor,nomor);
                values.put(DBHelper.row_nama,nama);
                values.put(DBHelper.row_tempat,tempat);
                values.put(DBHelper.row_tgl,tgl);
                values.put(DBHelper.row_jk,jk);
                values.put(DBHelper.row_alamat,alamat);
                if(c.moveToFirst()) {
                    String foto = c.getString(c.getColumnIndex(DBHelper.row_img));
                    if(!uri.equals("null")){
                        values.put(DBHelper.row_img,String.valueOf(uri));
                    }else{
                        values.put(DBHelper.row_img,String.valueOf(foto));
                    }
                }
                c.close();
                if(nama.equals("")|| nomor.equals("")|| tempat.equals("")||tgl.equals("")||alamat.equals("")){
                    Toast.makeText(EditActivity.this, "Empty value not permitted", Toast.LENGTH_SHORT).show();

                }else {
                    dbHelper.updateData(values,id);
                    Toast.makeText(EditActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        switch (item.getItemId()){
            case R.id.edit_delete:
                AlertDialog.Builder b1 = new AlertDialog.Builder(EditActivity.this);
                b1.setMessage("Data ini akan dihapus");
                b1.setCancelable(true);
                b1.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteData(id);
                        Toast.makeText(EditActivity.this,"data terhapus",Toast.LENGTH_SHORT).show();
                        finish();
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
        return  super.onOptionsItemSelected(item);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resulturi = result.getUri();
                uri = resulturi;
                imageView.setImageURI(resulturi);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}