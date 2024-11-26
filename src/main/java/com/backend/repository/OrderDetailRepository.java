package com.backend.repository;

import com.backend.entity.OrderDetail;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {}
