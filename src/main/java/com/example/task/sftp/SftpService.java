package com.example.task.sftp;

import com.example.task.exception.CustomJSchException;
import com.example.task.exception.CustomSftpException;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

@Service
@Slf4j
public class SftpService {

    @Value("${sftp.host}")
    private String sftpHost;
    @Value("${sftp.port}")
    private String sftpPort;
    @Value("${sftp.username}")
    private String sftpUser;
    @Value("${sftp.password}")
    private String sftpPass;

    @Autowired
    private JSch jSch;

    public Session connectSession() throws CustomJSchException {
        try{
            Session session = jSch.getSession(sftpUser, sftpHost, Integer.parseInt(sftpPort));
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(sftpPass);
            log.info("Oturum Açılıyor...");
            session.connect();
            log.info("Oturum Başarıyla Açıldı");
            return session;
        } catch (JSchException e){
            throw new CustomJSchException("Oturum Bağlantısında Bir Hata Oluştu: " + e.getLocalizedMessage());
        }
    }

    public ChannelSftp connectSftpChannel(Session session) throws CustomJSchException {
        try{
            Channel channel = session.openChannel("sftp");
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            log.info("SFTP Kanalı Oluşturuldu");
            log.info("SFTP Kanalına Bağlanılıyor...");
            sftpChannel.connect();
            log.info("SFTP Kanalında Bağlantı Kuruldu");
            return sftpChannel;
        } catch (JSchException e){
            throw new CustomJSchException("SFTP Kanal Bağlantısında Bir Sorun Oluştu: " + e.getLocalizedMessage());
        }
    }

    public Stream<String> readSftpPathFile(ChannelSftp sftpChannel, String sftpPath) throws CustomSftpException {
        try {
            InputStream inputStream = sftpChannel.get(sftpPath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            log.info("Hedef dosya başarıyla okundu");
            return bufferedReader.lines();
        } catch (SftpException e) {
            throw new CustomSftpException(e.id, "Hedef dosya okunmasında bir sorun oluştu: " + e.getLocalizedMessage());
        }
    }

    public void disconnectSession(Session session){
        log.info("Oturum Kapatılıyor...");
        session.disconnect();
        log.info("Oturum Kapatıldı");
    }

    public void disconnectSftpChannel(ChannelSftp sftpChannel){
        log.info("SFTP Kanalı Kapatılıyor...");
        sftpChannel.disconnect();
        if (sftpChannel.isClosed()) {
            log.info("SFTP Kanalı Kapatıldı");
        }
    }
}
