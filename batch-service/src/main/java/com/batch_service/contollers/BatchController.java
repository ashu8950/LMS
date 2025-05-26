package com.batch_service.contollers;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.batch_service.dto.BatchDTO;
import com.batch_service.service.impl.BatchService;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @PostMapping
    public ResponseEntity<BatchDTO> createBatch(@RequestBody BatchDTO batchDTO) {
        BatchDTO createdBatch = batchService.createBatch(batchDTO);
        return ResponseEntity.ok(createdBatch);
    }

    @GetMapping
    public ResponseEntity<List<BatchDTO>> getAllBatches() {
        return ResponseEntity.ok(batchService.getAllBatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchDTO> getBatchById(@PathVariable Long id) {
        return ResponseEntity.ok(batchService.getBatchById(id));
    }
}

