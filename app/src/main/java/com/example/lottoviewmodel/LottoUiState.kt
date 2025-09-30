package com.example.lottoviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class LottoUiState(
    val selecteds: Set<Int> = emptySet(),
    val winning: Set<Int>?  = null,
    val score: Int = 0,
    val canCheck: Boolean = false
)

class LottoViewModel: ViewModel() {
    private val _uiState = MutableLiveData(LottoUiState())
    val uiState: LiveData<LottoUiState> = _uiState

    fun onNumberTapped(selected: Int){
        val current = _uiState.value ?: LottoUiState()
        val nextSelecteds = if (selected in current.selecteds){
            current.selecteds - selected
        } else {
            if (current.selecteds.size >= 7) current.selecteds else current.selecteds + selected
        }
        _uiState.value = current.copy(
            selecteds = nextSelecteds,
            canCheck = nextSelecteds.size == 7,

            winning = if (nextSelecteds.size == 7) current.winning else null,
            score = if ( nextSelecteds.size == 7) current.score else 0
        )
    }

    fun check(){
        val current = _uiState.value ?: return
        if (current.selecteds.size != 7) return

        viewModelScope.launch {
            val win = (1..40).shuffled().take(7).toSet()
            val hits = current.selecteds.intersect(win).size
            _uiState.value = current.copy(winning = win, score = hits)
        }
    }

    fun reset(){
        _uiState.value = LottoUiState()
    }
}