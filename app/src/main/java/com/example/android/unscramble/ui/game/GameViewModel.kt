package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.ArrayList

class GameViewModel : ViewModel() {
    private var _score = 0
    private var _currentWordCount = 0
    private lateinit var _currentScrambledWord : String
    private lateinit var _currentWord : String
    private val rand = java.util.Random()
    private val words = ArrayList<String>()

    init {
        Log.d("GameFragment", "GaveViewModel created")
        words.addAll(allWordsList)
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel clear")
    }

    private fun increaseScore() {
        _score += SCORE_INCREASE
    }

    fun isUserWordCorrnect(w : String) : Boolean {
        if(w.equals(_currentWord, true)) {
            increaseScore()
            return true;
        }
        return false
    }

    val score : Int get() = _score
    val currentWordCount : Int get() = _currentWordCount
    val currentScrambledWord : String get() = _currentScrambledWord

    fun reinit() {
        _score = 0
        _currentWordCount = 0
        words.clear()
        words.addAll(allWordsList)
    }
    fun getNextWord() {
        val str = words[rand.nextInt(words.size)]
        words.remove(str)
        while(true) {
            val ls = ArrayList<Char>(str.toList())
            val sb = StringBuilder()
            while (ls.size > 0) {
                val index = rand.nextInt(ls.size)
                sb.append(ls[index])
                ls.removeAt(index)
            }
            val vv = sb.toString()
            if(vv == str) continue
            _currentScrambledWord = sb.toString()
            _currentWord = str
            break
        }
        ++_currentWordCount
    }

    fun nextWord():Boolean {
        return if(currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }
}