package com.kuba.example.service.impl.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryItemDao {
    @Query("SELECT * FROM repositories WHERE id = :id")
    suspend fun getRepository(id: Int): RepositoryItem

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRepository(repository: RepositoryItem)

    @Query("SELECT * FROM repositories ORDER BY name ASC")
    fun getAllRepositories(): Flow<List<RepositoryItem>>

    @Query("SELECT * FROM repositories WHERE name LIKE '%' || :query || '%' ORDER BY stars DESC")
    suspend fun searchRepos(query: String) : List<RepositoryItem>
}