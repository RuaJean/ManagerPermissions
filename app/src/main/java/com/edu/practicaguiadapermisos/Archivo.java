package com.edu.practicaguiadapermisos;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Archivo {
    private Context context;
    private Activity activity;

    public Archivo(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    private void crearDir(File file){
        if (!file.exists()) file.mkdirs();
    }

    public void guardarArchivo(String nombreArchivo, String infomacion){
        File directorio = null;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            //Para la versión menor o igual a P(9.0)
            directorio = new File(Environment.getExternalStorageDirectory(),"ArchivoClase");
            crearDir(directorio);
            Toast.makeText(context, "Ruta: "+directorio, Toast.LENGTH_LONG).show();
        }else{
            //Versiones Q(10.0) y posteriores
            directorio = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),"ArchivoClase");
            crearDir(directorio);
            Toast.makeText(context, "Ruta: "+directorio, Toast.LENGTH_LONG).show();
        }
        if (directorio != null){
            //Creación del archivo
            File file = new File(directorio,nombreArchivo);
            try {
                FileWriter writer = new FileWriter(file);
                writer.append(infomacion);
                writer.flush();
                writer.close();
                Toast.makeText(context, "Se ha guardado el archivo: "+nombreArchivo, Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Log.i("Archivo","Guardar Archivo: "+e);
                Toast.makeText(context, ""+e, Toast.LENGTH_LONG).show();
            }
        }
    }
}
