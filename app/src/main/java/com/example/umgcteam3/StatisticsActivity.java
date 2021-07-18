package com.example.umgcteam3;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;

public class StatisticsActivity extends AppCompatActivity {
    GraphView graph;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(getDataPoint());
    private Spinner spinner;
    TextView fullName;
    ImageView profileImage;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    public String userId;
    FirebaseUser user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);
        fullName = findViewById(R.id.fullName);
        profileImage = findViewById(R.id.profileImage);
        spinner = findViewById(R.id.exercise_name_spinner);
        graph = findViewById(R.id.graph);
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        try {
            storageReference = FirebaseStorage.getInstance().getReference();
            userId = fAuth.getCurrentUser().getUid();
            user = fAuth.getCurrentUser();
            fullName.setText(user.getDisplayName());
            StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profileImage);
                }
            });
        } catch (Exception e) {
            Log.d("StatisticsActivity: ", e.getMessage());
            finish();
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.exercise_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        graph.addSeries(series);
    }

    private DataPoint[] getDataPoint(){
        DataPoint[] dataPoints = new DataPoint[]{
                //grab data points from database based on selected drop-down
        };
        return dataPoints;
    }

    public void goToProfile(View view){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }


}