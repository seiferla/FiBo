package de.dhbw.ka.se.fibo.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class ApiUtils {

    private static String getBaseURL() {
        if (ActivityUtils.isEspressoTesting()) {
            return "http://localhost:8000";
        } else {
            return "10.0.2.2:8000";
        }
    }

    /**
     * This method creates a StringRequest that can be send to the backend.
     * The URL is currently defined as a field
     *
     * @param url       the desired URL
     * @param method    the integer according to the Request.Method interface
     * @param params    The data that will be send to the backend in the body
     * @param onSuccess a custom success response
     * @param onError   a custom error response
     * @return the created StringRequest with given callbacks and parameters
     */
    public static StringRequest createAPIStringRequest(String url, int method, Map<String, String> params, Response.Listener<String> onSuccess, Response.ErrorListener onError) {

        return new StringRequest(
                method,
                ApiUtils.getBaseURL() + url,
                onSuccess,
                onError) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
    }


    /**
     * This method creates a StringRequest that can be send to the backend.
     * The URL is currently defined as a field
     *
     * @param responseType the Class that is represented by the JSON response
     * @param url          the desired URL
     * @param method       the integer according to the Request.Method interface
     * @param body         The body data that will be send to the backend
     * @param onSuccess    a custom success response
     * @param onError      a custom error response
     * @return the created StringRequest with given callbacks and parameters
     */
    public static <T> JsonRequest<T> createAPIJSONRequest(Class<T> responseType, String url, int method, Map<String, String> body, Response.Listener<T> onSuccess, Response.ErrorListener onError) {

        String jsonRequestBody = new Gson().toJson(body);

        return new JsonRequest<>(
                method,
                ApiUtils.getBaseURL() + url,
                jsonRequestBody,
                onSuccess,
                onError) {
            @Override
            protected Response<T> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    T parsedResponse = new Gson().fromJson(json, responseType);
                    return Response.success(parsedResponse, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JsonSyntaxException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
    }
}
