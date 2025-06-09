package com.example.uap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.example.uap.model.Plant;
import java.text.NumberFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private ImageView ivPlantDetailImage;
    private TextView tvPlantDetailName, tvPlantDetailPrice, tvPlantDetailDescription;
    private Button btnUpdate;
    private Plant plant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        ivPlantDetailImage = findViewById(R.id.ivPlantDetailImage);
        tvPlantDetailName = findViewById(R.id.tvPlantDetailName);
        tvPlantDetailPrice = findViewById(R.id.tvPlantDetailPrice);
        tvPlantDetailDescription = findViewById(R.id.tvPlantDetailDescription);
        btnUpdate = findViewById(R.id.btn_update);

        plant = (Plant) getIntent().getSerializableExtra(AddEditActivity.EXTRA_PLANT);

        if (plant != null) {
            populateData();
        }

        btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, AddEditActivity.class);
            intent.putExtra(AddEditActivity.EXTRA_PLANT, plant);
            startActivity(intent);
        });
    }

    private void populateData() {
        tvPlantDetailName.setText(plant.getName());
        tvPlantDetailDescription.setText(plant.getDescription());

        try {
            double priceValue = Double.parseDouble(plant.getPrice());
            Locale localeID = new Locale("in", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
            formatRupiah.setMaximumFractionDigits(0);
            tvPlantDetailPrice.setText(formatRupiah.format(priceValue));
        } catch (Exception e) {
            tvPlantDetailPrice.setText(plant.getPrice());
        }

        Glide.with(this)
                .load(plant.getImage())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(ivPlantDetailImage);
    }

    @Override
    public void onBackPressed() {
        findViewById(R.id.toolbar).callOnClick();
        super.onBackPressed();
    }
}