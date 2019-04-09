package com.hx.nc.service;

import com.hx.nc.data.dao.OARestRepository;
import com.hx.nc.data.dao.PollingRepository;
import com.hx.nc.data.entity.NCPollingRecord;
import com.hx.nc.data.entity.OARestRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/19 10:05
 * @Description
 */
@Service
public class RepoService {

    private final OARestRepository oaRestRepository;
    private final PollingRepository pollingRepository;
//    private final OAUserRepository oaUserRepository;

    @Autowired
    public RepoService(OARestRepository oaRestRepository, PollingRepository pollingRepository) {
        this.oaRestRepository = oaRestRepository;
        this.pollingRepository = pollingRepository;
//        this.oaUserRepository = oaUserRepository;
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

    public NCPollingRecord savePollingRecord(NCPollingRecord pollingRecord) {
        return pollingRepository.save(pollingRecord);
    }

    public List<NCPollingRecord> listAllPollingRecords() {
        return pollingRepository.findAll();
    }

    public Page<NCPollingRecord> pagePollingRecords(Pageable pageable) {
        return pollingRepository.findAll(pageable);
    }

    public String getLastPollingRecord(String groupId) {
        return pollingRepository.selectLastPollingTime(groupId);
    }

    public void deleteRedundantPollingRecords(String pollAtBefore) {
        pollingRepository.deleteByPollAtBefore(pollAtBefore);
    }

//    public List<OAUser> findAllOAUserById(Set<String> ids) {
//        return oaUserRepository.findAllById(ids);
//    }
//
//    public void saveOAUserInfo(Iterable<OAUser> oaUsers) {
//        oaUserRepository.saveAll(oaUsers);
//    }

}
