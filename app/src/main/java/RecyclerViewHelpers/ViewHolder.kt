package RecyclerViewHelpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eduardo.sanchez.appcrud.R

class ViewHolder (view: View):RecyclerView.ViewHolder(view){
    val txtTitulo:TextView=view.findViewById(R.id.txtTituloCard)
    val txtEstado:TextView=view.findViewById(R.id.txtEstado)
    val imgBorrar:ImageView=view.findViewById(R.id.imgBorrar)
    val imgEditar:ImageView=view.findViewById(R.id.imgEditar)

}