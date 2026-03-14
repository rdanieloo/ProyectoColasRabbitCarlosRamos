//Programacion3
//CarlosRamos
//0905-23-14141

package consumer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Detalle {
    private String nombreBeneficiario;
    private String tipoTransferencia;
    private String descripcion;
    private Referencias referencias;

    public Detalle() {}

    public String getNombreBeneficiario() { return nombreBeneficiario; }
    public void setNombreBeneficiario(String v) { this.nombreBeneficiario = v; }

    public String getTipoTransferencia() { return tipoTransferencia; }
    public void setTipoTransferencia(String v) { this.tipoTransferencia = v; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String v) { this.descripcion = v; }

    public Referencias getReferencias() { return referencias; }
    public void setReferencias(Referencias v) { this.referencias = v; }
}
