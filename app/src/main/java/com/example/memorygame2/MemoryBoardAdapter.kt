package com.example.memorygame2

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame2.models.BoardSize
import com.example.memorygame2.models.MemoryCard
import kotlin.math.min

class MemoryBoardAdapter(
    private val context: Context,
    private val cardPcs: BoardSize,
    private val memCards: List<MemoryCard>,
    private val cardClickListener: CardClickListener) : RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {
    companion object{
        private const val MARGIN_SIZE=8
        private const val TAG="MemoryBoardAdapter"
    }

    interface CardClickListener{
        fun onCardClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardW=parent.width/cardPcs.getWidth()-(2* MARGIN_SIZE)
        val cardH=parent.height/cardPcs.getHeight()-(2* MARGIN_SIZE)
        val cardS= min(cardH,cardW)
        val view= LayoutInflater.from(context).inflate(R.layout.memory_card,parent,false)
        val layoutparams: ViewGroup.MarginLayoutParams = view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams
        layoutparams.width=cardS
        layoutparams.height=cardS
        layoutparams.setMargins(MARGIN_SIZE,MARGIN_SIZE,MARGIN_SIZE,MARGIN_SIZE)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cardPcs.allCards

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        private val imageButton= itemView.findViewById<ImageButton>(R.id.imageButton)

        fun bind(position: Int) {
            val memoryCard: MemoryCard=memCards[position]
            imageButton.setImageResource(if (memCards[position].isFace) memCards[position].id else R.drawable.ic_lock)
            imageButton.alpha=if(memoryCard.isMAtch) .4f else 1.0f
            val colorState :ColorStateList?= if(memoryCard.isMAtch) ContextCompat.getColorStateList(context,R.color.color_gray) else null
            ViewCompat.setBackgroundTintList(imageButton,colorState)
            imageButton.setOnClickListener{
                Log.i(TAG,"Clicked $position")
                cardClickListener.onCardClicked(position)
            }

        }
    }


}
