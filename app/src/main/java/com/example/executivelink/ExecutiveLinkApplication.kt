package com.example.executivelink

import android.app.Application
import com.example.executivelink.data.local.DatabaseInitializer
import com.example.executivelink.data.local.ExecutiveDatabase
import com.example.executivelink.data.repository.ExecutiveRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ExecutiveLinkApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database by lazy { ExecutiveDatabase.getDatabase(this) }
    val repository by lazy { ExecutiveRepository(database.executiveDao()) }

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            DatabaseInitializer.populateInitialDataIfEmpty(database.executiveDao())
        }
    }
}
