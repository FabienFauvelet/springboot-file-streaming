package com.example.StreamingDemo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping ("/api")
public class Download {

    private final Logger logger = LoggerFactory.getLogger(Download.class);

    @Value("classpath:files/10MB.jpg")
    Resource resourceFile;

    @GetMapping (value = "/downloadwithstreaming", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StreamingResponseBody> downloadwithstreaming() {
        StreamingResponseBody stream = out -> {
            final String home = System.getProperty("user.home");
            final InputStream file= resourceFile.getInputStream();
            writeFileToStream(file,out);
        };
        return new ResponseEntity(stream, HttpStatus.OK);
    }

    @GetMapping (value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> download(final HttpServletResponse response) throws IOException {
        final String home = System.getProperty("user.home");
        final InputStream file = resourceFile.getInputStream();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeFileToStream(file,baos);
        return new ResponseEntity(baos.toByteArray(), HttpStatus.OK);
    }

    private void writeFileToStream(InputStream file,OutputStream outputStream){
        try {
            byte[] bytes=new byte[1024];
            int length;
            while ((length=file.read(bytes)) >= 0) {
                outputStream.write(bytes, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}