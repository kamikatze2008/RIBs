package com.badoo.ribs.tutorials.tutorial2.rib.greetings_container

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflateOnDemand
import com.badoo.ribs.tutorials.tutorial2.R
import io.reactivex.functions.Consumer

interface GreetingsContainer : Rib {

    interface Dependency {
        fun greetingsContainerOutput(): Consumer<Output>
    }

    sealed class Output {
        data class GreetingsSaid(val greeting: String) : Output()
    }

    class Customisation(
        val viewFactory: ViewFactory<GreetingsContainerView> = inflateOnDemand(
            R.layout.rib_greetings_container
        )
    )
}
