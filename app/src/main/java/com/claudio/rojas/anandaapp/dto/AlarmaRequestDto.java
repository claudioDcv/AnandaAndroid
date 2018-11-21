package com.claudio.rojas.anandaapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmaRequestDto {
    Long usuario;
    String tipo;
    String descripcion;
    String hora;
    String fecha;
}
