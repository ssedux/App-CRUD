package eduardo.sanchez.appcrud

import RecyclerViewHelpers.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.Conexion
import modelo.Ticket
import modelo.dataClassTicket

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

   val txtTitulo=findViewById<EditText>(R.id.txtTitulo)
   val txtContacto=findViewById<EditText>(R.id.txtContacto)
   val txtDescripcion=findViewById<EditText>(R.id.txtDescripcion)
   val txtAutor=findViewById<EditText>(R.id.txtAutor)
   val btnAgregar=findViewById<Button>(R.id.btnAgregar)

   val rcvTickets=findViewById<RecyclerView>(R.id.rcvTickets)
   rcvTickets.layoutManager=LinearLayoutManager(this)

        fun mostrarDatos():List<Ticket>{
            val objConexion = Conexion().cadenaConexion()

            val statement=objConexion?.createStatement()
            val resultSet=statement?.executeQuery("SELECT*FROM tickets")!!

            val Tickets= mutableListOf<Ticket>()

            while (resultSet.next()){
                val ticketId=resultSet.getInt("ticket_id")
                val titulo=resultSet.getString("titulo")
                val descripcion=resultSet.getString("descripcion")
                val autorId=resultSet.getInt("autor_id")
                val emailContacto=resultSet.getString("email_contacto")
                val estado=resultSet.getString("estado")
                val fechaCreacion=resultSet.getString("fecha_creacion")
                val Ticketadd=Ticket(ticketId,titulo,descripcion,autorId,emailContacto,estado,fechaCreacion)
                Tickets.add(Ticketadd)
            }
            return Tickets
        }
        CoroutineScope(Dispatchers.IO).launch {
            val TicketDB=mostrarDatos()
            withContext(Dispatchers.Main){
                val miAdapatador=Adaptador(TicketDB)
                rcvTickets.adapter=miAdapatador
            }
        }

        btnAgregar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion=Conexion().cadenaConexion()

                val addTicket=objConexion?.prepareStatement("INSERT INTO tickets (titulo,email_contacto,descripcion,autor_id) VALUES(?,?,?,?)")!!
                addTicket.setString(1,txtTitulo.text.toString())
                addTicket.setString(2,txtContacto.text.toString())
                addTicket.setString(3,txtDescripcion.text.toString())
                addTicket.setInt(4,txtAutor.text.toString().toInt())
                addTicket.executeUpdate()

                val nuevoTicket=mostrarDatos()
                withContext(Dispatchers.Main){
                    (rcvTickets.adapter as? Adaptador)?.actualizarListado(nuevoTicket)
                    txtTitulo.setText("")
                    txtContacto.setText("")
                    txtDescripcion.setText("")
                    txtAutor.setText("")
                }
            }
        }
    }
}