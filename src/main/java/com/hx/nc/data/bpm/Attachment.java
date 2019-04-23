package com.hx.nc.data.bpm;

import lombok.Data;
import yonyou.bpm.rest.response.AttachmentResponse;

@Data
public class Attachment extends AttachmentResponse {
    private String author;
    private Long time;
    private String filename;
}
