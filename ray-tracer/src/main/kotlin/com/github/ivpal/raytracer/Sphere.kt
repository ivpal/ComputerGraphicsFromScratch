package com.github.ivpal.raytracer

import kotlin.math.sqrt

class Sphere(
    val center: Vector3,
    val radius: Float,
    val color: Color,
    val specular: Int,
    val reflective: Float
) {
    fun intersectRay(o: Vector3, ray: Vector3): Pair<Float, Float> {
        val toCenter = o - center
        val a = dot(ray, ray)
        val b = 2 * dot(toCenter, ray)
        val c = dot(toCenter, toCenter) - radius * radius
        val discriminant = b * b - 4 * a * c
        if (discriminant < 0) {
            return Float.NEGATIVE_INFINITY to Float.NEGATIVE_INFINITY
        }

        val t1 = (-b + sqrt(discriminant)) / 2 * a
        val t2 = (-b - sqrt(discriminant)) / 2 * a
        return t1 to t2
    }
}
