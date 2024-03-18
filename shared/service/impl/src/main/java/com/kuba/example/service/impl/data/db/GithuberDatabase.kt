package com.kuba.example.service.impl.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RepositoryItem::class], version = 1, exportSchema = false)
abstract class GithuberDatabase : RoomDatabase() {

    abstract fun repositoryItemDao(): RepositoryItemDao

    companion object {
        const val DATABASE_NAME: String = "github_database"
    }
}