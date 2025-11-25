package com.example.draveterinaria.data.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "TIPO_SERVICIO")

class TipoServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_serv_seq")
    @SequenceGenerator(name = "tipo_serv_seq", sequenceName = "seq_tiposervicio", allocationSize = 1)
    @Column(name = "ID_TIPO_SERVICIO")
    private var idTipo: Long? = null

    @Column(name = "TIPO_SERVICIO", length = 100, nullable = false)
    private var tipoServicio: String? = null

    @OneToMany(mappedBy = "tipoServicio", cascade = [CascadeType.ALL])
    @JsonManagedReference
    private val subtipos: MutableList<SubtipoServicio?>? = null
}