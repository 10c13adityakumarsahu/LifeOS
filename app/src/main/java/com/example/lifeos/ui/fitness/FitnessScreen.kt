package com.example.lifeos.ui.fitness

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.ui.AppViewModelProvider
import java.time.format.DateTimeFormatter

@Composable
fun FitnessScreen(
    viewModel: FitnessViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val todaysLogs by viewModel.todaysLogs.collectAsState()
    
    // Calculate total water (assuming all logs are water for MVP)
    val totalWater = todaysLogs.sumOf { it.value }
    val goal = 2500.0
    val progress = (totalWater / goal).toFloat().coerceIn(0f, 1f)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Fitness & Health", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Water Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.LocalDrink, 
                            contentDescription = null, 
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Hydration", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                            Text(
                                "${totalWater.toInt()} / ${goal.toInt()} ml",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    LinearProgressIndicator(
                        progress = progress, 
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        QuickAddButton(150, viewModel)
                        QuickAddButton(250, viewModel)
                        QuickAddButton(500, viewModel)
                    }
                }
            }
        }
        
        item {
            Text("Today's History", style = MaterialTheme.typography.titleMedium)
        }
        
        items(items = todaysLogs) { log ->
             Row(
                 modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                 verticalAlignment = Alignment.CenterVertically
             ) {
                 Box(
                     modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                 )
                 Spacer(modifier = Modifier.width(16.dp))
                 Text("Drank water", modifier = Modifier.weight(1f))
                 Text(
                     "${log.value.toInt()} ml",
                     fontWeight = FontWeight.Bold
                 )
                 Spacer(modifier = Modifier.width(8.dp))
                 Text(
                     log.timestamp.format(DateTimeFormatter.ofPattern("HH:mm")),
                     style = MaterialTheme.typography.labelMedium,
                     color = MaterialTheme.colorScheme.onSurfaceVariant
                 )
             }
        }
    }
}

@Composable
fun QuickAddButton(amount: Int, viewModel: FitnessViewModel) {
    OutlinedButton(
        onClick = { viewModel.logWater(amount.toDouble()) },
        shape = RoundedCornerShape(12.dp)
    ) {
        Text("+$amount ml")
    }
}
