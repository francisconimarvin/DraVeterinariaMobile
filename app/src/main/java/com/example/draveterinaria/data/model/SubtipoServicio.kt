package com.example.draveterinaria.data.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table


@Entity
@Table(name = "SUBTIPO_SERVICIO")

class SubtipoServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subtipo_seq")
    @SequenceGenerator(name = "subtipo_seq", sequenceName = "SUBTIPO_SEQ", allocationSize = 1)
     var idSubtipo: Long? = null

    @Column(name = "NOMBRE_SUBTIPO")
    var nombreSubtipo: String? = null

    @Column(name = "PRECIO")
    var precio: Double? = null

    @ManyToOne
    @JoinColumn(name = "ID_TIPO_SERVICIO", nullable = false)
    @JsonBackReference
    var tipoServicio: TipoServicio? = null
}