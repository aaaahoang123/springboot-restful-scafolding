package vn.amit.common.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class WithTimestampEntity {
    @Column(updatable = false, columnDefinition = "datetime")
    @CreationTimestamp
    open var createdDate: LocalDateTime? = null

    @Column(columnDefinition = "datetime")
    @UpdateTimestamp
    open var updatedDate: LocalDateTime? = null
}