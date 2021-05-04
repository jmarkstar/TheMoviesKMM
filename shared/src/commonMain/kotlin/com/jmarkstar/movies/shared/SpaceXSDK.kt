package com.jmarkstar.movies.shared

import com.jmarkstar.movies.shared.cache.Database
import com.jmarkstar.movies.shared.cache.DatabaseDriverFactory
import com.jmarkstar.movies.shared.entity.RocketLaunch
import com.jmarkstar.movies.shared.network.SpaceXApi

class SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory) {

    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()

    @Throws(Exception::class) suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }
}