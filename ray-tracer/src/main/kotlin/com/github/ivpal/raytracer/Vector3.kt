package com.github.ivpal.raytracer

import kotlin.math.sqrt

class Vector3(val x: Float, val y: Float, val z: Float) {
    val length: Float
        get() = sqrt(x * x + y * y + z * z)

    fun normalize(): Vector3 {
        return this / length
    }

    operator fun plus(v: Vector3): Vector3 = Vector3(x + v.x, y + v.y, z + v.z)

    operator fun minus(v: Vector3): Vector3 = Vector3(x - v.x, y - v.y, z - v.z)

    operator fun times(f: Float): Vector3 = Vector3(x * f, y * f, z * f)

    operator fun div(f: Float): Vector3 = Vector3(x / f, y / f, z / f)

    operator fun unaryMinus(): Vector3 = Vector3(-x, -y, -z)

    companion object {
        val ZERO = Vector3(0.0f, 0.0f, 0.0f)
    }
}

fun dot(u: Vector3, v: Vector3): Float = u.x * v.x + u.y * v.y + u.z * v.z

fun unitVector(v: Vector3): Vector3 = v / v.length
