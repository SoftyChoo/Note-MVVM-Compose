package com.example.note_mvvm_compose.feature.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.note_mvvm_compose.feature.domain.model.Note
import kotlinx.coroutines.flow.Flow

// DAO : 데이터 베이스에 접근하는 함수 제공
// 편의 메서드: SQL 코드 작성 없이 데이터베이스에서 행을 삽입하고 업데이트하고 삭제할 수 있습니다.
// 쿼리 메서드: 자체 SQL 쿼리를 작성하여 데이터베이스와 상호작용할 수 있습니다.

// Flow : Kotlin의 비동기적인 데이터 스트림 (발행자가 데이터를 발행하고 - 해당 발행자는 데이터의 소비자에게 지속적으로 데이터를 전달.)
// 구성요소
//  Producer: Upstream 으로 데이터를 제공하는 생산자
//  Consumer: Upstream 에서 제공된 데이터를 가져와서 읽어들이는 소비자
//  Intermediary(선택): 데이터를 경유하는 중간자

@Dao
interface NoteDao {

    @Query("SELECT * FROM note") // 모든 데이터 가져오기
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id") // id 값에 맞는 데이터 검색
    suspend fun getNoteById(id: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 데이터 추가
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}