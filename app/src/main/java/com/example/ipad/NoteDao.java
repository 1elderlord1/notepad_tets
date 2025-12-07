package com.example.ipad;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes WHERE title LIKE :query OR content LIKE :query ORDER BY timestamp DESC")
    LiveData<List<Note>> searchNotes(String query);

    @Query("SELECT * FROM notes WHERE category = :category ORDER BY timestamp DESC")
    LiveData<List<Note>> getNotesByCategory(String category);

    @Query("SELECT * FROM notes WHERE isTodo = 1 ORDER BY timestamp DESC")
    LiveData<List<Note>> getTodoNotes();
}

