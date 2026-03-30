package com.univ_smb_m1_isc_2026.BDGest_api.controller;

import com.univ_smb_m1_isc_2026.BDGest_api.model.Bd;
import com.univ_smb_m1_isc_2026.BDGest_api.repository.BdRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BdControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BdRepository bdRepository;

    @InjectMocks
    private BdController controller;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldSearchBd() throws Exception {

        when(bdRepository.findBdByFilters("Tintin", null, null, null, 12, 0))
                .thenReturn(List.of());

        mockMvc.perform(get("/search")
                        .param("titre", "Tintin"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnRandomBd() throws Exception {

        when(bdRepository.findAll())
                .thenReturn(List.of(new Bd(), new Bd()));

        mockMvc.perform(get("/random-bd/1"))
                .andExpect(status().isOk());
    }


}