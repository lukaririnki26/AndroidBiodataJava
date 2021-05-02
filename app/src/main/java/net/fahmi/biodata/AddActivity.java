package net.fahmi.biodata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
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

public class AddActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    public EditText add_nomor, add_nama, add_tempat, add_tgl, add_alamat;
    public Spinner add_jk;
    private  long id;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormat;
    private CircularImageView imageView;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbHelper = new DBHelper(this);
        id = getIntent().getLongExtra(DBHelper.row_id,0);

        add_nomor = (EditText) findViewById(R.id.add_nomor);
        add_nama = (EditText) findViewById(R.id.add_nama);
        add_tempat = (EditText) findViewById(R.id.add_tempat);
        add_tgl = (EditText) findViewById(R.id.add_tgl);
        add_alamat = (EditText) findViewById(R.id.add_alamat);
        add_jk = (Spinner) findViewById(R.id.add_jk);
        imageView =(CircularImageView) findViewById(R.id.add_img);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        add_tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(AddActivity.this);
            }
        });

    }

    private void showDateDialog() {
        Calendar c = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,month,dayOfMonth);
                add_tgl.setText(dateFormat.format(newDate.getTime()));
            }
        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return  true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            Uri imguri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imguri)) {
                uri = imguri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                startCrop(imguri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(result.getUri());
                uri = result.getUri();
            }
        }
    }

    private void startCrop(Uri imguri) {
        CropImage.activity(imguri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);
        uri= imguri;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_save :
                String nomor = add_nomor.getText().toString().trim();
                String nama = add_nama.getText().toString().trim();
                String tempat = add_tempat.getText().toString().trim();
                String tgl = add_tgl.getText().toString().trim();
                String jk = add_jk.getSelectedItem().toString().trim();
                String alamat = add_alamat.getText().toString().trim();

                ContentValues values = new ContentValues();

                values.put(DBHelper.row_nomor,nomor);
                values.put(DBHelper.row_nama,nama);
                values.put(DBHelper.row_tempat,tempat);
                values.put(DBHelper.row_tgl,tgl);
                values.put(DBHelper.row_jk,jk);
                values.put(DBHelper.row_alamat,alamat);
                values.put(DBHelper.row_img,String.valueOf(uri));

                if(nama.equals("")|| nomor.equals("")|| tempat.equals("")||tgl.equals("")||alamat.equals("")){
                    Toast.makeText(AddActivity.this, "Empty value not permitted", Toast.LENGTH_SHORT).show();

                }else {
                    dbHelper.insertData(values);
                    Toast.makeText(AddActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        return  super.onOptionsItemSelected(item);
    }
}