package com.example.uap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.uap.R;
import com.example.uap.model.Plant;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private List<Plant> plantList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDetailClick(Plant plant);
        void onDeleteClick(Plant plant);
    }

    public PlantAdapter(List<Plant> plantList, OnItemClickListener listener) {
        this.plantList = plantList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plant, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plantList.get(position);
        holder.bind(plant, listener);
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public void updateData(List<Plant> newPlantList) {
        this.plantList.clear();
        this.plantList.addAll(newPlantList);
        notifyDataSetChanged();
    }

    static class PlantViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlantImage;
        TextView tvPlantName, tvPlantPrice;
        Button btnDelete, btnDetail;

        public PlantViewHolder(View itemView) {
            super(itemView);
            ivPlantImage = itemView.findViewById(R.id.imageViewPlant);
            tvPlantName = itemView.findViewById(R.id.textViewPlantName);
            tvPlantPrice = itemView.findViewById(R.id.textViewPlantPrice);
            btnDelete = itemView.findViewById(R.id.buttonDelete);
            btnDetail = itemView.findViewById(R.id.buttonDetail);
        }

        public void bind(final Plant plant, final OnItemClickListener listener) {
            tvPlantName.setText(plant.getName());
            try {
                double priceValue = Double.parseDouble(plant.getPrice());
                Locale localeID = new Locale("in", "ID");
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                formatRupiah.setMaximumFractionDigits(0);
                tvPlantPrice.setText(formatRupiah.format(priceValue));
            } catch (Exception e) {
                tvPlantPrice.setText(plant.getPrice());
            }

            Glide.with(itemView.getContext())
                    .load(plant.getImage())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(ivPlantImage);

            btnDetail.setOnClickListener(v -> listener.onDetailClick(plant));
            btnDelete.setOnClickListener(v -> listener.onDeleteClick(plant));
        }
    }
}