package com.stocktrading.repository;

import com.stocktrading.model.Portfolio;
import com.stocktrading.model.User;
import com.stocktrading.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByUser(User user);
    Optional<Portfolio> findByUserAndStock(User user, Stock stock);
}
