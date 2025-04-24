package com.example.carcomparison.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.Instant

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditedEntity : Serializable {

    @Column(nullable = false, updatable = false, columnDefinition = "timestamp with time zone")
    @CreatedDate
    var createdAt: Instant = Instant.now()

    @Column(nullable = false, columnDefinition = "timestamp with time zone")
    @LastModifiedDate
    var updatedAt: Instant = Instant.now()
}