package com.example.lab6_images.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [ImageEntity::class], version = 1)
abstract class ImageDatabase : RoomDatabase() {
    abstract fun imageEntityDao(): ImageDao
}

@Dao
interface ImageDao {
    @Query("SELECT * FROM images")
    fun getAllImagesEntity(): LiveData<List<ImageEntity>>

    @Query("SELECT * FROM images")
    suspend fun getAllImagesEntityList(): List<ImageEntity>

    @Insert
    suspend fun insert(imageEntity: ImageEntity)

    @Query("DELETE FROM images WHERE id = :imageId")
    suspend fun deleteById(imageId: Int)

    @Query("DELETE FROM images")
    suspend fun deleteAll()

    @Query("UPDATE images SET name = :name, uri = :uri, description = :description WHERE id = :imageId")
    suspend fun updateById(imageId: Int?, name: String, uri: String, description: String)
}

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): ImageDatabase {
        return Room.databaseBuilder(
            application,
            ImageDatabase::class.java, "image_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideImageDao(database: ImageDatabase): ImageDao {
        return database.imageEntityDao()
    }
}