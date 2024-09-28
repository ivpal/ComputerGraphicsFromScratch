package com.github.ivpal.raytracer

data class Color(val r: Int, val g: Int, val b: Int) {
    operator fun times(f: Float): Color =
        Color(
            (r * f).toInt().coerceIn(0, 255),
            (g * f).toInt().coerceIn(0, 255),
            (b * f).toInt().coerceIn(0, 255),
        )

    companion object {
        val WHITE = Color(255, 255, 255)
        val RED = Color(255, 0, 0)
        val GREEN = Color(0, 255, 0)
        val BLUE = Color(0, 0, 255)
    }
}
