package com.claudio.rojas.anandaapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmaResponseDto {

    Long id;
    Long usuario;
    String tipo;
    String descripcion;
    String hora;
    String fecha;
}
