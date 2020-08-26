package vn.amit.common.storage

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import vn.amit.common.datetime.formatDateTime
import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.URL
import java.net.URLConnection
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDateTime


@Service
class StorageServiceImpl @Autowired constructor(private val properties: StorageProperties) : StorageService {
    private val folderLikeDateFormatter = "yyyy/MM/dd"

    private fun getDirectoryPath(vararg paths: String): Path {
        return Paths.get(properties.staticFolder, properties.uploadPath, *paths)
    }

    override fun store(file: MultipartFile, rawFolder: String): String {
        val folder = resolveFolder(rawFolder)
        return storeFileInternal(
                buildFileInfo(file, folder)
        )
    }

    override fun store(source: String, rawFolder: String): String {
        val folder = resolveFolder(rawFolder)
        val fileInfo = tryResolveAsUrl(source)?.let { buildFileInfo(it, folder) }
                ?: tryResolveAsBase64(source)?.let { buildFileInfo(it, folder) }
                ?: throw StorageException("file_type_not_supported")

        return storeFileInternal(fileInfo)
    }

    protected fun storeFileInternal(fileInfo: SimpleFileInfo): String {
        if (!properties.acceptExtensions.contains(fileInfo.extension)) {
            throw StorageException("forbidden_extension", null, arrayOf(fileInfo.extension))
        }

        if (properties.compressibleExtensions.contains(fileInfo.extension)) {
            return resolveCompressibleFile(fileInfo)
        }
        return resolveIncompressibleFile(fileInfo)
    }

    private fun resolveCompressibleFile(fileInfo: SimpleFileInfo): String {
        val folder = fileInfo.folder
        val fileName = generateRandomFileName("jpg")
        val bufferedImage = getResizedBufferedImage(fileInfo.stream, 1000)
        val writer = getCompressibleImageWriter()
        val param = getCompressImageParams(writer, 0.8f)

        saveImage(writer, bufferedImage, getDirectoryPath(folder, fileName), param)
        return "${if (folder.isNotEmpty()) "$folder/" else ""}$fileName"
    }

    private fun resolveIncompressibleFile(fileInfo: SimpleFileInfo): String {
        val folder = fileInfo.folder

        val filename = generateRandomFileName(fileInfo.extension)
        fileInfo.stream.use {
            Files.copy(it, getDirectoryPath(folder, filename),
                    StandardCopyOption.REPLACE_EXISTING)
        }
        return "${if (folder.isNotEmpty()) "$folder/" else ""}$filename"
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(getDirectoryPath())
    }

    override fun remove(path: String) {
        try {
            Files.delete(getDirectoryPath(path))
        } catch (e: IOException) {
            throw StorageFileNotFoundException("file_not_found", null, arrayOf(path))
        }
    }

    override fun resolveFolder(rawFolder: String): String {
        try {
            var folder = rawFolder
            if (properties.dailyFolder.contains(rawFolder)) {
                folder += "/" + formatDateTime(LocalDateTime.now(), folderLikeDateFormatter)
            }
            val dir = getDirectoryPath(folder)
            if (!Files.exists(dir))
                Files.createDirectories(dir)
            return folder
        } catch (e: IOException) {
            throw StorageException("Can not initialize storage", e)
        }
    }

    override fun getUploadedUrl(path: String): String {
        return "${properties.storageDomain}${properties.uploadPath}/$path"
    }

    override fun getFullPath(path: String): String {
        return "${properties.uploadPath}/$path"
    }

    private fun buildFileInfo(file: MultipartFile, folder: String): SimpleFileInfo {
        val filename = StringUtils.cleanPath(file.originalFilename!!)
        if (file.isEmpty) {
            throw StorageException("the_file_can_not_be_empty", null, arrayOf(filename))
        }
        val extension = getExtensionOfFile(file)

        return SimpleFileInfo(file.inputStream, folder, extension)
    }

    private fun buildFileInfo(url: URL, folder: String): SimpleFileInfo {
        try {
            val connection = url.openConnection()
            val contentType = connection.contentType
            val extension = getExtensionFromContentType(contentType)
            val stream = connection.getInputStream()
            return SimpleFileInfo(stream, folder, extension)
        } catch (e: Exception) {
            throw StorageException("can_not_connect_to_url", null, arrayOf(url.toString()))
        }
    }

    private fun buildFileInfo(byteArray: ByteArray, folder: String): SimpleFileInfo {
        val stream = ByteArrayInputStream(byteArray)
        val contentType = URLConnection.guessContentTypeFromStream(stream)
        val extension = getExtensionFromContentType(contentType)

        return SimpleFileInfo(stream, folder, extension)
    }
}