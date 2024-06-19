package modelo
data class Ticket(
        val ticketId: Int,
        val titulo: String,
        val descripcion: String,
        val autorId: Int,
        val emailContacto: String,
        val estado: String,
        val fechaCreacion: String
    )
