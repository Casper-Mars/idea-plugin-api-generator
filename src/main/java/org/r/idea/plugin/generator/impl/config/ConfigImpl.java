package org.r.idea.plugin.generator.impl.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.r.idea.plugin.generator.core.builder.DocBuilder;
import org.r.idea.plugin.generator.core.builder.JarBuilder;
import org.r.idea.plugin.generator.core.config.Config;
import org.r.idea.plugin.generator.core.parser.Parser;
import org.r.idea.plugin.generator.core.probe.Probe;
import org.r.idea.plugin.generator.impl.Constants;
import org.r.idea.plugin.generator.impl.Utils;
import org.r.idea.plugin.generator.impl.builder.DocBuilderImpl;
import org.r.idea.plugin.generator.impl.builder.JarBuilderImpl;
import org.r.idea.plugin.generator.impl.parser.InterfaceParser;
import org.r.idea.plugin.generator.impl.probe.FileProbe;
import org.r.idea.plugin.generator.utils.CollectionUtils;
import org.r.idea.plugin.generator.utils.StringUtils;

/**
 * @ClassName ConfigImpl
 * @Author Casper
 * @DATE 2019/6/24 13:49
 **/
public class ConfigImpl implements Config {


    private List<String> interfaceFilePaths;

    private String workSpace;

    private String markdownPath;

    private boolean isDebug;

    public ConfigImpl(List<String> interfaceFilePaths, String workSpace, String baseClass, String markdownPath) {
        init(interfaceFilePaths,workSpace, baseClass, markdownPath, false);
    }

    public ConfigImpl(List<String> interfaceFilePaths, String workSpace, String baseClass, String markdownPath,
        boolean isDebug) {
        init(interfaceFilePaths,workSpace, baseClass, markdownPath, isDebug);
    }


    private void init(List<String> interfaceFilePaths, String workSpace, String baseClass,
        String markdownPath, boolean isDebug) {
        this.isDebug = isDebug;
        this.markdownPath = formatPath(markdownPath);
        setBaseClass(baseClass);
        this.interfaceFilePaths = interfaceFilePaths.stream().map(this::formatPath).collect(Collectors.toList());
        this.workSpace = formatPath(workSpace);
    }


    public void setBaseClass(String baseClass) {
        if (StringUtils.isNotEmpty(baseClass)) {
            Utils.baseClass = merge(baseClass.split(Constants.SPLITOR), Utils.baseClass);
        }
    }

    private String[] merge(String[] arr1, String[] arr2) {
        /*去重性合并两个数组*/
        List<String> list1 = new ArrayList<>(Arrays.asList(arr1));
        List<String> list2 = new ArrayList<>(Arrays.asList(arr2));

        list1.addAll(list2);
        return list1.stream().distinct().filter(t -> !StringUtils.isEmpty(t)).toArray(String[]::new);
    }

    @Override
    public List<String> getInterfaceFilesPath() {
        return interfaceFilePaths;
    }

    @Override
    public String getWorkSpace() {
        return workSpace;
    }

    @Override
    public String getMarkdownPath() {
        return markdownPath;
    }

    @Override
    public boolean isDebug() {
        return this.isDebug;
    }

    private String formatPath(String path) {
        if (StringUtils.isEmpty(path) || path.endsWith("/")) {
            return path;
        }
        return path + "/";
    }


}
