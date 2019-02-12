//
// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE.md file in the project root for full license information.
//
package com.microsoft.cognitiveservices.speech.translation;

import java.io.Closeable;
import java.util.ArrayList;

import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.util.Contracts;

/**
 * Speech translation configuration.
 */
public final class SpeechTranslationConfig extends SpeechConfig implements Closeable {

    // load the native library.
    static {
        // trigger loading of native library
        try {
            Class.forName(SpeechConfig.class.getName());
        }
        catch (ClassNotFoundException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Creates an instance of recognizer config.
     */
    private SpeechTranslationConfig(com.microsoft.cognitiveservices.speech.internal.SpeechTranslationConfig configImpl) {
        super(configImpl);

        Contracts.throwIfNull(configImpl, "configImpl");

        this.translatorConfigImpl = configImpl;
    }

    /**
     * Static instance of SpeechTranslationConfig returned by passing subscriptionKey and service region.
     * @param subscriptionKey The subscription key.
     * @param region The region name (see the <a href="https://aka.ms/csspeech/region">region page</a>).
     * @return The speech config
     */
    public static SpeechTranslationConfig fromSubscription(String subscriptionKey, String region) {
        Contracts.throwIfIllegalSubscriptionKey(subscriptionKey, "subscriptionKey");
        Contracts.throwIfNullOrWhitespace(region, "region");

        return new SpeechTranslationConfig(com.microsoft.cognitiveservices.speech.internal.SpeechTranslationConfig.FromSubscription(subscriptionKey, region));
    }

    /**
     * Static instance of SpeechTranslationConfig returned by passing authorization token and service region.
     * Note: The caller needs to ensure that the authorization token is valid. Before the authorization token
     * expipres, the caller needs to refresh it by calling this setter with a new valid token.
     * Otherwise, all the recognizers created by this SpeechTranslationConfig instance will encounter errors during recognition.
     * @param authorizationToken The authorization token.
     * @param region The region name (see the <a href="https://aka.ms/csspeech/region">region page</a>).
     * @return The speech config
     */
    public static SpeechTranslationConfig fromAuthorizationToken(String authorizationToken, String region) {
        Contracts.throwIfNullOrWhitespace(authorizationToken, "authorizationToken");
        Contracts.throwIfNullOrWhitespace(region, "region");

        return new SpeechTranslationConfig(com.microsoft.cognitiveservices.speech.internal.SpeechTranslationConfig.FromAuthorizationToken(authorizationToken, region));
    }

    /**
     * Creates an instance of the speech config with specified endpoint and subscription key.
     * This method is intended only for users who use a non-standard service endpoint or paramters.
     * Note: The query parameters specified in the endpoint URL are not changed, even if they are set by any other APIs.
     * For example, if language is defined in the uri as query parameter "language=de-DE", and also set by CreateSpeechRecognizer("en-US"),
     * the language setting in uri takes precedence, and the effective language is "de-DE".
     * Only the parameters that are not specified in the endpoint URL can be set by other APIs.
     * Note: To use authorization token with fromEndpoint, pass an empty string to the subscriptionKey in the fromEndpoint method,
     * and then call setAuthorizationToken() on the created SpeechTranslationConfig instance to use the authorization token.
     * @param endpoint The service endpoint to connect to.
     * @param subscriptionKey The subscription key.
     * @return A speech config instance.
     */
    public static SpeechTranslationConfig fromEndpoint(java.net.URI endpoint, String subscriptionKey) {
        Contracts.throwIfNull(endpoint, "endpoint");
        if(subscriptionKey == null) {
            throw new NullPointerException("subscriptionKey");
        }

        return new SpeechTranslationConfig(com.microsoft.cognitiveservices.speech.internal.SpeechTranslationConfig.FromEndpoint(endpoint.toString(), subscriptionKey));
    }

    /**
     * Sets the authorization token.
     * Note: The caller needs to ensure that the authorization token is valid. Before the authorization token
     * expires, the caller needs to refresh it by calling this setter with a new valid token.
     * As configuration values are copied when creating a new recognizer, the new token value will not apply to recognizers that have already been created.
     * For recognizers that have been created before, you need to set authorization token of the corresponding recognizer
     * to refresh the token. Otherwise, the recognizers will encounter errors during recognition.
     * @param value the authorization token.
     */
    @Override
    public void setAuthorizationToken(String value) {
        Contracts.throwIfNullOrWhitespace(value, "value");
        translatorConfigImpl.SetAuthorizationToken(value);
    }

    /**
     * Sets the speech recognition language
     * @param value the authorization token.
     */
    @Override
    public void setSpeechRecognitionLanguage(String value) {
        Contracts.throwIfNullOrWhitespace(value, "value");

        translatorConfigImpl.SetSpeechRecognitionLanguage(value);
    }

    /**
     * Adds a (text) target language for translation.
     * @param value the language identifier in BCP-47 format.
     */
    public void addTargetLanguage(String value) {
        Contracts.throwIfNullOrWhitespace(value, "value");

        translatorConfigImpl.AddTargetLanguage(value);
    }

    /**
     * Gets all target languages that have been added for translation.
     * @return the list of target languages.
     */
    public ArrayList<String> getTargetLanguages() {

        ArrayList<String> result = new ArrayList<String>();
        com.microsoft.cognitiveservices.speech.internal.StringVector v = translatorConfigImpl.GetTargetLanguages();
        for (int i = 0; i < v.size(); ++i)
        {
            result.add(v.get(i));
        }

        return result;
    }

    /**
     * Returns the selected voice name.
     * @return The voice name.
     */
    public String getVoiceName() {
        return  translatorConfigImpl.GetVoiceName();
    }

    /**
     * Sets voice of the translated language, enable voice synthesis output.
     * @param value the voice name of synthesis output
     */
    public void setVoiceName(String value) {
        Contracts.throwIfNullOrWhitespace(value, "value");

        translatorConfigImpl.SetVoiceName(value);
    }

    /**
     * Sets a named property as value
     * @param name the name of the property
     * @param value the value
     */
    @Override
    public void setProperty(String name, String value) {
        Contracts.throwIfNullOrWhitespace(value, "value");

        translatorConfigImpl.SetProperty(name, value);
    }

    /**
     * Dispose of associated resources.
     */
    @Override
    public void close() {
        if (disposed) {
            return;
        }

        translatorConfigImpl.delete();
        disposed = true;
    }

    /*! \cond INTERNAL */

    /**
     * Returns the speech translation config implementation
     * @return The implementation of the speech translation config.
     */
    @Override
    public com.microsoft.cognitiveservices.speech.internal.SpeechTranslationConfig getImpl() {
        return translatorConfigImpl;
    }

    /*! \endcond */

    private com.microsoft.cognitiveservices.speech.internal.SpeechTranslationConfig translatorConfigImpl;
    private boolean disposed = false;
}
