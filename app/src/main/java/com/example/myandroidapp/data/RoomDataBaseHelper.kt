package com.example.myandroidapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Todo::class), version = 1)
abstract class RoomDataBaseHelper : RoomDatabase() {

    abstract fun todoDao(): TodoDao?


//    fun onDelete() {
//        contactDataDao()?.deleteAll()
//        transactionInfoDao()?.deleteAll()
//    }

    companion object {

        // This is just for example for migration for next version of database
//        private val MIGRATION_1_2 = object : Migration(3, 4) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE ContactData ADD COLUMN MEMO TEXT")
//                database.execSQL("ALTER TABLE TransactionInformation ADD COLUMN TAGS TEXT")
//            }
//        }

        private var INSTANCE: RoomDataBaseHelper? = null

        fun getInstance(context: Context): RoomDataBaseHelper? {
            if (INSTANCE == null) {
                synchronized(RoomDataBaseHelper::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        RoomDataBaseHelper::class.java, "MyApp-room.db"
                    )
//                            .addMigrations(MIGRATION_1_2)
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }
}