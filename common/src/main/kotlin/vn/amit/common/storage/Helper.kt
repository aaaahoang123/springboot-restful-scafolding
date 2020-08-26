package vn.amit.common.storage

import net.bytebuddy.utility.RandomString
import org.apache.commons.codec.binary.Base64
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.awt.AlphaComposite
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Path
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.ImageWriter
import javax.imageio.stream.FileImageOutputStream

internal fun generateRandomFileName(extension: String = "jpg"): String {
    val randomName = RandomString.make(10)
    val now = System.currentTimeMillis()
    return StringUtils.cleanPath( "$randomName-$now.$extension")
}

internal fun getExtensionOfFile(file: MultipartFile): String {
    val filename = StringUtils.cleanPath(file.originalFilename!!)
    return getExtensionOfFile(filename)
}

internal fun getExtensionOfFile(filename: String): String {
    return filename.substring(filename.lastIndexOf(".") + 1)
}

internal fun getExtensionFromContentType(contentType: String): String {
    return contentType.substring(contentType.lastIndexOf("/") + 1)
}

internal fun tryResolveAsUrl(dirtyUrl: String): URL? {
    return try {
        URL(dirtyUrl)
    } catch (e: MalformedURLException) {
        null
    }
}

internal fun tryResolveAsBase64(source: String): ByteArray? {
    val delimiter = "base64,"
    val resolvedSource = if(source.contains(delimiter)) {
        source.split(delimiter)[1]
    } else {
        source
    }

    return try {
        Base64.decodeBase64(resolvedSource)
    } catch (e: Exception) {
        null
    }
}

internal fun getResizedBufferedImage(inputStream: InputStream, maxWidth: Int? = null): BufferedImage {
    val originalBufferedImage = ImageIO.read(inputStream)
    val originalWidth = originalBufferedImage.width
    val originalHeight = originalBufferedImage.height

    var width = originalWidth
    var height = originalHeight
    maxWidth?.let {
        if (width > maxWidth) {
            height = (originalHeight.toDouble() * (it.toDouble() / width.toDouble())).toInt()
            width = it
        }
    }
    val pixels = IntArray(width * height)

    val bi2 = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    bi2.setRGB(0, 0, width, height, pixels, 0, width)

    val g: Graphics2D = bi2.createGraphics()
    g.drawImage(originalBufferedImage, 0, 0, width, height, null)
    g.dispose()
    g.composite = AlphaComposite.Src
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    return bi2
}

internal fun getCompressibleImageWriter() : ImageWriter {
    return ImageIO.getImageWritersByFormatName("jpg").next()
}

internal fun getCompressImageParams(writer: ImageWriter, quality: Float = 1f): ImageWriteParam {
    val imageWriteParam = writer.defaultWriteParam
    imageWriteParam.compressionMode = ImageWriteParam.MODE_EXPLICIT
    imageWriteParam.compressionQuality = quality
    return imageWriteParam
}

internal fun saveImage(writer: ImageWriter, buffer: BufferedImage, outputPath: Path, param: ImageWriteParam) {
    val compressedImageFile = outputPath.toFile()
    val imageOutputStream = FileImageOutputStream(compressedImageFile)
    writer.output = imageOutputStream
    val iioimage = IIOImage(buffer, null, null)
    writer.write(null, iioimage, param)
    imageOutputStream.flush()
}