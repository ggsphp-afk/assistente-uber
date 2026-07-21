package com.exemplo.assistenteuber

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class UberAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val rootNode = rootInActiveWindow ?: return
        val textList = mutableListOf<String>()

        extractAllTexts(rootNode, textList)

        val rideInfo = UberDataParser.parse(textList)

        if (rideInfo.price != null && rideInfo.km != null && rideInfo.km > 0) {
            val valorPorKm = rideInfo.price / rideInfo.km
            OverlayService.showFloatingBadge(this, valorPorKm)
        }
    }

    private fun extractAllTexts(node: AccessibilityNodeInfo?, list: MutableList<String>) {
        if (node == null) return
        if (!node.text.isNullOrEmpty()) {
            list.add(node.text.toString())
        }
        for (i in 0 until node.childCount) {
            extractAllTexts(node.getChild(i), list)
        }
    }

    override fun onInterrupt() {}
}

object UberDataParser {
    data class Ride(val price: Double?, val km: Double?)

    fun parse(texts: List<String>): Ride {
        var price: Double? = null
        var totalKm = 0.0

        val priceRegex = Regex("""R\$\s*(\d+[\.,]\d{2})""")
        val kmRegex = Regex("""(\d+[\.,]?\d*)\s*km""", RegexOption.IGNORE_CASE)

        for (text in texts) {
            priceRegex.find(text)?.let {
                price = it.groupValues[1].replace(",", ".").toDoubleOrNull()
            }
            kmRegex.findAll(text).forEach {
                it.groupValues[1].replace(",", ".").toDoubleOrNull()?.let { km ->
                    totalKm += km
                }
            }
        }
        return Ride(price, if (totalKm > 0) totalKm else null)
    }
}
