package es.cic.curso25.proy015.controller;

import es.cic.curso25.proy015.enums.EstadoVehiculo;
import es.cic.curso25.proy015.enums.VehiculoTipo;
import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.repository.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VehiculoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehiculoRepository vehiculoRepo;

    @BeforeEach
    void setUp() {
        vehiculoRepo.deleteAll();
    }

    @Test
    void crearVehiculo_devuelve201() throws Exception {
        mockMvc.perform(post("/vehiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "matricula": "ABC123",
                                "tipo": "COCHE"
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.matricula", is("ABC123")))
                .andExpect(jsonPath("$.tipo", is("COCHE")))
                .andExpect(jsonPath("$.estado", is("ALTA")));
    }

    @Test
    void crearVehiculo_matriculaDuplicada_devuelve409() throws Exception {
        // Insertamos un vehiculo igual previamente
        Vehiculo v = new Vehiculo();
        v.setMatricula("XYZ789");
        v.setTipo(VehiculoTipo.MOTO);
        v.setEstado(EstadoVehiculo.ALTA);
        vehiculoRepo.save(v);

        mockMvc.perform(post("/vehiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "matricula": "XYZ789",
                                "tipo": "MOTO"
                            }
                        """))
                .andExpect(status().isConflict())
                .andExpect(content().string("Ya existe un vehículo con la matrícula: XYZ789"));
    }

    @Test
    void obtenerVehiculo_inexistente_devuelve404() throws Exception {
        mockMvc.perform(get("/vehiculos/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Vehículo con ID 999 no encontrado."));
    }
}
