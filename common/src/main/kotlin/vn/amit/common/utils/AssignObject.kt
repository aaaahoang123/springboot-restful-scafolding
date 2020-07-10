package vn.amit.common.utils

import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

fun <T: Any> assignObject(output: T, source: Any, ignoreField: List<String> = listOf()): T {
    val mapValue = objectToMap(source)
    output.javaClass.kotlin.memberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .forEach { prop ->
                if (mapValue[prop.name] != null && !ignoreField.contains(prop.name)) {
                    prop.setter.call(output, mapValue[prop.name])
                }
            }
    return output
}

fun objectToMap(source: Any): Map<String, Any?> {
    val result = mutableMapOf<String, Any?>()
    source.javaClass.kotlin.memberProperties.forEach { prop ->
        result[prop.name] = prop.get(source)
    }
    return result
}