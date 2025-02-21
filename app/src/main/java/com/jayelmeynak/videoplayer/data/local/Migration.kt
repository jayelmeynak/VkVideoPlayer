package com.jayelmeynak.videoplayer.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Создаем таблицу video_remote_keys
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `video_remote_keys` (
                `remoteKeyId` INTEGER NOT NULL, 
                `nextPage` INTEGER,
                PRIMARY KEY(`remoteKeyId`)
            )
            """.trimIndent()
        )
    }
}