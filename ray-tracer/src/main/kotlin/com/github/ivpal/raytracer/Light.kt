package com.github.ivpal.raytracer

abstract class Light(protected val intensity: Float) {
    protected abstract fun computeDirection(point: Vector3): Vector3

    open fun computeLighting(point: Vector3, normal: Vector3): Float {
        val l = computeDirection(point)
        val nDotL = normal.dot(l)
        if (nDotL > 0) {
            return intensity * nDotL / (normal.length * l.length)
        }
        return 0.0f
    }
}

class AmbientLight(intensity: Float): Light(intensity) {
    override fun computeDirection(point: Vector3): Vector3 {
        throw UnsupportedOperationException()
    }

    override fun computeLighting(point: Vector3, normal: Vector3): Float = intensity
}

class PointLight(intensity: Float, private val position: Vector3): Light(intensity) {
    override fun computeDirection(point: Vector3): Vector3 {
        return position - point
    }
}

class DirectionLight(intensity: Float, private val direction: Vector3, ): Light(intensity) {
    override fun computeDirection(point: Vector3): Vector3 = direction
}
