package com.example.draveterinaria.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime


@Entity
@Table(name = "SERVICIO")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Servicio(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_seq")
    @SequenceGenerator(name = "service_seq", sequenceName = "seq_servicio", allocationSize = 1)
    @Column(name = "ID_SERVICIO")
    var idServicio: Long? = null,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_MASCOTA", nullable = false)
    var mascota: Mascota,

    @Column(name = "FECHA")
    var fecha: LocalDateTime,

    @Column(name = "COSTO", precision = 10, scale = 2, nullable = false)
    var costo: BigDecimal,

    @Column(name = "ID_AGENDA")
    var idAgenda: Long? = null,


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_SUBTIPO", nullable = false)
    var subtipo: SubtipoServicio,

    @Column(name = "ID_FACTURA")
    var idFactura: Long? = null,

    @Column(name = "ESTADO_PAGO", length = 20)
    var estadoPago: String = "PENDIENTE"
)