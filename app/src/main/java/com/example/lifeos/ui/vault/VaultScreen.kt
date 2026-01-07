package com.example.lifeos.ui.vault

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.lifeos.data.local.entity.VaultItemEntity
import com.example.lifeos.data.local.entity.VaultItemType
import com.example.lifeos.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultScreen(
    onNavigateBack: () -> Unit,
    viewModel: VaultViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val items by viewModel.allItems.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showChangePin by remember { mutableStateOf(false) }
    var expandedImageFile by remember { mutableStateOf<java.io.File?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current

    if (expandedImageFile != null) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { expandedImageFile = null },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                 coil.compose.AsyncImage(
                     model = expandedImageFile,
                     contentDescription = "Expanded Image",
                     modifier = Modifier.fillMaxSize(),
                     contentScale = androidx.compose.ui.layout.ContentScale.Fit
                 )
                 IconButton(
                     onClick = { expandedImageFile = null },
                     modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                 ) {
                     Icon(Icons.Filled.Close, "Close", tint = Color.White)
                 }
            }
        }
    }

    if (showChangePin) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showChangePin = false },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Box {
                    com.example.lifeos.ui.auth.PinLockScreen(
                        isSettingUp = true,
                        onPinCorrect = {},
                        onPinSet = { newPin ->
                            viewModel.updatePin(newPin)
                            showChangePin = false
                            android.widget.Toast.makeText(context, "PIN Updated", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    )
                    IconButton(
                        onClick = { showChangePin = false },
                        modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
                    ) {
                        Icon(Icons.Filled.Close, "Close")
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Secure Vault") },
                navigationIcon = {
                   // Back button or similar could go here
                },
                actions = {
                    IconButton(onClick = { showChangePin = true }) {
                        Icon(Icons.Filled.Lock, "Change PIN")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, "Add Item")
            }
        }
    ) { innerPadding ->
        if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Vault is empty. Add secure notes.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { item ->
                    VaultItemCard(
                        item, 
                        onDelete = { viewModel.deleteItem(item) },
                        onImageClick = { file -> expandedImageFile = file }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddVaultItemDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, content, typeIdx, uri ->
                if (typeIdx == 1 && uri != null) {
                    try {
                        val inputStream = context.contentResolver.openInputStream(uri)
                        val fileName = "vault_${System.currentTimeMillis()}.jpg" // Simple naming
                        val file = java.io.File(context.filesDir, fileName)
                        inputStream?.use { input ->
                            file.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                        viewModel.addItem(title, file.absolutePath, VaultItemType.IMAGE)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        android.widget.Toast.makeText(context, "Failed to save image", android.widget.Toast.LENGTH_SHORT).show()
                    }
                } else {
                    viewModel.addItem(title, content, VaultItemType.TEXT)
                }
                showAddDialog = false
            }
        )
    }
}

@Composable
fun VaultItemCard(item: VaultItemEntity, onDelete: () -> Unit, onImageClick: (java.io.File) -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (item.type == VaultItemType.TEXT) Icons.Filled.Lock else Icons.Filled.Image,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    if (item.type == VaultItemType.TEXT && item.content.isNotBlank()) {
                        Text(item.content, style = MaterialTheme.typography.bodyMedium, maxLines = 3)
                    }
                    Text(
                        if (item.type == VaultItemType.TEXT) "Secure Note" else "Secure Image", 
                        style = MaterialTheme.typography.labelSmall, 
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
            // If image, show preview
            if (item.type == VaultItemType.IMAGE) {
                val file = java.io.File(item.content)
                if (file.exists()) {
                     coil.compose.AsyncImage(
                          model = file,
                          contentDescription = "Vault Image",
                          modifier = Modifier
                              .fillMaxWidth()
                              .height(200.dp)
                              .padding(top = 8.dp)
                              .clickable { onImageClick(file) },
                          contentScale = androidx.compose.ui.layout.ContentScale.Crop
                      )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVaultItemDialog(onDismiss: () -> Unit, onConfirm: (String, String, Int, android.net.Uri?) -> Unit) { 
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var typeIndex by remember { mutableStateOf(0) } // 0: Text, 1: Image
    var selectedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    
    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        selectedImageUri = uri
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add to Vault") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                // Type Selector
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = typeIndex == 0,
                        onClick = { typeIndex = 0 },
                        label = { Text("Text") },
                        leadingIcon = { Icon(Icons.Filled.Lock, null) }
                    )
                    FilterChip(
                        selected = typeIndex == 1,
                        onClick = { typeIndex = 1 },
                        label = { Text("Image") },
                        leadingIcon = { Icon(Icons.Filled.Image, null) }
                    )
                }

                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                
                if (typeIndex == 0) {
                    OutlinedTextField(
                        value = content, 
                        onValueChange = { content = it }, 
                        label = { Text("Secret Content") },
                        minLines = 3
                    )
                } else {
                   Button(onClick = { launcher.launch("image/*") }) {
                       Text(if (selectedImageUri == null) "Select Image" else "Image Selected")
                   }
                   if (selectedImageUri != null) {
                       Text("Image ready to save to Vault", style = MaterialTheme.typography.bodySmall)
                   }
                }
            }
        },
        confirmButton = {
            Button(onClick = { 
                if (title.isNotBlank()) {
                     onConfirm(title, content, typeIndex, selectedImageUri)
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
