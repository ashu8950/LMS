package com.batch_service.contollers;



import com.batch_service.dto.BatchDTO;
import com.batch_service.service.impl.BatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batches-for-student")
public class StudentController {

    @Autowired
    private BatchService batchService;

    @GetMapping("/{id}")
    public ResponseEntity<BatchDTO> getBatchById(@PathVariable Long id) {
        BatchDTO batch = batchService.getBatchById(id);
        if (batch != null) {
            return ResponseEntity.ok(batch);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
