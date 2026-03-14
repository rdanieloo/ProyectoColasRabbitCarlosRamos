//Programacion3
//CarlosRamos
//0905-23-14141

package consumer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaccion {

    // Campos originales de la API GET
    private String idTransaccion;
    private double monto;
    private String moneda;
    private String cuentaOrigen;
    private String bancoDestino;
    private Detalle detalle;

    // Campos adicionales requeridos 
    private String nombre;
    private String carnet;
    private String correo;

    public Transaccion() {}

    public String getIdTransaccion() { return idTransaccion; }
    public void setIdTransaccion(String v) { this.idTransaccion = v; }

    public double getMonto() { return monto; }
    public void setMonto(double v) { this.monto = v; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String v) { this.moneda = v; }

    public String getCuentaOrigen() { return cuentaOrigen; }
    public void setCuentaOrigen(String v) { this.cuentaOrigen = v; }

    public String getBancoDestino() { return bancoDestino; }
    public void setBancoDestino(String v) { this.bancoDestino = v; }

    public Detalle getDetalle() { return detalle; }
    public void setDetalle(Detalle v) { this.detalle = v; }

    public String getNombre() { return nombre; }
    public void setNombre(String v) { this.nombre = v; }

    public String getCarnet() { return carnet; }
    public void setCarnet(String v) { this.carnet = v; }

    public String getCorreo() { return correo; }
    public void setCorreo(String v) { this.correo = v; }
}
