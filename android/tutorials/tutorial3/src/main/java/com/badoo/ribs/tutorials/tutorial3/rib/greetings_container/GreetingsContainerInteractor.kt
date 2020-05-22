package com.badoo.ribs.tutorials.tutorial3.rib.greetings_container

import com.badoo.ribs.core.BackStackInteractor
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainerRouter.Configuration

class GreetingsContainerInteractor(
    buildParams: BuildParams<Nothing?>
) : BackStackInteractor<GreetingsContainer, Nothing, Configuration>(
    buildParams = buildParams,
    initialConfiguration = Configuration.Default
) {

}
