package com.example.newcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class SettingsActivity extends AppCompatActivity {

    private EditText path;
    private Button okBtn;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        switch (requestCode){
            case REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    init();
                }else {
                    Toast.makeText(getApplicationContext(), "Разрешение отсутствует", Toast.LENGTH_LONG).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void init(){

        okBtn = findViewById(R.id.okBtn);
        path = findViewById(R.id.pathText);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permissionStatus = ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionStatus == PackageManager.PERMISSION_GRANTED){

                    String fileName = "";
                    fileName = path.getText().toString().trim();

                    if (isExternalStorageReadable()){
                        File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                        if (imageFile.exists() && imageFile.isFile()){

                            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                            intent.putExtra("pathKey", fileName);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), "Неверное имя файла!", Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    ActivityCompat.requestPermissions(SettingsActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                }
            }
        });
    }



    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
