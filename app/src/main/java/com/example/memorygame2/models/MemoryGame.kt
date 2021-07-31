package com.example.memorygame2.models

import com.example.memorygame2.utils.DEF_ICON

class MemoryGame (private val boardSize: BoardSize)
{
    val cards: List<MemoryCard>
    var numPairsFoud=0
    var foundMatch: Boolean = false
    private var indexOfselectedCard: Int? = null

    init {
        val images= DEF_ICON.shuffled().take(boardSize.getNumPairs())
        val randImg=(images+images).shuffled()
        cards=randImg.map{MemoryCard(it)}
    }

    fun flipit(position: Int) {

        val card:MemoryCard=cards[position]
        if(indexOfselectedCard==null)
        {
            restorecards()
            indexOfselectedCard=position
        }
        else
        {
            foundMatch =checkforMatch(indexOfselectedCard!!,position)
            indexOfselectedCard=null
        }

        card.isFace=!card.isFace

    }

    private fun checkforMatch(pos1: Int, pos2: Int): Boolean {
        if(cards[pos1].id!=cards[pos2].id)
            return false
        cards[pos1].isMAtch=true
        cards[pos2].isMAtch=true
        numPairsFoud++
        return true
    }

    private fun restorecards() {
        for(c:MemoryCard in cards) {
            if (!c.isMAtch)
                c.isFace = false
        }
    }

    fun winner(): Boolean {

        return numPairsFoud==boardSize.getNumPairs()

    }

    fun isFace(position: Int): Boolean {

        return cards[position].isFace
    }

}