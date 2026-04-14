package com.example.eduapp1.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eduapp1.Routes
import com.example.eduapp1.viewmodel.AppViewModel

@Composable
private fun PremiumMathBackground() {
    val bgBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F172A),
            Color(0xFF1E293B),
            Color(0xFF312E81)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
    ) {
        Text("9 + 7 = 16", color = Color.White.copy(alpha = 0.08f), fontSize = 28.sp, modifier = Modifier.padding(start = 30.dp, top = 90.dp))
        Text("4² = 16", color = Color.White.copy(alpha = 0.06f), fontSize = 32.sp, modifier = Modifier.padding(start = 220.dp, top = 180.dp))
        Text("pattern • shape • logic", color = Color.White.copy(alpha = 0.05f), fontSize = 22.sp, modifier = Modifier.padding(start = 40.dp, top = 320.dp))
        Text("11  22  33", color = Color.White.copy(alpha = 0.06f), fontSize = 30.sp, modifier = Modifier.padding(start = 170.dp, top = 520.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavHostController,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Select Level") })
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PremiumMathBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.12f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Choose Difficulty",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Current Level: ${viewModel.selectedLevel}",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Row {
                            listOf("1", "2", "3").forEachIndexed { index, level ->
                                Button(
                                    onClick = { viewModel.updateLevel(level) },
                                    shape = RoundedCornerShape(18.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (viewModel.selectedLevel == level)
                                            Color(0xFF3B82F6) else Color(0xFF64748B),
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(level, fontWeight = FontWeight.Bold)
                                }

                                if (index != 2) {
                                    Spacer(modifier = Modifier.width(12.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        Button(
                            onClick = {
                                viewModel.startNewGame()
                                navController.navigate(Routes.GAME)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF22C55E),
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "START GAME",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
