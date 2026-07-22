package com.exemplo.assistenteuber

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import java.util.regex.Pattern

class LeitorUberService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val rootNode = rootInActiveWindow ?: return

        // Procura por valores em Reais (ex: R$ 15,50) e Distâncias (ex: 4.2 km)
        var valorCorrida: Double? = null
        var kmTotal: Double? = null

        extrairTextoECalcular(rootNode) { texto ->
            if (texto.contains("R$")) {
                valorCorrida = extrairNumero(texto)
            } else if (texto.contains("km", ignoreCase = true)) {
                kmTotal = extrairNumero(texto)
            }
        }

        // Se encontrou valor e KM na tela, faz a conta automática!
        if (valorCorrida != null && kmTotal != null && kmTotal!! > 0) {
            val valorPorKm = valorCorrida!! / kmTotal!!
            
            // Exemplo de alerta rápido (Valor por KM)
            val mensagem = if (valorPorKm >= 2.0) {
                "✅ CORRIDA BOA! R$ ${String.format("%.2f", valorPorKm)}/km"
            } else {
                "⚠️ CORRIDARUIM! R$ ${String.format("%.2f", valorPorKm)}/km"
            }

            // Exibe mensagem instantânea
            Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
        }
    }

    private fun extrairTextoECalcular(node: AccessibilityNodeInfo?, aoEncontrarTexto: (String) -> Unit) {
        if (node == null) return
        if (node.text != null) {
            aoEncontrarTexto(node.text.toString())
        }
        for (i in 0 until node.childCount) {
            extrairTextoECalcular(node.getChild(i), aoEncontrarTexto)
        }
    }

    private fun extrairNumero(texto: String): Double? {
        val matcher = Pattern.compile("(\\d+[,.]?\\d*)").matcher(texto)
        return if (matcher.find()) {
            matcher.group(1)?.replace(",", ".")?.toDoubleOrNull()
        } else null
    }

    override fun onInterrupt() {}
}
