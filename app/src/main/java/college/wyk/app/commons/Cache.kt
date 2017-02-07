package college.wyk.app.commons

import android.util.Log
import college.wyk.app.WykApplication
import com.coolerfall.download.*
import java.io.File
import java.util.concurrent.TimeUnit

object Cache {

    val downloadManager: DownloadManager = DownloadManager.Builder().context(WykApplication.instance).downloader(OkHttpDownloader.create()).threadPoolSize(2).build()

    fun publicationFolder(): File = File(app.cacheDir, "publications").apply { mkdirs() }

    fun publicationFile(filename: String): File = File(publicationFolder(), filename)

    fun download(folder: File, filename: String, url: String, callback: DownloadCallback): Int {
        Log.i("WYK", File(folder, filename).absolutePath)
        val request = DownloadRequest.Builder()
                .url(url)
                .progressInterval(10, TimeUnit.MILLISECONDS)
                .priority(Priority.HIGH)
                .allowedNetworkTypes(DownloadRequest.NETWORK_WIFI or DownloadRequest.NETWORK_MOBILE)
                .destinationFilePath(File(folder, filename).absolutePath)
                .downloadCallback(callback)
                .build()
        return downloadManager.add(request)
    }

}