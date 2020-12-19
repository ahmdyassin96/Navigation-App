package com.example.newone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {


    private Button button;
    private Button buttonSignin,buttonRegister,buttonHelp;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // init buttons in XML page
        buttonSignin=findViewById(R.id.button3);
        buttonRegister=findViewById(R.id.button);
        buttonHelp=findViewById(R.id.buttonHelp);



        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivitySignin();
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityRegister();
            }
        });
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

    }

    public void openActivityRegister(){
        Intent intent=new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    public void openActivitySignin(){
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}
