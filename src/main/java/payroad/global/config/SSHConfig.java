package payroad.global.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import jakarta.annotation.PreDestroy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Component
@ConfigurationProperties(prefix = "ssh")
@Validated
@Setter
@Getter
public class SSHConfig {

    @Value("${cloud.aws.ec2.remote_jump_host}")
    private String remoteJumpHost;
    @Value("${cloud.aws.ec2.ssh_port}")
    private int sshPort;
    @Value("${cloud.aws.ec2.user}")
    private String user;
    @Value("${cloud.aws.ec2.private_key_path}")
    private String privateKeyPath;

    private Session session;

    @PreDestroy
    public void destroy() {
        if (session.isConnected()) {
            session.disconnect();
        }
    }

    // private key 경로 확인 메서드
    private void validatePrivateKeyPath() {
        Path keyPath = Paths.get(privateKeyPath);
        if (!Files.exists(keyPath)) {
            log.error("Private key file not found at: {}", privateKeyPath);
            throw new RuntimeException("Private key file does not exist at: " + privateKeyPath);
        }
        if (!Files.isReadable(keyPath)) {
            log.error("Private key file is not readable at: {}", privateKeyPath);
            throw new RuntimeException("Private key file is not readable at: " + privateKeyPath);
        }
        log.info("Private key file found and is readable at: {}", privateKeyPath);
    }

    public Integer buildSshConnection(String endpoint, int port) {
        Integer forwardPort = null;

        try {
            log.info("SSH  {}@{}:{}  with {}", user, remoteJumpHost, sshPort, privateKeyPath);
            JSch jsch = new JSch();

            jsch.addIdentity(privateKeyPath);
            session = jsch.getSession(user, remoteJumpHost, sshPort);
            session.setConfig("StrictHostKeyChecking", "no");

            log.info("Starting SSH session connection...");
            session.connect();
            log.info("SSH session connected");

            forwardPort = session.setPortForwardingL(0, endpoint, port);
            log.info("ssh tunnel to  {}:{}",endpoint,port);
            log.info("Port forwarding created on local port {} to remote port {}", forwardPort,
                port);
        } catch (JSchException e) {
            log.error(e.getMessage());
            this.destroy();
            throw new RuntimeException(e);
        }
        return forwardPort;
    }


}