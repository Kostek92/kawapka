package com.application.kawapka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as KawapkaApplication
        setContent {
            KawapkaApp(
                catalogueRepository = app.catalogueRepository,
                collectionRepository = app.collectionRepository,
            )
        }
    }
}
