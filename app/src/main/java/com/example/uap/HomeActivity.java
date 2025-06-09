package com.example.uap;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.uap.adapter.PlantAdapter;
import com.example.uap.model.Plant;
import com.example.uap.model.PlantResponse;
import com.example.uap.network.ApiClient;
import com.example.uap.network.ApiService;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private PlantAdapter adapter;
    private ApiService apiService;
    private FirebaseAuth mAuth;
    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        btn_add = findViewById(R.id.btn_add);

        mAuth = FirebaseAuth.getInstance();
        apiService = ApiClient.getClient().create(ApiService.class);
        setupRecyclerView();

        btn_add.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddEditActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchPlants();
    }

    private void setupRecyclerView() {
        adapter = new PlantAdapter(new ArrayList<>(), new PlantAdapter.OnItemClickListener() {
            @Override
            public void onDetailClick(Plant plant) {
                Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                intent.putExtra(AddEditActivity.EXTRA_PLANT, plant);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Plant plant) {
                showDeleteConfirmationDialog(plant);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    // Di dalam class HomeActivity

    private void fetchPlants() {
        progressBar.setVisibility(View.VISIBLE);
        apiService.getPlants().enqueue(new Callback<PlantResponse>() {
            @Override
            public void onResponse(Call<PlantResponse> call, Response<PlantResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Plant> plantList = response.body().getData();
                    if (plantList != null) {
                        adapter.updateData(plantList);
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlantResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                android.util.Log.e("API_FAILURE", "Error Fetching Plants: ", t);
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmationDialog(Plant plant) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Tanaman")
                .setMessage("Yakin ingin menghapus " + plant.getName() + "?")
                .setPositiveButton("Hapus", (dialog, which) -> deletePlant(plant.getName()))
                .setNegativeButton("Batal", null)
                .show();
    }

    private void deletePlant(String name) {
        apiService.deletePlant(name).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(HomeActivity.this, "Produk dihapus", Toast.LENGTH_SHORT).show();
                    fetchPlants();
                } else {
                    Toast.makeText(HomeActivity.this, "Gagal menghapus", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            mAuth.signOut();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}