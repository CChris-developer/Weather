package com.example.myapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Cities::class, Weather::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    object Migration2_3 : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE test(id INTEGER NOT NULL PRIMARY KEY, name TEXT NOT NULL, country TEXT NOT NULL, time TEXT NOT NULL, weatherCode INTEGER NOT NULL, temperatureMax FLOAT NOT NULL, temperatureMin FLOAT NOT NULL)")
            db.execSQL("DROP TABLE Cities")
            db.execSQL("ALTER TABLE test RENAME TO Cities")
        }
    }
}