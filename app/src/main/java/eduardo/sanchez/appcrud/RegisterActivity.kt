package eduardo.sanchez.appcrud

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import modelo.Conexion

class RegisterActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets




        }
        val txtUsuario = findViewById<EditText>(R.id.txtUsuario)
        val txtCorreo = findViewById<EditText>(R.id.txtCorreo)
        val txtContraseña = findViewById<EditText>(R.id.txtContraseña)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val txtIngresar = findViewById<TextView>(R.id.txtIngresar1)

        btnRegistrar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion=Conexion().cadenaConexion()

                val queryRegistrar=objConexion?.prepareStatement("INSERT INTO usuarios (nombre_usuario, email, contraseña) VALUES (?, ?, ?)")!!
                queryRegistrar.setString(1,txtUsuario.text.toString())
                queryRegistrar.setString(2,txtCorreo.text.toString())
                queryRegistrar.setString(3,txtContraseña.text.toString())
                queryRegistrar.executeUpdate()

                Toast.makeText(this@RegisterActivity, "Usuario registrado", Toast.LENGTH_SHORT).show()
            }


        }
        txtIngresar.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}