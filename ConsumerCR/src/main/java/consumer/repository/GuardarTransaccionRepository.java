//Programacion3
//CarlosRamos
//0905-23-14141

package consumer.repository;

import consumer.model.Transaccion;

// I (Interface Segregation): interfaz especifica para guardar una transaccion.

// O (Open/Closed): si cambia la API, solo se cambia la implementacion.

public interface GuardarTransaccionRepository {

    boolean guardar(Transaccion transaccion);
}
