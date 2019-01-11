package com.hx.nc.data.bpm;

import lombok.Data;
import yonyou.bpm.rest.response.form.FormFieldResponse;

@Data
public class FormFieldResponseEx extends FormFieldResponse {
	private String tableFieldName;
}

