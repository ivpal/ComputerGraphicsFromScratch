package com.github.ivpal.raytracer

import java.io.PrintWriter
import kotlin.math.pow

const val canvasWidth = 640
const val canvasHeight = 640
const val viewportWidth = 1
const val viewportHeight = 1
const val projectionPlaneDistance = 1.0f

val scene = listOf(
    Sphere(
        center = Vector3(0.0f, -1.0f, 3.0f),
        radius = 1.0f,
        color = Color.RED,
        specular = 500
    ),
    Sphere(
        center = Vector3(2.0f, 0.0f, 4.0f),
        radius = 1.0f,
        color = Color.BLUE,
        specular = 500
    ),
    Sphere(
        center = Vector3(-2.0f, 0.0f, 4.0f),
        radius = 1.0f,
        color = Color.GREEN,
        specular = 10
    ),
    Sphere(
        center = Vector3(0.0f, -5001.0f, 0.0f),
        radius = 5000.0f,
        color = Color(255, 255, 0),
        specular = 1000
    )
)

val lights = listOf(
    Light.AmbientLight(0.2f),
    Light.PointLight(Vector3(2.0f, 1.0f, 0.0f), 0.6f),
    Light.DirectionLight(Vector3(1.0f, 4.0f, 4.0f), 0.2f)
)

fun main() {
    val w = canvasWidth / 2
    val h = canvasHeight / 2

    val builder = StringBuilder("P3\n${canvasWidth} ${canvasHeight}\n255\n")
    for (y in (h - 1) downTo -h) {
        for (x in (w - 1) downTo -w) {
            val ray = canvasToViewport(x, y)
            val color = traceRay(ray, 1.0f, Float.POSITIVE_INFINITY)
            builder.append("${color.r} ${color.g} ${color.b}\n")
        }
    }

    PrintWriter("image.ppm").use { pw ->
        pw.write(builder.toString())
    }
}

fun canvasToViewport(x: Int, y: Int): Vector3 =
    Vector3(
        1.0f * x * viewportWidth / canvasWidth,
        1.0f * y * viewportHeight / canvasHeight,
        projectionPlaneDistance
    )

fun computeLighting(point: Vector3, normal: Vector3, v: Vector3, s: Int): Float {
    var intensity = 0.0f
    for (light in lights) {
        if (light is Light.AmbientLight) {
            intensity += light.intensity
        } else {
           var l = Vector3.ORIGIN
            if (light is Light.PointLight) {
                l = light.position - point
            }

            if (light is Light.DirectionLight) {
                l = light.direction
            }

            // Diffuse
            val nDotL = normal.dot(l)
            if (nDotL > 0) {
                intensity += light.intensity * nDotL / (normal.length * l.length)
            }

            // Specular
            if (s != -1) {
                val r = normal * 2.0f * normal.dot(l) - l
                val rDotV = r.dot(v)
                if (rDotV > 0) {
                    intensity += light.intensity * (rDotV / (r.length * v.length)).pow(s)
                }
            }
        }
    }
    return intensity
}

fun traceRay(ray: Vector3, tMin: Float, tMax: Float): Color {
    var closestT = Float.POSITIVE_INFINITY
    var closestSphere: Sphere? = null
    for (sphere in scene) {
        val (t1, t2) = sphere.intersectRay(ray)
        if (t1 in tMin..tMax && t1 < closestT) {
            closestT = t1
            closestSphere = sphere
        }
        if (t2 in tMin..tMax && t2 < closestT) {
            closestT = t2
            closestSphere = sphere
        }
    }

    if (closestSphere == null) {
        return Color.WHITE
    }

    val point = Vector3.ORIGIN + ray * closestT
    val normal = point - closestSphere.center
    return closestSphere.color * computeLighting(point, normal.normalize(), -ray, closestSphere.specular)
}
