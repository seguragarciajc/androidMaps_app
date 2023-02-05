package com.example.comov;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class StorageHelper {



    /**
     * Escribe (sobrescribiendo si el archivo ya existÃ­a) el contenido "content" en un fichero llamado "filename" en el alamacenamiento "externo" de la app. Este se podrÃ¡
     * encontrar a travÃ©s de un explorador de archivos (PC o el del propio telÃ©fono) en AlmacenamientoInternoCompartido>Android>Data>(nombre paquete de app)>.
     * Los ficheros asÃ­ almacenados se borrarÃ¡n si la aplicaciÃ³n se desinstala.
     * @param filename Nombre del fichero
     * @param content Contenido que se escribirÃ¡ en el fichero
     * @param context Contexto (e.g. Activity) desde el que se llama al mÃ©todo
     * @throws IOException
     */
    public static void saveStringToFile(String filename,String content, Context context) throws IOException {
        File file = new File(context.getExternalFilesDir(null), filename);
        FileWriter writer= new FileWriter(file,false);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    /**
     * Lee el contenido de un fichero y lo devuelve como String, a partir del nombre del fichero (relativo al mismo path mencionado en el mÃ©todo de escritura:
     * AlmacenamientoInternoCompartido>Android>Data>(nombre paquete de app)>).
     * @param filename Nombre del fichero
     * @param context Contexto (e.g. Activity) desde el que se llama al mÃ©todo
     * @throws IOException
     */
    public static String readStringFromFile(String filename, Context context) throws IOException {
        File file=new File(context.getExternalFilesDir(null),filename);
        FileInputStream inputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            content.append(line);
        }
        bufferedReader.close();
        inputStream.close();
        inputStreamReader.close();
        return content.toString();
    }

}
