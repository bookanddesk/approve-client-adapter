package com.hx.nc.data.entity;

import com.hx.nc.data.OARestTypeConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Date;

/**
 * @author XingJiajun
 * @Date 2019/1/17 13:47
 * @Description
 */
@Data
@Entity
@Table(name = "oa_rest_record")
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class OARestRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rest_at")
    private Date restAt;

//    @NotBlank
    private String url;

    @NotBlank
    private String params;

    private String result;

    private short success;

    @Convert(converter = OARestTypeConverter.class)
    private Type type;

    @Builder
    public OARestRecord(Long id, Date restAt, @NotBlank String url, @NotBlank String params, String result, short success, Type type) {
        this.id = id;
        this.restAt = restAt;
        this.url = url;
        this.params = params;
        this.result = result;
        this.success = success;
        this.type = type;
    }

    @PrePersist
    void restAt() {
        this.restAt = Date.from(Instant.now());
    }

    public enum Type {
        sendTask, updateTask
    }

}
