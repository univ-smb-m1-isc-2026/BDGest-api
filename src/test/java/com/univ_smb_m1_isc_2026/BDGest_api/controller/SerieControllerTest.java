package com.univ_smb_m1_isc_2026.BDGest_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.univ_smb_m1_isc_2026.BDGest_api.model.Serie;
import com.univ_smb_m1_isc_2026.BDGest_api.repository.SerieRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SerieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SerieRepository serieRepository;

    @InjectMocks
    private SerieController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // =========================
    // 🧪 GET /list-serie
    // =========================
    @Test
    void shouldReturnAllSeries() throws Exception {

        Serie s1 = new Serie();
        s1.setId(1L);
        s1.setNom("Naruto");

        Serie s2 = new Serie();
        s2.setId(2L);
        s2.setNom("One Piece");

        when(serieRepository.findAll()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/list-serie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.series[0].nom").value("Naruto"));
    }

    // =========================
    // 🧪 GET /list-serie/search
    // =========================
    @Test
    void shouldSearchSerieByName() throws Exception {

        Serie s = new Serie();
        s.setId(1L);
        s.setNom("Attack on Titan");

        when(serieRepository.findByNomContainingIgnoreCase("attack"))
                .thenReturn(List.of(s));

        mockMvc.perform(get("/list-serie/search")
                        .param("nom", "attack"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.series[0].nom").value("Attack on Titan"));
    }

    // =========================
    // 🧪 POST /list-serie
    // =========================
    @Test
    void shouldAddSerie() throws Exception {

        Serie input = new Serie();
        input.setNom("Bleach");

        Serie saved = new Serie();
        saved.setId(10L);
        saved.setNom("Bleach");

        when(serieRepository.save(org.mockito.ArgumentMatchers.any(Serie.class)))
                .thenReturn(saved);

        mockMvc.perform(post("/list-serie")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.serieId").value(10))
                .andExpect(jsonPath("$.serie.nom").value("Bleach"));
    }

    // =========================
    // 🧪 cas vide (bonus)
    // =========================
    @Test
    void shouldReturnEmptyList() throws Exception {

        when(serieRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/list-serie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(0));
    }
}