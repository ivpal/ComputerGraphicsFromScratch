package com.github.ivpal.raytracer

import kotlin.math.sqrt

class Sphere(
    val center: Vector3,
    val radius: Float,
    val color: Color,
    val specular: Int
) {
    fun intersectRay(ray: Vector3): Pair<Float, Float> {
        val toCenter = Vector3.ORIGIN - center
        val a = ray.dot(ray)
        val b = 2 * toCenter.dot(ray)
        val c = toCenter.dot(toCenter) - radius * radius
        val discriminant = b * b - 4 * a * c
        if (discriminant < 0) {
            return Float.NEGATIVE_INFINITY to Float.NEGATIVE_INFINITY
        }

        val t1 = (-b + sqrt(discriminant)) / 2 * a
        val t2 = (-b - sqrt(discriminant)) / 2 * a
        return t1 to t2
    }
}
