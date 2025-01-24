package com.myjar.jarassignment.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myjar.jarassignment.createRetrofit
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.data.model.db.DbComputerItem
import com.myjar.jarassignment.data.model.db.DbItemData
import com.myjar.jarassignment.data.repository.JarRepository
import com.myjar.jarassignment.data.repository.JarRepositoryImpl
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JarViewModel : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    fun setQuery(value: String) {
        _query.value = value
    }

    private val _listStringData = MutableStateFlow<List<ComputerItem>>(emptyList())
    val listStringData: StateFlow<List<ComputerItem>>
        get() = _listStringData
            .combine(_query) { list, query ->
                list.filter { item ->
                    item.name.contains(query, ignoreCase = true)
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val repository: JarRepository = JarRepositoryImpl(
        createRetrofit(),
        Realm.open(RealmConfiguration.create(schema = setOf(DbComputerItem::class, DbItemData::class)))
    )

    fun fetchData() {
        viewModelScope.launch {
            _listStringData.emitAll(repository.fetchResults())
        }
    }
}
