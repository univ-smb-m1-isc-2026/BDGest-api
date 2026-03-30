package com.univ_smb_m1_isc_2026.BDGest_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.univ_smb_m1_isc_2026.BDGest_api.model.Auteur;
import com.univ_smb_m1_isc_2026.BDGest_api.repository.AuteurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuteurControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuteurRepository auteurRepository;

    @InjectMocks
    private AuteurController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // =========================
    // 🧪 GET /list-auteur
    // =========================
    @Test
    void shouldReturnAllAuteurs() throws Exception {

        Auteur a1 = new Auteur();
        a1.setId(1L);
        a1.setNom("Goscinny");

        Auteur a2 = new Auteur();
        a2.setId(2L);
        a2.setNom("Hergé");

        when(auteurRepository.findAll()).thenReturn(List.of(a1, a2));

        mockMvc.perform(get("/list-auteur"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.auteurs[0].nom").value("Goscinny"));
    }

    // =========================
    // 🧪 GET /list-auteur/search
    // =========================
    @Test
    void shouldSearchAuteurByName() throws Exception {

        Auteur a = new Auteur();
        a.setId(1L);
        a.setNom("Isayama");

        when(auteurRepository.findByNomContainingIgnoreCase("isa"))
                .thenReturn(List.of(a));

        mockMvc.perform(get("/list-auteur/search")
                        .param("nom", "isa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.auteurs[0].nom").value("Isayama"));
    }

    // =========================
    // 🧪 POST /list-auteur
    // =========================
    @Test
    void shouldAddAuteur() throws Exception {

        Auteur input = new Auteur();
        input.setNom("Oda");

        Auteur saved = new Auteur();
        saved.setId(10L);
        saved.setNom("Oda");

        when(auteurRepository.save(org.mockito.ArgumentMatchers.any(Auteur.class)))
                .thenReturn(saved);

        mockMvc.perform(post("/list-auteur")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.auteurId").value(10))
                .andExpect(jsonPath("$.auteur.nom").value("Oda"));
    }
}