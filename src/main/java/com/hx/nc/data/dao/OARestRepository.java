package com.hx.nc.data.dao;

import com.hx.nc.data.entity.OARestRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author XingJiajun
 * @Date 2019/1/17 14:46
 * @Description
 */
public interface OARestRepository extends JpaRepository<OARestRecord, Long>  {
}
