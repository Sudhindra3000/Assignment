package com.myjar.jarassignment.data.model.db

import io.realm.kotlin.types.RealmObject

class DbComputerItem: RealmObject {
    var id: String = ""
    var name: String = ""
    var data: DbItemData? = null
}

class DbItemData: RealmObject {
    var color: String = ""
    var capacity: String = ""
}
