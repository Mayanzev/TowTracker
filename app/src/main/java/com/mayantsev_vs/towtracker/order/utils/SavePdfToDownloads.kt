package com.mayantsev_vs.towtracker.order.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.mayantsev_vs.towtracker.order.data.cache.ServiceDao
import com.mayantsev_vs.towtracker.order.data.cache.TrackDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
fun generateAndSavePdf(
    context: Context,
    fileName: String,
    serviceDao: ServiceDao,
    trackDao: TrackDao,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch(Dispatchers.IO) {

        val services = serviceDao.getAllServicesPrint()
        val tracks = trackDao.getAllTracksPrint()
        var price = 0.0

        val pdfDocument = PdfDocument()
        val paint = Paint()
        paint.textSize = 16f
        paint.color = Color.BLACK

        val lineHeight = paint.textSize + 8f
        var yPosition = 100f

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        canvas.drawText("Список услуг:", 100f, yPosition, paint)
        yPosition += lineHeight

        services.forEach {
            canvas.drawText("- ${it.name}", 100f, yPosition, paint)
            yPosition += lineHeight
            price += it.price.toDouble()
        }

        yPosition += lineHeight
        canvas.drawText("Список маршрутов:", 100f, yPosition, paint)
        yPosition += lineHeight

        tracks.forEach {
            val firstCity = it.firstCity ?: "Неизвестный город"
            val secondCity = it.secondCity ?: "Неизвестный город"
            canvas.drawText("- $firstCity -> $secondCity", 100f, yPosition, paint)
            yPosition += lineHeight
            price += it.price.toDouble()
        }

        yPosition += lineHeight
        canvas.drawText("Итоговая стоимость: $price", 100f, yPosition, paint)
        yPosition += lineHeight

        pdfDocument.finishPage(page)
        savePdfToDownloads(context, fileName, pdfDocument)
        pdfDocument.close()
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun savePdfToDownloads(context: Context, fileName: String, pdfDocument: PdfDocument) {
    val contentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, "$fileName.pdf")
        put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }

    val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        contentResolver.openOutputStream(it)?.use { outputStream ->
            pdfDocument.writeTo(outputStream)
            //Toast.makeText(context, "PDF сохранен в Загрузки!", Toast.LENGTH_LONG).show()
        }
    } ?: run {
        //Toast.makeText(context, "Ошибка сохранения PDF", Toast.LENGTH_LONG).show()
    }
}

