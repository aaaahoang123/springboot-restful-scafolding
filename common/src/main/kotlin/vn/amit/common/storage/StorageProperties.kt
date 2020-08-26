package vn.amit.common.storage

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
class StorageProperties {
    @Value("\${storage.public.folder:public}")
    lateinit var staticFolder: String
    @Value("\${storage.upload.path:/upload}")
    lateinit var uploadPath: String
    @Value("\${storage.serve.domain:#{null}}")
    var storageDomain: String? = null
    val acceptExtensions = listOf("jpg", "jpeg", "png", "gif", "doc", "docx", "xls", "xlsx")

    val compressibleExtensions = listOf("jpg", "jpeg", "png")

    val dailyFolder = listOf(UploadFolder.DOCUMENTS)
}