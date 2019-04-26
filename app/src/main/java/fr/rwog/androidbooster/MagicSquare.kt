package fr.rwog.androidbooster

import android.graphics.Color
import kotlin.random.Random

data class MagicSquare(val maxX:Float, val maxY:Float)  {
    var delta:Int

    var mColor:Int
    var size:Int
    var dx:Int
    var dy:Int

    var ct:Float
    var cl:Float
    var cr:Float
    var cb:Float

    companion object {
        var NB_SQUARES = 0
    }

    init {
        NB_SQUARES++
        println("==> "+NB_SQUARES)
        mColor =  Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        size = Random.nextInt(150)
        ct = Random.nextFloat()*maxY
        cl = Random.nextFloat()*maxX
        cb = ct+size
        cr = cl+size
        delta = Random.nextInt(CustomView.DELTA*10)+1
        dx = delta
        dy = delta

    }

    fun move() {
        when {
            ct !in 0F..maxY-size && cl !in 0F..maxX-size -> {
                dx = -dx
                dy = -dy
            }

            ct !in 0F..maxY-size -> dy = -dy
            cl !in 0F..maxX-size -> dx = -dx

        }
        ct += dy
        cl += dx
        cb = ct+size
        cr = cl+size
    }

    fun gather(fingerX:Float, fingerY:Float) {
        ct = fingerY
        cl = fingerX
        cr = fingerX+size
        cb = fingerY+size
        dx = 0
        dy = 0
    }

    fun escape() {
        dx = Random.nextInt(delta + 1 +delta) -delta
        dy = Random.nextInt(delta + 1 +delta) -delta
    }
}