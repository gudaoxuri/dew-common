/*
 * Copyright 2020. the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ecfront.dew.common;

/**
 * The enum Standard code.
 *
 * @author gudaoxuri
 */
public enum StandardCode {
    /**
     * Success standard code.
     */
    SUCCESS("200"),
    /**
     * Bad request standard code.
     */
    BAD_REQUEST("400"),
    /**
     * Unauthorized standard code.
     */
    UNAUTHORIZED("401"),
    /**
     * Forbidden standard code.
     */
    FORBIDDEN("403"),
    /**
     * Not found standard code.
     */
    NOT_FOUND("404"),
    /**
     * Conflict standard code.
     */
    CONFLICT("409"),
    /**
     * Locked standard code.
     */
    LOCKED("423"),
    /**
     * Unsupported media type standard code.
     */
    UNSUPPORTED_MEDIA_TYPE("415"),
    /**
     * Internal server error standard code.
     */
    INTERNAL_SERVER_ERROR("500"),
    /**
     * Not implemented standard code.
     */
    NOT_IMPLEMENTED("501"),
    /**
     * Service unavailable standard code.
     */
    SERVICE_UNAVAILABLE("503"),
    /**
     * Unknown standard code.
     */
    UNKNOWN("-1");

    private String code;

    StandardCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
