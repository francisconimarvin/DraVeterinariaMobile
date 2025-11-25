package com.example.draveterinaria.data.model

import jakarta.persistence.*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@Entity
@Table(name = "ESPECIE")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")

data class Especie(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_especie")
    @SequenceGenerator(name = "seq_especie", sequenceName = "SEQ_ESPECIE", allocationSize = 1)
    @Column(name = "ID_ESPECIE")
    // ID es nullable al inicio, por eso Long?
    var idEspecie: Long? = null,

    @Column(name = "NOMBRE_ESPECIE", nullable = false, length = 255)
    var nombreEspecie: String
)