package vn.amit.common.storage

class StorageResponseDto() {
    lateinit var path: String
    lateinit var fullPath: String
    lateinit var url: String

    constructor(path: String) : this() {
        val storageService = StorageProvider.getStaticService()

        this.path = path
        fullPath = storageService.getFullPath(path)
        url = storageService.getUploadedUrl(path)
    }
}