package org.r.idea.plugin.generator.impl.upload;

import com.jcraft.jsch.*;
import org.r.idea.plugin.generator.core.exceptions.UplodaException;
import org.r.idea.plugin.generator.core.upload.Deliveryman;

import java.io.*;

/**
 * @author casper
 */ // TODO: 19-8-19 完善上传功能，添加错误异常处理，增强健壮性
public class DeliverymanImpl implements Deliveryman {


    private String localFilePath;

    private String localFilename;

    private String remoteIp;

    private String remoteUsername;

    private String password;

    private Integer port;

    private String remoteFilePath;

    private String remoteFilename;


    public DeliverymanImpl(String localFilename, String remoteIp, String remoteUsername, String password, Integer port, String remoteFilePath) {
        this.localFilename = localFilename;
        this.remoteIp = remoteIp;
        this.remoteUsername = remoteUsername;
        this.password = password;
        this.port = port;
        this.remoteFilePath = remoteFilePath;
    }

    /**
     * 获取远端的ip地址
     *
     * @return ip地址字符串
     */
    @Override
    public String getDistanceIp() {
        return remoteIp;
    }

    /**
     * 获取本地待上传的文件的文件名
     */
    @Override
    public String getLocalFilename() {
        return localFilename;
    }

    /**
     * 获取本地待上传的文件的文件路径
     */
    @Override
    public String getLocalFilePath() {
        return localFilePath;
    }

    /**
     * 获取上传到远端的文件的文件名
     *
     * @return
     */
    @Override
    public String getRemoteFilename() {
        return remoteFilename;
    }

    /**
     * 获取上传到远端的文件的文件路径
     *
     * @return
     */
    @Override
    public String getRemoteFilePath() {
        return remoteFilePath;
    }

    /**
     * 上传
     */
    @Override
    public void doDeliver() throws UplodaException {
        try {

            /*上传jar包*/
            System.out.println("uploading .......");
            uploadJar();
            System.out.println("uploading finish");
            /*检查有没有相同的jar包在跑，有则kill*/
            System.out.println("check");
            String checkCmd = "kill -9 `ps -ef | grep api-doc.jar | grep -v grep | awk '{print $2}'`";
            execCmd(checkCmd);
            System.out.println("mv");
            String mvCmd = "mv " + remoteFilePath + "api-doc-t.jar " + remoteFilePath + "api-doc.jar";
            execCmd(mvCmd);
            System.out.println("run");
            String runCmd = "java -jar " + remoteFilePath + "api-doc.jar > " + remoteFilePath + "logfile.log 2>&1";
            execCmd(runCmd);
            System.out.println("finish");
        } catch (JSchException | FileNotFoundException | SftpException e) {
            e.printStackTrace();
            throw new UplodaException("上传有误，检查网络链接以及服务器密码资料");
        }

    }

    /**
     * 上传jar包
     *
     * @throws JSchException
     * @throws SftpException
     * @throws FileNotFoundException
     */
    private void uploadJar() throws JSchException, SftpException, FileNotFoundException {

        Session session = getSession();
        /*获取信道*/
        ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
        /*获取文件输入流*/
        try (InputStream in = getLocalFileInputStream(localFilename)) {
            System.out.println("connect");
            sftp.connect();
            System.out.println("put");
            sftp.put(in, remoteFilePath + "api-doc-t.jar");
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileNotFoundException();
        }
        sftp.disconnect();
        session.disconnect();
    }

    /**
     * 获取文件的输入流
     *
     * @param filenameWithPath 文件名带路径
     * @return
     */
    private InputStream getLocalFileInputStream(String filenameWithPath) throws FileNotFoundException {
        File file = new File(filenameWithPath);
        return new FileInputStream(file);
    }

    private void execCmd(String cmd) throws JSchException {
        Session session = getSession();
        /*获取ssh信道*/
        ChannelExec exec = (ChannelExec) session.openChannel("exec");
        exec.setCommand(cmd);
        exec.connect();
        int exitStatus = exec.getExitStatus();
        exec.disconnect();
        session.disconnect();
    }

    /**
     * 获取会话
     *
     * @return 会话
     */
    private Session getSession() {
        JSch jSch = new JSch();
        Session session = null;
        try {
            session = jSch.getSession(remoteUsername, remoteIp, port);
            if (session == null) {
                throw new UplodaException("无法打开链接");
            }
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(3000);
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return session;
    }


}
