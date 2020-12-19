package com.example.newone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.newone.Controller.DatabaseHandler;
import com.example.newone.Model.Place;
import com.google.android.gms.maps.model.LatLng;
import java.util.HashMap;
import java.util.Map;
// adding place to the DB and show in the map
public class AddActivity<button> extends AppCompatActivity {

    TextView latitude;
    TextView longitude;
    EditText title;
    Button save;
    private DatabaseHandler db;
    private Bundle extras ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final MediaPlayer sound= MediaPlayer.create(this,R.raw.sound);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        latitude= findViewById(R.id.latitudetext);
        longitude=findViewById(R.id.longitudetext);
        title=findViewById(R.id.titleText);
        save=findViewById(R.id.buttonSave);

        db=new DatabaseHandler(this);
        extras= getIntent().getExtras();

        if(extras != null){
            latitude.setText(String.valueOf(extras.getDouble("latitude")));
            longitude.setText(String.valueOf(extras.getDouble("longitude")));

        }

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sound.start();
                db.addPlace(new Place(latitude.getText().toString(),longitude.getText().toString(),title.getText().toString()));

                Intent intent=new Intent(AddActivity.this,Maps1Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
