package net.kencochrane.sentry;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: ken cochrane
 * Date: 2/8/12
 * Time: 1:16 PM
 */
public class RavenConfig {

    private String host, protocol, publicKey, secretKey, path, projectId;
    private int port;

    /**
     * Takes in a sentryDSN and builds up the configuration
     *
     * @param sentryDSN '{PROTOCOL}://{PUBLIC_KEY}:{SECRET_KEY}@{HOST}/{PATH}{PROJECT_ID}'
     */
    public RavenConfig(String sentryDSN) {

        try {
            URL url = new URL(sentryDSN);
            this.host = url.getHost();
            this.protocol = url.getProtocol();
            String urlPath = url.getPath();
            String[] urlParts = urlPath.split("/");
            this.path = urlPath;
            this.projectId = urlParts[1];

            String userInfo = url.getUserInfo();
            String[] userParts = userInfo.split(":");

            this.secretKey = userParts[1];
            this.publicKey = userParts[0];

            this.port = url.getPort();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    /**
     * The Sentry server URL that we post the message to.
     * @return sentry server url
     */
    public String getSentryURL() {
        StringBuilder serverUrl = new StringBuilder();
        serverUrl.append(getProtocol());
        serverUrl.append("://");
        serverUrl.append(getHost());
        if ((getPort() != 0) && (getPort() != 80)) {
            serverUrl.append(":").append(getPort());
        }
        serverUrl.append("/api/store/");
        return serverUrl.toString();
    }

    /**
     * The sentry server host
     * @return server host
     */
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Sentry server protocol http https?
     * @return http or https
     */
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * The Sentry public key
     * @return Sentry public key
     */
    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * The Sentry secret key
     * @return Sentry secret key
     */
    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * sentry url path
     * @return url path
     */
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Sentry project Id
     * @return project Id
     */
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * sentry server port
     * @return server port
     */
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "RavenConfig{" +
                "host='" + host + '\'' +
                ", protocol='" + protocol + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", path='" + path + '\'' +
                ", projectId='" + projectId + '\'' +
                ", SentryUrl='" + getSentryURL() + '\'' +
                '}';
    }

}
