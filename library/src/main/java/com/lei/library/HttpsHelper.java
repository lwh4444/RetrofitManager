package com.lei.library;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class HttpsHelper {
    public HttpsHelper() {
    }

    public static X509TrustManager getTrustManager(Context context) {
        try {
            return getTrustManager(trustedCertificatesInputStream(context));
        } catch (IOException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static X509TrustManager getTrustManager(InputStream trustedCertificatesInputStream) {
        try {
            return trustManagerForCertificates(trustedCertificatesInputStream);
        } catch (GeneralSecurityException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    private static InputStream trustedCertificatesInputStream(Context context) throws IOException {
        InputStream certInputStream = context.getAssets().open("cert.pem");
        return certInputStream;
    }

    private static X509TrustManager trustManagerForCertificates(InputStream in) throws GeneralSecurityException {
        char[] password = password();
        KeyStore keyStore = getKeyStore(in, password);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length == 1 && trustManagers[0] instanceof X509TrustManager) {
            return (X509TrustManager)trustManagers[0];
        } else {
            throw new IllegalStateException("Unexpected default trust manager:" + Arrays.toString(trustManagers));
        }
    }

    private static char[] password() {
        return "password".toCharArray();
    }

    private static KeyStore getKeyStore(InputStream in, char[] password) throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        Iterator var6 = certificates.iterator();

        while(var6.hasNext()) {
            Certificate certificate = (Certificate)var6.next();
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        return keyStore;
    }

    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load((InputStream)null, password);
            return keyStore;
        } catch (IOException var2) {
            throw new AssertionError(var2);
        }
    }
}
