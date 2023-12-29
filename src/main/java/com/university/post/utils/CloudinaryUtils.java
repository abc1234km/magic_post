package com.university.post.utils;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CloudinaryUtils {
    @Autowired
    private final Cloudinary cloudinary;

    public String upload(byte[] files)  {
        try{
            Map data = this.cloudinary.uploader().upload(files, Map.of());
            return (String) data.get("url");
        }catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }
}
