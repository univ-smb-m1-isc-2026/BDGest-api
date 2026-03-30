package com.univ_smb_m1_isc_2026.BDGest_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.univ_smb_m1_isc_2026.BDGest_api.dto.LoginRequest;
import com.univ_smb_m1_isc_2026.BDGest_api.dto.RegisterRequest;
import com.univ_smb_m1_isc_2026.BDGest_api.model.Utilisateur;
import com.univ_smb_m1_isc_2026.BDGest_api.repository.*;
import com.univ_smb_m1_isc_2026.BDGest_api.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UtilisateurControllerTest {

    private MockMvc mockMvc;

    @Mock private UtilisateurRepository utilisateurRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtils jwtUtils;
    @Mock private BdRepository bdRepository;
    @Mock private SerieRepository serieRepository;
    @Mock private AuteurRepository auteurRepository;
    @Mock private PretRepository pretRepository;

    @InjectMocks
    private UtilisateurController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // =========================
    // 🧪 REGISTER
    // =========================
    @Test
    void shouldRegisterUser() throws Exception {

        RegisterRequest request = new RegisterRequest();
        request.setMail("test@mail.com");
        request.setMdp("1234");

        when(utilisateurRepository.findByMail("test@mail.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("1234"))
                .thenReturn("hashed");

        mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // =========================
    // 🧪 LOGIN OK
    // =========================
    @Test
    void shouldLoginSuccess() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setMail("test@mail.com");
        request.setMdp("1234");

        Utilisateur user = new Utilisateur();
        user.setMail("test@mail.com");
        user.setMdp("hashed");
        user.setId(1L);

        when(utilisateurRepository.findByMail("test@mail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("1234", "hashed"))
                .thenReturn(true);

        when(jwtUtils.generateJwtToken("test@mail.com"))
                .thenReturn("token123");

        mockMvc.perform(post("/users/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.token").value("token123"));
    }

    // =========================
    // 🧪 LOGIN FAIL
    // =========================
    @Test
    void shouldFailLoginWrongPassword() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setMail("test@mail.com");
        request.setMdp("wrong");

        Utilisateur user = new Utilisateur();
        user.setMail("test@mail.com");
        user.setMdp("hashed");

        when(utilisateurRepository.findByMail("test@mail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong", "hashed"))
                .thenReturn(false);

        mockMvc.perform(post("/users/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    // =========================
    // 🧪 GET /users/me
    // =========================
    @Test
    void shouldReturnCurrentUser() throws Exception {

        when(jwtUtils.getUsernameFromJwt("token123"))
                .thenReturn("test@mail.com");

        when(jwtUtils.validateJwtToken("token123"))
                .thenReturn(true);

        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer token123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.valid").value(true));
    }

    // =========================
    // 🧪 DELETE /users/me
    // =========================
    @Test
    void shouldDeleteUser() throws Exception {

        Utilisateur user = new Utilisateur();
        user.setMail("test@mail.com");

        when(jwtUtils.getUsernameFromJwt("token123"))
                .thenReturn("test@mail.com");

        when(utilisateurRepository.findByMail("test@mail.com"))
                .thenReturn(Optional.of(user));

        mockMvc.perform(delete("/users/me")
                        .header("Authorization", "Bearer token123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(utilisateurRepository).delete(user);
    }
}