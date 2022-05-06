package com.example.myproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{


    ArrayList<SongModel> songList;
    Context context;

    public CustomAdapter(ArrayList<SongModel> songList, Context context) {
        this.songList = songList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new CustomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SongModel song_data = songList.get(position);
        holder.title.setText(song_data.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player.getInstance().reset();
                Player.currentIndex = position;
                Intent intent = new Intent(context,PlayerActivity.class);
                intent.putExtra("LIST",songList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView icon;
        public ViewHolder(View item){
            super(item);
            title = item.findViewById(R.id.songNameList);
            icon = item.findViewById(R.id.imgSongList);
            title.setSelected(true);
        }
    }
}
