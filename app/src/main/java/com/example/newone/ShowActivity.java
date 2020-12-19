package com.example.newone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newone.Controller.DatabaseHandler;
import com.example.newone.Model.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

// showing information for the place
public class ShowActivity extends AppCompatActivity implements DirectionFinderListener {

    private TextView latitude,longitude,title;
    private Button delete , go;
    private DatabaseHandler db;
    private Bundle extras;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //xml
        setContentView(R.layout.activity_show);


        // xml
        latitude= findViewById(R.id.textViewlatitude);
        longitude=findViewById(R.id.textViewlongitude);
        title=findViewById(R.id.textTitle);
        delete=findViewById(R.id.buttonDelete);
        go=findViewById(R.id.buttonGo);
        


        db=new DatabaseHandler(this);
        extras= getIntent().getExtras();

        if(extras != null){

            latitude.setText(String.valueOf(extras.getDouble("latitude")));
            longitude.setText(String.valueOf(extras.getDouble("longitude")));
            title.setText(extras.getString("title"));

        }
        // get direction for this location
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String vv="israel "+title.getText().toString();;
                Intent intent=new Intent(ShowActivity.this,Maps1Activity.class);
                intent.putExtra("title",vv);
                startActivity(intent);
                finish();

            }
        });
        // delete the favorite place from the map
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //sound.start();
                db.deletePlace(new Place(latitude.getText().toString(),longitude.getText().toString(),title.getText().toString()));

                Intent intent=new Intent(ShowActivity.this,Maps1Activity.class);
                startActivity(intent);
                finish();
            }
        });


    }


    @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {

    }
}
