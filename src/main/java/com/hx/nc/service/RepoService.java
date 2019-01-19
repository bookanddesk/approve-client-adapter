package com.hx.nc.service;

import com.hx.nc.data.dao.OARestRepository;
import com.hx.nc.data.dao.PollingRepository;
import com.hx.nc.data.entity.OARestRecord;
import com.hx.nc.data.entity.PollingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/19 10:05
 * @Description
 */
@Service
public class RepoService {

    private OARestRepository oaRestRepository;
    private PollingRepository pollingRepository;

    @Autowired
    public RepoService(OARestRepository oaRestRepository, PollingRepository pollingRepository) {
        this.oaRestRepository = oaRestRepository;
        this.pollingRepository = pollingRepository;
    }

    public OARestRecord saveOARestRecord(OARestRecord restRecord) {
        return oaRestRepository.save(restRecord);
    }

    public List<OARestRecord> listAllOARestRecords() {
        return oaRestRepository.findAll();
    }

    public Page<OARestRecord> pageOARestRecords(Pageable pageable) {
        return oaRestRepository.findAll(pageable);
    }

    public PollingRecord savePollingRecord(PollingRecord pollingRecord) {
        return pollingRepository.save(pollingRecord);
    }

    public List<PollingRecord> listAllPollingRecords() {
        return pollingRepository.findAll();
    }

    public Page<PollingRecord> pagePollingRecords(Pageable pageable) {
        return pollingRepository.findAll(pageable);
    }

    public String getLastPollingRecord() {
        return pollingRepository.selectLastPollingTime();
    }

    public void deleteRedundantPollingRecords(String pollAtBefore) {
        pollingRepository.deleteByPollAtBefore(pollAtBefore);
    }

}
