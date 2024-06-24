package com.golden.labelapp.labelapp.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.golden.labelapp.labelapp.models.dtos.ObjectDetectDto;
import com.golden.labelapp.labelapp.models.entities.Image;
import com.golden.labelapp.labelapp.models.entities.Labels;
import com.golden.labelapp.labelapp.models.entities.YoloV5;
import com.golden.labelapp.labelapp.services.DatasetServices;
import com.golden.labelapp.labelapp.services.ImageServices;
import com.golden.labelapp.labelapp.services.LabelServices;
import com.golden.labelapp.labelapp.services.YoloV5Service;

/**
 * Controlador para el manejo de los conjuntos de datos.
 */
@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/dataset")
public class DatasetController {

    @Autowired
    private DatasetServices datasetServices;
    @Autowired
    private LabelServices labelsServices;
    @Autowired
    private ImageServices imageServices;
    @Autowired 
    private YoloV5Service yoloV5Impl;
    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Método para obtener los archivos del directorio de carga y generar el conjunto de datos.
     * @return ResponseEntity con la lista de nombres de archivos generados.
     */
    @PostMapping("path")
    public ResponseEntity<?> getFiles() {
        try {
            List<String> names = datasetServices.getFolder(uploadDir);
            List<Image> annotations= imageServices.getAllImages();
            // Crear un conjunto con los nombres de las anotaciones
            Set<String> annotationNames = annotations.stream()
                .map(Image::getName)
                .collect(Collectors.toSet());

            for(String n : names) {
                // Si el nombre no está en el conjunto de anotaciones, eliminar el archivo
                if (!annotationNames.contains(n)) {
                    Files.deleteIfExists(Paths.get(uploadDir, n));
                    names.remove(n);
                }
            }
            generateDataset(names);
            return ResponseEntity.ok(names);       
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }

    }

    /**
     * Genera el conjunto de datos a partir de los nombres de archivos proporcionados.
     * @param names Lista de nombres de archivos.
     */
    public void generateDataset(List<String> names) {
        List<String> name = new ArrayList<>();
        for (String n : names) {
            name.add(n);
        }
        datasetServices.generateDataset(name);
    }

    /**
     * Método para descargar todos los documentos del conjunto de datos en formato ZIP.
     * @return ResponseEntity con el archivo ZIP que contiene los documentos.
     * @throws IOException Si ocurre un error al leer los archivos o escribir en el archivo ZIP.
     */
    @GetMapping("/downloaddataset")
    public ResponseEntity<?> downloadAllDocuments() throws IOException {
        //String[] collections = { "train", "test", "validation" };

        ByteArrayOutputStream zipBytes = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(zipBytes)) {
            // Crear un mapa para almacenar las etiquetas
            Map<Integer, String> keys = new HashMap<>();
            List<YoloV5> yoloV5List = yoloV5Impl.getAllYoloV5List();
            List<String> labelaux = new ArrayList<>();
            // Iterar sobre las etiquetas y agregar las claves al mapa
            for (YoloV5 yoloV5 : yoloV5List) {
            for (ObjectDetectDto objectDetect : yoloV5.getObjectdetect()) {
                String label = objectDetect.getLabel();
                int id = objectDetect.getIdlabel();
                if (!labelaux.contains(label)) {
                keys.put(id, label);
                labelaux.add(label);
                }
            }
            }

            Map<String, Object> configYaml = datasetServices.generate_config_yaml(keys);
            writeYamlToZip(zipOut, configYaml, "config/config.yaml");

            // Obtener todas las etiquetas
            List<Labels> labelsList = labelsServices.getAllLabels();
            // Crear un mapa para almacenar el recuento de etiquetas
            Map<String, Integer> labelCounts = new HashMap<>();
            // Iterar sobre las etiquetas y agregar el recuento al mapa
            for (Labels label : labelsList) {
            String labelName = label.getLabel();
            if (labelName != null && !labelName.isEmpty()){
                labelCounts.put(label.getLabel(), label.getCant());
            } else {
                labelCounts.put(label.getSubcategoria(), label.getCant());
            }
            }
            // Convertir el mapa de recuento de etiquetas a formato JSON
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(labelCounts);
            String jsonconfig= mapper.writerWithDefaultPrettyPrinter().writeValueAsString(configYaml);
            writeStringToZip(zipOut, json, "config/detail_class.json");
            writeStringToZip(zipOut, jsonconfig, "config/config.json");

        //     // Iterar sobre las colecciones y agregar los documentos al ZIP
        //     for (String collectionName : collections) {
        //     // Obtener los documentos de la colección en formato DatasetRequestDto
        //     List<DatasetRequestDto> resultList = datasetServices.convertJsonToYoloV5(collectionName);
        //     String labelDirName = "labels/" + collectionName + "/";
        //     String imageDirName = "images/" + collectionName + "/";

        //     for (DatasetRequestDto docResult : resultList) {
        //         // Agregar el archivo de etiqueta al ZIP
        //         String labelFilename = labelDirName + docResult.getName().replaceAll("\\.(jpeg|png|jpg)$", ".txt");
        //         writeStringToZip(zipOut, docResult.getContent().toString(), labelFilename);

        //         // Agregar la imagen al ZIP si existe
        //         Path imagePath = Paths.get("src/main/resources/images", docResult.getName());
        //         if (Files.exists(imagePath)) {
        //         writeBytesToZip(zipOut, Files.readAllBytes(imagePath), imageDirName + docResult.getName());
        //         }
        //     }
        //     }
        }

        // // Crear un recurso de ByteArrayResource para el archivo ZIP
        ByteArrayResource resource = new ByteArrayResource(zipBytes.toByteArray());

        // Configurar las cabeceras de la respuesta HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=dataset.zip");

        // Devolver la respuesta HTTP con el archivo ZIP
        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(resource.contentLength())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
        }
        
        // Método para escribir un objeto YAML en el archivo ZIP
        private void writeYamlToZip(ZipOutputStream zipOut, Object data, String filename) throws IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(false);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        Yaml yaml = new Yaml(options);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yaml.dump(data, new OutputStreamWriter(baos, StandardCharsets.UTF_8));
        writeBytesToZip(zipOut, baos.toByteArray(), filename);
    }

    private void writeStringToZip(ZipOutputStream zipOut, String content, String filename) throws IOException {
        writeBytesToZip(zipOut, content.getBytes(StandardCharsets.UTF_8), filename);
    }

    private void writeBytesToZip(ZipOutputStream zipOut, byte[] bytes, String filename) throws IOException {
        zipOut.putNextEntry(new ZipEntry(filename));
        zipOut.write(bytes);
        zipOut.closeEntry();
    }

    /**
        * Obtiene el gráfico del conjunto de datos.
        * 
        * @param page el número de página a obtener (por defecto: 0) (cambiar este valor para cambiar de página)
        * @param size el tamaño de la página a obtener (por defecto: 10) 
        * (cambiar este valor para cambiar el tamaño de elementos por página)
        * @return ResponseEntity con el gráfico del conjunto de datos
        */
    @GetMapping("/graph")
    public ResponseEntity<?> getGraph(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(datasetServices.getGraph(page, size));
    }

}
