package cr.ac.una.security.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class AppContext {

    private static AppContext INSTANCE = null;
    private static HashMap<String, Object> context = new HashMap<>();

    private AppContext() {
        cargarPropiedades();
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (AppContext.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppContext();
                }
            }
        }
    }

    public static AppContext getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    private void cargarPropiedades() {
        try {
            FileInputStream configFile;

            File configFile1 = new File("classes/config/properties.ini");
            if (configFile1.exists()) {
                configFile = new FileInputStream(configFile1);
            } else {
                File configFile2 = new File("config/properties.ini");
                if (configFile2.exists()) {
                    configFile = new FileInputStream(configFile2);
                } else {
                    throw new IOException("Archivo de configuración no encontrado en ninguna ubicación.");
                }
            }

            Properties appProperties = new Properties();
            appProperties.load(configFile);
            configFile.close();

            if (appProperties.getProperty("propiedades.resturl") != null) {
                this.set("resturl", appProperties.getProperty("propiedades.resturl"));
            }
        } catch (IOException io) {
            System.out.println("Archivo de configuración no encontrado.");
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public Object get(String parameter) {
        return context.get(parameter);
    }

    public void set(String nombre, Object valor) {
        context.put(nombre, valor);
    }

    public void delete(String parameter) {
        context.put(parameter, null);
    }

}