package com.example.trashlearn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trashlearn.ui.theme.TrashLearnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrashLearnTheme {
                TrashLearnApp()
            }
        }
    }
}

enum class Screen {
    Dashboard, Catalog, Guide, Quiz
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashLearnApp() {
    var currentScreen by remember { mutableStateOf(Screen.Dashboard) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Delete, 
                            contentDescription = null, 
                            tint = Color.White,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Trash Learn", fontWeight = FontWeight.ExtraBold)
                    }
                },
                navigationIcon = {
                    if (currentScreen != Screen.Dashboard) {
                        IconButton(onClick = { currentScreen = Screen.Dashboard }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color.White)) {
            androidx.compose.animation.AnimatedVisibility(
                visible = true,
                enter = androidx.compose.animation.fadeIn(),
                exit = androidx.compose.animation.fadeOut()
            ) {
                when (currentScreen) {
                    Screen.Dashboard -> DashboardScreen(onNavigate = { currentScreen = it })
                    Screen.Catalog -> CatalogScreen()
                    Screen.Guide -> GuideScreen()
                    Screen.Quiz -> QuizScreen(onBack = { currentScreen = Screen.Dashboard })
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(onNavigate: (Screen) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        // Trash Can Logo
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Logo Trash Can",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Belajar Kelola Sampah",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Mari menjaga bumi kita tetap bersih",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
        )
        
        MenuCard("Katalog Sampah", Icons.Default.Category, "Kenali jenis-jenis sampah") {
            onNavigate(Screen.Catalog)
        }
        Spacer(modifier = Modifier.height(16.dp))
        MenuCard("Panduan Daur Ulang", Icons.Default.AutoFixHigh, "Cara mengolah sampah") {
            onNavigate(Screen.Guide)
        }
        Spacer(modifier = Modifier.height(16.dp))
        MenuCard("Kuis Interaktif", Icons.Default.Quiz, "Uji pengetahuanmu!") {
            onNavigate(Screen.Quiz)
        }
    }
}

@Composable
fun MenuCard(title: String, icon: ImageVector, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon, 
                    contentDescription = null, 
                    modifier = Modifier.size(28.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = description, fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White)
        }
    }
}

@Composable
fun CatalogScreen() {
    val trashTypes = listOf(
        "Organik" to "Sampah yang berasal dari sisa makhluk hidup dan mudah membusuk secara alami.\n\nContoh: Sisa makanan, kulit buah, daun kering, tulang ikan, dan ampas kopi.",
        "Anorganik" to "Sampah yang dihasilkan dari proses teknologi dan sangat sulit terurai secara alami.\n\nContoh: Botol plastik, tas kresek, styrofoam, kaleng minuman, dan sedotan plastik.",
        "B3 (Berbahaya)" to "Sampah yang mengandung zat beracun, mudah meledak, atau menyala yang berbahaya bagi kesehatan.\n\nContoh: Baterai, lampu neon, botol pestisida, termometer merkuri, dan sisa oli.",
        "Kertas" to "Sampah berbahan dasar serat kayu yang dapat didaur ulang menjadi bubur kertas kembali.\n\nContoh: Majalah, koran, buku bekas, kardus, dan kertas HVS.",
        "Logam" to "Sampah benda berbahan besi atau aluminium yang memiliki nilai ekonomi tinggi untuk dilebur kembali.\n\nContoh: Kaleng soda, tutup botol logam, kawat, paku, dan kerangka besi.",
        "Tekstil" to "Sampah berupa potongan kain atau pakaian yang sudah tidak layak pakai lagi.\n\nContoh: Pakaian bekas, kain perca, tas kain rusak, dan gorden lama.",
        "Kaca" to "Sampah berbahan kaca yang transparan atau berwarna. Bisa didaur ulang tanpa menurunkan kualitasnya.\n\nContoh: Botol sirup, toples kaca, gelas pecah, dan botol parfum."
    )

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item { 
            Text(
                "Jenis-Jenis Sampah", 
                fontWeight = FontWeight.ExtraBold, 
                fontSize = 24.sp, 
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            ) 
        }
        items(trashTypes) { (type, desc) ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = type, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = desc, color = Color.White.copy(alpha = 0.9f))
                }
            }
        }
    }
}

@Composable
fun GuideScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text(
                "Panduan Daur Ulang", 
                fontWeight = FontWeight.ExtraBold, 
                fontSize = 24.sp, 
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
        
        item {
            GuideSection("🏠 Tips Rumah Tangga", 
                "Pilah sampah di dapur menjadi dua wadah: Hijau (Organik) dan Kuning (Anorganik). Gunakan sisa sayuran untuk kompos.")
        }
        
        item {
            GuideSection("🎓 Tips Pelajar", 
                "Kurangi botol plastik sekali pakai. Kumpulkan kertas bekas dan kardus untuk disetorkan ke bank sampah.")
        }

        item {
            GuideSection("🏢 Tips di Kantor", 
                "Gunakan mug sendiri daripada gelas plastik. Kurangi penggunaan kertas dengan beralih ke dokumen digital.")
        }

        item {
            GuideSection("🍃 Membuat Kompos", 
                "Cincang sampah organik, campur dengan tanah, dan biarkan dalam wadah tertutup. Aduk seminggu sekali hingga jadi tanah hitam.")
        }

        item {
            GuideSection("💰 Bank Sampah", 
                "Kumpulkan sampah anorganik yang bernilai (botol, kaleng, kertas) dan bawa ke bank sampah terdekat untuk ditukar uang.")
        }
        
        item {
            GuideSection("🛠️ Langkah Umum", 
                "1. Pisahkan sampah sesuai jenis.\n2. Bersihkan wadah anorganik.\n3. Olah organik jadi kompos.\n4. Kreasikan plastik jadi barang berguna.")
        }
    }
}

@Composable
fun GuideSection(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = content, color = Color.White.copy(alpha = 0.9f))
        }
    }
}

@Composable
fun QuizScreen(onBack: () -> Unit) {
    var questionIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    
    val questions = listOf(
        QuizData("Botol plastik termasuk jenis sampah?", listOf("Organik", "Anorganik", "B3"), 1),
        QuizData("Sampah daun kering sebaiknya?", listOf("Dibakar", "Dikompos", "Dibuang ke sungai"), 1),
        QuizData("Baterai bekas termasuk sampah?", listOf("Anorganik", "B3", "Organik"), 1),
        QuizData("Warna tempat sampah untuk organik adalah?", listOf("Kuning", "Merah", "Hijau"), 2),
        QuizData("Apa itu 3R?", listOf("Reduce, Reuse, Recycle", "Read, Run, Ride", "Remove, Relocate, Rebuild"), 0),
        QuizData("Kaleng minuman bekas termasuk?", listOf("Organik", "Anorganik", "Kertas"), 1),
        QuizData("Sampah yang bisa membusuk disebut?", listOf("Anorganik", "B3", "Organik"), 2),
        QuizData("Kardus bekas sebaiknya...", listOf("Dibuang saja", "Dibakar", "Didaur ulang"), 2),
        QuizData("Plastik membutuhkan waktu berapa lama untuk terurai?", listOf("1 tahun", "Ratusan tahun", "1 bulan"), 1),
        QuizData("Menggunakan tas belanja kain adalah contoh dari?", listOf("Recycle", "Reduce", "Reuse"), 1)
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (questionIndex < questions.size) {
            LinearProgressIndicator(
                progress = { (questionIndex + 1).toFloat() / questions.size },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color.LightGray
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            Text("Pertanyaan ${questionIndex + 1} dari ${questions.size}", color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                questions[questionIndex].question, 
                fontSize = 22.sp, 
                fontWeight = FontWeight.Bold, 
                textAlign = TextAlign.Center,
                lineHeight = 28.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            questions[questionIndex].options.forEachIndexed { index, answer ->
                Button(
                    onClick = { 
                        if (index == questions[questionIndex].correctIndex) score++
                        questionIndex++ 
                    }, 
                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(answer, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Icon(
                Icons.Default.CheckCircle, 
                contentDescription = null, 
                modifier = Modifier.size(80.dp), 
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Kuis Selesai!", fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = MaterialTheme.colorScheme.primary)
            Text("Skor kamu: $score / ${questions.size}", fontSize = 20.sp, modifier = Modifier.padding(top = 8.dp))
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { 
                    questionIndex = 0
                    score = 0
                }, 
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Ulangi Kuis")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text("Kembali ke Dashboard", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

data class QuizData(val question: String, val options: List<String>, val correctIndex: Int)
