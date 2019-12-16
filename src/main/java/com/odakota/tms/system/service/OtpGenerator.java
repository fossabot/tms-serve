package com.odakota.tms.system.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.odakota.tms.constant.Constant;
import org.apache.commons.lang3.RandomStringUtils;
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

    private static final Integer EXPIRE_MIN = 15;
    private LoadingCache<String, Map<String, Object>> otpCache;

    /**
     * Constructor configuration.
     */
    public OtpGenerator() {
        super();
        otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES).build(
                new CacheLoader<String, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> load(String s) {
                        return new HashMap<>();
                    }
                });
    }

    /**
     * Method for generating OTP and put it in cache.
     *
     * @param key - cache key
     * @return cache value (generated OTP number)
     */
    public String generateOTP(String key, int len, Object data) {
        String otp = RandomStringUtils.randomAlphanumeric(len);
        Map<String, Object> map = new HashMap<>();
        map.put(Constant.OTP_CODE_OTP, otp);
        map.put(Constant.OTP_DATA, data);
        otpCache.put(key, map);
        return otp;
    }

    /**
     * Method for generating OTP and put it in cache.
     *
     * @param key - cache key
     * @return cache value (generated OTP number)
     */
    public String generateOTP(String key) {
        String otp = RandomStringUtils.randomNumeric(1000, 9999);
        Map<String, Object> map = new HashMap<>();
        map.put(Constant.OTP_CODE_OTP, otp);
        otpCache.put(key, map);
        return otp;
    }

    /**
     * Method for getting OTP value by key.
     *
     * @param key - target key
     * @return OTP value
     */
    public Map<String, Object> getOPTByKey(String key) {
        try {
            return otpCache.get(key);
        } catch (ExecutionException e) {
            return null;
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
