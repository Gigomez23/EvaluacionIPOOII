package ni.edu.uam.evaluacionipooii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                PantallaControlAsistencia()
            }
        }
    }
}

data class RegistroAsistencia(
    val nombre: String,
    val estado: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaControlAsistencia() {

    var nombreEstudiante by remember { mutableStateOf("") }
    var estadoSeleccionado by remember { mutableStateOf("Presente") }
    var expanded by remember { mutableStateOf(false) }
    var mensajeConfirmacion by remember { mutableStateOf("Aún no se ha registrado asistencia.") }

    val listaEstados = listOf("Presente", "Ausente", "Tarde")
    val registros = remember { mutableStateListOf<RegistroAsistencia>() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Control de Asistencia",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Text(
                text = "Registrar estudiante",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = nombreEstudiante,
                onValueChange = { nombreEstudiante = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre del estudiante") },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Person,
//                        contentDescription = "Ícono de estudiante"
//                    )
//                },
                singleLine = true,
                colors = TextFieldDefaults.colors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = estadoSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listaEstados.forEach { estado ->
                        DropdownMenuItem(
                            text = { Text(estado) },
                            onClick = {
                                estadoSeleccionado = estado
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val nombreLimpio = nombreEstudiante.trim()

                    if (nombreLimpio.isNotEmpty()) {
                        registros.add(
                            RegistroAsistencia(
                                nombre = nombreLimpio,
                                estado = estadoSeleccionado
                            )
                        )
                        mensajeConfirmacion =
                            "Asistencia registrada: $nombreLimpio - $estadoSeleccionado"
                        nombreEstudiante = ""
                        estadoSeleccionado = "Presente"
                    } else {
                        mensajeConfirmacion = "Por favor, ingresa el nombre del estudiante."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar asistencia")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = mensajeConfirmacion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Estudiantes registrados",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (registros.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay registros todavía.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(registros) { registro ->
                        TarjetaRegistro(registro = registro)
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaRegistro(registro: RegistroAsistencia) {

//    val icono = when (registro.estado) {
//        "Presente" -> Icons.Default.CheckCircle
//        "Ausente" -> Icons.Default.Warning
//        else -> Icons.Default.Schedule
//    }

    val textoSecundario = when (registro.estado) {
        "Presente" -> "Asistió a clases"
        "Ausente" -> "No asistió a clases"
        else -> "Llegó después de la hora"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            androidx.compose.foundation.layout.Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Icon(
//                    imageVector = icono,
//                    contentDescription = "Estado de asistencia",
//                    tint = MaterialTheme.colorScheme.primary
//                )

                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(6.dp))

                Column {
                    Text(
                        text = registro.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Estado: ${registro.estado}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = textoSecundario,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}