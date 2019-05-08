package br.com.vg.controlededespesas.br.com.vg.controlededespesas.model;

import java.io.Serializable;

public class RendimentoClass implements Serializable {

    private Integer idRendimento;

    private String descricaoRR;

    private Double valorRend;

    private ReferenciaClass referencia;

    public RendimentoClass(){}

    public Integer getIdRendimento() {
        return idRendimento;
    }

    public void setIdRendimento(Integer idRendimento) {
        this.idRendimento = idRendimento;
    }

    public String getDescricaoRR() {
        return descricaoRR;
    }

    public void setDescricaoRR(String descricaoRR) {
        this.descricaoRR = descricaoRR;
    }

    public ReferenciaClass getReferencia() {
        return referencia;
    }

    public void setReferencia(ReferenciaClass referencia) {
        this.referencia = referencia;
    }

    public Double getValorRend() {
        return valorRend;
    }

    public void setValorRend(Double valorRend) {
        this.valorRend = valorRend;
    }

    @Override
    public String toString() {
        return "Renda: " + descricaoRR + " Valor: " + valorRend ;
    }
}
