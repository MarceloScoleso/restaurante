package com.jmj.restaurante.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "restaurantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 255)
    private String endereco;

    private String telefone;

    private Integer capacidade;

    @OneToMany(mappedBy = "restaurante", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Mesa> mesas;

}
