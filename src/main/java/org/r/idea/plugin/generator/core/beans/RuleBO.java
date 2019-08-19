package org.r.idea.plugin.generator.core.beans;

import java.math.BigDecimal;

public class RuleBO {


    /**
     * 正则表达式
     */
    private String pattern;

    /**
     * 最大值
     */
    private BigDecimal max;

    /**
     * 最小值
     */
    private BigDecimal min;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }
}
