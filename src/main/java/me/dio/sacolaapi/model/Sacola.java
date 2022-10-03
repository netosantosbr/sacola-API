package me.dio.sacolaapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.dio.sacolaapi.enumeration.FormaPagamento;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor //Define um construtor que inicializa todos os atributos da classe (LOMBOK)
@Builder //Facilita a criação do objeto (LOMBOK)
@NoArgsConstructor //Define um construtor vazio, exigido pelo hibernate. (LOMBOK)
@Data // Define os métodos getters e setters (LOMBOK)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity //JPA
public class Sacola {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private Cliente cliente;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> itensSacola;
    private double valorTotal;
    @Enumerated
    private FormaPagamento formaPagamento;
    private Boolean fechada;

}
