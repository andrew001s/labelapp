package com.golden.labelapp.labelapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golden.labelapp.labelapp.dto.DatasetRequest;
import com.golden.labelapp.labelapp.dto.YoloV5;
import com.golden.labelapp.labelapp.services.DatasetServices;
import com.golden.labelapp.labelapp.services.YoloV5Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/yolov5")
public class yolov5Controller {

    @Autowired
    private YoloV5Service yoloV5Impl;

    @Autowired
    private DatasetServices datasetServices;

    @Transactional(readOnly = true)
    @GetMapping("/all")
    public List<YoloV5> getAllYolov5() {
        return (List<YoloV5>) yoloV5Impl.getAllYoloV5();
    }

    @Transactional(readOnly = true)
     @GetMapping("/downloaddataset")
    public ResponseEntity<?> downloadAllDocuments() throws IOException {
        String[] collections = {"train", "test", "validation"};
        ByteArrayOutputStream zipGeneralBaos = new ByteArrayOutputStream();
        ZipOutputStream zipGeneralOut = new ZipOutputStream(zipGeneralBaos);

        for (String collectionName : collections) {
            List<DatasetRequest> resultList = datasetServices.convertJsonToYoloV5(collectionName);

            String dirName = "labels_me/"+collectionName + "/";
            zipGeneralOut.putNextEntry(new ZipEntry(dirName));
            zipGeneralOut.closeEntry();

            for (DatasetRequest docResult : resultList) {
                String filename = dirName + docResult.getName().replaceAll("\\.(jpeg|png|jpg)$", ".txt");
                zipGeneralOut.putNextEntry(new ZipEntry(filename));
                zipGeneralOut.write(docResult.getContent().toString().getBytes());
                zipGeneralOut.closeEntry();
            }
        }

        zipGeneralOut.close();
        ByteArrayResource resource = new ByteArrayResource(zipGeneralBaos.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=dataset.zip");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
   
    

