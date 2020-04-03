package com.puddlealley.flux.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.jakewharton.rxbinding3.view.clicks
import com.puddlealley.flux.R
import com.puddlealley.flux.service.CodeVerificationResult
import com.puddlealley.flux.store.AppStore
import com.puddlealley.flux.store.device.SecretCaveEvents
import com.puddlealley.splash.android.connect
import com.puddlealley.splash.android.events
import io.reactivex.rxkotlin.merge
import io.reactivex.rxkotlin.ofType
import kotlinx.android.synthetic.main.activity_secret_cave.*
import org.koin.android.ext.android.inject
import timber.log.Timber


class SecretCaveActivity : AppCompatActivity() {

    private val appStore: AppStore by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secret_cave)

        appStore.events(this) {
            val buttonAClicked = buttonA.clicks().map { SecretCaveEvents.LetteredEntered("A") }.share()
            val buttonBClicked = buttonB.clicks().map { SecretCaveEvents.LetteredEntered("B") }.share()
            val buttonCClicked = buttonC.clicks().map { SecretCaveEvents.LetteredEntered("C") }.share()

            // emits CodeEntered after every 6th letter
            val codeEntered =
                listOf(buttonAClicked, buttonBClicked, buttonCClicked)
                    .merge()
                    .map { it.letter }
                    .buffer(6)
                    .map { SecretCaveEvents.CodeEntered(it.joinToString(separator = "")) }

            val buttonClearClicked = buttonClear.clicks().map {
                //Not sure this is correct as I need a way to reset the current buffer value
                SecretCaveEvents.ClearLetters
            }.share()

            listOf(
                buttonAClicked,
                buttonBClicked,
                buttonCClicked,
                buttonClearClicked,
                codeEntered
            ).merge()
        }

        appStore.updates.connect(this) {
            val secretCaveState =  it.secretCaveState
            Timber.d("updating Ui: $secretCaveState")

            progressBar.isVisible = secretCaveState.loading
            codeState.isVisible = !secretCaveState.loading

            buttonA.isEnabled = !secretCaveState.loading
            buttonB.isEnabled = !secretCaveState.loading
            buttonC.isEnabled = !secretCaveState.loading
            buttonClear.isEnabled = !secretCaveState.loading

            enteredCode.text = secretCaveState.enteredCode

            if (secretCaveState.codeCorrect) {
                codeState.setImageResource(R.drawable.ic_check_circle_green_24dp)
            } else {
                codeState.setImageResource(R.drawable.ic_error_red_24dp)
            }
        }

        appStore.actions
            .ofType<CodeVerificationResult.Error>()
            .connect(this){
                 Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SecretCaveActivity::class.java)
        }
    }

}

