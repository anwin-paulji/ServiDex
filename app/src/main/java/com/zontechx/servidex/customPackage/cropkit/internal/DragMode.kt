package com.zontechx.servidex.customPackage.cropkit.internal

internal sealed interface DragMode {

    data object None : DragMode
    data object Move : DragMode
    data class Handle(val handle: DragHandle) : DragMode

}