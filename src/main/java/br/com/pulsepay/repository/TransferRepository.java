
package br.com.pulsepay.repository;

import br.com.pulsepay.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    // Buscar somente transferências das quais a conta participou
    List<Transfer> findBySender_IdOrReceiver_Id(Long senderId, Long receiverId);

}
