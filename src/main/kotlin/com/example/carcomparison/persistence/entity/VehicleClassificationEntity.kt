package com.example.carcomparison.persistence.entity

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.UUID
import com.vladmihalcea.hibernate.type.json.JsonType
import org.springframework.data.jpa.domain.support.AuditingEntityListener


@Entity
@Table(name = "vehicle_classification")
@EntityListeners(AuditingEntityListener::class)
data class VehicleClassificationEntity(

    @Id
    @GeneratedValue(generator = "uuid2") // Use the correct UUID generator
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    var id: UUID? = null,  // Use UUID instead of String for better compatibility

    @Column(nullable = false)
    var vehicleListingId: String,

    @Column(nullable = false)
    var isCategorised: Boolean = false,

    @Type(JsonType::class)
    @Column(columnDefinition = "jsonb") // Ensure your DB supports JSONB (PostgreSQL)
    var categorizedResult: String,

    ) : AuditedEntity()
