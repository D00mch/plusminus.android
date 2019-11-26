package com.livermor.plusminus.screen


interface IMsg
interface IModel

/**
 * Takes [IMsg] and [IModel] and return new [IModel]
 */
interface EventHandler<MSG : IMsg, MODEL : IModel> {
    operator fun invoke(msg: MSG, model: MODEL): MODEL
}

/**
 * Takes [IMsg] and previous [IModel] and produce new [IMsg]
 */
interface EffectHandler<MSG : IMsg, MODEL : IModel, DISPATCHER : IDispatcher<in MSG>> {
    operator fun invoke(msg: IMsg, model: MODEL, dispatcher: DISPATCHER)
}

/**
 * Takes [IMsg], update the state
 */
interface IDispatcher<T : IMsg> {
    fun dispatch(msg: T)
}
