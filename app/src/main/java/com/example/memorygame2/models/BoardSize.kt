package com.example.memorygame2.models

enum class BoardSize (val allCards: Int){

    EASY(allCards = 8),
    MEDIUM (allCards = 18),
    HARD(allCards = 24),
    NIGHTMARE(allCards = 40),
    HELL(allCards=60);

    fun getWidth(): Int{
        return when(this){
            EASY -> 2
            MEDIUM -> 3
            HARD -> 4
            NIGHTMARE -> 5
            HELL->6
        }
    }

    fun getHeight(): Int{
        return allCards/getWidth()
    }

    fun getNumPairs(): Int{
        return allCards/2
    }
}