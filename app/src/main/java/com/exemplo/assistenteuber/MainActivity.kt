package com.exemplo.assistenteuber

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 60, 60, 60)
        }

        val titulo = TextView(this).apply {
            text = "🤖 Assistente Uber Automático"
            textSize = 22f
        }

        val btnAcessibilidade = Button(this).apply {
            text = "1. Ativar Leitura Automática"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }

        val btnSobreposicao = Button(this).apply {
            text = "2. Permitir Alertas por cima de Apps"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
            }
        }

        layout.addView(titulo)
        layout.addView(btnAcessibilidade)
        layout.addView(btnSobreposicao)

        setContentView(layout)
    }
}
