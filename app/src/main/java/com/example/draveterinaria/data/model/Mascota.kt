package com.example.draveterinaria.data.model


import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "MASCOTA")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Mascota(
    // 1. Clave Primaria
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mascota_seq")
    @SequenceGenerator(name = "mascota_seq", sequenceName = "ID_MASC_SEQ", allocationSize = 1)
    @Column(name = "ID_MASCOTA")
    var idMascota: Long? = null, // Usamos 'var' para que sea mutable (setter) y 'Long?' para que sea nullable (permite null antes de ser guardado/generado)

    // 2. Propiedades Simples
    @Column(name = "NOMBRE_MASCOTA", nullable = false, length = 255)
    var nombreMascota: String,

    @Column(name = "FECHA_NACIMIENTO")
    var fechaNacimiento: LocalDate? = null, // Puede ser nullable si no es obligatorio

    @Column(name = "SEXO", nullable = false, length = 1)
    var sexo: String,

    // 3. Relación con Tutor
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TUTOR", nullable = false)
    @JsonBackReference
    var tutor: Tutor, // Asume que la clase Tutor está definida

    // 4. Relación con Especie
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ESPECIE", nullable = false)
    var especie: Especie // Asume que la clase Especie está definida
) {

}