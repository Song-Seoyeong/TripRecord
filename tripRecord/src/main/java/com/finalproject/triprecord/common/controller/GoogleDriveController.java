package com.finalproject.triprecord.common.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finalproject.triprecord.common.model.service.GoogleDriveService;
import com.finalproject.triprecord.common.model.vo.CustomResource;

@RestController
public class GoogleDriveController {

    @Autowired
    private GoogleDriveService googleDriveService;

    @GetMapping("/downloadImage")
    public ResponseEntity<Resource> downloadImage(@RequestParam(name = "fileId") String fileId) {
        try {
            CustomResource image = googleDriveService.downloadImage(fileId);

            if (image == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            String filename = image.getFileName();
            //System.out.println(filename);
            String contentType = "image/jpeg"; // Default content type; adjust if needed

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(image);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}