package com.example.newone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newone.Controller.DatabaseHandler;
import com.example.newone.Controller.DatabaseHelper;
import com.example.newone.Model.Place;
import com.example.newone.Model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText userName,passWord,confirm;
    private Button regButton;
    private DatabaseHelper db;
    private Bundle extras ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //music
        final MediaPlayer sound= MediaPlayer.create(this,R.raw.sound);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // link  with database for new users
        db=new DatabaseHelper(this);

        //for new username
        userName=(EditText)findViewById(R.id.etUsername);
        // password
        passWord=(EditText)findViewById(R.id.etPassword);
        confirm=(EditText)findViewById(R.id.editTextConfirm);


        //
        regButton=(Button)findViewById(R.id.btnRegister);

        regButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String s1=userName.getText().toString();
                String s2=passWord.getText().toString();
                String s3=confirm.getText().toString();

                if(s1.equals("") || s2.equals("") || s3.equals("")){
                    Toast.makeText(getApplicationContext(),"Fields are empty",Toast.LENGTH_SHORT).show();
                }else
                {
                    if(s2.equals(s3)){
                        //username
                        Boolean checkUsername=db.checkUsername(s1);
                        // check if this user is exist
                        if(checkUsername==true){
                            Boolean insert=db.insert(s1,s2);
                            if(insert==true){
                                sound.start();
                                Toast.makeText(getApplicationContext(),"Registered Successfully !\n Welcome "+s1,Toast.LENGTH_SHORT).show();
                                // m3ver aoto llmap
                                Intent intent=new Intent(RegisterActivity.this, Maps1Activity.class);
                                startActivity(intent);
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Email already exists",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(RegisterActivity.this, Maps1Activity.class);
                            startActivity(intent);
                        }
                    }
                    else
                    Toast.makeText(getApplicationContext(),"Password doesn't match !",Toast.LENGTH_SHORT).show();
                }
            }
        });




    }


}
