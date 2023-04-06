package net.yousra.dore_meet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter {
    private Context mcontext;
    MusicienAdapter musicienAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<Musicien> musiciens, List<String> keys){

        mcontext = context;
        musicienAdapter = new MusicienAdapter(musiciens,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(musicienAdapter);
    }



    class MusicItemView extends RecyclerView.ViewHolder{


        private TextView nom;
        private TextView specialite;
        private TextView locali;



        private String key;

        public MusicItemView(ViewGroup parent) {
            super(LayoutInflater.from(mcontext).inflate(R.layout.musicienlist, parent, false));
            nom = (TextView) itemView.findViewById(R.id.nom);
            specialite = (TextView) itemView.findViewById(R.id.specia);
            locali = (TextView) itemView.findViewById(R.id.localisa);



        }
        public void Bind(Musicien m, String key){
            nom.setText(m.getName());
            specialite.setText(m.getSpecialite());
            locali.setText(m.getLocalisa());

            this.key = key;
        }

    }

    class MusicienAdapter extends RecyclerView.Adapter<MusicItemView>{

        private List<Musicien> musicienList;
        private List<String> mKeys;

        public MusicienAdapter(List<Musicien> musicienList, List<String> mKeys) {
            this.musicienList = musicienList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public MusicItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MusicItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MusicItemView holder, int position) {
             holder.Bind(musicienList.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return musicienList.size();
        }
    }


}

