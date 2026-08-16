package ru.igni.manager.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.room.withTransaction
import ru.igni.manager.IgniApplication
import ru.igni.manager.data.local.ActiveTableSessionEntity
import ru.igni.manager.data.local.FavoriteMixEntity
import ru.igni.manager.data.local.FlavorWithStock
import ru.igni.manager.data.local.MixFlavorOption
import ru.igni.manager.data.local.TobaccoBrandEntity
import ru.igni.manager.data.local.TobaccoContainerEntity
import ru.igni.manager.data.local.TobaccoFlavorEntity
import ru.igni.manager.data.local.PurchaseOrderItemEntity
import ru.igni.manager.data.local.StockMovementEntity
import ru.igni.manager.data.local.OrderedFlavor
import ru.igni.manager.data.local.BrandInventoryInput
import ru.igni.manager.data.local.BrandConsumptionAnalytics
import ru.igni.manager.data.local.GuestEntity
import ru.igni.manager.data.local.CrmDao
import ru.igni.manager.data.local.VisitEntity
import ru.igni.manager.data.local.HookahHistoryEntity
import ru.igni.manager.data.local.HookahMixItemEntity
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import java.util.Base64
import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import android.graphics.pdf.PdfDocument
import android.graphics.Paint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val COAL_INTERVAL_MS = 22 * 60 * 1000L

private data class DashboardItem(
    val route: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)

private val dashboardItems = listOf(
    DashboardItem("hall", "Зал", "Карта столов и таймеры", Icons.Outlined.Map),
    DashboardItem("guests", "Гости", "CRM и история посещений", Icons.Outlined.People),
    DashboardItem("shelf", "Полка и склад", "Бренды, вкусы и остатки", Icons.Outlined.Inventory2),
    DashboardItem("analytics", "Аналитика", "Расход и показатели", Icons.Outlined.Analytics),
    DashboardItem("employees", "Сотрудники", "Роли и смены", Icons.Outlined.Groups),
    DashboardItem("settings", "Настройки", "Резервные копии и доступ", Icons.Outlined.Settings)
)

private data class HallTable(
    val number: Int,
    val zone: String
)

internal data class HookahMixComponent(
    val flavorId: Long,
    val brandName: String,
    val flavorName: String,
    val percent: Int,
    val grams: Double
)

internal data class HookahSession(
    val bowlType: String,
    val strength: Int,
    val mix: List<HookahMixComponent>,
    val startedAt: Long,
    val lastCoalAt: Long,
    val coalChanges: Int = 0
)

internal data class TableSession(
    val guestName: String,
    val guestId: Long? = null,
    val hookahs: List<HookahSession>
) {
    val hookahCount: Int get() = hookahs.size
}

private class HallState {
    val sessions: SnapshotStateMap<Int, TableSession> = mutableStateMapOf()

    fun restore(items: List<ActiveTableSessionEntity>) {
        sessions.clear()
        items.forEach { entity -> sessions[entity.tableNumber] = entity.toSession() }
    }

    fun openTable(number: Int, guest: String, guestId: Long?, bowl: String, strength: Int, count: Int, mix: List<HookahMixComponent>): TableSession {
        val now = System.currentTimeMillis()
        val hookahs = List(count.coerceAtLeast(1)) {
            HookahSession(bowlType = bowl, strength = strength, mix = mix, startedAt = now, lastCoalAt = now)
        }
        return TableSession(
            guestName = guest.trim().ifBlank { "Гость" },
            guestId = guestId,
            hookahs = hookahs
        ).also { sessions[number] = it }
    }

    fun replaceCoal(number: Int, hookahIndex: Int): TableSession? {
        val current = sessions[number] ?: return null
        if (hookahIndex !in current.hookahs.indices) return null
        val updatedHookahs = current.hookahs.mapIndexed { index, hookah ->
            if (index == hookahIndex) hookah.copy(
                lastCoalAt = System.currentTimeMillis(),
                coalChanges = hookah.coalChanges + 1
            ) else hookah
        }
        return current.copy(hookahs = updatedHookahs).also { sessions[number] = it }
    }

    fun addRepeatedHookah(number: Int, sourceIndex: Int): TableSession? {
        val current = sessions[number] ?: return null
        val source = current.hookahs.getOrNull(sourceIndex) ?: current.hookahs.lastOrNull() ?: return null
        val now = System.currentTimeMillis()
        val added = source.copy(startedAt = now, lastCoalAt = now, coalChanges = 0)
        return current.copy(hookahs = current.hookahs + added).also { sessions[number] = it }
    }

    fun addHookah(number: Int, bowl: String, strength: Int, mix: List<HookahMixComponent>): TableSession? {
        val current = sessions[number] ?: return null
        val now = System.currentTimeMillis()
        val added = HookahSession(
            bowlType = bowl,
            strength = strength,
            mix = mix,
            startedAt = now,
            lastCoalAt = now,
            coalChanges = 0
        )
        return current.copy(hookahs = current.hookahs + added).also { sessions[number] = it }
    }

    fun removeHookah(number: Int, hookahIndex: Int): TableSession? {
        val current = sessions[number] ?: return null
        if (current.hookahs.size <= 1 || hookahIndex !in current.hookahs.indices) return current
        return current.copy(hookahs = current.hookahs.filterIndexed { index, _ -> index != hookahIndex })
            .also { sessions[number] = it }
    }

    fun closeTable(number: Int) {
        sessions.remove(number)
    }
}

private suspend fun saveVisitToCrm(crmDao: CrmDao, tableNumber: Int, session: TableSession) {
    val guestId = session.guestId ?: return
    val closedAt = System.currentTimeMillis()
    val startedAt = session.hookahs.minOfOrNull { it.startedAt } ?: closedAt
    val visitId = crmDao.insertVisit(
        VisitEntity(
            guestId = guestId,
            tableNumber = tableNumber,
            startedAt = startedAt,
            closedAt = closedAt,
            status = "CLOSED",
            notes = "Автоматически сохранено при закрытии стола"
        )
    )
    session.hookahs.forEach { hookah ->
        val hookahId = crmDao.createHookahWithMix(
            HookahHistoryEntity(
                visitId = visitId,
                guestId = guestId,
                tableNumber = tableNumber,
                bowlType = hookah.bowlType,
                strength = hookah.strength,
                startedAt = hookah.startedAt,
                closedAt = closedAt,
                status = "CLOSED"
            ),
            hookah.mix.map { component ->
                HookahMixItemEntity(
                    hookahId = 0,
                    flavorId = component.flavorId,
                    brandNameSnapshot = component.brandName,
                    flavorNameSnapshot = component.flavorName,
                    percentage = component.percent,
                    grams = component.grams
                )
            }
        )
    }
    crmDao.updateGuestAfterVisit(
        guestId = guestId,
        hookahCount = session.hookahCount,
        closedAt = closedAt,
        preferredStrength = crmDao.favoriteStrength(guestId),
        brands = crmDao.favoriteBrands(guestId).joinToString(", "),
        flavors = crmDao.favoriteFlavors(guestId).joinToString(", ")
    )
}

private fun encodeText(value: String): String =
    Base64.getUrlEncoder().withoutPadding().encodeToString(value.toByteArray(Charsets.UTF_8))

private fun decodeText(value: String): String =
    String(Base64.getUrlDecoder().decode(value), Charsets.UTF_8)

private fun serializeMix(mix: List<HookahMixComponent>): String = mix.joinToString("\n") { component ->
    listOf(
        component.flavorId.toString(),
        encodeText(component.brandName),
        encodeText(component.flavorName),
        component.percent.toString(),
        component.grams.toString()
    ).joinToString("|")
}

private fun deserializeMix(value: String): List<HookahMixComponent> = value.lineSequence().mapNotNull { line ->
    val parts = line.split('|')
    if (parts.size != 5) return@mapNotNull null
    runCatching {
        HookahMixComponent(
            flavorId = parts[0].toLong(),
            brandName = decodeText(parts[1]),
            flavorName = decodeText(parts[2]),
            percent = parts[3].toInt(),
            grams = parts[4].toDouble()
        )
    }.getOrNull()
}.toList()

private fun serializeHookahs(hookahs: List<HookahSession>): String = buildString {
    append("H43\n")
    hookahs.forEachIndexed { index, hookah ->
        if (index > 0) append("\n---HOOKAH---\n")
        append(listOf(
            encodeText(hookah.bowlType),
            hookah.strength.toString(),
            hookah.startedAt.toString(),
            hookah.lastCoalAt.toString(),
            hookah.coalChanges.toString()
        ).joinToString("|"))
        append("\n")
        append(serializeMix(hookah.mix))
    }
}

private fun deserializeHookahs(value: String): List<HookahSession> {
    if (!value.startsWith("H43\n")) return emptyList()
    return value.removePrefix("H43\n").split("\n---HOOKAH---\n").mapNotNull { block ->
        val lines = block.lines()
        val meta = lines.firstOrNull()?.split('|') ?: return@mapNotNull null
        if (meta.size != 5) return@mapNotNull null
        runCatching {
            HookahSession(
                bowlType = decodeText(meta[0]),
                strength = meta[1].toInt(),
                startedAt = meta[2].toLong(),
                lastCoalAt = meta[3].toLong(),
                coalChanges = meta[4].toInt(),
                mix = deserializeMix(lines.drop(1).joinToString("\n"))
            )
        }.getOrNull()
    }
}

private fun TableSession.toEntity(tableNumber: Int): ActiveTableSessionEntity {
    val first = hookahs.first()
    return ActiveTableSessionEntity(
        tableNumber = tableNumber,
        guestName = guestName,
        guestId = guestId,
        bowlType = first.bowlType,
        strength = first.strength,
        hookahCount = hookahs.size,
        mixData = serializeHookahs(hookahs),
        startedAt = first.startedAt,
        lastCoalAt = first.lastCoalAt,
        coalChanges = first.coalChanges
    )
}

private fun ActiveTableSessionEntity.toSession(): TableSession {
    val restored = deserializeHookahs(mixData)
    val hookahs = if (restored.isNotEmpty()) restored else {
        val legacy = HookahSession(
            bowlType = bowlType,
            strength = strength,
            mix = deserializeMix(mixData),
            startedAt = startedAt,
            lastCoalAt = lastCoalAt,
            coalChanges = coalChanges
        )
        List(hookahCount.coerceAtLeast(1)) { legacy }
    }
    return TableSession(guestName = guestName, guestId = guestId, hookahs = hookahs)
}

@Composable
fun IgniApp() {
    val navController = rememberNavController()
    val hallState = remember { HallState() }
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(onOpen = { navController.navigate(it) }) }
        composable("hall") { HallScreen(onBack = { navController.popBackStack() }, hallState = hallState) }
        composable("shelf") { ShelfScreen(onBack = { navController.popBackStack() }) }
        composable("guests") { GuestsScreen(onBack = { navController.popBackStack() }) }
        composable("settings") { SettingsScreen(onBack = { navController.popBackStack() }) }
        dashboardItems.filterNot { it.route == "hall" || it.route == "shelf" || it.route == "guests" || it.route == "settings" }.forEach { item ->
            composable(item.route) {
                PlaceholderScreen(
                    title = item.title,
                    subtitle = "Модуль будет добавлен в следующей версии",
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(onOpen: (String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Column {
                        Text("IGNI MANAGER", fontWeight = FontWeight.Bold)
                        Text(
                            "5.0 • Alpha 4.8",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 28.dp, vertical = 18.dp)
        ) {
            Text("Панель управления", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(
                "Живая панель смены, CRM гостей, зал, миксы и складской учёт.",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(24.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(dashboardItems) { item -> DashboardCard(item = item, onClick = { onOpen(item.route) }) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardCard(item: DashboardItem, onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.97f else 1f, label = "dashboardScale")
    Card(
        onClick = {
            pressed = true
            onClick()
        },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.height(150.dp).graphicsLayer { scaleX = scale; scaleY = scale }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(item.icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            Column {
                Text(item.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text(item.subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HallScreen(onBack: () -> Unit, hallState: HallState) {
    val context = LocalContext.current
    val database = remember { (context.applicationContext as IgniApplication).database }
    val shelfDao = remember { database.shelfDao() }
    val hallDao = remember { database.hallDao() }
    val crmDao = remember { database.crmDao() }
    val mixOptions by shelfDao.observeMixOptions().collectAsState(initial = emptyList())
    val persistedSessions by hallDao.observeActiveSessions().collectAsState(initial = emptyList())
    val favoriteMixes by hallDao.observeFavoriteMixes().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val tables = buildList {
        add(HallTable(77, "Основной зал"))
        (101..114).forEach { add(HallTable(it, "Основной зал")) }
        (301..308).forEach { add(HallTable(it, "Летняя веранда")) }
    }
    var selectedZone by remember { mutableStateOf("Основной зал") }
    var selectedTable by remember { mutableStateOf<HallTable?>(null) }
    var addHookahTable by remember { mutableStateOf<HallTable?>(null) }
    var quickActionTable by remember { mutableStateOf<HallTable?>(null) }
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(persistedSessions) {
        hallState.restore(persistedSessions)
    }

    LaunchedEffect(Unit) {
        while (true) {
            now = System.currentTimeMillis()
            delay(1_000)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Зал", fontWeight = FontWeight.Bold)
                        Text(
                            "Свободно: ${tables.size - hallState.sessions.size} • Занято: ${hallState.sessions.size}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    TextButton(onClick = onBack, modifier = Modifier.padding(start = 8.dp)) { Text("Назад") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HallZoneSwitcher(selectedZone = selectedZone, onSelect = { selectedZone = it })
            Row(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PremiumHallMap(
                    zone = selectedZone,
                    tables = tables.filter { it.zone == selectedZone },
                    sessions = hallState.sessions,
                    selectedTableNumber = selectedTable?.number,
                    now = now,
                    onTableClick = { selectedTable = it },
                    onTableLongClick = { quickActionTable = it },
                    modifier = Modifier.weight(0.76f).fillMaxHeight()
                )
                val activeTable = selectedTable
                val activeSession = activeTable?.let { hallState.sessions[it.number] }
                if (activeTable != null && activeSession != null) {
                    ActiveTableSidePanel(
                        table = activeTable,
                        session = activeSession,
                        now = now,
                        modifier = Modifier.weight(0.24f).fillMaxHeight(),
                        onCoal = { hookahIndex ->
                            scope.launch {
                                hallState.replaceCoal(activeTable.number, hookahIndex)?.let { hallDao.saveSession(it.toEntity(activeTable.number)) }
                            }
                        },
                        onRepeat = { _ ->
                            addHookahTable = activeTable
                        },
                        onDuplicate = { hookahIndex ->
                            scope.launch {
                                val current = hallState.sessions[activeTable.number] ?: return@launch
                                val source = current.hookahs.getOrNull(hookahIndex) ?: return@launch
                                try {
                                    database.withTransaction {
                                        source.mix.forEach { shelfDao.deductFlavorOrThrow(it.flavorId, it.grams) }
                                        hallState.addRepeatedHookah(activeTable.number, hookahIndex)
                                            ?.let { hallDao.saveSession(it.toEntity(activeTable.number)) }
                                    }
                                    snackbarHostState.showSnackbar("Кальян продублирован")
                                } catch (error: Exception) {
                                    snackbarHostState.showSnackbar(error.message ?: "Не удалось продублировать кальян")
                                }
                            }
                        },
                        onRemove = { hookahIndex ->
                            scope.launch {
                                hallState.removeHookah(activeTable.number, hookahIndex)?.let { hallDao.saveSession(it.toEntity(activeTable.number)) }
                            }
                        },
                        onClose = {
                            scope.launch {
                                val closingSession = hallState.sessions[activeTable.number]
                                if (closingSession != null) saveVisitToCrm(crmDao, activeTable.number, closingSession)
                                hallState.closeTable(activeTable.number)
                                hallDao.deleteSession(activeTable.number)
                                selectedTable = null
                            }
                        },
                        onDismiss = { selectedTable = null }
                    )
                } else {
                    Card(
                        modifier = Modifier.weight(0.24f).fillMaxHeight(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF171715)),
                        border = BorderStroke(1.dp, Color(0xFF3A3328)),
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Column(
                            Modifier.fillMaxSize().padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("IGNI", color = Color(0xFFE5C17A), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium)
                            Spacer(Modifier.height(12.dp))
                            Text("Выберите занятый стол", color = Color.White.copy(alpha = 0.72f), textAlign = TextAlign.Center)
                            Spacer(Modifier.height(6.dp))
                            Text("Карточка гостя и таймеры откроются здесь", color = Color.White.copy(alpha = 0.42f), textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            HallLegendHorizontal()
        }
    }

    quickActionTable?.let { table ->
        val session = hallState.sessions[table.number]
        AlertDialog(
            onDismissRequest = { quickActionTable = null },
            title = { Text("Стол ${table.number}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (session == null) {
                        Text("Стол свободен")
                        Button(onClick = { quickActionTable = null; selectedTable = table }, modifier = Modifier.fillMaxWidth()) {
                            Text("Открыть стол")
                        }
                    } else {
                        Text("${session.guestName} • кальянов: ${session.hookahCount}")
                        Button(onClick = {
                            scope.launch { hallState.replaceCoal(table.number, 0)?.let { hallDao.saveSession(it.toEntity(table.number)) } }
                            quickActionTable = null
                        }, modifier = Modifier.fillMaxWidth()) { Text("Заменить угли") }
                        OutlinedButton(onClick = {
                            scope.launch {
                                try {
                                    database.withTransaction {
                                        session.hookahs.last().mix.forEach { shelfDao.deductFlavorOrThrow(it.flavorId, it.grams) }
                                        hallState.addRepeatedHookah(table.number, session.hookahs.lastIndex)?.let { hallDao.saveSession(it.toEntity(table.number)) }
                                    }
                                    snackbarHostState.showSnackbar("Повторный кальян добавлен")
                                } catch (error: Exception) {
                                    snackbarHostState.showSnackbar(error.message ?: "Не удалось повторить кальян")
                                }
                            }
                            quickActionTable = null
                        }, modifier = Modifier.fillMaxWidth()) { Text("Повторить последний микс") }
                        OutlinedButton(onClick = { quickActionTable = null; selectedTable = table }, modifier = Modifier.fillMaxWidth()) {
                            Text("Открыть карточку стола")
                        }
                        TextButton(onClick = {
                            scope.launch {
                                val closingSession = hallState.sessions[table.number]
                                if (closingSession != null) saveVisitToCrm(crmDao, table.number, closingSession)
                                hallState.closeTable(table.number)
                                hallDao.deleteSession(table.number)
                            }
                            quickActionTable = null
                        }, modifier = Modifier.fillMaxWidth()) { Text("Закрыть стол") }
                    }
                }
            },
            confirmButton = {},
            dismissButton = { TextButton(onClick = { quickActionTable = null }) { Text("Отмена") } }
        )
    }

    addHookahTable?.let { table ->
        TableDialog(
            table = table,
            session = null,
            selected = true,
            now = now,
            mixOptions = mixOptions,
            favoriteMixes = favoriteMixes,
            addingHookah = true,
            onDismiss = { addHookahTable = null },
            onStart = { _, _, bowl, strength, _, mix ->
                scope.launch {
                    try {
                        database.withTransaction {
                            mix.forEach { shelfDao.deductFlavorOrThrow(it.flavorId, it.grams) }
                            hallState.addHookah(table.number, bowl, strength, mix)
                                ?.let { hallDao.saveSession(it.toEntity(table.number)) }
                        }
                        addHookahTable = null
                        snackbarHostState.showSnackbar("Новый кальян добавлен")
                    } catch (error: Exception) {
                        snackbarHostState.showSnackbar(error.message ?: "Не удалось добавить кальян")
                    }
                }
            },
            onSaveFavorite = { name, mix ->
                scope.launch {
                    runCatching {
                        hallDao.insertFavorite(FavoriteMixEntity(name = name.trim(), mixData = serializeMix(mix)))
                    }.onFailure { snackbarHostState.showSnackbar(it.message ?: "Не удалось сохранить избранный микс") }
                }
            },
            onDeleteFavorite = { favorite -> scope.launch { hallDao.deleteFavorite(favorite) } },
            onCoal = {},
            onRepeat = {},
            onRemoveHookah = {},
            onClose = {}
        )
    }

    selectedTable?.takeIf { hallState.sessions[it.number] == null }?.let { table ->
        TableDialog(
            table = table,
            session = null,
            selected = true,
            now = now,
            mixOptions = mixOptions,
            favoriteMixes = favoriteMixes,
            onDismiss = { selectedTable = null },
            onStart = { guest, selectedGuestId, bowl, strength, count, mix ->
                scope.launch {
                    try {
                        database.withTransaction {
                            mix.forEach { shelfDao.deductFlavorOrThrow(it.flavorId, it.grams * count) }
                            val linkedGuestId = selectedGuestId ?: guest.trim().takeIf { it.isNotBlank() }?.let { name ->
                                crmDao.insertGuest(GuestEntity(name = name))
                            }
                            val session = hallState.openTable(table.number, guest, linkedGuestId, bowl, strength, count, mix)
                            hallDao.saveSession(session.toEntity(table.number))
                        }
                    } catch (error: Exception) {
                        snackbarHostState.showSnackbar(error.message ?: "Не удалось списать табак")
                    }
                }
            },
            onSaveFavorite = { name, mix ->
                scope.launch {
                    runCatching {
                        hallDao.insertFavorite(FavoriteMixEntity(name = name.trim(), mixData = serializeMix(mix)))
                    }.onFailure { snackbarHostState.showSnackbar(it.message ?: "Не удалось сохранить избранный микс") }
                }
            },
            onDeleteFavorite = { favorite -> scope.launch { hallDao.deleteFavorite(favorite) } },
            onCoal = { hookahIndex ->
                scope.launch {
                    hallState.replaceCoal(table.number, hookahIndex)?.let { hallDao.saveSession(it.toEntity(table.number)) }
                }
            },
            onRepeat = { hookahIndex ->
                scope.launch {
                    val current = hallState.sessions[table.number] ?: return@launch
                    val source = current.hookahs.getOrNull(hookahIndex) ?: return@launch
                    try {
                        database.withTransaction {
                            source.mix.forEach { shelfDao.deductFlavorOrThrow(it.flavorId, it.grams) }
                            hallState.addRepeatedHookah(table.number, hookahIndex)?.let { hallDao.saveSession(it.toEntity(table.number)) }
                        }
                        snackbarHostState.showSnackbar("Новый кальян добавлен и списан с полки")
                    } catch (error: Exception) {
                        snackbarHostState.showSnackbar(error.message ?: "Не удалось добавить кальян")
                    }
                }
            },
            onRemoveHookah = { hookahIndex ->
                scope.launch {
                    hallState.removeHookah(table.number, hookahIndex)?.let { hallDao.saveSession(it.toEntity(table.number)) }
                }
            },
            onClose = {
                scope.launch {
                    val closingSession = hallState.sessions[table.number]
                    if (closingSession != null) saveVisitToCrm(crmDao, table.number, closingSession)
                    hallState.closeTable(table.number)
                    hallDao.deleteSession(table.number)
                    selectedTable = null
                }
            }
        )
    }
}

private enum class FloorTableShape { ROUND, RECT, WIDE }

private data class FloorTableSpec(
    val number: Int,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val shape: FloorTableShape,
    val rotation: Float = 0f
)

@Composable
private fun HallZoneSwitcher(selectedZone: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp)).padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        listOf("Основной зал", "Летняя веранда").forEach { zone ->
            val selected = selectedZone == zone
            if (selected) {
                Button(onClick = { onSelect(zone) }, modifier = Modifier.weight(1f).height(48.dp)) { Text(zone) }
            } else {
                TextButton(onClick = { onSelect(zone) }, modifier = Modifier.weight(1f).height(48.dp)) { Text(zone) }
            }
        }
    }
}

@Composable
private fun PremiumHallMap(
    zone: String,
    tables: List<HallTable>,
    sessions: Map<Int, TableSession>,
    selectedTableNumber: Int?,
    now: Long,
    onTableClick: (HallTable) -> Unit,
    onTableLongClick: (HallTable) -> Unit,
    modifier: Modifier = Modifier
) {
    val specs = if (zone == "Основной зал") mainHallSpecs else verandaSpecs
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111315)),
        border = BorderStroke(1.dp, Color(0xFF4A3924))
    ) {
        BoxWithConstraints(Modifier.fillMaxSize().padding(10.dp)) {
            val mapWidth = maxWidth
            val mapHeight = maxHeight
            Box(
                Modifier.fillMaxSize()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF17191B))
                    .border(1.dp, Color(0xFF73552D), RoundedCornerShape(14.dp))
            )
            if (zone == "Основной зал") MainHallDecor(mapWidth, mapHeight) else VerandaDecor(mapWidth, mapHeight)
            specs.forEach { spec ->
                val table = tables.firstOrNull { it.number == spec.number } ?: return@forEach
                FloorTableNode(
                    spec = spec,
                    table = table,
                    session = sessions[table.number],
                    selected = selectedTableNumber == table.number,
                    now = now,
                    mapWidth = mapWidth,
                    mapHeight = mapHeight,
                    onClick = { onTableClick(table) },
                    onLongClick = { onTableLongClick(table) }
                )
            }
        }
    }
}

private val mainHallSpecs = listOf(
    FloorTableSpec(77, .055f, .775f, .16f, .17f, FloorTableShape.WIDE),
    FloorTableSpec(101, .27f, .08f, .095f, .14f, FloorTableShape.ROUND),
    FloorTableSpec(102, .46f, .06f, .075f, .20f, FloorTableShape.RECT),
    FloorTableSpec(103, .62f, .06f, .075f, .20f, FloorTableShape.RECT),
    FloorTableSpec(104, .79f, .06f, .075f, .20f, FloorTableShape.RECT),
    FloorTableSpec(105, .39f, .34f, .075f, .22f, FloorTableShape.RECT),
    FloorTableSpec(106, .55f, .34f, .075f, .22f, FloorTableShape.RECT),
    FloorTableSpec(107, .72f, .34f, .075f, .22f, FloorTableShape.RECT),
    FloorTableSpec(108, .38f, .64f, .075f, .21f, FloorTableShape.RECT),
    FloorTableSpec(109, .53f, .68f, .16f, .12f, FloorTableShape.WIDE),
    FloorTableSpec(110, .76f, .65f, .075f, .21f, FloorTableShape.RECT),
    FloorTableSpec(111, .25f, .58f, .075f, .12f, FloorTableShape.ROUND),
    FloorTableSpec(112, .20f, .47f, .075f, .12f, FloorTableShape.ROUND),
    FloorTableSpec(113, .04f, .39f, .14f, .12f, FloorTableShape.WIDE),
    FloorTableSpec(114, .04f, .59f, .14f, .12f, FloorTableShape.WIDE)
)

private val verandaSpecs = listOf(
    FloorTableSpec(301, .085f, .20f, .16f, .16f, FloorTableShape.WIDE, -42f),
    FloorTableSpec(302, .31f, .16f, .11f, .13f, FloorTableShape.ROUND),
    FloorTableSpec(303, .49f, .16f, .11f, .13f, FloorTableShape.ROUND),
    FloorTableSpec(304, .67f, .16f, .11f, .13f, FloorTableShape.ROUND),
    FloorTableSpec(305, .83f, .16f, .11f, .13f, FloorTableShape.ROUND),
    FloorTableSpec(306, .32f, .62f, .09f, .23f, FloorTableShape.RECT),
    FloorTableSpec(307, .53f, .60f, .09f, .23f, FloorTableShape.RECT),
    FloorTableSpec(308, .75f, .61f, .09f, .23f, FloorTableShape.RECT)
)

@Composable
private fun MainHallDecor(w: androidx.compose.ui.unit.Dp, h: androidx.compose.ui.unit.Dp) {
    // Геометрия повторяет фактический план: вход и хост слева сверху,
    // три центральных ряда столов, камин справа и бар внизу.
    DecorBlock("Хост", .025f, .045f, .105f, .225f, w, h)
    DecorLabel("ВХОД", .205f, .025f, w, h)
    DecorBlock("", .395f, .065f, .045f, .17f, w, h)
    DecorLine(.015f, .305f, .255f, .012f, w, h)
    DecorLine(.40f, .565f, .46f, .018f, w, h)
    DecorBlock("VIP 77", .025f, .75f, .23f, .22f, w, h)
    DecorDiamond("WC", .285f, .875f, .065f, .075f, w, h)
    DecorBlock("Барная зона", .375f, .875f, .59f, .07f, w, h)
    DecorPlant(.015f, .36f, w, h)
    DecorPlant(.84f, .035f, w, h)
    DecorPlant(.89f, .70f, w, h)
    DecorPlant(.34f, .47f, w, h)
}

@Composable
private fun VerandaDecor(w: androidx.compose.ui.unit.Dp, h: androidx.compose.ui.unit.Dp) {
    DecorLine(.02f, .018f, .96f, .05f, w, h)
    DecorLine(.03f, .115f, .20f, .010f, w, h)
    DecorLine(.30f, .115f, .25f, .010f, w, h)
    DecorLine(.62f, .115f, .34f, .010f, w, h)
    DecorLine(.025f, .33f, .010f, .52f, w, h)
    DecorLine(.955f, .33f, .010f, .52f, w, h)
    DecorLabel("ВХОД", .245f, .095f, w, h)
    DecorLabel("ВХОД", .18f, .79f, w, h)
    DecorLine(.27f, .91f, .66f, .032f, w, h)
    DecorPlant(.015f, .17f, w, h)
    DecorPlant(.90f, .18f, w, h)
    DecorPlant(.05f, .83f, w, h)
    DecorPlant(.90f, .83f, w, h)
}

@Composable
private fun DecorPlant(x: Float, y: Float, w: androidx.compose.ui.unit.Dp, h: androidx.compose.ui.unit.Dp) {
    Box(
        Modifier.offset(w * x, h * y).size(22.dp)
            .background(Color(0xFF26321F), CircleShape)
            .border(1.dp, Color(0xFF6C8048), CircleShape),
        contentAlignment = Alignment.Center
    ) { Text("✦", color = Color(0xFF93A861), style = MaterialTheme.typography.labelSmall) }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FloorTableNode(
    spec: FloorTableSpec,
    table: HallTable,
    session: TableSession?,
    selected: Boolean,
    now: Long,
    mapWidth: androidx.compose.ui.unit.Dp,
    mapHeight: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val activeHookah = session?.hookahs?.maxByOrNull { now - it.lastCoalAt }
    val elapsed = activeHookah?.let { now - it.lastCoalAt } ?: 0L
    val borderColor = when {
        session == null -> Color(0xFF987243)
        elapsed >= COAL_INTERVAL_MS -> Color(0xFFE15454)
        elapsed >= COAL_INTERVAL_MS - 3 * 60_000L -> Color(0xFFD7A63A)
        else -> Color(0xFF4DA365)
    }
    val shape = if (spec.shape == FloorTableShape.ROUND) CircleShape else RoundedCornerShape(if (spec.shape == FloorTableShape.WIDE) 14.dp else 10.dp)
    val selectedScale by animateFloatAsState(if (selected) 1.07f else 1f, label = "tableScale")
    val surfaceColor = when {
        session == null -> Color(0xFF242526)
        elapsed >= COAL_INTERVAL_MS -> Color(0xFF351F20)
        elapsed >= COAL_INTERVAL_MS - 3 * 60_000L -> Color(0xFF332A1C)
        else -> Color(0xFF1E2C23)
    }
    Card(
        modifier = Modifier
            .offset(x = mapWidth * spec.x, y = mapHeight * spec.y)
            .width(mapWidth * spec.width)
            .height(mapHeight * spec.height)
            .graphicsLayer { rotationZ = spec.rotation; scaleX = selectedScale; scaleY = selectedScale }
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 12.dp else 5.dp),
        border = BorderStroke(if (selected) 3.dp else if (session == null) 1.dp else 2.dp, borderColor)
    ) {
        Column(
            Modifier.fillMaxSize().padding(4.dp).graphicsLayer { rotationZ = -spec.rotation },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(table.number.toString(), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = if (selected) Color(0xFFF3D18B) else Color.White)
            if (session != null) {
                Text(session.guestName, style = MaterialTheme.typography.labelSmall, maxLines = 1, textAlign = TextAlign.Center)
                Text(coalTimerText(activeHookah ?: session.hookahs.first(), now), style = MaterialTheme.typography.labelSmall, color = borderColor, textAlign = TextAlign.Center)
                if (session.hookahCount > 1) {
                    Text("•".repeat(session.hookahCount.coerceAtMost(4)), color = Color(0xFFE5C17A), style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun DecorBlock(label: String, x: Float, y: Float, width: Float, height: Float, w: androidx.compose.ui.unit.Dp, h: androidx.compose.ui.unit.Dp) {
    Box(
        Modifier.offset(w * x, h * y).width(w * width).height(h * height)
            .background(Color(0xFF201D19), RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF75552C), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) { Text(label, color = Color(0xFFE5C17A), style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center) }
}

@Composable
private fun DecorLine(x: Float, y: Float, width: Float, height: Float, w: androidx.compose.ui.unit.Dp, h: androidx.compose.ui.unit.Dp, label: String = "") {
    Box(
        Modifier.offset(w * x, h * y).width(w * width).height(h * height)
            .background(Color(0xFF5B4428), RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) { if (label.isNotBlank()) Text(label, color = Color(0xFFE5C17A), style = MaterialTheme.typography.labelSmall) }
}

@Composable
private fun DecorLabel(label: String, x: Float, y: Float, w: androidx.compose.ui.unit.Dp, h: androidx.compose.ui.unit.Dp) {
    Text(label, color = Color(0xFFE5C17A), style = MaterialTheme.typography.labelSmall, modifier = Modifier.offset(w * x, h * y))
}

@Composable
private fun DecorDiamond(label: String, x: Float, y: Float, width: Float, height: Float, w: androidx.compose.ui.unit.Dp, h: androidx.compose.ui.unit.Dp) {
    Box(
        Modifier.offset(w * x, h * y).width(w * width).height(h * height)
            .graphicsLayer { rotationZ = 45f }
            .background(Color(0xFF201D19), RoundedCornerShape(5.dp))
            .border(1.dp, Color(0xFF75552C), RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    ) { Text(label, color = Color(0xFFE5C17A), style = MaterialTheme.typography.labelSmall, modifier = Modifier.graphicsLayer { rotationZ = -45f }) }
}

@Composable
private fun HallLegendHorizontal() {
    Row(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp)).padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendRow(Color(0xFF987243), "Свободен")
        LegendRow(Color(0xFF4DA365), "Курится")
        LegendRow(Color(0xFFD7A63A), "До замены < 3 мин")
        LegendRow(Color(0xFFE15454), "Пора менять угли")
    }
}

@Composable
private fun HallLegend() {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Статусы", fontWeight = FontWeight.SemiBold)
            LegendRow(MaterialTheme.colorScheme.surfaceVariant, "Свободен")
            LegendRow(MaterialTheme.colorScheme.primaryContainer, "Кальян активен")
            LegendRow(MaterialTheme.colorScheme.errorContainer, "Пора менять угли")
        }
    }
}

@Composable
private fun LegendRow(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(16.dp).background(color, RoundedCornerShape(5.dp)))
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun ActiveTableSidePanel(
    table: HallTable,
    session: TableSession,
    now: Long,
    modifier: Modifier = Modifier,
    onCoal: (Int) -> Unit,
    onRepeat: (Int) -> Unit,
    onDuplicate: (Int) -> Unit,
    onRemove: (Int) -> Unit,
    onClose: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedHookah by remember(table.number, session.hookahCount) { mutableStateOf(0) }
    LaunchedEffect(session.hookahCount) {
        if (selectedHookah > session.hookahs.lastIndex) selectedHookah = session.hookahs.lastIndex.coerceAtLeast(0)
    }
    val hookah = session.hookahs.getOrNull(selectedHookah) ?: session.hookahs.first()
    val elapsed = (now - hookah.lastCoalAt).coerceAtLeast(0L)
    val progress = (elapsed.toFloat() / COAL_INTERVAL_MS.toFloat()).coerceIn(0f, 1f)
    val statusColor = when {
        elapsed >= COAL_INTERVAL_MS -> Color(0xFFE15454)
        elapsed >= COAL_INTERVAL_MS - 3 * 60_000L -> Color(0xFFD7A63A)
        else -> Color(0xFF4DA365)
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF171715)),
        border = BorderStroke(1.dp, Color(0xFF4A3B28)),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("СТОЛ ${table.number}", color = Color(0xFFE5C17A), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    Text(session.guestName, color = Color.White, style = MaterialTheme.typography.titleMedium)
                }
                TextButton(onClick = onDismiss) { Text("×", style = MaterialTheme.typography.headlineSmall) }
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                lazyItems(session.hookahs.indices.toList()) { index ->
                    val active = selectedHookah == index
                    if (active) {
                        Button(onClick = { selectedHookah = index }, contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)) {
                            Text("Кальян ${index + 1}")
                        }
                    } else {
                        OutlinedButton(onClick = { selectedHookah = index }, contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)) {
                            Text("Кальян ${index + 1}")
                        }
                    }
                }
            }

            Box(Modifier.fillMaxWidth().height(178.dp), contentAlignment = Alignment.Center) {
                Canvas(Modifier.size(150.dp)) {
                    drawCircle(color = Color(0xFF302C26), style = Stroke(width = 12f))
                    drawArc(
                        color = statusColor,
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = Stroke(width = 12f)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(coalTimerText(hookah, now), color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium)
                    Text("до замены углей", color = statusColor, style = MaterialTheme.typography.labelMedium)
                    Text("замен: ${hookah.coalChanges}", color = Color.White.copy(alpha = 0.5f), style = MaterialTheme.typography.labelSmall)
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF211F1B)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("${hookah.bowlType} • крепость ${hookah.strength}/10", color = Color(0xFFE5C17A), fontWeight = FontWeight.SemiBold)
                    if (hookah.mix.isEmpty()) {
                        Text("Состав микса не указан", color = Color.White.copy(alpha = 0.5f), style = MaterialTheme.typography.bodySmall)
                    } else {
                        hookah.mix.take(4).forEach { item ->
                            Row(Modifier.fillMaxWidth()) {
                                Text("${item.brandName} ${item.flavorName}", color = Color.White.copy(alpha = 0.82f), modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                                Text("${item.percent}%", color = Color(0xFFE5C17A), style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(4.dp))
            Button(onClick = { onCoal(selectedHookah) }, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                Text("Заменить угли")
            }
            Button(onClick = { onDuplicate(selectedHookah) }, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                Text("Повторить текущий кальян")
            }
            OutlinedButton(onClick = { onRepeat(selectedHookah) }, modifier = Modifier.fillMaxWidth()) {
                Text("Новый кальян")
            }
            if (session.hookahCount > 1) {
                TextButton(onClick = { onRemove(selectedHookah) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Удалить кальян", color = Color(0xFFE15454))
                }
            }
            OutlinedButton(onClick = onClose, modifier = Modifier.fillMaxWidth(), border = BorderStroke(1.dp, Color(0xFFE15454))) {
                Text("Закрыть стол", color = Color(0xFFE15454))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TableDialog(
    table: HallTable,
    session: TableSession?,
    selected: Boolean,
    now: Long,
    mixOptions: List<MixFlavorOption>,
    favoriteMixes: List<FavoriteMixEntity>,
    onDismiss: () -> Unit,
    onStart: (String, Long?, String, Int, Int, List<HookahMixComponent>) -> Unit,
    onSaveFavorite: (String, List<HookahMixComponent>) -> Unit,
    onDeleteFavorite: (FavoriteMixEntity) -> Unit,
    onCoal: (Int) -> Unit,
    onRepeat: (Int) -> Unit,
    onRemoveHookah: (Int) -> Unit,
    onClose: () -> Unit,
    addingHookah: Boolean = false
) {
    if (session == null) {
        var guest by remember { mutableStateOf("") }
        var selectedGuestId by remember { mutableStateOf<Long?>(null) }
        var bowl by remember { mutableStateOf("Классическая") }
        var strength by remember { mutableStateOf(5f) }
        var search by remember { mutableStateOf("") }
        var flavorPickerOpen by remember { mutableStateOf(false) }
        var selectedMix by remember { mutableStateOf<List<Pair<MixFlavorOption, Int>>>(emptyList()) }
        var favoriteName by remember { mutableStateOf("") }
        var guestSuggestionsExpanded by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val crmDao = remember { (context.applicationContext as IgniApplication).database.crmDao() }
        val guestQuery = guest.trim()
        val guestSuggestions by remember(guestQuery) { crmDao.observeGuests(guestQuery) }
            .collectAsState(initial = emptyList())

        val bowlWeight = bowlWeightGrams(bowl)
        val effectiveHookahCount = 1
        val totalPercent = selectedMix.sumOf { it.second }
        val calculatedMix = calculateMix(selectedMix, bowlWeight)
        val enoughStock = calculatedMix.all { component ->
            val option = mixOptions.firstOrNull { it.flavorId == component.flavorId }
            option != null && option.totalGrams + 0.0001 >= component.grams * effectiveHookahCount
        }
        val canStart = selectedMix.isNotEmpty() && totalPercent == 100 && enoughStock

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(if (addingHookah) "Добавить кальян • стол ${table.number}" else "Открыть стол ${table.number}") },
            text = {
                LazyColumn(
                    modifier = Modifier.widthIn(min = 680.dp, max = 820.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (!addingHookah) {
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = guest,
                                    onValueChange = {
                                        guest = it
                                        selectedGuestId = null
                                        guestSuggestionsExpanded = it.isNotBlank()
                                    },
                                    label = { Text("Имя гостя") },
                                    supportingText = { Text("Начни вводить имя, телефон или номер бонусной карты") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                if (guestSuggestionsExpanded && guestQuery.isNotBlank() && guestSuggestions.isNotEmpty()) {
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                            guestSuggestions.take(6).forEach { savedGuest ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            guest = savedGuest.name
                                                            selectedGuestId = savedGuest.id
                                                            savedGuest.preferredStrength?.let { strength = it.toFloat().coerceIn(1f, 10f) }
                                                            savedGuest.preferredBowlType?.takeIf { it.isNotBlank() }?.let { bowl = it }
                                                            guestSuggestionsExpanded = false
                                                        }
                                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                ) {
                                                    Icon(Icons.Outlined.People, contentDescription = null)
                                                    Column(Modifier.weight(1f)) {
                                                        Text(savedGuest.name, fontWeight = FontWeight.SemiBold)
                                                        Text(
                                                            listOfNotNull(
                                                                savedGuest.phone?.takeIf { it.isNotBlank() },
                                                                savedGuest.telegram?.takeIf { it.isNotBlank() }?.let { "Карта $it" }
                                                            ).joinToString(" • ").ifBlank { "Карточка гостя" },
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                                                        )
                                                    }
                                                    if (savedGuest.visitCount > 0) {
                                                        Text("${savedGuest.visitCount} виз.", style = MaterialTheme.typography.labelMedium)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Text("Тип чаши • ${formatGrams(bowlWeight)}", fontWeight = FontWeight.SemiBold)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Классическая", "Фруктовая", "Электронная", "Авторская").forEach { option ->
                                if (bowl == option) Button(onClick = { bowl = option }) { Text(option) }
                                else OutlinedButton(onClick = { bowl = option }) { Text(option) }
                            }
                        }
                    }
                    item {
                        Text("Крепость: ${strength.roundToInt()} / 10")
                        Slider(value = strength, onValueChange = { strength = it }, valueRange = 1f..10f, steps = 8)
                    }
                    if (!addingHookah) {
                        item {
                            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                                Text(
                                    "При открытии стола создаётся один кальян. Дополнительные кальяны добавляются отдельно из карточки стола, поэтому у каждого будет свой микс, чаша и крепость.",
                                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                    item { Divider() }
                    item {
                        Text("Микс табака", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            if (mixOptions.isEmpty()) "Сначала добавь табак и остатки в разделе «Полка»" else "Найди вкус по бренду, названию или дескриптору",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )
                    }
                    if (favoriteMixes.isNotEmpty()) {
                        item {
                            Text("⭐ Избранные миксы", fontWeight = FontWeight.SemiBold)
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                favoriteMixes.forEach { favorite ->
                                    val favoriteComponents = deserializeMix(favorite.mixData)
                                    AssistChip(
                                        onClick = {
                                            val loaded = favoriteComponents.mapNotNull { component ->
                                                mixOptions.firstOrNull { it.flavorId == component.flavorId }?.let { it to component.percent }
                                            }
                                            if (loaded.isNotEmpty()) selectedMix = loaded
                                        },
                                        label = { Text(favorite.name) }
                                    )
                                    TextButton(onClick = { onDeleteFavorite(favorite) }) { Text("×") }
                                }
                            }
                        }
                    }
                    item {
                        OutlinedButton(
                            onClick = { flavorPickerOpen = true },
                            enabled = mixOptions.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth().height(52.dp)
                        ) {
                            Icon(Icons.Outlined.Search, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Найти и добавить вкус")
                        }
                        Text(
                            "Поиск откроется в отдельном окне — список останется виден над клавиатурой",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )
                    }
                    lazyItems(selectedMix, key = { it.first.flavorId }) { (option, percent) ->
                        val grams = calculatedMix.firstOrNull { it.flavorId == option.flavorId }?.grams ?: 0.0
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text("${option.brandName} • ${option.flavorName}", fontWeight = FontWeight.SemiBold)
                                    Text("${formatGrams(grams)} на чашу • остаток ${formatGrams(option.totalGrams)}", style = MaterialTheme.typography.bodySmall)
                                }
                                IconButton(onClick = {
                                    selectedMix = selectedMix.map { if (it.first.flavorId == option.flavorId) it.first to (it.second - 5).coerceAtLeast(5) else it }
                                }) { Icon(Icons.Outlined.Remove, null) }
                                Text("$percent%", fontWeight = FontWeight.Bold)
                                IconButton(onClick = {
                                    selectedMix = selectedMix.map { if (it.first.flavorId == option.flavorId) it.first to (it.second + 5).coerceAtMost(100) else it }
                                }) { Icon(Icons.Outlined.Add, null) }
                                TextButton(onClick = { selectedMix = selectedMix.filterNot { it.first.flavorId == option.flavorId } }) { Text("Удалить") }
                            }
                        }
                    }
                    item {
                        val statusColor = if (totalPercent == 100 && enoughStock) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        Text(
                            when {
                                selectedMix.isEmpty() -> "Добавь хотя бы один вкус"
                                totalPercent != 100 -> "Сумма микса: $totalPercent% — нужно ровно 100%"
                                !enoughStock -> "На полке недостаточно табака для этого кальяна"
                                else -> "Готово: ${formatGrams(bowlWeight)} на чашу • будет списано ${formatGrams(bowlWeight)}"
                            },
                            color = statusColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    if (selectedMix.isNotEmpty() && totalPercent == 100) {
                        item {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = favoriteName,
                                    onValueChange = { favoriteName = it },
                                    label = { Text("Название избранного микса") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedButton(
                                    enabled = favoriteName.isNotBlank(),
                                    onClick = {
                                        onSaveFavorite(favoriteName, calculatedMix)
                                        favoriteName = ""
                                    }
                                ) { Text("Сохранить ⭐") }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    enabled = canStart,
                    onClick = { onStart(guest, selectedGuestId, bowl, strength.roundToInt(), effectiveHookahCount, calculatedMix) }
                ) { Text(if (addingHookah) "Добавить кальян" else "Создать кальян") }
            },
            dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
        )

        if (flavorPickerOpen) {
            FlavorPickerDialog(
                options = mixOptions,
                selectedFlavorIds = selectedMix.map { it.first.flavorId }.toSet(),
                initialQuery = search,
                onDismiss = {
                    flavorPickerOpen = false
                    search = ""
                },
                onSelect = { option ->
                    val remaining = (100 - selectedMix.sumOf { it.second }).coerceAtLeast(10)
                    selectedMix = selectedMix + (option to remaining.coerceAtMost(100))
                    flavorPickerOpen = false
                    search = ""
                }
            )
        }
    } else {
        var selectedHookahIndex by remember(session.hookahs.size) { mutableStateOf(0) }
        if (selectedHookahIndex !in session.hookahs.indices) selectedHookahIndex = session.hookahs.lastIndex.coerceAtLeast(0)
        val hookah = session.hookahs[selectedHookahIndex]
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Стол ${table.number} • ${session.guestName}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Кальяны", fontWeight = FontWeight.SemiBold)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        lazyItems(session.hookahs.indices.toList()) { index ->
                            if (index == selectedHookahIndex) {
                                Button(onClick = { selectedHookahIndex = index }) { Text("Кальян ${index + 1}") }
                            } else {
                                OutlinedButton(onClick = { selectedHookahIndex = index }) { Text("Кальян ${index + 1}") }
                            }
                        }
                    }
                    InfoLine("Чаша", hookah.bowlType)
                    InfoLine("Крепость", "${hookah.strength} / 10")
                    InfoLine("Замен углей", hookah.coalChanges.toString())
                    Divider()
                    Text("Микс", fontWeight = FontWeight.SemiBold)
                    hookah.mix.forEach { component ->
                        InfoLine("${component.brandName} • ${component.flavorName} (${component.percent}%)", formatGrams(component.grams))
                    }
                    Spacer(Modifier.height(4.dp))
                    Text("До замены углей", style = MaterialTheme.typography.labelLarge)
                    Text(
                        coalTimerText(hookah, now),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = if (now - hookah.lastCoalAt >= COAL_INTERVAL_MS) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = { Button(onClick = { onCoal(selectedHookahIndex) }) { Text("Угли заменены") } },
            dismissButton = {
                Column(horizontalAlignment = Alignment.End) {
                    Row {
                        TextButton(onClick = { onRepeat(selectedHookahIndex) }) { Text("+ Кальян") }
                        if (session.hookahs.size > 1) {
                            TextButton(onClick = { onRemoveHookah(selectedHookahIndex) }) { Text("Удалить кальян") }
                        }
                    }
                    Row {
                        TextButton(onClick = onClose) { Text("Гость ушёл") }
                        TextButton(onClick = onDismiss) { Text("Закрыть") }
                    }
                }
            }
        )
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}

private fun bowlWeightGrams(bowlType: String): Double = when (bowlType) {
    "Фруктовая" -> 30.0
    "Электронная" -> 25.0
    "Авторская" -> 28.0
    else -> 28.0
}

private fun calculateMix(selected: List<Pair<MixFlavorOption, Int>>, bowlWeight: Double): List<HookahMixComponent> {
    val weightedTotal = selected.sumOf { (option, percent) -> option.densityCoefficient * percent }
    if (weightedTotal <= 0.0) return emptyList()
    return selected.map { (option, percent) ->
        HookahMixComponent(
            flavorId = option.flavorId,
            brandName = option.brandName,
            flavorName = option.flavorName,
            percent = percent,
            grams = bowlWeight * (option.densityCoefficient * percent) / weightedTotal
        )
    }
}

private fun coalTimerText(hookah: HookahSession, now: Long): String {
    val remaining = COAL_INTERVAL_MS - (now - hookah.lastCoalAt)
    if (remaining <= 0) {
        val overdue = -remaining / 1000
        return "Просрочено ${overdue / 60}:${(overdue % 60).toString().padStart(2, '0')}"
    }
    val seconds = remaining / 1000
    return "${seconds / 60}:${(seconds % 60).toString().padStart(2, '0')}"
}



@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun GuestsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val database = remember { (context.applicationContext as IgniApplication).database }
    val crmDao = remember { database.crmDao() }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var query by remember { mutableStateOf("") }
    val guests by crmDao.observeGuests(query).collectAsState(initial = emptyList())
    var vipOnly by remember { mutableStateOf(false) }
    var selectedGuest by remember { mutableStateOf<GuestEntity?>(null) }
    var showEditor by remember { mutableStateOf(false) }
    var editorGuest by remember { mutableStateOf<GuestEntity?>(null) }
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { entered = true }

    val shownGuests = remember(guests, vipOnly) {
        if (vipOnly) guests.filter { it.vipStatus != "STANDARD" } else guests
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Гости", fontWeight = FontWeight.Bold)
                        Text("${shownGuests.size} карточек • CRM IGNI", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    }
                },
                navigationIcon = { TextButton(onClick = onBack, modifier = Modifier.padding(start = 8.dp)) { Text("Назад") } },
                actions = {
                    Button(onClick = { editorGuest = null; showEditor = true }, modifier = Modifier.padding(end = 16.dp)) {
                        Icon(Icons.Outlined.PersonAdd, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Новый гость")
                    }
                }
            )
        }
    ) { padding ->
        AnimatedVisibility(
            visible = entered,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 5 }),
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            Row(Modifier.fillMaxSize().padding(18.dp), horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                Column(Modifier.weight(1f).fillMaxHeight()) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Outlined.Search, null) },
                        label = { Text("Поиск по имени, телефону, бонусной карте или комментарию") },
                        singleLine = true
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AssistChip(onClick = { vipOnly = !vipOnly }, label = { Text(if (vipOnly) "VIP: включён" else "Только VIP") }, leadingIcon = { Icon(Icons.Outlined.Star, null) })
                        Spacer(Modifier.width(12.dp))
                        Text("Сортировка: последние посещения", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
                    }
                    Spacer(Modifier.height(12.dp))
                    if (shownGuests.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Outlined.People, null, modifier = Modifier.size(54.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.height(12.dp))
                                Text(if (query.isBlank()) "Пока нет гостей" else "Ничего не найдено", style = MaterialTheme.typography.titleLarge)
                                Text("Создай первую карточку или измени запрос", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
                            }
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 28.dp)) {
                            lazyItems(shownGuests, key = { it.id }) { guest ->
                                GuestListCard(guest = guest, selected = selectedGuest?.id == guest.id, onClick = { selectedGuest = guest })
                            }
                        }
                    }
                }
                Card(Modifier.width(360.dp).fillMaxHeight(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    val guest = selectedGuest
                    if (guest == null) {
                        Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                            Text("Выбери гостя, чтобы открыть карточку", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
                        }
                    } else {
                        GuestDetails(
                            guest = guest,
                            crmDao = crmDao,
                            onEdit = { editorGuest = guest; showEditor = true },
                            onArchive = {
                                scope.launch {
                                    crmDao.archiveGuest(guest.id)
                                    selectedGuest = null
                                    snackbarHostState.showSnackbar("Карточка гостя перемещена в архив")
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showEditor) {
        GuestEditorDialog(
            guest = editorGuest,
            onDismiss = { showEditor = false },
            onSave = { value ->
                scope.launch {
                    runCatching {
                        if (value.id == 0L) crmDao.insertGuest(value) else crmDao.updateGuest(value)
                    }.onSuccess {
                        showEditor = false
                        selectedGuest = value.takeIf { it.id != 0L }
                        snackbarHostState.showSnackbar(if (value.id == 0L) "Гость добавлен" else "Карточка обновлена")
                    }.onFailure {
                        snackbarHostState.showSnackbar("Не удалось сохранить: проверь уникальность телефона")
                    }
                }
            }
        )
    }
}

@Composable
private fun GuestListCard(guest: GuestEntity, selected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(46.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f), RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) { Text(guest.name.take(1).uppercase(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(guest.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    if (guest.vipStatus != "STANDARD") {
                        Spacer(Modifier.width(8.dp)); Text("★ ${guest.vipStatus}", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
                    }
                }
                Text(guest.phone ?: "Телефон не указан", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${guest.visitCount}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("визитов", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun GuestDetails(guest: GuestEntity, crmDao: CrmDao, onEdit: () -> Unit, onArchive: () -> Unit) {
    val visits by remember(guest.id) { crmDao.observeVisits(guest.id) }.collectAsState(initial = emptyList())
    LazyColumn(Modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(guest.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(if (guest.vipStatus == "STANDARD") "Обычный гость" else "★ ${guest.vipStatus}", color = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onEdit) { Icon(Icons.Outlined.Edit, "Редактировать") }
            }
        }
        item { Divider() }
        item { InfoLine("Телефон", guest.phone ?: "—") }
        item { InfoLine("Номер бонусной карты Игни", guest.telegram ?: "—") }
        item { InfoLine("Крепость", guest.preferredStrength?.let { "$it / 10" } ?: "—") }
        item { InfoLine("Чаша", guest.preferredBowlType ?: "—") }
        item { InfoLine("Дескрипторы", guest.preferredDescriptors.ifBlank { "—" }) }
        item { InfoLine("Стоп-лист", guest.dislikedFlavors.ifBlank { "—" }) }
        item { InfoLine("Комментарий", guest.comment.ifBlank { "—" }) }
        item { InfoLine("Любимые бренды", guest.favoriteBrands.ifBlank { "—" }) }
        item { InfoLine("Любимые вкусы", guest.favoriteFlavors.ifBlank { "—" }) }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.padding(14.dp)) { Text("Визиты", style = MaterialTheme.typography.labelMedium); Text("${guest.visitCount}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold) }
                }
                Card(Modifier.weight(1f)) {
                    Column(Modifier.padding(14.dp)) { Text("Кальянов", style = MaterialTheme.typography.labelMedium); Text(guest.totalHookahs.toString(), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold) }
                }
            }
        }
        item { Text("История посещений", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
        if (visits.isEmpty()) {
            item { Text("Посещений пока нет", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)) }
        } else {
            lazyItems(visits, key = { it.id }) { visit ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date(visit.closedAt ?: visit.startedAt)), fontWeight = FontWeight.SemiBold)
                        Text("Стол ${visit.tableNumber} • визит завершён", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        item { OutlinedButton(onClick = onArchive, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Outlined.DeleteOutline, null); Spacer(Modifier.width(8.dp)); Text("Архивировать карточку") } }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlavorPickerDialog(
    options: List<MixFlavorOption>,
    selectedFlavorIds: Set<Long>,
    initialQuery: String,
    onDismiss: () -> Unit,
    onSelect: (MixFlavorOption) -> Unit
) {
    var query by remember(initialQuery) { mutableStateOf(initialQuery) }
    val normalized = query.trim().lowercase()
    val filtered = options.filter { option ->
        option.flavorId !in selectedFlavorIds &&
            (normalized.isBlank() ||
                option.brandName.lowercase().contains(normalized) ||
                option.flavorName.lowercase().contains(normalized) ||
                option.descriptor.lowercase().contains(normalized))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выбрать вкус") },
        text = {
            Column(
                modifier = Modifier
                    .widthIn(min = 680.dp, max = 900.dp)
                    .heightIn(min = 360.dp, max = 620.dp)
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Бренд, вкус или дескриптор") },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "Найдено: ${filtered.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    lazyItems(filtered, key = { it.flavorId }) { option ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(option) },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text("${option.brandName} • ${option.flavorName}", fontWeight = FontWeight.SemiBold)
                                    if (option.descriptor.isNotBlank()) {
                                        Text(
                                            option.descriptor,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                                            maxLines = 2
                                        )
                                    }
                                }
                                Text(formatGrams(option.totalGrams), style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                    if (filtered.isEmpty()) {
                        item {
                            Text(
                                "Ничего не найдено",
                                modifier = Modifier.fillMaxWidth().padding(24.dp),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Закрыть") } }
    )
}

@Composable
private fun GuestEditorDialog(guest: GuestEntity?, onDismiss: () -> Unit, onSave: (GuestEntity) -> Unit) {
    var name by remember(guest) { mutableStateOf(guest?.name.orEmpty()) }
    var phone by remember(guest) { mutableStateOf(guest?.phone.orEmpty()) }
    var bonusCardNumber by remember(guest) { mutableStateOf(guest?.telegram.orEmpty()) }
    var comment by remember(guest) { mutableStateOf(guest?.comment.orEmpty()) }
    var descriptors by remember(guest) { mutableStateOf(guest?.preferredDescriptors.orEmpty()) }
    var disliked by remember(guest) { mutableStateOf(guest?.dislikedFlavors.orEmpty()) }
    var strength by remember(guest) { mutableStateOf(guest?.preferredStrength ?: 5) }
    var bowl by remember(guest) { mutableStateOf(guest?.preferredBowlType ?: "Классическая") }
    var vip by remember(guest) { mutableStateOf(guest?.vipStatus ?: "STANDARD") }
    val bowls = listOf("Классическая", "Фруктовая", "Электронная", "Авторская")
    val statuses = listOf("STANDARD", "VIP", "PREMIUM")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (guest == null) "Новый гость" else "Редактирование гостя") },
        text = {
            LazyColumn(Modifier.widthIn(min = 560.dp, max = 680.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item { OutlinedTextField(name, { name = it }, label = { Text("Имя *") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(phone, { phone = it.filter { ch -> ch.isDigit() || ch == '+' || ch == ' ' || ch == '-' || ch == '(' || ch == ')' } }, label = { Text("Телефон") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(
                            bonusCardNumber,
                            { bonusCardNumber = it.filter(Char::isDigit).take(24) },
                            label = { Text("Номер бонусной карты Игни") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }
                item { Text("Любимая крепость: $strength / 10", fontWeight = FontWeight.SemiBold); Slider(strength.toFloat(), { strength = it.roundToInt().coerceIn(1, 10) }, valueRange = 1f..10f, steps = 8) }
                item { Text("Тип чаши"); LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { lazyItems(bowls) { option -> AssistChip(onClick = { bowl = option }, label = { Text(if (bowl == option) "✓ $option" else option) }) } } }
                item { Text("Статус"); LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { lazyItems(statuses) { option -> AssistChip(onClick = { vip = option }, label = { Text(if (vip == option) "✓ $option" else option) }) } } }
                item { OutlinedTextField(descriptors, { descriptors = it }, label = { Text("Любимые дескрипторы") }, modifier = Modifier.fillMaxWidth()) }
                item { OutlinedTextField(disliked, { disliked = it }, label = { Text("Не любит / стоп-лист") }, modifier = Modifier.fillMaxWidth()) }
                item { OutlinedTextField(comment, { comment = it }, label = { Text("Комментарий") }, modifier = Modifier.fillMaxWidth(), minLines = 2) }
            }
        },
        confirmButton = {
            Button(enabled = name.isNotBlank(), onClick = {
                val now = System.currentTimeMillis()
                onSave((guest ?: GuestEntity(name = name.trim())).copy(
                    name = name.trim(), phone = phone.trim().ifBlank { null }, telegram = bonusCardNumber.trim().ifBlank { null },
                    comment = comment.trim(), preferredStrength = strength, preferredBowlType = bowl,
                    preferredDescriptors = descriptors.trim(), dislikedFlavors = disliked.trim(), vipStatus = vip, updatedAt = now
                ))
            }) { Text("Сохранить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(onBack: () -> Unit) {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = { Text("Настройки", fontWeight = FontWeight.Bold) },
                navigationIcon = { TextButton(onClick = onBack) { Text("Назад") } }
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Резервные копии", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
            Text("Экспорт и восстановление .igni будут подключены после проверки CRM и быстрых действий в этой тестовой сборке.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Button(onClick = { scope.launch { snackbar.showSnackbar("Экспорт .igni готовится к Alpha 4.0.4") } }) { Text("Создать резервную копию") }
            OutlinedButton(onClick = { scope.launch { snackbar.showSnackbar("Восстановление .igni готовится к Alpha 4.0.4") } }) { Text("Восстановить резервную копию") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShelfScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val database = remember { (context.applicationContext as IgniApplication).database }
    val dao = remember { database.shelfDao() }
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    val brands by dao.observeBrands().collectAsState(initial = emptyList())
    val flavors by dao.observeFlavorStock().collectAsState(initial = emptyList())
    val ordered by dao.observeOrderedItems().collectAsState(initial = emptyList())
    val brandAnalytics by dao.observeBrandAnalytics().collectAsState(initial = emptyList())
    val inventoryHistory by dao.observeInventoryHistory().collectAsState(initial = emptyList())

    var query by remember { mutableStateOf("") }
    var selectedFlavor by remember { mutableStateOf<FlavorWithStock?>(null) }
    var dialog by remember { mutableStateOf<ShelfDialog?>(null) }
    var section by remember { mutableStateOf("stock") }
    var purchaseTab by remember { mutableStateOf("need") }

    LaunchedEffect(flavors, selectedFlavor?.flavorId) {
        selectedFlavor?.let { current -> selectedFlavor = flavors.firstOrNull { it.flavorId == current.flavorId } ?: current }
    }

    val filtered = remember(flavors, query) {
        val normalized = query.trim().lowercase()
        if (normalized.isBlank()) flavors else flavors.filter {
            it.brandName.lowercase().contains(normalized) || it.flavorName.lowercase().contains(normalized) || it.descriptor.lowercase().contains(normalized)
        }
    }
    val orderedIds = remember(ordered) { ordered.map { it.flavorId }.toSet() }
    val needPurchase = remember(flavors, orderedIds) { flavors.filter { it.totalGrams <= 50.0 && it.flavorId !in orderedIds } }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(when (section) { "stock" -> "Полка и склад"; "purchase" -> "Закупки"; else -> "Аналитика брендов" }, fontWeight = FontWeight.Bold)
                        Text(
                            when (section) {
                                "stock" -> "Брендов: ${brands.size} • Вкусов: ${flavors.size} • Остаток: ${formatGrams(flavors.sumOf { it.totalGrams })}"
                                "purchase" -> "Нужно заказать: ${needPurchase.size} • Уже заказано: ${ordered.size} • Порог: 50 г"
                                else -> "Инвентаризаций: ${inventoryHistory.size} • Коэффициент активируется после 4 периодов"
                            },
                            style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = { TextButton(onClick = onBack, modifier = Modifier.padding(start = 8.dp)) { Text("Назад") } },
                actions = {
                    TextButton(onClick = { section = "stock" }) { Text(if (section == "stock") "● Склад" else "Склад") }
                    TextButton(onClick = { section = "purchase" }) { Text(if (section == "purchase") "● Закупки" else "Закупки (${needPurchase.size})") }
                    TextButton(onClick = { section = "analytics" }) { Text(if (section == "analytics") "● Аналитика" else "Аналитика") }
                    if (section == "stock") {
                        TextButton(onClick = { dialog = ShelfDialog.AddBrand }) { Text("+ Бренд") }
                        TextButton(onClick = { dialog = ShelfDialog.AddFlavor }, enabled = brands.isNotEmpty()) { Text("+ Вкус") }
                    }
                }
            )
        },
        floatingActionButton = {
            if (section == "stock") FloatingActionButton(
                onClick = { dialog = if (flavors.isEmpty()) ShelfDialog.AddFlavor else ShelfDialog.AddContainer(selectedFlavor) },
                containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary
            ) { Icon(Icons.Outlined.Add, contentDescription = "Добавить контейнер") }
        }
    ) { padding ->
        if (section == "stock") {
            Row(modifier = Modifier.fillMaxSize().padding(padding).padding(18.dp), horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                Column(modifier = Modifier.weight(1.25f).fillMaxHeight()) {
                    OutlinedTextField(query, { query = it }, label = { Text("Поиск по бренду, вкусу или дескриптору") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(14.dp))
                    if (filtered.isEmpty()) EmptyShelfState(brands.isNotEmpty(), { dialog = ShelfDialog.AddBrand }, { dialog = ShelfDialog.AddFlavor })
                    else LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        lazyItems(filtered, key = { it.flavorId }) { flavor ->
                            FlavorStockCard(flavor, selectedFlavor?.flavorId == flavor.flavorId, { selectedFlavor = flavor }) { available ->
                                scope.launch { dao.setFlavorAvailability(flavor.flavorId, available) }
                            }
                        }
                        item { Spacer(Modifier.height(80.dp)) }
                    }
                }
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), modifier = Modifier.width(390.dp).fillMaxHeight()) {
                    selectedFlavor?.let { flavor -> ContainerPanel(flavor, dao, { dialog = ShelfDialog.AddContainer(flavor) }, { dialog = ShelfDialog.EditContainer(it) }) }
                        ?: Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) { Text("Выбери вкус слева, чтобы увидеть контейнеры и историю", textAlign = TextAlign.Center) }
                }
            }
        } else if (section == "purchase") {
            Column(Modifier.fillMaxSize().padding(padding).padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { purchaseTab = "need" }, enabled = purchaseTab != "need") { Text("Требуют закупки (${needPurchase.size})") }
                    Button(onClick = { purchaseTab = "ordered" }, enabled = purchaseTab != "ordered") { Text("Заказано (${ordered.size})") }
                    Spacer(Modifier.weight(1f))
                    OutlinedButton(onClick = {
                        val items = if (purchaseTab == "need") needPurchase.map { Triple(it.brandName, it.flavorName, 250.0) }
                        else ordered.map { Triple(it.brandName, it.flavorName, it.recommendedGrams) }
                        if (items.isEmpty()) scope.launch { snackbar.showSnackbar("Список закупки пуст") }
                        else {
                            exportPurchaseFiles(context, items)
                            scope.launch { snackbar.showSnackbar("Excel-совместимый CSV и PDF сохранены в Downloads/IGNI") }
                        }
                    }) { Text("Экспорт Excel/PDF") }
                }
                if ((purchaseTab == "need" && needPurchase.isEmpty()) || (purchaseTab == "ordered" && ordered.isEmpty())) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(if (purchaseTab == "need") "Все вкусы выше порога 50 г" else "Заказанных позиций пока нет") }
                } else LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (purchaseTab == "need") {
                        val grouped = needPurchase.groupBy { it.brandName }
                        grouped.forEach { (brand, items) ->
                            item { Text(brand, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
                            lazyItems(items, key = { it.flavorId }) { flavor ->
                                Card(Modifier.fillMaxWidth()) {
                                    Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Column(Modifier.weight(1f)) {
                                            Text(flavor.flavorName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                            Text("Остаток: ${formatGrams(flavor.totalGrams)} • Рекомендуется заказать: 250 г")
                                        }
                                        Button(onClick = { scope.launch { dao.insertOrderItem(PurchaseOrderItemEntity(flavorId = flavor.flavorId, recommendedGrams = 250.0)) } }) { Text("Заказано") }
                                    }
                                }
                            }
                        }
                    } else {
                        val grouped = ordered.groupBy { it.brandName }
                        grouped.forEach { (brand, items) ->
                            item { Text(brand, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
                            lazyItems(items, key = { it.orderId }) { item ->
                                Card(Modifier.fillMaxWidth()) {
                                    Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Column(Modifier.weight(1f)) {
                                            Text(item.flavorName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                            Text("Остаток: ${formatGrams(item.currentGrams)} • Заказ: ${formatGrams(item.recommendedGrams)}")
                                        }
                                        TextButton(onClick = { scope.launch { dao.removeOrderItem(item.flavorId) } }) { Text("Отменить") }
                                        Button(onClick = { dialog = ShelfDialog.ReceiveDelivery(item) }) { Text("Получить") }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            BrandAnalyticsPanel(
                modifier = Modifier.fillMaxSize().padding(padding).padding(18.dp),
                analytics = brandAnalytics,
                historyCount = inventoryHistory.size,
                onInventory = { dialog = ShelfDialog.Inventory }
            )
        }
    }

    when (val current = dialog) {
        ShelfDialog.AddBrand -> AddBrandDialog({ dialog = null }) { name, coefficient -> scope.launch { dao.insertBrand(TobaccoBrandEntity(name = name.trim(), densityCoefficient = coefficient)) }; dialog = null }
        ShelfDialog.AddFlavor -> AddFlavorDialog(brands, { dialog = null }) { brandId, name, descriptor ->
            scope.launch {
                val id = dao.insertFlavor(TobaccoFlavorEntity(brandId = brandId, name = name.trim(), descriptor = descriptor.trim()))
                selectedFlavor = FlavorWithStock(id, brandId, brands.firstOrNull { it.id == brandId }?.name.orEmpty(), name.trim(), descriptor.trim(), true, 0.0, 0)
            }; dialog = null
        }
        is ShelfDialog.AddContainer -> AddContainerDialog(flavors, current.flavor, { dialog = null }) { flavorId, label, grams ->
            scope.launch {
                dao.insertContainer(TobaccoContainerEntity(flavorId = flavorId, label = label.trim(), remainingGrams = grams))
                dao.insertMovement(StockMovementEntity(flavorId = flavorId, deltaGrams = grams, movementType = "DELIVERY", note = label.trim().ifBlank { "Ручное пополнение" }))
            }; dialog = null
        }
        is ShelfDialog.EditContainer -> EditContainerDialog(current.container, { dialog = null }) { grams, active -> scope.launch { dao.updateContainerStock(current.container.id, grams, active) }; dialog = null }
        is ShelfDialog.ReceiveDelivery -> ReceiveDeliveryDialog(current.item, { dialog = null }) { grams, label ->
            scope.launch { dao.receiveDelivery(current.item.flavorId, grams, label) }; dialog = null
        }
        ShelfDialog.Inventory -> WeeklyInventoryDialog(brands, flavors, { dialog = null }) { inputs ->
            scope.launch {
                dao.completeWeeklyInventory(inputs)
                snackbar.showSnackbar(if (inventoryHistory.isEmpty()) "Базовая инвентаризация сохранена" else "Инвентаризация сохранена, аналитика обновлена")
            }
            dialog = null
        }
        null -> Unit
    }
}

private sealed interface ShelfDialog {
    data object AddBrand : ShelfDialog
    data object AddFlavor : ShelfDialog
    data class AddContainer(val flavor: FlavorWithStock?) : ShelfDialog
    data class EditContainer(val container: TobaccoContainerEntity) : ShelfDialog
    data class ReceiveDelivery(val item: OrderedFlavor) : ShelfDialog
    data object Inventory : ShelfDialog
}

@Composable
private fun BrandAnalyticsPanel(
    modifier: Modifier,
    analytics: List<BrandConsumptionAnalytics>,
    historyCount: Int,
    onInventory: () -> Unit
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Накопительный анализ расхода", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Раз в неделю внеси фактический остаток каждого бренда. После четырёх завершённых периодов коэффициент начнёт применяться автоматически.")
            }
            Button(onClick = onInventory) { Text("Провести инвентаризацию") }
        }
        if (analytics.isEmpty()) Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Добавь бренды и проведи первую инвентаризацию") }
        else LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            lazyItems(analytics, key = { it.brandId }) { item ->
                val ready = item.completedPeriods >= 4 && item.totalCalculatedConsumptionGrams > 0.0
                val difference = item.totalActualConsumptionGrams - item.totalCalculatedConsumptionGrams
                Card(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(item.brandName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(if (ready) "Коэффициент активен • ${item.completedPeriods} периодов" else "Сбор данных • ${item.completedPeriods}/4 периодов", color = if (ready) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
                            Text("Факт: ${formatGrams(item.totalActualConsumptionGrams)} • По чашам: ${formatGrams(item.totalCalculatedConsumptionGrams)} • Разница: ${formatGrams(difference)}", style = MaterialTheme.typography.bodySmall)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(String.format("%.2f", item.densityCoefficient), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Text(if (ready) "накопительный" else "пока 1.00", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WeeklyInventoryDialog(
    brands: List<TobaccoBrandEntity>,
    flavors: List<FlavorWithStock>,
    onDismiss: () -> Unit,
    onSave: (List<BrandInventoryInput>) -> Unit
) {
    val calculatedByBrand = remember(flavors) { flavors.groupBy { it.brandId }.mapValues { (_, v) -> v.sumOf { it.totalGrams } } }
    val values = remember(brands, calculatedByBrand) { mutableStateMapOf<Long, String>().apply { brands.forEach { put(it.id, String.format("%.0f", calculatedByBrand[it.id] ?: 0.0)) } } }
    val valid = brands.isNotEmpty() && brands.all { values[it.id]?.replace(',', '.')?.toDoubleOrNull()?.let { grams -> grams >= 0 } == true }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Еженедельная инвентаризация") },
        text = {
            Column(Modifier.fillMaxWidth().height(460.dp)) {
                Text("Введи фактический суммарный остаток всех боксов каждого бренда. Текущие расчётные остатки уже подставлены.", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(10.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    lazyItems(brands, key = { it.id }) { brand ->
                        OutlinedTextField(
                            value = values[brand.id].orEmpty(),
                            onValueChange = { values[brand.id] = it.filter { ch -> ch.isDigit() || ch == ',' || ch == '.' } },
                            label = { Text("${brand.name}, г") },
                            supportingText = { Text("По программе: ${formatGrams(calculatedByBrand[brand.id] ?: 0.0)}") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(brands.map { brand -> BrandInventoryInput(brand.id, brand.name, calculatedByBrand[brand.id] ?: 0.0, values[brand.id]!!.replace(',', '.').toDouble()) })
            }, enabled = valid) { Text("Сохранить инвентаризацию") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}

@Composable
private fun EmptyShelfState(hasBrands: Boolean, onAddBrand: () -> Unit, onAddFlavor: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Outlined.Inventory2, null, modifier = Modifier.size(54.dp), tint = MaterialTheme.colorScheme.primary)
            Text(if (hasBrands) "На полке пока нет вкусов" else "Полка пока пустая", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = if (hasBrands) onAddFlavor else onAddBrand) {
                Text(if (hasBrands) "Добавить первый вкус" else "Добавить первый бренд")
            }
        }
    }
}

@Composable
private fun FlavorStockCard(
    flavor: FlavorWithStock,
    selected: Boolean,
    onClick: () -> Unit,
    onAvailabilityChange: (Boolean) -> Unit
) {
    val lowStock = flavor.totalGrams in 0.01..50.0
    val containerColor = when {
        selected -> MaterialTheme.colorScheme.primaryContainer
        !flavor.isAvailable || flavor.totalGrams <= 0.0 -> MaterialTheme.colorScheme.surfaceVariant
        lowStock -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.surface
    }
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(flavor.brandName, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(flavor.flavorName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                if (flavor.descriptor.isNotBlank()) {
                    Text(flavor.descriptor, maxLines = 1, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(formatGrams(flavor.totalGrams), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Контейнеров: ${flavor.containerCount}", style = MaterialTheme.typography.bodySmall)
            }
            Switch(checked = flavor.isAvailable, onCheckedChange = onAvailabilityChange)
        }
    }
}

@Composable
private fun ContainerPanel(
    flavor: FlavorWithStock,
    dao: ru.igni.manager.data.local.ShelfDao,
    onAddContainer: () -> Unit,
    onEditContainer: (TobaccoContainerEntity) -> Unit
) {
    val containers by dao.observeContainers(flavor.flavorId).collectAsState(initial = emptyList())
    val movements by dao.observeMovements(flavor.flavorId).collectAsState(initial = emptyList())
    Column(Modifier.fillMaxSize().padding(18.dp)) {
        Text(flavor.brandName, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
        Text(flavor.flavorName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("Общий остаток: ${formatGrams(flavor.totalGrams)}")
        Spacer(Modifier.height(16.dp))
        Button(onClick = onAddContainer, modifier = Modifier.fillMaxWidth()) { Text("Добавить контейнер") }
        Spacer(Modifier.height(14.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item { Text("Контейнеры", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            if (containers.isEmpty()) item { Text("Контейнеров пока нет", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)) }
            else lazyItems(containers, key = { "c${it.id}" }) { container ->
                Card(onClick = { onEditContainer(container) }, colors = CardDefaults.cardColors(containerColor = if (container.isActive) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))) {
                    Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) { Text(container.label, fontWeight = FontWeight.SemiBold); Text(if (container.isActive) "Активен" else "Закрыт", style = MaterialTheme.typography.bodySmall) }
                        Text(formatGrams(container.remainingGrams), fontWeight = FontWeight.Bold)
                    }
                }
            }
            item { Spacer(Modifier.height(8.dp)); Text("История движения", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            if (movements.isEmpty()) item { Text("Операций пока нет", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)) }
            else lazyItems(movements, key = { "m${it.id}" }) { movement ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))) {
                    Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(if (movement.movementType == "DELIVERY") "Поставка" else "Списание", fontWeight = FontWeight.SemiBold)
                            if (movement.note.isNotBlank()) Text(movement.note, style = MaterialTheme.typography.bodySmall)
                            Text(SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date(movement.createdAt)), style = MaterialTheme.typography.bodySmall)
                        }
                        Text((if (movement.deltaGrams > 0) "+" else "") + formatGrams(movement.deltaGrams), fontWeight = FontWeight.Bold)
                    }
                }
            }
            item { Spacer(Modifier.height(40.dp)) }
        }
    }
}

@Composable
private fun AddBrandDialog(onDismiss: () -> Unit, onSave: (String, Double) -> Unit) {
    var name by remember { mutableStateOf("") }
    var coefficientText by remember { mutableStateOf("1.0") }
    val coefficient = coefficientText.replace(',', '.').toDoubleOrNull()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новый бренд") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("Название бренда") }, singleLine = true)
                OutlinedTextField(coefficientText, { coefficientText = it }, label = { Text("Коэффициент плотности") }, singleLine = true)
                Text("На старте можно оставить 1.0. Позже коэффициент будет уточняться по ревизиям.", style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = { Button(onClick = { onSave(name, coefficient ?: 1.0) }, enabled = name.isNotBlank() && coefficient != null && coefficient > 0) { Text("Сохранить") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddFlavorDialog(
    brands: List<TobaccoBrandEntity>,
    onDismiss: () -> Unit,
    onSave: (Long, String, String) -> Unit
) {
    var selectedBrandId by remember(brands) { mutableLongStateOf(brands.firstOrNull()?.id ?: 0L) }
    var name by remember { mutableStateOf("") }
    var descriptor by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новый вкус") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Бренд", fontWeight = FontWeight.SemiBold)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    brands.forEach { brand ->
                        AssistChip(onClick = { selectedBrandId = brand.id }, label = { Text(if (brand.id == selectedBrandId) "✓ ${brand.name}" else brand.name) })
                    }
                }
                OutlinedTextField(name, { name = it }, label = { Text("Название вкуса") }, singleLine = true)
                OutlinedTextField(descriptor, { descriptor = it }, label = { Text("Дескрипторы: ягоды, цитрус, холод…") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = { Button(onClick = { onSave(selectedBrandId, name, descriptor) }, enabled = selectedBrandId != 0L && name.isNotBlank()) { Text("Сохранить") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddContainerDialog(
    flavors: List<FlavorWithStock>,
    preselected: FlavorWithStock?,
    onDismiss: () -> Unit,
    onSave: (Long, String, Double) -> Unit
) {
    var selectedFlavorId by remember(preselected, flavors) { mutableLongStateOf(preselected?.flavorId ?: flavors.firstOrNull()?.flavorId ?: 0L) }
    var label by remember { mutableStateOf("") }
    var gramsText by remember { mutableStateOf("") }
    val grams = gramsText.replace(',', '.').toDoubleOrNull()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новый контейнер") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (preselected == null) {
                    Text("Вкус", fontWeight = FontWeight.SemiBold)
                    LazyColumn(modifier = Modifier.height(150.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        lazyItems(flavors, key = { it.flavorId }) { flavor ->
                            AssistChip(
                                onClick = { selectedFlavorId = flavor.flavorId },
                                label = { Text(if (flavor.flavorId == selectedFlavorId) "✓ ${flavor.brandName} • ${flavor.flavorName}" else "${flavor.brandName} • ${flavor.flavorName}") }
                            )
                        }
                    }
                } else {
                    Text("${preselected.brandName} • ${preselected.flavorName}", color = MaterialTheme.colorScheme.primary)
                }
                OutlinedTextField(label, { label = it }, label = { Text("Маркировка контейнера") }, placeholder = { Text("Например: A-01") }, singleLine = true)
                OutlinedTextField(gramsText, { gramsText = it }, label = { Text("Вес табака, г") }, singleLine = true)
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(selectedFlavorId, label.ifBlank { "Контейнер" }, grams ?: 0.0) },
                enabled = selectedFlavorId != 0L && grams != null && grams > 0
            ) { Text("Добавить") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}

@Composable
private fun EditContainerDialog(
    container: TobaccoContainerEntity,
    onDismiss: () -> Unit,
    onSave: (Double, Boolean) -> Unit
) {
    var gramsText by remember(container) { mutableStateOf(container.remainingGrams.toString()) }
    var active by remember(container) { mutableStateOf(container.isActive) }
    val grams = gramsText.replace(',', '.').toDoubleOrNull()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(container.label) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(gramsText, { gramsText = it }, label = { Text("Текущий остаток, г") }, singleLine = true)
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Контейнер активен", modifier = Modifier.weight(1f))
                    Switch(checked = active, onCheckedChange = { active = it })
                }
                OutlinedButton(
                    onClick = { gramsText = "0"; active = false },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Контейнер закончился") }
            }
        },
        confirmButton = { Button(onClick = { onSave((grams ?: 0.0).coerceAtLeast(0.0), active && (grams ?: 0.0) > 0) }, enabled = grams != null && grams >= 0) { Text("Сохранить") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}

@Composable
private fun ReceiveDeliveryDialog(item: OrderedFlavor, onDismiss: () -> Unit, onSave: (Double, String) -> Unit) {
    var gramsText by remember(item) { mutableStateOf(item.recommendedGrams.toString()) }
    var label by remember { mutableStateOf("Поставка") }
    val grams = gramsText.replace(',', '.').toDoubleOrNull()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Получить поставку") },
        text = { Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("${item.brandName} • ${item.flavorName}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            Text("Текущий остаток: ${formatGrams(item.currentGrams)}")
            OutlinedTextField(gramsText, { gramsText = it }, label = { Text("Получено, г") }, singleLine = true)
            OutlinedTextField(label, { label = it }, label = { Text("Комментарий / номер поставки") }, singleLine = true)
        } },
        confirmButton = { Button(onClick = { onSave(grams ?: 0.0, label.ifBlank { "Поставка" }) }, enabled = grams != null && grams > 0) { Text("Принять") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}

private fun exportPurchaseFiles(context: android.content.Context, items: List<Triple<String, String, Double>>) {
    val stamp = SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.getDefault()).format(Date())
    val resolver = context.contentResolver
    val csv = buildString {
        append("Бренд;Вкус;Заказать, г\n")
        items.forEach { (brand, flavor, grams) -> append("\"").append(brand.replace("\"", "\"\"")).append("\";\"").append(flavor.replace("\"", "\"\"")).append("\";").append(grams.toInt()).append('\n') }
    }
    val csvValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, "IGNI_purchase_$stamp.csv")
        put(MediaStore.Downloads.MIME_TYPE, "text/csv")
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/IGNI")
    }
    resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, csvValues)?.let { uri -> resolver.openOutputStream(uri)?.use { it.write(csv.toByteArray(Charsets.UTF_8)) } }

    val pdf = PdfDocument()
    val paint = Paint().apply { textSize = 14f }
    val titlePaint = Paint().apply { textSize = 20f; isFakeBoldText = true }
    var pageNumber = 1
    var page = pdf.startPage(PdfDocument.PageInfo.Builder(595, 842, pageNumber).create())
    var canvas = page.canvas
    var y = 55f
    canvas.drawText("IGNI — список закупки", 40f, y, titlePaint); y += 35f
    var currentBrand = ""
    items.forEach { (brand, flavor, grams) ->
        if (y > 800f) { pdf.finishPage(page); pageNumber++; page = pdf.startPage(PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()); canvas = page.canvas; y = 50f }
        if (brand != currentBrand) { currentBrand = brand; paint.isFakeBoldText = true; canvas.drawText(brand, 40f, y, paint); paint.isFakeBoldText = false; y += 24f }
        canvas.drawText("• $flavor — ${grams.toInt()} г", 60f, y, paint); y += 22f
    }
    pdf.finishPage(page)
    val pdfValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, "IGNI_purchase_$stamp.pdf")
        put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/IGNI")
    }
    resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, pdfValues)?.let { uri -> resolver.openOutputStream(uri)?.use { pdf.writeTo(it) } }
    pdf.close()
}

private fun formatGrams(value: Double): String = if (value % 1.0 == 0.0) "${value.toInt()} г" else "${"%.1f".format(value)} г"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaceholderScreen(title: String, subtitle: String, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = { TextButton(onClick = onBack, modifier = Modifier.padding(start = 8.dp)) { Text("Назад") } }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                Text(subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
            }
        }
    }
}
