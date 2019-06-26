package org.r.idea.plugin.generator.core.probe;

import com.intellij.psi.PsiClass;
import java.io.File;
import java.io.FileFilter;
import java.util.List;
import org.r.idea.plugin.generator.core.beans.FileBO;

/**
 * @ClassName Probe
 * @Author Casper
 * @DATE 2019/6/21 16:54
 **/

public interface Probe {

    List<PsiClass> getAllInterfaceClass(List<String> interfaceFilePath);

    String saveDoc(List<FileBO> docList, String workSpace);


    List<File> searchFile(String searchPath, FileFilter fileFilter);


}
