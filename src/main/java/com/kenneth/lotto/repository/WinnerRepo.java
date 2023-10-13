package com.kenneth.lotto.repository;

import com.kenneth.lotto.model.Winner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface WinnerRepo extends JpaRepository<Winner,Integer> {}