package com.neo.matrixserver.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.neo.matrixserver.R;
import com.neo.matrixserver.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Button btnStartService = findViewById(R.id.btn_start_service);
        Button btnStopService = findViewById(R.id.btn_stop_service);

        btnStartService.setOnClickListener(v -> viewModel.startService());
        btnStopService.setOnClickListener(v -> viewModel.stopService());

        viewModel.toastMessage.observe(this, msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }
}
