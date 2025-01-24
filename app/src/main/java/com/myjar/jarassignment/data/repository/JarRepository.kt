package com.myjar.jarassignment.data.repository

import android.util.Log
import com.myjar.jarassignment.data.api.ApiService
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.data.model.db.DbComputerItem
import com.myjar.jarassignment.data.model.db.DbItemData
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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
        try {
            val apiResp = apiService.fetchResults()
            emit(apiResp)
            db.write {
                apiResp.forEach { apiItem ->
                    copyToRealm(
                        DbComputerItem().apply {
                            id = apiItem.id
                            name = apiItem.name
                            if (apiItem.data != null)
                                data = DbItemData().apply {
                                    color = apiItem.data.color ?: ""
                                    capacity = apiItem.data.capacity ?: ""
                                }
                        },
                        updatePolicy = UpdatePolicy.ALL,
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "fetchResults: ", e)
            val dbList = db.query<DbComputerItem>().asFlow().first().list.toList().map {  dbItem ->
                ComputerItem(
                    id = dbItem.id,
                    name = dbItem.name,
                )
            }
            emit(dbList)
        }
    }
}