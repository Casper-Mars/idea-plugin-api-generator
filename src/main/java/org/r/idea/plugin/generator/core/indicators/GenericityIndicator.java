package org.r.idea.plugin.generator.core.indicators;

import java.util.List;

/**
 * @ClassName GenericityIndicator
 * @Author Casper
 * @DATE 2019/6/21 15:04
 **/
public interface GenericityIndicator {


    /**
     * 指示是否为泛型类型，如果是返回true，并把实际类型放进param中，否则返回false，
     *
     * @param name 全路径名称
     */
    boolean isGenricityType(String name, List<String> param);


}
