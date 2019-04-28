package br.com.vg.controlededespesas.br.com.vg.controlededespesas.model;

import java.io.Serializable;

public class ReferenciaClass implements Serializable {

    private Integer idReferencia;

    private String descricaoR;

    public ReferenciaClass(){

    }

    public Integer getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(Integer idReferencia) {
        this.idReferencia = idReferencia;
    }

    public String getDescricaoR() {
        return descricaoR;
    }

    public void setDescricaoR(String descricaoR) {
        this.descricaoR = descricaoR;
    }

    @Override
    public String toString(){
        return descricaoR;
    }

}
