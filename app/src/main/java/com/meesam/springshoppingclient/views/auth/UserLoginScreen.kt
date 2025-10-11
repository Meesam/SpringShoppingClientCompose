package com.meesam.springshoppingclient.views.auth


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meesam.springshoppingclient.views.theme.AppTheme

@Composable
fun UserLoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(top = 50.dp, bottom = 20.dp),
        ) {

        Text(
            "Welcome back",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            //color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            "Sign In to your registered account",
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
           // color = MaterialTheme.colorScheme.onSurface
        )

        ElevatedCard (modifier.fillMaxWidth().padding(top = 50.dp)) {
           Column(modifier
               .fillMaxWidth()
               .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 20.dp)) {
               OutlinedTextField(
                   value = "",
                   onValueChange = {},
                   placeholder = {
                       Text("Enter your email")
                   },
                   label = {
                       Text("Email")
                   },
                   shape = MaterialTheme.shapes.medium,
                  // textStyle = MaterialTheme.typography.bodyLarge,
                   modifier = Modifier
                       .fillMaxWidth()
               )
               Spacer(modifier = Modifier.height(10.dp))
               OutlinedTextField(
                   value = "",
                   onValueChange = {},
                   placeholder = {
                       Text("Enter your password")
                   },
                   label = {
                       Text("Password")
                   },
                   shape = MaterialTheme.shapes.medium,
                   //textStyle = MaterialTheme.typography.bodySmall,
                   modifier = Modifier
                       .fillMaxWidth()
               )

               Row(modifier = Modifier.fillMaxWidth()) {
                   TextButton(onClick = {}) {
                       Text("Forgot Password?", textAlign = TextAlign.End, modifier =  Modifier.fillMaxWidth())
                   }
               }

               Button(
                   onClick = onNavigateToRegister,
                   modifier = Modifier.fillMaxWidth(),
                   shape = MaterialTheme.shapes.medium,
                   elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                   colors = ButtonDefaults.buttonColors(
                       containerColor = MaterialTheme.colorScheme.primary,
                       contentColor = MaterialTheme.colorScheme.onPrimary
                   )
               ) {
                   Text("Sign In", style = MaterialTheme.typography.titleSmall)
               }

               Spacer(modifier = Modifier.height(10.dp))

               Text("Don't have an Account?", modifier = Modifier.fillMaxWidth().clickable {
                   onNavigateToRegister()
               }, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary,
                   textAlign = TextAlign.Center)
           }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun UserLoginScreenPre(modifier: Modifier = Modifier) {
    AppTheme {
        UserLoginScreen(onLoginSuccess = {}) {}
    }
}