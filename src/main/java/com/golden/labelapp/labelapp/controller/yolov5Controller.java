package com.golden.labelapp.labelapp.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.golden.labelapp.labelapp.dao.LabelServicesImpl;
import com.golden.labelapp.labelapp.dto.DatasetRequest;
import com.golden.labelapp.labelapp.dto.Labels;
import com.golden.labelapp.labelapp.dto.YoloV5;
import com.golden.labelapp.labelapp.services.DatasetServices;
import com.golden.labelapp.labelapp.services.YoloV5Service;

@RestController
@RequestMapping("/yolov5")
public class yolov5Controller {

    @Autowired
    private YoloV5Service yoloV5Impl;

    @Autowired
    private DatasetServices datasetServices;

    @Autowired
    private LabelServicesImpl labelServicesImpl;

    @Transactional(readOnly = true)
    @GetMapping("/all")
    public List<YoloV5> getAllYolov5() {
        return (List<YoloV5>) yoloV5Impl.getAllYoloV5();
    }

    @Transactional(readOnly = true)
    @GetMapping("/downloaddataset")
    public ResponseEntity<?> downloadAllDocuments() throws IOException {
        String[] collections = { "train", "test", "validation" };

        ByteArrayOutputStream zipBytes = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(zipBytes)) {
            // Generar y escribir el archivo config.yaml
            List<String> keys = labelServicesImpl.getAllLabels().stream()
                    .map(Labels::getLabel)
                    .collect(Collectors.toList());
            List<String> names = datasetServices.getFolder("src/main/resources/images");
            Map<String, Object> configYaml = datasetServices.generate_config_yaml(names, keys);
            writeYamlToZip(zipOut, configYaml, "config/config.yaml");

            // Iterar sobre las colecciones y agregar los documentos al ZIP
            for (String collectionName : collections) {
                List<DatasetRequest> resultList = datasetServices.convertJsonToYoloV5(collectionName);
                String labelDirName = "labels_me/" + collectionName + "/";
                String imageDirName = "images/" + collectionName + "/";

                for (DatasetRequest docResult : resultList) {
                    // Agregar el archivo de etiqueta al ZIP
                    String labelFilename = labelDirName + docResult.getName().replaceAll("\\.(jpeg|png|jpg)$", ".txt");
                    writeStringToZip(zipOut, docResult.getContent().toString(), labelFilename);

                    // Agregar la imagen al ZIP si existe
                    Path imagePath = Paths.get("src/main/resources/images", docResult.getName());
                    if (Files.exists(imagePath)) {
                        writeBytesToZip(zipOut, Files.readAllBytes(imagePath), imageDirName + docResult.getName());
                    }
                }
            }
        }

        ByteArrayResource resource = new ByteArrayResource(zipBytes.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=dataset.zip");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    private void writeYamlToZip(ZipOutputStream zipOut, Object data, String filename) throws IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
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
}
