package com.fangjun.yuaiagent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fangjun
 * @modify 2025-05-07 23:50:41
 * @motto Talk is cheap, show me the code!
 * @description <h1> </h1>
 */
@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping
    public String health() {
        return "ok";
    }
}
