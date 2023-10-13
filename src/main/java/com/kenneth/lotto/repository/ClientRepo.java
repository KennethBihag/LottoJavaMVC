package com.kenneth.lotto.repository;

import com.kenneth.lotto.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ClientRepo extends JpaRepository<Client,Integer> {}