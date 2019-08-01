package org.r.idea.plugin.generator.impl.parser;

import com.intellij.openapi.roots.ui.configuration.artifacts.sourceItems.actions.PutIntoDefaultLocationActionBase;
import org.r.idea.plugin.generator.impl.nodes.ParamNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author Casper
 * @DATE 2019/8/1 21:48
 **/
public class EntityContainer {

    private static Map<String, ParamNode> map = new TreeMap<>();


    public synchronized static void addEntityType(String type) {
        map.put(type, null);
    }

    public static List<String> getAllKey() {
        return new ArrayList<>(map.keySet());
    }

    public static ParamNode getEntity(String key) {
        return map.get(key);
    }

    public static List<ParamNode> getAllValues() {
        return new ArrayList<>(map.values());
    }


}
