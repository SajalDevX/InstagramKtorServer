package com.mrsajal.dao.user

import org.jetbrains.exposed.sql.Table

object UserTable: Table(name = "users"){
    val id = long(name = "user_id").autoIncrement()
    val name = varchar(name = "user_name", length = 250)
    val email = varchar(name = "user_email", length = 250)
    val bio = text(name = "user_bio").default(
        defaultValue = "Hey what's up? Welcome to my instagram page"
    )
    val password = varchar(name = "user_password", length = 100)
    val avatar = text(name = "user_avatar").nullable()

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(id)
}

data class UserRow(
    val id:Long,
    val name:String,
    val bio:String,
    val avatar:String?,
    val password:String
)