package com.puddlealley.flux.store.device

import com.puddlealley.flux.service.CodeVerificationResult
import com.puddlealley.splash.core.Action
import com.puddlealley.splash.core.createReducer

val secretCaveReducer = createReducer<SecretCaveState> { action ->
    copy(
        loading = reducerLoading(loading, action),
        codeCorrect = reduceCodeCorrect(codeCorrect, action),
        enteredCode = reduceEnteredCode(enteredCode, action)
    )
}

fun reduceEnteredCode(enteredCode: String, action: Action): String =
    when (action) {
        is SecretCaveEvents.LetteredEntered -> enteredCode + action.letter
        is SecretCaveEvents.CodeEntered -> action.code
        is SecretCaveEvents.ClearLetters -> ""
        else -> enteredCode
    }

fun reducerLoading(loading: Boolean, action: Action): Boolean =
    when (action) {
        CodeVerificationResult.Loading -> true
        is CodeVerificationResult.Success -> false
        is CodeVerificationResult.Error -> false
        else -> loading
    }

fun reduceCodeCorrect(codeCorrect: Boolean, action: Action): Boolean =
    when (action) {
        CodeVerificationResult.Loading -> false
        is CodeVerificationResult.Success -> action.correct
        is CodeVerificationResult.Error -> false
        else -> codeCorrect
    }