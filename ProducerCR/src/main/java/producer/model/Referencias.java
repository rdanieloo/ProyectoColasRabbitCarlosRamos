//Programacion3
//CarlosRamos
//0905-23-14141

package producer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Referencias {
    private String factura;
    private String codigoInterno;

    public Referencias() {}

    public String getFactura() { return factura; }
    public void setFactura(String factura) { this.factura = factura; }

    public String getCodigoInterno() { return codigoInterno; }
    public void setCodigoInterno(String codigoInterno) { this.codigoInterno = codigoInterno; }
}
