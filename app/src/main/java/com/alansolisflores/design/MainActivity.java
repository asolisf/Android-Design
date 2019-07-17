package com.alansolisflores.design;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alansolisflores.design.Helpers.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;
    private final String FULL_NAME = "Alan Solis Flores";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
    }

    private void initializeComponents()
    {
        loginButton = findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
        intent.putExtra(Constants.FULL_NAME_EXTRA,FULL_NAME);
        startActivity(intent);
    }
}
