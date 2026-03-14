//Programacion3
//CarlosRamos
//0905-23-14141

package producer.repository;

import producer.model.LoteTransacciones;

 // I (Interface Segregation): interfaz especifica solo para obtener transacciones.
 // O (Open/Closed): si cambia la API, se crea otra implementacion sin tocar el resto.
 
public interface TransaccionRepository {

    LoteTransacciones obtenerTransacciones() throws Exception;
}
