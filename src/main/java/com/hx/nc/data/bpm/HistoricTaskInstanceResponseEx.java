package com.hx.nc.data.bpm;

import lombok.Data;
import yonyou.bpm.rest.response.historic.HistoricTaskInstanceResponse;

@Data
public class HistoricTaskInstanceResponseEx extends HistoricTaskInstanceResponse {
	private String userName;
}

