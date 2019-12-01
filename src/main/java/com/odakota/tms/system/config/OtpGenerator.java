package com.odakota.tms.system.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.utils.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Service for generating and validating OTP.
 *
 * @author Đoàn Hải
 * @version 1.0
 * @see <a href="https://github.com/google/guava">Guava: Google Core Libraries for Java</a>
 */
@Service
public class OtpGenerator {

    private static final Integer EXPIRE_MIN = 1;
    private LoadingCache<String, Map> otpCache;

    /**
     * Constructor configuration.
     */
    public OtpGenerator() {
        super();
        otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES).build(
                new CacheLoader<String, Map>() {
                    @Override
                    public Map load(String s) {
                        return new HashMap();
                    }
                });
    }

    /**
     * Method for generating OTP and put it in cache.
     *
     * @param key - cache key
     * @return cache value (generated OTP number)
     */
    public String generateOTP(String key, Object data) {
        String otp = RandomUtils.generateAlphaNumeric(6);
        Map<String, Object> map = new HashMap<>();
        map.put(Constant.OTP_CODE_OTP, otp);
        map.put(Constant.OTP_DATA, data);
        otpCache.put(key, map);
        return otp;
    }

    /**
     * Method for getting OTP value by key.
     *
     * @param key - target key
     * @return OTP value
     */
    public Object getOPTByKey(String key) {
        try {
            return otpCache.get(key);
        } catch (ExecutionException e) {
            return -1;
        }
    }

    /**
     * Method for removing key from cache.
     *
     * @param key - target key
     */
    public void clearOTPFromCache(String key) {
        otpCache.invalidate(key);
    }
}
