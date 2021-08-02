package com.mor.queenzl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    lateinit var queenChoices : ArrayList<Int>
    lateinit var choices : ArrayList<Boolean>
    var counter = 0
    var answer :Answer = Answer.None

    private val TAG = "GameFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initArr()
    }

    private fun initArr() {
        choices = ArrayList()
        queenChoices = ArrayList()
        for (i in 0..64){
            choices.add(i,false)
            queenChoices.add(i,0)
        }
    }


    fun setPointer(view: View) {
        //1. set background
        val tag:Int = view.tag.toString().toInt()
        val currentBool = choices[tag]
        choices[tag] = !currentBool
        Log.e(TAG, "setPointer: choices[tag] ${choices[tag]} $tag")

        findViewById<Button>(R.id.btn_check_answer).setOnClickListener {
            counter = 0
            for ( i in 1 until choices.size){
                Log.e(TAG, "setPointer: $i from ${choices.size}", )
                if(counter<9){
                    if(choices[i]){
                        queenChoices[i]=i
                        counter +=1
                        if (generateForbiddenPlacesForOneQueen(i)){
                            answer = Answer.Correct
                        } else{
                            answer = Answer.Wrong
                            updateAnswer()
                            return@setOnClickListener
                        }
                    }
                }else{
                    answer = Answer.TooMuchQueens
                }
            }

            initArr()
            updateAnswer()
        }
//        2. update the map of <tag,bool> and the for on the map, and then caclculate
        updateView(view)
    }



    private fun updateView(view: View) {
        if (choices[view.tag.toString().toInt()]){
            view.setBackgroundResource(R.drawable.queen_selected)
        }else{
            view.setBackgroundResource(R.drawable.queen_no_selected)
        }
    }


    private fun generateForbiddenPlacesForOneQueen(index: Int) :Boolean {
        var forbiddenCalc = ArrayList<Int>()

        val x:Int = index%8
        val y = (ceil((index.toDouble())/8)).toInt()//-1 לטובת הספירה של השורות?

        for (j in 1..8){
            if (j != (x)){
                //calc values of the forbidden places - horizontal
                val horizontal = index + j-x
                Log.e(TAG, "horizontal $horizontal" )
                //calc values of the forbidden places - vertical
                val vertical = index + ((j-y)*8)
                Log.e(TAG, "vertical $vertical" )
                //add values to list of forbidden places
                forbiddenCalc.addAll( listOf<Int>(horizontal,vertical))
            }
        }

        //135 D
        val p = x+y-1//סכום ההמהלכים
        var z:Int = index -((y-1)*7) //  מיקום תחילת הריצה
        var u = 1 // counter
        for (l in 0..p){
            forbiddenCalc.add(z)
            z+=7
        }
//        Log.e(TAG, "generateForbiddenPlacesForOneQueen: 2 $forbiddenCalc" )

        //45 D
        var r = index-((y-1)*9)// מיקום תחילת הריצה
        val t = y+(8-x)//סכום המהלכים

        for (b in 0..t){
            forbiddenCalc.add(r)
            r += 9
        }

        Log.e(TAG, "forbiddenCalc:  $forbiddenCalc" )
        return mergeAndCheck(forbiddenCalc)
    }

    private fun mergeAndCheck(forbiddenCalcList : ArrayList<Int>) :Boolean {
        forbiddenCalcList.addAll(queenChoices)
        return !hasDuplicates(forbiddenCalcList.toArray())
    }

    private fun hasDuplicates(arr: Array<Any>): Boolean { //return false if there is duplicates
        return arr.size != arr.distinct().count();
    }



    private fun updateAnswer() {
//        Log.e(TAG, "updateAnswer: $answer" )
        when (answer){

            Answer.Correct ->{
                findViewById<TextView>(R.id.q_and_a).setText("Well Done! Correct Answer")
            }

            Answer.Wrong ->{
                findViewById<TextView>(R.id.q_and_a).setText("Try Again... the answer is not correct")
                findViewById<TextView>(R.id.explain_question).setText("")
            }

            Answer.TooMuchQueens ->{
                findViewById<TextView>(R.id.q_and_a).setText("Try Again... ")
                findViewById<TextView>(R.id.explain_question).setText("too much queens...")
            }

            Answer.None ->{
                findViewById<TextView>(R.id.q_and_a).setText("Try Again... ")
                findViewById<TextView>(R.id.explain_question).setText("")
            }
        }
    }

}

    sealed class Answer {
        object Wrong : Answer()
        object Correct : Answer()
        object TooMuchQueens : Answer()
        object None : Answer()
    }