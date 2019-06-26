package org.r.idea.plugin.generator.core.probe;

import com.intellij.psi.PsiClass;
import java.io.File;
import java.io.FileFilter;
import java.util.List;
import org.r.idea.plugin.generator.core.beans.FileBO;
import org.r.idea.plugin.generator.core.config.ServerManager;

/**
 * @ClassName Probe
 * @Author Casper
 * @DATE 2019/6/21 16:54
 **/

public interface Probe {

    static Probe getInstance() {
        return ServerManager.getServer(Probe.class);
    }

    List<PsiClass> getAllInterfaceClass(List<String> interfaceFilePath);

    void writerFile(String filename, String content);

    List<File> searchFile(String searchPath, FileFilter fileFilter);


}
