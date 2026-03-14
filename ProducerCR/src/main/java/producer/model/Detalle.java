//Programacion3
//CarlosRamos
//0905-23-14141

package producer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Detalle {
    private String nombreBeneficiario;
    private String tipoTransferencia;
    private String descripcion;
    private Referencias referencias;

    public Detalle() {}

    public String getNombreBeneficiario() { return nombreBeneficiario; }
    public void setNombreBeneficiario(String nombreBeneficiario) { this.nombreBeneficiario = nombreBeneficiario; }

    public String getTipoTransferencia() { return tipoTransferencia; }
    public void setTipoTransferencia(String tipoTransferencia) { this.tipoTransferencia = tipoTransferencia; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Referencias getReferencias() { return referencias; }
    public void setReferencias(Referencias referencias) { this.referencias = referencias; }
}
