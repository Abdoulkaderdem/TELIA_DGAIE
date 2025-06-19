package com.telia.Lease_management.repository.contract;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telia.Lease_management.entity.contract.Contract;
import com.telia.Lease_management.entity.contract.Invoice;

import java.util.List;
import java.util.Optional;


public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByContract(Contract contract);

    Optional<Invoice> findByContractIdAndInvoicesOrder(Long contractId, int invoicesOrder);

    List<Invoice> findByContractAndStartIntervalAndEndInterval(Contract contract, String startInterval, String endInterval);
    
}
