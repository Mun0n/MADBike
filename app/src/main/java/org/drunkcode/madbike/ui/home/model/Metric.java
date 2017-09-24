package org.drunkcode.madbike.ui.home.model;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.utils.JSONUtils;

/**
 * Created by mun0n on 6/6/16.
 */
public class Metric {

    @SerializedName(JSONUtils.FORMULA)
    private String formula;
    @SerializedName(JSONUtils.UNIT)
    private String unit;
    @SerializedName(JSONUtils.VALUES)
    private double[] values;

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }
}
