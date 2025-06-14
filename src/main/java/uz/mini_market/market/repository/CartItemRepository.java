package uz.mini_market.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mini_market.market.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
