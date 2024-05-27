package com.example.task;

import com.example.task.domain.Info;
import com.example.task.service.InfoService;
import com.example.task.sftp.SftpService;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EnableScheduling
@Slf4j
@SpringBootApplication
public class TaskApplication extends SpringBootServletInitializer {

	@Value("${sftp.path}")
	private String sftpPath;
	@Value("${sftp.file}")
	private String sftpFile;

	@Autowired
	private InfoService infoService;

	@Autowired
	private SftpService sftpService;

    public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {
			Session session = sftpService.connectSession();
			ChannelSftp sftpChannel = sftpService.connectSftpChannel(session);
			Stream<String> lines = sftpService.readSftpPathFile(sftpChannel, sftpPath);
			Optional<Info> optionalInfo = infoService.find(sftpFile);
			if (optionalInfo.isEmpty()) {
				infoService.save(lines.collect(Collectors.joining()), sftpFile);
				log.info("Sistem Dosyası Başarıyla Kaydedildi");
			}
			sftpService.disconnectSession(session);
			sftpService.disconnectSftpChannel(sftpChannel);
		};
	}



}
