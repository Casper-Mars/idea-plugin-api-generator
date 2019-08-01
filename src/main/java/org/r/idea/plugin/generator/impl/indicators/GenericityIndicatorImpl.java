package org.r.idea.plugin.generator.impl.indicators;

import java.util.Arrays;
import java.util.List;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.core.indicators.GenericityIndicator;

/**
 * @ClassName GenericityIndicatorImpl
 * @Author Casper
 * @DATE 2019/6/21 15:09
 **/
public class GenericityIndicatorImpl implements GenericityIndicator {

    /**
     * 指示是否为泛型类型，如果是返回true，并把实际类型放进param中，否则返回false，
     *
     * @param name 全路径名称
     */
    @Override
    public boolean isGenricityType(String name, List<String> param) {

        int left = name.indexOf('<');
        int right = name.lastIndexOf('>');
        if (left - right != 0) {
            String substring = name.substring(left + 1, right);
            String[] split = substring.split(Constants.SPLITOR);
            param.addAll(Arrays.asList(split));
            return true;
        }
        return false;
    }
}
