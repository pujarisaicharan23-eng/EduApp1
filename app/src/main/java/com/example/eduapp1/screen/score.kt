package com.example.eduapp1.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eduapp1.Routes
import com.example.eduapp1.database.User
import com.example.eduapp1.viewmodel.AppViewModel

@Composable
private fun ScoreMathBackground() {
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
        Text("x + y = ?", color = Color.White.copy(alpha = 0.07f), fontSize = 30.sp, modifier = Modifier.padding(start = 24.dp, top = 100.dp))
        Text("42 ÷ 6 = 7", color = Color.White.copy(alpha = 0.06f), fontSize = 28.sp, modifier = Modifier.padding(start = 210.dp, top = 220.dp))
        Text("logic • score • rank", color = Color.White.copy(alpha = 0.05f), fontSize = 22.sp, modifier = Modifier.padding(start = 50.dp, top = 500.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreScreen(
    navController: NavHostController,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val users: List<User> by viewModel.users.collectAsState(initial = emptyList())
    val bestScore = users.maxOfOrNull { it.score } ?: 0
    val totalQuestions = viewModel.getTotalQuestions()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Score") }
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ScoreMathBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "🏆 Great Job!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Player: ${viewModel.username}", fontSize = 18.sp, color = Color.White)
                        Text("Level: ${viewModel.selectedLevel}", fontSize = 18.sp, color = Color.White)
                        Text(
                            "Score: ${viewModel.currentScore}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF59E0B)
                        )
                        Text("Duration: ${viewModel.elapsedSeconds} sec", fontSize = 18.sp, color = Color.White)
                        Text(
                            "Answered: ${viewModel.answeredQuestionsCount} / $totalQuestions",
                            fontSize = 18.sp,
                            color = Color(0xFF93C5FD)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ScoreChip("⭐ Best", bestScore.toString(), Color(0xFF22C55E))
                            ScoreChip("🎯 Level", viewModel.selectedLevel, Color(0xFF60A5FA))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            viewModel.resetForReplay()
                            navController.navigate(Routes.SETTING) {
                                popUpTo(Routes.LANDING)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3B82F6),
                            contentColor = Color.White
                        )
                    ) {
                        Text("PLAY AGAIN")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            navController.navigate(Routes.LANDING) {
                                popUpTo(Routes.LANDING) { inclusive = true }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF64748B),
                            contentColor = Color.White
                        )
                    ) {
                        Text("HOME")
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "📚 Previous Attempts",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn {
                    items(users) { user ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.12f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "👦 ${user.username}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("🎯 Level: ${user.level}", color = Color.White)
                                Text("🏅 Score: ${user.score}", color = Color.White)
                                Text("⏱ Duration: ${user.duration} sec", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreChip(
    label: String,
    value: String,
    color: Color
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.18f))
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = color
            )
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}
