package vn.amit.common.annotation

@Target(
        AnnotationTarget.PROPERTY,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.FIELD,
        AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class FormattedDateTime(val format: String = "HH:mm dd/MM/yyyy", val toMs: Boolean = false)