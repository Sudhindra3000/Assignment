package com.myjar.jarassignment

import com.myjar.jarassignment.data.model.db.DbComputerItem
import com.myjar.jarassignment.data.model.db.DbItemData
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

fun createRealm(): Realm {
    val configuration =
        RealmConfiguration
            .Builder(
                schema = setOf(
                    DbComputerItem::class,
                    DbItemData::class
                ),
            )
            .schemaVersion(2)
            .deleteRealmIfMigrationNeeded()
            .build()
    return Realm.open(configuration)
}
