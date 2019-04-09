package com.hx.nc.data.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * @author XingJiajun
 * @Date 2019/1/19 9:54
 * @Description
 */
@Data
@Entity
@Table(name = "polling_record")
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class NCPollingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "poll_at")
    private String pollAt;
    @NotBlank
    @Column(name = "nc_result")
    private String ncResult;
    @Column(name = "task_count")
    private Integer taskCount;
    @NotBlank
    @Column(name = "next_time")
    private String nextTime;
    @NotBlank
    @Column(name = "group_id")
    private String groupId;

    @Builder
    public NCPollingRecord(Long id, @NotBlank String pollAt, @NotBlank String ncResult,
                           Integer taskCount, String nextTime, String groupId) {
        this.id = id;
        this.pollAt = pollAt;
        this.ncResult = ncResult;
        this.taskCount = taskCount;
        this.nextTime = nextTime;
        this.groupId = groupId;
    }
}
