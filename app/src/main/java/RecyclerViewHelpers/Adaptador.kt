package RecyclerViewHelpers

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import eduardo.sanchez.appcrud.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import modelo.Conexion
import modelo.Ticket

class Adaptador(var Datos:List<Ticket>):RecyclerView.Adapter<ViewHolder>() {

    fun actualizarListado(nuevaLista:List<Ticket>){
        Datos=nuevaLista
        notifyDataSetChanged()
    }

    fun eliminarDatos(tituloTicket: String,posicion:Int){
        val listaDatos=Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch (Dispatchers.IO){
            val objConexion=Conexion().cadenaConexion()

            val deleteTicket=objConexion?.prepareStatement("delete tickets where titulo=?")!!
            deleteTicket.setString(1,tituloTicket)
            deleteTicket.executeUpdate()

            val commit=objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }
        Datos=listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    fun actualizarDato(
        nuevoTitulo:String,
        Estado: Int,
        Descripcion:String,
        Contacto:String,
        idAutor:Int){
        GlobalScope.launch(Dispatchers.IO){
            val objConexion=Conexion().cadenaConexion()

            val updateTicket=objConexion?.prepareStatement("update tickets set titulo=?,estado=?,descripcion=? email_contacto=? WHERE ticket_id=?")!!
            updateTicket.setString(1,nuevoTitulo)
            updateTicket.setString(2, Estado.toString())
            updateTicket.setString(3,Descripcion)
            updateTicket.setString(4,Contacto)
            updateTicket.setInt(5,idAutor)
            updateTicket.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista=LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card,parent,false)
        return ViewHolder(vista)
    }

    override fun getItemCount()=Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=Datos[position]
        holder.txtTitulo.text=item.titulo

        holder.imgBorrar.setOnClickListener {
            val context=holder.itemView.context

            val builder = AlertDialog.Builder(context)

            builder.setTitle("Confirmación")
            builder.setMessage("¿Está seguro que quiere borrar?")

            builder.setPositiveButton("Si"){ Dialog,wich->eliminarDatos(item.titulo,position)}
            builder.setNegativeButton("No"){Dialog,wich->Dialog.dismiss()}
            val dialog=builder.create()
            dialog.show()
    }
        holder.imgEditar.setOnClickListener {
            val context= holder.itemView.context

            val builder=AlertDialog.Builder(context)
            builder.setTitle("Actualizar")

            val cuadroTexto= EditText(context)
            cuadroTexto.setHint(item.titulo)
            builder.setView(cuadroTexto)

            builder.setPositiveButton("Actualizar"){
                Dialog,wich->actualizarDato(cuadroTexto.text.toString(),item.ticketId)
            }
            builder.setNegativeButton("Cancelar"){
                Dialog,wich->Dialog.dismiss()
            }
            val dialog=builder.create()
            dialog.show()
        }
}}