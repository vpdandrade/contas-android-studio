package br.com.vg.controlededespesas.br.com.vg.controlededespesas.model;

import java.io.Serializable;

public class TipoClass implements Serializable {

    private Integer idTipo;

    private String descricaoT;

    public TipoClass(){
    }

    public Integer getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public String getDescricaoT() {
        return descricaoT;
    }

    public void setDescricaoT(String descricaoT) {
        this.descricaoT = descricaoT;
    }

    @Override
    public String toString(){
        return descricaoT;
    }
}
