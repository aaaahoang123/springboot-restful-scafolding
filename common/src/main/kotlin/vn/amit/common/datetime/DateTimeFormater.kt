package vn.amit.common.datetime

import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

val COMMON_DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
val COMMON_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
val ZONE_OFFSET: ZoneOffset = OffsetDateTime.now().offset