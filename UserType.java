package com.minipro.starcsimul;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserType extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users);

        Button normal = (Button) findViewById(R.id.unormal);
        Button deaf = (Button) findViewById(R.id.udeaf);
        Button ddumb = (Button) findViewById(R.id.uddumb);

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserType.this, ChatN.class));

            }
        });

        deaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (UserType.this, ChatD.class));
            }
        });

        ddumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (UserType.this, Chat.class));
            }
        });

    }
}
