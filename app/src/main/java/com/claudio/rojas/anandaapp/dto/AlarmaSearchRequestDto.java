package com.claudio.rojas.anandaapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmaSearchRequestDto {
    Long usuario;
    String hora;
    String fecha;
}