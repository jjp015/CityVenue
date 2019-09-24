package com.example.cityvenue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private ArrayList<CityItem> mCityItemArrayList;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    static class CityViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mLocationView;

        CityViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.city_image);
            mLocationView = itemView.findViewById(R.id.city_location);

            itemView.setOnClickListener(v -> {
                if(onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });
        }
    }

    CityAdapter(ArrayList<CityItem> cityList) {
        mCityItemArrayList = cityList;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent,
                false);
        return new CityViewHolder(v, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        CityItem currentItem = mCityItemArrayList.get(position);

        holder.mImageView.setImageResource(currentItem.getImage());
        holder.mLocationView.setText(currentItem.getLocation());
    }

    @Override
    public int getItemCount() {
        return mCityItemArrayList.size();
    }
}