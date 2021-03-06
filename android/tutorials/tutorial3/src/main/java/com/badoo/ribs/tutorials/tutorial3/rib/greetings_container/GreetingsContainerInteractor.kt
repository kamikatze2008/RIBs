package com.badoo.ribs.tutorials.tutorial3.rib.greetings_container

import android.os.Bundle
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainerRouter.Configuration
import io.reactivex.functions.Consumer

class GreetingsContainerInteractor(
    savedInstanceState: Bundle?,
    private val router: Router<Configuration, Nothing, Configuration, Nothing, Nothing>,
    output: Consumer<GreetingsContainer.Output>
) : Interactor<Nothing>(
    savedInstanceState = savedInstanceState,
    disposables = null
) {

}
