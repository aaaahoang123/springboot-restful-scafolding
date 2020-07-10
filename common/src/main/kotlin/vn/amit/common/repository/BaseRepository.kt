package vn.amit.common.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface BaseRepository<EntityType, IDType> : JpaRepository<EntityType, IDType>, JpaSpecificationExecutor<EntityType> {
    fun findByIdIsIn(ids: List<IDType>): List<EntityType>
}