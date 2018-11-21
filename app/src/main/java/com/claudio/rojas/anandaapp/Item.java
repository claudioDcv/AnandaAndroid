package com.claudio.rojas.anandaapp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {

    Long id;
    String name;

    public Item(String n, Long id) {
        this.name = n;
        this.id = id;
    }
}
