package com.example.demo;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Base64;

@Configuration
public class MutualSslConfig {

    public SSLContext sslContext;
    //@Value("${security.enabled:false}")
    private boolean enabled;
    //@Value("${security.keystorePassword}")
    private String keystorePassword;
    //@Value("${security.trustStorePassword}")
    private String trustStorePassword;
    //@Value("${security.keystoreType}")
    private String keystoreType;
    //@Value("${security.trustStoreType}")
    private String trustStoreType;
    //@Value("${security.sslContextType}")
    private String sslContextType;
    //@Value("${security.keystoreData}")
    private String keystoreData;
    //@Value("${security.trustStoreData}")
    private String trustStoreData;

    static KeyStore decodeKeyStore(String xStoreBase64Data, String xStorePassword, String xStoreType)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {

        final KeyStore xStore = KeyStore.getInstance(xStoreType);
        final byte[] xStoreBytes = Base64.getDecoder().decode(xStoreBase64Data.replaceAll("\\n", ""));
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(xStoreBytes);

        xStore.load(inputStream, xStorePassword.toCharArray());
        return xStore;
    }

    @PostConstruct
    public void setup() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException, KeyManagementException {
        if (!enabled) {
            return;
        }

        sslContext = createSSLContext(getKeystore(), getTrustStore());
    }

    private KeyStore getKeystore() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        return decodeKeyStore(keystoreData, keystorePassword, keystoreType);
    }

    private KeyStore getTrustStore() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        return decodeKeyStore(trustStoreData, trustStorePassword, trustStoreType);
    }

    SSLContext createSSLContext(KeyStore keyStore, KeyStore trustStore)
            throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, KeyManagementException {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keystorePassword.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        SSLContext trustedSslContext = SSLContext.getInstance(sslContextType);
        trustedSslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

        return trustedSslContext;
    }


}
