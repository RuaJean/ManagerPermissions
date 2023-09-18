package com.edu.practicaguiadapermisos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private Activity activity;

    //Version de android;
    private TextView versionAndroid;
    private int versionSDK;

    //Batería
    private ProgressBar pbLevelBaterry;
    private TextView tvLevelBaterry;
    IntentFilter baterryFilter;

    //Camera
    CameraManager cameraManager;
    String cameraId;
    private Button btnOnLigth;
    private Button btnOffLigth;

    //Archivo
    private EditText nameFile;
    private Archivo archivo;
    private Button imageButton;

    //Bluetooh
    private Button btnBluetooh;
    BluetoothAdapter adapter;

    //Conexión
    private TextView tvConexion;
    ConnectivityManager conexion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        begin();
        baterryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(broadcastReceiver, baterryFilter);

        this.btnOnLigth.setOnClickListener(this::onLingth);
        this.btnOffLigth.setOnClickListener(this::offLingth);

        //bluetooh
        adapter = BluetoothAdapter.getDefaultAdapter();

        btnBluetooh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBluetooh();
            }
        });

    }

    // En tu actividad, solicita permisos de Bluetooth en tiempo de ejecución


    private void btnBluetooh() {
        if (adapter == null) {
            // El dispositivo no es compatible con Bluetooth
            // Puedes manejar esto según tus necesidades
            return;
        }

        if (!adapter.isEnabled()) {
            // El Bluetooth está apagado, intenta encenderlo
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                adapter.enable();
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

        } else {
            // El Bluetooth está encendido, apágalo
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                adapter.disable();
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

        }
    }

    private void begin() {
        this.context = getApplicationContext();
        this.activity = this;
        this.versionAndroid = findViewById(R.id.tvVersionAndroid);
        this.pbLevelBaterry = findViewById(R.id.pbLevelBaterry);
        this.tvLevelBaterry = findViewById(R.id.tvLevelBaterry_2);
        this.nameFile = findViewById(R.id.etNameFile);
        this.tvConexion = findViewById(R.id.tvConexion);
        this.btnOnLigth = findViewById(R.id.btnOn);
        this.btnOffLigth = findViewById(R.id.btnOff);
        this.btnBluetooh = findViewById(R.id.Bluetooh);
    }

    //SO version
    @Override
    protected void onResume() {
        super.onResume();
        String versionSO = Build.VERSION.RELEASE;
        versionSDK = Build.VERSION.SDK_INT;
        versionAndroid.setText("Versión SO: " + versionSO + " /SDK: " + versionSDK);
        checkConnetion();
    }

    //Linterna
    private void onLingth(View view) {
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        try {
            cameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void offLingth(View view) {
        try {
            cameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //Batería
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int levelBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            pbLevelBaterry.setProgress((levelBattery));
            tvLevelBaterry.setText("Nivel de la batería: " + levelBattery + " %");

        }
    };

    private void checkConnetion() {
        conexion = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conexion.getActiveNetworkInfo();
        boolean stateNet = network != null && network.isConnectedOrConnecting();
        if (stateNet) tvConexion.setText("Status: Connected");
        else tvConexion.setText("Status: Disconnected");
    }

    private void storageFiles(){

    }


}