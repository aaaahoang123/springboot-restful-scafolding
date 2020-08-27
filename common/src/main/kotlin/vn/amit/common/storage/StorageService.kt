package vn.amit.common.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream


interface StorageService {

    fun resolveFolder(rawFolder: String): String

    fun store(file: MultipartFile, rawFolder: String = ""): String

    fun store(source: String, rawFolder: String): String

    fun deleteAll()

    fun remove(path: String)

    fun getUploadedUrl(path: String): String

    fun getFullPath(path: String): String

}