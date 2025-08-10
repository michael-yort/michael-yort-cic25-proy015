package es.cic.curso25.proy015.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.cic.curso25.proy015.enums.EstadoVehiculo;
import es.cic.curso25.proy015.enums.VehiculoTipo;
import es.cic.curso25.proy015.model.Estancia;
import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.repository.AsignacionPlazaRepository;
import es.cic.curso25.proy015.repository.EstanciaRepository;
import es.cic.curso25.proy015.repository.MultaRepository;
import es.cic.curso25.proy015.repository.PlazaRepository;
import es.cic.curso25.proy015.repository.VehiculoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
class EstanciaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VehiculoRepository vehiculoRepo;

    @Autowired
    private PlazaRepository plazaRepo;

    @Autowired
    private EstanciaRepository estanciaRepo;

    @Autowired
    private AsignacionPlazaRepository asignacionRepo;

    @Autowired
    private MultaRepository multaRepo;

    private Vehiculo vehiculo;
    private Plaza plaza;

    @BeforeEach
    void setUp() {

        multaRepo.deleteAll(); // hijos
        estanciaRepo.deleteAll(); // si también referencia a vehiculo/plaza
        asignacionRepo.deleteAll(); // si aplica
        vehiculoRepo.deleteAll(); // padres
        plazaRepo.deleteAll(); // padres

        vehiculo = new Vehiculo();
        vehiculo.setMatricula("ABC123");
        vehiculo.setTipo(VehiculoTipo.COCHE);
        vehiculo.setEstado(EstadoVehiculo.ALTA);
        vehiculoRepo.save(vehiculo);

        plaza = new Plaza();
        plaza.setCodigo("P-001");
        plaza.setTipo(VehiculoTipo.COCHE);
        plaza.setActiva(true);
        plazaRepo.save(plaza);
    }

    @Test
    void crearEstancia_OK() throws Exception {
        Estancia estancia = new Estancia();
        estancia.setVehiculo(vehiculo);
        estancia.setPlaza(plaza);

        mockMvc.perform(post("/estancias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estancia)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        // Verifica que se guardó en base de datos
        assert estanciaRepo.count() == 1;
    }

    @Test
    void crearEstancia_VehiculoConEstanciaAbierta_Conflict() throws Exception {
        // Estancia previa ABIERTA para el mismo vehículo
        Estancia e1 = new Estancia();
        e1.setVehiculo(vehiculo); // vehiculo ya guardado en @BeforeEach y con estado ALTA
        e1.setPlaza(plaza); // plaza ya guardada y activa
        e1.setEntradaTs(LocalDateTime.now()); // ← IMPORTANTE si entradaTs es NOT NULL
        e1.setSalidaTs(null); // ← abierta
        estanciaRepo.save(e1);

        // Intento de crear otra estancia para el mismo vehículo
        Estancia e2 = new Estancia();
        e2.setVehiculo(vehiculo);
        e2.setPlaza(plaza);

        mockMvc.perform(post("/estancias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(e2)))
                .andExpect(status().isConflict())// Me tiene que devolver error 409 que es que ya hay uno
                .andExpect(content().string("El vehículo ya tiene una estancia abierta"));
    }

    @Test
    void cerrarEstancia_OK() throws Exception {

        Estancia e1 = new Estancia();
        e1.setVehiculo(vehiculo); 
        e1.setPlaza(plaza); 
        e1.setEntradaTs(LocalDateTime.now()); 
        e1.setSalidaTs(null); 

        mockMvc.perform(put("/estancias/" + e1.getId() + "/salida"))
                .andExpect(status().isOk());

        assert estanciaRepo.findById(e1.getId()).get().getSalidaTs() != null;
    }

    @Test
    void cerrarEstancia_DosVeces_Conflict() throws Exception {
        Estancia e1 = new Estancia();
        e1.setVehiculo(vehiculo);
        e1.setPlaza(plaza);
        e1.setSalidaTs(java.time.LocalDateTime.now());
        estanciaRepo.save(e1);

        mockMvc.perform(put("/estancias/" + e1.getId() + "/salida"))
                .andExpect(status().isConflict());
    }
}
