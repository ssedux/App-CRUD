package eduardo.sanchez.appcrud

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import modelo.Conexion
import java.sql.ResultSet

class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtUsuario=findViewById<EditText>(R.id.txtUsuario1)
        val txtContraseña=findViewById<EditText>(R.id.txtContraseña1)
        val btnIngresar=findViewById<Button>(R.id.btnIngresar1)
        val txtRegistrar=findViewById<TextView>(R.id.txtRegistrar1)

        fun loginUsuario(Usuario: String, Contraseña: String): Boolean {
            val objConexion= Conexion().cadenaConexion()
            return try {
                val queryIngresar=objConexion?.prepareStatement("SELECT * FROM usuarios WHERE nombre_usuario = ? AND contraseña = ?")!!
                queryIngresar.setString(1,Usuario)
                queryIngresar.setString(2,Contraseña)
                val resultSet:ResultSet=queryIngresar.executeQuery()
                val exists= resultSet.next()

                resultSet.close()
                queryIngresar.close()
                objConexion.close()

                exists
            }
            catch (e:Exception){
                e.printStackTrace()
                false
            }

        }
        btnIngresar.setOnClickListener {
            val Usuario = txtUsuario.text.toString()
            val Contraseña = txtContraseña.text.toString()
            if (loginUsuario(Usuario,Contraseña)) {
            val intent1= Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(intent1)
            }
        }

        txtRegistrar.setOnClickListener {
            val intent=Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
        }

        }
    }