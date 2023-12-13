package com.example.android.unscramble.ui.game

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.ArrayList

class GameViewModel : ViewModel() {
    private var _score = MutableLiveData(0)
    private var _currentWordCount = MutableLiveData(0)
    private val _currentScrambledWord = MutableLiveData("")
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
        _score.value = _score.value!! + SCORE_INCREASE
    }

    fun isUserWordCorrnect(w : String) : Boolean {
        Log.i(null, "$w $_currentWord")
        if(w.equals(_currentWord, true)) {
            increaseScore()
            return true;
        }
        return false
    }

    val score : LiveData<Int> get() = _score
    val currentWordCount : LiveData<Int> get() = _currentWordCount

    fun reinit() {
        _score.value = 0
        _currentWordCount.value = 0
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
            _currentScrambledWord.value = sb.toString()
            _currentWord = str
            break
        }
        _currentWordCount.value = _currentWordCount.value!! + 1
    }

    fun nextWord():Boolean {
        return if(currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    val currentScrambledWord: LiveData<Spannable> = Transformations.map(_currentScrambledWord) {
        if (it == null) {
            SpannableString("")
        } else {
            val scrambledWord = it.toString()
            val spannable: Spannable = SpannableString(scrambledWord)
            spannable.setSpan(
                TtsSpan.VerbatimBuilder(scrambledWord).build(),
                0,
                scrambledWord.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable
        }
    }

}