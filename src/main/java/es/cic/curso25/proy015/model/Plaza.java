package es.cic.curso25.proy015.model;

import jakarta.persistence.*;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.cic.curso25.proy015.enums.VehiculoTipo;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "plaza", uniqueConstraints = @UniqueConstraint(name = "uk_plaza_codigo", columnNames = "codigo"))
public class Plaza {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 20)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VehiculoTipo tipo;

    @Column(nullable = false)
    private boolean activa = true;

    @OneToMany(mappedBy = "plaza", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<AsignacionPlaza> asignaciones;

    @OneToMany(mappedBy = "plaza", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Estancia> estancias;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public VehiculoTipo getTipo() {
        return tipo;
    }

    public void setTipo(VehiculoTipo tipo) {
        this.tipo = tipo;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
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
}
