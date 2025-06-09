package com.example.uap;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.example.uap.model.Plant;
import com.example.uap.network.ApiClient;
import com.example.uap.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditActivity extends AppCompatActivity {
    public static final String EXTRA_PLANT = "EXTRA_PLANT";

    private EditText etName, etPrice, etDescription;
    private ImageView ivPlantImage;
    private Button btnSave;
    private ApiService apiService;
    private boolean isEditMode = false;
    private Plant plantToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etDescription = findViewById(R.id.etDescription);
        ivPlantImage = findViewById(R.id.ivPlantDetailImage);
        btnSave = findViewById(R.id.btnSave);
        apiService = ApiClient.getClient().create(ApiService.class);

        if (getIntent().hasExtra(EXTRA_PLANT)) {
            isEditMode = true;
            plantToEdit = (Plant) getIntent().getSerializableExtra(EXTRA_PLANT);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Update Item");
            }
            btnSave.setText("Update");
            populateForm(plantToEdit);
        } else {
            isEditMode = false;
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Tambah Item");
            }
            btnSave.setText("Tambah");
            Glide.with(this).load(R.drawable.placeholder_image).into(ivPlantImage);
        }

        btnSave.setOnClickListener(v -> savePlant());
    }

    private void populateForm(Plant plant) {
        etName.setText(plant.getName());
        etPrice.setText(plant.getPrice());
        etDescription.setText(plant.getDescription());
        Glide.with(this)
                .load(plant.getImage())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(ivPlantImage);
    }

    private void savePlant() {
        String name = etName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        Plant plant = new Plant(name, price, description);

        if (isEditMode) {
            updatePlant(plantToEdit.getName(), plant);
        } else {
            createPlant(plant);
        }
    }

    private void createPlant(Plant plant) {
        apiService.createPlant(plant).enqueue(new Callback<Plant>() {
            @Override
            public void onResponse(Call<Plant> call, Response<Plant> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddEditActivity.this, "Tanaman ditambahkan", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddEditActivity.this, "Gagal menambahkan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Plant> call, Throwable t) {
                Toast.makeText(AddEditActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePlant(String originalName, Plant plant) {
        apiService.updatePlant(originalName, plant).enqueue(new Callback<Plant>() {
            @Override
            public void onResponse(Call<Plant> call, Response<Plant> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddEditActivity.this, "Tanaman diperbarui", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddEditActivity.this, "Gagal memperbarui", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Plant> call, Throwable t) {
                Toast.makeText(AddEditActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}