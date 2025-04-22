package com.example.canvas.data.database

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

@Database(entities = [CanvasEntity::class], version = 1)
abstract class CanvasDataBase : RoomDatabase() {
    abstract fun canvasEntityDao(): CanvasDao
}

@Dao
interface CanvasDao {
    @Query("SELECT * FROM canvases")
    fun getAllCanvasesEntity(): LiveData<List<CanvasEntity>>

    @Insert
    suspend fun insert(canvasEntity: CanvasEntity)

    @Query("DELETE FROM canvases WHERE id = :canvasesId")
    suspend fun deleteById(canvasesId: Int)

    @Query("DELETE FROM canvases")
    suspend fun deleteAll()

    @Query("UPDATE canvases SET title = :title, content = :content WHERE id = :canvasesId")
    suspend fun updateById(canvasesId: Int?, title: String, content: String)
}

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): CanvasDataBase {
        return Room.databaseBuilder(
            application,
            CanvasDataBase::class.java, "canvas_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideNoteDao(database: CanvasDataBase): CanvasDao {
        return database.canvasEntityDao()
    }
}
