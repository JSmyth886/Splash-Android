package com.puddlealley.flux

import com.puddlealley.flux.service.CodeVerificationResult
import com.puddlealley.flux.store.device.SecretCaveEvents
import com.puddlealley.flux.store.device.reduceCodeCorrect
import com.puddlealley.flux.store.device.reduceEnteredCode
import com.puddlealley.flux.store.device.reducerLoading
import com.puddlealley.splash.core.Event
import junit.framework.Assert.*
import org.junit.Test

class SecretCaveReducerTest {

    inner class UnknownEvent: Event

    //reduceEnteredCode tests
    @Test
    fun `When action is LetteredEntered, reduceEnteredCode should return the currently entered code plus the new letter entered`() {
        assertEquals("ABC", reduceEnteredCode("AB", SecretCaveEvents.LetteredEntered("C")))
    }

    @Test
    fun `When action is CodeEntered, reduceEnteredCode should return the code`() {
        assertEquals("ABC", reduceEnteredCode("AB", SecretCaveEvents.CodeEntered("ABC")))
    }

    @Test
    fun `When action is ClearLetters, reduceEnteredCode should return an empty string`() {
        assertEquals("", reduceEnteredCode("AB", SecretCaveEvents.ClearLetters))
    }

    @Test
    fun `When action isn't of known type, reduceEnteredCode should return the currently entered code`() {
        assertEquals("DEF", reduceEnteredCode("DEF", UnknownEvent()))
    }

    //reduceLoading tests
    @Test
    fun `When CodeVerificationResult is Loading, reduceLoading should return true`() {
        assertTrue(reducerLoading(false, CodeVerificationResult.Loading))
    }

    @Test
    fun `When CodeVerificationResult is Success, reduceLoading should return false`() {
        assertFalse(reducerLoading(true, CodeVerificationResult.Success(true)))
    }

    @Test
    fun `When CodeVerificationResult is Error, reduceLoading should return false`() {
        assertFalse(reducerLoading(true, CodeVerificationResult.Error("Loading failed")))
    }

    @Test
    fun `When action isn't a known type, reduceLoading should return stored loading value`() {
        assertTrue(reducerLoading(true, UnknownEvent()))
    }

    //reduceCodeCorrect tests
    @Test
    fun `When CodeVerificationResult is Loading, reduceCodeCorrect should return false`() {
        assertFalse(reduceCodeCorrect(true, CodeVerificationResult.Loading))
    }

    @Test
    fun `When CodeVerificationResult is Success, reduceCodeCorrect should return the value from the Server`() {
        assertTrue(reduceCodeCorrect(false, CodeVerificationResult.Success(true)))
    }

    @Test
    fun `When CodeVerificationResult is Error, reduceCodeCorrect should return false`() {
        assertFalse(reduceCodeCorrect(true, CodeVerificationResult.Error("Loading failed")))
    }

    @Test
    fun `When action isn't a known type, reduceCodeCorrect should return stored loading value`() {
        assertTrue(reduceCodeCorrect(true, UnknownEvent()))
    }
}