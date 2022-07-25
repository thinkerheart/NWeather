package com.ngocthanhnguyen.nweather.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "api_call_result")
data class APICallResult(
    @PrimaryKey @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "result") val result: String
)