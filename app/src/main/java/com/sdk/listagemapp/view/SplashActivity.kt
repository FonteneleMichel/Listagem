package com.sdk.listagemapp.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.sdk.listagemapp.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY = 2000L // Tempo de exibição do splash screen em milissegundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        // Aguarda um tempo definido antes de iniciar a próxima atividade
        Handler().postDelayed({
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
            finish() // Finaliza a atividade do splash screen
        }, SPLASH_DELAY)
    }
}
