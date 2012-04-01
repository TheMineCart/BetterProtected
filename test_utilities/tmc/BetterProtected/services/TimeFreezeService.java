package tmc.BetterProtected.services;

import org.joda.time.DateTimeUtils;

public class TimeFreezeService {
    public static void freeze() {
        DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.getInstantMillis(null));
    }
    public static void unfreeze() {
        DateTimeUtils.setCurrentMillisSystem();
    }
}
