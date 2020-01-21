package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement

/**
 * Attaches views of associated [Node]s to a parentNode, and executes the associated [RoutingAction].
 *
 * The [Node]s are expected to be already added to the parentNode on a logical level.
 */
internal class ActivateAction<C : Parcelable>(
    private var item: Resolved<C>,
    private val params: ActionExecutionParams<C>
) : Action<C> {

    object Factory:
        ActionFactory {
        override fun <C : Parcelable> create(key: ConfigurationKey, params: ActionExecutionParams<C>): Action<C> {
            val item = params.resolver.invoke(key)
            return ActivateAction(item, params)
        }
    }

    private var canExecute: Boolean =
        false

    override var transitionElements: List<TransitionElement<C>> =
        emptyList()


    override var result: Resolved<C> =
        item

    override fun onBeforeTransition() {
        canExecute = when {
            // If there's no view available (i.e. globalActivationLevel == SLEEPING) we must not execute
            // routing actions or try to attach view. That will be done on next WakeUp. For now, let's
            // just mark the element to the same value.
            params.globalActivationLevel != ACTIVE -> false
            // Don't execute activation twice
            else -> true
        }

        if (canExecute) {
            val actionableNodes = item.nodes
                .filter { it.viewAttachMode == Node.AttachMode.PARENT && !it.node.isAttachedToView }

            actionableNodes.forEach {
                params.parentNode.createChildView(it.node)
                params.parentNode.attachChildView(it.node)
            }

            transitionElements = actionableNodes.mapNotNull {
                it.node.view?.let { ribView ->
                    TransitionElement(
                        configuration = item.configuration,
                        direction = TransitionDirection.Enter,
                        parentViewGroup = params.parentNode.targetViewGroupForChild(it.node),
                        identifier = it.node.identifier,
                        view = ribView.androidView
                    )
                }
            }
        }
    }

    override fun onTransition() {
        if (canExecute) {
            item.routingAction.execute()
        }
    }

    override fun onPostTransition() {
    }

    // TODO check if can be merged with [CappedLifecycle], as this one has the same conceptual effect
    override fun onFinish() {
        result = result.copy(activationState = params.globalActivationLevel)
    }
}
