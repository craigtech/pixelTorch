package com.example.torch;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {
    ImageButton imageButton;
    boolean state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton = findViewById(R.id.torchButton);

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                lightTorch();
            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(MainActivity.this, "Camera permission is required.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
            }
        }).check();
    }

    private void lightTorch() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (!state) {
                    CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                    try {
                        String cameraID = cameraManager.getCameraIdList()[0];
                        cameraManager.setTorchMode(cameraID, true);
                        state = true;
                        imageButton.setImageResource(R.drawable.lit);
                        imageButton.setBackgroundResource(R.drawable.gradient_background);
                    } catch (CameraAccessException e) { }
                } else {
                    CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                    try {
                        String cameraID = cameraManager.getCameraIdList()[0];
                        cameraManager.setTorchMode(cameraID, false);
                        state = false;
                        imageButton.setImageResource(R.drawable.unlit);
                        imageButton.setBackgroundResource(R.color.black);
                    } catch (CameraAccessException e) { }
                }
            }
        });
    }
}