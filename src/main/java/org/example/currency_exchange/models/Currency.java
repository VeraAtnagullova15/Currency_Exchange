package org.example.currency_exchange.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

    private int id;
    private String code;
    private String fullName;
    private String sign;
}
