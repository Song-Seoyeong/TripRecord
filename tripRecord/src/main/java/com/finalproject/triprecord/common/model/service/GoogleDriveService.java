package com.finalproject.triprecord.common.model.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.finalproject.triprecord.common.model.vo.CustomResource;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

@Service
public class GoogleDriveService {
    private static final String APPLICATION_NAME = "TripRecord";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = Arrays.asList(
    		DriveScopes.DRIVE
    		);
    		

    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private Drive service;
    private Credential credential;

    public GoogleDriveService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GoogleDriveService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(6786).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
    
    // 주기적으로 엑세스 토큰을 갱신하는 메서드
    public void refreshTokenIfNeeded() throws IOException {
        // 만료된 토큰을 갱신
        if (credential.getExpiresInSeconds() != null && credential.getExpiresInSeconds() <= 60) {
            credential.refreshToken();
            System.out.println("Access token was refreshed.");
        } else {
            System.out.println("Access token is still valid.");
        }
    }

    // 토큰 갱신이나 재인증이 필요할 때 이 메서드를 호출
    public Drive getDriveService() throws IOException {
        refreshTokenIfNeeded();
        return service;
    }

    public String uploadFile(InputStream inputStream, String originalFileName) throws IOException {
        // Create file metadata
        File fileMetadata = new File();
        fileMetadata.setName(originalFileName);
        //https://drive.google.com/drive/folders/10BPVZYHdskiRresHwcdt44BNOuRCNzSq?usp=share_link
        fileMetadata.setParents(Collections.singletonList("10BPVZYHdskiRresHwcdt44BNOuRCNzSq"));
        // Create the file content with InputStream
        AbstractInputStreamContent mediaContent = new InputStreamContent("application/octet-stream", inputStream);

        // Upload the file
        File file = service.files().create(fileMetadata, mediaContent)
    		.setFields("id, name, parents")
            .execute();

        // Return the file ID
        return file.getId();
    }
    
    public String selectLogFile(String fileName) throws IOException {
    	
    	File logFile = findFileByName(fileName);
    	// Read the existing file content
        
        String existingContent;
        try (InputStream inputStream = service.files().get(logFile.getId()).executeMediaAsInputStream();
             InputStreamReader reader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                contentBuilder.append(line);
                contentBuilder.append(System.lineSeparator());
            }
            existingContent = contentBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error reading file content", e);
        }
        
        return existingContent;
    }
    
    public void logActivity(String logMessage) throws IOException {
        String fileName = "login.txt";
        String folderId = "1ZoaRQe3-tzYgU4GwbMFIloVT79r87157"; // Change this to your folder ID
        
        // Check if the file exists
        File existingFile = findFileByName(fileName);

        if (existingFile != null) {
            // File exists, update its content
            updateFileContent(existingFile.getId(), logMessage);
        } else {
            // File does not exist, create a new file
            createNewFile(fileName, logMessage, folderId);
        }
    }
    

    private File findFileByName(String fileName) throws IOException {
        String query = "name = '" + fileName + "' and mimeType = 'text/plain'";
        FileList result = service.files().list()
            .setQ(query)
            .setFields("files(id, name)")
            .execute();
        
        List<File> files = result.getFiles();
        return files.isEmpty() ? null : files.get(0);
    }

    private void updateFileContent(String fileId, String logMessage) throws IOException {
        // Read the existing file content
        File file = service.files().get(fileId).execute();
        
        String existingContent;
        try (InputStream inputStream = service.files().get(fileId).executeMediaAsInputStream();
             InputStreamReader reader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                contentBuilder.append(line);
                contentBuilder.append(System.lineSeparator());
            }
            existingContent = contentBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error reading file content", e);
        }
        
        // Append new log message
        String updatedContent = existingContent + "\n" + logMessage;
        // Update the file with the new content
        ByteArrayContent content = new ByteArrayContent("text/plain", updatedContent.getBytes());
        
        service.files().update(fileId, new File().setName(file.getName()), content).execute();
    }

    private void createNewFile(String fileName, String logMessage, String folderId) throws IOException {
        // Create file metadata
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(folderId));

        // Create file content
        ByteArrayContent mediaContent = new ByteArrayContent("text/plain", logMessage.getBytes());

        // Upload the file
        service.files().create(fileMetadata, mediaContent)
            .setFields("id, name, parents")
            .execute();
    }
    
    public void deleteFile(String fileId) throws IOException {
        try {
            service.files().delete(fileId).execute();
            System.out.println("File deleted: " + fileId);
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + e.getMessage());
            throw e;
        }
    }
    
    public CustomResource downloadImage(String fileId) throws IOException {
        File fileMetadata = service.files().get(fileId).execute();
        String fileName = fileMetadata.getName();
        System.out.println("File Name: " + fileName); // Add log
        // Fetch file content as byte array
        try (InputStream inputStream = service.files().get(fileId).executeMediaAsInputStream()) {
            byte[] fileBytes = convertStreamToByteArray(inputStream);
            System.out.println("File Size: " + fileBytes.length); // Add log

            String mimeType = service.files().get(fileId).execute().getMimeType();
            System.out.println("MIME Type: " + mimeType); // Log MIME type
            
            // Check if the fileBytes are empty
            if (fileBytes.length == 0) {
                throw new IOException("The file content is empty.");
            }

            return new CustomResource(fileBytes, fileName);
        }
    }

    private byte[] convertStreamToByteArray(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            byte[] data = new byte[1024];
            int nRead;
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        }
    }
    
}
