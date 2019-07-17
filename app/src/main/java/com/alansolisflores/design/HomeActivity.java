package com.alansolisflores.design;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alansolisflores.design.Helpers.Constants;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView messageTextView;
    private Button intentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        messageTextView = findViewById(R.id.MessageTextView);
        intentButton = findViewById(R.id.IntentButton);

        intentButton.setOnClickListener(HomeActivity.this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            messageTextView.setText(bundle.getString(Constants.FULL_NAME_EXTRA));
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(HomeActivity.this,MainSecondActivity.class);
        startActivity(intent);
    }
}
