package ru.igni.manager.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM employees ORDER BY active DESC, name ASC")
    fun observeAll(): Flow<List<EmployeeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(employee: EmployeeEntity): Long

    @Query("UPDATE employees SET active = 0 WHERE id = :employeeId")
    suspend fun archive(employeeId: Long)
}
