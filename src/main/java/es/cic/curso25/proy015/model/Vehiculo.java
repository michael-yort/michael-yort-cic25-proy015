package es.cic.curso25.proy015.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.cic.curso25.proy015.enums.EstadoVehiculo;
import es.cic.curso25.proy015.enums.VehiculoTipo;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "vehiculo", uniqueConstraints = @UniqueConstraint(name = "uk_vehiculo_matricula", columnNames = "matricula"))
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 15)
    private String matricula;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VehiculoTipo tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 5)
    private EstadoVehiculo estado;

    @Column(nullable = false)
    private LocalDateTime fechaAlta = LocalDateTime.now();

    private LocalDateTime fechaBaja;

    @OneToMany(mappedBy = "vehiculo", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<AsignacionPlaza> asignaciones;

    @OneToMany(mappedBy = "vehiculo", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Estancia> estancias;

    @OneToMany(mappedBy = "vehiculo", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Multa> multas;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public VehiculoTipo getTipo() {
        return tipo;
    }

    public void setTipo(VehiculoTipo tipo) {
        this.tipo = tipo;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDateTime getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDateTime fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Set<AsignacionPlaza> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(Set<AsignacionPlaza> asignaciones) {
        this.asignaciones = asignaciones;
    }

    public Set<Estancia> getEstancias() {
        return estancias;
    }

    public void setEstancias(Set<Estancia> estancias) {
        this.estancias = estancias;
    }

    public Set<Multa> getMultas() {
        return multas;
    }

    public void setMultas(Set<Multa> multas) {
        this.multas = multas;
    }
}
