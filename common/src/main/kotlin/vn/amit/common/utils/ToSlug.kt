package vn.amit.common.utils

import java.text.Normalizer
import java.util.*
import java.util.regex.Pattern

val WHITESPACE: Pattern = Pattern.compile("[\\s]")
val UNICODE: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
val D_CHARACTER: Pattern = Pattern.compile("[đĐ]")
fun toSlug(input: String, whiteSpace: String = "-"): String {
    val nonLatin: Pattern = Pattern.compile("[^\\w$whiteSpace]")

    var str = WHITESPACE.matcher(input).replaceAll(whiteSpace)
    str = Normalizer.normalize(str, Normalizer.Form.NFD)
    str = UNICODE.matcher(str).replaceAll("")
    str = D_CHARACTER.matcher(str).replaceAll("d")
    str = nonLatin.matcher(str).replaceAll(whiteSpace)
    return str.toLowerCase(Locale.ENGLISH)
}