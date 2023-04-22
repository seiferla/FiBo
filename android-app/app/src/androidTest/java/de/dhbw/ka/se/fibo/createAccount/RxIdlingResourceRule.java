package de.dhbw.ka.se.fibo.createAccount;

import androidx.annotation.NonNull;

import com.squareup.rx2.idler.Rx2Idler;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;

public class RxIdlingResourceRule implements TestRule {
    @NonNull
    @Override
    public Statement apply(@NonNull Statement base, @NonNull Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxJavaPlugins.setInitIoSchedulerHandler(
                        Rx2Idler.create("RxJava 2.x Io Scheduler"));
                RxJavaPlugins.setInitNewThreadSchedulerHandler(
                        Rx2Idler.create("RxJava 2.x New Thread Scheduler"));
                RxJavaPlugins.setInitSingleSchedulerHandler(
                        Rx2Idler.create("RxJava 2.x Single Scheduler"));
                RxJavaPlugins.setInitComputationSchedulerHandler(
                        Rx2Idler.create("RxJava 2.x Computation Scheduler"));

                try {
                    base.evaluate();
                } finally {
                    RxJavaPlugins.reset();
                    RxAndroidPlugins.reset();
                }
            }
        };
    }
}
