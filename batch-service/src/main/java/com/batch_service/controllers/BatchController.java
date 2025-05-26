package com.batch_service.controllers;


import org.springframework.web.bind.annotation.*;

import com.batch_service.service.impl.BatchService;

@RestController
@RequestMapping("/batches")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @PostMapping
    public String createBatch(@RequestBody String batchData) {
        batchService.processBatch(batchData);
        return "Batch created and message sent";
    }
}
