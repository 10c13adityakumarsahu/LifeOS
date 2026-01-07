package com.example.lifeos.ui.finance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.animation.core.*
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.data.local.entity.GoalEntity
import com.example.lifeos.data.local.entity.TransactionEntity
import com.example.lifeos.data.local.entity.TransactionType
import com.example.lifeos.data.local.entity.RecoveryStatus
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.foundation.layout.heightIn
import com.example.lifeos.ui.AppViewModelProvider
import java.time.LocalDateTime
import com.example.lifeos.data.local.entity.PersonEntity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun FinanceScreen(
    viewModel: FinanceViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val transactions by viewModel.allTransactions.collectAsState()
    val goals by viewModel.allGoals.collectAsState()
    val people by viewModel.allPeople.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var showAddGoalDialog by remember { mutableStateOf(false) }
    var showAddPersonDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) } // 0: Recent, 1: Due & Recur, 2: Category, 3: Goals, 4: People
    
    // Custom Duration State
    var showDurationPicker by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }
    
    val recursivePayments by viewModel.recursiveTransactions.collectAsState()
    val duePayments by viewModel.duePayments.collectAsState()
    
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedTransaction by remember { mutableStateOf<TransactionEntity?>(null) }
    var selectedGoalForAmount by remember { mutableStateOf<GoalEntity?>(null) }
    
    // Export Specific State
    var showExportDurationPicker by remember { mutableStateOf(false) }
    var exportStartDate by remember { mutableStateOf(startDate) }
    var exportEndDate by remember { mutableStateOf(endDate) }
    
    val context = androidx.compose.ui.platform.LocalContext.current
    
    fun shareReport(sDate: LocalDate, eDate: LocalDate) {
        val filtered = transactions.filter { it.date.toLocalDate() in sDate..eDate }
        val totalSpent = filtered.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val report = StringBuilder("LifeOS Finance Report\n")
        report.append("Period: ${sDate} to ${eDate}\n")
        report.append("Total Spent: ₹${String.format("%.2f", totalSpent)}\n\n")
        report.append("Transactions:\n")
        filtered.sortedByDescending { it.date }.take(100).forEach { t ->
            report.append("- ${t.date.format(DateTimeFormatter.ofPattern("dd MMM"))}: ${t.description} (₹${t.amount})\n")
        }
        
        val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_SUBJECT, "Finance Statement (${sDate} to ${eDate})")
            putExtra(android.content.Intent.EXTRA_TEXT, report.toString())
        }
        context.startActivity(android.content.Intent.createChooser(intent, "Share Report"))
    }

    Scaffold(
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                 when (selectedTab) {
                      0, 1, 2 -> { // Recent, Bills, Category
                        FloatingActionButton(
                            onClick = { showAddDialog = true },
                            containerColor = if (selectedTab == 1) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                            contentColor = if (selectedTab == 1) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Add Transaction")
                        }
                      }
                      3 -> { // Goals
                        FloatingActionButton(
                            onClick = { showAddGoalDialog = true },
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        ) {
                             Icon(Icons.Filled.Savings, contentDescription = "Add Goal")
                        }
                      }
                      4 -> { // People
                        FloatingActionButton(
                            onClick = { showAddPersonDialog = true },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                             Icon(Icons.Filled.Person, contentDescription = "Add Person")
                        }
                      }
                 }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Finance",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    // Mini Period Summary moved to top corner/header
                    Row(
                        modifier = Modifier.clickable { showDurationPicker = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${startDate.format(DateTimeFormatter.ofPattern("MMM dd"))} - ${endDate.format(DateTimeFormatter.ofPattern("MMM dd"))}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(Icons.Filled.Settings, contentDescription = null, modifier = Modifier.size(12.dp).padding(start = 4.dp), tint = MaterialTheme.colorScheme.primary)
                    }
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.IconButton(onClick = { 
                        exportStartDate = startDate
                        exportEndDate = endDate
                        showExportDurationPicker = true 
                    }) {
                        Icon(Icons.Filled.Share, "Share Report", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            
            // Simplified Balance Summary
             val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
             val pendingIn = transactions.filter { it.type == TransactionType.LEND }.sumOf { it.amount }
             
             Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                 SummaryCard("Spent", totalExpense, MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer, Modifier.weight(1f))
                 SummaryCard("Lent", pendingIn, MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer, Modifier.weight(1f))
                 androidx.compose.material3.IconButton(
                     onClick = { showAddDialog = true },
                     modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                 ) {
                     Icon(Icons.Filled.Add, contentDescription = "Quick Add", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                 }
             }
             
             Spacer(modifier = Modifier.height(16.dp))

            TabRow(selectedTabIndex = selectedTab, modifier = Modifier.fillMaxWidth(), containerColor = Color.Transparent, contentColor = MaterialTheme.colorScheme.primary) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Recent", style = MaterialTheme.typography.labelSmall) })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Bills", style = MaterialTheme.typography.labelSmall) })
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Category", style = MaterialTheme.typography.labelSmall) })
                Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("Goals", style = MaterialTheme.typography.labelSmall) })
                Tab(selected = selectedTab == 4, onClick = { selectedTab = 4 }, text = { Text("People", style = MaterialTheme.typography.labelSmall) })
            }

            when (selectedTab) {
                0 -> TransactionList(
                    transactions = transactions, 
                    onTransactionClick = { selectedTransaction = it; showEditDialog = true },
                    onDurationClick = { showDurationPicker = true },
                    startDate = startDate,
                    endDate = endDate
                )
                1 -> CombinedDueAndRecursiveList(
                    recursive = recursivePayments, 
                    due = duePayments, 
                    onTransactionClick = { selectedTransaction = it; showEditDialog = true },
                    onTogglePay = { viewModel.updateTransaction(it.copy(isPaid = !it.isPaid)) }
                )
                2 -> CategoryList(transactions, startDate, endDate)
                3 -> GoalsList(goals, onAddAmount = { goal -> selectedGoalForAmount = goal })
                4 -> PeopleList(
                    people = people, 
                    transactions = transactions,
                    onSettle = { personName -> viewModel.settleAllForPerson(personName) }
                )
            }
        }

        if (showDurationPicker) {
            DateRangePickerDialog(
                start = startDate,
                end = endDate,
                onDismiss = { showDurationPicker = false },
                onConfirm = { s, e ->
                    startDate = s
                    endDate = e
                    showDurationPicker = false
                }
            )
        }

        if (showExportDurationPicker) {
            DateRangePickerDialog(
                title = "Export Statement Range",
                start = exportStartDate,
                end = exportEndDate,
                onDismiss = { showExportDurationPicker = false },
                onConfirm = { s, e ->
                    exportStartDate = s
                    exportEndDate = e
                    showExportDurationPicker = false
                    shareReport(s, e)
                }
            )
        }

        if (showAddDialog) {
            AddTransactionDialog(
                people = people,
                onDismiss = { showAddDialog = false },
                onConfirm = { transactions ->
                    transactions.forEach { viewModel.addTransaction(it) }
                    showAddDialog = false
                }
            )
        }
        
        if (showAddGoalDialog) {
             AddGoalDialog(
                 onDismiss = { showAddGoalDialog = false },
                 onConfirm = { name, target ->
                     viewModel.addGoal(name, target)
                     showAddGoalDialog = false
                 }
             )
        }
        
        if (showAddPersonDialog) {
            AddPersonDialog(
                onDismiss = { showAddPersonDialog = false },
                onConfirm = { name ->
                    viewModel.addPerson(name)
                    showAddPersonDialog = false
                }
            )
        }
        
        if (selectedGoalForAmount != null) {
            AddGoalAmountDialog(
                goalName = selectedGoalForAmount!!.name,
                onDismiss = { selectedGoalForAmount = null },
                onConfirm = { amount ->
                     viewModel.updateGoalAmount(selectedGoalForAmount!!, amount)
                     selectedGoalForAmount = null
                }
            )
        }
        
        if (showEditDialog && selectedTransaction != null) {
            // Edit Dialog would need update too, but for now focus on Add per prompt requirements.
            // I'll leave EditTransactionDialog strictly as is or update if needed. 
            // Prompt specifically mentioned "when someone adds a expense".
            EditTransactionDialog(
                transaction = selectedTransaction!!,
                onDismiss = { showEditDialog = false; selectedTransaction = null },
                onConfirm = { updated ->
                    viewModel.updateTransaction(updated)
                    showEditDialog = false
                    selectedTransaction = null
                },
                onDelete = { toDelete ->
                    viewModel.deleteTransaction(toDelete)
                    showEditDialog = false
                    selectedTransaction = null
                }
            )
        }
    }
}

@Composable
fun SummaryCard(title: String, amount: Double, bgColor: Color, contentColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelMedium, color = contentColor.copy(alpha = 0.8f))
            Text(
                "₹${String.format("%.2f", amount)}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}

@Composable
fun TransactionList(
    transactions: List<TransactionEntity>, 
    onTransactionClick: (TransactionEntity) -> Unit,
    onDurationClick: () -> Unit,
    startDate: LocalDate,
    endDate: LocalDate
) {
    val filtered = transactions.filter { it.date.toLocalDate() in startDate..endDate }
    val grouped = filtered.groupBy { it.date.format(DateTimeFormatter.ofPattern("MMMM yyyy")) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        grouped.forEach { (month, monthTransactions) ->
            item {
                Text(
                    text = month.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 2.sp
                )
            }
            
            val dateGrouped = monthTransactions.groupBy { it.date.toLocalDate() }
            dateGrouped.forEach { (date, dailyTransactions) ->
                val dailyTotal = dailyTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (date == LocalDate.now()) "TODAY" else if (date == LocalDate.now().minusDays(1)) "YESTERDAY" else date.format(DateTimeFormatter.ofPattern("EEEE, dd MMM")).uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                        if (dailyTotal > 0) {
                            Text(
                                "₹${String.format("%.0f", dailyTotal)}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
                items(items = dailyTransactions) { t ->
                    TransactionItem(t, onClick = { onTransactionClick(t) })
                }
            }
        }
    }
}

@Composable
fun CategoryList(transactions: List<TransactionEntity>, startDate: LocalDate, endDate: LocalDate) {
    val filtered = transactions.filter { it.date.toLocalDate() in startDate..endDate && it.type == TransactionType.EXPENSE }
    val categoryTotals = filtered.groupBy { it.category }.mapValues { it.value.sumOf { t -> t.amount } }.toList().sortedByDescending { it.second }
    val grandTotal = categoryTotals.sumOf { it.second }

    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Total Spending", style = MaterialTheme.typography.labelMedium)
                    Text("₹${String.format("%.2f", grandTotal)}", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Black)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("CHANNELS", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }

        items(categoryTotals, key = { it.first }) { (category, amount) ->
            val progressStep = if (grandTotal > 0) (amount / grandTotal).toFloat() else 0f
            var animatedProgress by remember { mutableStateOf(0f) }
            val progress by animateFloatAsState(
                targetValue = animatedProgress,
                animationSpec = tween(1000, easing = FastOutSlowInEasing)
            )
            
            LaunchedEffect(categoryTotals) {
                animatedProgress = progressStep
            }

            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(category, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                    Text("₹${String.format("%.0f", amount)}", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                )
                Text("${(progressStep * 100).toInt()}% contribution", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Composable
fun DateRangePickerDialog(
    start: LocalDate,
    end: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate, LocalDate) -> Unit
) {
    var tempStart by remember { mutableStateOf(start) }
    var tempEnd by remember { mutableStateOf(end) }
    val context = androidx.compose.ui.platform.LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Duration") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("From:", modifier = Modifier.width(50.dp))
                    Button(onClick = {
                        DatePickerDialog(context, { _, y, m, d -> tempStart = LocalDate.of(y, m + 1, d) }, tempStart.year, tempStart.monthValue - 1, tempStart.dayOfMonth).show()
                    }, modifier = Modifier.weight(1f)) {
                        Text(tempStart.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")))
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("To:", modifier = Modifier.width(50.dp))
                    Button(onClick = {
                        DatePickerDialog(context, { _, y, m, d -> tempEnd = LocalDate.of(y, m + 1, d) }, tempEnd.year, tempEnd.monthValue - 1, tempEnd.dayOfMonth).show()
                    }, modifier = Modifier.weight(1f)) {
                        Text(tempEnd.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")))
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(tempStart, tempEnd) }) { Text("Apply") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun CombinedDueAndRecursiveList(
    recursive: List<TransactionEntity>, 
    due: List<TransactionEntity>, 
    onTransactionClick: (TransactionEntity) -> Unit,
    onTogglePay: (TransactionEntity) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (due.any { !it.isPaid }) {
            item { Text("PENDING BILLS", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error) }
            items(due.filter { !it.isPaid }) { t ->
                BillItem(t, onTransactionClick, onTogglePay)
            }
        }
        
        if (recursive.isNotEmpty()) {
            item { Text("RECURRING PAYMENTS", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
            items(recursive) { t ->
                RecursiveItem(t, onTransactionClick)
            }
        }

        val paidBills = due.filter { it.isPaid }
        if (paidBills.isNotEmpty()) {
            item { Text("PAID BILLS", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.Gray) }
            items(paidBills) { t ->
                BillItem(t, onTransactionClick, onTogglePay)
            }
        }
    }
}

@Composable
fun BillItem(t: TransactionEntity, onTransactionClick: (TransactionEntity) -> Unit, onTogglePay: (TransactionEntity) -> Unit) {
    val now = LocalDateTime.now()
    val isOverdue = t.dueDate?.isBefore(now) == true && !t.isPaid
    val daysLeft = t.dueDate?.let { java.time.Duration.between(now, it).toDays() }
    
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onTransactionClick(t) },
        colors = CardDefaults.cardColors(
            containerColor = if (t.isPaid) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) 
                            else if (isOverdue) MaterialTheme.colorScheme.errorContainer 
                            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (t.isPaid) Icons.Filled.CheckCircle else if (isOverdue) Icons.Filled.Warning else Icons.Filled.MoneyOff,
                contentDescription = null,
                tint = if (t.isPaid) MaterialTheme.colorScheme.primary else if (isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(t.description, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                t.dueDate?.let {
                    val dateStr = it.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                    val helpText = when {
                        t.isPaid -> "Paid on time"
                        isOverdue -> "Overdue by ${-daysLeft!!} days"
                        else -> "$daysLeft days remaining"
                    }
                    Text("Due: $dateStr • $helpText", 
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isOverdue) MaterialTheme.colorScheme.error else if (t.isPaid) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("₹${t.amount}", fontWeight = FontWeight.Bold)
                Button(
                    onClick = { onTogglePay(t) }, 
                    modifier = Modifier.height(32.dp), 
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    colors = if (t.isPaid) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) else ButtonDefaults.buttonColors()
                ) {
                    Text(if (t.isPaid) "Undo" else "Pay", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun RecursiveItem(t: TransactionEntity, onTransactionClick: (TransactionEntity) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onTransactionClick(t) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.ArrowUpward, contentDescription = null, tint = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(t.description, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text("Every ${t.recurrencePattern ?: "Month"}", style = MaterialTheme.typography.bodySmall)
            }
            Text("₹${t.amount}", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PeopleList(people: List<PersonEntity>, transactions: List<TransactionEntity>, onSettle: (String) -> Unit) {
    if (people.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No people added yet.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = people) { person ->
                 // Calculate totals
                 val lent = transactions.filter { it.type == TransactionType.LEND && it.payerOrPayeeName == person.name && it.recoveryStatus == RecoveryStatus.PENDING }.sumOf { it.amount }
                 val borrowed = transactions.filter { it.type == TransactionType.BORROW && it.payerOrPayeeName == person.name && !it.isPaid }.sumOf { it.amount }
                 PersonItem(person, lent, borrowed, onSettle)
            }
        }
    }
}

@Composable
fun PersonItem(person: PersonEntity, lent: Double, borrowed: Double, onSettle: (String) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Person, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                 Text(person.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                 Row {
                    if (lent > 0) {
                        Text("Lent: ₹${String.format("%.2f", lent)} ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.tertiary)
                    }
                    if (borrowed > 0) {
                        Text("Borrowed: ₹${String.format("%.2f", borrowed)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                    }
                    if (lent == 0.0 && borrowed == 0.0) {
                        Text("Settled", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                 }
            }
            
            if (lent > 0 || borrowed > 0) {
                Button(
                    onClick = { onSettle(person.name) },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Settle", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun GoalsList(goals: List<GoalEntity>, onAddAmount: (GoalEntity) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = goals) { goal ->
             GoalItem(goal, onAddAmount)
        }
    }
}

@Composable
fun GoalItem(goal: GoalEntity, onAddAmount: (GoalEntity) -> Unit) {
    val progress = (goal.currentAmount / goal.targetAmount).toFloat().coerceIn(0f, 1f)
    
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(goal.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
               Text("₹${goal.currentAmount} / ₹${goal.targetAmount}", style = MaterialTheme.typography.bodySmall)
               TextButton(onClick = { onAddAmount(goal) }) {
                   Text("+ Add Funds")
               }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionEntity, onClick: () -> Unit) {
    val icon = when (transaction.type) {
        TransactionType.EXPENSE -> Icons.Filled.ArrowUpward
        TransactionType.LEND -> Icons.Filled.Person // Money leaving
        TransactionType.BORROW -> Icons.Filled.ArrowDownward // Money entering
        TransactionType.INCOME -> Icons.Filled.ArrowDownward
    }
    
    val color = when (transaction.type) {
        TransactionType.EXPENSE -> MaterialTheme.colorScheme.error
        TransactionType.LEND -> MaterialTheme.colorScheme.tertiary
        TransactionType.BORROW -> MaterialTheme.colorScheme.primary
        TransactionType.INCOME -> MaterialTheme.colorScheme.primary // Green/Primary for income? Using primary for now
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.1f))
                    .padding(8.dp)
            ) {
                Icon(icon, contentDescription = null, tint = color)
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(transaction.description, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                if (!transaction.payerOrPayeeName.isNullOrEmpty()) {
                     Text("Person: ${transaction.payerOrPayeeName}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                }
                Text(
                     if (transaction.category != "General") transaction.category else transaction.date.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    transaction.date.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
            
            Text(
                "₹${String.format("%.2f", transaction.amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (transaction.type == TransactionType.BORROW) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    people: List<PersonEntity>,
    onDismiss: () -> Unit,
    onConfirm: (List<TransactionEntity>) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("General") }
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }
    var selectedPerson by remember { mutableStateOf<String?>(null) }
    
    // Split Logic
    var isSplit by remember { mutableStateOf(false) }
    val splitPeople = remember { mutableStateListOf<PersonEntity>() }
    var includeSelf by remember { mutableStateOf(true) }
    
    // Recursive / Bill
    var isRecursive by remember { mutableStateOf(false) }
    var recurrencePattern by remember { mutableStateOf("Monthly") }
    var isBill by remember { mutableStateOf(false) }
    var dueDate by remember { mutableStateOf(LocalDate.now().plusMonths(1)) }
    
    // Date Time
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    
    val context = androidx.compose.ui.platform.LocalContext.current
    
    val expenseCategories = listOf("Food", "Transport", "Rent", "Utilities", "Entertainment", "Health", "Education", "Shopping", "Others", "Salary")
    
    // Dropdown states
    var categoryExpanded by remember { mutableStateOf(false) }
    var personExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Transaction") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                // Type Selector
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    TransactionType.values().forEach { t ->
                        val selected = type == t
                        Card(
                            colors = CardDefaults.cardColors(containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
                            modifier = Modifier.clickable { type = t }
                        ) {
                            Text(
                                t.name.lowercase().replaceFirstChar { it.uppercase() },
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                                color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = amount,
                    onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    singleLine = true
                )
                
                // Split Checkbox
                if (type == TransactionType.EXPENSE) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { isSplit = !isSplit }) {
                        Checkbox(checked = isSplit, onCheckedChange = { isSplit = it })
                        Text("Split Expense")
                    }
                }
                
                if (isSplit && type == TransactionType.EXPENSE) {
                    Text("Select people to split with:", style = MaterialTheme.typography.labelMedium)
                    LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) {
                        item {
                             Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { includeSelf = !includeSelf }) {
                                Checkbox(checked = includeSelf, onCheckedChange = { includeSelf = it })
                                Text("Me")
                            }
                        }
                        items(people) { person ->
                             Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { 
                                 if (splitPeople.contains(person)) splitPeople.remove(person) else splitPeople.add(person)
                             }) {
                                Checkbox(checked = splitPeople.contains(person), onCheckedChange = {
                                    if (it) splitPeople.add(person) else splitPeople.remove(person)
                                })
                                Text(person.name)
                            }
                        }
                    }
                } else {
                    // Person Dropdown
                    ExposedDropdownMenuBox(
                        expanded = personExpanded,
                        onExpandedChange = { personExpanded = !personExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedPerson ?: "Select Person (Optional)",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = personExpanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = personExpanded,
                            onDismissRequest = { personExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("None") },
                                onClick = { selectedPerson = null; personExpanded = false }
                            )
                            people.forEach { person ->
                                DropdownMenuItem(
                                    text = { Text(person.name) },
                                    onClick = { selectedPerson = person.name; personExpanded = false }
                                )
                            }
                        }
                    }
                }
                
                // Category Dropdown
                 ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        expenseCategories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = { category = cat; categoryExpanded = false }
                            )
                        }
                    }
                }

                // Date Time Buttons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        DatePickerDialog(context, { _, y, m, d -> selectedDate = LocalDate.of(y, m + 1, d) }, selectedDate.year, selectedDate.monthValue - 1, selectedDate.dayOfMonth).show()
                    }, modifier = Modifier.weight(1f)) {
                         Text(selectedDate.format(DateTimeFormatter.ofPattern("MMM dd")))
                    }
                    Button(onClick = {
                         TimePickerDialog(context, { _, h, m -> selectedTime = LocalTime.of(h, m) }, selectedTime.hour, selectedTime.minute, false).show()
                    }, modifier = Modifier.weight(1f)) {
                         Text(selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")))
                    }
                }

                // Recursive and Bill Toggles
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Checkbox(checked = isRecursive, onCheckedChange = { isRecursive = it })
                    Text("Recursive Payment", style = MaterialTheme.typography.bodyMedium)
                }
                
                if (isRecursive) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        listOf("Daily", "Weekly", "Monthly").forEach { p ->
                            androidx.compose.material3.FilterChip(
                                selected = recurrencePattern == p,
                                onClick = { recurrencePattern = p },
                                label = { Text(p) }
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Checkbox(checked = isBill, onCheckedChange = { isBill = it })
                    Text("Mark as Bill (Due Payment)", style = MaterialTheme.typography.bodyMedium)
                }

                if (isBill) {
                    Button(onClick = {
                        DatePickerDialog(context, { _, y, m, d -> dueDate = LocalDate.of(y, m + 1, d) }, dueDate.year, dueDate.monthValue - 1, dueDate.dayOfMonth).show()
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Due Date: ${dueDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountVal = amount.toDoubleOrNull()
                    if (amountVal != null && description.isNotBlank()) {
                         val transactions = mutableListOf<TransactionEntity>()
                        
                        if (isSplit && type == TransactionType.EXPENSE) {
                             val count = (if (includeSelf) 1 else 0) + splitPeople.size
                             if (count > 0 && amountVal > 0) {
                                 val splitAmount = amountVal / count
                                 
                                 if (includeSelf) {
                                     transactions.add(TransactionEntity(
                                         amount = splitAmount,
                                         description = description,
                                         type = TransactionType.EXPENSE,
                                         category = category,
                                         date = selectedDate.atTime(selectedTime),
                                         payerOrPayeeName = null,
                                         isRecursive = isRecursive,
                                         recurrencePattern = if(isRecursive) recurrencePattern else null,
                                         isBill = isBill,
                                         dueDate = if (isBill) dueDate.atStartOfDay() else null
                                     ))
                                 }
                                 
                                 splitPeople.forEach { p ->
                                     transactions.add(TransactionEntity(
                                         amount = splitAmount,
                                         description = "$description (Split)",
                                         type = TransactionType.LEND,
                                         category = category,
                                         date = selectedDate.atTime(selectedTime),
                                         payerOrPayeeName = p.name,
                                         isRecoverable = true,
                                         recoveryStatus = RecoveryStatus.PENDING,
                                         isRecursive = isRecursive,
                                         recurrencePattern = if(isRecursive) recurrencePattern else null,
                                         isBill = isBill,
                                         dueDate = if (isBill) dueDate.atStartOfDay() else null
                                     ))
                                 }
                             }
                        } else {
                             transactions.add(TransactionEntity(
                                 amount = amountVal,
                                 description = description,
                                 type = type,
                                 category = category,
                                 date = selectedDate.atTime(selectedTime),
                                 payerOrPayeeName = selectedPerson,
                                 isRecursive = isRecursive,
                                 recurrencePattern = if(isRecursive) recurrencePattern else null,
                                 isBill = isBill,
                                 dueDate = if (isBill) dueDate.atStartOfDay() else null
                             ))
                        }
                        
                        onConfirm(transactions)
                    }
                }
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun EditTransactionDialog(
    transaction: TransactionEntity,
    onDismiss: () -> Unit,
    onConfirm: (TransactionEntity) -> Unit,
    onDelete: (TransactionEntity) -> Unit
) {
    var amount by remember { mutableStateOf(transaction.amount.toString()) }
    var description by remember { mutableStateOf(transaction.description) }
    var category by remember { mutableStateOf(transaction.category) }
    var type by remember { mutableStateOf(transaction.type) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Transaction") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                // Type Selector
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    TransactionType.values().forEach { t ->
                        val selected = type == t
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier.clickable { type = t }
                        ) {
                            Text(
                                t.name.lowercase().replaceFirstChar { it.uppercase() },
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                                color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = amount,
                    onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description/Person") },
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    singleLine = true
                )

                // Add Recursive/Bill status in edit
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = transaction.isRecursive, onCheckedChange = { /* simplified: read only in edit for now or full implementation */ })
                    Text("Recursive Payment", style = MaterialTheme.typography.bodyMedium)
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = transaction.isBill, onCheckedChange = { /* simplified */ })
                    Text("Bill (Due Payment)", style = MaterialTheme.typography.bodyMedium)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountVal = amount.toDoubleOrNull()
                    if (amountVal != null && description.isNotBlank()) {
                        onConfirm(transaction.copy(amount = amountVal, description = description, type = type, category = category))
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = { onDelete(transaction) }, colors = androidx.compose.material3.ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
                     Text("Delete")
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}

@Composable
fun AddGoalDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Financial Goal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Goal Name") }
                )
                OutlinedTextField(
                    value = target,
                    onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) target = it },
                    label = { Text("Target Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val t = target.toDoubleOrNull()
                if (name.isNotBlank() && t != null) {
                    onConfirm(name, t)
                }
            }) { Text("Create") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun AddGoalAmountDialog(
    goalName: String,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add to $goalName") },
        text = {
            OutlinedTextField(
                value = amount,
                onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) amount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = {
                val a = amount.toDoubleOrNull()
                if (a != null) {
                    onConfirm(a)
                }
            }) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun AddPersonDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Person") },
        text = {
            OutlinedTextField(
                value = name, 
                onValueChange = { name = it }, 
                label = { Text("Name") }, 
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onConfirm(name) }) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}@Composable
fun DateRangePickerDialog(
    title: String = "Select Date Range",
    start: LocalDate,
    end: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate, LocalDate) -> Unit
) {
    var tempStart by remember { mutableStateOf(start) }
    var tempEnd by remember { mutableStateOf(end) }
    val context = androidx.compose.ui.platform.LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = {
                        val picker = android.app.DatePickerDialog(context, { _, y, m, d ->
                            tempStart = LocalDate.of(y, m + 1, d)
                        }, tempStart.year, tempStart.monthValue - 1, tempStart.dayOfMonth)
                        picker.show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Start: ${tempStart.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}")
                }

                Button(
                    onClick = {
                        val picker = android.app.DatePickerDialog(context, { _, y, m, d ->
                            tempEnd = LocalDate.of(y, m + 1, d)
                        }, tempEnd.year, tempEnd.monthValue - 1, tempEnd.dayOfMonth)
                        picker.show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("End: ${tempEnd.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}")
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(tempStart, tempEnd) }) { Text("Set Range") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
