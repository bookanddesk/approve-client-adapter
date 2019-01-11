package com.hx.nc.data.bpm;

import lombok.Data;
import yonyou.bpm.rest.response.form.FormResponse;

@Data
public class FormResponseEx extends FormResponse {
	private String tableName;
}

