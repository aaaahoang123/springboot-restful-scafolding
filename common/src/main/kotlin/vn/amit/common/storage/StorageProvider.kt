package vn.amit.common.storage

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class StorageProvider : InitializingBean {
    @Autowired
    private lateinit var storageService: StorageService

    companion object {
        @JvmStatic
        private lateinit var serviceStatic: StorageService

        fun getStaticService(): StorageService {
            return serviceStatic
        }
    }

    override fun afterPropertiesSet() {
        serviceStatic = storageService
    }
}