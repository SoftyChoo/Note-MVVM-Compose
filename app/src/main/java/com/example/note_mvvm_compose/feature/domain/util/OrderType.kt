package com.example.note_mvvm_compose.feature.domain.util

// (오름차순, 내림차순) 정렬 타입을 설정하기 위한 Sealed Class
sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}
