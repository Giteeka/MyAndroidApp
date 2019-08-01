package com.example.myandroidapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.myandroidapp.data.AppDbHelper
import com.example.myandroidapp.data.DpHelper
import com.example.myandroidapp.data.FirebaseDatabaseHelper
import com.example.myandroidapp.data.RoomDataBaseHelper
import com.example.myandroidapp.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {


    @Provides
    @Singleton
    fun provideContext(application: Application): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideRoomDataBaseHelper(context: Context, @DatabaseInfo name: String): RoomDataBaseHelper {
        return Room.databaseBuilder(
            context.applicationContext, RoomDataBaseHelper::class.java, name
        ).build()
    }


    @Provides
    @DatabaseInfo
    fun provideName(): String {
        return Constants.DB_NAME
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabaseHelper(): FirebaseDatabaseHelper {
        return FirebaseDatabaseHelper()
    }

    @Provides
    @Singleton
    fun providedpHelper(roomDataBaseHelper: RoomDataBaseHelper): DpHelper {
        return AppDbHelper(roomDataBaseHelper)
    }
}