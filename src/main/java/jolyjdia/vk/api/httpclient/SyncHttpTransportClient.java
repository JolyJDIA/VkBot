package jolyjdia.vk.api.httpclient;

import jolyjdia.vk.api.client.ClientResponse;
import jolyjdia.vk.api.client.TransportClient;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SyncHttpTransportClient implements TransportClient {
    private static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String EMPTY_PAYLOAD = "-";
    private static final int FULL_CONNECTION_TIMEOUT_S = 60;
    private final HttpClient httpClient;

    private final int retryInvalidCount;

    public SyncHttpTransportClient() {
        this(3);
    }

    public SyncHttpTransportClient(int retryInvalidCount) {
        this.retryInvalidCount = retryInvalidCount;

        this.httpClient = HttpClients.custom()
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .build();
    }


    @NotNull
    private static Map<String, String> getHeaders(Header[] headers) {
        Map<String, String> result = new HashMap<>();
        for (Header header : headers) {
            result.put(header.getName(), header.getValue());
        }

        return result;
    }

    private ClientResponse callWithStatusCheck(HttpRequestBase request) throws IOException {
        ClientResponse response;
        int attempts = 0;

        do {
            response = this.call(request);
            ++attempts;
        } while (attempts < retryInvalidCount && isInvalidGatewayStatus(response.getStatusCode()));

        return response;
    }

    private boolean isInvalidGatewayStatus(int status) {
        return status == HttpStatus.SC_BAD_GATEWAY || status == HttpStatus.SC_GATEWAY_TIMEOUT;
    }

    @NotNull
    private ClientResponse call(HttpRequestBase request) throws IOException {
        HttpResponse response = httpClient.execute(request);

        try (InputStream content = response.getEntity().getContent()) {
            String result = IOUtils.toString(content, StandardCharsets.UTF_8);
     //       Map<String, String> responseHeaders = getHeaders(response.getAllHeaders());
            return new ClientResponse(response.getStatusLine().getStatusCode(), result, null);
        } finally {
            request.abort();
        }
    }


    private String getRequestPayload(HttpRequestBase request) throws IOException {
        if (!(request instanceof HttpPost)) {
            return EMPTY_PAYLOAD;
        }

        HttpPost postRequest = (HttpPost) request;
        if (postRequest.getEntity() == null) {
            return EMPTY_PAYLOAD;
        }

        if (StringUtils.isNotEmpty(postRequest.getEntity().getContentType().getValue())) {
            String contentType = postRequest.getEntity().getContentType().getValue();
            if (contentType.contains("multipart/form-data")) {
                return EMPTY_PAYLOAD;
            }
        }

        return IOUtils.toString(postRequest.getEntity().getContent(), StandardCharsets.UTF_8);
    }


    @Override
    public ClientResponse get(String url) throws IOException {
        return get(url, FORM_CONTENT_TYPE);
    }

    @Override
    public ClientResponse get(String url, String contentType) throws IOException {
        HttpGet request = new HttpGet(url);
        request.setHeader(CONTENT_TYPE_HEADER, contentType);
        return callWithStatusCheck(request);
    }

    @Override
    public ClientResponse post(String url) throws IOException {
        return post(url, null);
    }

    @Override
    public ClientResponse post(String url, String body) throws IOException {
        return post(url, body, FORM_CONTENT_TYPE);
    }

    @Override
    public ClientResponse post(String url, String body, String contentType) throws IOException {
        HttpPost request = new HttpPost(url);
        request.setHeader(CONTENT_TYPE_HEADER, contentType);
        if (body != null) {
            request.setEntity(new StringEntity(body, "UTF-8"));
        }

        return callWithStatusCheck(request);
    }

    @Override
    public ClientResponse post(String url, String fileName, File file) throws IOException {
        HttpPost request = new HttpPost(url);
        FileBody fileBody = new FileBody(file);
        HttpEntity entity = MultipartEntityBuilder
                .create()
                .addPart(fileName, fileBody).build();

        request.setEntity(entity);
        return callWithStatusCheck(request);
    }

    @Override
    public ClientResponse delete(String url) throws IOException {
        return delete(url, null, FORM_CONTENT_TYPE);
    }

    @Override
    public ClientResponse delete(String url, String body) throws IOException {
        return delete(url, body, FORM_CONTENT_TYPE);
    }

    @Override
    public ClientResponse delete(String url, String body, String contentType) throws IOException {
        HttpDeleteWithBody request = new HttpDeleteWithBody(url);
        request.setHeader(CONTENT_TYPE_HEADER, contentType);
        if (body != null) {
            request.setEntity(new StringEntity(body));
        }

        return callWithStatusCheck(request);
    }
}