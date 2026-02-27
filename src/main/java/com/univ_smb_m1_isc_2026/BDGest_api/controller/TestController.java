package com.univ_smb_m1_isc_2026.BDGest_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test")
    public String test() {
        return "BDGest API fonctionne";
    }
}