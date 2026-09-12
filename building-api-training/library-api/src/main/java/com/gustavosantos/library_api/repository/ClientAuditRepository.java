package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.model.ClientAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientAuditRepository extends JpaRepository<ClientAudit, Long> {
}
