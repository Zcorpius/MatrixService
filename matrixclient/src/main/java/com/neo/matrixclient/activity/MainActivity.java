package com.neo.matrixclient.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.neo.matrixclient.R;
import com.neo.matrixclient.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Button btnStartService = findViewById(R.id.btn_start_service);
        Button btnGetState = findViewById(R.id.btn_get_state);
        Button btnSetState = findViewById(R.id.btn_set_state);
        Button btnStopService = findViewById(R.id.btn_stop_service);
        TextView tvState = findViewById(R.id.tv_state);

        btnStartService.setOnClickListener(v -> viewModel.startService());
        btnGetState.setOnClickListener(v -> viewModel.getState());
        btnSetState.setOnClickListener(v -> viewModel.setState());
        btnStopService.setOnClickListener(v -> viewModel.stopService());

        viewModel.stateText.observe(this, tvState::setText);
        viewModel.toastMessage.observe(this, msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());


    }
}