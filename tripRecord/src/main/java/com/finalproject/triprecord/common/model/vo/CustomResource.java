package com.finalproject.triprecord.common.model.vo;

import org.springframework.core.io.ByteArrayResource;

public class CustomResource extends ByteArrayResource  {

	private final String FILENAME;

    public CustomResource(byte[] byteArray, String fileName) {
        super(byteArray);
        this.FILENAME = fileName;
    }

    public String getFileName() {
        return FILENAME;
    }
}