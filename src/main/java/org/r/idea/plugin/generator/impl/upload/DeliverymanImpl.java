package org.r.idea.plugin.generator.impl.upload;

import com.jcraft.jsch.*;
import org.r.idea.plugin.generator.core.upload.Deliveryman;

import java.io.*;

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
    public void doDeliver() {


        JSch jSch = new JSch();
        try {
            Session session = jSch.getSession(remoteUsername, remoteIp, port);
            if (session == null) {
                // TODO: 19-7-21 抛出异常
            }
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(3000);

            /*上传jar包*/
            System.out.println("uploading .......");
            uploadJar(session);
            System.out.println("uploading finish");
            /*检查有没有相同的jar包在跑，有则kill*/
            System.out.println("check");
            String checkCmd = "kill -9 `ps -ef | grep api-doc.jar | grep -v grep | awk '{print $2}'`";
            execCmd(session, checkCmd);
            System.out.println("run");
            String runCmd = "cd " + remoteFilePath + "&& mv api-doc-t.jar api-doc.jar && java -jar -Dserver.port=18180 api-doc.jar > logfile.log 2>&1";
            execCmd(session, runCmd);
            System.out.println("finish");
            session.disconnect();

        } catch (JSchException | FileNotFoundException | SftpException e) {
            e.printStackTrace();
        }

    }

    /**
     * 上传jar包
     *
     * @param session
     * @throws JSchException
     * @throws SftpException
     * @throws FileNotFoundException
     */
    private void uploadJar(Session session) throws JSchException, SftpException, FileNotFoundException {

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

    private void execCmd(Session session, String cmd) throws JSchException {
        /*获取ssh信道*/
        ChannelExec exec = (ChannelExec) session.openChannel("exec");
        exec.setCommand(cmd);
        exec.connect();
        int exitStatus = exec.getExitStatus();
        exec.disconnect();
    }

}
