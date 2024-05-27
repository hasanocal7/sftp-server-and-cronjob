package com.example.task.service;

import com.example.task.domain.Info;
import com.example.task.exception.InfoNotFoundException;
import com.example.task.repository.InfoRepository;
import com.example.task.sftp.SftpService;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class InfoService {

    @Value("${sftp.path}")
    private String sftpPath;
    @Value("${sftp.file}")
    private String sftpFile;

    @Autowired
    private Info info;

    @Autowired
    private InfoRepository infoRepository;

    @Autowired
    private SftpService sftpService;

    public void save(String jsonServers, String fileName){
        info.setServerInfos(jsonServers);
        info.setJsonFileName(fileName);
        infoRepository.save(info);
        log.info("Sistem Bilgisi Kaydedildi");
    }

    public Optional<Info> find(String fileName){
        Optional<Info> info = Optional.ofNullable(infoRepository.findByJsonFileName(fileName));
        if (info.isEmpty()){
            throw new InfoNotFoundException("Sistem Bilgisi Bulunamadı");
        }
        log.info("Mevcut Sistem Dosyası Başarıyla Tespit Edildi");
        return info;
    }

    @Scheduled(cron = "0 0 0/6 * * ?")
    public void checkData() {
        Session session = sftpService.connectSession();
        ChannelSftp sftpChannel = sftpService.connectSftpChannel(session);
        Stream<String> lines = sftpService.readSftpPathFile(sftpChannel, sftpPath);
        Info existingInfo = infoRepository.findByJsonFileName(sftpFile);
        existingInfo.setServerInfos(lines.collect(Collectors.joining()));
        infoRepository.save(existingInfo);
        log.info("Mevcut sistem bilgi dosyasına uygun veriler aktarıldı");
        sftpService.disconnectSession(session);
        sftpService.disconnectSftpChannel(sftpChannel);
    }
}
