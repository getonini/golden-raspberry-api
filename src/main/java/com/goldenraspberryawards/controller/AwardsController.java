package com.goldenraspberryawards.controller;

import com.goldenraspberryawards.dto.AwardIntervalResponse;
import com.goldenraspberryawards.service.AwardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/awards")
public class AwardsController {
    @Autowired
    private AwardsService service;

    @GetMapping("/intervals")
    public ResponseEntity<AwardIntervalResponse> getAwardIntervals() {
        return ResponseEntity.ok(service.calculateAwardIntervals());
    }
}
