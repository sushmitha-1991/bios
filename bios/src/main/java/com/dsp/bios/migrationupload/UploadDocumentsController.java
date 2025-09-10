package com.dsp.bios.migrationupload;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dsp1")
public class UploadDocumentsController {

    @Autowired
    private UploadService service;

    @GetMapping("/migration/{moduleName}")
    public ResponseEntity<List<ResponseUuids>> uploadToDsp2(@PathVariable String moduleName,@RequestParam String locale) {
        System.out.println("Received in Controller: ");
        List<ResponseUuids> uuidString = service.uploadToDsp2(moduleName,locale);
        return ResponseEntity.ok(uuidString);
    }


}