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
public class PollingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "poll_at")
    private String pollAt;
    @Column(name = "task_count")
    private Integer taskCount;
    @NotBlank
    @Column(name = "next_time")
    private String nextTime;

    @Builder
    public PollingRecord(Long id, @NotBlank String pollAt, Integer taskCount, String nextTime) {
        this.id = id;
        this.pollAt = pollAt;
        this.taskCount = taskCount;
        this.nextTime = nextTime;
    }
}
