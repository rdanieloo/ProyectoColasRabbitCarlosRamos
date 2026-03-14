# Proyecto: Colas con Rabbit Carlos Ramos

## Video de Demostración del Proyecto

Link del video: https://drive.google.com/file/d/1dUNWtTDNhA4mc_i2vaBvSs0dCgkz_bQH/view?usp=sharing

Espero que le parezca bien, gracias...

## Descripcion General

Este proyecto fue desarrollado para la clase de Programacion 3. Consiste en un sistema que procesa transacciones bancarias utilizando RabbitMQ como intermediario de mensajeria, aplicando el patron Producer-Consumer.

El sistema obtiene un lote de transacciones bancarias desde una API externa, las distribuye en colas segun el banco destino, y luego las procesa enviandolas a otra API para guardarlas en una base de datos.

## Arquitectura

El proyecto esta dividido en dos componentes independientes que se comunican a traves de RabbitMQ:

API GET (transacciones)
        |
        v
   ProducerCR
        |
        v
   RabbitMQ (una cola por banco)
        |
        v
   ConsumerCR
        |
        v
API POST (guardarTransacciones)

### ProducerCR

Este componente se encarga de obtener las transacciones y publicarlas en RabbitMQ. Su flujo es el siguiente:

1. Llama al endpoint GET de la API del docente para obtener el lote de transacciones.
2. Recorre cada transaccion y lee el campo bancoDestino.
3. Crea una cola en RabbitMQ con el nombre del banco si no existe.
4. Publica el JSON de la transaccion en esa cola.

### ConsumerCR

Este componente se encarga de escuchar las colas y guardar cada transaccion. Su flujo es el siguiente:

1. Se conecta a RabbitMQ y escucha las 11 colas de bancos configuradas.
2. Cuando llega un mensaje, lo deserializa a un objeto Java.
3. Modifica el campo idTransaccion concatenando un UUID para que sea unico.
4. Agrega los datos del estudiante: nombre, carnet y correo.
5. Llama al endpoint POST para guardar la transaccion en la base de datos.
6. Si el POST responde exitosamente, envia un ACK para confirmar el mensaje.
7. Si el POST falla, envia un NACK para que RabbitMQ reencole el mensaje y no se pierda.

## Estructura de los proyectos

ProducerCR
    pom.xml
    src/main/java/producer/
        MainProducerCR.java
        config/
            Configuracion.java
        model/
            LoteTransacciones.java
            Transaccion.java
            Detalle.java
            Referencias.java
        service/
            ApiService.java
            RabbitMQService.java

ConsumerCR
    pom.xml
    src/main/java/consumer/
        MainConsumerCR.java
        config/
            Configuracion.java
        model/
            Transaccion.java
            Detalle.java
            Referencias.java
        service/
            ApiPostService.java
            RabbitMQConsumerService.java

## Descripcion de las clases principales

MainProducerCR.java
Es el punto de entrada del Producer. Inicializa el ApiService y el RabbitMQService, luego ejecuta el flujo completo: obtener transacciones y publicarlas en RabbitMQ.

MainConsumerCR.java
Es el punto de entrada del Consumer. Inicializa el ApiPostService y el RabbitMQConsumerService, luego inicia la escucha de todas las colas configuradas. El programa permanece corriendo indefinidamente esperando mensajes.

Configuracion.java (Producer)
Contiene los datos de conexion a RabbitMQ: host, puerto, usuario y contrasena. Tambien tiene la URL de la API GET.

Configuracion.java (Consumer)
Contiene los datos de conexion a RabbitMQ, la URL de la API POST, los datos del estudiante (nombre, carnet y correo), la lista de bancos a escuchar y el numero maximo de reintentos.

ApiService.java
Se encarga de llamar al endpoint GET y retornar el lote de transacciones deserializado en objetos Java usando Jackson.

RabbitMQService.java
Maneja la conexion con RabbitMQ, declara las colas dinamicamente segun el banco destino y publica cada transaccion como mensaje JSON.

ApiPostService.java
Se encarga de enviar cada transaccion al endpoint POST del docente. Implementa un mecanismo de reintentos: si el POST falla, lo intenta hasta 3 veces antes de retornar un fallo.

RabbitMQConsumerService.java
Es la clase mas importante del Consumer. Se conecta a RabbitMQ, escucha todas las colas de bancos y procesa cada mensaje con ACK manual. Aqui se modifica el idTransaccion con el UUID y se agregan los datos del estudiante.

## Decisiones que tome en el proyecto

UUID en el idTransaccion
Decidi concatenar un UUID al idTransaccion original para garantizar que cada registro guardado en la base de datos sea unico. Por ejemplo, TX-10001 se convierte en TX-10001-a3f7c2b1-4b2d. Esto lo sugiri el catedratico en el grupo de estudio.

Campo correo
Las instrucciones pedian agregar nombre y carnet. Sin embargo, en la conversacion del grupo el catedratico mostro que tambien esperaba el campo correo en el payload, por lo que lo agregue tambien.

ACK manual
Implemente el ACK manual en el Consumer para garantizar que ningun mensaje se pierda. Un mensaje solo se elimina de la cola si el POST responde exitosamente. Si el POST falla, el mensaje se reencola automaticamente.

Respuesta 201 del POST
Durante el desarrollo note que la API del docente responde con codigo 201 en lugar de 200 al guardar una transaccion. Consulte esto en el grupo y el catedratico confirmo que el 201 es correcto porque significa que el registro fue creado exitosamente. Ajuste el codigo para aceptar ambas respuestas como exitosas.

Estructura por paquetes
Organice el codigo en paquetes separados (config, model, service) para mantener el proyecto ordenado y que cada clase tenga una responsabilidad clara.

## Tecnologias utilizadas

- Java 17
- Maven
- RabbitMQ 4.2.4
- Erlang 28.4.1
- Jackson para parsear JSON
- Java HttpClient para llamadas HTTP
- Amazon DynamoDB como base de datos del docente, accesada via API

## Requisitos para ejecutar

- Java 17 instalado
- Maven instalado
- RabbitMQ corriendo en localhost:5672
- Conexion a internet para acceder a las APIs del docente

## Como ejecutar

1. Importar ambos proyectos en Eclipse como Maven Projects.
2. Esperar que Eclipse descargue las dependencias.
3. Modificar los datos del estudiante en ConsumerCR/src/main/java/consumer/config/Configuracion.java.
4. Ejecutar primero MainConsumerCR.java, debe estar escuchando antes de que lleguen mensajes.
5. Ejecutar MainProducerCR.java en otra ventana.
6. Verificar en http://localhost:15672 que las colas se crean y se procesan correctamente.

## APIs utilizadas

GET https://hly784ig9d.execute-api.us-east-1.amazonaws.com/default/transacciones
Obtiene el lote completo de transacciones bancarias.

POST https://7e0d9ogwzd.execute-api.us-east-1.amazonaws.com/default/guardarTransacciones
Recibe una transaccion individual y la guarda en la base de datos.
