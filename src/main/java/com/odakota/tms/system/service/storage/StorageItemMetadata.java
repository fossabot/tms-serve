package com.odakota.tms.system.service.storage;

import com.odakota.tms.constant.FieldConstant;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Class that retains metadata to be set in storage item
 *
 * @author haidv
 * @version 1.0
 */
@AllArgsConstructor
public class StorageItemMetadata {

    private Map<String, Object> metadata;

    /**
     * Generate a new content metadata builder.
     *
     * @return {@link StorageItemMetadata.Builder} instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Get the Last-Modified header value.
     *
     * @return Last-Modified
     */
    public Optional<Date> getLastModified() {
        return Optional.ofNullable((Date) metadata.get(FieldConstant.LAST_MODIFIED));
    }

    /**
     * Get the Content-Length header value.
     *
     * @return Content-Length
     */
    public Optional<Long> getContentLength() {
        return Optional.ofNullable((Long) metadata.get(FieldConstant.CONTENT_LENGTH));
    }

    /**
     * Get the Content-Type header value.
     *
     * @return Content-Type
     */
    public Optional<String> getContentType() {
        return Optional.ofNullable((String) metadata.get(FieldConstant.CONTENT_TYPE));
    }

    /**
     * Get the Content-Language header value.
     *
     * @return Content-Language
     */
    public Optional<String> getContentLanguage() {
        return Optional.ofNullable((String) metadata.get(FieldConstant.CONTENT_LANGUAGE));
    }

    /**
     * Get the Content-Encoding header value.
     *
     * @return Content-Encoding
     */
    public Optional<String> getContentEncoding() {
        return Optional.ofNullable((String) metadata.get(FieldConstant.CONTENT_ENCODING));
    }

    /**
     * Get the Content-MD5 header value.
     *
     * @return Content-MD5
     */
    public Optional<String> getContentMD5() {
        return Optional.ofNullable((String) metadata.get(FieldConstant.CONTENT_DISPOSITION));
    }

    /**
     * Get Content-Disposition header value.
     *
     * @return Content-Disposition
     */
    public Optional<String> getContentDisposition() {
        return Optional.ofNullable((String) metadata.get(FieldConstant.CONTENT_MD5));
    }

    /**
     * Get the Cache-Control header value.
     *
     * @return Cache-Control
     */
    public Optional<String> getCacheControl() {
        return Optional.ofNullable((String) metadata.get(FieldConstant.CACHE_CONTROL));
    }

    /**
     * Builder class for {@link StorageItemMetadata}.
     *
     * @author haidv
     * @version 1.0
     */
    public static class Builder {

        private final Map<String, Object> metadata = new TreeMap<>();

        public Builder lastModified(Date lastModified) {
            metadata.put(FieldConstant.LAST_MODIFIED, lastModified);
            return this;
        }

        public Builder contentLength(long contentLength) {
            metadata.put(FieldConstant.CONTENT_LENGTH, contentLength);
            return this;
        }

        public Builder contentType(String contentType) {
            metadata.put(FieldConstant.CONTENT_TYPE, contentType);
            return this;
        }

        public Builder contentLanguage(String contentLanguage) {
            metadata.put(FieldConstant.CONTENT_LANGUAGE, contentLanguage);
            return this;
        }

        public Builder contentEncoding(String contentEncoding) {
            metadata.put(FieldConstant.CONTENT_ENCODING, contentEncoding);
            return this;
        }

        public Builder contentMD5(String contentMD5) {
            metadata.put(FieldConstant.CONTENT_MD5, contentMD5);
            return this;
        }

        public Builder contentDisposition(String contentDisposition) {
            metadata.put(FieldConstant.CONTENT_DISPOSITION, contentDisposition);
            return this;
        }

        public Builder cacheControl(String cacheControl) {
            metadata.put(FieldConstant.CACHE_CONTROL, cacheControl);
            return this;
        }

        public StorageItemMetadata build() {
            return new StorageItemMetadata(metadata);
        }
    }
}
