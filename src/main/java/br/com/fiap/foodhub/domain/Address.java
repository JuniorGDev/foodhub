package br.com.fiap.foodhub.domain;

import br.com.fiap.foodhub.dtos.request.AddressRequest;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Address {
    private Long id;
    private String street;
    private int number;
    private String city;
    private String zipCode;

    public Address(
            AddressRequest address
    ) {
        this.street = address.street();
        this.number = address.number();
        this.city = address.city();
        this.zipCode = address.zipCode();
    }
}
