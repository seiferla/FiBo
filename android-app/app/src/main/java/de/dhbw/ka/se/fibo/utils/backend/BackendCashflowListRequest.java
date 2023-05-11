package de.dhbw.ka.se.fibo.utils.backend;

import static de.dhbw.ka.se.fibo.utils.ApiUtils.createAPIJSONRequest;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonRequest;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class BackendCashflowListRequest extends AbstractBackendListRequest<CashflowListResponse> {
    public BackendCashflowListRequest(CountDownLatch latch, String accessToken) {
        super(latch, accessToken);
    }

    @Override
    protected JsonRequest<CashflowListResponse[]> populateRequest(String accessToken) {
        return createAPIJSONRequest(CashflowListResponse[].class, "/cashflows/", Request.Method.GET, null, this::onSuccess, this::onFailure, Optional.of(accessToken));
    }
}
