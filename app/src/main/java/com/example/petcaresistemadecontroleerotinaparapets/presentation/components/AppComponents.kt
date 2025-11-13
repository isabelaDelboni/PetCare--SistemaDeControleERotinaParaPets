package com.example.petcaresistemadecontroleerotinaparapets.presentation.components

// ✅ Imports necessários para o botão
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petcaresistemadecontroleerotinaparapets.R // ✅ Import para o R.drawable

/**
 * Botão customizado para "Sign in with Google", seguindo as diretrizes de design.
 *
 * @param onClick Ação a ser executada quando o botão for clicado.
 * @param modifier Modificador para customização.
 */
@Composable
fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp), // Cantos levemente arredondados
        border = BorderStroke(1.dp, Color.LightGray), // Borda cinza clara
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White, // Fundo branco
            contentColor = Color.Black // Texto/ícone preto (por padrão)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start, // Alinha ao início
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp) // Padding interno
        ) {
            // Ícone do Google
            Icon(
                // ✅ Referencia o drawable que você acabou de criar
                painter = painterResource(id = R.drawable.ic_google_logo),
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified // IMPORTANTE: Impede o Compose de tingir o logo
            )

            // Espaço entre o ícone e o texto
            Spacer(modifier = Modifier.width(24.dp))

            // Texto do botão
            Text(
                text = "Entrar com o Google",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color.DarkGray // Cor do texto mais suave
            )
        }
    }
}