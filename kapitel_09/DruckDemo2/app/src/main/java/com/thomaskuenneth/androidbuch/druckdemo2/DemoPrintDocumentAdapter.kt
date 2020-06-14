package com.thomaskuenneth.androidbuch.druckdemo2

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.*
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.pdf.PrintedPdfDocument
import java.io.*
import kotlin.math.cos
import kotlin.math.sin

class DemoPrintDocumentAdapter(private val context: Context) : PrintDocumentAdapter() {
    private var numPages = 0
    private var pdf: PrintedPdfDocument? = null

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes,
        cancellationSignal: CancellationSignal,
        callback: LayoutResultCallback,
        extras: Bundle?
    ) {
        // sofern vorhanden, altes freigeben
        disposePdf()
        // neues PDF-Dokument mit den gewünschten Attributen erzeugen
        pdf = PrintedPdfDocument(context, newAttributes)
        // auf Abbruchwunsch reagieren
        if (cancellationSignal.isCanceled) {
            callback.onLayoutCancelled()
            disposePdf()
            return
        }
        // erwartete Seitenzahl berechnen
        numPages = computePageCount(newAttributes)
        if (numPages > 0) {
            // Informationen an das Print-Framework zurückliefern
            val info = PrintDocumentInfo.Builder("sin_cos.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(numPages)
                .build()
            callback.onLayoutFinished(info, true)
        } else {
            // einen Fehler melden
            callback.onLayoutFailed("Fehler beim Berechnen der Seitenzahl")
        }
    }

    override fun onWrite(
        pages: Array<PageRange?>?,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal,
        callback: WriteResultCallback
    ) {
        pdf?.let { it ->
            // über alle Seiten des Dokuments iterieren
            for (i in 0 until numPages) {
                // Abbruch?
                if (cancellationSignal.isCanceled) {
                    callback.onWriteCancelled()
                    disposePdf()
                    return
                }
                val page = it.startPage(i)
                drawPage(page)
                it.finishPage(page)
            }
            // PDF-Dokument schreiben
            try {
                FileOutputStream(
                    destination.fileDescriptor
                ).let { stream ->
                    it.writeTo(stream)
                }
            } catch (e: IOException) {
                callback.onWriteFailed(e.toString())
                return
            }
            try {
                destination.close()
            } catch (e: IOException) {
                callback.onWriteFailed(e.toString())
                return
            }
        }
        callback.onWriteFinished(pages)
    }

    override fun onFinish() {
        disposePdf()
    }

    private fun computePageCount(printAttributes: PrintAttributes): Int {
        val size = printAttributes.mediaSize
        return if (size == null || !size.isPortrait) 2 else 1
    }

    // Einheiten entsprechen 1/72 Zoll
    private fun drawPage(page: PdfDocument.Page) {
        val nr = page.info.pageNumber.toFloat()
        // Breite und Höhe
        val w = page.canvas.width.toFloat()
        val h = page.canvas.height.toFloat()
        // Mittelpunkt
        val cx = w / 2f
        val cy = h / 2f
        val paint = Paint()
        paint.strokeWidth = 3f
        paint.color = Color.BLUE
        page.canvas.drawLine(cx, 0f, cx, h - 1f, paint)
        page.canvas.drawLine(0f, cy, w - 1f, cy, paint)
        paint.color = Color.BLACK
        var i = 0f
        while (i < w) {
            val y = if (nr == 0f) {
                (sin(i * (2f * Math.PI / w)) * cy + cy).toFloat()
            } else {
                (cos(i * (2f * Math.PI / w)) * cy + cy).toFloat()
            }
            page.canvas.drawPoint(i, y, paint)
            i++
        }
    }

    private fun disposePdf() {
        pdf?.close()
        pdf = null
    }
}