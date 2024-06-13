package com.mrsajal.dao.follows

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object FollowsTable : Table(name = "follows") {
    val followerId = long("follower_id")
    val followingId = long("following_id")
    val followDate = datetime(name = "follow_date").defaultExpression(defaultValue = CurrentDateTime)
}