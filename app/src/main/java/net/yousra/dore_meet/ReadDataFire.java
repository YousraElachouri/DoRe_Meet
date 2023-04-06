package net.yousra.dore_meet;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReadDataFire {


    private FirebaseDatabase myData;
    private DatabaseReference myRefMusicien;
    private List<Musicien> musiciens = new ArrayList<>();

    public interface DataStatus{

        void dataIsLoader(List<Musicien> musiciens, List<String> keys);
        void dataISInserted();
        void dataIsUpdated();
        void dataIsDeleted();
    }

    public ReadDataFire() {
        myData = FirebaseDatabase.getInstance();
        myRefMusicien = myData.getReference("Users");
    }

    public void readMusicien(final DataStatus dataStatus){

        myRefMusicien.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                musiciens.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keynode : snapshot.getChildren()){
                    keys.add(keynode.getKey());
                    Musicien musicien = keynode.getValue(Musicien.class);
                    musiciens.add(musicien);
                }
                dataStatus.dataIsLoader(musiciens,keys);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
