package com.univ_smb_m1_isc_2026.BDGest_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/")
    public String test() {
        return """
        <h1 style="color: green;">BDGest API fonctionne</h1>
        """;
    }
}