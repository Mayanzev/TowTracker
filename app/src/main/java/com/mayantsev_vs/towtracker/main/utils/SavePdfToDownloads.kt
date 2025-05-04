package com.mayantsev_vs.towtracker.main.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.mayantsev_vs.towtracker.service.data.cache.ServiceDao
import com.mayantsev_vs.towtracker.track.data.cache.TrackDao
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
        val services = serviceDao.getAllServicesList()
        val tracks = trackDao.getAllTracksList()

        var totalPrice = 0.0
        val maxY = 800f

        val pdfDocument = PdfDocument()
        val paint = Paint().apply {
            textSize = 15f
            color = Color.BLACK
        }

        val lineHeight = paint.textSize + 6f
        var yPosition = 60f

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        fun checkPageSpace(lines: Int = 1): Boolean {
            return yPosition + lines * lineHeight > maxY
        }

        fun newPage() {
            pdfDocument.finishPage(page)
            page = pdfDocument.startPage(pageInfo)
            canvas = page.canvas
            yPosition = 60f
        }

        val currentDate = TimeUtils.getDate()
        yPosition = canvas.drawWrappedText(
            text = "Дата формирования чека: $currentDate",
            x = 40f,
            startY = yPosition,
            paint = paint,
            maxWidth = 400f,
            lineHeight = lineHeight
        )
        yPosition += lineHeight

        yPosition = canvas.drawWrappedText(
            text = "Выполненные маршруты:",
            x = 40f,
            startY = yPosition,
            paint = paint,
            maxWidth = 400f,
            lineHeight = lineHeight
        )

        for (track in tracks) {
            val fields = listOf(
                "• Дата: ${track.date ?: "неизвестно"}",
                "  Время: ${track.time ?: "неизвестно"}",
                "  Дистанция: ${track.distance ?: "0"} км",
                "  Средняя скорость: ${track.speed ?: "0"} км/ч",
                "  Цена: ${track.price ?: "0"} ₽",
                "  Откуда: ${track.firstCity ?: "неизвестно"}",
                "  Куда: ${track.secondCity ?: "неизвестно"}"
            )

            for (field in fields) {
                if (checkPageSpace()) newPage()
                yPosition = canvas.drawWrappedText(field, 80f, yPosition, paint, 360f, lineHeight)
            }

            yPosition += lineHeight * 0.5f
            totalPrice += track.price.toDoubleOrNull() ?: 0.0
        }

        // Услуги
        if (checkPageSpace()) newPage()
        yPosition = canvas.drawWrappedText("Выполненные услуги:", 40f, yPosition, paint, 400f, lineHeight)

        for (service in services) {
            val text = "• ${service.name ?: "неизвестно"} — ${service.price ?: "0"} ₽ (Дата: ${service.date ?: "неизвестно"})"
            if (checkPageSpace(2)) newPage()
            yPosition = canvas.drawWrappedText(text, 80f, yPosition, paint, 360f, lineHeight)
            totalPrice += service.price?.toDoubleOrNull() ?: 0.0
        }

        yPosition += lineHeight

        if (checkPageSpace()) newPage()
        val formattedPrice = String.format("%.2f", totalPrice)
        canvas.drawText("ИТОГО: $formattedPrice ₽", 40f, yPosition, paint)

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
        }
    }
}

fun Canvas.drawWrappedText(
    text: String,
    x: Float,
    startY: Float,
    paint: Paint,
    maxWidth: Float,
    lineHeight: Float
): Float {
    var y = startY
    val words = text.split(" ")
    var currentLine = ""

    for (word in words) {
        val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
        if (paint.measureText(testLine) > maxWidth) {
            drawText(currentLine, x, y, paint)
            y += lineHeight
            currentLine = word
        } else {
            currentLine = testLine
        }
    }

    if (currentLine.isNotEmpty()) {
        drawText(currentLine, x, y, paint)
        y += lineHeight
    }

    return y
}
