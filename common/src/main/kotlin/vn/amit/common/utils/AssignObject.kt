package vn.amit.common.utils

import org.hibernate.Hibernate
import vn.amit.common.annotation.DateTimeFromMs
import vn.amit.common.annotation.FormattedDateTime
import vn.amit.common.datetime.ZONE_OFFSET
import vn.amit.common.datetime.formatDateTime
import vn.amit.common.datetime.getDateTimeFromMs
import vn.amit.common.interfaces.AssignableObject
import java.time.LocalDateTime
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.*
import kotlin.reflect.jvm.*

fun <O : Any, S : Any> assignObject(output: O, source: S, ignoreField: List<String> = listOf()): O {
//    val mapValue = objectToMap(source, ignoreField)
    val sourcePropsMap = getSourcePropsMap(source)
    output::class.memberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .forEach { prop ->
                val sourceProp = sourcePropsMap[prop.name]
                if (sourceProp != null) {
                    sourceProp.isAccessible = true
                }
                val sourceValue = sourceProp?.getter?.call(source)
                if (!ignoreField.contains(prop.name) && sourceValue != null) {
                    prop.isAccessible = true
                    if (prop.returnType.withNullability(false) == sourceProp.returnType.withNullability(false)) {
                        prop.setter.call(output, sourceValue)
                    } else {
                        if (
                                prop.findAnnotation<DateTimeFromMs>() != null
                                && sourceProp.returnType.withNullability(false).javaType == java.lang.Long::class.java
                                && prop.returnType.withNullability(false).javaType == LocalDateTime::class.java
                        ) {
                            prop.setter.call(output, getDateTimeFromMs(sourceValue as Long))
                        } else if (
                                prop.findAnnotation<FormattedDateTime>() != null
                                && sourceProp.returnType.withNullability(false).javaType == LocalDateTime::class.java
                        ) {
                            val anno = prop.findAnnotation<FormattedDateTime>()
                            if (anno!!.toMs) {
                                prop.setter.call(
                                        output,
                                        (sourceValue as LocalDateTime).toInstant(ZONE_OFFSET).toEpochMilli()
                                )
                            } else {
                                prop.setter.call(
                                        output,
                                        formatDateTime(sourceValue as LocalDateTime, anno.format)
                                )
                            }
                        } else {
                            tryTransformRelationProp(prop, sourceProp, source, output)
                        }
                    }
                }
            }

    if (output is AssignableObject<*>) {
        (output as AssignableObject<S>).afterInitialized(source)
    }

    return output
}

fun getSourcePropsMap(source: Any): Map<String, KProperty1<Any, *>> {
    return source.javaClass.kotlin.memberProperties.map {
        it.name to it
    }.toMap()
}

fun objectToMap(source: Any, ignoreField: List<String> = listOf()): Map<String, Any?> {
    val result = mutableMapOf<String, Any?>()
    source.javaClass.kotlin.memberProperties.forEach { prop ->
        if (!ignoreField.contains(prop.name)) {
            prop.isAccessible = true
            result[prop.name] = prop.get(source)
        }
    }
    return result
}

private fun tryTransformRelationProp(targetProp: KMutableProperty<*>, sourceProp: KProperty<*>, source: Any, target: Any) {
    val sourceValue = sourceProp.getter.call(source)
    sourceValue?.let {
        if (Hibernate.isInitialized(it)) {
            val sourceClass = source::class
            val propJvmEnsure = targetProp.returnType.jvmErasure

            if (it is Collection<*>) {
                val childJvmEnsure = targetProp.returnType.arguments[0].type!!.jvmErasure
                var ignoreFields: List<String>? = null
                val propInstance = it.map { item ->
                    item?.let {
                        ignoreFields = ignoreFields ?: getIgnoreProps(item, sourceClass)
                        assignObject(childJvmEnsure.createInstance(), item, ignoreFields!!)
                    }
                }
                targetProp.setter.call(target, propInstance)
            } else {
                val propInstance = assignObject(propJvmEnsure.createInstance(), it, getIgnoreProps(sourceValue, sourceClass))
                targetProp.setter.call(target, propInstance)
            }
        }
    }
}

fun getIgnoreProps(source: Any, relationClass: KClass<*>): List<String> {
    return source::class.memberProperties.filter {
        it.returnType.withNullability(false) == relationClass.createType().withNullability(false)
    }.map { it.name }
}
