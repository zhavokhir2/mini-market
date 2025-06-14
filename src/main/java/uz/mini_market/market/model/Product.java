package uz.mini_market.market.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Название продукта не может быть пустым")
    @Size(max = 255, message = "Название продукта не может быть длиннее 255 символов")
    private String name;

    @Column(length = 1000)
    private String description;

    @NotNull(message = "Цена не может быть пустой")
    private BigDecimal price;

    @Pattern(regexp = "^(http|https)://.*", message = "Неверный формат URL изображения")
    private String imageUrl;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;
}
