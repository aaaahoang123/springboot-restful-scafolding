package vn.amit.common.storage

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("vn.amit.storage")
class StorageProperties {
    var storeFolder: String = "public"
    var uploadPath: String = "/upload"
    var serveDomain: String = ""

    val acceptExtensions = listOf("jpg", "jpeg", "png", "gif", "doc", "docx", "xls", "xlsx")
    val compressibleExtensions = listOf("jpg", "jpeg", "png")
    val dailyFolder = listOf(UploadFolder.DOCUMENTS)
}