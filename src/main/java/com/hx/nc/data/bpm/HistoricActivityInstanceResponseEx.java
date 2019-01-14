package com.hx.nc.data.bpm;

import lombok.Data;
import yonyou.bpm.rest.response.historic.HistoricActivityInstanceResponse;

@Data
public class HistoricActivityInstanceResponseEx extends HistoricActivityInstanceResponse {

	private String userName;

}

