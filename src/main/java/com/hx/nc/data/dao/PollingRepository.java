package com.hx.nc.data.dao;

import com.hx.nc.data.entity.PollingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author XingJiajun
 * @Date 2019/1/19 10:04
 * @Description
 */
public interface PollingRepository extends JpaRepository<PollingRecord, Long> {
    @Query(value = "SELECT MAX(NEXT_TIME) FROM POLLING_RECORD", nativeQuery = true)
    String selectLastPollingTime();

    @Transactional
    int deleteByPollAtBefore(String pollAt);
}
