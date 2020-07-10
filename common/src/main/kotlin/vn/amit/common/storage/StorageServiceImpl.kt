package vn.amit.common.storage

import net.bytebuddy.utility.RandomString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.awt.AlphaComposite
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.ImageWriter
import javax.imageio.plugins.jpeg.JPEGImageWriteParam
import javax.imageio.stream.FileImageOutputStream


@Service
class StorageServiceImpl @Autowired constructor(private val properties: StorageProperties) : StorageService {
    private val rootLocation = Paths.get("${properties.staticFolder}${properties.uploadPath}")

    private fun getDirectoryPath(folder: String): Path {
        return Paths.get("${properties.staticFolder}${properties.uploadPath}/${folder}")
    }

    override fun store(file: MultipartFile, folder: String): String {
        val filename = StringUtils.cleanPath(file.originalFilename!!)
        init(folder)
        try {
            if (file.isEmpty) {
                throw StorageException("failed_to_store_file", null, arrayOf(filename))
            }

            val extension = filename.substring(filename.lastIndexOf(".") + 1)
            if (!properties.acceptExtensions.contains(extension)) {
                throw StorageException("forbidden_extension")
            }
            val randomName = RandomString.make(10)
            val now = System.currentTimeMillis()
            val trueFileName = StringUtils.cleanPath( "$randomName-$now.jpg")

            val bufferedImage = resizeImageFromInputStream(file.inputStream, 999)
            val writer = getImageWriter()
            val param = getCompressImageParams(writer, 0.8f)

            compressAndWrite(writer, bufferedImage, getDirectoryPath(folder).resolve(trueFileName), param)
            return "${if (folder.isNotEmpty()) "$folder/" else ""}$trueFileName"
        } catch (e: IOException) {
            throw StorageException("failed_to_store_file", e, arrayOf(filename))
        }

    }

    private fun resizeImageFromInputStream(inputStream: InputStream, maxWidth: Int? = null): BufferedImage {
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

    private fun getCompressImageParams(writer: ImageWriter, quality: Float = 1f): ImageWriteParam {
        val imageWriteParam = writer.defaultWriteParam
        imageWriteParam.compressionMode = ImageWriteParam.MODE_EXPLICIT
        imageWriteParam.compressionQuality = quality
        return imageWriteParam
    }

    private fun getImageWriter(): ImageWriter {
        val iterator = ImageIO.getImageWritersByFormatName("jpg")
        return iterator.next()
    }

    private fun compressAndWrite(writer: ImageWriter, buffer: BufferedImage,  outputPath: Path, param: ImageWriteParam) {
        val compressedImageFile = outputPath.toFile()
        val imageOutputStream = FileImageOutputStream(compressedImageFile)

//        val originalWidth: Int = bufferedImage.getWidth()
//        val originalHeight: Int = bufferedImage.getHeight()
//        val type = if (bufferedImage.getType() === 0) BufferedImage.TYPE_INT_ARGB else bufferedImage.getType()

        //rescale 50%

        //rescale 50%
//        val resizedImage = BufferedImage(originalWidth / 2, originalHeight / 2, type)
//        val g = resizedImage.createGraphics()
//        g.drawImage(bufferedImage, 0, 0, originalWidth / 2, originalHeight / 2, null)
//        g.dispose()
//        g.composite = AlphaComposite.Src
//        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
//        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
//        ImageIO.write(resizedImage, "jpg", File("Lenna50.jpg"))

//        val imageOutputStream = new MemoryCacheImageOutputStream(outputStream);
        writer.output = imageOutputStream
        val iioimage = IIOImage(buffer, null, null);
        writer.write(null, iioimage, param);
        imageOutputStream.flush()
    }

    override fun loadAll(): Stream<Path> {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter { path -> path != this.rootLocation }
                    .map(this.rootLocation::relativize)
        } catch (e: IOException) {
            throw StorageException("failed_to_read_store_file", e)
        }

    }

    override fun load(filename: String): Path {
        return rootLocation.resolve(filename)
    }

    override fun loadAsResource(filename: String): Resource {
        try {
            val file = load(filename)
            val resource = UrlResource(file.toUri())
            return if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw StorageFileNotFoundException("failed_to_read_store_file")
            }
        } catch (e: MalformedURLException) {
            throw StorageFileNotFoundException("failed_to_read_store_file", e)
        }

    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

    override fun init(folder: String) {
        try {
            val dir = getDirectoryPath(folder)
            if (!Files.exists(dir))
                Files.createDirectories(dir)
        } catch (e: IOException) {
            throw StorageException("Could not initialize storage", e)
        }

    }
}