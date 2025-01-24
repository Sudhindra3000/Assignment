package com.myjar.jarassignment.data.repository

import android.util.Log
import com.myjar.jarassignment.data.api.ApiService
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.data.model.db.DbComputerItem
import com.myjar.jarassignment.data.model.db.DbItemData
import io.realm.kotlin.Realm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface JarRepository {
    suspend fun fetchResults(): Flow<List<ComputerItem>>
}

private const val TAG = "JarRepository"

class JarRepositoryImpl(
    private val apiService: ApiService,
    private val db: Realm,
) : JarRepository {
    override suspend fun fetchResults(): Flow<List<ComputerItem>> = flow {
        val apiResp = apiService.fetchResults()
        emit(apiResp)
        db.write {
            apiResp.forEach { apiItem ->
                val x = copyToRealm(
                    DbComputerItem().apply {
                        id = apiItem.id
                        name = apiItem.name
                        if (apiItem.data != null)
                            data = DbItemData().apply {
                                color = apiItem.data.color ?: ""
                                capacity = apiItem.data.capacity ?: ""
                            }
                    }
                )
            }
        }
    }
}