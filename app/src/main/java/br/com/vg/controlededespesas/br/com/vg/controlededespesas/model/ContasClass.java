package br.com.vg.controlededespesas.br.com.vg.controlededespesas.model;
import java.io.Serializable;

public class ContasClass implements Serializable {

    private Integer idContas;
    private String descricaoC;
    private Boolean pago;
    private Double valor;
    private ReferenciaClass referencia;
    private TipoClass tipo;

    public ContasClass(){
    }

    public Integer getIdContas() {
        return idContas;
    }

    public void setIdContas(Integer idContas) {
        this.idContas = idContas;
    }

    public String getDescricaoC() {
        return descricaoC;
    }

    public void setDescricaoC(String descricaoC) {
        this.descricaoC = descricaoC;
    }

    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public ReferenciaClass getReferencia() {
        return referencia;
    }

    public void setReferencia(ReferenciaClass referencia) {
        this.referencia = referencia;
    }

    public TipoClass getTipo(){
        return tipo;
    }

    public void setTipo(TipoClass tipo) {
       this.tipo =  tipo;
    }

    @Override
    public String toString() {
        return "Nome: " +  descricaoC + ", valor: " + valor + " T: " + (tipo == null ? "" : tipo.getDescricaoT());
    }
}
