package com.ngocthanhnguyen.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ngocthanhnguyen.core.database.model.APICallResult

@Dao
interface APICallResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAPICallResult(apiCallResult: APICallResult)

    @Query("SELECT * FROM api_call_result WHERE url = :url")
    fun getAPICallResult(url: String): APICallResult?
}