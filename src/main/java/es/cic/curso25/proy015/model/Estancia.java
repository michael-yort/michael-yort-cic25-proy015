package es.cic.curso25.proy015.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "estancia", indexes = {
        @Index(name = "idx_estancia_plaza", columnList = "plaza_id"),
        @Index(name = "idx_estancia_vehiculo", columnList = "vehiculo_id"),
        @Index(name = "idx_estancia_salida", columnList = "salida_ts")
})
public class Estancia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "plaza_id", nullable = false)
    private Plaza plaza;

    @Column(name = "entrada_ts", nullable = false)
    private LocalDateTime entradaTs = LocalDateTime.now();

    @Column(name = "salida_ts")
    private LocalDateTime salidaTs;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal importeEstancia = BigDecimal.ZERO;

    @OneToMany(mappedBy = "estancia", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Multa> multas;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Plaza getPlaza() {
        return plaza;
    }

    public void setPlaza(Plaza plaza) {
        this.plaza = plaza;
    }

    public LocalDateTime getEntradaTs() {
        return entradaTs;
    }

    public void setEntradaTs(LocalDateTime entradaTs) {
        this.entradaTs = entradaTs;
    }

    public LocalDateTime getSalidaTs() {
        return salidaTs;
    }

    public void setSalidaTs(LocalDateTime salidaTs) {
        this.salidaTs = salidaTs;
    }

    public BigDecimal getImporteEstancia() {
        return importeEstancia;
    }

    public void setImporteEstancia(BigDecimal importeEstancia) {
        this.importeEstancia = importeEstancia;
    }

    public Set<Multa> getMultas() {
        return multas;
    }

    public void setMultas(Set<Multa> multas) {
        this.multas = multas;
    }
}
