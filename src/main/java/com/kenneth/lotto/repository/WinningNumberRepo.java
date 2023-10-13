package com.kenneth.lotto.repository;

import com.kenneth.lotto.model.WinningNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface WinningNumberRepo extends JpaRepository<WinningNumber,Integer> {}