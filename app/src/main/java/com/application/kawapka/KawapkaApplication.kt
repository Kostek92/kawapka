package com.application.kawapka

import android.app.Application
import androidx.room.Room
import com.application.kawapka.data.catalogue.CatalogueRepository
import com.application.kawapka.data.catalogue.GeneratedCatalogueRepository
import com.application.kawapka.data.collection.CollectionRepository
import com.application.kawapka.data.collection.KawapkaDatabase
import com.application.kawapka.data.collection.RoomCollectionRepository

/**
 * Application-level dependency graph.
 *
 * The bundled catalogue is read-only; all personal state lives in Room and is
 * exposed through [collectionRepository].
 */
class KawapkaApplication : Application() {
    private val database: KawapkaDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            KawapkaDatabase::class.java,
            "kawapka.db",
        )
            .addMigrations(KawapkaDatabase.MIGRATION_1_2)
            .build()
    }

    val catalogueRepository: CatalogueRepository by lazy { GeneratedCatalogueRepository() }

    val collectionRepository: CollectionRepository by lazy {
        RoomCollectionRepository(database.collectionDao())
    }
}
