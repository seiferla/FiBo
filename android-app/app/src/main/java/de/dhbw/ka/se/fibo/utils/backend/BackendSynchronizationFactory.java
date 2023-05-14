package de.dhbw.ka.se.fibo.utils.backend;

import static de.dhbw.ka.se.fibo.utils.ActivityUtils.shouldContactBackend;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import de.dhbw.ka.se.fibo.ApplicationState;
import de.dhbw.ka.se.fibo.BuildConfig;
import de.dhbw.ka.se.fibo.SharedVolleyRequestQueue;
import de.dhbw.ka.se.fibo.models.Cashflow;
import de.dhbw.ka.se.fibo.models.Category;
import de.dhbw.ka.se.fibo.models.Place;
import de.dhbw.ka.se.fibo.utils.ActivityUtils;

/**
 * Factory that pulls all data from the backend and hands them over to interested consumers.
 */
public class BackendSynchronizationFactory {
    private final Context context;
    private List<Consumer<BackendSynchronizationResult>> resultListeners = new ArrayList<>();

    private boolean wasSyncStarted = false;

    private static final String TAG = "BackendSynchronizationFactory";

    public BackendSynchronizationFactory(Context context) {
        this.context = context;
    }

    public BackendSynchronizationFactory addResultListener(Consumer<BackendSynchronizationResult> listener) {
        if (wasSyncStarted) {
            throw new IllegalStateException("cannot add listeners if already syncing!");
        }

        resultListeners.add(listener);

        return this;
    }

    public void startSynchronization() throws IllegalStateException {
        if (!shouldContactBackend()) {
            throw new IllegalStateException("backend synchronization is disabled");
        }

        synchronized (this) {
            if (wasSyncStarted) {
                throw new IllegalStateException("cannot start synchronization if already running!");
            }
            wasSyncStarted = true;
        }

        Optional<String> accessToken = ApplicationState.getInstance(context).getAccessToken();
        if (!accessToken.isPresent()) {
            throw new IllegalStateException("need to have an access token to sync!");
        }

        Runnable r = () -> {
            SharedVolleyRequestQueue requestQueue = SharedVolleyRequestQueue.getInstance(context);
            CountDownLatch latch = new CountDownLatch(3);

            // build requests
            BackendCashflowListRequest cashflowsRequest = new BackendCashflowListRequest(latch, accessToken.get());
            BackendCategoryListRequest categoriesRequest = new BackendCategoryListRequest(latch, accessToken.get());
            BackendPlacesListRequest placesRequest = new BackendPlacesListRequest(latch, accessToken.get());

            // enqueue requests
            requestQueue.addToRequestQueue(cashflowsRequest.getRequest());
            requestQueue.addToRequestQueue(categoriesRequest.getRequest());
            requestQueue.addToRequestQueue(placesRequest.getRequest());

            // wait for all requests to finish
            try {
                latch.await();
            } catch (InterruptedException e) {
                failWithThrowable(e);
                return;
            }

            // handle responses
            BackendSynchronizationResult result = mergeResponses(cashflowsRequest.getResponse(), categoriesRequest.getResponse(), placesRequest.getResponse());

            propagateResult(result);
        };

        // waiting for the latch is blocking, hence we run this on another thread
        new Thread(r).start();
    }

    private BackendSynchronizationResult mergeResponses(CashflowListResponse[] cashflows, CategoryListResponse[] categories, PlacesListResponse[] places) {
        Map<Integer, Category> categoryLookupTable = new HashMap<>();
        Map<Integer, Place> placesLookupTable = new HashMap<>();
        SortedSet<Cashflow> newCashflowSet = new TreeSet<>();

        for (CategoryListResponse serializedCategory : categories) {
            categoryLookupTable.put(serializedCategory.id, serializedCategory.toCategory());
        }

        for (PlacesListResponse serializedPlace : places) {
            placesLookupTable.put(serializedPlace.id, serializedPlace.toPlace());
        }

        for (CashflowListResponse serializedCashflow : cashflows) {
            Category category = categoryLookupTable.get(serializedCashflow.categoryID);
            Place place = placesLookupTable.get(serializedCashflow.placeID);

            Cashflow cashflow = new Cashflow(category, serializedCashflow.getCashflowType(), serializedCashflow.overallValue, serializedCashflow.created, place);

            newCashflowSet.add(cashflow);
        }

        Log.i(TAG, "places = " + Arrays.toString(places));

        return buildResult(newCashflowSet, categoryLookupTable.values(), placesLookupTable.values());
    }

    private BackendSynchronizationResult buildResult(SortedSet<Cashflow> newCashflowSet, Collection<? extends Category> values, Collection<? extends Place> places) {
        return new BackendSynchronizationResult(
                newCashflowSet,
                new TreeSet<>(values),
                new TreeSet<>(places),
                null
        );
    }

    private void propagateResult(BackendSynchronizationResult result) {
        resultListeners.forEach(consumer -> consumer.accept(result));
    }

    /**
     * Informs all listeners that the synchronization has failed
     */
    private void failWithThrowable(Throwable e) {
        BackendSynchronizationResult result = new BackendSynchronizationResult(null, null, null, e);

        resultListeners.forEach(consumer -> consumer.accept(result));
    }
}
