package ee.example.grocerystoreNPTV23.interfaces;

import ee.example.grocerystoreNPTV23.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    /**
     * Найти покупки за определенный период (между началом и концом).
     *
     * @param date    начало периода.
     * @param dateEnd конец периода.
     * @return список покупок.
     */
    List<Purchase> findByPurchaseDateBetween(LocalDateTime date, LocalDateTime dateEnd);
}
