package vn.amit.common.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class WithTimestampEntity {
    @Column(updatable = false, columnDefinition = "timestamp null")
    @CreationTimestamp
    open var createdAt: LocalDateTime? = null

    @Column(columnDefinition = "timestamp null")
    @UpdateTimestamp
    open var updatedAt: LocalDateTime? = null
}