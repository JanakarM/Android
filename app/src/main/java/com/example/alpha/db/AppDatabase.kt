package com.example.alpha.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.alpha.MainActivity
import com.example.alpha.db.converter.TypeConverter
import com.example.alpha.db.dao.AppDao
import com.example.alpha.db.entity.Line
import com.example.alpha.db.entity.Party
import com.example.alpha.db.entity.PartyTransaction
import com.example.alpha.db.entity.User

@Database(entities = [User::class, Line::class, Party::class, PartyTransaction::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun appDao(): AppDao

    companion object {
        var INSTANCE: AppDatabase? = null
        fun getDatabase(): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                                MainActivity.context.applicationContext,
//                                AppDatabase::class.java, "/storage/emulated/0/alpha/databases/alpha/dbBackup"
                                AppDatabase::class.java, "alpha"
//                                AppDatabase::class.java, "" + MainActivity.context.applicationContext.obbDir + "/databases/alpha"
                        )
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}