package com.example.healthcare.sqllite;

import static com.example.healthcare.sqllite.DBmain.TABLENAME;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.healthcare.ConsultantActivity;
import com.example.healthcare.MainActivity;
import com.example.healthcare.NewsActivity;
import com.example.healthcare.ProfileActivity;
import com.example.healthcare.R;
import com.example.healthcare.api.MovieActivity;
import com.example.healthcare.databinding.ActivityTambahBeritaBinding;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class TambahBerita extends AppCompatActivity {
  private ActivityTambahBeritaBinding binding;
  private DrawerLayout drawer;
  private ActionBarDrawerToggle toggle;
  DBmain dBmain;
  SQLiteDatabase sqLiteDatabase;
  Calendar calendar;
  int id = 0;
  public static final int MY_CAMERA_REQUEST_CODE = 100;
  public static final int MY_STORAGE_REQUEST_CODE = 101;
  String cameraPermission[];
  String storagePermission[];
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    super.onCreate(savedInstanceState);
    binding = ActivityTambahBeritaBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    // action bar
    Toolbar toolbar = binding.toolbar;
    setSupportActionBar(toolbar);
    drawer = binding.drawerLayout;
    NavigationView navigationView = binding.navView;
    toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
    drawer.addDrawerListener(toggle);
    toggle.syncState();
    navigationView.setCheckedItem(R.id.nav_sql);
    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
          case R.id.nav_alarm:
            Intent intent = new Intent(TambahBerita.this, MainActivity.class);
            startActivity(intent);
            break;
          case R.id.nav_news:
            Intent intent1 = new Intent(TambahBerita.this, NewsActivity.class);
            startActivity(intent1);
            break;
          case R.id.nav_consult:
            Intent intent2 = new Intent(TambahBerita.this, ConsultantActivity.class);
            startActivity(intent2);
            break;
          case R.id.nav_profile:
            Intent intent3 = new Intent(TambahBerita.this, ProfileActivity.class);
            startActivity(intent3);
            break;
          case R.id.nav_sql:
            Intent intent4 = new Intent(TambahBerita.this, DisplayData.class);
            startActivity(intent4);
            break;
          case R.id.nav_movie:
            Intent intent5 = new Intent(TambahBerita.this, MovieActivity.class);
            startActivity(intent5);
            break;
        }
        return true;
      }
    });

    calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String date = dayOfMonth + "/" + month + "/" + year;
        binding.editPublishDate.setText(date);
      }
    };

    binding.editPublishDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(TambahBerita.this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
      }
    });

    dBmain = new DBmain(this);
    insertData();
    editData();
    binding.edtimage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int avatar = 0;
        if (avatar == 0) {
          if (!checkCameraPermission()) {
            requestCameraPermission();
          } else {
            pickFromGallery();
          }
        } else if (avatar == 1) {
          if (!checkStoragePermission()) {
            requestStoragePermission();
          } else {
            pickFromGallery();
          }
        }
      }
    });
  }
  private void editData() {
    if (getIntent().getBundleExtra("userdata")!= null){
      Bundle bundle = getIntent().getBundleExtra("userdata");
      id = bundle.getInt("id");
      binding.editTitle.setText(bundle.getString("title"));
      binding.editPublishDate.setText(bundle.getString("date_publish"));
      byte[]bytes = bundle.getByteArray("avatar");
      Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,
              0, bytes.length);
      binding.edtimage.setImageBitmap(bitmap);
      binding.btnSubmit.setVisibility(View.GONE);
      binding.btnEdit.setVisibility(View.VISIBLE);
      binding.textViewTitle.setText("Edit Data Berita " + bundle.getString("title"));
    }
  }
  private void requestStoragePermission() {
    requestPermissions(storagePermission,
            MY_STORAGE_REQUEST_CODE);
  }
  private boolean checkStoragePermission() {
    boolean result = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            (PackageManager.PERMISSION_GRANTED);
    return result;
  }
  private void pickFromGallery() {
    CropImage.activity().start(this);
  }
  private void requestCameraPermission() {
    requestPermissions(cameraPermission,
            MY_CAMERA_REQUEST_CODE);
  }
  private boolean checkCameraPermission() {
    boolean result = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            (PackageManager.PERMISSION_GRANTED);
    boolean result1 = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA) ==
            (PackageManager.PERMISSION_GRANTED);
    return result && result1;
  }
  private void insertData() {
    binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          if (binding.editTitle.getText().toString().isEmpty() ||
                  binding.editPublishDate.getText().toString().isEmpty() ||
                  binding.edtimage.getDrawable() == null) {
            Toast.makeText(TambahBerita.this, "Data tidak boleh kosong",
                    Toast.LENGTH_SHORT).show();
          } else {
            ContentValues cv = new ContentValues();
            cv.put("title", binding.editTitle.getText().toString());
            cv.put("date_publish",binding.editPublishDate.getText().toString());
            cv.put("avatar", imageViewToBy(binding.edtimage));

            sqLiteDatabase = dBmain.getWritableDatabase();
            Long rec = sqLiteDatabase.insert("berita",null ,cv);
            if (rec != null) {
              Toast.makeText(TambahBerita.this, "Data Inserted", Toast.LENGTH_SHORT).show();
              binding.editTitle.setText("");
              binding.editPublishDate.setText("");
              binding.edtimage.setImageResource(R.drawable.uploadimage);

              Intent intent = new Intent(TambahBerita.this, DisplayData.class);
              startActivity(intent);
            } else {
              Toast.makeText(TambahBerita.this, "Something Wrong", Toast.LENGTH_SHORT).show();
            }
          }
        } catch (Exception e) {
          Toast.makeText(TambahBerita.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      }
    });

    binding.btnDisplay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent( TambahBerita.this, DisplayData.class));
      }
    });

    binding.btnEdit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ContentValues cv = new ContentValues();

        cv.put("title",binding.editTitle.getText().toString());
        cv.put("date_publish",binding.editPublishDate.getText().toString());
        cv.put("avatar", imageViewToBy(binding.edtimage));
        sqLiteDatabase = dBmain.getWritableDatabase();
        long recedit = sqLiteDatabase.update(TABLENAME,cv,"id="+id, null);
        if(recedit != -1){
          Toast.makeText(TambahBerita.this, "Update Succesfully", Toast.LENGTH_SHORT).show();
          binding.editTitle.setText("");
          binding.editPublishDate.setText("");
          binding.edtimage.setImageResource(R.drawable.uploadimage);
          binding.btnEdit.setVisibility(View.GONE);
          binding.btnSubmit.setVisibility(View.VISIBLE);
          binding.textViewTitle.setText("Tambah Data Berita");
          Intent a = new Intent(TambahBerita.this, DisplayData.class);
          startActivity(a);
        }
      }
    });
  }
  public static byte[] imageViewToBy(ImageView avatar) {
    Bitmap bitmap = ((BitmapDrawable)
            avatar.getDrawable()).getBitmap();
    ByteArrayOutputStream stream = new
            ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
    byte[] bytes = stream.toByteArray();
    return bytes;
  }
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode,
            permissions, grantResults);
    switch (requestCode) {
      case MY_CAMERA_REQUEST_CODE: {
        if (grantResults.length > 0) {
          boolean camera_accepted = grantResults[0] ==
                  PackageManager.PERMISSION_GRANTED;
          boolean storage_accepted = grantResults[1] ==
                  PackageManager.PERMISSION_GRANTED;
          if (camera_accepted && storage_accepted) {
            pickFromGallery();
          } else {
            Toast.makeText(this, "enable camera and storage permission", Toast.LENGTH_SHORT).show();
          }
        }
      }
      break;
      case MY_STORAGE_REQUEST_CODE: {
        boolean storage_accepted = grantResults[0] ==
                PackageManager.PERMISSION_GRANTED;
        if (storage_accepted) {
          pickFromGallery();
        } else {
          Toast.makeText(this, "please enable storage permission", Toast.LENGTH_SHORT).show();
        }
      }
      break;
    }
  }
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
      CropImage.ActivityResult result =
              CropImage.getActivityResult(data);
      if(resultCode == RESULT_OK){
        Uri resultUri = result.getUri();
        Picasso.with(this).load(resultUri).into(binding.edtimage);
      }
    }
  }
}