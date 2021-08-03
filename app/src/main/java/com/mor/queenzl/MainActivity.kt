package com.mor.queenzl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    var queenChoices : ArrayList<Int> = ArrayList()
    var choicesForUi : ArrayList<Boolean> = ArrayList()
    var counter = 0
    var answer :Answer = Answer.None

    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initArr()

        findViewById<Button>(R.id.btn_check_answer).setOnClickListener {
            checkAllQueens(this.choicesForUi)
            updateAnswer()
        }

    }

    private fun initArr() {
        for (i in 0..64){
            choicesForUi.add(i,false)
        }
        queenChoices = ArrayList()
    }


    fun setPointer(view: View) {
        //1. set background
        val tag:Int = view.tag.toString().toInt()
        val currentBool = choicesForUi[tag]
        choicesForUi[tag] = !currentBool

//        2. update the map of <tag,bool> and the for on the map, and then caclculate
        updateView(view)
    }



    private fun updateView(view: View) {
        if (choicesForUi[view.tag.toString().toInt()]){
            view.setBackgroundResource(R.drawable.queen_selected)
        }else{
            view.setBackgroundResource(R.drawable.queen_no_selected)
        }
    }


    private fun checkAllQueens(queensList: List<Boolean>){
        var arr: ArrayList<Int> = ArrayList()
        var arrTest: ArrayList<Int> = ArrayList()
        for (i in queensList.indices){
            if(queensList[i]){
                arr.add(i)
            }
        }
        if (arr.size>9){
            answer = Answer.TooMuchQueens
            return
        }

        for (q in 0 until arr.size){
            var currentQX :Int = arr[q]%8
            var currentQY: Int = ceil(arr[q].toDouble()/8).toInt()
            var slantQ45 : Int = currentQX - currentQY
            var slantQ135: Int = currentQX + currentQY
            for (a in q+1 until arr.size){
                var currentAX :Int = arr[a]%8
                var currentAY: Int = ceil(arr[a].toDouble()/8).toInt()
                var slantA45 : Int = currentAX - currentAY
                var slantA135: Int = currentAX + currentAY

                arrTest.addAll(listOf(
                    currentAX,currentQX,currentAY,currentQY,slantQ45,slantA45,slantA135,slantQ135 ))
                Log.d(TAG, "checkAllQueens: $arrTest")
                if (
                    currentQX == currentAX ||
                    currentQY == currentAY ||
                    slantA45 == slantQ45 ||
                    slantA135 == slantQ135
                ){
                    answer = Answer.Wrong
                    return
                }else{
                    answer = Answer.Correct
                }
            }

        }

    }

//    private fun generateForbiddenPlacesForOneQueen(index: Int) :Boolean {
//        var forbiddenCalc = ArrayList<Int>()
//
//        val x:Int = index%8
//        val y = (ceil((index.toDouble())/8)).toInt()//-1 לטובת הספירה של השורות?
//
//
//        for (j in 1..8){
//            if (j != (x)){
//                //calc values of the forbidden places - horizontal
//                val horizontal = index + j-x
//                //calc values of the forbidden places - vertical
//                val vertical = index + ((j-y)*8)
//                //add values to list of forbidden places
//                forbiddenCalc.addAll( listOf<Int>(horizontal,vertical))
//            }
//        }
//
//        //135 D
//        val p = x+y-1//סכום ההמהלכים
//        var z:Int = index -((y-1)*7) //  מיקום תחילת הריצה
//        for (l in 0..p){
//            if(index != z){
//                forbiddenCalc.add(z)
//                z+=7
//            }
//        }
////        Log.e(TAG, "generateForbiddenPlacesForOneQueen: 2 $forbiddenCalc" )
//
//        //45 D
//        var r = index-((y-1)*9)// מיקום תחילת הריצה
//        val t = y+(8-x)//סכום המהלכים
//
//        for (b in 0..t){
//            if (index!=r){
//                forbiddenCalc.add(r)
//                r += 9
//            }
//        }
//
//        return mergeAndCheck(forbiddenCalc)
//    }
//
//    private fun mergeAndCheck(forbiddenCalcList : ArrayList<Int>) :Boolean {
//        var queenCheckedPlaces = ArrayList<Int>()
//        for (i in queenChoices){
//            if (i > 0){
//                queenCheckedPlaces.add(i)
//            }
//        }
//        forbiddenCalcList.addAll(queenCheckedPlaces)
//        forbiddenCalcList.trimToSize()
//        return hasDuplicates(forbiddenCalcList.toTypedArray())
//    }
//
//    private fun hasDuplicates(arr: Array<Int>): Boolean { //return false if there is duplicates
//        //sort and check take more time. check it:)
//        return arr.size == arr.distinct().count()
//    }



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