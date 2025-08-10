package es.cic.curso25.proy015.controller;

import es.cic.curso25.proy015.enums.VehiculoTipo;
import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.repository.PlazaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PlazaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlazaRepository plazaRepo;

    @BeforeEach
    void setUp() {
        plazaRepo.deleteAll();
    }

    @Test
    void testCrearPlaza() throws Exception {
        mockMvc.perform(post("/plazas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "codigo": "P-001",
                                "tipo": "COCHE",
                                "activa": true
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo", is("P-001")))
                .andExpect(jsonPath("$.tipo", is("COCHE")))
                .andExpect(jsonPath("$.activa", is(true)));

        // Assert en BD
        List<Plaza> plazas = plazaRepo.findAll();
        assertThat(plazas).hasSize(1);
        assertThat(plazas.get(0).getCodigo()).isEqualTo("P-001");
        assertThat(plazas.get(0).getTipo()).isEqualTo(VehiculoTipo.COCHE);
        assertThat(plazas.get(0).isActiva()).isTrue();
    }

    @Test
    void testActualizarPlaza() throws Exception {
        // Crear plaza inicial
        Plaza plaza = new Plaza();
        plaza.setCodigo("P-002");
        plaza.setTipo(VehiculoTipo.MOTO);
        plaza.setActiva(true);
        Plaza guardada = plazaRepo.save(plaza);

        // Actualizar
        mockMvc.perform(put("/plazas/" + guardada.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "codigo": "P-002-UPDATED",
                                "tipo": "CARAVANA",
                                "activa": false
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo", is("P-002-UPDATED")))
                .andExpect(jsonPath("$.tipo", is("CARAVANA")))
                .andExpect(jsonPath("$.activa", is(false)));

        Plaza actualizada = plazaRepo.findById(guardada.getId()).orElseThrow();
        assertThat(actualizada.getCodigo()).isEqualTo("P-002-UPDATED");
        assertThat(actualizada.getTipo()).isEqualTo(VehiculoTipo.CARAVANA);
        assertThat(actualizada.isActiva()).isFalse();
    }

    @Test
    void testListarPlazas() throws Exception {
        Plaza p1 = new Plaza();
        p1.setCodigo("P-003");
        p1.setTipo(VehiculoTipo.COCHE);
        p1.setActiva(true);
        plazaRepo.save(p1);

        Plaza p2 = new Plaza();
        p2.setCodigo("P-004");
        p2.setTipo(VehiculoTipo.MOTO);
        p2.setActiva(true);
        plazaRepo.save(p2);

        mockMvc.perform(get("/plazas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo", is("P-003")))
                .andExpect(jsonPath("$[1].codigo", is("P-004")));

        assertThat(plazaRepo.count()).isEqualTo(2);
    }

    @Test
    void testObtenerPlaza() throws Exception {

        Plaza p = new Plaza();
        p.setCodigo("P-005");
        p.setTipo(VehiculoTipo.CARAVANA);
        p.setActiva(true);
        Plaza guardada = plazaRepo.save(p);

        mockMvc.perform(get("/plazas/" + guardada.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo", is("P-005")))
                .andExpect(jsonPath("$.tipo", is("CARAVANA")));

        
        Plaza enBD = plazaRepo.findById(guardada.getId()).orElseThrow();
        assertThat(enBD.getCodigo()).isEqualTo("P-005");
        assertThat(enBD.getTipo()).isEqualTo(VehiculoTipo.CARAVANA);
    }
}
