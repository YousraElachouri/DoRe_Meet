package net.yousra.dore_meet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ApproximiteActivity extends AppCompatActivity {
    RecyclerView musicienList;
    private TextView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approximite);
        banner = (TextView) findViewById(R.id.banner);
        musicienList = (RecyclerView) findViewById(R.id.recyclerview);
        musicienList.setLayoutManager(new LinearLayoutManager(this));
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ApproximiteActivity.this, MainActivity.class));
            }
        });

        new ReadDataFire().readMusicien(new ReadDataFire.DataStatus() {
            @Override
            public void dataIsLoader(List<Musicien> musiciens, List<String> keys) {

                new UserAdapter().setConfig(musicienList,ApproximiteActivity.this,musiciens,keys);

            }

            @Override
            public void dataISInserted() {

            }

            @Override
            public void dataIsUpdated() {

            }

            @Override
            public void dataIsDeleted() {

            }
        });


        
    }




}