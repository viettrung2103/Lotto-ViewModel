package com.example.lottoviewmodel

import android.R.attr.onClick
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lottoviewmodel.ui.theme.LottoViewModelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LottoViewModelTheme {
                Surface (){
                    LottoLayout()
                }
            }
        }
    }
}

@Composable
fun LottoLayout(vm: LottoViewModel = viewModel()){
    val ui by vm.uiState.observeAsState(LottoUiState())

    Spacer(modifier = Modifier.padding(32.dp))
    Column (
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize()
    ){
        NumberGrid(
            numbers = (1..40).toList(),
        selecteds = ui.selecteds,
        onClick = vm::onNumberTapped,
            modifier = Modifier
//                .weight(1f)
                .fillMaxWidth())
        Spacer(Modifier.height(12.dp))

        Button(
            enabled = ui.selecteds.size == 7,
            onClick = vm::check,
            modifier = Modifier.height(32.dp)

        ) {

            Text("Check result")
        }

        if (ui.selecteds.size >0){
            Log.d("QWERTY", "click ${ui.selecteds.sorted().joinToString(" ")}")

            Text(
                text="Selected: ${ui.selecteds.sorted().joinToString(" ")}",
                style = MaterialTheme.typography.bodyMedium)
        }
        Text(
            text="Winning numbers: ${ui.winning?.sorted()?.joinToString (" ") ?: "-"} ",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Score: ${ui.score} out of 7",
            style = MaterialTheme.typography.bodyMedium
        )

        if (ui.selecteds.size== 7){
            if (ui.score == 7){
                Text("you win")
            } else {
                Text("Try again")
            }
            Button(
                enabled = ui.selecteds.size == 7,
                onClick = vm::reset,
                modifier = Modifier.height(32.dp)

            ) {

                Text("Reset")
            }

        }

    }
}

@Composable
fun NumberGrid(
    numbers: List<Int>,
    selecteds: Set<Int>,
    onClick: (Int)-> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(10),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
    ) {
        items(numbers){  num ->
            Box(
                modifier = Modifier.width(54.dp)
                    .height(44.dp),
                contentAlignment = Alignment.Center
            ){
                FilterChip( // Material3 chip works great for selectable “pills”
                    selected = num in selecteds,
                    onClick = { onClick(num) },
                    modifier= Modifier
                        .size(44.dp),
                    label = {
                        Text(
//                            maxLines = 1,
                            text = num.toString(),
//                            textAlign = TextAlign.Left,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )
            }


        }
    }
}