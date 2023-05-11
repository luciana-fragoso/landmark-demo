package com.demo.landmark.controller;

import com.demo.landmark.dto.FileDTO;
import com.demo.landmark.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/readFile")
    public ResponseEntity readFile(@RequestBody FileDTO fileDTO) {


        fileService.readFileInfo(fileDTO);
        return ResponseEntity.ok().build();

    }
}
