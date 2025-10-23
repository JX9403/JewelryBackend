package com.ndn.JewelryBackend.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Helper {
    public static HttpRequest.BodyPublisher buildMultipartBody(MultipartFile file) throws IOException {
        String boundary = "---JavaBoundary";
        var byteArrays = new ArrayList<byte[]>();

        String filePartHeader = "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getOriginalFilename() + "\"\r\n" +
                "Content-Type: " + file.getContentType() + "\r\n\r\n";
        byteArrays.add(filePartHeader.getBytes(StandardCharsets.UTF_8));
        byteArrays.add(file.getBytes());
        byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));

        String end = "--" + boundary + "--";
        byteArrays.add(end.getBytes(StandardCharsets.UTF_8));

        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }
}
