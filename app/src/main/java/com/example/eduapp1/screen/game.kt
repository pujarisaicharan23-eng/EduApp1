package com.example.eduapp.screen

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.eduapp.Routes
import com.example.eduapp.helper.getBitmapFromAssetsByIndex
import com.example.eduapp.viewmodel.AppViewModel
import kotlinx.coroutines.delay

@Composable
private fun GameMathBackground() {
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
        Text(
            "5 × 11 = 55",
            color = Color.White.copy(alpha = 0.07f),
            fontSize = 28.sp,
            modifier = Modifier.padding(start = 25.dp, top = 80.dp)
        )
        Text(
            "2 + 2 = 4",
            color = Color.White.copy(alpha = 0.05f),
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 220.dp, top = 170.dp)
        )
        Text(
            "logic  •  numbers",
            color = Color.White.copy(alpha = 0.05f),
            fontSize = 22.sp,
            modifier = Modifier.padding(start = 40.dp, top = 350.dp)
        )
        Text(
            "8 16 24 32",
            color = Color.White.copy(alpha = 0.06f),
            fontSize = 30.sp,
            modifier = Modifier.padding(start = 160.dp, top = 600.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    navController: NavHostController,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val level = viewModel.selectedLevel
    val imageIndex = viewModel.getCurrentImageIndex()
    val totalQuestions = viewModel.getTotalQuestions()

    var showExitDialog by remember { mutableStateOf(false) }

    val imageBitmap = remember(level, imageIndex) {
        getBitmapFromAssetsByIndex(context, level, imageIndex)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "game_animation")

    val popScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pop_scale"
    )

    val buttonScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_scale"
    )

    val toneGenerator = remember { ToneGenerator(AudioManager.STREAM_MUSIC, 100) }

    DisposableEffect(Unit) {
        onDispose {
            toneGenerator.release()
        }
    }

    LaunchedEffect(viewModel.showResultDialog, viewModel.lastAnswerCorrect) {
        if (viewModel.showResultDialog) {
            if (viewModel.lastAnswerCorrect == true) {
                toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 200)
                delay(220)
                toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP2, 250)
            } else if (viewModel.lastAnswerCorrect == false) {
                toneGenerator.startTone(ToneGenerator.TONE_SUP_ERROR, 300)
            }
        }
    }

    LaunchedEffect(viewModel.gameFinished) {
        while (!viewModel.gameFinished) {
            delay(1000)
            viewModel.tickTimer()
        }
    }

    LaunchedEffect(viewModel.gameFinished) {
        if (viewModel.gameFinished) {
            viewModel.saveGameResult()
            navController.navigate(Routes.SCORE) {
                popUpTo(Routes.GAME) { inclusive = true }
            }
        }
    }

    if (viewModel.showResultDialog) {
        val isCorrect = viewModel.lastAnswerCorrect == true

        AlertDialog(
            onDismissRequest = { },
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isCorrect) {
                        Text(
                            text = "🎉 🎊 🎉",
                            fontSize = 28.sp,
                            modifier = Modifier.scale(popScale)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Correct Answer!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF22C55E)
                        )
                    } else {
                        Text("😔", fontSize = 34.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Wrong Answer",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444)
                        )
                    }
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (!isCorrect) {
                        Text(
                            text = "Correct answer: ${viewModel.getCurrentCorrectAnswer()}",
                            fontSize = 18.sp,
                            color = Color(0xFF2563EB),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.dismissDialogAndContinue() },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCorrect) Color(0xFF22C55E) else Color(0xFFEF4444),
                        contentColor = Color.White
                    )
                ) {
                    Text(if (viewModel.currentQuestionIndex == totalQuestions - 1) "FINISH" else "NEXT")
                }
            },
            containerColor = if (isCorrect) Color(0xFFECFDF5) else Color(0xFFFEF2F2)
        )
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Exit Game?") },
            text = { Text("Your current score will still be saved and shown.") },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        viewModel.saveGameResult()
                        navController.navigate(Routes.SCORE) {
                            popUpTo(Routes.GAME) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF4444),
                        contentColor = Color.White
                    )
                ) {
                    Text("EXIT")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("CANCEL")
                }
            }
        )
    }

    val progress = (viewModel.currentQuestionIndex + 1).toFloat() / totalQuestions.toFloat()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CharanEduApp") },
                actions = {
                    TextButton(onClick = { showExitDialog = true }) {
                        Text("Exit", color = Color.Red)
                    }
                }
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            GameMathBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.12f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Game Dashboard",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp),
                            color = Color(0xFF3B82F6),
                            trackColor = Color.White.copy(alpha = 0.2f)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Score",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                        Text(
                            text = "${viewModel.currentScore} / ${totalQuestions * 10}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF59E0B)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("🧩 Puzzle", color = Color(0xFF93C5FD), fontSize = 14.sp)
                                Text(
                                    "${viewModel.currentQuestionIndex + 1} / $totalQuestions",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("⏱ Time", color = Color(0xFF86EFAC), fontSize = 14.sp)
                                Text(
                                    "${viewModel.elapsedSeconds} sec",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("👦 Player", color = Color(0xFFFCA5A5), fontSize = 14.sp)
                                Text(
                                    viewModel.username,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("🎯 Level", color = Color(0xFFC4B5FD), fontSize = 14.sp)
                                Text(
                                    viewModel.selectedLevel,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.14f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (imageBitmap != null) {
                            Image(
                                bitmap = imageBitmap,
                                contentDescription = "Puzzle image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Text("Image not found", color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = viewModel.currentAnswer,
                    onValueChange = { viewModel.updateCurrentAnswer(it) },
                    label = {
                        Text(
                            "Enter your answer",
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White.copy(alpha = 0.05f),
                            RoundedCornerShape(18.dp)
                        ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    shape = RoundedCornerShape(18.dp),
                    textStyle = TextStyle(color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        focusedLabelColor = Color(0xFF93C5FD),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (viewModel.currentAnswer.isNotBlank()) {
                            viewModel.submitAnswer()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(buttonScale),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "CHECK",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}