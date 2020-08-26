package vn.amit.common.storage

import java.io.InputStream

class SimpleFileInfo(
        val stream: InputStream,
        val folder: String,
        val extension: String
)