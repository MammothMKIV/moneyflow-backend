package io.moneyflow.server.mapper

import org.mapstruct.ObjectFactory
import org.mapstruct.TargetType
import org.springframework.lang.NonNull
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Component
class ReferenceMapper(
    @PersistenceContext
    val entityManager: EntityManager
) {
    @ObjectFactory
    fun <T> map(@NonNull id: Long?, @TargetType type: Class<T>): T? {
        return if (id == null) null else entityManager.getReference(type, id)
    }
}