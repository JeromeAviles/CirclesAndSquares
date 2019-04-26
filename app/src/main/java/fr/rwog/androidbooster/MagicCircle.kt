package fr.rwog.androidbooster

import android.graphics.Color
import kotlin.random.Random

data class MagicCircle(val maxX:Float, val maxY:Float) {
    var delta:Int
    var cx:Float
    var cy:Float
    var mColor:Int
    var dx:Int
    var dy:Int
    var size:Int

    companion object {
        var NB_CIRCLES = 0
    }

    init {
        NB_CIRCLES++
        println("==> "+NB_CIRCLES)
        mColor =  Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        cx = Random.nextFloat()*maxX
        cy = Random.nextFloat()*maxY
        delta = Random.nextInt(CustomView.DELTA*10)+1
        dx = delta
        dy = delta
        size = Random.nextInt(150)
    }

    fun move() {
        when {
            cy !in 0F..maxY && cx !in 0F..maxX -> {
                dx = -dx
                dy = -dy
            }
            cx !in 0F..maxX -> dx = -dx
            cy !in 0F..maxY -> dy = -dy

        }
        cx += dx
        cy += dy
    }

    fun gather(fingerX:Float, fingerY:Float) {
        cx = fingerX
        cy = fingerY
        dx = 0
        dy = 0
    }

    fun escape() {
        dx = Random.nextInt(delta + 1 +delta) -delta
        dy = Random.nextInt(delta + 1 +delta) -delta
    }
}