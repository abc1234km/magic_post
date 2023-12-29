package com.university.post.service.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.university.post.service.CloudinaryService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService{
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
