package com.example.draveterinaria.data.model


import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.Email

@Entity
@Table(name = "TUTOR")
// Usar data class da los 'getters', 'setters', constructores, equals/hashCode, etc.
data class Tutor(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tutor_seq")
    @SequenceGenerator(name = "tutor_seq", sequenceName = "ID_TUTOR_SEQ", allocationSize = 1)
    @Column(name = "ID_TUTOR")
    // ID es nullable al inicio
    var idTutor: Long? = null,

    @Column(name = "RUN_TUTOR", length = 8)
    var runTutor: String,

    @Column(name = "DV_RUN", length = 1)
    var dvRun: String,

    @Column(name = "NOMBRE_TUTOR", length = 20)
    var nombreTutor: String,

    @Column(name = "SNOMBRE_TUTOR", length = 20)
    var sNombreTutor: String? = null, // Asumimos que el segundo nombre puede ser nulo

    @Column(name = "APPATERNO_TUTOR", length = 20)
    var apPaternoTutor: String,

    @Column(name = "AMATERNO_TUTOR", length = 20)
    var aMaternoTutor: String,

    @Column(name = "TLF", length = 14)
    var telefono: String? = null, // Asumimos que el teléfono puede ser nulo

    @Column(name = "DIRECCION", length = 50)
    var direccion: String? = null, // Asumimos que la dirección puede ser nula

    @Email // Anotación de validación de Jakarta
    @Column(name = "EMAIL", length = 50)
    var email: String,

    @OneToMany(mappedBy = "tutor", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Lado "manager" en la serialización JSON (evita bucles)
    var mascotas: List<Mascota> = emptyList()
)