package com.example.newone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newone.Controller.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button bulogin;
    // link to database -for check
    DatabaseHelper db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //     meep meep sound
        final MediaPlayer sound= MediaPlayer.create(this,R.raw.sound);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //        DATABASE

        db=new DatabaseHelper(this);


        // XML
        username=(EditText)findViewById(R.id.etUsername);
        password=(EditText)findViewById(R.id.etPassword);
        bulogin=(Button)findViewById(R.id.btnLogin);

        // ON CLICK
        bulogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=username.getText().toString();
                String pass=password.getText().toString();
                // check if this username is exist in the database
                Boolean checkUserPass=db.emailPassword(user,pass);
                if(checkUserPass==true){
                    Toast.makeText(getApplicationContext(),"Successfully Login\n Welcome "+user,Toast.LENGTH_SHORT).show();
                    sound.start();
                    Intent intent=new Intent(LoginActivity.this, Maps1Activity.class);
                    startActivity(intent);
                }

                else
                    Toast.makeText(getApplicationContext(),"Something got wrong\n ",Toast.LENGTH_SHORT).show();


            }
        });

    }

}
