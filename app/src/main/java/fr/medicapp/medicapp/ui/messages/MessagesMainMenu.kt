package fr.medicapp.medicapp.ui.messages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.medicapp.medicapp.ui.theme.EUBlue100
import fr.medicapp.medicapp.ui.theme.EUBlue80

/**
 * Composable représentant le menu principal des messages de l'application MedicApp.
 *
 * Ce menu affiche la liste des conversations de messages de l'utilisateur. Chaque conversation est représentée par une carte qui affiche le dernier message de la conversation.
 * Si l'utilisateur n'a pas de messages, un message d'information est affiché.
 *
 * @param messages La liste des conversations de messages à afficher.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesMainMenu(
    messages: List<TestMessages>
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                ),
                title = {
                    Text(
                        "Messages",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        bottomBar = {

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = EUBlue100
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                    tint = Color.White
                )
            }

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(10.dp)
        ) {
            if (messages.isNotEmpty()) {
                for (i in messages) {
                    ElevatedCard(
                        onClick = { /*TODO*/ },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 75.dp),
                        colors = if (i.messageVu) {
                            // Cas du message vu
                            CardDefaults.cardColors(
                                containerColor = EUBlue80,
                                contentColor = Color.White
                            )
                        } else {
                            // Cas du message pas vu
                            CardDefaults.cardColors(
                                containerColor = EUBlue100,
                                contentColor = Color.White
                            )
                        },

                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                        ) {
                            Text(
                                text = i.destinataire,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold

                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                i.messages[i.messages.size - 1],
                                fontSize = 15.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                ) {
                    Text(
                        "Vous n'avez pas de messages.\nPour commencer une conversation, cliquez sur\nle bouton en bas.",
                        color = EUBlue100,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontStyle = FontStyle.Italic
                    )
                }

            }
        }
    }
}

/**
 * Prévisualisation du menu principal des messages.
 *
 * Cette prévisualisation permet de voir à quoi ressemble le menu principal des messages sans avoir à lancer l'application.
 */
@Preview(showBackground = true)
@Composable
private fun MessagesMainMenuPreview() {
    var messages = listOf(
        TestMessages(
            "Dr. MOTTU",
            false,
            listOf("Bonjour", "Bonjour", "Au revoir")
        ),
        TestMessages(
            "Dr. CAZALAS",
            true,
            listOf("")
        ),
        TestMessages(
            "Dr. BERDJUGIN",
            true,
            listOf("Bonjour je veux mon médicament s'il vous plait")
        )
    )
    MessagesMainMenu(messages)
}
