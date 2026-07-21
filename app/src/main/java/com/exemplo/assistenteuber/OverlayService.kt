package com.exemplo.assistenteuber

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView

object OverlayService {
    private var overlayView: TextView? = null

    fun showFloatingBadge(context: Context, valorPorKm: Double) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (overlayView == null) {
            overlayView = TextView(context).apply {
                setPadding(30, 20, 30, 20)
                textSize = 18f
                setTextColor(Color.WHITE)
            }

            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                y = 150
            }

            windowManager.addView(overlayView, params)
        }

        val textoFormatado = String.format("R$ %.2f / km", valorPorKm)
        if (valorPorKm >= 2.20) {
            overlayView?.setBackgroundColor(Color.parseColor("#2E7D32"))
            overlayView?.text = "🟢 $textoFormatado"
        } else {
            overlayView?.setBackgroundColor(Color.parseColor("#C62828"))
            overlayView?.text = "🔴 $textoFormatado"
        }
    }
}
