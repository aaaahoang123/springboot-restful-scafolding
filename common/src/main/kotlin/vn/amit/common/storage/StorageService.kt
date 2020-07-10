package vn.amit.common.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream


interface StorageService {

    fun init(folder: String)

//    fun store(file: MultipartFile): String

    fun store(file: MultipartFile, folder: String = ""): String

    fun loadAll(): Stream<Path>

    fun load(filename: String): Path

    fun loadAsResource(filename: String): Resource

    fun deleteAll()

}