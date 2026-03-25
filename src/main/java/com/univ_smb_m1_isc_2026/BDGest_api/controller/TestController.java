package com.univ_smb_m1_isc_2026.BDGest_api.controller;

import com.univ_smb_m1_isc_2026.BDGest_api.model.Bd;
import com.univ_smb_m1_isc_2026.BDGest_api.repository.BdRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    private final BdRepository bdRepository;

    // Injection du repository via le constructeur
    public TestController(BdRepository bdRepository) {
        this.bdRepository = bdRepository;
    }

    @GetMapping("/")
    public String test() {
        return """
        <h1 style="color: green;">BDGest API fonctionne</h1>
        """;
    }

    // Nouveau endpoint pour tester la BDD
    @GetMapping("/test-bd")
    public List<Bd> testBd() {
        return bdRepository.findAll();
    }
}