package io.benjamintan.ankogit.data.api.interceptors;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();

        System.out.println("> " + curlRequest(request));
        System.out.println(
                String.format(">>> Sending request %s on %s%n%s",
                        request.url(),
                        chain.connection(),
                        request.headers()));

        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        System.out.println(
                String.format("<<< Received response for %s in %.1fms%nHTTP %d%n%s",
                        response.request().url(),
                        (t2 - t1) / 1e6d,
                        response.code(),
                        response.headers()));

        return response;
    }

    private static String curlRequest(Request request) {
        StringBuilder builder = new StringBuilder();
        builder.append("curl -i -X '").append(request.method()).append("'");

        final RequestBody body = request.body();
        if (body != null) {
            try {
                Buffer buffer = new Buffer();
                body.writeTo(buffer);
                String data = buffer.readUtf8().replaceAll("\"", "\\\"");
                builder.append(" -d '").append(data).append("'");
            } catch (IOException e) {
                System.out.println("!!! Unable include request body in curl command");
            }

            builder.append(" -H 'Content-Type: ")
                    .append(body.contentType().toString())
                    .append("'");
        }

        for (String name : request.headers().names()) {
            for (String value : request.headers(name)) {
                builder.append(" -H '").append(name).append(": ").append(value).append("'");
            }
        }

        builder.append(" \"").append(request.url().toString()).append("\"");
        return builder.toString();
    }
}

