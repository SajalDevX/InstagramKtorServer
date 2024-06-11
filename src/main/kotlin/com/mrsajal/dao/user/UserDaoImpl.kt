package com.mrsajal.dao.user

import com.mrsajal.dao.DatabaseFactory.dbQuery
import com.mrsajal.model.SignUpParams
import com.mrsajal.dao.user.UserTable.id
import com.mrsajal.security.hashPassword
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserDaoImpl : UserDao {
    override suspend fun insert(params: SignUpParams): UserRow? {
        return dbQuery{
            val insertStatement = UserTable.insert {
                it[name]=params.name
                it[email]=params.email
                it[password]= hashPassword(params.password)
            }
            insertStatement.resultedValues?.singleOrNull()?.let {
                rowToUser(it)
            }
        }
    }

    override suspend fun findByEmail(email: String): UserRow? {
        return dbQuery {
            UserTable.select{ UserTable.email eq email}
                .map { rowToUser(it) }
                .singleOrNull()
        }
    }

    private fun rowToUser(row:ResultRow): UserRow {
        return UserRow(
            id = row[id],
            name = row[UserTable.name],
            bio = row[UserTable.bio],
            avatar = row[UserTable.avatar],
            password = row[UserTable.password]
        )
    }
}