package com.example.databaseroomanddagger.data

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


@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteEntityDao(): NoteDao
}

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    fun getAllNotesEntity(): LiveData<List<NoteEntity>>

    @Insert
    suspend fun insert(noteEntity: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteById(noteId: Int)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()

    @Query("UPDATE notes SET title = :title, content = :content WHERE id = :noteId")
    suspend fun updateById(noteId: Int?, title: String, content: String)
}

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): NoteDatabase {
        return Room.databaseBuilder(
            application,
            NoteDatabase::class.java, "note_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideNoteDao(database: NoteDatabase): NoteDao {
        return database.noteEntityDao()
    }
}
