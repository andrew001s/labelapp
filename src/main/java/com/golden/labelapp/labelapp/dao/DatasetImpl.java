package com.golden.labelapp.labelapp.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.golden.labelapp.labelapp.services.DatasetServices;

@Service
public class DatasetImpl implements DatasetServices {

    @Override
    public List<String> getFolder(String path) {
        File folder = new File(path);
        List<String> files = new ArrayList<>();
        if (folder.exists() && folder.isDirectory() ) {
            for (File file : folder.listFiles()) {
                if (isImage(file.getAbsolutePath()))
                    files.add(file.getAbsolutePath());
            }
        } else {
            throw new RuntimeException("La ruta de la carpeta no es válida o no existe");
        }

        return files;
    }
        
    private boolean isImage(String path) {
        String[] validExtensions = { "jpg", "jpeg", "png" };
        for (String extension : validExtensions) {
            if (path.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

}
