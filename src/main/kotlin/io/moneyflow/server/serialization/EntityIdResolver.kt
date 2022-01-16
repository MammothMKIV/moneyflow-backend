package io.moneyflow.server.serialization

import com.fasterxml.jackson.annotation.ObjectIdGenerator
import com.fasterxml.jackson.annotation.ObjectIdResolver
import javax.persistence.EntityManager


class EntityIdResolver(
    private val entityManager: EntityManager
) : ObjectIdResolver {
    override fun bindItem(id: ObjectIdGenerator.IdKey?, pojo: Any?) {

    }

    override fun resolveId(id: ObjectIdGenerator.IdKey?): Any {
        return entityManager.find(id?.scope, id?.key)
    }

    override fun newForDeserialization(context: Any?): ObjectIdResolver {
        return this
    }

    override fun canUseFor(resolverType: ObjectIdResolver?): Boolean {
        return false
    }
}