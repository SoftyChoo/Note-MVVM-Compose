package com.example.note_mvvm_compose.feature.data.data_source

import androidx.room.Dao

// DAO : 데이터 베이스에 접근하는 함수 제공
//편의 메서드: SQL 코드 작성 없이 데이터베이스에서 행을 삽입하고 업데이트하고 삭제할 수 있습니다.
//쿼리 메서드: 자체 SQL 쿼리를 작성하여 데이터베이스와 상호작용할 수 있습니다.

@Dao
interface NoteDao {
}