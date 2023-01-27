package com.example.reviews.ApiError;

import org.springframework.http.HttpStatus;

public class ApiError {

    private HttpStatus mStatus;
    private String mMessage;
    private String mSuggestedAction;

    public static String DEFAULT_MESSAGE = "Something went wrong";
    public static String DEFAULT_SUGGESTED_ACTION = "Try again later!";

    public ApiError(HttpStatus status, String message, String suggestedAction) {
        mStatus = status;
        mMessage = message;
        mSuggestedAction = suggestedAction;
    }

    public HttpStatus getStatus() {
        return mStatus;
    }

    public void setStatus(HttpStatus status) {
        mStatus = status;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getSuggestedAction() {
        return mSuggestedAction;
    }

    public void setSuggestedAction(String suggestedAction) {
        mSuggestedAction = suggestedAction;
    }
}