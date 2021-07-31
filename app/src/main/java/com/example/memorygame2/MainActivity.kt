package com.example.memorygame2

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame2.models.BoardSize
import com.example.memorygame2.models.MemoryGame
import com.google.android.material.snackbar.Snackbar
import nl.dionsegijn.konfetti.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class MainActivity : AppCompatActivity() {
    companion object{
        //private const val TAG="MainActivity"
    }



    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var memoryGame: MemoryGame
    private lateinit var rvBoard: RecyclerView
    private lateinit var  tvMoves: TextView
    private lateinit var  tvPairs: TextView
    private lateinit var confettiView: KonfettiView
    private var boardSize:BoardSize=BoardSize.NIGHTMARE
    private val TAG="MainActivity"
    private lateinit var clRoot: ConstraintLayout
    private var moveCount: Int=0
    private var pairsFound: Int=0
    private var totalPairs: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        rvBoard=findViewById(R.id.rvBoard)
        tvMoves=findViewById(R.id.tvMoves)
        tvPairs=findViewById(R.id.tvPairs)
        clRoot=findViewById(R.id.clRoot)
        confettiView=findViewById(R.id.viewConfetti)

        setupBoard(1)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mbRefresh -> {
                if(moveCount>0)
                    showAlertDialog(getString(R.string.quit),null,View.OnClickListener {
                        showNewSizeDialoge()
                    })
            }
            R.id.mbActionMenu ->{
                showNewSizeDialoge()

            }
        }
        return true
    }

    private fun showNewSizeDialoge() {

        val rbViwer =LayoutInflater.from(this).inflate(R.layout.radio_group,null)
        val rbGroup =rbViwer.findViewById<RadioGroup>(R.id.rgSize)
        var bs: Int =0
        showAlertDialog(getString(R.string.choose),rbViwer,View.OnClickListener {
            bs=when(rbGroup.checkedRadioButtonId){
                R.id.rbEasy-> 1
                R.id.rbHard-> 3
                R.id.rbNightmare-> 4
                R.id.rbHell->5
                else -> 2
            }
            setupBoard(bs)
        })


    }

    private fun updateGameWithFlip(position: Int) {
        if(memoryGame.winner()) {
            Snackbar.make(clRoot,getString(R.string.youWon),Snackbar.LENGTH_LONG).show()
            return
        }


        if(memoryGame.isFace(position)) {
            Snackbar.make(clRoot,"Invalid Move!!",Snackbar.LENGTH_SHORT).show()
            return
        }

        memoryGame.flipit(position)
        adapter.notifyDataSetChanged()
        moveCount++
        pairsFound = memoryGame.numPairsFoud
        totalPairs=boardSize.getNumPairs()
        tvMoves.setText("Moves : "+ moveCount/2)
        tvPairs.setText("Pairs :$pairsFound /$totalPairs")
        if(pairsFound==totalPairs)
        {
            confettiView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA,Color.BLUE,R.color.purple_200)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 4f)
                .setFadeOutEnabled(true)
                .setTimeToLive(3000L)
                .addShapes(Shape.Square, Shape.Circle)
                .addSizes(Size(8))
                .setPosition(-50f, confettiView.width + 50f, -50f, -50f)
                .streamFor(500, 5000L)
            showAlertDialog(getString(R.string.Winner),null,View.OnClickListener{
                showAlertDialog(getString(R.string.startNew),null,View.OnClickListener{
                    showNewSizeDialoge()
                })
            })
        }
    }


    private fun showAlertDialog(title:String,view: View?,positiveClickListener:View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton(getString(R.string.cancel),null)
            .setPositiveButton(getString(R.string.ok)){_,_->
                positiveClickListener.onClick(null)
            }
            .show()
    }

    private fun setupBoard(bs:Int) {

        moveCount=0
        pairsFound=0
        if(bs==4) {
            totalPairs = 20
            tvMoves.setText(getString(R.string.nightmare))
            tvPairs.setText("Pairs :$pairsFound /$totalPairs")
            boardSize = BoardSize.NIGHTMARE
        }
        else if(bs==2) {
            boardSize = BoardSize.MEDIUM
            totalPairs = 9
            tvMoves.setText(getString(R.string.medium))
            tvPairs.setText("Pairs :$pairsFound /$totalPairs")
        }
        else if(bs==3) {
            boardSize = BoardSize.HARD
            totalPairs = 12
            tvMoves.setText(getString(R.string.hard))
            tvPairs.setText("Pairs :$pairsFound /$totalPairs")

        }
        else if(bs==5)
        {
            boardSize = BoardSize.HELL
            totalPairs = 27
            tvMoves.setText(getString(R.string.hell))
            tvPairs.setText("Pairs :$pairsFound /$totalPairs")
        }
        else {
            boardSize = BoardSize.EASY
            totalPairs = 4
            tvMoves.setText(getString(R.string.easy))
            tvPairs.setText("Pairs :$pairsFound /$totalPairs")
        }

        memoryGame=MemoryGame(boardSize)
        adapter=MemoryBoardAdapter(this,boardSize,memoryGame.cards,object: MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                Log.i(TAG,"Clicked $position")
                updateGameWithFlip(position)
            }

        })
        rvBoard.adapter=adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager=GridLayoutManager(this,boardSize.getWidth())

    }
}