package com.example.pm1er201910050084;

import android.content.Context;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class ViewHolder extends RecyclerView.ViewHolder {

    View mview;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mview = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        itemView.setOnLongClickListener(new OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });


    }

    public void setDetails(Context ctx, String title,String Image){

        TextView mTitle = mview.findViewById(R.id.rTiletv);
        ImageView mImage = mview.findViewById(R.id.rImage);

        mTitle.setText(title);

        //Picasso.get().load(Image);
    }

    private ViewHolder.ClickListener ClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }


    public void setOnClickListener (ViewHolder.ClickListener clickListener){

    }



}
