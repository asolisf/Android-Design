package com.alansolisflores.design;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alansolisflores.design.Helpers.Constants;

import java.security.InvalidParameterException;

public class MainSecondActivity extends AppCompatActivity implements View.OnClickListener {

    //region Properties

    /**
     * Define UI elements
     */
    private EditText phoneEditText;
    private EditText webEditText;

    private ImageButton cameraImageButton;
    private ImageButton phoneImageButton;
    private ImageButton webImageButton;

    /**
     * Permissions request codes
     */
    private final int PHONE_CALL_CODE = 100;
    private final int CAMERA_CODE = 101;
    private final int WEB_CODE = 102;

    //endregion Properties


    //region Methods

    /**
     * Main method of activity
     * @param savedInstanceState Bundle saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_second);
        initializeComponents();
    }

    /**
     * Initialize UI elements and set on click listener
     */
    private void initializeComponents() {
        phoneEditText       = findViewById(R.id.PhoneEditText);
        webEditText         = findViewById(R.id.WebEditText);
        phoneImageButton    = findViewById(R.id.PhoneImageButton);
        cameraImageButton   = findViewById(R.id.CameraImageButton);
        webImageButton      = findViewById(R.id.WebImageButton);

        phoneImageButton.setOnClickListener(MainSecondActivity.this);
        cameraImageButton.setOnClickListener(MainSecondActivity.this);
        webImageButton.setOnClickListener(MainSecondActivity.this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.CameraImageButton:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_CODE);
                }else {
                    olderVersionCamera();
                }
                break;
            case R.id.PhoneImageButton:
                String phoneNumber = phoneEditText.getText().toString();

                if(phoneNumber != null && !phoneNumber.trim().isEmpty())
                {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        if(checkPermission(Manifest.permission.CALL_PHONE))
                        {
                            callNumber(phoneNumber);
                        }else{
                            if(!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE))
                            {
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                        PHONE_CALL_CODE);
                            }else {
                                Toast.makeText(MainSecondActivity.this,
                                        "Please, enable request permission",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:"+getPackageName()));

                                //When click back button in settings return here
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                                startActivity(intent);
                            }
                        }
                    }else{
                        oldVersionsCallPhone(phoneNumber);
                    }
                }else{
                    Toast.makeText(MainSecondActivity.this,
                            "Insert phone number.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.WebImageButton:
                String webUri = webEditText.getText().toString();

                if(webUri != null && !webUri.trim().isEmpty())
                {
                    openWeb(webUri);
                }else{
                    Toast.makeText(MainSecondActivity.this,
                            "Insert web.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                throw  new InvalidParameterException("Invalid parameter "+view.getId());
        }
    }

    /**
     * This method is a override callback that is executed after request permission method
     * @param requestCode Integer with request code
     * @param permissions String array with permission
     * @param grantResults Integer array with granted results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        String permission = permissions[Constants.INTEGER_DEFAULT];
        int grantResult = grantResults[Constants.INTEGER_DEFAULT];

        switch(requestCode)
        {
            case CAMERA_CODE:
                if(permission.equals(Manifest.permission.CAMERA))
                {
                    if(grantResult == PackageManager.PERMISSION_GRANTED)
                    {
                        openCamera();
                    }else {
                        Toast.makeText(MainSecondActivity.this,
                                "You declined camera access.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case PHONE_CALL_CODE:
                if(permission.equals(Manifest.permission.CALL_PHONE))
                {
                    if(grantResult == PackageManager.PERMISSION_GRANTED)
                    {
                        String phoneNumber = phoneEditText.getText().toString();
                        callNumber(phoneNumber);
                    }else{
                        Toast.makeText(MainSecondActivity.this,
                                "You declined call phone access.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }

    }

    /**
     * SDK versions less to android M, call this method for open call phone
     * @param phoneNumber String phone number
     */
    private void oldVersionsCallPhone(String phoneNumber)
    {
        if(checkPermission(Manifest.permission.CALL_PHONE))
        {
            callNumber(phoneNumber);
        }else {
            Toast.makeText(MainSecondActivity.this,
                    "You don't have phone permission.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void olderVersionCamera()
    {
        if(checkPermission(Manifest.permission.CAMERA))
        {
            openCamera();
        }else {
            Toast.makeText(MainSecondActivity.this,
                    "You don't have camera permission.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Call method startActivity() with intent action call
     * @param phoneNumber String phone number
     */
    private void callNumber(String phoneNumber){
        Intent intentCall = new Intent(Intent.ACTION_CALL,
                Uri.parse(String.format(Constants.PHONE_FORMAT,phoneNumber)));
        startActivity(intentCall);
    }

    /**
     * Open web uri in browser
     * @param webUri String web uri
     */
    private void openWeb(String webUri){
        Intent intentWeb = new Intent(Intent.ACTION_VIEW);
        intentWeb.setData(Uri.parse(webUri));
        startActivity(intentWeb);
    }

    private void openCamera(){
        Intent intentCall = new Intent(Intent.ACTION_CAMERA_BUTTON);
        startActivity(intentCall);
    }


    /**
     * Method check if permission is granted
     * @param permission Android manifest permission
     * @return Boolean if is granted or denied
     */
    private boolean checkPermission(String permission)
    {
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    //endregion Methods


}
